
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.*;
import  java.text.DecimalFormat;
import java.util.List;


/**
 * @author Alexia Hassan
 */
public class GUIDriver {
	public static void main(String args[]) {
		
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		GUIWindow newGUIWindow = new GUIWindow();
	}
}

/**
 * @author Alexia Hassan, Shreyes Kaliyur, Emma Ong,  Robert Eads
 */ 
 class GUIWindow {
	static int maxHeight = 1080;
	static int maxWidth = 1500;

	static Color darkRed = new Color(165,58,59);
	static Color blueHighlight = new Color(184, 204, 220);
	static Border line = new LineBorder(Color.black);
	static Font defaultButtons = new Font("SansSerif", Font.PLAIN, 28); //font used in text box
	static Font searchFont = new Font("SansSerif", Font.PLAIN, 25); //font used in text box

	JFrame frame;

	JPanel mainPanel;
	JPanel rightPanel;

	JLabel serverName;
	JButton logout;
	JPanel customerNamePanel;
	JTextField customerNameField;
	JTextField searchTextField;
	JPanel rewardsMemberIdPanel;
	JTextField rewardsMemberIdField;
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

	/**
	 * Makes visible components required for logging in
	 */
	public void showLoginScreen() {
		rightPanel.removeAll();
		rightPanel.revalidate();
		rightPanel.repaint();

		clearMainPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));


		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.LINE_AXIS));
		loginPanel.setBackground(Color.white);

		JTextField loginTextField = new JTextField();
		loginTextField.setPreferredSize(new Dimension(1000,75));
		loginTextField.setMaximumSize(new Dimension(1000,75));
		loginTextField.setHorizontalAlignment(JTextField.LEFT);
		loginTextField.setFont(searchFont);
		loginTextField.setAlignmentY(Component.CENTER_ALIGNMENT);
		loginTextField.setAlignmentX(Component.LEFT_ALIGNMENT);

		JButton loginButton = new JButton("Login");
		loginButton.setForeground(Color.black);
		loginButton.setMargin(new Insets(8, 10, 8, 10));
		loginButton.setBackground(Color.white);
		loginButton.setPreferredSize(new Dimension(100,73));
		loginButton.setMaximumSize(new Dimension(100,73));

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


		mainPanel.add(loginPanel);
		mainPanel.revalidate();
		mainPanel.repaint();
	}


/**
 * Updates search results when adding additions to an OrderItem object
 * @param searchBarText the string to search the database for 
 * @param searchPanel the panel that will house the search results as buttons
 */
	public void updateAdditions(String searchBarText, JPanel searchPanel) {
		searchPanel.removeAll();
		searchPanel.setLayout(new FlowLayout());
	

		try {
			dbConnection.createDbConnection();
			String sqlStatement = String.format("SELECT id FROM ingredients WHERE name ILIKE '%s%s%s' ", "%", searchBarText, "%");
			ResultSet results = dbConnection.dbQuery(sqlStatement);
			while(results.next()) {
				ingredient tempIngredient = serverFunctions.getIngredient(results.getInt("id")); 
				JButton button = new JButton(tempIngredient.getName());
				button.setFont(defaultButtons);
				searchPanel.add(button);
				button.setAlignmentX(Component.LEFT_ALIGNMENT);
				button.setBackground(Color.white);
				button.setForeground(darkRed);
				searchPanel.add(Box.createRigidArea(new Dimension(30, 0)));
				button.addActionListener(new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) {
						orderItem temp = serverFunctions.updateItemWithAddition(newTicket.getOrderItems().lastElement(), tempIngredient.getId(), false) ;
						updateModifications(currentProduct);
						newTicket.removeItemFromOrder(newTicket.getOrderItems().lastElement()); //add item
						newTicket.addItemToOrder(temp);
					}
				} );
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		} 
	
		
		searchPanel.revalidate();
		searchPanel.repaint();
	}
	
/**
 * Creates and makes visible the components required to modify a product including size, additions, and subtractions
 * @param product the product that will be used to determine which subtraction options are available
 */
	public void updateModifications(product product) { // needs product object parameter product product
		mainPanel.removeAll();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		Border padding = new EmptyBorder(10, 10, 10, 10);
		mainPanel.setBorder(new CompoundBorder(line,padding));


		currentProduct = product;
	

		//Creating product label
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
		topPanel.setBackground(Color.white);
		topPanel.setMinimumSize(new Dimension(1100,75));

		JLabel productName = new JLabel(product.getName()); //pull from database
		productName.setMinimumSize(new Dimension(300, 75));
		productName.setPreferredSize(new Dimension(300, 75));
		productName.setMaximumSize(new Dimension(300, 75));
		productName.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		//Creating product header font
		Font productNameFont = new Font("SansSerif", Font.PLAIN, 35); 
		productName.setFont(productNameFont);
		productName.setForeground(darkRed);

		JButton finishItem = new JButton("  Finish Item  ");
		finishItem.setBackground(darkRed);
		finishItem.setForeground(Color.white);
		finishItem.setFont(defaultButtons);
		finishItem.setAlignmentX(Component.LEFT_ALIGNMENT);
		finishItem.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					updateMainView();
				}
			} );

			
		topPanel.add(productName);
		topPanel.add(Box.createRigidArea(new Dimension(440, 75)));
		topPanel.add(finishItem);
		topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		mainPanel.add(topPanel);
	
	
		JLabel sizeHeader = new JLabel("Size");
		sizeHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		//Creating size header font
		Font sizeHeaderFont = new Font("SansSerif", Font.PLAIN, 28);
		sizeHeader.setFont(sizeHeaderFont);
		mainPanel.add(sizeHeader);
	
		JPanel sizeButtonPanel = new JPanel();
		sizeButtonPanel.setLayout(new BoxLayout(sizeButtonPanel, BoxLayout.X_AXIS));
		sizeButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		sizeButtonPanel.setBackground(Color.white);
		mainPanel.add(sizeButtonPanel);

		
		JButton size20 = new JButton("20oz.");
		JButton size32 = new JButton("32oz.");
		JButton size40 = new JButton("40oz.");

		size20.setFont(defaultButtons);
		sizeButtonPanel.add(size20);
		size20.setAlignmentX(Component.LEFT_ALIGNMENT);
		if (newTicket.getOrderItems().lastElement().getItemSize() == 20) {
			size20.setBackground(blueHighlight);
		}
		else {
			size20.setBackground(Color.white);
		}

		size20.setBackground(Color.white);
		size20.setForeground(darkRed);
		size20.setRolloverEnabled(false);
		size20.setFocusPainted(false);
		size20.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					size20.setBackground(blueHighlight);
					size32.setBackground(Color.white);
					size40.setBackground(Color.white);

					orderItem temp = serverFunctions.updateItemWithSize(newTicket.getOrderItems().lastElement(), 20);
					newTicket.removeItemFromOrder(newTicket.getOrderItems().lastElement());
					newTicket.addItemToOrder(temp);

					mainPanel.revalidate();
					mainPanel.repaint();
				}
			} 
		);
	
		sizeButtonPanel.add(Box.createRigidArea(new Dimension(60, 0)));
		
		size32.setFont(defaultButtons);
		sizeButtonPanel.add(size32);
		size32.setAlignmentX(Component.LEFT_ALIGNMENT);
		size32.setBackground(Color.white);
		size32.setForeground(darkRed);
		size32.setRolloverEnabled(false);
		size32.setFocusPainted(false);

		if (newTicket.getOrderItems().lastElement().getItemSize() == 32) {
			size32.setBackground(blueHighlight);
		}
		else {
			size32.setBackground(Color.white);
		}


		size32.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					size20.setBackground(Color.white);
					size32.setBackground(blueHighlight);
					size40.setBackground(Color.white);

					orderItem temp = serverFunctions.updateItemWithSize(newTicket.getOrderItems().lastElement(), 32);
					newTicket.removeItemFromOrder(newTicket.getOrderItems().lastElement());
					newTicket.addItemToOrder(temp);
					
					mainPanel.revalidate();
					mainPanel.repaint();
				}
			} 
		);
	
		sizeButtonPanel.add(Box.createRigidArea(new Dimension(60, 0)));
	
		size40.setFont(defaultButtons);
		sizeButtonPanel.add(size40);
		size40.setAlignmentX(Component.LEFT_ALIGNMENT);
		size40.setBackground(Color.white);
		size40.setForeground(darkRed);
		size40.setRolloverEnabled(false);
		size40.setFocusPainted(false);

		if (newTicket.getOrderItems().lastElement().getItemSize() == 40) {
			size40.setBackground(blueHighlight);
		}
		else {
			size40.setBackground(Color.white);
		}


		size40.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					size20.setBackground(Color.white);
					size32.setBackground(Color.white);
					size40.setBackground(blueHighlight);
					
					orderItem temp = serverFunctions.updateItemWithSize(newTicket.getOrderItems().lastElement(), 40);
					newTicket.removeItemFromOrder(newTicket.getOrderItems().lastElement());
					newTicket.addItemToOrder(temp);

					mainPanel.revalidate();
					mainPanel.repaint();
				}
			} 
		);
		
		
		JLabel subtractionsHeader = new JLabel("Subtractions");
		subtractionsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		//Creating subtractions header font
		Font subtractionsHeaderFont = new Font("SansSerif", Font.PLAIN, 28);
		subtractionsHeader.setFont(subtractionsHeaderFont);
		mainPanel.add(subtractionsHeader);
	
		JPanel subtractionsButtonPanel = new JPanel();
		subtractionsButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		subtractionsButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		subtractionsButtonPanel.setBackground(Color.white);

	
		List<JButton> buttonList = new ArrayList<JButton>();
		for (int index = 0; index < product.ingredients.size(); index++) {	
            int myIndex = index;
			SubtractionButton button = new SubtractionButton(serverFunctions, product, myIndex);
			if (!(serverFunctions.isInSubtractions(newTicket.getOrderItems().lastElement(), product.ingredients.get(myIndex).getName()))) {
				button.mainButton.setBackground(Color.white);
				mainPanel.revalidate();
				mainPanel.repaint();
			}
			else { //add subtraction to item in order
				button.mainButton.setBackground(blueHighlight);
				mainPanel.revalidate();
				mainPanel.repaint();
			}

			button.mainButton.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
						orderItem temp = serverFunctions.updateItemWithSubtraction(newTicket.getOrderItems().lastElement(), product.ingredients().get(myIndex).getId(), button.selected);
						if(button.selected) {
							button.selected = false;
							button.mainButton.setBackground(Color.white);
							mainPanel.revalidate();
							mainPanel.repaint();
							}				
						else { 
							button.selected = true;
							button.mainButton.setBackground(blueHighlight);
							mainPanel.revalidate();
							mainPanel.repaint();
						}

						newTicket.removeItemFromOrder(newTicket.getOrderItems().lastElement()); //add item
						newTicket.addItemToOrder(temp);
					}
				} 
			);
			
			buttonList.add(button.mainButton);
			subtractionsButtonPanel.add(button.mainButton);
			subtractionsButtonPanel.add(Box.createRigidArea(new Dimension(15, 10)));
		}

		mainPanel.add(subtractionsButtonPanel);


		JLabel additionsHeader = new JLabel("Additions");
		additionsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		//Creating additions header font
		Font additionsHeaderFont = new Font("SansSerif", Font.PLAIN, 28);
		additionsHeader.setFont(additionsHeaderFont);
	
		//Search panel for additions
		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(Color.white);
		searchPanel.setPreferredSize(new Dimension(1100, 50));
		searchPanel.setMaximumSize(new Dimension(1100, 50));
		searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));
	
		//Create text component of search bar
		JTextField additionSearchTextField = new JTextField();
		additionSearchTextField.setPreferredSize(new Dimension(1050,50));
		additionSearchTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
		additionSearchTextField.setFont(searchFont);
	
		// Panel to display search results
		JPanel searchResultPanel = new JPanel();
		searchResultPanel.setBackground(Color.white);
		searchResultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	

		//Create button component of search bar
		JButton searchButton = new JButton(new ImageIcon(((new ImageIcon("searchIcon.png")).getImage()).getScaledInstance(43, 43, java.awt.Image.SCALE_SMOOTH)));
		searchButton.setPreferredSize(new Dimension(50,50));
		searchButton.setBackground(Color.white);
		searchButton.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					currentTextFieldEntry = additionSearchTextField.getText();
					updateAdditions(currentTextFieldEntry, searchResultPanel);
				}
			} );
	

		JPanel additionButtonPanel = new JPanel();
		additionButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		additionButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		additionButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		additionButtonPanel.setBackground(Color.white);


		for (int k = 0; k < newTicket.getOrderItems().lastElement().getAdditions().size();  k++) {
			AdditionButton tempButton = new AdditionButton(serverFunctions, newTicket.getOrderItems().lastElement().getAdditions().get(k).getIngredientName(), newTicket.getOrderItems().lastElement().getAdditions().get(k).getIngredientId(), product);
			tempButton.mainButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//REMOVE ITEM FROM ITEM THING
						serverFunctions.updateItemWithAddition(newTicket.getOrderItems().lastElement(), tempButton.id, true);
						updateModifications(tempButton.product);
					}
				} 
			);

			additionButtonPanel.add(tempButton.mainButton);

		} 

		
		searchPanel.add(searchButton);
		searchPanel.add(additionSearchTextField);		
			
		mainPanel.add(additionsHeader);
		mainPanel.add(additionButtonPanel);
		mainPanel.add(searchPanel);
		mainPanel.add(searchResultPanel);

	
		mainPanel.revalidate();
		mainPanel.repaint();
	}


/**
 * Clears the center or main panel completely and repaints
 */
	public void clearMainPanel() {
		mainPanel.removeAll();
		mainPanel.revalidate();
		mainPanel.repaint();
	}

/**
 * Creates and displays search results for products as a tile object of pictures and product names
 * @param searchBarText The string to search the database for 
 */
	public void updateTileView(String searchBarText) {

		mainPanel.removeAll();
		

		JPanel subScrollPanel = new JPanel();
		subScrollPanel.setLayout(new BoxLayout(subScrollPanel, BoxLayout.PAGE_AXIS));
		subScrollPanel.setBackground(Color.white);
		subScrollPanel.add(Box.createRigidArea(new Dimension(0,15)));

		try {
			dbConnection.createDbConnection();
			String sqlStatement = String.format("SELECT id FROM products WHERE name ILIKE '%s%s%s' ", "%", searchBarText, "%");
			ResultSet results = dbConnection.dbQuery(sqlStatement);
 
						
			while (results.next()) { 
				JPanel rowOfTiles = new JPanel();
				rowOfTiles.setLayout(new BoxLayout(rowOfTiles,BoxLayout.LINE_AXIS));
				rowOfTiles.setBackground(Color.white);
				rowOfTiles.setAlignmentX(Component.LEFT_ALIGNMENT);
				for (int j = 0; j < 5; j++) { 
					product tempProduct = serverFunctions.getProduct(results.getInt("id")); 
					TilePanel tempPanel = new TilePanel(tempProduct.getName(), tempProduct);
					tempPanel.mainPanel.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								newTicket = serverFunctions.createOrderTicketItem(newTicket, tempProduct);
								updateModifications(tempPanel.productInformation); 
								
							}
						} 
					);

					rowOfTiles.add(tempPanel.mainPanel);
					rowOfTiles.add(Box.createRigidArea(new Dimension(15, 0)));
					if (j < 4 && !results.next())
						break;
				}

				subScrollPanel.add(rowOfTiles);
				subScrollPanel.add(Box.createRigidArea(new Dimension(15,10)));
			}
		} 
		catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 

		JScrollPane scrollPanel = new JScrollPane(subScrollPanel);		
		scrollPanel.setHorizontalScrollBar(null);
		mainPanel.add(scrollPanel);
		mainPanel.revalidate();
		mainPanel.repaint();
	}

/**
 * Creates and displays the current order as a series of items with size, quantity, and price
 * Calculates and displays order total
 */
	public void updateMainView() {
		mainPanel.removeAll();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		
		JPanel subScrollPanel = new JPanel();
		subScrollPanel.setLayout(new BoxLayout(subScrollPanel, BoxLayout.PAGE_AXIS));
		subScrollPanel.add(Box.createRigidArea(new Dimension(10,10)));
		subScrollPanel.setBackground(Color.white);
		subScrollPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		
		//Will house buttons dedicated to the main view 
		JPanel buttonPanel = new JPanel(); 
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
		buttonPanel.add(Box.createRigidArea(new Dimension(600,5)));
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
								tempItem.quantityDecrease.setForeground(Color.black);
								updateMainView();
								mainPanel.revalidate();
								mainPanel.repaint();
							}
							else {
								mainPanel.revalidate();
								mainPanel.repaint();
								
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
								currentlySelectedComponent.mainPanel.setBackground(Color.white);
								currentlySelectedComponent.itemNamePanel.setBackground(Color.white);
								currentlySelectedComponent.quantityIncrease.setBackground(Color.white);
								currentlySelectedComponent.quantityDecrease.setBackground(Color.white);
							}

							currentlySelectedComponent = tempItem;
							currentlySelectedComponent.mainPanel.setBackground(blueHighlight);
							currentlySelectedComponent.itemNamePanel.setBackground(blueHighlight);
							currentlySelectedComponent.quantityIncrease.setBackground(blueHighlight);
							currentlySelectedComponent.quantityDecrease.setBackground(blueHighlight);
							mainPanel.revalidate();
							mainPanel.repaint();
						}
					}
				);
				subScrollPanel.add(tempItem.mainPanel); 
			}
		}

		JPanel orderTotal = new JPanel();
		orderTotal.setBackground(Color.white);
		orderTotal.setLayout(new BoxLayout(orderTotal, BoxLayout.LINE_AXIS));
		JLabel orderPrice = new JLabel("Order Total:                                                                                 $" + Double.toString(serverFunctions.getCurrentOrderTotal(newTicket)));
		orderPrice.setFont(defaultButtons);
		orderTotal.add(orderPrice);
		orderTotal.setAlignmentX(Component.LEFT_ALIGNMENT);

		subScrollPanel.add(Box.createRigidArea(new Dimension(0, 35)));
		subScrollPanel.add(orderTotal);
		JScrollPane scrollPanel = new JScrollPane(subScrollPanel);
		scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		mainPanel.add(scrollPanel);
		mainPanel.revalidate();
		mainPanel.repaint();
	}

/**
 * Creates and displays components required to complete an order, including the sever name, logout button, cancel button, and finish and pay button.
 */
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
			managerViewButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed),  BorderFactory.createEmptyBorder(15, 8, 15, 10)));
			managerViewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			rightPanel.add(managerViewButton);
		}

		rightPanel.add(logout);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 400))); //this may need to be edited
		rightPanel.add(customerNamePanel);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		rightPanel.add(rewardsMemberIdPanel);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 80)));
		rightPanel.add(cancel);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		rightPanel.add(finishAndPay);
		

		rightPanel.revalidate();
		rightPanel.repaint();
	}


/**
 * Creates and displays for making an order such as the "Create Order" Button
 */
	public void updateLoginButtons() {
		rightPanel.removeAll();
		mainPanel.removeAll();
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		serverName = new JLabel(serverFunctions.getEmployeeName(currentEmployeeId)); 
		serverName.setHorizontalAlignment(SwingConstants.CENTER);
		serverName.setAlignmentX(Component.CENTER_ALIGNMENT);
		Font serverNameFont = new Font("SansSerif", Font.BOLD, 35); 
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
			managerViewButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed),  BorderFactory.createEmptyBorder(15, 8, 15, 10)));
			managerViewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			managerViewButton.addActionListener(e -> {
				new ManagerView(serverFunctions);
				frame.dispose();
			});
			rightPanel.add(managerViewButton);
		}
		rightPanel.add(logout);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 575))); //this may need to be edited
		rightPanel.add(cancel);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		rightPanel.add(createNewOrder);


		searchTextField.setEditable(false);


		mainPanel.revalidate();
		mainPanel.repaint();

		rightPanel.revalidate();
		rightPanel.repaint();
	}

/**
 * Gets current input of customer name text entry box and assigns it as needed
 * @param customerName the name of the customer for the current order
 */
	public void updateCustomerName(String customerName) {
		String[] splittedString = customerName.trim().split(" ");
		newTicket.setCustomerFirstName(splittedString[0]);
	}

/**
 * 
 * @param rewardsMemberId the rewards member Id of the customer for the current order
 */
	public void updateRewardsMemberId(String rewardsMemberId) {
		String strippedString = rewardsMemberId.trim();
		newTicket.setRewardsMemberId(Integer.parseInt(strippedString));
	}

/**
 * The GUIWindow constructor. Creates all visual and functional objects required for the server view application to run
 */
	public GUIWindow() {
		//Establishing database connection

		serverFunctions = new serverViewFunctions();
		dbConnection = new dbFunctions();
		
		//Create JFrame and initial settings
		frame = new JFrame("Smoothie King");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(maxWidth, maxHeight));
		frame.setMinimumSize(new Dimension(maxWidth, maxHeight));
		frame.setMaximumSize(new Dimension(maxWidth, maxHeight));
		frame.setResizable(false);
		frame.setLayout(null);

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.white);
		mainPanel.setBounds(13, 150, 975, 800);
		mainPanel.setBorder(line);
		mainPanel.setLayout(new BorderLayout());

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
		searchTextField = new JTextField();
		searchTextField.setEditable(false);
		searchTextField.setPreferredSize(new Dimension(925,50));
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
					searchTextField.setText("");
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
		loginTextField.setSize(new Dimension(500,50));
		loginTextField.setHorizontalAlignment(JTextField.LEFT);
		loginTextField.setFont(searchFont);
		loginTextField.setAlignmentY(Component.CENTER_ALIGNMENT);


		//Creating Logout Button
		logout = new JButton("      Logout      ");
		logout.setFont(defaultButtons);
		logout.setBackground(Color.white);
		logout.setForeground(darkRed);
		logout.setRolloverEnabled(false);
		logout.setFocusPainted(false);
		logout.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed), BorderFactory.createEmptyBorder(15, 0, 15, 0)));	
		logout.setAlignmentX(Component.CENTER_ALIGNMENT);
		logout.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					currentEmployeeId = -1;
					showLoginScreen();
				}
			}
		);

		customerNamePanel = new JPanel();
		customerNamePanel.setLayout(new BoxLayout(customerNamePanel, BoxLayout.LINE_AXIS));
		customerNamePanel.setBackground(Color.white);
		customerNamePanel.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black));

		customerNameField = new JTextField();
		customerNameField.setPreferredSize(new Dimension(250, 40));
		customerNameField.setMaximumSize(new Dimension(250, 40));
		customerNameField.setBackground(Color.white);
		customerNameField.setForeground(darkRed);
		customerNameField.setHorizontalAlignment(JTextField.LEFT);
		customerNameField.setBorder(BorderFactory.createEmptyBorder());
		customerNameField.setFont(searchFont); 
		customerNameField.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton customerNameButton = new JButton("Customer Name");
		customerNameButton.setPreferredSize(new Dimension(150, 40));
		customerNameButton.setMaximumSize(new Dimension(150, 40));
		customerNameButton.setForeground(Color.black);
		customerNameButton.setMargin(new Insets(4, 5, 5, 5));
		customerNameButton.setBackground(Color.white);
		customerNameButton.setRolloverEnabled(false);
		customerNameButton.setFocusPainted(false);
		Font customerFont = new Font("SansSerif", Font.PLAIN, 16);
		customerNameButton.setFont(customerFont);
		customerNameButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					updateCustomerName(customerNameField.getText());
				}
			}
		);

		customerNamePanel.add(customerNameButton);
		customerNamePanel.add(Box.createRigidArea(new Dimension(2,0)));
		customerNamePanel.add(customerNameField);


		//Rewards member panel: lets employee type in the rewards member id and add it to the order ticket
		rewardsMemberIdPanel = new JPanel();
		rewardsMemberIdPanel.setLayout(new BoxLayout(rewardsMemberIdPanel, BoxLayout.LINE_AXIS));
		rewardsMemberIdPanel.setBackground(Color.white);
		rewardsMemberIdPanel.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black));

		rewardsMemberIdField = new JTextField();
		rewardsMemberIdField.setPreferredSize(new Dimension(250, 40));
		rewardsMemberIdField.setMaximumSize(new Dimension(250, 40));
		rewardsMemberIdField.setBackground(Color.white);
		rewardsMemberIdField.setForeground(darkRed);
		rewardsMemberIdField.setHorizontalAlignment(JTextField.LEFT);
		rewardsMemberIdField.setBorder(BorderFactory.createEmptyBorder());
		rewardsMemberIdField.setFont(searchFont); 
		rewardsMemberIdField.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton rewardsMemberIdButton = new JButton("Member ID");
		rewardsMemberIdButton.setPreferredSize(new Dimension(150, 40));
		rewardsMemberIdButton.setMaximumSize(new Dimension(150, 40));
		rewardsMemberIdButton.setForeground(Color.black);
		rewardsMemberIdButton.setMargin(new Insets(4, 5, 5, 5));
		rewardsMemberIdButton.setBackground(Color.white);
		rewardsMemberIdButton.setRolloverEnabled(false);
		rewardsMemberIdButton.setFocusPainted(false);
		rewardsMemberIdButton.setFont(customerFont);
		rewardsMemberIdButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					updateRewardsMemberId(rewardsMemberIdField.getText());
				}
			}
		);

		rewardsMemberIdPanel.add(rewardsMemberIdButton);
		rewardsMemberIdPanel.add(Box.createRigidArea(new Dimension(2,0)));
		rewardsMemberIdPanel.add(rewardsMemberIdField);


		cancel = new JButton("      Cancel     ");
		cancel.setFont(defaultButtons);
		cancel.setBackground(Color.white);
		cancel.setForeground(darkRed);
		cancel.setRolloverEnabled(false);
		cancel.setFocusPainted(false);
		cancel.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, darkRed), BorderFactory.createEmptyBorder(15, 0, 15, 0)));	
		cancel.setAlignmentX(Component.CENTER_ALIGNMENT);
		cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					clearMainPanel();
					updateLoginButtons();
					//REMOVE ORDER TICKET
					newTicket = new orderTicketInfo();
				}
			}
		);


		finishAndPay = new JButton("Finish and Pay");
		finishAndPay.setFont(defaultButtons);
		finishAndPay.setBackground(Color.white);
		finishAndPay.setForeground(darkRed);
		finishAndPay.setRolloverEnabled(false);
		finishAndPay.setFocusPainted(false);
		finishAndPay.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed), BorderFactory.createEmptyBorder(15, 0, 15, 0)));	
		finishAndPay.setAlignmentX(Component.CENTER_ALIGNMENT);
		finishAndPay.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					newTicket.setCustomerFirstName(customerNameField.getText());
                    serverFunctions.updateDbWithOrder(newTicket);
					updateLoginButtons();
					clearMainPanel();
					orderCreated = true;
					customerNameField.setText("");
					rewardsMemberIdField.setText("");
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
		createNewOrder.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed), BorderFactory.createEmptyBorder(15, 0, 15, 0)));	
		createNewOrder.setAlignmentX(Component.CENTER_ALIGNMENT);
		createNewOrder.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					updateOrderCreatedButton();
					//UPDATE STUFF HERE
					newTicket = new orderTicketInfo();
					newTicket.setEmployeeId(currentEmployeeId);
					searchTextField.setEditable(true);
					updateMainView();
				}
			});


		//Create right panel to house side buttons
		rightPanel = new JPanel();
		rightPanel.setBackground(Color.white);
		rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));		
		rightPanel.setBounds(1050, 0, 450, maxHeight);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

		
		//Create left panel to house logo, search, and all other functionalities
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.white);
		leftPanel.setBounds(0, 0, 1050, maxHeight);
		leftPanel.setLayout(null);


		//Adding logo, search , and main panel to left panel
		leftPanel.add(logoPanel);
		leftPanel.add(searchPanel);
		leftPanel.add(mainPanel);

        //Adding right and left panel to main frame
		frame.add(rightPanel);
		frame.add(leftPanel);
        frame.setVisible(true);


		//Default to login screen
		showLoginScreen();
	}

}

/**
 * @author Alexia Hassan
 */
class TilePanel {
    public JButton mainPanel;
	public product productInformation;
    private JTextArea itemName;
	private static Border line = new LineBorder(Color.black);
	private static Font itemNameFont =  new Font("SansSerif", Font.PLAIN, 20); 

	/**
	 * TilePanel constructor creates a stacked panel of an image and a text box
	 * @param itemNameString the name of the product to be displayed under the image
	 * @param product the product to be displayed above the text
	 */
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
		itemName.setHighlighter(null);
		itemName.setMaximumSize(new Dimension(175, 75));

		JLabel picLabel = new JLabel();
		picLabel.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			picLabel.setIcon(new ImageIcon(new ImageIcon("ProductImages/" + itemNameString + ".png").getImage().getScaledInstance(175, 225, Image.SCALE_SMOOTH)));
		}
		catch (Exception e) {
			System.out.println(e);
		}

		mainPanel.add(picLabel, BorderLayout.PAGE_START);
		mainPanel.add(itemName, BorderLayout.PAGE_END);
    }
}


/**
 * @author Alexia Hassan
 */
class ItemInOrder {
	public JButton mainPanel;
	public JButton quantityIncrease;
	public JButton quantityDecrease;
	public JPanel subScrollPanel;
	public JPanel itemNamePanel;
	public orderItem itemInformation;

	private static Font itemNameFont =  new Font("SansSerif", Font.PLAIN, 28); 
	private static Font defaultButtons =  new Font("SansSerif", Font.PLAIN, 25); 

	static Color darkRed = new Color(165,58,59);
	serverViewFunctions serverFunctions;


/**
 * Get itemAdditons and itemSubtractions from database and display them as buttons 
 * @param item The order item to create a display from
 * @param serverFunctions the serverViewFunctions object responsible for communicating with the database 
 */
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
		mainPanel.setBorderPainted(false);
		

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
		itemName.setMinimumSize(new Dimension(350, 50));
		itemName.setMaximumSize(new Dimension(350, 50));
		

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
		itemNamePanel.add(Box.createRigidArea(new Dimension(325,0)));
		itemNamePanel.add(itemSize);
		itemNamePanel.add(Box.createRigidArea(new Dimension(50,10)));
		itemNamePanel.add(quantityDecrease);
		itemNamePanel.add(quantity);
		itemNamePanel.add(quantityIncrease);
		itemNamePanel.add(Box.createRigidArea(new Dimension(50,10)));
		itemNamePanel.add(price);
		itemNamePanel.add(Box.createRigidArea(new Dimension(70,10)));


		mainPanel.add(itemNamePanel);


		for(orderItemModification modification : item.getAdditions()) { 
			JLabel currentAddition = new JLabel("          Addition: " + modification.getIngredientName());
			currentAddition.setOpaque(false);
			currentAddition.setFont(defaultButtons);
			currentAddition.setAlignmentX(Component.LEFT_ALIGNMENT);
			mainPanel.add(currentAddition);
			
		}

		for(orderItemModification modification : item.getSubtractions()) {
			JLabel currentSubtraction = new JLabel("          Subtractions: " + modification.getIngredientName());
			currentSubtraction.setOpaque(false);
			currentSubtraction.setFont(defaultButtons);
			currentSubtraction.setAlignmentX(Component.LEFT_ALIGNMENT);
			mainPanel.add(currentSubtraction);
		}
	}
}

/**
 * @author Alexia Hassan
 */
class SubtractionButton {
	public JButton mainButton;
	public boolean selected;
	public product product;
	private static Font defaultButtons =  new Font("SansSerif", Font.PLAIN, 25); 
	static Color darkRed = new Color(165,58,59);

	/**
	 * Constructor for SubtractionButton class creates buttons with associated products
	 * @param serverFunctions
	 * @param product
	 * @param myIndex
	 */
	public SubtractionButton(serverViewFunctions serverFunctions, product product, int myIndex) {
		this.product = product;
		selected = false;
		
		mainButton = new JButton(product.ingredients().get(myIndex).getName());
		mainButton.setFont(defaultButtons);
		mainButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainButton.setBackground(Color.white);
		mainButton.setForeground(darkRed);
		mainButton.setRolloverEnabled(false);
		mainButton.setFocusPainted(false);
	}
}

/**
 * @author Alexia Hassan
 */
class AdditionButton {
	public JButton mainButton;
	public boolean selected;
	public int id;
	public product product;
	public String name;
	private static Font defaultButtons =  new Font("SansSerif", Font.PLAIN, 25); 
	static Color darkRed = new Color(165,58,59);
	static Color blueHighlight = new Color(184, 204, 220);

	/**
	 * Constructor for additionButton class creates buttons with associated products
	 * @param serverFunctions serverViewFunction item that is responsible for communicating with the database
	 * @param name The name of the ingredient that the button will display
	 * @param myIndex
	 */
	public AdditionButton(serverViewFunctions serverFunctions,  String name, int id, product product) {
		selected = false;
		this.name = name;
		this.id = id;
		this.product = product;
		
		mainButton = new JButton(name);
		mainButton.setFont(defaultButtons);
		mainButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainButton.setBackground(blueHighlight);
		mainButton.setForeground(darkRed);
		mainButton.setRolloverEnabled(false);
		mainButton.setFocusPainted(false);
	}
}
