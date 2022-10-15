import java.awt.Component;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.*;
import  java.text.DecimalFormat;
import java.util.List;

public class ManagerHome extends ManagerViewScreen {
    public ManagerHome(ManagerView managerView) {super(managerView);}
	static Border line = new LineBorder(Color.black);
    static Font defaultButtons = new Font("SansSerif", Font.PLAIN, 28); //font used in text box
    static Color darkRed = new Color(165,58,59);
    int maxHeight = 1000;


    public void setHomeView() {
       
        JButton orderHistory = new JButton("Order History");
        orderHistory.setForeground(Color.black);
        orderHistory.setFont(defaultButtons);
		orderHistory.setBackground(Color.white);
		orderHistory.setRolloverEnabled(false);
		orderHistory.setFocusPainted(false);
        orderHistory.setPreferredSize(new Dimension(975,75));
        orderHistory.setMinimumSize(new Dimension(975,75));
        orderHistory.setMaximumSize(new Dimension(975,75));


        JButton inventoryStatus = new JButton("Inventory Status");
        inventoryStatus.setForeground(Color.black);
        inventoryStatus.setFont(defaultButtons);
		inventoryStatus.setBackground(Color.white);
		inventoryStatus.setRolloverEnabled(false);
		inventoryStatus.setFocusPainted(false);
        inventoryStatus.setPreferredSize(new Dimension(975,75));
        inventoryStatus.setMinimumSize(new Dimension(975,75));
        inventoryStatus.setMaximumSize(new Dimension(975,75));


        JButton reorderStatement = new JButton("Generate Reorder Statement");
        reorderStatement.setForeground(Color.black);
        reorderStatement.setFont(defaultButtons);
		reorderStatement.setBackground(Color.white);
		reorderStatement.setRolloverEnabled(false);
		reorderStatement.setFocusPainted(false);
        reorderStatement.setPreferredSize(new Dimension(975,75));
        reorderStatement.setMinimumSize(new Dimension(975,75));
        reorderStatement.setMaximumSize(new Dimension(975,75));


        JButton generateTrends = new JButton("Generate Trends");
        generateTrends.setForeground(Color.black);
        generateTrends.setFont(defaultButtons);
		generateTrends.setBackground(Color.white);
		generateTrends.setRolloverEnabled(false);
		generateTrends.setFocusPainted(false);
        generateTrends.setPreferredSize(new Dimension(975,75));
        generateTrends.setMinimumSize(new Dimension(975,75));
        generateTrends.setMaximumSize(new Dimension(975,75));


        managerView.mainPanel.setLayout(null);

        
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


        JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.white);
		mainPanel.setBounds(13, 150, 975, 800);
		mainPanel.setBorder(line);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        mainPanel.add(orderHistory);
        mainPanel.add(inventoryStatus);
        mainPanel.add(reorderStatement);
        mainPanel.add(generateTrends);


		//Logo panel will house Smoothie King logo in top left 
		JPanel logoPanel = new JPanel();
		logoPanel.setBackground(Color.white);
		logoPanel.setBounds(0, 0, 975, 75);
		logoPanel.setLayout(new BorderLayout());

		//Create Label as icon and add it to the logo panel
		JLabel picLabel = new JLabel();
		picLabel.setIcon(new ImageIcon(new ImageIcon("logo.png").getImage().getScaledInstance(400, 70, Image.SCALE_SMOOTH)));
		logoPanel.add(picLabel);

		
		//Search panel will house static search bar in top left
		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(Color.white);
		searchPanel.setBounds(13, 87, 975, 50);
		searchPanel.setLayout(new BorderLayout());

		//Create text component of search bar
		JTextField searchTextField = new JTextField();
		searchTextField.setEditable(false);
		searchTextField.setPreferredSize(new Dimension(925,50));
		searchTextField.setHorizontalAlignment(JTextField.LEFT);
		Font searchFont = new Font("SansSerif", Font.PLAIN, 20); //font used in text box
		searchTextField.setFont(searchFont);
        searchTextField.setEditable(false);

		//Create button component of search bar
		JButton searchButton = new JButton(new ImageIcon(((new ImageIcon("searchIcon.png")).getImage()).getScaledInstance(43, 43, java.awt.Image.SCALE_SMOOTH)));
		searchButton.setPreferredSize(new Dimension(50,50));


		//Set searchButton colors
		searchButton.setForeground(Color.black);
		searchButton.setBackground(Color.white);
		searchButton.setRolloverEnabled(false);
		searchButton.setFocusPainted(false);


		//Set border colors for search bar and search button
		searchTextField.setBorder(BorderFactory.createCompoundBorder(line, BorderFactory.createEmptyBorder(0, 5, 0, 0)));
		searchButton.setBorder(line);

		//Add both search bar and search button to panel in appropriate place 
		searchPanel.add(searchButton, BorderLayout.LINE_START);
		searchPanel.add(searchTextField, BorderLayout.LINE_END);


        JButton serverViewButton = new JButton("   Server View   ");
        serverViewButton.setFont(defaultButtons);
        serverViewButton.setBackground(Color.white);
        serverViewButton.setForeground(darkRed);
        serverViewButton.setRolloverEnabled(false);
        serverViewButton.setFocusPainted(false);
        serverViewButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, darkRed),  BorderFactory.createEmptyBorder(15, 8, 15, 10)));
        serverViewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        serverViewButton.addActionListener(e -> {
            new GUIWindow();
            managerView.myFrame.dispose();
        });

		
		
		//Create left panel to house logo, search, and all other functionalities
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.white);
		leftPanel.setBounds(0, 0, 1050, maxHeight);
		leftPanel.setLayout(null);


		//Adding logo, search , and main panel to left panel
		leftPanel.add(logoPanel);
		leftPanel.add(searchPanel);
		leftPanel.add(mainPanel);


        JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.white);
		rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));		
		rightPanel.setBounds(1050, 0, 450, maxHeight);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

        rightPanel.add(serverViewButton);

        //Adding right and left panel to main frame
        managerView.mainPanel.add(leftPanel);
        managerView.mainPanel.add(rightPanel);

        
    }
    
}
