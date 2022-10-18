import java.util.*;
import java.text.SimpleDateFormat;


/**
 * @author Robert Eads
 */
class orderTicketInfo {  
    private
        int id;
        String timestamp;
        String customerFirstName;
        int rewardsMemberId;
        int employeeId;
        double orderPriceTotal; 
        Vector<orderItem> items;


    //Constructors
    /**
     * Default constructor gives example values to all private fields
     */
    public orderTicketInfo() {
        id = -1;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        java.util.Date date = new java.util.Date();
        timestamp = formatter.format(date);
        customerFirstName = "";
        rewardsMemberId = 0;
        employeeId = -1;
        orderPriceTotal = -1;
        items = new Vector<orderItem>();
    }

    /**
     * Parametrized constructor for orderTicketInfo
     * @param id the orderId
     * @param timestamp the time the order was made
     * @param customerFirstName customer's first name
     * @param rewardsMemberId the associated reward id for the customer
     * @param employeeId the id of the employee who took the order
     * @param orderPriceTotal the total price of the order
     */
    public orderTicketInfo(int id, String timestamp, String customerFirstName, int rewardsMemberId, int employeeId, double orderPriceTotal) {
        this.id = id;
        this.timestamp = timestamp;
        this.customerFirstName = customerFirstName;
        this.rewardsMemberId = rewardsMemberId;
        this.employeeId = employeeId;
        this.orderPriceTotal = orderPriceTotal;
        items = new Vector<orderItem>();
    }


    public
        //Getters
        /**
         * Gets the id of the order ticket
         * @return the id of the order ticket
         */
        int getId() {return id;}
        /**
         * Gets the time the order was made
         * @return the time the order was made
         */
        String getTimestamp() {return timestamp;}
        /**
         * Gets the first name of the customer who made the order
         * @return the first name of the customer who made the order
         */
        String getCustomerFirstName() {return customerFirstName;}
        /**
         * Gets the rewards member id associated with the customer
         * @return the rewards member id associated with the customer
         */
        int getRewardsMemberId() {return rewardsMemberId;}
        /**
         * Gets the id of the employee who took the order
         * @return the id of the employee who took the order
         */
        int getEmployeeId() {return employeeId;}
        /**
         * Gets the total price of the order
         * @return the total price of the order
         */
        double getOrderPriceTotal() {return orderPriceTotal;}
        /**
         * Gets the date extracted from the timestamp field
         * @return the date extracted from the timestamp field
         */
        String getDate() {return timestamp.substring(0,10);}
        /**
         * Gets the time extracted from the timestamp field
         * @return the time extracted from the timestamp field
         */
        String getTime() {return timestamp.substring(11);}
        /**
         * Gets a vector of orderItem objects which are in the current order ticket
         * @return a vector of orderItem objects which are in the current order ticket
         */
        Vector<orderItem> getOrderItems() {return items;}

        //Setters
        /**
         * Sets orderId for the ticket
         * @param id orderId for the ticket
         */
        void setId(int id) {this.id = id;}
        /**
         * Sets the time which the ticket order was placed
         * @param timestamp the time which the ticket order was placed
         */
        void settimestamp(String timestamp) {this.timestamp = timestamp;}
        /**
         * Sets the first name of the customer who made the order
         * @param customerFirstName the first name of the customer who made the order
         */
        void setCustomerFirstName(String customerFirstName) {this.customerFirstName = customerFirstName;}
        /**
         * Sets the rewards member id associated with the customer
         * @param rewardsMemberId the rewards member id associated with the customer
         */
        void setRewardsMemberId(int rewardsMemberId) {this.rewardsMemberId = rewardsMemberId;}
        /**
         * Sets the employee id of the employee who took the order
         * @param employeeId the employee id of the employee who took the order
         */
        void setEmployeeId(int employeeId) {this.employeeId = employeeId;}
        /**
         * Sets the total price of the order ticket
         * @param orderPriceTotal the total price of the order ticket
         */
        void setOrderPriceTotal(double orderPriceTotal) {this.orderPriceTotal = orderPriceTotal;}
        /**
         * Adds an item to the order ticket vector
         * @param item item to add to the order ticket
         */
        void addItemToOrder(orderItem item) {items.add(item);}
        /**
         * Removes an item from the order ticket vector
         * @param item item to remove from the order ticket
         */
        void removeItemFromOrder(orderItem item) {items.remove(item);}


    /**
     * Prints out an order ticket in a human readable format
     */
    @Override
    public String toString() {
        String print = String.format("Id: %d\ntimestamp: %s\nCustomer First Name: %s\nRewards Member Id: %d\nEmployee Id: %d\nOrder Price Total: %.2f",
            id, timestamp, customerFirstName, rewardsMemberId, employeeId, orderPriceTotal);
        return print;
    }
}//End orderTicketInfo 

/**
 * @author Robert Eads
 */
class orderItem {
    private
        int id;
        int orderId;
        int productId;
        int itemNumberInOrder;
        String itemName;
        int itemAmount;
        int itemSize;
        double itemPrice;
        Vector<orderItemModification> additions;
        Vector<orderItemModification> subtractions;


    //Constructors
    /**
     * Default constructor for orderItem, gives all fields default values
     * itemSize is set to 20, the smallest size
     */
    public orderItem() {
        id = -1;
        orderId = -1;
        productId = -1;
        itemNumberInOrder = 1;
        itemName = "";
        itemAmount = 1;
        itemSize = 20;
        itemPrice = -1;
        additions = new Vector<orderItemModification>();
        subtractions = new Vector<orderItemModification>();
    }

    /**
     * Parametrized constructor for an orderItem
     * @param id the id of the item on the order ticket
     * @param orderId the orderId this item is a part of
     * @param itemNumberInOrder the index of this item in the order ticket
     * @param itemName the name of the item
     * @param itemAmount the number of this specific item in the order ticket
     * @param itemSize the size of the item (one of our 3 integer sizes in oz)
     */
    public orderItem(int id, int orderId, int itemNumberInOrder, String itemName, int itemAmount, int itemSize) {
        this.id = id;
        this.orderId = orderId;
        this.itemNumberInOrder = itemNumberInOrder;
        this.itemName = itemName;
        this.itemAmount = itemAmount;
        this.itemSize = itemSize;
        additions = new Vector<orderItemModification>();
        subtractions = new Vector<orderItemModification>();
    }


    public
        //Getters
        /**
         * Gets the id of the order item
         * @return the id of the order item
         */
        int getId() {return id;}
        /**
         * Gets the order id of the order ticket this item is a part of 
         * @return the order id of the order ticket this item is a part of 
         */
        int getOrderId() {return orderId;}
        /**
         * Gets the id of the product this item refers to
         * @return the id of the product this item refers to
         */
        int getProductId() {return productId;}
        /**
         * Gets the index of the item in the order ticket
         * @return the index of the item in the order ticket
         */
        int getItemNumberInOrder() {return itemNumberInOrder;}
        /**
         * Gets the name of the item
         * @return the name of the item
         */
        String getItemName() {return itemName;}
        /**
         * Gets the count of this specific item in the order ticket
         * @return the count of this specific item in the order ticket
         */
        int getItemAmount() {return itemAmount;}
        /**
         * Gets the size of the item
         * @return the size of the item
         */
        int getItemSize() {return itemSize;}
        /**
         * Gets the price of the item not taking into account the count
         * @return the price of the item not taking into account the count
         */
        double getItemPrice() {return itemPrice;}
        /**
         * Gets the additions to the default item
         * @return the additions to the default item
         */
        Vector<orderItemModification> getAdditions() {return additions;}
        /**
         * Gets the subtractions from the default item
         * @return the subtractions from the default item
         */
        Vector<orderItemModification> getSubtractions() {return subtractions;}

        //Setters
        /**
         * Sets the id of the order item
         * @param id the id of the order item
         */
        void setId(int id) {this.id = id;}
        /**
         * Sets the order id of the order ticket this item is a part of 
         * @param orderId the order id of the order ticket this item is a part of 
         */
        void setOrderId(int orderId) {this.orderId = orderId;}
        /**
         * Sets the id of the product that is a part of this order item
         * @param productId the id of the product that is a part of this order item
         */
        void setProductId(int productId) {this.productId = productId;}
        /**
         * Sets the index of the item in the order
         * @param itemNumberInOrder the index of the item in the order
         */
        void setItemNumberInOrder(int itemNumberInOrder) {this.itemNumberInOrder = itemNumberInOrder;}
        /**
         * Sets the name of the item
         * @param itemName the name of the item
         */
        void setItemName(String itemName) {this.itemName = itemName;}
        /**
         * Sets the count of the item in the order ticket
         * @param itemAmount the count of the item in the order ticket
         */
        void setItemAmount(int itemAmount) {this.itemAmount = itemAmount;}
        /**
         * Sets the size of the item
         * @param itemSize the size of the item
         */
        void setItemSize(int itemSize) {this.itemSize = itemSize;}
        /**
         * Sets the price of the item
         * @param itemPrice the price of the item
         */
        void setItemPrice(double itemPrice) {this.itemPrice = itemPrice;}
        /**
         * Adds a modification to the current item
         * @param modification a modification to be added to the current item
         */
        void addAddition(orderItemModification modification) {additions.add(modification);}
        /**
         * Removes an added modification from the current item
         * @param modification a modification to be removed from the current list of additions
         */
        void removeAddition(orderItemModification modification) {additions.remove(modification);}
        /**
         * Removes an ingredient from the current item
         * @param modification a modification to be removed from the current item
         */
        void addSubtraction(orderItemModification modification) {subtractions.add(modification);}
        /**
         * Adds back an ingredient that was removed from the item
         * @param modification a modification to be readded back to the item
         */
        void removeSubraction(orderItemModification modification) {subtractions.remove(modification);}
     
        /**
         * returns a human readable representation of an orderItem
         */
        @Override
        public String toString() {
            String print = String.format("Id: %d\nOrder Id: %d\nItem Number in Order: %d\nItem Name: %s\nItem Amount: %d\nItem Size: %d",
                id, orderId, itemNumberInOrder, itemName, itemAmount, itemSize);
            return print;
        }        
}//End orderItem

class orderItemModification {
    private
        int id;
        int orderId;
        int itemNumberInOrder;
        int ingredientId;
        String ingredientName = "";


    //Constructors
    public orderItemModification() {
        id = -1;
        orderId = -1;
        itemNumberInOrder = -1;
        ingredientId = -1;
    }

    public orderItemModification(int id, int orderId, int itemNumberInOrder, int ingredientId) {
        this.id = id;
        this.orderId = orderId;
        this.itemNumberInOrder = itemNumberInOrder;
        this.ingredientId = ingredientId;
    }

    public orderItemModification(int id, int orderId, int itemNumberInOrder, int ingredientId, String ingredientName) {
        this.id = id;
        this.orderId = orderId;
        this.itemNumberInOrder = itemNumberInOrder;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
    }

    public orderItemModification(orderItemModification otherMod) {
        this.id = otherMod.getId();
        this.orderId = otherMod.getOrderId();
        this.itemNumberInOrder = otherMod.getItemNumberInOrder();
        this.ingredientId = otherMod.getIngredientId();
        this.ingredientName = otherMod.getIngredientName();
    }


    public
        //Getters
        int getId() {return id;}
        int getOrderId() {return orderId;}
        int getItemNumberInOrder() {return itemNumberInOrder;}
        int getIngredientId() {return ingredientId;}
        String getIngredientName() {return ingredientName;}

        //Setters
        void setId(int id) {this.id = id;}
        void setOrderId(int orderId) {this.orderId = orderId;}
        void setItemNumberInOrder(int itemNumberInOrder) {this.itemNumberInOrder = itemNumberInOrder;}
        void setingredientId(int ingredientId) {this.ingredientId = ingredientId;}
        void setIngredientName(String ingredientName) {this.ingredientName = ingredientName;}
     

    @Override
    public String toString() {
        String print = String.format("Id: %d\nOrder Id: %d\nItem Number in Order: %d\nIngredient Id: %d\nIngredient Name: %s", id, orderId, itemNumberInOrder, ingredientId, ingredientName);
        return print;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {return true;}
        if(!(o instanceof orderItemModification)) {return false;}
        orderItemModification order = (orderItemModification) o;
        if(order.getIngredientId() == ingredientId) {return true;}
        return false;
    }
}//End orderItemModification

class ingredient {

    int id;
    String name;
    String expirationDate; // Date
    double quantityRemaining;
    double quantityTarget;
    String measurementUnits;
    double pricePerUnitLastOrder;
    String lastOrderDate; // Date
    double unitsInLastOrder;

    public ingredient() {
        this.id = -1;
        this.name = "";
        this.expirationDate = "";
        this.quantityRemaining = -1;
        this.quantityTarget = -1;
        this.measurementUnits = "";
        this.pricePerUnitLastOrder = -1;
        this.lastOrderDate = "";
        this.unitsInLastOrder = -1;
    }
    
    public ingredient(int id, String name, String expirationDate, double quantityRemaining, double quantityTarget,
                      String measurementUnits, double pricePerUnitLastOrder, String lastOrderDate, double unitsInLastOrder) {
        this.id = id;
        this.name = name;
        this.expirationDate = expirationDate;
        this.quantityRemaining = quantityRemaining;
        this.quantityTarget = quantityTarget;
        this.measurementUnits = measurementUnits;
        this.pricePerUnitLastOrder = pricePerUnitLastOrder;
        this.lastOrderDate = lastOrderDate;
        this.unitsInLastOrder = unitsInLastOrder;
    }

    public 
        int getId() {return this.id;}
        String getName() {return this.name;}
        String getExpirationDate() {return this.expirationDate;}
        double getQuantityRemaining() {return this.quantityRemaining;}
        double getQuantityTarget() {return this.quantityTarget;}
        String getMeasurementUnits() {return this.measurementUnits;}
        double getPricePerUnitLastOrder() {return this.pricePerUnitLastOrder;}
        String getLastOrderDate() {return this.lastOrderDate;}
        double getUnitsInLastOrder() {return this.unitsInLastOrder;}

        void setId(int id) {this.id = id;}
        void setName(String name) {this.name = name;}
        void setExpirationDate(String expirationDate) {this.expirationDate = expirationDate;}
        void setQuantityRemaining(double quantityRemaining) {this.quantityRemaining = quantityRemaining;}
        void setMeasurementUnits(String measurementUnits) {this.measurementUnits = measurementUnits;}
        void setPricePerUnitLastOrder(double pricePerUnitLastOrder) {this.pricePerUnitLastOrder = pricePerUnitLastOrder;}
        void setLastOrderDate(String lastOrderDate) {this.lastOrderDate = lastOrderDate;}
        void setUnitsInLastOrder(double unitsInLastOrder) { this.unitsInLastOrder = unitsInLastOrder; }
    

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", quantityRemaining='" + getQuantityRemaining() + "'" +
            ", measurementUnits='" + getMeasurementUnits() + "'" +
            ", pricePerUnitLastOrder='" + getPricePerUnitLastOrder() + "'" +
            ", lastOrderDate='" + getLastOrderDate() + "'" +
            ", unitsInLastOrder='" + getUnitsInLastOrder() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {return true;}
        if(!(o instanceof ingredient)) {return false;}
        ingredient order = (ingredient) o;
        if(order.getId() == id) {return true;}
        return false;

    }
}//End ingredient

class material {
    private
        int id;
        String name;
        String size;
        int quantityRemaining;
        String measurementUnits;
        int itemsPerUnit;
        double pricePerUnitLastOrder;
        String lastOrderDate;
        double unitsInLastOrder;


    //Constructors
    public material() {
        id = -1;
        name = "";
        size = "";
        quantityRemaining = -1;
        measurementUnits = "";
        itemsPerUnit = -1;
        pricePerUnitLastOrder = -1;
        lastOrderDate =  "";
        unitsInLastOrder = -1;
    }

    public material(int id, String name, String size, int quantityRemaining, String measurementUnits, int itemsPerUnit, 
        double pricePerUnitLastOrder, String lastOrderDate, double unitsInLastOrder) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.quantityRemaining = quantityRemaining;
        this.measurementUnits = measurementUnits;
        this.itemsPerUnit = itemsPerUnit;
        this.pricePerUnitLastOrder = pricePerUnitLastOrder;
        this.lastOrderDate =  lastOrderDate;
        this.unitsInLastOrder = unitsInLastOrder;
    }


    public
        //Getters
        int getId() {return id;}
        String getName() {return name;}
        String getSize() {return size;}
        int getQuantityRemaining() {return quantityRemaining;}
        String getMeasurementUnits() {return measurementUnits;}
        int getItemsPerUnit() {return itemsPerUnit;}
        double getPricePerUnitLastOrder() {return pricePerUnitLastOrder;}
        String getLastOrderDate() {return lastOrderDate;}
        double getUnitsInLastOrder() {return unitsInLastOrder;}

        //Setters
        void setId(int id) {this.id = id;}
        void setName(String name) {this.name = name;}
        void setSize(String size) {this.size = size;}
        void setQuantityRemaining(int quantityRemaining) {this.quantityRemaining = quantityRemaining;}
        void setMeasurementUnits(String measurementUnits) {this.measurementUnits = measurementUnits;}
        void setItemsPerUnit(int itemsPerUnit) {this.itemsPerUnit = itemsPerUnit;}
        void setPricePerUnitLastOrder(double pricePerUnitLastOrder) {this.pricePerUnitLastOrder = pricePerUnitLastOrder;}
        void setLastOrderDate(String lastOrderDate) {this.lastOrderDate = lastOrderDate;}
        void setUnitsInLastOrder(double unitsInLastOrder) {this.unitsInLastOrder = unitsInLastOrder;}


    @Override
    public String toString() {
        String print = String.format("Id: %d\nMaterial Name: %s\nMaterial Size: %s\nQuantity Remaining: %d\nMeasurement Units: %s\nItems Per Unit: %d\nPrice Per Unit Last Order: %.2f\nLast Order Date: %s\nUnits in Last Order: %.2f",
            id, name, size, quantityRemaining, measurementUnits, itemsPerUnit, pricePerUnitLastOrder, lastOrderDate, unitsInLastOrder);
        return print;
    }
}//End material

class product {
    private
        int id;
        String name;
        double price;
        Vector<ingredient> ingredients;

    // Constructors
    public product() {
        id = -1;
        name = "";
        price = -1;
        ingredients = new Vector<ingredient>();
    }

    public product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        ingredients = new Vector<ingredient>();
    }


    public
        //Getters
        int getId() {return id;}
        String getName() {return name;}
        double getPrice() {return price;}
        Vector<ingredient> ingredients() {return ingredients;}

        //Setters
        void setId(int id) {this.id = id;}
        void setName(String name) {this.name = name;}
        void setPrice(double price) {this.price = price;}
        void addIngredient(ingredient ingredientAdded) {ingredients.add(ingredientAdded);}
        void removeIngredient(ingredient ingredientRemoved) {ingredients.remove(ingredientRemoved);}
        void setIngredients(Vector<ingredient> ingredients) {this.ingredients = ingredients;}


    @Override
    public String toString() {
        String print = String.format("Id: %d\nName: %s\nPrice: %.2f",
            id, name, price);
        return print;
    }
}//End product

/**
 * @author Robert Eads
 */
class dateStruct {
    String month;
    String day;
    String year;
    String hours;
    String minutes;
    String seconds;
    
    /**
     * Constructor for dateStruct that stores the date input from users
     * @param date a string of the data in the format of yyyy-mm-dd
     */
    public dateStruct(String date) {
        String[] parts = date.split("-");
        this.year = parts[0];
        this.month = parts[1];
        this.day = parts[2];
    }

    /**
     * A function to format the stored date into database formatting
     * @return The stored date in proper format for the database
     */
    public String formatString() {return String.format("%s-%s-%s", year, month, day);}

    /**
     * A function to format the end time of the day in database format
     * @return The end time of the day in database format
     */
    public String getEndOfDay() {return "23:59:59";}

    /**
     * A function to format the start time of the day in database format
     * @return The start time of the day in database format
     */
    public String getStartOfDay() {return "00:00:00";}

    /**
     * A function to format the stored time into database formatting
     * @return The stored time in proper format for the database
     */
    public String getTimeOfDay() {return String.format("%s:%s:%s", hours, minutes, seconds);}
}

/**
 * @author Robert Eads
 */
class salesReportItem {
    public
        int productId;
        String productName;
        int quantitySold;
        double totalSales;

    /**
     * Constructor for salesReportItem that contains product information
     * @param id id of the product
     * @param name name of the product
     * @param quantity amount of the product sold
     * @param sales monetary total of sales for this product 
     */
    public salesReportItem(int id, String name, int quantity, double sales) {
        productId = id;
        productName = name;
        quantitySold = quantity;
        totalSales = sales;

    }
}

/**
 * @author Robert Eads
 */
class excessReportItem {
    int ingredientId;
    String ingredientName;
    double quantityRemaining;
    double quantityUsed;
    double quantityAtStart;

    /**
     * Constructor for excessReportItem that contains ingredient information
     * @param id id of the ingredient
     * @param name name of the ingredient
     * @param remaining remaining amount of inventory for the ingredient
     * @param used used amount of inventory for the ingredient
     * @param atStart inital amount of inventory for the ingredient
     */
    public excessReportItem(int id, String name, double remaining, double used, double atStart) {
        ingredientId = id;
        ingredientName = name;
        quantityRemaining = remaining;
        quantityUsed = used;
        quantityAtStart = atStart;
    }
}