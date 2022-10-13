
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.*;
import  java.text.DecimalFormat;
import java.util.List;


public class GUIDriver {
	public static void main(String args[]) {
		
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		GUIWindow newGUIWindow = new GUIWindow();
		//newGUIWindow.updateMainView();
	}
}

 class GUIWindow {
	static int maxHeight = 1080;
	static int maxWidth = 1500;

	static Color darkRed = new Color(165,58,59);
	static Color blueHighlight = new Color(184, 204, 220);
	static Border line = new LineBorder(Color.black);
	static Font defaultButtons = new Font("SansSerif", Font.PLAIN, 28); //font used in text box

	JPanel mainPanel;
	JPanel rightPanel;

	JLabel serverName;
	JButton logout;
	JButton cancel;
	JButton finishAndPay = null;
	JButton createNewOrder;
	JButton managerViewButton;

	ItemInOrder currentlySelectedComponent;

	String currentTextFieldEntry;

	product currentProduct;

	serverViewFunctions serverFunctions;
	dbFunctions dbConnection;
	boolean orderCreated;
	int currentEmployeeId;
	orderTicketInfo newTicket;

	boolean currentPageIsMain;
	boolean currentPageIsTile;
	boolean currentPageIsModifications;

	// public void updateAdditions(String searchBarText, JPanel searchPanel) {
	// 	searchPanel.removeAll();
	// 	searchPanel.setLayout(new FlowLayout());
	
	// 	try {
	// 		dbConnection.createDbConnection();
	// 		String sqlStatement = String.format("SELECT id FROM ingredients WHERE name ILIKE '%s%s%s' ", "%", searchBarText, "%");
	// 		ResultSet results = dbConnection.dbQuery(sqlStatement);
	// 		while(results.next()) {
	// 			ingredient tempIngredient = serverFunctions.getIngredient(results.getInt("id")); 
	// 			JButton button = new JButton(tempIngredient.getName());
	// 			button.setFont(defaultButtons);
	// 			searchPanel.add(button);
	// 			button.setAlignmentX(Component.LEFT_ALIGNMENT);
	// 			button.setBackground(Color.white);
	// 			button.setForeground(darkRed);
	
	// 			searchPanel.add(Box.createRigidArea(new Dimension(30, 0)));
	// 		}
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 		System.err.println(e.getClass().getName()+": "+e.getMessage());
	// 		System.exit(0);
	// 	} 
	
	// 	searchPanel.revalidate();
	// 	searchPanel.repaint();
	// }
	
	public void updateModifications(product product) { // needs product object parameter product product
		mainPanel.removeAll();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		Border padding = new EmptyBorder(10, 10, 10, 10);
		mainPanel.setBorder(new CompoundBorder(line,padding));

		currentProduct = product;
	
		//Creating product label
		JLabel productName = new JLabel("Product Name"); //pull from database
		productName.setAlignmentX(Component.LEFT_ALIGNMENT);
		// productName.setVerticalAlignment(JLabel.NORTH);
	
		//Creating product header font
		Font productNameFont = new Font("SansSerif", Font.PLAIN, 35); 
		productName.setFont(productNameFont);
		productName.setForeground(darkRed);
		mainPanel.add(productName);
	
	
		JLabel sizeHeader = new JLabel("Size");
		sizeHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		//Creating size header font
		Font sizeHeaderFont = new Font("SansSerif", Font.PLAIN, 28);
		sizeHeader.setFont(sizeHeaderFont);
		mainPanel.add(sizeHeader);
	
		JPanel sizeButtonPanel = new JPanel();
		sizeButtonPanel.setLayout(new BoxLayout(sizeButtonPanel, BoxLayout.X_AXIS));
		mainPanel.add(sizeButtonPanel);
		sizeButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		sizeButtonPanel.setBackground(Color.white);
		
		// sizeButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		// sizeButtonPanel.add(Box.createHorizontalGlue());
	
		JButton size20 = new JButton("20oz.");
		size20.setFont(defaultButtons);
		sizeButtonPanel.add(size20);
		size20.setAlignmentX(Component.LEFT_ALIGNMENT);
		size20.setBackground(Color.white);
		size20.setForeground(darkRed);
		size20.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					orderItem temp = serverFunctions.updateItemWithSize(newTicket.getOrderItems().lastElement(), 20);
                    newTicket.removeItemFromOrder(newTicket.getOrderItems().lastElement());
					newTicket.addItemToOrder(temp);
				}
			} );
	
		sizeButtonPanel.add(Box.createRigidArea(new Dimension(60, 0)));
		
		JButton size32 = new JButton("32oz.");
		size32.setFont(defaultButtons);
		sizeButtonPanel.add(size32);
		size32.setAlignmentX(Component.LEFT_ALIGNMENT);
		size32.setBackground(Color.white);
		size32.setForeground(darkRed);
		size32.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					orderItem temp = serverFunctions.updateItemWithSize(newTicket.getOrderItems().lastElement(), 32);
                    newTicket.removeItemFromOrder(newTicket.getOrderItems().lastElement());
					newTicket.addItemToOrder(temp);
				}
			} );
	
		sizeButtonPanel.add(Box.createRigidArea(new Dimension(60, 0)));
	
		JButton size40 = new JButton("40oz.");
		size40.setFont(defaultButtons);
		sizeButtonPanel.add(size40);
		size40.setAlignmentX(Component.LEFT_ALIGNMENT);
		size40.setBackground(Color.white);
		size40.setForeground(darkRed);
		size40.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					orderItem temp = serverFunctions.updateItemWithSize(newTicket.getOrderItems().lastElement(), 40);
                    newTicket.removeItemFromOrder(newTicket.getOrderItems().lastElement());
					newTicket.addItemToOrder(temp);
				}
			} );
		
		
		JLabel subtractionsHeader = new JLabel("Subtractions");
		subtractionsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		//Creating subtractions header font
		Font subtractionsHeaderFont = new Font("SansSerif", Font.PLAIN, 28);
		subtractionsHeader.setFont(subtractionsHeaderFont);
		mainPanel.add(subtractionsHeader);
	
		JPanel subtractionsButtonPanel = new JPanel();
		subtractionsButtonPanel.setLayout(new BoxLayout(subtractionsButtonPanel, BoxLayout.X_AXIS));
		mainPanel.add(subtractionsButtonPanel);
		subtractionsButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		subtractionsButtonPanel.setBackground(Color.white);
	
		// TODO: for loop using imported ingredients
		// create a button for each ingredient
		// add spacing between each button
		// product.ingredients.size()
		List<JButton> buttonList = new ArrayList<JButton>();
		for (int index = 0; index < product.ingredients.size(); index++) {	//product.ingredients.size(); i++) {
			JButton button = new JButton(product.ingredients().get(index).getName());
            int myIndex = index;
			button.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					orderItem temp = serverFunctions.updateItemWithSubtraction(newTicket.getOrderItems().lastElement(), product.ingredients().get(myIndex).getId());
					//serverFunctions.updateItemWithSubtraction(newTicket.getOrderItems().remove(newTicket.getOrderItems.lastElement());
					newTicket.removeItemFromOrder(newTicket.getOrderItems().lastElement());
					newTicket.addItemToOrder(temp);//(newTicket.getOrderItems().lastElement());
				}
			} );
			button.setFont(defaultButtons);
			buttonList.add(button);
			subtractionsButtonPanel.add(button);
			button.setAlignmentX(Component.LEFT_ALIGNMENT);
			button.setBackground(Color.white);
			button.setForeground(darkRed);
	
			subtractionsButtonPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		}

		//ADDITIONS
		
		// JLabel additionsHeader = new JLabel("Additions");
		// additionsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		// //Creating additions header font
		// Font additionsHeaderFont = new Font("SansSerif", Font.PLAIN, 28);
		// additionsHeader.setFont(additionsHeaderFont);
		// mainPanel.add(additionsHeader);
	
		// //Search panel will house static search bar in top left
		// JPanel searchPanel = new JPanel();
		// searchPanel.setBackground(Color.green);
		// //searchPanel.setBounds(13, 87, 800, 30);
		// searchPanel.setMaximumSize(new Dimension(1050, 50));
		// mainPanel.add(searchPanel);
		// searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		// searchPanel.setLayout(new BorderLayout());
	
		// //Create text component of search bar
		// JTextField searchTextField = new JTextField();
		// searchTextField.setPreferredSize(new Dimension(1050,50));
		// searchTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
		// Font searchFont = new Font("SansSerif", Font.PLAIN, 20); //font used in text box
		// searchTextField.setFont(searchFont);
	
		// // Panel to display search results
		// JPanel searchResultPanel = new JPanel();
		// searchResultPanel.setBackground(Color.orange);
		// // searchResultPanel.setBounds(13, 87, 800, 30);
		// mainPanel.add(searchResultPanel);
		// searchResultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		// //Create button component of search bar
		// JButton searchButton = new JButton(new ImageIcon(((new ImageIcon("searchIcon.png")).getImage()).getScaledInstance(43, 43, java.awt.Image.SCALE_SMOOTH)));
		// searchButton.setPreferredSize(new Dimension(50,50));
		// searchButton.addActionListener(new ActionListener() 
		// 	{
		// 		@Override
		// 		public void actionPerformed(ActionEvent e) {
		// 			currentTextFieldEntry = searchTextField.getText();
		// 			updateAdditions(currentTextFieldEntry, searchResultPanel);
		// 		}
		// 	} );
	
		// searchPanel.add(searchButton, BorderLayout.LINE_START);
		// searchPanel.add(searchTextField, BorderLayout.LINE_END);		
			
		
	
		mainPanel.revalidate();
		mainPanel.repaint();
	}


	public void updateTileView(String searchBarText) {
		mainPanel.removeAll();
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		try {
			dbConnection.createDbConnection();
			String sqlStatement = String.format("SELECT id FROM products WHERE name ILIKE '%s%s%s' ", "%", searchBarText, "%");
			ResultSet results = dbConnection.dbQuery(sqlStatement);
			while(results.next()) {
				product tempProduct = serverFunctions.getProduct(results.getInt("id")); 
				TilePanel tempPanel = new TilePanel(tempProduct.getName(), tempProduct);
				tempPanel.mainPanel.addActionListener(new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) {
                        newTicket = serverFunctions.createOrderTicketItem(newTicket, tempProduct);
						updateModifications(tempPanel.productInformation); //product ??
                        
					}
				} );

				mainPanel.add(tempPanel.mainPanel);
				mainPanel.add(Box.createRigidArea(new Dimension(15,300)));
			}
		} catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 


		mainPanel.revalidate();
		mainPanel.repaint();
	}

	public void updateMainView() {
		mainPanel.removeAll();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		
		JPanel subScrollPanel = new JPanel();
		subScrollPanel.setLayout(new BoxLayout(subScrollPanel, BoxLayout.PAGE_AXIS));
		subScrollPanel.add(Box.createRigidArea(new Dimension(10,10)));
		subScrollPanel.setBackground(Color.white);
		subScrollPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		
		JPanel buttonPanel = new JPanel(); //will house buttons dedicated to the main view 
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBackground(Color.white);
		buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);


		JButton duplicate = new JButton("Duplicate");
		duplicate.setBackground(darkRed);
		duplicate.setFont(defaultButtons);
		duplicate.setForeground(Color.white);
		duplicate.setRolloverEnabled(false);
		duplicate.setFocusPainted(false);
		duplicate.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					newTicket = serverFunctions.duplicateItem(newTicket, currentlySelectedComponent.itemInformation);
					updateMainView();
				}
			}
		);


		/*JButton edit = new JButton("Edit");
		edit.setBackground(darkRed);
		edit.setFont(defaultButtons);
		edit.setForeground(Color.white);
		edit.setRolloverEnabled(false);
		edit.setFocusPainted(false);*/

	
		JButton removeItem = new JButton("Remove Item");
		removeItem.setBackground(darkRed);
		removeItem.setFont(defaultButtons);
		removeItem.setForeground(Color.white);
		removeItem.setRolloverEnabled(false);
		removeItem.setFocusPainted(false);
		removeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newTicket = serverFunctions.deleteFromOrder(newTicket, currentlySelectedComponent.itemInformation);
				updateMainView();
			}
		}
	);

		buttonPanel.add(removeItem);
		buttonPanel.add(Box.createRigidArea(new Dimension(675,5)));
		//buttonPanel.add(edit);
		buttonPanel.add(Box.createRigidArea(new Dimension(30,5)));
		buttonPanel.add(duplicate);
		buttonPanel.add(Box.createRigidArea(new Dimension(30,5)));


		mainPanel.add(Box.createRigidArea(new Dimension(0,10)));
		mainPanel.add(buttonPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0,10)));


		if(newTicket != null) {
			for(orderItem item : newTicket.items) {
				ItemInOrder tempItem = new ItemInOrder(item, serverFunctions);
				tempItem.quantityDecrease.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
							if (tempItem.itemInformation.getItemAmount() > 1) {
								tempItem.itemInformation.setItemAmount(item.getItemAmount() - 1);
								updateMainView();
							}
							else {
								tempItem.quantityDecrease.setForeground(Color.lightGray);
							}
						}
					}
				);

				tempItem.quantityIncrease.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
								tempItem.itemInformation.setItemAmount(item.getItemAmount() + 1);
								updateMainView();
							}
					}
				);
				
				tempItem.mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
				tempItem.mainPanel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
							if (currentlySelectedComponent != null) {
								System.out.println(currentlySelectedComponent.itemInformation.getItemName());
								System.out.println(currentlySelectedComponent.itemInformation.itemName);
								currentlySelectedComponent.mainPanel.setBackground(Color.white);
								currentlySelectedComponent.itemNamePanel.setBackground(Color.white);
							}
							currentlySelectedComponent = tempItem;
							currentlySelectedComponent.mainPanel.setBackground(blueHighlight);
							currentlySelectedComponent.itemNamePanel.setBackground(blueHighlight);
							currentlySelectedComponent.quantityIncrease.setBackground(blueHighlight);
							currentlySelectedComponent.quantityIncrease.setBackground(blueHighlight);
							mainPanel.revalidate();
							mainPanel.repaint();
						}
					}
				);
				subScrollPanel.add(tempItem.mainPanel); 
			}
		}


		JScrollPane scrollPanel = new JScrollPane(subScrollPanel);

		mainPanel.add(scrollPanel);
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	public void resetMainPanel() {
		mainPanel.removeAll();
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	public void updateOrderCreatedButton() {
		rightPanel.removeAll();
		
		rightPanel.add(serverName);
		rightPanel.add(Box.createRigidArea(new Dimension(0,5)));

		if (serverFunctions.isAdmin(currentEmployeeId)) {
			managerViewButton = new JButton("Manager View");
			managerViewButton.setFont(defaultButtons);
			managerViewButton.setBackground(Color.white);
			managerViewButton.setForeground(darkRed);
			managerViewButton.setRolloverEnabled(false);
			managerViewButton.setFocusPainted(false);
			managerViewButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed),  BorderFactory.createEmptyBorder(8, 8, 10, 10)));
			managerViewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			rightPanel.add(managerViewButton);
		}

		rightPanel.add(logout);
		rightPanel.add(Box.createRigidArea(new Dimension(0,675)));
		rightPanel.add(cancel);
		rightPanel.add(Box.createRigidArea(new Dimension(0,5)));
		rightPanel.add(finishAndPay);

		rightPanel.revalidate();
		rightPanel.repaint();
	}

	public void updateLoginButtons() {
		rightPanel.removeAll();
		mainPanel.removeAll();
		
		serverName = new JLabel(serverFunctions.getEmployeeName(currentEmployeeId)); //pull from database
		serverName.setHorizontalAlignment(SwingConstants.CENTER);
		serverName.setAlignmentX(Component.CENTER_ALIGNMENT);
		Font serverNameFont = new Font("SansSerif", Font.BOLD, 35); //font used in text box
		serverName.setFont(serverNameFont);

		rightPanel.add(serverName);
		rightPanel.add(Box.createRigidArea(new Dimension(0,5)));


		if (serverFunctions.isAdmin(currentEmployeeId)) {
			managerViewButton = new JButton("Manager View");
			managerViewButton.setFont(defaultButtons);
			managerViewButton.setBackground(Color.white);
			managerViewButton.setForeground(darkRed);
			managerViewButton.setRolloverEnabled(false);
			managerViewButton.setFocusPainted(false);
			managerViewButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed),  BorderFactory.createEmptyBorder(8, 8, 10, 10)));
			managerViewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			rightPanel.add(managerViewButton);
		}
		rightPanel.add(logout);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 675)));
		rightPanel.add(cancel);
		rightPanel.add(Box.createRigidArea(new Dimension(0,5)));
		rightPanel.add(createNewOrder);


		mainPanel.revalidate();
		mainPanel.repaint();

		rightPanel.revalidate();
		rightPanel.repaint();
	}

	public void stateMachine() {
		//defaults to main page
		//if (text is entered and the search button is pressed) and currentPage is Main
			//update tile part

		//if (on tile click and currentPage is tile)
			//go to modifications
		
		//if (on modification exit and currentPage is modification)
			//add database info, orderID and ticket etc
			// go back to main view
	}

	public GUIWindow() {
		//Establishing database connection
		serverFunctions = new serverViewFunctions();
		dbConnection = new dbFunctions();
		
		//TESTING REMOVE
		//Item 1
        /*orderItemModification add1_1 = new orderItemModification();
        add1_1.setingredientId(0);
        orderItemModification add1_2 = new orderItemModification();
        add1_2.setingredientId(1);
        orderItemModification subtract1_1 = new orderItemModification();
        subtract1_1.setingredientId(2);
        
        orderItem item1 = new orderItem();
        item1.setItemNumberInOrder(0);
        item1.setItemName("Test Drink 1");
        item1.setItemAmount(1);
        item1.setItemSize(20);
        item1.addAddition(add1_1);
        item1.addAddition(add1_2);
		item1.setId(20);
        item1.addSubtraction(subtract1_1);
        
        //Item 2
        orderItemModification add2_1 = new orderItemModification();
        add2_1.setingredientId(0);
		add2_1.setIngredientName("Good morning");
        orderItemModification subtract2_1 = new orderItemModification();
        subtract2_1.setingredientId(1);
        orderItemModification subtract2_2 = new orderItemModification();
        subtract2_2.setingredientId(2);

        orderItem item2 = new orderItem();
        item2.setItemNumberInOrder(1);
        item2.setItemName("very very veyr long name");
        item2.setItemAmount(2);
        item2.setItemSize(32);
		item2.setId(21);
        item2.addAddition(add2_1);
        item2.addSubtraction(subtract2_1);
        item2.addSubtraction(subtract2_2);
        
        //Order ticket
        newTicket = new orderTicketInfo();
        newTicket.setCustomerFirstName("Test-Person");
        newTicket.setRewardsMemberId(2);
        newTicket.setEmployeeId(2);
        newTicket.setOrderPriceTotal(4.99);
        newTicket.addItemToOrder(item1);
        newTicket.addItemToOrder(item2);
        //remove testing*/


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
		mainPanel.setLayout(new BorderLayout());

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

		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.LINE_AXIS));
		loginPanel.setBackground(Color.white);

		//Creating login field
		JTextField loginTextField = new JTextField();
		loginTextField.setPreferredSize(new Dimension(500,50));
		loginTextField.setSize(new Dimension(505,50));
		loginTextField.setHorizontalAlignment(JTextField.LEFT);
		loginTextField.setFont(searchFont);
		loginTextField.setAlignmentY(Component.CENTER_ALIGNMENT);
		//FIX VERTICAL ALIGNMENT

		JButton loginButton = new JButton("Login");
		loginButton.setForeground(Color.black);
		loginButton.setMargin(new Insets(8, 10, 10, 10));
		loginButton.setBackground(Color.white);
		loginButton.setRolloverEnabled(false);
		loginButton.setFocusPainted(false);
		loginButton.setFont(defaultButtons);
		loginButton.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					currentEmployeeId = Integer.parseInt(loginTextField.getText());
					updateLoginButtons();
				}
			}
		);

		loginPanel.add(loginButton);
		loginPanel.add(loginTextField);
		

		//Creating Logout Button
		logout = new JButton("      Logout      ");
		//logout.setBounds(32, 50, 150, 75);
		logout.setFont(defaultButtons);
		logout.setBackground(Color.white);
		logout.setForeground(darkRed);
		logout.setRolloverEnabled(false);
		logout.setFocusPainted(false);
		logout.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed));	
		logout.setAlignmentX(Component.CENTER_ALIGNMENT);


		cancel = new JButton("      Finish Item      ");
		//cancel.setBounds(32, 50, 150, 75);
		cancel.setFont(defaultButtons);
		cancel.setBackground(Color.white);
		cancel.setForeground(darkRed);
		cancel.setRolloverEnabled(false);
		cancel.setFocusPainted(false);
		cancel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, darkRed));	
		cancel.setAlignmentX(Component.CENTER_ALIGNMENT);
		cancel.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					//newTicket.addItemToOrder(new );
					updateMainView();
				}
			}
		);


		finishAndPay = new JButton("Finish and Pay");
		//finishAndPay.setBounds(32, 50, 150, 75);
		finishAndPay.setFont(defaultButtons);
		finishAndPay.setBackground(Color.white);
		finishAndPay.setForeground(darkRed);
		finishAndPay.setRolloverEnabled(false);
		finishAndPay.setFocusPainted(false);
		finishAndPay.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed));	
		finishAndPay.setAlignmentX(Component.CENTER_ALIGNMENT);
		finishAndPay.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					//updateTileView(currentTextFieldEntry);
                    //serverFunctions.updateDbWithOrder(newTicket);
					updateMainView();
					orderCreated = true;
				}
			}
		);


		orderCreated = false;
		createNewOrder = new JButton("Create new Order");
		createNewOrder.setFont(defaultButtons);
		createNewOrder.setBackground(Color.white);
		createNewOrder.setForeground(darkRed);
		createNewOrder.setRolloverEnabled(false);
		createNewOrder.setFocusPainted(false);
		createNewOrder.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed));	
		createNewOrder.setAlignmentX(Component.CENTER_ALIGNMENT);
		createNewOrder.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					updateOrderCreatedButton();
					//UPDATE STUFF HERE
					newTicket = new orderTicketInfo();
					newTicket.setEmployeeId(currentEmployeeId);
					updateMainView();
				}
			});


		//Create right panel to house side buttons
		rightPanel = new JPanel();
		rightPanel.setBackground(Color.white);
		rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));		
		rightPanel.setBounds(1150, 0, 350, maxHeight);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

		/*rightPanel.add(serverName);
		rightPanel.add(Box.createRigidArea(new Dimension(0,5)));
		rightPanel.add(logout);
		rightPanel.add(Box.createRigidArea(new Dimension(0,750)));
		rightPanel.add(cancel);
		rightPanel.add(Box.createRigidArea(new Dimension(0,5)));
		rightPanel.add(createNewOrder);*/

		
		//Create left panel to house logo, search, and all other functionalities
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.white);
		leftPanel.setBounds(0, 0, 1150, maxHeight);
		leftPanel.setLayout(null);


		mainPanel.add(loginPanel, BorderLayout.PAGE_START);
		//ALL TESTING DONE HERE
		//Adding logo, search , and main panel to left panel
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
	public product productInformation;
    private JTextArea itemName;
	private static Border line = new LineBorder(Color.black);
	private static Font itemNameFont =  new Font("SansSerif", Font.PLAIN, 20); //font used in text box

    public TilePanel(String itemNameString, product product) {

		productInformation = product;
		mainPanel = new JButton();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.white);
        mainPanel.setBorder(line);
		mainPanel.setMinimumSize(new Dimension(175,300));
		mainPanel.setMaximumSize(new Dimension(175,300));
		

		itemName = new JTextArea(itemNameString, 2, 1);
		itemName.setBackground(Color.white);
		itemName.setFont(itemNameFont);
		itemName.setLineWrap(true);
		itemName.setEditable(false);
		itemName. setHighlighter(null);
		itemName.setMaximumSize(new Dimension(175, 75));

		JLabel picLabel = new JLabel();
		picLabel.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			picLabel.setIcon(new ImageIcon(new ImageIcon("ProductImages/pumpkinWhatever.png").getImage().getScaledInstance(175, 225, Image.SCALE_SMOOTH)));
			//picLabel.setIcon(new ImageIcon(new ImageIcon("ProductImages/" + itemNameString.replaceAll("\\s", "") + ".png").getImage().getScaledInstance(175, 225, Image.SCALE_SMOOTH)));
		}
		catch (Exception e) {
			System.out.println(e);
		}
		mainPanel.add(picLabel, BorderLayout.PAGE_START);
		mainPanel.add(itemName, BorderLayout.PAGE_END);
    }
}

class ItemInOrder {
	public JButton mainPanel;
	public JButton quantityIncrease;
	public JButton quantityDecrease;
	public JPanel subScrollPanel;
	public JPanel itemNamePanel;
	public orderItem itemInformation;

	private static Font itemNameFont =  new Font("SansSerif", Font.PLAIN, 28); //font used in text box
	private static Font defaultButtons =  new Font("SansSerif", Font.PLAIN, 25); //font used in text box

	static Color darkRed = new Color(165,58,59);
	serverViewFunctions serverFunctions;

	//get itemAdditons and itemSubtractions from database and display them
	public ItemInOrder(orderItem item, serverViewFunctions serverFunctions) {

		if (serverFunctions == null) {
			System.out.println("serverFunctions is null");
			return;
		}

		itemInformation = item;

		mainPanel = new JButton();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setBackground(Color.white);
		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.setRolloverEnabled(false);
		mainPanel.setFocusPainted(false);
		

		subScrollPanel = new JPanel();
		subScrollPanel.setLayout(new BoxLayout(subScrollPanel, BoxLayout.PAGE_AXIS));
		subScrollPanel.setBackground(Color.white);
		subScrollPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		itemNamePanel = new JPanel();
		itemNamePanel.setLayout(new BoxLayout(itemNamePanel, BoxLayout.LINE_AXIS));
		itemNamePanel.setOpaque(false);
		itemNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel itemName = new JLabel("  " + item.getItemName());
		itemName.setFont(itemNameFont);
		itemName.setForeground(darkRed);
		itemName.setOpaque(false);
		itemName.setAlignmentX(Component.LEFT_ALIGNMENT);
		itemName.setMinimumSize(new Dimension(400, 50));
		itemName.setMaximumSize(new Dimension(400, 50));
		

		JLabel itemSize = new JLabel(Integer.toString(item.getItemSize()));
		itemSize.setFont(defaultButtons);
		itemSize.setOpaque(false);
		itemSize.setAlignmentX(Component.RIGHT_ALIGNMENT);
		

		JLabel quantity = new JLabel (Integer.toString(item.getItemAmount()));
		quantity.setFont(defaultButtons);
		quantity.setOpaque(false);
		quantity.setAlignmentX(Component.RIGHT_ALIGNMENT);

		quantityIncrease = new JButton("+");
		quantityIncrease.setBackground(Color.white);
		quantityIncrease.setBorderPainted(false);
		quantityIncrease.setRolloverEnabled(false);
		quantityIncrease.setFocusPainted(false);

		
		quantityDecrease = new JButton("-");
		quantityDecrease.setBackground(Color.white);
		quantityDecrease.setBorderPainted(false);
		quantityDecrease.setRolloverEnabled(false);
		quantityDecrease.setFocusPainted(false);



		String pattern = "###.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
        JLabel price = new JLabel("$" + decimalFormat.format((serverFunctions.getProduct(item.getProductId())).getPrice()));

		price.setFont(defaultButtons);
		price.setAlignmentX(Component.LEFT_ALIGNMENT);

		itemNamePanel.add(itemName);
		itemNamePanel.add(Box.createRigidArea(new Dimension(375,0)));
		itemNamePanel.add(itemSize);
		itemNamePanel.add(Box.createRigidArea(new Dimension(75,10)));
		itemNamePanel.add(quantityDecrease);
		itemNamePanel.add(quantity);
		itemNamePanel.add(quantityIncrease);
		itemNamePanel.add(Box.createRigidArea(new Dimension(75,10)));
		itemNamePanel.add(price);

		mainPanel.add(itemNamePanel);

		//for loop, for all additions
		for(orderItemModification modification : item.getAdditions()) { 
			JLabel currentAddition = new JLabel("          Addition: " + modification.getIngredientName());
			currentAddition.setOpaque(false);
			currentAddition.setFont(defaultButtons);
			currentAddition.setAlignmentX(Component.LEFT_ALIGNMENT);
			subScrollPanel.add(currentAddition);
			
		}

		for(orderItemModification modification : item.getSubtractions()) {
			JLabel currentSubtraction = new JLabel("          Subtractions: " + modification.getIngredientName());
			currentSubtraction.setOpaque(false);
			currentSubtraction.setFont(defaultButtons);
			currentSubtraction.setAlignmentX(Component.LEFT_ALIGNMENT);
			mainPanel.add(currentSubtraction);
		}


		//mainPanel.add(subScrollPanel);
	}


}