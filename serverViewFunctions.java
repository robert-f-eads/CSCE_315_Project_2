import java.sql.*;
import java.util.*;

public class serverViewFunctions {
    private
        Dictionary<Integer, ingredient> ingredients = new Hashtable<Integer, ingredient>();
        Dictionary<Integer, material> materials = new Hashtable<Integer, material>();
        Dictionary<Integer, product> products = new Hashtable<Integer, product>();
        Dictionary<Integer, orderTicketInfo> orderTickets = new Hashtable<Integer, orderTicketInfo>();
        //For product search bar use "SELECT id FROM products WHERE name ILIKE '%Search_string%'"

    public serverViewFunctions() {
        importIngredients();
        importMaterials();
        importProducts();
        importOrderTickets();
    }

    private void importOrderTickets() {
       try{
            dbFunctions dbConnection = new dbFunctions();
            dbConnection.createDbConnection();
            // TODO see if sql has get first ten, then next ten, and page it
            String sqlStatement = "SELECT * FROM orderTickets LIMIT 10";
            ResultSet result = dbConnection.dbQuery(sqlStatement);
            
            //Import all ingredients
            while(result.next()) {
                /*
                    int id;
                    String timestamp;
                    String customerFirstName;
                    int rewardsMemberId;
                    int employeeId;
                    double orderPriceTotal; 
                    Vector<orderItem> items;
                */
                orderTicketInfo oti = new orderTicketInfo(result.getInt(1), result.getString(2), result.getString(3), result.getInt(4), 
                    result.getInt(5), result.getDouble(6));

                sqlStatement = "SELECT * FROM orderItems WHERE orderId=" + oti.getId();
                ResultSet itemResult = dbConnection.dbQuery(sqlStatement);
                while(itemResult.next()) {
                    /*
                    int id;
                    int orderId;
                    int itemNumberInOrder;
                    String itemName;
                    int itemAmount;
                    int itemSize;
                    Vector<orderItemModification> additions;
                    Vector<orderItemModification> subtractions;
                    */
                    orderItem oi = new orderItem(itemResult.getInt(1), itemResult.getInt(2), itemResult.getInt(3), itemResult.getString(4), itemResult.getInt(5), itemResult.getInt(6));
                    sqlStatement = "SELECT * FROM orderItemAdditions WHERE orderId=" + oti.getId() + " AND itemNumberInOrder=" + oi.getItemNumberInOrder();
                    ResultSet additionResult = dbConnection.dbQuery(sqlStatement);
                    while(additionResult.next()) {
                        /*
                        int id;
                        int orderId;
                        int itemNumberInOrder;
                        int ingredientId;
                        String ingredientName = "";
                        */ 
                        orderItemModification a = new orderItemModification(additionResult.getInt(1), additionResult.getInt(2), additionResult.getInt(3), additionResult.getInt(4), additionResult.getString(4));
                        oi.addAddition(a);
                    }

                    sqlStatement = "SELECT * FROM orderItemSubtractions WHERE orderId=" + oti.getId() + " AND itemNumberInOrder=" + oi.getItemNumberInOrder();
                    ResultSet subtractionResult = dbConnection.dbQuery(sqlStatement);
                    while(subtractionResult.next()) {
                        /*
                        int id;
                        int orderId;
                        int itemNumberInOrder;
                        int ingredientId;
                        String ingredientName = "";
                        */ 
                        orderItemModification s = new orderItemModification(subtractionResult.getInt(1), subtractionResult.getInt(2), subtractionResult.getInt(3), subtractionResult.getInt(4), subtractionResult.getString(4));
                        oi.addSubtraction(s);
                    }
                    oti.addItemToOrder(oi);
                }
                orderTickets.put(oti.getId(), oti);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
    }

    // private void importIngredients() {
    //     try{
    //         dbFunctions dbConnection = new dbFunctions();
    //         dbConnection.createDbConnection();
    //         String sqlStatement = "SELECT * FROM ingredients";
    //         ResultSet result = dbConnection.dbQuery(sqlStatement);
            
    //         //Import all ingredients
    //         while(result.next()) {
    //             ingredient temp_ingredient = new ingredient(result.getInt(1), result.getString(2), result.getString(3), result.getDouble(4), 
    //             result.getString(5), result.getDouble(6), result.getString(7), result.getDouble(8));
    //             ingredients.put(temp_ingredient.getId(), temp_ingredient);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         System.err.println(e.getClass().getName()+": "+e.getMessage());
    //         System.exit(0);
    //     }
    // }//Close importIngredients


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


                //Update ingredient counts
                int id = item.getId();
                product temp_product = products.get(id);
                Vector<ingredient> temp_ingreds = new Vector<ingredient>();

                //Adds default
                for(ingredient ingred : temp_product.ingredients()) {temp_ingreds.add(ingred);}

                //Adds additions
                for(orderItemModification modification : item.getAdditions()) {
                    ingredient temp_ingredient = ingredients.get(modification.getId());
                    temp_ingreds.add(temp_ingredient);
                }
                
                //Removes subtractions
                for(orderItemModification modification : item.getSubtractions()) {
                    ingredient temp_ingredient = ingredients.get(modification.getId());
                    temp_ingreds.remove(temp_ingredient);
                }

                //Decrement the ingredient quanitity
                for(ingredient temp_ingredient : temp_ingreds) {
                    sqlStatement = String.format("SELECT quantityremaining FROM ingredients WHERE id = %d", temp_ingredient.getId());
                    result = dbConnection.dbQuery(sqlStatement);
                    int quantRemaining = 0;
                    while(result.next()){quantRemaining = result.getInt(1);}
                    quantRemaining -= 1;
                    sqlStatement = String.format("UPDATE ingredients SET quantityremaining = %.2f WHERE id = %d", quantRemaining, temp_ingredient.getId());
                    resultInt = dbConnection.dbUpsert(sqlStatement);
                }
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
        orderTicketInfo getOrderTicket(int id) {return orderTickets.get(id);}



    
    

}