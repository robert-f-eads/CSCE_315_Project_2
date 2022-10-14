import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.swing.*;
import java.awt.*;

public class ManagerOrder extends ManagerViewScreen {
    public ManagerOrder(ManagerView managerView) {super(managerView);}
    
    public void setOrderView() {
        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = managerView.createButton("Back", buttonWidth, buttonHeight);
        JButton week = managerView.createButton("1 Week", buttonWidth, buttonHeight);
        JButton week2 = managerView.createButton("2 Weeks", buttonWidth, buttonHeight);
        JButton month = managerView.createButton("1 Month", buttonWidth, buttonHeight);
        JButton quarter = managerView.createButton("1 Quarter", buttonWidth, buttonHeight);
        JButton year = managerView.createButton("1 Year", buttonWidth, buttonHeight);
        JButton generate = managerView.createButton("Generate", buttonWidth, buttonHeight);

        JLabel orders = managerView.createLabel("Orders", buttonWidth, buttonHeight);
        JLabel startDateLabel = managerView.createLabel("Start Date", buttonWidth, buttonHeight);
        JLabel endDateLabel = managerView.createLabel("End Date", buttonWidth, buttonHeight);
        HintTextField startDate = new HintTextField("yyyy-mm-dd", buttonWidth, buttonHeight);
        HintTextField endDate = new HintTextField("yyyy-mm-dd", buttonWidth, buttonHeight);

        JTable ordersTable = new JTable();
        ResultSet row = managerView.myDbConnection.dbQuery("SELECT * FROM ordertickets");
        ordersTable.setModel(managerView.resultSetToTableModel(null, row));
        ordersTable.setSize(900, 700);

        back.addActionListener(e -> {
            managerView.setHomeView();
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
            ordersTable.setModel(managerView.resultSetToTableModel(null,
                managerView.myDbConnection.dbQuery("SELECT * FROM ordertickets WHERE timestamp > date '" + startDate.getText() + "' AND timestamp < date '" + endDate.getText() + "' LIMIT 30000")));
            managerView.myFrame.repaint();
            managerView.myFrame.revalidate();
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
        managerView.borderPanel.add(flow, BorderLayout.PAGE_START);
        managerView.borderPanel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);

        managerView.borderPanel.revalidate();
        managerView.borderPanel.repaint();
    }
}
