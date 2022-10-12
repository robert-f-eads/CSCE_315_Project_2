import java.sql.*;
import java.util.*;

public class serverViewFunctions {
    private
        Dictionary<Integer, ingredient> ingredients = new Hashtable<Integer, ingredient>();
        Dictionary<Integer, material> materials = new Hashtable<Integer, material>();
        Dictionary<Integer, product> products = new Hashtable<Integer, product>();
        //For product search bar use "SELECT id FROM products WHERE name ILIKE '%Search_string%'"

    public serverViewFunctions() {
        importIngredients();
        importMaterials();
        importProducts();
    }

    private void importMaterials() {
       try{
            dbFunctions dbConnection = new dbFunctions();
            dbConnection.createDbConnection();
            String sqlStatement = "SELECT * FROM materials";
            ResultSet result = dbConnection.dbQuery(sqlStatement);
            
            //Import all ingredients
            while(result.next()) {
                /*
                    int id;
                    String name;
                    String size;
                    int quantityRemaining;
                    String measurementUnits;
                    int itemsPerUnit;
                    double pricePerUnitLastOrder;
                    String lastOrderDate;
                    double unitsInLastOrder;
                */
                material temp_material = new material(result.getInt(1), result.getString(2), result.getString(3), result.getInt(4), 
                result.getString(5), result.getInt(6), result.getDouble(7), result.getString(8), result.getDouble(9));
                materials.put(temp_material.getId(), temp_material);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
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

    public void updateDbWithOrder(orderTicketInfo new_ticket) {
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

                /*
                int id = item.getId();
                product temp_product = products.get(id);
                for(orderItemModification modification : item.getAdditions()) {
                    ingredient temp_ingredient = ingredients.get(modification.getId());
                    
                }
                for(orderItemModification modification : item.getSubtractions()) {

                }
                
                for(ingredient ingred : temp_product.ingredients()) {

                }

                ingerigent number - 1


                */
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        
    }//Close updateDbWithOrder

    
    public boolean isAdmin(int employeeId) {
        ResultSet result;
        boolean check = false;
        dbFunctions dbConnection = new dbFunctions();
        try{
            dbConnection.createDbConnection();
            String sqlStatement = String.format("SELECT isadmin FROM employees WHERE id = %d", employeeId);
            result = dbConnection.dbQuery(sqlStatement);
            
            //Check employee permissions
            while(result.next()) {
                check = result.getBoolean("isadmin");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return check;
    } 

    
    public String getEmployeeName(int employeeId) {
        ResultSet result;
        String name = "";
        dbFunctions dbConnection = new dbFunctions();
        try{
            dbConnection.createDbConnection();
            String sqlStatement = String.format("SELECT firstname, lastname FROM employees WHERE id = %d", employeeId);
            result = dbConnection.dbQuery(sqlStatement);
            
            //Check employee permissions
            while(result.next()) {
                name = result.getString("firstname") + " " + result.getString("lastname");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return name;
    }
    
    
    public 
        product getProduct(int id) {return products.get(id);}
        material getMaterial(int id) {return materials.get(id);}
        ingredient getIngredient(int id) {return ingredients.get(id);}
        



    
    

}