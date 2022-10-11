
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TilePanel {
    public JPanel mainPanel;
    public Image imageIcon;
    public JLabel itemName;
    public TilePanel(String itemNameString) {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        this.itemName = new JLabel(itemNameString);

        JLabel picLabel = new JLabel();
		picLabel.setIcon(new ImageIcon(new ImageIcon("ProductImages/" + itemNameString + ".png").getImage().getScaledInstance(400, 70, Image.SCALE_SMOOTH)));

        mainPanel.add(picLabel, BorderLayout.PAGE_START);
        mainPanel.add(itemName, BorderLayout.PAGE_END);


    }
    
    

    

}
