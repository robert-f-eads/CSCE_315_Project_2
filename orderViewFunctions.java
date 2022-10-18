import java.util.*;
import java.sql.ResultSet;

/**
 * @author Shreyes, Robert, Alexia, Emma
 */
public class orderViewFunctions {
    serverViewFunctions serverFunctions;
    dbFunctions dbConnection;

    /**
     * 
     * @return order tickets as received from dbFunctions
     */
    public Vector<orderTicketInfo> getOrders() {
        Vector<orderTicketInfo> orderTickets = new Vector<>();
        try {
			dbConnection.createDbConnection();
			String sqlStatement = "SELECT * FROM orderTickets";
			ResultSet results = dbConnection.dbQuery(sqlStatement);
			while(results.next()) {
				orderTicketInfo oti = serverFunctions.getOrderTicket(results.getInt("id")); // serverFunctions.getIngredient(results.getInt("id")); 
                orderTickets.add(oti);
			}
		} catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
        return orderTickets;
    }

    public orderViewFunctions() {
        serverFunctions = new serverViewFunctions();
        dbConnection = new dbFunctions();
        dbConnection.createDbConnection();
    }

    public Vector<salesReportItem> generateSalesReportBetweenDates(dateStruct startDate, dateStruct endDate, boolean useDefaultTime) {
        Vector<salesReportItem> salesItems = new Vector<salesReportItem>();
        try{ 
            dbConnection.createDbConnection();

            for(Map.Entry<Integer, product> prodId : serverFunctions.getProducts().entrySet()) {
                //Get amount of items
                String sqlStatement = String.format("SELECT SUM(itemamount) FROM orderitems INNER JOIN ordertickets ON ordertickets.id = orderitems.orderid WHERE itemname = '%s' ", prodId.getValue().getName());
                sqlStatement += "AND timestamp BETWEEN ";
                        if(useDefaultTime) {
                            sqlStatement += String.format("'%s %s' AND '%s %s'", startDate.formatString(), startDate.getStartOfDay(), endDate.formatString(), endDate.getEndOfDay());
                        }
                        else {
                            sqlStatement += String.format("'%s %s' AND '%s %s'", startDate.formatString(), startDate.getTimeOfDay(), endDate.formatString(), endDate.getTimeOfDay());
                        }
                ResultSet temp_results = dbConnection.dbQuery(sqlStatement);
                temp_results.next();
                int total = temp_results.getInt(1);
                //SELECT SUM(itemamount) FROM orderitems INNER JOIN ordertickets ON ordertickets.id = orderitems.orderid WHERE itemname = 'keto-champ-coffee' AND timestamp BETWEEN '2022-09-08 00:00:00' AND '2022-09-13 00:00:00'
                salesReportItem temp_item = new salesReportItem(prodId.getValue().getId(), prodId.getValue().getName(), total, Double.parseDouble(String.format("%.2f", total * prodId.getValue().getPrice())));
                salesItems.add(temp_item);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
        return salesItems;
    }//End generateSalesReportBetweenDate

    public ResultSet generateRestockReport() {
        ResultSet results = null;
        try{ 
            dbConnection.createDbConnection();
            String sqlString = "SELECT id AS \"Id\", name AS \"Name\", quantityremaining AS \"Quantity Remaining\", quantitytarget AS \"Target Quantity\",";
            sqlString += "(quantitytarget - quantityremaining) AS \"Amount Under Target\", measurementunits AS \"Units\" FROM ingredients WHERE quantityremaining < quantitytarget"; 
            results = dbConnection.dbQuery(sqlString);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
        return results;
    }//End generateRestockReport

    public void addSeasonalItem(product newProduct) {
        try{ 
            dbConnection.createDbConnection();
            
            //Insert product
            String sqlString = String.format("INSERT INTO products (name, price) VALUES ('%s', %.2f)", newProduct.getName(), newProduct.getPrice());
            dbConnection.dbUpsert(sqlString);
            
            //Getting new product id
            sqlString = String.format("SELECT id FROM products WHERE name = '%s'", newProduct.getName());
            ResultSet results = dbConnection.dbQuery(sqlString);
            results.next();
            int productId = results.getInt(1);

            //Updating products-ingredients bridge table
            for(ingredient ingred : newProduct.ingredients()) {
                sqlString = String.format("INSERT INTO productstoingredients (productid, ingredientid) VALUES (%d, %d)", productId, ingred.getId());
                dbConnection.dbUpsert(sqlString);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
    }//End addSeasonalItem

    public Vector<excessReportItem> generateExcessReport(dateStruct startDate, boolean useDefaultTime) {
        //ResultSet finalResults = null;
        Vector<excessReportItem> excessItems = new Vector<excessReportItem>(); 
        try{ 
            dbConnection.createDbConnection();
            //Vector<ingredient> targetIngredient = new Vector<ingredient>();
            String sqlString = "";
            
            for(Map.Entry<Integer, ingredient> ingred : serverFunctions.getIngredients().entrySet()) { 
                int totalAmountUsed = 0;
                for(Map.Entry<Integer, product> prod : serverFunctions.getProducts().entrySet()) {
                    if(prod.getValue().ingredients().contains(ingred.getValue())) {
                        
                        //sum(itemamount)
                        sqlString = "SELECT sum(itemamount) FROM orderitems INNER JOIN products ON products.name = orderitems.itemname ";
                        sqlString += "INNER JOIN ordertickets ON ordertickets.id = orderitems.orderid ";
                        sqlString += String.format("WHERE products.id = %d AND timestamp BETWEEN ", prod.getValue().getId());
                        if(useDefaultTime) {
                            sqlString += String.format("'%s %s' AND NOW()", startDate.formatString(), startDate.getStartOfDay());
                        }
                        else {
                            sqlString += String.format("'%s %s' AND NOW()", startDate.formatString(), startDate.getTimeOfDay());
                        }
                        System.out.println(sqlString);

                        ResultSet itemAmountResults = dbConnection.dbQuery(sqlString);
                        itemAmountResults.next();
                        int numTimes = itemAmountResults.getInt(1);
                        totalAmountUsed += numTimes;
                    }
                }

                //amount of times in additions
                sqlString = String.format("SELECT COUNT(*) FROM orderitemadditions INNER JOIN ordertickets ON orderitemadditions.orderid = ordertickets.id WHERE ingredientid = %d AND timestamp BETWEEN ", ingred.getValue().getId());
                if(useDefaultTime) {
                            sqlString += String.format("'%s %s' AND NOW()", startDate.formatString(), startDate.getStartOfDay());
                        }
                        else {
                            sqlString += String.format("'%s %s' AND NOW()", startDate.formatString(), startDate.getTimeOfDay());
                        }
                ResultSet additionsResults = dbConnection.dbQuery(sqlString);
                additionsResults.next();
                int numTimes = additionsResults.getInt(1);
                totalAmountUsed += numTimes;

                //amount of times in subtractions
                sqlString = String.format("SELECT COUNT(*) FROM orderitemsubtractions INNER JOIN ordertickets ON orderitemsubtractions.orderid = ordertickets.id WHERE ingredientid = %d AND timestamp BETWEEN ", ingred.getValue().getId());
                if(useDefaultTime) {
                            sqlString += String.format("'%s %s' AND NOW()", startDate.formatString(), startDate.getStartOfDay());
                        }
                        else {
                            sqlString += String.format("'%s %s' AND NOW()", startDate.formatString(), startDate.getTimeOfDay());
                        }
                ResultSet subtractionsResults = dbConnection.dbQuery(sqlString);
                subtractionsResults.next();
                numTimes = subtractionsResults.getInt(1);
                totalAmountUsed -= numTimes;


                //Calculating if it's < 10%
                double percentUsed = totalAmountUsed / (totalAmountUsed + ingred.getValue().getQuantityRemaining());
                if(percentUsed < 0.1) {
                    excessReportItem temp_Item = new excessReportItem(ingred.getValue().getId(), ingred.getValue().getName(), ingred.getValue().getQuantityRemaining(), totalAmountUsed, totalAmountUsed + ingred.getValue().getQuantityRemaining());
                    excessItems.add(temp_Item);
                }

            }

            /*//Get ingredients that have < 10%
            String idList = "(";
            int i = 0;
            for(ingredient temp : targetIngredient) {
                if(i == targetIngredient.size()-1) {idList += String.format("%d", temp.getId());}
                else {idList += String.format("%d, ", temp.getId());}
                i++;
            }
            idList += ")";

            sqlString = String.format("SELECT id AS \"Id\", name AS \"Name\", quantityremaining AS \"Quantity Remaining\" FROM ingredients WHERE id IN %s", idList);
            finalResults = dbConnection.dbQuery(sqlString);*/

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 

        return excessItems;
    }//End generateExcessReport

}