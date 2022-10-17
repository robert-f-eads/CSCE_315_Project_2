import javax.swing.*;
import java.awt.*;

public class ManagerInventory extends ManagerViewScreen {
    boolean toggleInventory = true;

    public ManagerInventory(ManagerView managerView) {super(managerView);}
    
    public void setInventoryView() {
        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = managerView.createButton("Back", buttonWidth, buttonHeight);
        JButton searchButton = managerView.createButton("Search", buttonWidth, buttonHeight);
        JButton update = managerView.createButton("Update", buttonWidth, buttonHeight);
        JButton add = managerView.createButton("Add", buttonWidth, buttonHeight);
        JButton delete = managerView.createButton("Delete", buttonWidth, buttonHeight);
        JButton toggleTable = managerView.createButton("Switch to Product Table", buttonWidth, buttonHeight);

        JLabel inventory = managerView.createLabel("Inventory", buttonWidth, buttonHeight);
        inventory.setFont(new Font("SansSerif", Font.PLAIN, 28));
        inventory.setBackground(Color.green);
        inventory.setAlignmentX(Component.CENTER_ALIGNMENT);


        HintTextField searchText = (HintTextField) formatTextArea(new HintTextField("Search", buttonWidth, buttonHeight));
        HintTextField id = (HintTextField) formatTextArea(new HintTextField("ID", 200, buttonHeight));

       


        HintTextField[] attributeFields = new HintTextField[8];
        HintTextField name = (HintTextField) formatTextArea(new HintTextField("Name", 250, buttonHeight));
        // name.setPreferredSize(new Dimension(250, buttonHeight));
        // name.setMaximumSize(new Dimension(250, buttonHeight));
        HintTextField expiration = (HintTextField) formatTextArea(new HintTextField("Expiration", 250, buttonHeight));
        HintTextField quantity = (HintTextField) formatTextArea(new HintTextField("Quantity", 100, buttonHeight));
        HintTextField target = (HintTextField) formatTextArea(new HintTextField("Target", 100, buttonHeight));
        HintTextField measurement = (HintTextField) formatTextArea(new HintTextField("Measurement", buttonWidth, buttonHeight));
        HintTextField price = (HintTextField) formatTextArea(new HintTextField("Price", 150, buttonHeight));
        HintTextField last = (HintTextField) formatTextArea(new HintTextField("Last", 250, buttonHeight));
        HintTextField units = (HintTextField) formatTextArea(new HintTextField("Units", 250, buttonHeight));

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

        JTable inventoryTable = new JTable();
        JTable productTable = new JTable();
        managerView.updateTable(inventoryTable, "ingredients", -1);
        managerView.updateTable(productTable, "products", -1);

        back.addActionListener(e -> {
            new ManagerView();
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
            managerView.updateTable(productTable, "products", -1);
            managerView.updateTable(inventoryTable, "ingredients", -1);
        });


        add.addActionListener(e -> {
            String table = tableText.getText();
            String sql = "";
            if(table.equals("products")) {
                sql = "INSERT INTO products VALUES (" + id.getText() + ", '" + name.getText() + "', " + price.getText() + ")";
            } else {
                sql = "INSERT INTO ingredients VALUES (" + id.getText() + ", '" + name.getText() + "', '" + 
                    expiration.getText() + "', " + quantity.getText() + ", " + target.getText() + ", '" + measurement.getText() + "', " + 
                    price.getText() + ", '" + last.getText() + "', " + units.getText() + ")";
            }
            managerView.myDbConnection.dbUpsert(sql);
            managerView.updateTable(productTable, "products", -1);
            managerView.updateTable(inventoryTable, "ingredients", -1);
        });


        delete.addActionListener(e -> {
            String table = tableText.getText();
            String sql = "";
            if(table.equals("products")) {
                sql = "DELETE FROM products WHERE id=" + id.getText();
            } else {
                sql = "DELETE FROM ingredients WHERE id=" + id.getText();
            }
            managerView.myDbConnection.dbUpsert(sql);
            managerView.updateTable(productTable, "products", -1);
            managerView.updateTable(inventoryTable, "ingredients", -1);
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


        JPanel pageStart = new JPanel();
        pageStart.setBackground(Color.white);
        pageStart.setLayout(new BoxLayout(pageStart, BoxLayout.PAGE_AXIS));
        pageStart.setAlignmentX(Component.CENTER_ALIGNMENT);

        pageStart.add(title);
        pageStart.add(Box.createRigidArea(new Dimension(0, 25)));
        pageStart.add(topButtons);

        JPanel south = new JPanel();
        south.setBackground(Color.white);
        south.add(id);
        south.add(name);
        south.add(expiration);
        south.add(quantity);
        south.add(target);
        south.add(measurement);
        south.add(price);
        south.add(last);
        south.add(units);
        south.add(update);
        south.add(add);
        south.add(delete);
        south.add(tableText);


        managerView.borderPanel.add(pageStart, BorderLayout.PAGE_START);
        managerView.borderPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
        managerView.borderPanel.add(south, BorderLayout.SOUTH);


        toggleTable.addActionListener(e -> {
            managerView.borderPanel.removeAll();
            if (toggleInventory) {
                System.out.println("Toggling to product table");
                toggleTable.setText("Switch to Inventory Table");
                tableText.setText("products");
                managerView.borderPanel.add(pageStart, BorderLayout.PAGE_START);
                managerView.borderPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
                managerView.borderPanel.add(south, BorderLayout.SOUTH);
                toggleInventory = false;
            }
            else {
                System.out.println("Toggling to inventory table");
                toggleTable.setText("Switch to Product Table");
                tableText.setText("ingredients");
                managerView.borderPanel.add(pageStart, BorderLayout.PAGE_START);
                managerView.borderPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
                managerView.borderPanel.add(south, BorderLayout.SOUTH);
                toggleInventory = true;
            }
            managerView.borderPanel.revalidate();
            managerView.borderPanel.repaint();
        });
    }

    JTextField formatTextArea(JTextField b) {
        b.setFont(new Font("SansSerif", Font.PLAIN, 23));
        b.setBackground(Color.white);
        b.setMinimumSize(new Dimension(400,75));
        b.setMaximumSize(new Dimension(400,75));
        return b;
    }
    
}
