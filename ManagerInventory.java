import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * @author Emma Ong
 * @author Shreyes Kaliyur
 * @author Alexia Hassan
 * @author Robert Eads
 */

public class ManagerInventory extends ManagerViewScreen {
    boolean toggleInventory = true;

    public ManagerInventory(ManagerView managerView) {super(managerView);}
    
    public void setInventoryView() {
        managerView.mainPanel.setLayout(new BoxLayout(managerView.mainPanel, BoxLayout.PAGE_AXIS));
        managerView.mainPanel.setBackground(Color.white);

        int buttonWidth = 150;
        int buttonHeight = 150;
        int generalHeight = 42;
        JButton back = managerView.createButton("Back", buttonWidth, buttonHeight);
        JButton searchButton = managerView.createButton("Search", buttonWidth, buttonHeight);
        JButton update = managerView.createButton("Update", buttonWidth, buttonHeight);
        JButton add = managerView.createButton("Add", buttonWidth, buttonHeight);
        JButton delete = managerView.createButton("Delete", buttonWidth, buttonHeight);
        JButton toggleTable = managerView.createButton("Switch to Product Table", buttonWidth, buttonHeight);

        back.setPreferredSize(new Dimension(110,generalHeight));
        searchButton.setPreferredSize(new Dimension(100,generalHeight));
        update.setPreferredSize(new Dimension(110,generalHeight));
        add.setPreferredSize(new Dimension(100,generalHeight));
        delete.setPreferredSize(new Dimension(110,generalHeight));

        back.setMaximumSize(new Dimension(110,generalHeight));
        searchButton.setMaximumSize(new Dimension(130,generalHeight));
        update.setMaximumSize(new Dimension(110,generalHeight));
        add.setMaximumSize(new Dimension(100,generalHeight));
        delete.setMaximumSize(new Dimension(100,generalHeight));

        JLabel inventory = managerView.createLabel("Inventory", buttonWidth, buttonHeight);
        inventory.setFont(new Font("SansSerif", Font.PLAIN, 28));
        inventory.setBackground(Color.green);
        inventory.setAlignmentX(Component.CENTER_ALIGNMENT);


        HintTextField searchText = (HintTextField) formatTextArea(new HintTextField("Search", buttonWidth, buttonHeight));
        HintTextField id = (HintTextField) formatTextArea(new HintTextField("Id", 60, buttonHeight));


        HintTextField[] attributeFields = new HintTextField[8];
        HintTextField name = (HintTextField) formatTextArea(new HintTextField("Name", 150, generalHeight));
        HintTextField expiration = (HintTextField) formatTextArea(new HintTextField("Expiration", 150, generalHeight));
        HintTextField quantity = (HintTextField) formatTextArea(new HintTextField("Quantity", 90, generalHeight));
        HintTextField target = (HintTextField) formatTextArea(new HintTextField("Target", 90, generalHeight));
        HintTextField measurement = (HintTextField) formatTextArea(new HintTextField("Measurement", 100, generalHeight));
        HintTextField price = (HintTextField) formatTextArea(new HintTextField("Price", 90, generalHeight));
        HintTextField last = (HintTextField) formatTextArea(new HintTextField("Last", 150, generalHeight));
        HintTextField units = (HintTextField) formatTextArea(new HintTextField("Units", 90, generalHeight));

        // we store attribute fields like this so it is easy to fill them using the table 
        // this lets the user not have to manually enter unimportant fields when updating items
        // in the table
        attributeFields[0] = name;
        attributeFields[1] = expiration;
        attributeFields[2] = quantity;
        attributeFields[3] = target;
        attributeFields[4] = measurement;
        attributeFields[5] = price;
        attributeFields[6] = last;
        attributeFields[7] = units;

        JTextField tableText = formatTextArea(new JTextField("ingredients"));
        tableText.setEditable(false);
        tableText.setVisible(false);

        JTable inventoryTable = new JTable();
        JTable productTable = new JTable();
        updateIngredientsTable(inventoryTable);
        updateProductsTable(productTable);

        back.addActionListener(e -> {
            new ManagerView(managerView.serverFunctions);
            managerView.myFrame.dispose();
        });

        
        searchButton.addActionListener(e -> {
            productTable.setModel(managerView.resultSetToTableModel(null,
                managerView.myDbConnection.dbQuery("SELECT * FROM products WHERE name ILIKE '%" + searchText.getText() + "%'")));
            inventoryTable.setModel(managerView.resultSetToTableModel(null,
                managerView.myDbConnection.dbQuery("SELECT * FROM ingredients WHERE name ILIKE '%" + searchText.getText() + "%'")));
            managerView.myFrame.repaint();
            managerView.myFrame.revalidate();
        });


        update.addActionListener(e -> {
            String table = tableText.getText();
            for(int i = 0; i < attributeFields.length; i++) {
                HintTextField attributeField = attributeFields[i];
                if(attributeField.getText().equals(attributeField.getHint())) {
                    // we add one to i because we do not include id as an attribute
                    managerView.setUneditedAttribute(table, id.getText(), i + 1, attributeField);
                }
            }
            String sql = "";
            if(table.equals("products")) {
                sql = "UPDATE products SET name='" + name.getText() + "', price=" + price.getText() + " WHERE id=" + id.getText();
            } else {
                sql = "UPDATE ingredients SET name='" + name.getText() + "', expirationdate= date'" + expiration.getText() + 
                    "', measurementunits='" + measurement.getText() + "', lastorderdate= date'" + last.getText() + "', priceperunitlastorder=" + 
                    price.getText() + ", quantityRemaining=" + quantity.getText() + ", quantityTarget=" + target.getText() + ", unitsinlastorder=" + units.getText() +
                    " WHERE id=" + id.getText();
                System.out.println(sql);
            }
            for(HintTextField attributeField : attributeFields) {
                attributeField.resetText();
            }
            managerView.myDbConnection.dbUpsert(sql);
            updateProductsTable(productTable);
            updateIngredientsTable(inventoryTable);
        });


        add.addActionListener(e -> {
            String table = tableText.getText();
            String sql = "";
            if(table.equals("products")) {
                sql = "INSERT INTO products (name, price) VALUES ('" + name.getText() + "', " + price.getText() + ")";
            } else {
                sql = "INSERT INTO ingredients (name, expirationdate, quantityremaining, quantitytarget, measurementunits, priceperunitlastorder, lastorderdate, unitsinlastorder) VALUES ('" + name.getText() + "', '" + 
                    expiration.getText() + "', " + quantity.getText() + ", " + target.getText() + ", '" + measurement.getText() + "', " + 
                    price.getText() + ", '" + last.getText() + "', " + units.getText() + ")";
            }
            managerView.myDbConnection.dbUpsert(sql);
            updateProductsTable(productTable);
            updateIngredientsTable(inventoryTable);
        });


        delete.addActionListener(e -> {
            String table = tableText.getText();
            String sqlFirst = "";
            String sqlSecond = "";

            if(table.equals("products")) {
                sqlFirst = "DELETE FROM productstoingredients WHERE productid =" + id.getText();
                sqlSecond = "DELETE FROM products WHERE id=" + id.getText();
            } else {
                sqlFirst = "DELETE FROM productstoingredients WHERE ingredientid =" + id.getText();
                sqlSecond = "DELETE FROM ingredients WHERE id=" + id.getText();
            }
            managerView.myDbConnection.dbUpsert(sqlFirst);
            managerView.myDbConnection.dbUpsert(sqlSecond);
            updateProductsTable(productTable);
            updateIngredientsTable(inventoryTable);
        });

        JPanel title = new JPanel();
        title.setLayout(new BoxLayout(title, BoxLayout.LINE_AXIS));
        title.setBackground(Color.white);
        title.add(Box.createRigidArea(new Dimension(700, 0)));
        title.add(inventory);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel topButtons = new JPanel();
        topButtons.setBackground(Color.white);
        topButtons.setLayout(new BoxLayout(topButtons, BoxLayout.LINE_AXIS));
        //topButtons.add(Box.createRigidArea(new Dimension(15, 0)));
        topButtons.add(back);
        topButtons.add(Box.createRigidArea(new Dimension(465, 0)));
        topButtons.add(searchText);
        topButtons.add(searchButton);
        topButtons.add(Box.createRigidArea(new Dimension(200, 0)));
        topButtons.add(toggleTable);
        topButtons.setAlignmentX(Component.LEFT_ALIGNMENT);


        JPanel header = new JPanel();
        header.setBackground(Color.white);
        header.setLayout(new BoxLayout(header, BoxLayout.PAGE_AXIS));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 25)));
        header.add(topButtons);
        header.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.LINE_AXIS));
        south.setPreferredSize(new Dimension(1500,140));
        south.setMaximumSize(new Dimension(1500,140));
        south.setAlignmentX(Component.CENTER_ALIGNMENT);
        south.setBackground(Color.white);

        south.add(Box.createHorizontalGlue());
        south.setBackground(Color.white);
        south.add(id);
        south.add(Box.createHorizontalGlue());
        south.add(name);
        south.add(Box.createHorizontalGlue());
        south.add(expiration);
        south.add(Box.createHorizontalGlue());
        south.add(quantity);
        south.add(Box.createHorizontalGlue());
        south.add(target);
        south.add(Box.createHorizontalGlue());
        south.add(measurement);
        south.add(Box.createHorizontalGlue());
        south.add(price);
        south.add(Box.createHorizontalGlue());
        south.add(last);
        south.add(Box.createHorizontalGlue());
        south.add(units);
        south.add(Box.createHorizontalGlue());
        south.add(update);
        south.add(Box.createHorizontalGlue());
        south.add(add);
        south.add(Box.createHorizontalGlue());
        south.add(delete);
        south.add(Box.createHorizontalGlue());
        south.add(tableText); 
        south.add(Box.createHorizontalGlue());
        south.setAlignmentX(Component.CENTER_ALIGNMENT);

        id.setPreferredSize(new Dimension(60, generalHeight));
        name.setPreferredSize(new Dimension(150, generalHeight));
        expiration.setPreferredSize(new Dimension(150, generalHeight));
        quantity.setPreferredSize(new Dimension(90, generalHeight));
        target.setPreferredSize(new Dimension(90, generalHeight));
        measurement.setPreferredSize(new Dimension(100, generalHeight));
        price.setPreferredSize(new Dimension(90, generalHeight));
        last.setPreferredSize(new Dimension(150, generalHeight));
        units.setPreferredSize(new Dimension(90, generalHeight));

        id.setMaximumSize(new Dimension(60, generalHeight));
        name.setMaximumSize(new Dimension(150, generalHeight));
        expiration.setMaximumSize(new Dimension(150, generalHeight));
        quantity.setMaximumSize(new Dimension(90, generalHeight));
        target.setMaximumSize(new Dimension(90, generalHeight));
        measurement.setMaximumSize(new Dimension(100, generalHeight));
        price.setMaximumSize(new Dimension(90, generalHeight));
        last.setMaximumSize(new Dimension(150, generalHeight));
        units.setMaximumSize(new Dimension(90, generalHeight));


        managerView.mainPanel.add(header);
        managerView.mainPanel.add(new JScrollPane(inventoryTable));
        managerView.mainPanel.add(south);
        managerView.mainPanel.add(Box.createRigidArea(new Dimension(0,60)));
        
        


        toggleTable.addActionListener(e -> {
            managerView.mainPanel.removeAll();
            if (toggleInventory) {
                System.out.println("Toggling to product table");
                toggleTable.setText("Switch to Inventory Table");
                tableText.setText("products");
                managerView.mainPanel.add(header, BorderLayout.PAGE_START);
                managerView.mainPanel.add(new JScrollPane(productTable));
                managerView.mainPanel.add(south);
                toggleInventory = false;
            }
            else {
                System.out.println("Toggling to inventory table");
                toggleTable.setText("Switch to Product Table");
                tableText.setText("ingredients");
                managerView.mainPanel.add(header);
                managerView.mainPanel.add(new JScrollPane(inventoryTable));
                managerView.mainPanel.add(south);
                toggleInventory = true;
            }
            managerView.mainPanel.revalidate();
            managerView.mainPanel.repaint();
        });
    }

    JTextField formatTextArea(JTextField b) {
        b.setFont(new Font("SansSerif", Font.PLAIN, 15));
        b.setBackground(Color.white);
        b.setMinimumSize(new Dimension(400,75));
        b.setMaximumSize(new Dimension(400,75));
        return b;
    }


    /**
     * A function to update the ingredients table
     * @param table the JTable to update
     */
    void updateIngredientsTable(JTable table) {
        ResultSet row;
        row = managerView.myDbConnection.dbQuery("SELECT id AS \"Ingredient Id\", name AS \"Name\", expirationdate AS \"Expiration Date\", quantitytarget AS \"Target Quantity\", quantityremaining AS \"Quantity Remaining\", measurementunits AS \"Measurement Units\", priceperunitlastorder AS \"Last Order's Price Per Unit\", lastorderdate AS \"Last Order Date\", unitsinlastorder AS \"Units In Last Order\" FROM ingredients");
        table.setModel(managerView.resultSetToTableModel(null, row));
    }

    /**
     * A function to update the products table
     * @param table the JTable to update
     */
    void updateProductsTable(JTable table) {
        ResultSet row;
        row = managerView.myDbConnection.dbQuery("SELECT id AS \"Product Id\", name AS \"Name\", price AS \"Price\" FROM products");
        table.setModel(managerView.resultSetToTableModel(null, row));
    }
}
