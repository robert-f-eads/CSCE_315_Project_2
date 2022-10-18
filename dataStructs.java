import java.util.*;
import java.text.SimpleDateFormat;

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
        int getId() {return id;}
        String getTimestamp() {return timestamp;}
        String getCustomerFirstName() {return customerFirstName;}
        int getRewardsMemberId() {return rewardsMemberId;}
        int getEmployeeId() {return employeeId;}
        double getOrderPriceTotal() {return orderPriceTotal;}
        String getDate() {return timestamp.substring(0,10);}
        String getTime() {return timestamp.substring(11);}
        Vector<orderItem> getOrderItems() {return items;}

        //Setters
        void setId(int id) {this.id = id;}
        void settimestamp(String timestamp) {this.timestamp = timestamp;}
        void setCustomerFirstName(String customerFirstName) {this.customerFirstName = customerFirstName;}
        void setRewardsMemberId(int rewardsMemberId) {this.rewardsMemberId = rewardsMemberId;}
        void setEmployeeId(int employeeId) {this.employeeId = employeeId;}
        void setOrderPriceTotal(double orderPriceTotal) {this.orderPriceTotal = orderPriceTotal;}
        void addItemToOrder(orderItem item) {items.add(item);}
        void removeItemFromOrder(orderItem item) {items.remove(item);}


    @Override
    public String toString() {
        String print = String.format("Id: %d\ntimestamp: %s\nCustomer First Name: %s\nRewards Member Id: %d\nEmployee Id: %d\nOrder Price Total: %.2f",
            id, timestamp, customerFirstName, rewardsMemberId, employeeId, orderPriceTotal);
        return print;
    }
}//End orderTicketInfo   

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
        int getId() {return id;}
        int getOrderId() {return orderId;}
        int getProductId() {return productId;}
        int getItemNumberInOrder() {return itemNumberInOrder;}
        String getItemName() {return itemName;}
        int getItemAmount() {return itemAmount;}
        int getItemSize() {return itemSize;}
        double getItemPrice() {return itemPrice;}
        Vector<orderItemModification> getAdditions() {return additions;}
        Vector<orderItemModification> getSubtractions() {return subtractions;}

        //Setters
        void setId(int id) {this.id = id;}
        void setOrderId(int orderId) {this.orderId = orderId;}
        void setProductId(int productId) {this.productId = productId;}
        void setItemNumberInOrder(int itemNumberInOrder) {this.itemNumberInOrder = itemNumberInOrder;}
        void setItemName(String itemName) {this.itemName = itemName;}
        void setItemAmount(int itemAmount) {this.itemAmount = itemAmount;}
        void setItemSize(int itemSize) {this.itemSize = itemSize;}
        void setItemPrice(double itemPrice) {this.itemPrice = itemPrice;}
        void addAddition(orderItemModification modification) {additions.add(modification);}
        void removeAddition(orderItemModification modification) {additions.remove(modification);}
        void addSubtraction(orderItemModification modification) {subtractions.add(modification);}
        void removeSubraction(orderItemModification modification) {subtractions.remove(modification);}
     

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


    @Override
    public String toString() {
        String print = String.format("Id: %d\nName: %s\nPrice: %.2f",
            id, name, price);
        return print;
    }
}//End product

class dateStruct {
    String month;
    String day;
    String year;
    String hours;
    String minutes;
    String seconds;

    public dateStruct(String date) {
        String[] parts = date.split("-");
        this.year = parts[0];
        this.month = parts[1];
        this.day = parts[2];
    }

    public String formatString() {return String.format("%s-%s-%s", year, month, day);}
    public String getEndOfDay() {return "23:59:59";}
    public String getStartOfDay() {return "00:00:00";}
    public String getTimeOfDay() {return String.format("%s:%s:%s", hours, minutes, seconds);}
}

class salesReportItem {
    public
        int productId;
        String productName;
        int quantitySold;
        double totalSales;

    public salesReportItem(int id, String name, int quantity, double sales) {
        productId = id;
        productName = name;
        quantitySold = quantity;
        totalSales = sales;

    }
}