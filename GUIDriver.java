
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class GUIDriver {
	public static void main(String args[]) {
		GUIWindow newGUIWindow = new GUIWindow();
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

	public void updateTilesFromSearch(String searchBarText) {
		mainPanel.removeAll();

		TilePanel test = new TilePanel("pumpkin Whatever");
		mainPanel.add(test.mainPanel);
		//Use search string to display results etc
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	public void updateMainView() {
		mainPanel.removeAll();
		mainPanel.setLayout(new GridBagLayout());

		ItemInOrder testItem = new ItemInOrder("pumpkin Whatever");
		mainPanel.add(testItem.mainPanel);

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

					updateTilesFromSearch(currentTextFieldEntry);
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
		serverName.setVerticalAlignment(SwingConstants.CENTER);

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


		JButton cancel = new JButton("      Cancel      ");
		//cancel.setBounds(32, 50, 150, 75);
		cancel.setFont(defaultButtons);
		cancel.setBackground(Color.white);
		cancel.setForeground(darkRed);
		cancel.setRolloverEnabled(false);
		cancel.setFocusPainted(false);
		cancel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, darkRed));	


		JButton finishAndPay = new JButton("Finish and Pay");
		//finishAndPay.setBounds(32, 50, 150, 75);
		finishAndPay.setFont(defaultButtons);
		finishAndPay.setBackground(Color.white);
		finishAndPay.setForeground(darkRed);
		finishAndPay.setRolloverEnabled(false);
		finishAndPay.setFocusPainted(false);
		finishAndPay.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, darkRed));	


		//Create right panel to house side buttons
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.white);
		rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));		
		rightPanel.setBounds(1150, 0, 350, maxHeight);
		rightPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		//Add objects to panel
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		gbc.weighty = 0; 
		gbc.ipady = 30;
		rightPanel.add(serverName, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.weightx = 0.0;
		gbc.weighty = 4.5; 
		gbc.fill = GridBagConstraints.NONE;
		rightPanel.add(logout, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0; 
		gbc.fill = GridBagConstraints.NONE;
		rightPanel.add(cancel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.weightx = 0.0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.NONE;
		rightPanel.add(finishAndPay, gbc);
		
		
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
	private ArrayList<String> itemAdditons;
	private ArrayList<String> itemSubtractions;
	private static Font itemNameFont =  new Font("SansSerif", Font.PLAIN, 23); //font used in text box

	//get itemAdditons and itemSubtractions from database and display them
	public ItemInOrder(String itemNameString) {
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.blue);
		mainPanel.setLayout(new BorderLayout());

		JPanel subScrollPanel = new JPanel();
		subScrollPanel.setLayout(new GridBagLayout());
		subScrollPanel.setBackground(Color.red);
		GridBagConstraints gbc = new GridBagConstraints();

		
		itemName = new JLabel(itemName + "\n today's additions are as follows: \n pain, pain, pain pain pain");
		itemName.setFont(itemNameFont);

		//for loop, for all additions
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0; 
		gbc.gridy = 0; //set in for loop

		subScrollPanel.add(itemName, gbc);

		JScrollPane scrollPane = new JScrollPane(subScrollPanel);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

	
	}


}