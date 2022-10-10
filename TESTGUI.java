import javax.swing.*;
import javax.swing.border.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class TESTGUI{
    public static void main(String args[]){

		//Create JFrame and initial settings
		JFrame frame = new JFrame("Smoothie King");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(1300, 900);
		//frame.setResizable(false);
		frame.setLayout(null);



		//Logo panel will house Smoothie King logo in top left 
		JPanel logoPanel = new JPanel();
		logoPanel.setBackground(Color.white);
		logoPanel.setBounds(0, 0, 1125, 75);
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
		Font searchFont = new Font("SansSerif", Font.PLAIN, 20); //font used in text box
		JTextField searchTextField = new JTextField();
		searchTextField.setPreferredSize(new Dimension(1050,50));
		searchTextField.setHorizontalAlignment(JTextField.LEFT);
		searchTextField.setLocation(0, 75);
		searchTextField.setFont(searchFont);

		//Create button component of search bar
		JButton searchButton = new JButton(new ImageIcon(((new ImageIcon("searchIcon.png")).getImage()).getScaledInstance(43, 43, java.awt.Image.SCALE_SMOOTH)));
		//JButton searchButton = new JButton(searchIcon.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH));
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
		searchButton.setForeground(Color.BLACK);
		searchButton.setBackground(Color.WHITE);


		//Set border colors for search bar and search button
		Border line = new LineBorder(Color.BLACK);
		searchTextField.setBorder(line);
		searchButton.setBorder(line);

		//Add both search bar and search button to panel in appropriate place 
		searchPanel.add(searchButton, BorderLayout.LINE_START);
		searchPanel.add(searchTextField, BorderLayout.LINE_END);

		//Create right panel to house side buttons
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.red);
		rightPanel.setBounds(1125, 0, 275, 1000);
		rightPanel.setLayout(new BorderLayout());
		
		
		//Create left panel to house logo, search, and all other functionalities
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.green);
		leftPanel.setBounds(0, 0, 1124, 1000);
		leftPanel.setLayout(null);

		leftPanel.add(logoPanel);
		leftPanel.add(searchPanel);

                
		frame.add(rightPanel);
		frame.add(leftPanel);

        frame.setVisible(true);

    }
}