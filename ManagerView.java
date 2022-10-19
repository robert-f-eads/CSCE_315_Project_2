import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;

/**
 * @author Emma Ong, Alexia Hassan, Shreyes Kaliyur
 */
public class ManagerView {
    JFrame myFrame;
    JPanel mainPanel;
    JPanel borderPanel;
    int myRows;
    int myCols;
    dbFunctions myDbConnection;
    int maxWidth = 1500;
    int maxHeight = 1080; 
    serverViewFunctions serverFunctions;

    /**
     * Constructor for a manager view, borrows server functions for faster load time
     * @param serverFunctions the server functions needed to interact with the database
     */
    public ManagerView(serverViewFunctions serverFunctions) {
        myFrame = new JFrame(); // creating instance of JFrame  
        myFrame.setSize(maxWidth, maxHeight);
        myFrame.setPreferredSize(new Dimension(maxWidth, maxHeight));
		myFrame.setMinimumSize(new Dimension(maxWidth, maxHeight));
		myFrame.setMaximumSize(new Dimension(maxWidth, maxHeight));
		myFrame.setResizable(false);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setBackground(Color.black);

        this.serverFunctions = serverFunctions;

        myDbConnection = new dbFunctions();
        myDbConnection.createDbConnection();

        borderPanel = new JPanel();
        mainPanel = new JPanel();

        myFrame.setVisible(true);

        this.setHomeView();
    }

    /**
     * Convert a result set to a table model for display via swing
     * @param model the start of the table model, null if starting from blank
     * @param row the result set to be adding to the table from
     * @return a talbe model which contains a table view of the result set
     */
    DefaultTableModel resultSetToTableModel(DefaultTableModel model, ResultSet row) {

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

    /**
     * Grab information from the database to update a table, typ products or ingredients
     * @param table the table to be updated
     * @param tableName the name of the table in the database to pull information from
     * @param limit the amount of rows to pull from the database
     */
    void updateTable(JTable table, String tableName, int limit) {
        ResultSet row;
        if(limit == -1) {
            row = myDbConnection.dbQuery("SELECT * FROM " + tableName);
        } else {
            row = myDbConnection.dbQuery("SELECT * FROM " + tableName + " LIMIT " + limit);
        }
        table.setModel(resultSetToTableModel(null, row));
    }

    /**
     * Fill in a text field with the corresponding value in the database before sending an update SQL statement
     * @param table the table name in our database to pull information from
     * @param id the id of the element we want to update
     * @param attributeIndex the index of the attribute in our table headers
     * @param attribute the JTextField to be updated
     */
    void setUneditedAttribute(String table, String id, int attributeIndex, HintTextField attribute) {
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

    /**
     * Sets the manager view to the add product screen
     */
    public void setAddView() {
        this.clearView();
        (new ManagerAdd(this)).setAddView();
        myFrame.revalidate();
        myFrame.repaint();
    }

    /**
     * Sets the manager view to the generate trends screen
     */
    public void setTrendView() {
        this.clearView();
        setBorderLayout();
        (new ManagerTrend(this)).setTrendView();
        myFrame.revalidate();
        myFrame.repaint();
    }

    /**
     * Sets the manager view to the get restock report screen
     */
    public void setReorderView() {
        this.clearView();
        setBorderLayout();
        (new ManagerReorder(this)).setReorderView();
        myFrame.revalidate();
        myFrame.repaint();
    }

    /**
     * Sets the manager view to the inventory screen
     */
    public void setInventoryView() {
        this.clearView();
        (new ManagerInventory(this)).setInventoryView();
        myFrame.repaint();
        myFrame.revalidate();
    }

    /**
     * Sets the manager view to the view order history screen
     */
    public void setOrderView() {
        this.clearView();
        setBorderLayout();
        (new ManagerOrder(this)).setOrderView();
        myFrame.revalidate();
        myFrame.repaint();
    }

    /**
     * Sets the manager view to the home screen
     */
    public void setHomeView() {
        this.clearView();
        (new ManagerHome(this)).setHomeView();
        myFrame.revalidate();
        myFrame.repaint();
    }


    /**
     * Creates a button with default syling for consistent display
     * @param text the text the button should have
     * @param width the width of the button
     * @param height the height of the btton
     * @return a JButton element that is styled
     */
    JButton createButton(String text, int width, int height) {
        JButton b = new JButton(text);
        b.setForeground(new Color(165,58,59));
        b.setFont(new Font("SansSerif", Font.PLAIN, 23));
		b.setBackground(Color.white);
		b.setRolloverEnabled(false);
		b.setFocusPainted(false);

        return b;
    }

    /**
     * Creates a label with default styling for consistent display
     * @param text the text the label should have 
     * @param width the width of the label
     * @param height the height of the label
     * @return a JLabel element that is styled
     */
    JLabel createLabel(String text, int width, int height) {
        JLabel l = new JLabel(text);
        l.setSize(width, height);
        return l;
    }

    /**
     * Sets the frame to have a border layout, removing all current content
     */
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
    
    private void clearView() {
        borderPanel.removeAll();
        mainPanel.removeAll();
    }
}
