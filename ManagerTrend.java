import javax.swing.*;
import java.awt.*;

public class ManagerTrend extends ManagerViewScreen {
    private orderViewFunctions ovf;
    JButton currentlySelectedButton;
    static Color blueHighlight = new Color(184, 204, 220);


    public ManagerTrend(ManagerView managerView) {
        super(managerView);
        ovf = new orderViewFunctions();
    }
    
    public void setTrendView() {
        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = managerView.createButton("Back", buttonWidth, buttonHeight);
        JButton week = managerView.createButton("1 Week", buttonWidth, buttonHeight);
        week.addActionListener(e -> {
            if (currentlySelectedButton != null) {
                currentlySelectedButton.setBackground(Color.white);
            }
            currentlySelectedButton = week;
            week.setBackground(blueHighlight);
        });

        JButton week2 = managerView.createButton("2 Weeks", buttonWidth, buttonHeight);
        week2.addActionListener(e -> {
            if (currentlySelectedButton != null) {
                currentlySelectedButton.setBackground(Color.white);
            }
            currentlySelectedButton = week2;
            week2.setBackground(blueHighlight);
        });

        JButton month = managerView.createButton("1 Month", buttonWidth, buttonHeight);
        month.addActionListener(e -> {
            if (currentlySelectedButton != null) {
                currentlySelectedButton.setBackground(Color.white);
            }
            currentlySelectedButton = month;
            month.setBackground(blueHighlight);
        });

        JButton quarter = managerView.createButton("1 Quarter", buttonWidth, buttonHeight);
        quarter.addActionListener(e -> {
            if (currentlySelectedButton != null) {
                currentlySelectedButton.setBackground(Color.white);
            }
            currentlySelectedButton = quarter;
            quarter.setBackground(blueHighlight);
        });

        JButton year = managerView.createButton("1 Year", buttonWidth, buttonHeight);
        year.addActionListener(e -> {
            if (currentlySelectedButton != null) {
                currentlySelectedButton.setBackground(Color.white);
            }
            currentlySelectedButton = year;
            year.setBackground(blueHighlight);
        });

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


        
        // TODO make all other trends also be allowed to generate, expand gui to include selection of these trends
        generate.addActionListener(e -> {
            JTable sales = new JTable(managerView.resultSetToTableModel(null, 
                ovf.generateSalesReportBetweenDates(new dateStruct("2020-10-10"), 
                    new dateStruct("2022-10-10"), true)));
            managerView.borderPanel.removeAll();
            managerView.borderPanel.add(pageStart);
            managerView.borderPanel.add(new JScrollPane(sales));
            managerView.myFrame.revalidate();
            managerView.myFrame.repaint();
        });
    }

    JTextField formatTextArea(JTextField b) {
        b.setFont(new Font("SansSerif", Font.PLAIN, 23));
        b.setBackground(Color.white);
        b.setMinimumSize(new Dimension(400,75));
        b.setMaximumSize(new Dimension(400,75));
        return b;
    }





        
}

