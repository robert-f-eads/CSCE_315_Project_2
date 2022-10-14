import java.awt.Component;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;

public class ManagerHome extends ManagerViewScreen {
    public ManagerHome(ManagerView managerView) {super(managerView);}

    public void setHomeView() {
        // int buttonWidth = 150;
        // int buttonHeight = 150;
        // JButton serverView = managerView.createButton("Server View", buttonWidth, buttonHeight);
        // JButton orderHistory = managerView.createButton("Order History", buttonWidth, buttonHeight);
        // JButton inventoryStatus = managerView.createButton("Inventory Status", buttonWidth, buttonHeight);
        // JButton reorderStatement = managerView.createButton("Generate Reorder Statement", buttonWidth, buttonHeight);
        // JButton generateTrends = managerView.createButton("Generate Trends", buttonWidth, buttonHeight);

        // JLabel smoothieKing = managerView.createLabel("Smoothie King", buttonWidth, buttonHeight);

        JPanel gridBoxPanel = new JPanel();
        JButton serverView = new JButton("Server View");
        JButton orderHistory = new JButton("Order History");
        JButton inventoryStatus = new JButton("Inventory Status");
        JButton reorderStatement = new JButton("Generate Reorder Statement");
        JButton generateTrends = new JButton("Generate Trends");

        // managerView.mainPanel.add(serverView);

        gridBoxPanel.setSize(400,400);

        gridBoxPanel.setLayout(new GridLayout(3,2));
        gridBoxPanel.add(serverView);
        gridBoxPanel.add(orderHistory);
        gridBoxPanel.add(inventoryStatus);
        gridBoxPanel.add(reorderStatement);
        gridBoxPanel.add(generateTrends);

        managerView.mainPanel.add(gridBoxPanel);

        serverView.addActionListener(e -> {
            new GUIWindow();
            managerView.myFrame.dispose();
        });
        orderHistory.addActionListener(e -> {
            managerView.setOrderView();
        });
        inventoryStatus.addActionListener(e -> {
            managerView.setInventoryView();
        });
        reorderStatement.addActionListener(e -> {
            managerView.setReorderView();
        });
        generateTrends.addActionListener(e -> {
            managerView.setTrendView();
        });
                
        // managerView.myPanels[0][2].add(smoothieKing);
        // managerView.myPanels[0][4].add(serverView);
        // managerView.myPanels[2][1].add(orderHistory);
        // managerView.myPanels[2][3].add(inventoryStatus);
        // managerView.myPanels[4][1].add(reorderStatement);
        // managerView.myPanels[4][3].add(generateTrends);
    }
    
}
