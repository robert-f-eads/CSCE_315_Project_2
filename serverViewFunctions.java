import java.sql.*;
import java.util.*;

public class serverViewFunctions {
    private
        Dictionary<Integer, ingredient> ingredients = new Hashtable<Integer, ingredient>();
        Dictionary<Integer, product> products = new Hashtable<Integer, product>();
        //For product search bar use "SELECT id FROM products WHERE name ILIKE '%Search_string%'"

    public serverViewFunctions() {
        importIngredients();
        importProducts();
    }

    private void importIngredients() {
        try{
            dbFunctions dbConnection = new dbFunctions();
            dbConnection.createDbConnection();
            String sqlStatement = "SELECT * FROM ingredients";
            ResultSet result = dbConnection.dbQuery(sqlStatement);
            
            //Import all ingredients
            while(result.next()) {
                ingredient temp_ingredient = new ingredient(result.getInt(1), result.getString(2), result.getString(3), result.getDouble(4), 
                result.getString(5), result.getDouble(6), result.getString(7), result.getDouble(8));
                ingredients.put(temp_ingredient.getId(), temp_ingredient);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }//Close importIngredients

    private void importProducts() {
        try{
            dbFunctions dbConnection = new dbFunctions();
            dbConnection.createDbConnection();
            String sqlStatement = "SELECT * FROM products";
            ResultSet result = dbConnection.dbQuery(sqlStatement);
            
            //Import all products
            while(result.next()) {
                product temp_product = new product(result.getInt(1), result.getString(2), result.getDouble(3));
                sqlStatement = String.format("SELECT ingredientid FROM productstoingredients WHERE productid = %d", temp_product.getId());
                ResultSet result_temp = dbConnection.dbQuery(sqlStatement);

                //Add ingredients
                while(result_temp.next()) {
                    temp_product.addIngredient(ingredients.get(result_temp.getInt(1)));
                }
                products.put(temp_product.getId(), temp_product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }//Close importProducts

    void updateDbWithOrder(orderTicketInfo new_ticket) {
        try{
            //Create database connection object
            dbFunctions dbConnection = new dbFunctions();
            dbConnection.createDbConnection();
            String sqlStatement;

            //Insert into ordertickets
            sqlStatement = "INSERT INTO ordertickets (timestamp, customerfirstname, rewardsmemberid, employeeid, orderpricetotal)";
            sqlStatement += String.format("VALUES ('%s', '%s', %d, %d, %.2f)", new_ticket.getTimestamp(), new_ticket.getCustomerFirstName(), 
                new_ticket.getRewardsMemberId(), new_ticket.getEmployeeId(), new_ticket.getOrderPriceTotal());
            int resultInt = dbConnection.dbUpsert(sqlStatement);

            //Retreive ticket id
            sqlStatement = "SELECT * FROM ordertickets ORDER BY id DESC LIMIT 1";
            ResultSet result = dbConnection.dbQuery(sqlStatement);
            result.next();
            int ticketId = result.getInt("id");


            //Insert into orderticketitems and additions/subtractions
            for(orderItem item : new_ticket.getOrderItems()) {
                //Insert order ticket item
                sqlStatement = "INSERT INTO orderitems (orderid, itemnumberinorder, itemname, itemamount, itemsize)";
                sqlStatement += String.format("VALUES (%d, %d, '%s', %d, %d)", ticketId, item.getItemNumberInOrder(), item.getItemName(), 
                    item.getItemAmount(), item.getItemSize());
                resultInt = dbConnection.dbUpsert(sqlStatement);
               
                //Retreive item id
                sqlStatement = "SELECT * FROM orderitems ORDER BY id DESC LIMIT 1";
                result = dbConnection.dbQuery(sqlStatement);
                result.next();
                int itemId = result.getInt("id");


                //Insert into additions 
                for(orderItemModification modification : item.getAdditions()) {
                    sqlStatement = "INSERT INTO orderitemadditions (orderid, itemnumberinorder, ingredientid)";
                    sqlStatement += String.format("VALUES (%d, %d, %d)", ticketId, itemId, modification.getIngredientId());
                    resultInt = dbConnection.dbUpsert(sqlStatement);
                }

                //Insert into subtractions
                for(orderItemModification modification : item.getSubtractions()) {
                    sqlStatement = "INSERT INTO orderitemsubtractions (orderid, itemnumberinorder, ingredientid)";
                    sqlStatement += String.format("VALUES (%d, %d, %d)", ticketId, itemId, modification.getIngredientId());
                    resultInt = dbConnection.dbUpsert(sqlStatement);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        
    }//Close updateDbWithOrder

    public 
        product getProduct(int id) {return products.get(id);}

    
    

}