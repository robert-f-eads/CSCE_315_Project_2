import java.sql.*;
import java.util.*;

public class serverViewFunctions {
    private
        Dictionary<Integer, ingredient> ingredients = new Hashtable<Integer, ingredient>();
        Dictionary<Integer, material> materials = new Hashtable<Integer, material>();
        Dictionary<Integer, product> products = new Hashtable<Integer, product>();
        Dictionary<Integer, orderTicketInfo> orderTickets = new Hashtable<Integer, orderTicketInfo>();

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
                 orderTicketInfo oti = new orderTicketInfo(result.getInt(1), result.getString(2), result.getString(3), result.getInt(4), 
                     result.getInt(5), result.getDouble(6));
                 sqlStatement = "SELECT * FROM orderItems WHERE orderId=" + oti.getId();
                 ResultSet itemResult = dbConnection.dbQuery(sqlStatement);
                 while(itemResult.next()) {
                     orderItem oi = new orderItem(itemResult.getInt(1), itemResult.getInt(2), itemResult.getInt(3), itemResult.getString(4), itemResult.getInt(5), itemResult.getInt(6));
                     sqlStatement = "SELECT * FROM orderItemAdditions WHERE orderId=" + oti.getId() + " AND itemNumberInOrder=" + oi.getItemNumberInOrder();
                     ResultSet additionResult = dbConnection.dbQuery(sqlStatement);
                     while(additionResult.next()) {
                         orderItemModification a = new orderItemModification(additionResult.getInt(1), additionResult.getInt(2), additionResult.getInt(3), additionResult.getInt(4), additionResult.getString(4));
                         oi.addAddition(a);
                     }
                     sqlStatement = "SELECT * FROM orderItemSubtractions WHERE orderId=" + oti.getId() + " AND itemNumberInOrder=" + oi.getItemNumberInOrder();
                     ResultSet subtractionResult = dbConnection.dbQuery(sqlStatement);
                     while(subtractionResult.next()) {
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

    private void importMaterials() {
       try{
            dbFunctions dbConnection = new dbFunctions();
            dbConnection.createDbConnection();
            String sqlStatement = "SELECT * FROM materials";
            ResultSet result = dbConnection.dbQuery(sqlStatement);
            
            //Import all ingredients
            while(result.next()) {
                material temp_material = new material(result.getInt(1), result.getString(2), result.getString(3), result.getInt(4), 
                result.getString(5), result.getInt(6), result.getDouble(7), result.getString(8), result.getDouble(9));
                materials.put(temp_material.getId(), temp_material);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
    }//End importMaterials

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
    }//End importIngredients

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
    }//End importProducts

    public void updateDbWithOrder(orderTicketInfo newTicket) {
        try{
            
            //Calculate order total
            newTicket.setOrderPriceTotal(getCurrentOrderTotal(newTicket));

            //Create database connection object
            dbFunctions dbConnection = new dbFunctions();
            dbConnection.createDbConnection();
            String sqlStatement;
            
            //Insert into ordertickets
            sqlStatement = "INSERT INTO ordertickets (timestamp, customerfirstname, rewardsmemberid, employeeid, orderpricetotal)";
            sqlStatement += String.format("VALUES ('%s', '%s', %d, %d, %.2f)", newTicket.getTimestamp(), newTicket.getCustomerFirstName(), 
                newTicket.getRewardsMemberId(), newTicket.getEmployeeId(), newTicket.getOrderPriceTotal());
            int resultInt = dbConnection.dbUpsert(sqlStatement);
            
            //Retreive ticket id
            sqlStatement = "SELECT * FROM ordertickets ORDER BY id DESC LIMIT 1";
            ResultSet result = dbConnection.dbQuery(sqlStatement);
            result.next();
            int ticketId = result.getInt("id");


            //Insert into orderticketitems and additions/subtractions
            for(orderItem item : newTicket.getOrderItems()) {
                //Insert order ticket item
                sqlStatement = "INSERT INTO orderitems (orderid, itemnumberinorder, itemname, itemamount, itemsize)";
                sqlStatement += String.format("VALUES (%d, %d, '%s', %d, %d)", ticketId, item.getItemNumberInOrder(), item.getItemName(), 
                    item.getItemAmount(), item.getItemSize());
                resultInt = dbConnection.dbUpsert(sqlStatement);
               
                //Retreive item id
                sqlStatement = "SELECT * FROM orderitems ORDER BY id DESC LIMIT 1";
                result = dbConnection.dbQuery(sqlStatement);
                result.next();
                int itemNum = result.getInt("itemnumberinorder");


                //Insert into additions 
                for(orderItemModification modification : item.getAdditions()) {
                    sqlStatement = "INSERT INTO orderitemadditions (orderid, itemnumberinorder, ingredientid)";
                    sqlStatement += String.format("VALUES (%d, %d, %d)", ticketId, itemNum, modification.getIngredientId());
                    resultInt = dbConnection.dbUpsert(sqlStatement);
                }

                //Insert into subtractions
                for(orderItemModification modification : item.getSubtractions()) {
                    sqlStatement = "INSERT INTO orderitemsubtractions (orderid, itemnumberinorder, ingredientid)";
                    sqlStatement += String.format("VALUES (%d, %d, %d)", ticketId, itemNum, modification.getIngredientId());
                    resultInt = dbConnection.dbUpsert(sqlStatement);
                }


                //Update ingredient counts
                int id = item.getProductId();
                product temp_product = products.get(id);
                Vector<ingredient> temp_ingreds = new Vector<ingredient>();

                //Adds default
                for(ingredient ingred : temp_product.ingredients()) {temp_ingreds.add(ingred);}

                //Adds additions
                for(orderItemModification modification : item.getAdditions()) {
                    ingredient temp_ingredient = ingredients.get(modification.getIngredientId());
                    temp_ingreds.add(temp_ingredient);
                }
                
                //Removes subtractions
                for(orderItemModification modification : item.getSubtractions()) {
                    ingredient temp_ingredient = ingredients.get(modification.getIngredientId());
                    temp_ingreds.remove(temp_ingredient);  
                }

                //Decrement the ingredient quanitity
                for(ingredient temp_ingredient : temp_ingreds) {
                    sqlStatement = String.format("SELECT quantityremaining FROM ingredients WHERE id = %d", temp_ingredient.getId());
                    result = dbConnection.dbQuery(sqlStatement);
                    double quantRemaining = 0;
                    while(result.next()){quantRemaining = result.getDouble("quantityremaining");}
                    quantRemaining = quantRemaining - (1 * item.getItemAmount());
                    sqlStatement = String.format("UPDATE ingredients SET quantityremaining = %.2f WHERE id = %d", quantRemaining, temp_ingredient.getId());
                    resultInt = dbConnection.dbUpsert(sqlStatement);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        
    }//End updateDbWithOrder
    
    public boolean isAdmin(int employeeId) {
        ResultSet result;
        boolean check = false;
        dbFunctions dbConnection = new dbFunctions();

        //Check if employee is admin
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
    }//End isAdmin
  
    public String getEmployeeName(int employeeId) {
        ResultSet result;
        String name = "";
        dbFunctions dbConnection = new dbFunctions();

        //Gets employee name from the database
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
    }//End getEmployeeName
    
    
    public 
        product getProduct(int id) {return products.get(id);}
        material getMaterial(int id) {return materials.get(id);}
        ingredient getIngredient(int id) {return ingredients.get(id);}
        orderTicketInfo getOrderTicket(int id) {return orderTickets.get(id);}


    public orderTicketInfo createOrderTicketItem(orderTicketInfo orderTicket, product tempProduct) {
        orderItem tempItem = new orderItem();
        int nextItemId = orderTicket.getOrderItems().size() + 1;
        tempItem.setItemNumberInOrder(nextItemId);
        tempItem.setItemName(tempProduct.getName());
        tempItem.setItemAmount(1);
        tempItem.setProductId(tempProduct.getId());
        tempItem.setItemPrice(tempProduct.getPrice());

        orderTicket.addItemToOrder(tempItem);
        return orderTicket;
    }//End createOrderTicketItem

    public orderItem updateItemWithSize(orderItem item, int size) {
        item.setItemSize(size);
        return item;
    }//End updateItemWithSize

    public orderItem updateItemWithSubtraction(orderItem item, int ingredientId, boolean isSelected) {
        orderItemModification modification = new orderItemModification();
        modification.setItemNumberInOrder(item.getItemNumberInOrder());
        modification.setingredientId(ingredientId);
        modification.setIngredientName(ingredients.get(ingredientId).getName());
        if(isSelected) {item.removeSubraction(modification);}   
        else {item.addSubtraction(modification);}
        
        return item;
    }//End updateItemWithSubtractions
    
    public orderItem updateItemWithAddition(orderItem item, int ingredientId, boolean isSelected) {
        orderItemModification modification = new orderItemModification();
        modification.setItemNumberInOrder(item.getItemNumberInOrder());
        modification.setingredientId(ingredientId);
        modification.setIngredientName(ingredients.get(ingredientId).getName());

        if(isSelected) {item.removeAddition(modification);}   
        else {item.addAddition(modification);}
       
        return item;
    }//End updateItemWithAddition

    public void updateOrderWithItem(orderTicketInfo orderTicket, orderItem item){
        orderTicket.addItemToOrder(item);
    }//End updateOrderWithItem

    public orderTicketInfo duplicateItem(orderTicketInfo orderTicket, orderItem item) {
        orderItem newItem = new orderItem();
        
        //Duplicate item
        int newNum = orderTicket.getOrderItems().size() + 1;
        newItem.setItemName(item.getItemName());
        newItem.setItemAmount(item.getItemAmount());
        newItem.setItemSize(item.getItemSize());
        newItem.setProductId(item.getProductId());
        newItem.setItemPrice(item.getItemPrice());
        for(orderItemModification modification : item.getAdditions()) {
            orderItemModification tempModification = new orderItemModification(modification);
            newItem.addAddition(tempModification);
        }
        for(orderItemModification modification : item.getSubtractions()) {
            orderItemModification tempModification = new orderItemModification(modification);
            newItem.addSubtraction(tempModification);
        }
        newItem = updateItemNumberInOrder(newItem, newNum);

        orderTicket.addItemToOrder(newItem);
		return orderTicket;
    }//End duplicateItem

	public orderTicketInfo deleteFromOrder(orderTicketInfo orderTicket, orderItem item) {
        int index = orderTicket.getOrderItems().indexOf(item);
        int newNum = orderTicket.getOrderItems().elementAt(index).getItemNumberInOrder();
        orderTicket.removeItemFromOrder(item);
        
        //Updated other items
        for(int i = index; i < orderTicket.getOrderItems().size(); i++) {
           orderItem temp_item = updateItemNumberInOrder(orderTicket.getOrderItems().get(i), newNum);
           orderTicket.getOrderItems().remove(i);
           orderTicket.getOrderItems().add(i, temp_item);
           newNum++;
        }
		return orderTicket;
    }//End deleteFromOrder

    private orderItem updateItemNumberInOrder(orderItem item, int newNum) {
        item.setItemNumberInOrder(newNum);
        for (orderItemModification modification : item.getAdditions()) {modification.setItemNumberInOrder(newNum);}
        for (orderItemModification modification : item.getSubtractions()) {modification.setItemNumberInOrder(newNum);}
        return item;
    }//End updateItemNumberInOrder

    public boolean isInSubtractions(orderItem item, String name) {
        for(orderItemModification modification : item.getSubtractions()) {
            if(modification.getIngredientName() == name) {
                return true;
            }
        }
        return false;
    }//End isInSubtractions

    public double getCurrentOrderTotal(orderTicketInfo orderTicket) {
        
        //Calcuating Order Total
        double orderTotal = 0;
        for (orderItem item : orderTicket.getOrderItems()) {orderTotal += (item.getItemPrice() * item.getItemAmount());}
        return orderTotal;

    }//End getCurrentOrderTotal

}