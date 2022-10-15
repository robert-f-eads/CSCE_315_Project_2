import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;

public class ManagerView {
    JFrame myFrame;
    JPanel[][] myPanels;
    JPanel mainPanel;
    JPanel borderPanel;
    int myRows;
    int myCols;
    dbFunctions myDbConnection;
    int maxWidth = 1500;
    int maxHeight = 1080;
    public ManagerView() {
        myFrame = new JFrame();//creating instance of JFrame  
        //myFrame.setSize(1500, 1080);
        myFrame.setPreferredSize(new Dimension(maxWidth, maxHeight));
		myFrame.setMinimumSize(new Dimension(maxWidth, maxHeight));
		myFrame.setMaximumSize(new Dimension(maxWidth, maxHeight));
		myFrame.setResizable(false);


       /* myRows = 5;
        myCols = 5;

        myPanels = new JPanel[myRows][myCols];
        for(int i = 0; i < myRows; i++) {
            for(int j = 0; j < myCols; j++) {
                myPanels[i][j] = new JPanel();
            }
        }*/

        myDbConnection = new dbFunctions();
        myDbConnection.createDbConnection();

        borderPanel = new JPanel();

        mainPanel = new JPanel();
        myFrame.add(mainPanel);
        myFrame.setVisible(true);

        
        this.setHomeView();
    }

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

    void updateTable(JTable table, String tableName, int limit) {
        ResultSet row;
        if(limit == -1) {
            row = myDbConnection.dbQuery("SELECT * FROM " + tableName);
        } else {
            row = myDbConnection.dbQuery("SELECT * FROM " + tableName + " LIMIT " + limit);
        }
        table.setModel(resultSetToTableModel(null, row));
    }

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


    public void setTrendView() {
        this.clearView();
        setGridLayout();
        (new ManagerTrend(this)).setTrendView();
        myFrame.repaint();
    }

    public void setReorderView() {
        this.clearView();
        setGridLayout();
        (new ManagerReorder(this)).setReorderView();
        myFrame.repaint();
    }

    public void setInventoryView() {
        this.clearView();
        setBorderLayout();
        (new ManagerInventory(this)).setInventoryView();
        myFrame.repaint();
        myFrame.revalidate();
    }

    public void setOrderView() {
        this.clearView();
        setBorderLayout();
        (new ManagerOrder(this)).setOrderView();
        myFrame.revalidate();
        myFrame.repaint();
    }

    public void setHomeView() {
        this.clearView();
        //this.setGridBoxLayout();
        // this.setGridLayout();
        (new ManagerHome(this)).setHomeView();
        myFrame.revalidate();
        myFrame.repaint();
    }


    JButton createButton(String text, int width, int height) {
        JButton b = new JButton(text);
        b.setSize(width, height);
        return b;
    }

    JLabel createLabel(String text, int width, int height) {
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
                myPanels[i][j].setBackground(new Color((int) (Math.random() * 100)));
                myFrame.add(myPanels[i][j]);
            }
        }
        myFrame.revalidate();
        myFrame.repaint();
        myFrame.setVisible(true);
    }

    private void setGridBoxLayout() {
        myFrame.getContentPane().removeAll();
        // myFrame.setLayout(new GridBagLayout());
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setSize(1000,1000);
        mainPanel.invalidate();
        mainPanel.revalidate();
        myFrame.add(mainPanel);
        
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
        /*for(int i = 0; i < myCols; i++) {
            for(int j = 0; j < myCols; j++) {
                myPanels[i][j].removeAll();
            }
        }*/
        borderPanel.removeAll();
        mainPanel.removeAll();
        mainPanel.revalidate();
    }
}
