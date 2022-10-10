import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

class TESTGUI{
    public static void main(String args[]){
       JFrame frame = new JFrame("My First GUI");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       frame.setSize(screenSize.width - 100, screenSize.height);
       //JButton button = new JButton("Press");
       //frame.getContentPane().add(button); // Adds Button to content pane of frame


       /*JLabel label = new JLabel();
		//label.setText("Hi");
		//label.setIcon(icon);
		label.setVerticalAlignment(JLabel.TOP);
		label.setHorizontalAlignment(JLabel.LEFT);
		//label.setBounds(100, 100, 75, 75);*/
		
		JPanel redPanel = new JPanel();
		redPanel.setBackground(Color.red);
		redPanel.setBounds(1125, 0, 275, 1000);
		redPanel.setLayout(new BorderLayout());
		
		JPanel logoPanel = new JPanel();
		logoPanel.setBackground(Color.white);
		logoPanel.setBounds(0, 0, 1125, 75);
		logoPanel.setLayout(new BorderLayout());

        JPanel orangePanel = new JPanel();
		orangePanel.setBackground(Color.orange);
		orangePanel.setBounds(0, 75, 1125, 75);
		orangePanel.setLayout(new BorderLayout());
		
		JPanel greenPanel = new JPanel();
		greenPanel.setBackground(Color.green);
		greenPanel.setBounds(0, 0, 20, 10);
		greenPanel.setLayout(new BorderLayout());

         BufferedImage myPicture;
        JLabel picLabel = new JLabel();
       /*  try {
            myPicture = ImageIO.read(new File("logo.png"));
            picLabel = new JLabel(new ImageIcon(myPicture));
            logoPanel.add(picLabel);    
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        picLabel.setPreferredSize(new Dimension(100, 50));
        picLabel.setMaximumSize(new Dimension(100, 50));*/

        picLabel.setIcon(new ImageIcon(new ImageIcon("logo.png").getImage().getScaledInstance(400, 70, Image.SCALE_SMOOTH)));
                
		/*JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize(750,750);
		frame.setVisible(true);	*/
		logoPanel.add(picLabel);
		frame.add(redPanel);
		frame.add(logoPanel);
        frame.add(orangePanel);
		frame.add(greenPanel);

        frame.setVisible(true);

    }
}