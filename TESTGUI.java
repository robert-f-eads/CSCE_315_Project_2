import javax.swing.*;
import javax.swing.border.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class TESTGUI{
	static int maxHeight = 1080;
	static int maxWidth = 1920;
    public static void main(String args[]){

		//Create JFrame and initial settings
		JFrame frame = new JFrame("Smoothie King");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(maxWidth, maxHeight));
		frame.setMinimumSize(new Dimension(maxWidth, maxHeight));
		frame.setMaximumSize(new Dimension(maxWidth, maxHeight));

		


		frame.setResizable(false);
		frame.setLayout(null);


		//Logo panel will house Smoothie King logo in top left 
		JPanel logoPanel = new JPanel();
		logoPanel.setBackground(Color.blue);
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
		searchTextField.setLocation(0, 75);
		Font searchFont = new Font("SansSerif", Font.PLAIN, 20); //font used in text box
		searchTextField.setFont(searchFont);

		//Create button component of search bar
		JButton searchButton = new JButton(new ImageIcon(((new ImageIcon("searchIcon.png")).getImage()).getScaledInstance(43, 43, java.awt.Image.SCALE_SMOOTH)));
		searchButton.setPreferredSize(new Dimension(50,50));
		searchButton.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					String typedValue = searchTextField.getText();
					System.out.println(typedValue);
				}
			}
		);

		//Set searchButton colors
		searchButton.setForeground(Color.black);
		searchButton.setBackground(Color.white);


		//Set border colors for search bar and search button
		Border line = new LineBorder(Color.black);
		searchTextField.setBorder(line);
		searchButton.setBorder(line);

		//Add both search bar and search button to panel in appropriate place 
		searchPanel.add(searchButton, BorderLayout.LINE_START);
		searchPanel.add(searchTextField, BorderLayout.LINE_END);


		//Creating server name label
		JLabel serverName = new JLabel("Hey");
		//serverName.setBounds(32, 0, 300, 300);
		serverName.setHorizontalAlignment(SwingConstants.CENTER);
		serverName.setVerticalAlignment(SwingConstants.CENTER);;

		//Creating server font
		Font serverNameFont = new Font("SansSerif", Font.BOLD, 35); //font used in text box
		serverName.setFont(serverNameFont);

		//for testing purposes, REMOVES
		//serverName.setBackground(Color.blue);
		//serverName.setOpaque(true);


		//Creating default font for rest of buttons
		Font defaultButtons = new Font("SansSerif", Font.PLAIN, 20); //font used in text box

		//Creating Logout Button
		JButton logout = new JButton("Logout");
		//logout.setBounds(32, 50, 150, 75);
		logout.setFont(defaultButtons);
		logout.setBackground(Color.white);
		logout.setMargin(new Insets(0,1,0,1));



		//Create right panel to house side buttons
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.red);
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
		gbc.weightx = 1.0;
		gbc.weighty = 0.1; 
		//gbc.ipady = 50;
		rightPanel.add(logout, gbc);
		
		
		//Create left panel to house logo, search, and all other functionalities
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.white);
		leftPanel.setBounds(0, 0, 1200, maxHeight);
		leftPanel.setLayout(null);

		//Adding logo and search to left panel
		leftPanel.add(logoPanel);
		leftPanel.add(searchPanel);

        //Adding right and left panel to main frame
		frame.add(rightPanel);
		frame.add(leftPanel);

        frame.setVisible(true);

    }
}