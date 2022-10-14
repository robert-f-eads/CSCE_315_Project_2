import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ManagerView {
    JFrame myFrame;
    JPanel[][] myPanels;
    JPanel borderPanel;
    int myRows;
    int myCols;
    dbFunctions myDbConnection;

    public ManagerView() {
        myFrame = new JFrame();//creating instance of JFrame  
        myFrame.setSize(1500, 1000);

        myRows = 5;
        myCols = 5;
        myDbConnection = new dbFunctions();
        myDbConnection.createDbConnection();

        myPanels = new JPanel[myRows][myCols];
        for(int i = 0; i < myRows; i++) {
            for(int j = 0; j < myCols; j++) {
                myPanels[i][j] = new JPanel();
            }
        }
        borderPanel = new JPanel();

        this.setHomeView();
    }

    private DefaultTableModel resultSetToTableModel(DefaultTableModel model, ResultSet row) {

        ResultSetMetaData meta;
        try {
            meta = row.getMetaData();
            if(model==null) model= new DefaultTableModel();

            String cols[]=new String[meta.getColumnCount()];
            for(int i=0;i< cols.length;++i) {
                cols[i]= meta.getColumnLabel(i+1);
            }

            model.setColumnIdentifiers(cols);

            while(row.next()) {
                Object data[]= new Object[cols.length];
                for(int i=0;i< data.length;++i) {
                    data[i]=row.getObject(i+1);
                }
                model.addRow(data);
            }
            return model;
        } catch (SQLException e) {
            System.out.println("Error retrieving stuff from database ----------");
            e.printStackTrace();
        }
        return null;
    }

    public void setTrendView() {
        this.clearView();
        setGridLayout();

        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = createButton("Back", buttonWidth, buttonHeight);
        JButton week = createButton("1 Week", buttonWidth, buttonHeight);
        JButton week2 = createButton("2 Weeks", buttonWidth, buttonHeight);
        JButton month = createButton("1 Month", buttonWidth, buttonHeight);
        JButton quarter = createButton("1 Quarter", buttonWidth, buttonHeight);
        JButton year = createButton("1 Year", buttonWidth, buttonHeight);
        JButton generate = createButton("Generate", buttonWidth, buttonHeight);

        JLabel trends = createLabel("Trends", buttonWidth, buttonHeight);
        JLabel startDateLabel = createLabel("Start Date", buttonWidth, buttonHeight);
        JLabel endDateLabel = createLabel("End Date", buttonWidth, buttonHeight);
        HintTextField startDate = new HintTextField("yyyy-mm-dd", buttonWidth, buttonHeight);
        HintTextField endDate = new HintTextField("yyyy-mm-dd", buttonWidth, buttonHeight);

        back.addActionListener(e -> {
            this.setHomeView();
        });
        generate.addActionListener(e -> {
            System.out.println("Generate trends");
        });

        myPanels[0][0].add(back);
        myPanels[0][2].add(trends);
        myPanels[1][0].add(startDateLabel);
        myPanels[1][2].add(startDate);
        myPanels[2][0].add(endDateLabel);
        myPanels[2][2].add(endDate);
        myPanels[2][4].add(generate);
        myPanels[3][0].add(week);
        myPanels[3][1].add(week2);
        myPanels[3][2].add(month);
        myPanels[3][3].add(quarter);
        myPanels[3][4].add(year);

        myFrame.repaint();
    }

    public void setReorderView() {
        this.clearView();
        setGridLayout();

        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = createButton("Back", buttonWidth, buttonHeight);
        JButton export = createButton("Export to CSV", buttonWidth, buttonHeight);

        JLabel reorder = createLabel("Inventory", buttonWidth, buttonHeight);

        back.addActionListener(e -> {
            this.setHomeView();
        });
        export.addActionListener(e -> {
            System.out.println("Export to csv");
        });

        myPanels[0][0].add(back);
        myPanels[0][2].add(reorder);
        myPanels[4][4].add(export);

        myFrame.repaint();
    }

    private void setUneditedAttribute(String table, String id, int attributeIndex, HintTextField attribute) {
        String sql = "SELECT * FROM " + table + " WHERE id=" + id;
        ResultSet row = myDbConnection.dbQuery(sql);
        ResultSetMetaData meta;
        try {
            meta = row.getMetaData();
            while(row.next()) {
                for(int i = 0; i < meta.getColumnCount(); i++) {
                    if(i == attributeIndex) {
                        // row naturally indexes from 0
                        attribute.setText(row.getObject(i + 1) + "");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTable(JTable table, String tableName, int limit) {
        ResultSet row;
        if(limit == -1) {
            row = myDbConnection.dbQuery("SELECT * FROM " + tableName);
        } else {
            row = myDbConnection.dbQuery("SELECT * FROM " + tableName + " LIMIT " + limit);
        }
        table.setModel(resultSetToTableModel(null, row));
    }

    public void setInventoryView() {
        this.clearView();
        setBorderLayout();

        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = createButton("Back", buttonWidth, buttonHeight);
        JButton searchButton = createButton("Search", buttonWidth, buttonHeight);
        JButton update = createButton("Update", buttonWidth, buttonHeight);
        JButton add = createButton("Add", buttonWidth, buttonHeight);
        JButton delete = createButton("Delete", buttonWidth, buttonHeight);

        JLabel inventory = createLabel("Inventory", buttonWidth, buttonHeight);

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

        HintTextField tableText = new HintTextField("Table", buttonWidth, buttonHeight);

        JTable inventoryTable = new JTable();
        JTable productTable = new JTable();
        updateTable(inventoryTable, "ingredients", -1);
        updateTable(productTable, "products", -1);

        back.addActionListener(e -> {
            this.setHomeView();
        });
        searchButton.addActionListener(e -> {
            productTable.setModel(resultSetToTableModel(null,
                myDbConnection.dbQuery("SELECT * FROM products WHERE name ILIKE '%" + searchText.getText() + "%'")));
            inventoryTable.setModel(resultSetToTableModel(null,
                myDbConnection.dbQuery("SELECT * FROM ingredients WHERE name ILIKE '%" + searchText.getText() + "%'")));
            myFrame.repaint();
            myFrame.revalidate();
        });
        update.addActionListener(e -> {
            String table = tableText.getText();
            for(int i = 0; i < attributeFields.length; i++) {
                HintTextField attributeField = attributeFields[i];
                if(attributeField.getText().equals(attributeField.getHint())) {
                    // we add one to i because we do not include id as an attribute
                    setUneditedAttribute(table, id.getText(), i + 1, attributeField);
                }
            }
            String sql = "";
            if(table.equals("products")) {
                sql = "UPDATE products SET name='" + name.getText() + "', price=" + price.getText() + " WHERE id=" + id.getText();
            } else {
                System.out.println("update ingredients");
                sql = "UPDATE ingredients SET name='" + name.getText() + "', expirationdate= date'" + expiration.getText() + 
                    "', measurementunits='" + measurement.getText() + "', lastorderdate= date'" + last.getText() + "', priceperunitlastorder=" + price.getText() + ", quantityRemaining=" + 
                    quantity.getText() + ", unitsinlastorder=" + units.getText() +
                    "WHERE id=" + id.getText();
            }
            for(HintTextField attributeField : attributeFields) {
                attributeField.resetText();
            }
            myDbConnection.dbUpsert(sql);
            updateTable(productTable, "products", -1);
            updateTable(inventoryTable, "ingredients", -1);
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
            myDbConnection.dbUpsert(sql);
            updateTable(productTable, "products", -1);
            updateTable(inventoryTable, "ingredients", -1);
        });
        delete.addActionListener(e -> {
            String table = tableText.getText();
            String sql = "";
            if(table.equals("products")) {
                sql = "DELETE FROM products WHERE id=" + id.getText();
            } else {
                sql = "DELETE FROM ingredients WHERE id=" + id.getText();
            }
            myDbConnection.dbUpsert(sql);
            updateTable(productTable, "products", -1);
            updateTable(inventoryTable, "ingredients", -1);
        });

        JPanel pageStart = new JPanel();
        pageStart.add(back);
        pageStart.add(inventory);
        pageStart.add(searchText);
        pageStart.add(searchButton);
        borderPanel.add(pageStart, BorderLayout.PAGE_START);
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
        borderPanel.add(south, BorderLayout.SOUTH);
        borderPanel.add(new JScrollPane(inventoryTable), BorderLayout.WEST);
        borderPanel.add(new JScrollPane(productTable), BorderLayout.EAST);

        // pageStart.revalidate();
        // pageStart.repaint();
        // borderPanel.revalidate();
        // borderPanel.repaint();
        myFrame.repaint();
        myFrame.revalidate();
    }

    public void setOrderView() {
        this.clearView();
        setBorderLayout();

        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = createButton("Back", buttonWidth, buttonHeight);
        JButton week = createButton("1 Week", buttonWidth, buttonHeight);
        JButton week2 = createButton("2 Weeks", buttonWidth, buttonHeight);
        JButton month = createButton("1 Month", buttonWidth, buttonHeight);
        JButton quarter = createButton("1 Quarter", buttonWidth, buttonHeight);
        JButton year = createButton("1 Year", buttonWidth, buttonHeight);
        JButton generate = createButton("Generate", buttonWidth, buttonHeight);

        JLabel orders = createLabel("Orders", buttonWidth, buttonHeight);
        JLabel startDateLabel = createLabel("Start Date", buttonWidth, buttonHeight);
        JLabel endDateLabel = createLabel("End Date", buttonWidth, buttonHeight);
        HintTextField startDate = new HintTextField("yyyy-mm-dd", buttonWidth, buttonHeight);
        HintTextField endDate = new HintTextField("yyyy-mm-dd", buttonWidth, buttonHeight);

        JTable ordersTable = new JTable();
        ResultSet row = myDbConnection.dbQuery("SELECT * FROM ordertickets");
        ordersTable.setModel(resultSetToTableModel(null, row));
        ordersTable.setSize(900, 700);

        back.addActionListener(e -> {
            this.setHomeView();
        });
        week.addActionListener(e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
            LocalDateTime now = LocalDateTime.now();  
            LocalDateTime weekAgo = now.minus(1, ChronoUnit.WEEKS);
            startDate.setText(dtf.format(weekAgo));
            endDate.setText(dtf.format(now));
        });
        week2.addActionListener(e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
            LocalDateTime now = LocalDateTime.now();  
            LocalDateTime week2Ago = now.minus(2, ChronoUnit.WEEKS);
            startDate.setText(dtf.format(week2Ago));
            endDate.setText(dtf.format(now));
        });
        month.addActionListener(e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
            LocalDateTime now = LocalDateTime.now();  
            LocalDateTime monthAgo = now.minus(1, ChronoUnit.MONTHS);
            startDate.setText(dtf.format(monthAgo));
            endDate.setText(dtf.format(now));
        });
        year.addActionListener(e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
            LocalDateTime now = LocalDateTime.now();  
            LocalDateTime yearAgo = now.minus(1, ChronoUnit.YEARS);
            startDate.setText(dtf.format(yearAgo));
            endDate.setText(dtf.format(now));
        });
        quarter.addActionListener(e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
            LocalDateTime now = LocalDateTime.now();  
            LocalDateTime quarterAgo = now.minus(3, ChronoUnit.MONTHS);
            startDate.setText(dtf.format(quarterAgo));
            endDate.setText(dtf.format(now));
        });
        generate.addActionListener(e -> {
            ordersTable.setModel(resultSetToTableModel(null,
                myDbConnection.dbQuery("SELECT * FROM ordertickets WHERE timestamp > date '" + startDate.getText() + "' AND timestamp < date '" + endDate.getText() + "' LIMIT 30000")));
            myFrame.repaint();
            myFrame.revalidate();
            System.out.println("Generate order history");
        });

        JPanel flow = new JPanel();
        flow.add(back);
        flow.add(orders);
        flow.add(startDateLabel);
        flow.add(startDate);
        flow.add(endDateLabel);
        flow.add(endDate);
        flow.add(generate);
        flow.add(week);
        flow.add(week2);
        flow.add(month);
        flow.add(quarter);
        flow.add(year);
        borderPanel.add(flow, BorderLayout.PAGE_START);
        borderPanel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);

        borderPanel.revalidate();
        borderPanel.repaint();
        myFrame.revalidate();
        myFrame.repaint();
    }

    public void setHomeView() {
        this.clearView();
        this.setGridLayout();

        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = createButton("Back", buttonWidth, buttonHeight);
        JButton serverView = createButton("Server View", buttonWidth, buttonHeight);
        JButton orderHistory = createButton("Order History", buttonWidth, buttonHeight);
        JButton inventoryStatus = createButton("Inventory Status", buttonWidth, buttonHeight);
        JButton reorderStatement = createButton("Generate Reorder Statement", buttonWidth, buttonHeight);
        JButton generateTrends = createButton("Generate Trends", buttonWidth, buttonHeight);

        JLabel smoothieKing = createLabel("Smoothie King", buttonWidth, buttonHeight);

        serverView.addActionListener(e -> {
            System.out.println("Switch to server view");
        });
        orderHistory.addActionListener(e -> {
            this.setOrderView();
        });
        inventoryStatus.addActionListener(e -> {
            this.setInventoryView();
        });
        reorderStatement.addActionListener(e -> {
            this.setReorderView();
        });
        generateTrends.addActionListener(e -> {
            this.setTrendView();
        });
                
        myPanels[0][0].add(back);
        myPanels[0][2].add(smoothieKing);
        myPanels[0][4].add(serverView);
        myPanels[2][1].add(orderHistory);
        myPanels[2][3].add(inventoryStatus);
        myPanels[4][1].add(reorderStatement);
        myPanels[4][3].add(generateTrends);

        myFrame.repaint();
    }


    private JButton createButton(String text, int width, int height) {
        JButton b = new JButton(text);
        b.setSize(width, height);
        return b;
    }

    private JLabel createLabel(String text, int width, int height) {
        JLabel l = new JLabel(text);
        l.setSize(width, height);
        return l;
    }

    private void setGridLayout() {
        myFrame.getContentPane().removeAll();;
        myFrame.setLayout(new GridLayout(myRows, myCols));
        myPanels = new JPanel[myRows][myCols];
        for(int i = 0; i < myRows; i++) {
            for(int j = 0; j < myCols; j++) {
                myPanels[i][j] = new JPanel();
                myFrame.add(myPanels[i][j]);
            }
        }
        myFrame.revalidate();
        myFrame.repaint();
        myFrame.setVisible(true);
    }

    private void setBorderLayout() {
        myFrame.getContentPane().removeAll();;
        borderPanel = new JPanel(new BorderLayout(10, 10));
        borderPanel.setSize(1000, 1000);
        borderPanel.invalidate();
        borderPanel.revalidate();
        myFrame.add(borderPanel);
        myFrame.revalidate();
        myFrame.repaint();
        myFrame.setVisible(true);
    }
    
    // clears both gridlayout and borderlayout
    private void clearView() {
        for(int i = 0; i < myCols; i++) {
            for(int j = 0; j < myCols; j++) {
                myPanels[i][j].removeAll();
            }
        }
        borderPanel.removeAll();
    }
}
