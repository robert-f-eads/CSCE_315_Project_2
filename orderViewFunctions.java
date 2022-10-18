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
                String sqlStatement = String.format("SELECT SUM(itemamount) FROM orderitems WHERE itemname = '%s'", prodId.getValue().getName());
                ResultSet temp_results = dbConnection.dbQuery(sqlStatement);
                temp_results.next();
                int total = temp_results.getInt(1);
                
                salesReportItem temp_item = new salesReportItem(prodId.getValue().getId(), prodId.getValue().getName(), total, total * prodId.getValue().getPrice());
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

    public ResultSet generateExcessReport(dateStruct startDate, boolean useDefaultTime) {
        ResultSet finalResults = null;
        try{ 
            dbConnection.createDbConnection();
            Vector<ingredient> targetIngredient = new Vector<ingredient>();
            
            //Get ingredients
            String sqlString = String.format("SELECT id FROM ingredients");
            ResultSet ingredsResults = dbConnection.dbQuery(sqlString);
            while(ingredsResults.next()) {
                ingredient temp_ingred = serverFunctions.getIngredient(ingredsResults.getInt("id"));
                int totalAmountUsed = 0;

                //Get amount of times in products
                sqlString = String.format("SELECT id FROM products");
                ResultSet productResults = dbConnection.dbQuery(sqlString);
                while(productResults.next()) {

                    //Runs count if ingredient in product
                    product temp_product = serverFunctions.getProduct(productResults.getInt("id"));
                    if(temp_product.ingredients().indexOf(temp_ingred) != -1) {

                        //sum(itemamount)
                        sqlString = "SELECT sum(itemamount) FROM orderitems INNER JOIN products ON products.name = orderitems.itemname ";
                        sqlString += "INNER JOIN ordertickets ON ordertickets.id = orderitems.orderid ";
                        sqlString += String.format("WHERE products.id = %d AND timestamp BETWEEN ", temp_product.getId());
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
                sqlString = String.format("SELECT COUNT(*) FROM orderitemadditions WHERE ingredientid = %d", temp_ingred.getId());
                ResultSet additionsResults = dbConnection.dbQuery(sqlString);
                additionsResults.next();
                int numTimes = additionsResults.getInt(1);
                totalAmountUsed += numTimes;

                //amount of times in subtractions
                sqlString = String.format("SELECT COUNT(*) FROM orderitemsubtractions WHERE ingredientid = %d", temp_ingred.getId());
                ResultSet subtractionsResults = dbConnection.dbQuery(sqlString);
                subtractionsResults.next();
                numTimes = subtractionsResults.getInt(1);
                totalAmountUsed -= numTimes;


                //Calculating if it's < 10%
                double percentUsed = totalAmountUsed / (totalAmountUsed + temp_ingred.getQuantityRemaining());
                if(percentUsed < 0.1) {targetIngredient.add(temp_ingred);}
            }

            //Get ingredients that have < 10%
            String idList = "(";
            int i = 0;
            for(ingredient temp : targetIngredient) {
                if(i == targetIngredient.size()-1) {
                    idList += String.format("%d", temp.getId());
                }
                idList += String.format("%d, ", temp.getId());
                i++;
            }
            idList += ")";

            sqlString = String.format("SELECT id AS \"Id\", name AS \"Name\", quantityremaining AS \"Quantity Remaining\" FROM ingredients WHERE id IN %s", idList);
            finalResults = dbConnection.dbQuery(sqlString);



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 

        return finalResults;
    }//End generateExcessReport

}