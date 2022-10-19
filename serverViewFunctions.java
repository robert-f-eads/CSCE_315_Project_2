import java.sql.*;
import java.util.*;

/**
 * @author Robert Eads
 * @author Shreyes Kaliyur
 */
public class serverViewFunctions {
    private
        Map<Integer, ingredient> ingredients = new Hashtable<Integer, ingredient>();
        Map<Integer, material> materials = new Hashtable<Integer, material>();
        Map<Integer, product> products = new Hashtable<Integer, product>();

    /**
     * Default constructor for the serverViewFunctions
     */
    public serverViewFunctions() {
        importIngredients();
        importMaterials();
        importProducts();
    }

    /**
     * Imports materials data from the database
     */
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

    /**
     * Imports ingredients data from the database
     */
    void importIngredients() {
        try{
            dbFunctions dbConnection = new dbFunctions();
            dbConnection.createDbConnection();
            String sqlStatement = "SELECT * FROM ingredients";
            ResultSet result = dbConnection.dbQuery(sqlStatement);
            
            //Import all ingredients
            while(result.next()) {
                ingredient temp_ingredient = new ingredient(result.getInt(1), result.getString(2), result.getString(3), result.getDouble(4),
                    result.getDouble(5), result.getString(6), result.getDouble(7), result.getString(8), result.getDouble(9));
                ingredients.put(temp_ingredient.getId(), temp_ingredient);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }//End importIngredients

    /**
     * Imports products data from the database
     */
    void importProducts() {
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

    /**
     * A function to update the database with infomation from the newly placed order
     * @param newTicket a orderticketInfo object with all the information about the order being added to the database
     */
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
            dbConnection.dbUpsert(sqlStatement);
            
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
                dbConnection.dbUpsert(sqlStatement);
               
                //Retreive item id
                sqlStatement = "SELECT * FROM orderitems ORDER BY id DESC LIMIT 1";
                result = dbConnection.dbQuery(sqlStatement);
                result.next();
                int itemNum = result.getInt("itemnumberinorder");

                //Insert into additions 
                for(orderItemModification modification : item.getAdditions()) {
                    sqlStatement = "INSERT INTO orderitemadditions (orderid, itemnumberinorder, ingredientid)";
                    sqlStatement += String.format("VALUES (%d, %d, %d)", ticketId, itemNum, modification.getIngredientId());
                    dbConnection.dbUpsert(sqlStatement);
                }

                //Insert into subtractions
                for(orderItemModification modification : item.getSubtractions()) {
                    sqlStatement = "INSERT INTO orderitemsubtractions (orderid, itemnumberinorder, ingredientid)";
                    sqlStatement += String.format("VALUES (%d, %d, %d)", ticketId, itemNum, modification.getIngredientId());
                    dbConnection.dbUpsert(sqlStatement);
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
                    dbConnection.dbUpsert(sqlStatement);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        
    }//End updateDbWithOrder
    
    /**
     * Checks the database to see if the employee is an admin
     * @param employeeId id of the employee to check
     * @return a whether or not the employee is an admin
     */
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
    }//End isAdmin
  
    /**
     * Checks the database to get the employee name
     * @param employeeId id of the employee to get the name for
     * @return first and last name of the employee
     */
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
        /**
         * Gets a product object from the map
         * @param id id of the product
         * @return the product object for the given id
         */
        product getProduct(int id) {return products.get(id);}

        /**
         * Gets a material object from the map
         * @param id id of the material
         * @return the material object for the given id
         */
        material getMaterial(int id) {return materials.get(id);}

        /**
         * Gets a ingredient object from the map
         * @param id id of the ingredient
         * @return the ingredient object for the given id
         */
        ingredient getIngredient(int id) {return ingredients.get(id);}

        /**
         * Gets the map containing the ingredient objects
         * @return map of ingredient objects
         */
        Map<Integer, ingredient> getIngredients() {return ingredients;}

        /**
         * Gets the map containing the material objects
         * @return map of material objects
         */
        Map<Integer, material> getMaterials() {return materials;}

        /**
         * Gets the map containing the product objects
         * @return map of product objects
         */
        Map<Integer, product> getProducts() {return products;}



    /**
     * Creates a new order item and adds it to the current order ticket
     * @param orderTicket the current order ticket
     * @param tempProduct the product that is being added to the order
     * @return the current order ticket with the added order item
     */
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

    /**
     * Updates the order item to a new size
     * @param item item to be updated
     * @param size new size to update the item with
     * @return order item updated with new size
     */
    public orderItem updateItemWithSize(orderItem item, int size) {
        item.setItemSize(size);
        return item;
    }//End updateItemWithSize

    /**
     * Updates the order item with a subtraction
     * @param item order item to be updated
     * @param ingredientId id of the ingredient to be subtracted 
     * @param isSelected whether or not to remove the ingredient from the item
     * @return the updated order item
     */
    public orderItem updateItemWithSubtraction(orderItem item, int ingredientId, boolean isSelected) {
        orderItemModification modification = new orderItemModification();
        modification.setItemNumberInOrder(item.getItemNumberInOrder());
        modification.setingredientId(ingredientId);
        modification.setIngredientName(ingredients.get(ingredientId).getName());
        if(isSelected) {item.removeSubraction(modification);}   
        else {item.addSubtraction(modification);}
        
        return item;
    }//End updateItemWithSubtractions
    
    /**
     * Updates the order item with an addition
     * @param item order item to be updated
     * @param ingredientId id of the ingredient to be added
     * @param isSelected whether or not to add the ingredient from the item
     * @return the updated order item
     */
    public orderItem updateItemWithAddition(orderItem item, int ingredientId, boolean isSelected) {
        orderItemModification modification = new orderItemModification();
        modification.setItemNumberInOrder(item.getItemNumberInOrder());
        modification.setingredientId(ingredientId);
        modification.setIngredientName(ingredients.get(ingredientId).getName());

        if(isSelected) {item.removeAddition(modification);}   
        else {item.addAddition(modification);}
       
        return item;
    }//End updateItemWithAddition

    /**
     * Updates the current order ticket with an order item
     * @param orderTicket the order ticket to be updated
     * @param item the order item to update the order ticket with
     */
    public void updateOrderWithItem(orderTicketInfo orderTicket, orderItem item){
        orderTicket.addItemToOrder(item);
    }//End updateOrderWithItem

    /**
     * Duplicates and order item
     * @param orderTicket the current order ticket
     * @param item the item to be duplicated
     * @return the updated order ticket with the duplicated item
     */
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

    /**
     * Deletes and item from the order
     * @param orderTicket the current order ticket to remove an item from
     * @param item the item to be removed
     * @return the updated order ticket without the item
     */
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

    /**
     * Updates the item number in order for an item including additions and subtractions
     * @param item the item to update
     * @param newNum the new number in the order
     * @return the updated order item
     */
    private orderItem updateItemNumberInOrder(orderItem item, int newNum) {
        item.setItemNumberInOrder(newNum);
        for (orderItemModification modification : item.getAdditions()) {modification.setItemNumberInOrder(newNum);}
        for (orderItemModification modification : item.getSubtractions()) {modification.setItemNumberInOrder(newNum);}
        return item;
    }//End updateItemNumberInOrder

    /**
     * Checks if a subraction has be selected for an order item
     * @param item the item to check in
     * @param name the name of the ingredient to check for
     * @return whether or not the ingredient was found
     */
    public boolean isInSubtractions(orderItem item, String name) {
        for(orderItemModification modification : item.getSubtractions()) {
            if(modification.getIngredientName() == name) {
                return true;
            }
        }
        return false;
    }//End isInSubtractions

    /**
     * Calculates the monetary total for the order
     * @param orderTicket the order ticket to calculate the total for
     * @return the monetary total for the given ticket
     */
    public double getCurrentOrderTotal(orderTicketInfo orderTicket) {
        
        //Calcuating Order Total
        double orderTotal = 0;
        for (orderItem item : orderTicket.getOrderItems()) {orderTotal += (item.getItemPrice() * item.getItemAmount());}
        return orderTotal;

    }//End getCurrentOrderTotal

}