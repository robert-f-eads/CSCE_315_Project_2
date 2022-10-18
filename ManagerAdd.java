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
        nameAndCost.setLayout(new BoxLayout(nameAndCost, BoxLayout.LINE_AXIS));

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
        JLabel additionsHeader = new JLabel("Product Ingredients");
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
		searchResultPanel.setPreferredSize(new Dimension(1050, 200));
		searchResultPanel.setMinimumSize(new Dimension(1050, 200));
		searchResultPanel.setMaximumSize(new Dimension(1050, 200));
		searchResultPanel.setBackground(Color.green);
	

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
		additionButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		additionButtonPanel.setBackground(Color.white);
		        

        managerView.mainPanel.add(nameAndCost);
        managerView.mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		searchPanel.add(searchButton);
		searchPanel.add(additionSearchTextField);		
			
		managerView.mainPanel.add(additionsHeader);
		managerView.mainPanel.add(additionButtonPanel);
		managerView.mainPanel.add(searchPanel);
		managerView.mainPanel.add(searchResultPanel);
		managerView.mainPanel.add(Box.createRigidArea(new Dimension(0, 400)));
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
		searchPanel.removeAll();
		searchPanel.setLayout(new FlowLayout());
	

		try {
			dbConnection.createDbConnection();
			String sqlStatement = String.format("SELECT id FROM ingredients WHERE name ILIKE '%s%s%s' ", "%", searchBarText, "%");
			ResultSet results = dbConnection.dbQuery(sqlStatement);
			while(results.next()) {
				ingredient tempIngredient = managerView.serverFunctions.getIngredient(results.getInt("id")); 
				JButton button = new JButton(tempIngredient.getName());
				button.setFont(defaultButtons);
				searchPanel.add(button);
				button.setAlignmentX(Component.LEFT_ALIGNMENT);
				button.setBackground(Color.white);
				button.setForeground(darkRed);
				searchPanel.add(Box.createRigidArea(new Dimension(30, 0)));
				button.addActionListener(e ->
				{
					ingredients.add(tempIngredient);

					searchResultPanel.remove(button);

					managerView.mainPanel.revalidate();
					managerView.mainPanel.repaint();

					updateAdditions(searchBarText, searchPanel);
					redrawButtonAdditions();
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

	public void redrawButtonAdditions() {
		additionButtonPanel.removeAll();
		for (int k = 0; k < ingredients.size();  k++) {
			AdditionButtonManager tempButton = new AdditionButtonManager(ingredients.get(k).getName(), ingredients.get(k));
			tempButton.mainButton.addActionListener(e -> {
						ingredients.remove(tempButton.ingredient);
						additionButtons.remove(tempButton);
						updateAdditions(currentSearchEntry, additionButtonPanel);
						redrawButtonAdditions();
						managerView.mainPanel.revalidate();
						managerView.mainPanel.repaint();
				} 
			);
			additionButtonPanel.add(tempButton.mainButton);
		} 
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
