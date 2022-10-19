import java.util.*;
import java.sql.ResultSet;


/**
 * @author Shreyes, Robert, Alexia, Emma
 */
public class orderViewFunctions {
    serverViewFunctions serverFunctions;
    dbFunctions dbConnection;

    /**
     * Parametrized constructor
     * @param serverFunctions the server functions passed from the creater
     */
    public orderViewFunctions(serverViewFunctions serverFunctions) {
        this.serverFunctions = serverFunctions;
        dbConnection = new dbFunctions();
        dbConnection.createDbConnection();
    }

    /**
     * Generate the sales report information between two dates
     * @param startDate the start date for the sales report
     * @param endDate the end date for the sales report
     * @param useDefaultTime whether the time should include up to minutes accuracy or not
     * @return a vector of the sales report items which are between the two dates
     */
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

                //Create salesReportItem for later use
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

    /**
     * Generates a report for what items need to be bought(restocked)
     * @return a result set of the items, their desired quantity, current quantity, and helper information
     */
    public ResultSet generateRestockReport() {
        ResultSet results = null;
        try{ 
            dbConnection.createDbConnection();
            String sqlString = "SELECT id AS \"Id\", name AS \"Name\", quantityremaining AS \"Quantity Remaining\", quantitytarget AS \"Target Quantity\",";
            sqlString += "(quantitytarget - quantityremaining) AS \"Amount Under Target\", measurementunits AS \"Units\" FROM ingredients WHERE quantityremaining < quantitytarget AND quantityremaining > 0"; 
            results = dbConnection.dbQuery(sqlString);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
        return results;
    }//End generateRestockReport

    /**
     * Adds a seasonal item to our menu
     * @param newProduct the product to be added to the menu
     */
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

    /**
     * Generates a report on the excess inventory we are storing (Ingredients which aren't selling a lot)
     * @param startDate the start date for the excess report
     * @param useDefaultTime whether the time should be specific down to the minute
     * @return a vector of the items in our excess report
     */
    public Vector<excessReportItem> generateExcessReport(dateStruct startDate, boolean useDefaultTime) {
        Vector<excessReportItem> excessItems = new Vector<excessReportItem>(); 
        try{ 
            dbConnection.createDbConnection();
            String sqlString = "";
            
            for(Map.Entry<Integer, ingredient> ingred : serverFunctions.getIngredients().entrySet()) { 
                int totalAmountUsed = 0;
                boolean hasProducts = false;

                //Get all product ids that have the ingredient
                sqlString = String.format("SELECT productid FROM productstoingredients WHERE ingredientid = %d", ingred.getValue().getId());
                ResultSet productsIdResults = dbConnection.dbQuery(sqlString);
                String idList = "(";
                if(productsIdResults.next()) {
                    idList += String.format("%d", productsIdResults.getInt("productid"));
                    hasProducts = true;
                }
                while(productsIdResults.next()) {
                    idList += String.format(", %d", productsIdResults.getInt("productid"));
                }
                idList += ")";
                
                if(hasProducts) {
                    //sum(itemamount)
                    sqlString = "SELECT sum(itemamount) FROM orderitems INNER JOIN products ON products.name = orderitems.itemname ";
                    sqlString += "INNER JOIN ordertickets ON ordertickets.id = orderitems.orderid ";
                    sqlString += String.format("WHERE products.id IN %s AND timestamp BETWEEN ", idList);
                    if(useDefaultTime) {
                        sqlString += String.format("'%s %s' AND NOW()", startDate.formatString(), startDate.getStartOfDay());
                    }
                    else {
                        sqlString += String.format("'%s %s' AND NOW()", startDate.formatString(), startDate.getTimeOfDay());
                    }
                    
                    ResultSet itemAmountResults = dbConnection.dbQuery(sqlString);
                    int numTimes = 0;
                    if(itemAmountResults.next()) {
                        numTimes = itemAmountResults.getInt(1);
                    }
                    totalAmountUsed += numTimes;

                    //amount of times in additions
                    sqlString = String.format("SELECT COUNT(*) FROM orderitemadditions INNER JOIN ordertickets ON orderitemadditions.orderid = ordertickets.id WHERE ingredientid = %d AND timestamp BETWEEN ", ingred.getValue().getId());
                    if(useDefaultTime) {
                                sqlString += String.format("'%s %s' AND NOW()", startDate.formatString(), startDate.getStartOfDay());
                            }
                            else {
                                sqlString += String.format("'%s %s' AND NOW()", startDate.formatString(), startDate.getTimeOfDay());
                            }
                    ResultSet additionsResults = dbConnection.dbQuery(sqlString);
                    numTimes = 0;
                    if(additionsResults.next()) {
                        numTimes = additionsResults.getInt(1);
                    }
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
                    numTimes = 0;
                    if(subtractionsResults.next()) {
                        numTimes = subtractionsResults.getInt(1);
                    }
                    totalAmountUsed -= numTimes;
                }
                
                //Calculating if it's < 10%
                double percentUsed = totalAmountUsed / (totalAmountUsed + ingred.getValue().getQuantityRemaining());
                if(percentUsed < 0.1) {
                    excessReportItem temp_Item = new excessReportItem(ingred.getValue().getId(), ingred.getValue().getName(), ingred.getValue().getQuantityRemaining(), totalAmountUsed, totalAmountUsed + ingred.getValue().getQuantityRemaining());
                    excessItems.add(temp_Item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 

        return excessItems;
    }//End generateExcessReport

}