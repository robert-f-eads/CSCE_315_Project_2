import javax.swing.*;
import java.awt.*;

public class ManagerTrend extends ManagerViewScreen {
    public ManagerTrend(ManagerView managerView) {super(managerView);}
    
    public void setTrendView() {
        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = managerView.createButton("Back", buttonWidth, buttonHeight);
        JButton week = managerView.createButton("1 Week", buttonWidth, buttonHeight);
        JButton week2 = managerView.createButton("2 Weeks", buttonWidth, buttonHeight);
        JButton month = managerView.createButton("1 Month", buttonWidth, buttonHeight);
        JButton quarter = managerView.createButton("1 Quarter", buttonWidth, buttonHeight);
        JButton year = managerView.createButton("1 Year", buttonWidth, buttonHeight);
        JButton generate = managerView.createButton("Generate", buttonWidth, buttonHeight);

        JLabel inventory = managerView.createLabel("Trends", buttonWidth, buttonHeight);
        inventory.setFont(new Font("SansSerif", Font.PLAIN, 28));
        inventory.setBackground(Color.green);
        inventory.setAlignmentX(Component.CENTER_ALIGNMENT);


        HintTextField searchText = (HintTextField) formatTextArea(new HintTextField("Search", buttonWidth, buttonHeight));
        HintTextField id = (HintTextField) formatTextArea(new HintTextField("ID", 200, buttonHeight));


        back.addActionListener(e -> {
            new ManagerView();
            managerView.myFrame.dispose();
        });

    

        JPanel title = new JPanel();
        title.setLayout(new BoxLayout(title, BoxLayout.LINE_AXIS));
        title.setBackground(Color.white);
        title.add(Box.createRigidArea(new Dimension(600, 0)));
        title.add(inventory);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel topButtons = new JPanel();
        topButtons.setBackground(Color.white);
        topButtons.setLayout(new BoxLayout(topButtons, BoxLayout.LINE_AXIS));
        topButtons.add(back);
        topButtons.add(Box.createRigidArea(new Dimension(200, 0)));
        topButtons.add(back);
        topButtons.add(week);
        topButtons.add(week2);
        topButtons.add(month);
        topButtons.add(quarter);
        topButtons.add(year);
        topButtons.add(Box.createRigidArea(new Dimension(75, 0)));
        topButtons.add(generate);


        topButtons.add(Box.createRigidArea(new Dimension(200, 0)));
        topButtons.setAlignmentX(Component.LEFT_ALIGNMENT);


        JPanel pageStart = new JPanel();
        pageStart.setBackground(Color.white);
        pageStart.setLayout(new BoxLayout(pageStart, BoxLayout.PAGE_AXIS));
        pageStart.setAlignmentX(Component.CENTER_ALIGNMENT);

        pageStart.add(title);
        pageStart.add(Box.createRigidArea(new Dimension(0, 25)));
        pageStart.add(topButtons);

        JPanel south = new JPanel();
       
        managerView.borderPanel.setLayout(new BoxLayout(managerView.borderPanel, BoxLayout.PAGE_AXIS));
        managerView.borderPanel.add(pageStart);
        managerView.borderPanel.add(south);
        managerView.borderPanel.setBackground(Color.white);
        managerView.borderPanel.revalidate();
        managerView.borderPanel.repaint();
        managerView.myFrame.add(managerView.borderPanel);
    }

    JTextField formatTextArea(JTextField b) {
        b.setFont(new Font("SansSerif", Font.PLAIN, 23));
        b.setBackground(Color.white);
        b.setMinimumSize(new Dimension(400,75));
        b.setMaximumSize(new Dimension(400,75));
        return b;
    }





        
}

