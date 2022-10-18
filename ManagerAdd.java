import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;


public class ManagerAdd extends ManagerViewScreen {
	dbFunctions dbConnection = new dbFunctions();

    static Font searchFont = new Font("SansSerif", Font.PLAIN, 20); 
    static Font defaultButtons = new Font("SansSerif", Font.PLAIN, 23); 
	static Color darkRed = new Color(165,58,59);

    String productNameTextFieldEntry;
    String currentSearchEntry;
    int productCostTextFieldEntry;
	Vector<ingredient> ingredients = new Vector<ingredient>();

	JPanel searchResultPanel;
	JPanel additionButtonPanel;
	JTextField searchBarText;

	Vector<AdditionButtonManager> additionButtons = new Vector<AdditionButtonManager>();


    public ManagerAdd(ManagerView managerView) {super(managerView);}

    public void setAddView() {
        int generalHeight = 42;
        
        managerView.mainPanel.removeAll();
        managerView.mainPanel.setLayout(new BoxLayout(managerView.mainPanel, BoxLayout.PAGE_AXIS));
        managerView.mainPanel.setBackground(Color.white);

        JPanel nameAndCost = new JPanel();
		nameAndCost.setBackground(Color.white);
        nameAndCost.setLayout(new BoxLayout(nameAndCost, BoxLayout.LINE_AXIS));
		nameAndCost.setAlignmentX(Component.LEFT_ALIGNMENT);

        HintTextField productName = (HintTextField) formatTextArea(new HintTextField("Product Name", 150, generalHeight));
        productName.setFont(new Font("SansSerif", Font.PLAIN, 28)); 

        HintTextField productCost = (HintTextField) formatTextArea(new HintTextField("Product Cost", 150, generalHeight));
        productCost.setFont(new Font("SansSerif", Font.PLAIN, 28)); 


        JButton back = formatButtons(new JButton("Back"));
        nameAndCost.add(back);
        nameAndCost.add(Box.createRigidArea(new Dimension(600,0)));
        nameAndCost.add(productName);
        nameAndCost.add(Box.createRigidArea(new Dimension(25,0)));
        nameAndCost.add(productCost);

        back.addActionListener(e -> {
            new ManagerView(managerView.serverFunctions);
            managerView.myFrame.dispose();
        });

    /////////////////////////////////////////// STAND BY SHITS FUCKED//////////////////////////////////
		JPanel title = new JPanel();
		title.setLayout(new BoxLayout(title, BoxLayout.LINE_AXIS));

        JLabel additionsHeader = new JLabel("Product Ingredients");
		additionsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
		additionsHeader.setForeground(darkRed);
		additionsHeader.setBackground(Color.white);

		title.add(Box.createRigidArea(new Dimension(50, 0)));
		title.setBackground(Color.white);
		title.add(additionsHeader);
		title.setAlignmentX(Component.LEFT_ALIGNMENT);
	
		//Creating additions header font
		Font additionsHeaderFont = new Font("SansSerif", Font.PLAIN, 28);
		additionsHeader.setFont(additionsHeaderFont);
	
		//Search panel for additions
		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(Color.white);
		searchPanel.setPreferredSize(new Dimension(1100, 50));
		searchPanel.setMaximumSize(new Dimension(1100, 50));
		searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchPanel.setFont(defaultButtons);
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));
	
		//Create text component of search bar
		JTextField additionSearchTextField = new JTextField();
		additionSearchTextField.setPreferredSize(new Dimension(1050,50));
		additionSearchTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchPanel.setFont(defaultButtons);
		additionSearchTextField.setFont(searchFont);
	
		// Panel to display search results
		searchResultPanel = new JPanel();
		searchResultPanel.setBackground(Color.white);
		searchResultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		searchResultPanel.setPreferredSize(new Dimension(1050, 350));
		searchResultPanel.setMinimumSize(new Dimension(1050, 350));
		searchResultPanel.setMaximumSize(new Dimension(1050, 350));
	
		JPanel searchResultPanelHolder = new JPanel();
		searchResultPanelHolder.setAlignmentX(Component.LEFT_ALIGNMENT);
		searchResultPanelHolder.setLayout(new BoxLayout(searchResultPanelHolder, BoxLayout.LINE_AXIS));
		searchResultPanelHolder.setBackground(Color.white);
		searchResultPanelHolder.add(Box.createRigidArea(new Dimension(50, 0)));
		searchResultPanelHolder.setMaximumSize(new Dimension(1098, 350));
		searchResultPanelHolder.add(new JScrollPane(searchResultPanel));


		//Create button component of search bar
		JButton searchButton = new JButton(new ImageIcon(((new ImageIcon("searchIcon.png")).getImage()).getScaledInstance(43, 43, java.awt.Image.SCALE_SMOOTH)));
		searchButton.setPreferredSize(new Dimension(50,50));
		searchButton.setBackground(Color.white);
		searchButton.addActionListener(e -> {				
					currentSearchEntry = additionSearchTextField.getText();
					updateAdditions(currentSearchEntry, searchResultPanel); 
			} );
	

		additionButtonPanel = new JPanel();
		additionButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		additionButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		additionButtonPanel.setBackground(Color.white);
		        

        managerView.mainPanel.add(nameAndCost);
        managerView.mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));

		searchPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		searchPanel.add(searchButton);
		searchPanel.add(additionSearchTextField);	
		
		JButton createItem = formatButtons(new JButton("Finalize Item"));
		createItem.addActionListener(e -> {
			//ROBERT HELP
			product newProduct = new product(-1, productName.getText(), Double.parseDouble(productCost.getText()));
			newProduct.setIngredients(ingredients);
			orderFunctions.addSeasonalItem(newProduct);
		});
			
		managerView.mainPanel.add(title);
		managerView.mainPanel.add(new JScrollPane(additionButtonPanel));
		managerView.mainPanel.add(Box.createRigidArea(new Dimension(0, 175)));
		managerView.mainPanel.add(searchPanel);
		managerView.mainPanel.add(searchResultPanelHolder);
		managerView.mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		managerView.mainPanel.add(createItem);
		managerView.mainPanel.add(Box.createRigidArea(new Dimension(0, 75)));
		//managerView.mainPanel.add(Box.createRigidArea(new Dimension(0, 75)));
        managerView.mainPanel.revalidate();
        managerView.mainPanel.repaint();
    }

    JTextField formatTextArea(JTextField b) {
        b.setFont(new Font("SansSerif", Font.PLAIN, 15));
        b.setBackground(Color.white);
        b.setMinimumSize(new Dimension(400,75));
        b.setMaximumSize(new Dimension(400,75));
        return b;
    }

    public JButton formatButtons(JButton button) {
		button.setForeground(darkRed);
		button.setBackground(Color.white);
		button.setFont(defaultButtons);
		button.setRolloverEnabled(false);
		button.setFocusPainted(false);
		button.setAlignmentY(Component.CENTER_ALIGNMENT);

		return button;
    }


	public void updateAdditions(String searchBarText, JPanel searchPanel) {
		searchResultPanel.removeAll();
		searchResultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		try {
			dbConnection.createDbConnection();
			String sqlStatement = String.format("SELECT id FROM ingredients WHERE name ILIKE '%s%s%s' ", "%", searchBarText, "%");
			ResultSet results = dbConnection.dbQuery(sqlStatement);
			while(results.next()) {
				
				ingredient tempIngredient = managerView.serverFunctions.getIngredient(results.getInt("id")); 
				if (!ingredients.contains(tempIngredient)) {
					JButton button = new JButton(tempIngredient.getName());
					button.setFont(defaultButtons);
					searchResultPanel.add(button);
					button.setAlignmentX(Component.CENTER_ALIGNMENT);
					button.setBackground(Color.white);
					button.setForeground(darkRed);
					searchPanel.add(Box.createRigidArea(new Dimension(30, 0)));
					button.addActionListener(e ->
					{
						ingredients.add(tempIngredient);
						searchResultPanel.remove(button);


						redrawButtonAdditions();
						updateAdditions(searchBarText, searchPanel);


						managerView.mainPanel.revalidate();
						managerView.mainPanel.repaint();

					} );

					managerView.mainPanel.revalidate();
					managerView.mainPanel.repaint();

				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		} 
	
		
		managerView.mainPanel.revalidate();
		managerView.mainPanel.repaint();
	}

	public void redrawButtonAdditions() {
		additionButtonPanel.removeAll();
		for (int k = 0; k < ingredients.size();  k++) {
			AdditionButtonManager tempButton = new AdditionButtonManager(ingredients.get(k).getName(), ingredients.get(k));
			tempButton.mainButton.addActionListener(e -> {
						ingredients.remove(tempButton.ingredient);
						additionButtons.remove(tempButton);
						additionButtonPanel.remove(tempButton.mainButton);
						updateAdditions(currentSearchEntry, additionButtonPanel);
						redrawButtonAdditions();
						managerView.mainPanel.revalidate();
						managerView.mainPanel.repaint();
				
				} 
			);
			additionButtonPanel.add(tempButton.mainButton);						

		} 
		managerView.mainPanel.revalidate();
		managerView.mainPanel.repaint();
	}


}
	
class AdditionButtonManager {
	public JButton mainButton;
	public boolean selected;
	public int id;
	public ingredient ingredient;
	public String name;
	private static Font defaultButtons =  new Font("SansSerif", Font.PLAIN, 25); 
	static Color darkRed = new Color(165,58,59);
	static Color blueHighlight = new Color(184, 204, 220);


	public AdditionButtonManager(String name, ingredient ingredientTemp) {
		selected = false;
		this.name = name;
		this.ingredient = ingredientTemp;
		
		mainButton = new JButton(name);
		mainButton.setFont(defaultButtons);
		mainButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainButton.setBackground(blueHighlight);
		mainButton.setForeground(darkRed);
		mainButton.setRolloverEnabled(false);
		mainButton.setFocusPainted(false);
	}
}
