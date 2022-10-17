import java.util.Vector;
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

    public ResultSet generateSalesReportBetweenDates(dateStruct startDate, dateStruct endDate, boolean useDefaultTime) {
        ResultSet results = null;
        try{ 
            dbConnection.createDbConnection();

            String sqlString = "SELECT orderitems.orderid AS \"Order Id\", orderitems.itemnumberinorder AS \"Item Number In Order\", itemname AS \"Item Name\", ";
            sqlString += "itemamount AS \"Quantity\", itemsize AS \"Size\", ordertickets.timestamp AS \"Timestamp\" FROM orderitems INNER JOIN ordertickets ON orderitems.orderid = ordertickets.id "; 
            
            if(useDefaultTime) {
                sqlString += String.format("WHERE ordertickets.timestamp BETWEEN '%s %s' AND '%s %s'", startDate.formatString(), startDate.getStartOfDay(), endDate.formatString(), endDate.getEndOfDay());
            }
            else {
                sqlString += String.format("WHERE ordertickets.timestamp BETWEEN '%s %s' AND '%s %s'", startDate.formatString(), startDate.getTimeOfDay(), endDate.formatString(), endDate.getTimeOfDay());
            }
            System.out.println(sqlString);
            results = dbConnection.dbQuery(sqlString);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
        return results;
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

}