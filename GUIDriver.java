
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.*;


public class GUIDriver {
	public static void main(String args[]) {
		GUIWindow newGUIWindow = new GUIWindow();
		newGUIWindow.updateMainView();
		//newGUIWindow.updateTileView("Good morning");
	}
}

 class GUIWindow {
	static int maxHeight = 1080;
	static int maxWidth = 1500;
	static Color darkRed = new Color(165,58,59);
	static Border line = new LineBorder(Color.black);
	static Font defaultButtons = new Font("SansSerif", Font.PLAIN, 28); //font used in text box
	JPanel mainPanel;
	String currentTextFieldEntry;

	boolean currentPageIsMain;
	boolean currentPageIsTile;
	boolean currentPageIsModifications;

	public void updateTileView(String searchBarText) {
		mainPanel.removeAll();
		mainPanel.setLayout(new FlowLayout());
		

		try {
			serverViewFunctions serverFunctions = new serverViewFunctions();
			dbFunctions dbConnection = new dbFunctions();
			dbConnection.createDbConnection();

			String sqlStatement = String.format("SELECT id FROM products WHERE name ILIKE '%%s%' ", searchBarText)
			ResultSet results = dbConnection.dbQuery(sqlStatement);
			while(results.next()) {
				product tempProduct = serverFunctions.getProduct(results.getInt("id")); 
				TilePanel tempPanel = new TilePanel(tempProduct.getName());
				mainPanel.add(tempPanel.mainPanel);
			}
		} catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 

		//Use search string to display results etc
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	public void updateMainView() {
		mainPanel.removeAll();


		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(Box.createRigidArea(new Dimension(0,10)));

		
		JPanel buttonPanel = new JPanel(); //will house buttons dedicated to the main view 
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBackground(Color.white);

		JButton duplicate = new JButton("Duplicate");
		duplicate.setBackground(darkRed);
		duplicate.setFont(defaultButtons);
		duplicate.setForeground(Color.white);
		duplicate.setRolloverEnabled(false);
		duplicate.setFocusPainted(false);

		JButton edit = new JButton("Edit");
		edit.setBackground(darkRed);
		edit.setFont(defaultButtons);
		edit.setForeground(Color.white);
		edit.setRolloverEnabled(false);
		edit.setFocusPainted(false);

	
		JButton removeItem = new JButton("Remove Item");
		removeItem.setBackground(darkRed);
		removeItem.setFont(defaultButtons);
		removeItem.setForeground(Color.white);
		removeItem.setRolloverEnabled(false);
		removeItem.setFocusPainted(false);


		buttonPanel.add(removeItem);

		buttonPanel.add(Box.createRigidArea(new Dimension(590,5)));
		buttonPanel.add(edit);
		buttonPanel.add(Box.createRigidArea(new Dimension(30,5)));
		buttonPanel.add(duplicate);


		mainPanel.add(buttonPanel);

		//ABOVE IS JUST BUTTONS//
		
		orderTicketInfo current_ticket = new orderTicketInfo();
		for(orderItem item : current_ticket.getOrderItems()){
			ItemInOrder tempItem = new ItemInOrder(item);
			mainPanel.add(tempItem.mainPanel); //adjust gbc
		}


		//ItemInOrder testItem = new ItemInOrder("pumpkin Whatever");
		//mainPanel.add(testItem.mainPanel);

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	public void resetMainPanel() {
		mainPanel.removeAll();
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	public void stateMachine() {
		//defaults to main page
		//if (text is entered and the search button is pressed) and currentPage is Main
			//update tile part

		//if (on tile click and currentPage is tile)
			//go to modifications
		
		//if (on modification exit and currentPage is modification)
			// go back to main view
	}

	public GUIWindow() {
		//Create JFrame and initial settings
		JFrame frame = new JFrame("Smoothie King");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(maxWidth, maxHeight));
		frame.setMinimumSize(new Dimension(maxWidth, maxHeight));
		frame.setMaximumSize(new Dimension(maxWidth, maxHeight));
		frame.setResizable(false);
		frame.setLayout(null);

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.white);
		mainPanel.setBounds(13, 150, 1100, 800);
		mainPanel.setBorder(line);

		//Logo panel will house Smoothie King logo in top left 
		JPanel logoPanel = new JPanel();
		logoPanel.setBackground(Color.white);
		logoPanel.setBounds(0, 0, 1100, 75);
		logoPanel.setLayout(new BorderLayout());

		//Create Label as icon and add it to the logo panel
		JLabel picLabel = new JLabel();
		picLabel.setIcon(new ImageIcon(new ImageIcon("logo.png").getImage().getScaledInstance(400, 70, Image.SCALE_SMOOTH)));
		logoPanel.add(picLabel);

		
		//Search panel will house static search bar in top left
		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(Color.orange);
		searchPanel.setBounds(13, 87, 1100, 50);
		searchPanel.setLayout(new BorderLayout());

		//Create text component of search bar
		JTextField searchTextField = new JTextField();
		searchTextField.setPreferredSize(new Dimension(1050,50));
		searchTextField.setHorizontalAlignment(JTextField.LEFT);
		//searchTextField.setLocation(5, 75);
		Font searchFont = new Font("SansSerif", Font.PLAIN, 20); //font used in text box
		searchTextField.setFont(searchFont);

		//Create button component of search bar
		JButton searchButton = new JButton(new ImageIcon(((new ImageIcon("searchIcon.png")).getImage()).getScaledInstance(43, 43, java.awt.Image.SCALE_SMOOTH)));
		searchButton.setPreferredSize(new Dimension(50,50));
		searchButton.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					currentTextFieldEntry = searchTextField.getText();
					updateTileView(currentTextFieldEntry);
				}
			}
		);

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


		//Creating server name label
		JLabel serverName = new JLabel("Server Name"); //pull from database
		serverName.setHorizontalAlignment(SwingConstants.CENTER);
		serverName.setAlignmentX(Component.CENTER_ALIGNMENT);


		//Creating server font
		Font serverNameFont = new Font("SansSerif", Font.BOLD, 35); //font used in text box
		serverName.setFont(serverNameFont);


		//Creating Logout Button
		JButton logout = new JButton("      Logout      ");
		//logout.setBounds(32, 50, 150, 75);
		logout.setFont(defaultButtons);
		logout.setBackground(Color.white);
		logout.setForeground(darkRed);
		logout.setRolloverEnabled(false);
		logout.setFocusPainted(false);
		logout.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed));	
		logout.setAlignmentX(Component.CENTER_ALIGNMENT);



		JButton cancel = new JButton("      Cancel      ");
		//cancel.setBounds(32, 50, 150, 75);
		cancel.setFont(defaultButtons);
		cancel.setBackground(Color.white);
		cancel.setForeground(darkRed);
		cancel.setRolloverEnabled(false);
		cancel.setFocusPainted(false);
		cancel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, darkRed));	
		cancel.setAlignmentX(Component.CENTER_ALIGNMENT);


		JButton finishAndPay = new JButton("Finish and Pay");
		//finishAndPay.setBounds(32, 50, 150, 75);
		finishAndPay.setFont(defaultButtons);
		finishAndPay.setBackground(Color.white);
		finishAndPay.setForeground(darkRed);
		finishAndPay.setRolloverEnabled(false);
		finishAndPay.setFocusPainted(false);
		finishAndPay.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed));	
		finishAndPay.setAlignmentX(Component.CENTER_ALIGNMENT);


		//Create right panel to house side buttons
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.white);
		rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));		
		rightPanel.setBounds(1150, 0, 350, maxHeight);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

		rightPanel.add(serverName);
		rightPanel.add(Box.createRigidArea(new Dimension(0,5)));
		rightPanel.add(logout);
		rightPanel.add(Box.createRigidArea(new Dimension(0,750)));
		rightPanel.add(cancel);
		rightPanel.add(Box.createRigidArea(new Dimension(0,5)));
		rightPanel.add(finishAndPay);

		
		//Create left panel to house logo, search, and all other functionalities
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.white);
		leftPanel.setBounds(0, 0, 1150, maxHeight);
		leftPanel.setLayout(null);


		//ALL TESTING DONE HERE
		//Adding logo and search to left panel
		leftPanel.add(logoPanel);
		leftPanel.add(searchPanel);
		leftPanel.add(mainPanel);

        //Adding right and left panel to main frame
		frame.add(rightPanel);
		frame.add(leftPanel);

        frame.setVisible(true);
	}

}

class TilePanel {
    public JButton mainPanel;
    private JLabel itemName;
	private static Border line = new LineBorder(Color.black);
	private static Font itemNameFont =  new Font("SansSerif", Font.PLAIN, 23); //font used in text box

    public TilePanel(String itemNameString) {
		mainPanel = new JButton();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.white);
        mainPanel.setBorder(line);

		itemName = new JLabel(itemNameString);
		itemName.setBackground(Color.white);
		itemName.setFont(itemNameFont);
		itemName.setLayout(new FlowLayout());

		JLabel picLabel = new JLabel();
		picLabel.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			picLabel.setIcon(new ImageIcon(new ImageIcon("ProductImages/" + itemNameString.replaceAll("\\s", "") + ".png").getImage().getScaledInstance(175, 225, Image.SCALE_SMOOTH)));
		}
		catch (Exception e) {
			System.out.println(e);
		}
		mainPanel.add(picLabel, BorderLayout.PAGE_START);
		mainPanel.add(itemName, BorderLayout.PAGE_END);
    }
}

class ItemInOrder {
	public JPanel mainPanel;
	private JLabel itemName;
	private static Font itemNameFont =  new Font("SansSerif", Font.PLAIN, 23); //font used in text box

	//get itemAdditons and itemSubtractions from database and display them
	public ItemInOrder(orderItem item) {
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.blue);
		mainPanel.setLayout(new BorderLayout());

		JPanel subScrollPanel = new JPanel();
		subScrollPanel.setLayout(new GridBagLayout());
		subScrollPanel.setBackground(Color.red);
		GridBagConstraints gbc = new GridBagConstraints();

		
		itemName = new JLabel(item.getItemName());
		itemName.setFont(itemNameFont);

		//for loop, for all additions
		int counter = 0;
		for(orderItemModification modification : item.getAdditions()) { //int k = 0; k < 
			JLabel currentAddition = new JLabel("Addition: " + modification.getIngredientName());
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0; 
			gbc.gridy = counter; //set in for loop
			mainPanel.add(currentAddition, gbc);
			counter+=1;
		}
		counter = 0;
		for(orderItemModification modification : item.getSubtractions()) {
			JLabel currentSubtraction = new JLabel("Subtractions: " + modification.getIngredientName());
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0; 
			gbc.gridy = counter; //set in for loop
			mainPanel.add(currentSubtraction, gbc);
			counter+=1;
		}
			

		subScrollPanel.add(itemName, gbc);

		JScrollPane scrollPane = new JScrollPane(subScrollPanel);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
	}


}