import javax.swing.*;
import java.awt.*;

public class ManagerInventory extends ManagerViewScreen {
    public ManagerInventory(ManagerView managerView) {super(managerView);}
    
    public void setInventoryView() {
        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = managerView.createButton("Back", buttonWidth, buttonHeight);
        JButton searchButton = managerView.createButton("Search", buttonWidth, buttonHeight);
        JButton update = managerView.createButton("Update", buttonWidth, buttonHeight);
        JButton add = managerView.createButton("Add", buttonWidth, buttonHeight);
        JButton delete = managerView.createButton("Delete", buttonWidth, buttonHeight);
        JButton toggleTable = managerView.createButton("Toggle Table", buttonWidth, buttonHeight);

        JLabel inventory = managerView.createLabel("Inventory", buttonWidth, buttonHeight);

        HintTextField searchText = new HintTextField("Search", buttonWidth, buttonHeight);
        HintTextField id = new HintTextField("Id", buttonWidth, buttonHeight);

        HintTextField[] attributeFields = new HintTextField[7];
        HintTextField name = new HintTextField("name", buttonWidth, buttonHeight);
        HintTextField expiration = new HintTextField("expiration", buttonWidth, buttonHeight);
        HintTextField quantity = new HintTextField("Quantity", buttonWidth, buttonHeight);
        HintTextField measurement = new HintTextField("Measurement", buttonWidth, buttonHeight);
        HintTextField price = new HintTextField("Price", buttonWidth, buttonHeight);
        HintTextField last = new HintTextField("Last", buttonWidth, buttonHeight);
        HintTextField units = new HintTextField("Units", buttonWidth, buttonHeight);
        attributeFields[0] = name;
        attributeFields[1] = expiration;
        attributeFields[2] = quantity;
        attributeFields[3] = measurement;
        attributeFields[4] = price;
        attributeFields[5] = last;
        attributeFields[6] = units;

        HintTextField tableText = new HintTextField("ingredients", buttonWidth, buttonHeight);

        JTable inventoryTable = new JTable();
        JTable productTable = new JTable();
        managerView.updateTable(inventoryTable, "ingredients", -1);
        managerView.updateTable(productTable, "products", -1);

        back.addActionListener(e -> {
            managerView.setHomeView();
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
                    "', measurementunits='" + measurement.getText() + "', lastorderdate= date'" + last.getText() + "', priceperunitlastorder=" + price.getText() + ", quantityRemaining=" + 
                    quantity.getText() + ", unitsinlastorder=" + units.getText() +
                    "WHERE id=" + id.getText();
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
                    expiration.getText() + "', " + quantity.getText() + ", '" + measurement.getText() + "', " + 
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

        JPanel pageStart = new JPanel();
        pageStart.add(back);
        pageStart.add(inventory);
        pageStart.add(searchText);
        pageStart.add(searchButton);
        managerView.borderPanel.add(pageStart, BorderLayout.PAGE_START);
        JPanel south = new JPanel();
        south.add(id);
        south.add(name);
        south.add(expiration);
        south.add(quantity);
        south.add(measurement);
        south.add(price);
        south.add(last);
        south.add(units);
        south.add(update);
        south.add(add);
        south.add(delete);
        south.add(tableText);
        managerView.borderPanel.add(south, BorderLayout.SOUTH);
        managerView.borderPanel.add(new JScrollPane(inventoryTable), BorderLayout.WEST);
        managerView.borderPanel.add(new JScrollPane(productTable), BorderLayout.EAST);
    }
}
