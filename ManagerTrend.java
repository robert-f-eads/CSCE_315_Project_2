import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ManagerTrend extends ManagerViewScreen {
    private orderViewFunctions ovf;
    JButton currentlySelectedButton;
    static Color blueHighlight = new Color(184, 204, 220);
    static Color darkRed = new Color(165,58,59);
	static Font defaultButtons = new Font("SansSerif", Font.PLAIN, 20); //font used in text box
    boolean toggleGenerate;


    public ManagerTrend(ManagerView managerView) {
        super(managerView);
        ovf = new orderViewFunctions();
    }

    public void setTrendView() {
        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = managerView.createButton("Back", buttonWidth, buttonHeight);
        back = formatButtons(back);
        JButton week = formatButtons(managerView.createButton("1 Week", buttonWidth, buttonHeight));
        JButton week2 = formatButtons(managerView.createButton("2 Weeks", buttonWidth, buttonHeight));
        JButton month = formatButtons(managerView.createButton("1 Month", buttonWidth, buttonHeight));
        JButton quarter = formatButtons(managerView.createButton("1 Quarter", buttonWidth, buttonHeight));
        JButton year = formatButtons(managerView.createButton("1 Year", buttonWidth, buttonHeight));
        JButton generateExcess = formatButtons(managerView.createButton("Generate Excess Report", buttonWidth, buttonHeight));
        JButton generateSales = formatButtons(managerView.createButton("Generate Sales Report", buttonWidth, buttonHeight));


        JLabel startDateLabel = managerView.createLabel("Start Date", buttonWidth, buttonHeight);
        startDateLabel.setFont((defaultButtons));
        JLabel endDateLabel = managerView.createLabel("End Date", buttonWidth, buttonHeight);
        endDateLabel.setFont((defaultButtons));
        HintTextField startDate = new HintTextField("yyyy-mm-dd", buttonWidth, buttonHeight);
        startDate.setFont((defaultButtons));
        HintTextField endDate = new HintTextField("yyyy-mm-dd", buttonWidth, buttonHeight);
        endDate.setFont((defaultButtons));

        JTable sales = new JTable();
        sales.setBackground(Color.white);
        // ResultSet row = managerView.myDbConnection.dbQuery("SELECT id AS \"Order Id\", timestamp AS \"Timestamp\", customerfirstname AS \"Customer Name\", rewardsmemberid AS \"Rewards Id\", employeeid AS \"Employee Id\", orderpricetotal AS \"Order Total\" FROM ordertickets");
        // sales.setModel(managerView.resultSetToTableModel(null, row));
        sales.setSize(900, 700);
        JTable excess = new JTable();
        excess.setBackground(Color.white);
        // ResultSet row = managerView.myDbConnection.dbQuery("SELECT id AS \"Order Id\", timestamp AS \"Timestamp\", customerfirstname AS \"Customer Name\", rewardsmemberid AS \"Rewards Id\", employeeid AS \"Employee Id\", orderpricetotal AS \"Order Total\" FROM ordertickets");
        // sales.setModel(managerView.resultSetToTableModel(null, row));
        excess.setSize(900, 700);

        JPanel title = new JPanel();
        title.setBackground(Color.white);
        title.setLayout(new BoxLayout(title, BoxLayout.LINE_AXIS));
        JLabel titleLabel = new JLabel("Trends");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
        title.add(Box.createRigidArea(new Dimension(300, 0)));
        title.add(titleLabel);
        title.add(Box.createRigidArea(new Dimension(300, 0)));


        back.addActionListener(e -> {
            new ManagerView();
            managerView.myFrame.dispose();
        });
        week.addActionListener(e -> {
            if (this.currentlySelectedButton != null) {
                this.currentlySelectedButton.setBackground(Color.white);
            }
            currentlySelectedButton = week;
            currentlySelectedButton.setBackground(blueHighlight);


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
            LocalDateTime now = LocalDateTime.now();  
            LocalDateTime weekAgo = now.minus(1, ChronoUnit.WEEKS);
            startDate.setText(dtf.format(weekAgo));
            endDate.setText(dtf.format(now));

            startDate.setForeground(Color.black);
            endDate.setForeground(Color.black);
        });

        week2.addActionListener(e -> {
            if (this.currentlySelectedButton != null) {
                this.currentlySelectedButton.setBackground(Color.white);
            }
            currentlySelectedButton = week2;
            currentlySelectedButton.setBackground(blueHighlight);


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
            LocalDateTime now = LocalDateTime.now();  
            LocalDateTime week2Ago = now.minus(2, ChronoUnit.WEEKS);
            startDate.setText(dtf.format(week2Ago));
            endDate.setText(dtf.format(now));

            startDate.setForeground(Color.black);
            endDate.setForeground(Color.black);
        });

        month.addActionListener(e -> {
            if (this.currentlySelectedButton != null) {
                this.currentlySelectedButton.setBackground(Color.white);
            }
            currentlySelectedButton = month;
            currentlySelectedButton.setBackground(blueHighlight);


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
            LocalDateTime now = LocalDateTime.now();  
            LocalDateTime monthAgo = now.minus(1, ChronoUnit.MONTHS);
            startDate.setText(dtf.format(monthAgo));
            endDate.setText(dtf.format(now));

            startDate.setForeground(Color.black);
            endDate.setForeground(Color.black);
        });

        year.addActionListener(e -> {
            if (this.currentlySelectedButton != null) {
                this.currentlySelectedButton.setBackground(Color.white);
            }
            currentlySelectedButton = year;
            currentlySelectedButton.setBackground(blueHighlight);


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
            LocalDateTime now = LocalDateTime.now();  
            LocalDateTime yearAgo = now.minus(1, ChronoUnit.YEARS);
            startDate.setText(dtf.format(yearAgo));
            endDate.setText(dtf.format(now));

            startDate.setForeground(Color.black);
            endDate.setForeground(Color.black);
        });

        quarter.addActionListener(e -> {
            if (this.currentlySelectedButton != null) {
                this.currentlySelectedButton.setBackground(Color.white);
            }
            currentlySelectedButton = quarter;
            currentlySelectedButton.setBackground(blueHighlight);


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
            LocalDateTime now = LocalDateTime.now();  
            LocalDateTime quarterAgo = now.minus(3, ChronoUnit.MONTHS);
            startDate.setText(dtf.format(quarterAgo));
            endDate.setText(dtf.format(now));

            startDate.setForeground(Color.black);
            endDate.setForeground(Color.black);
        });


       

        JPanel flow = new JPanel();
        flow.setBackground(Color.white);
        flow.setPreferredSize(new Dimension(1500, 100));
        flow.setMinimumSize(new Dimension(1500, 100));
        flow.setMaximumSize(new Dimension(1500,100));
        flow.add(back);
        //flow.add(orders);
        flow.add(Box.createRigidArea(new Dimension(5,0)));
        flow.add(startDateLabel);
        flow.
        add(Box.createRigidArea(new Dimension(5,0)));
        flow.add(startDate);
        flow.add(Box.createRigidArea(new Dimension(5,0)));
        flow.add(endDateLabel);
        flow.add(Box.createRigidArea(new Dimension(5,0)));
        flow.add(endDate);
        flow.add(Box.createRigidArea(new Dimension(250,0)));
        flow.add(generateSales);
        flow.add(Box.createRigidArea(new Dimension(5,0)));
        flow.add(generateExcess);
        flow.add(Box.createRigidArea(new Dimension(75, 0)));
        flow.add(week);
        flow.add(week2);
        flow.add(month);
        flow.add(quarter);
        flow.add(year);

        
        managerView.borderPanel.setLayout(new BoxLayout(managerView.borderPanel, BoxLayout.PAGE_AXIS));
        managerView.borderPanel.setBackground(Color.white);
        managerView.borderPanel.add(title);
        managerView.borderPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        managerView.borderPanel.add(flow);
        managerView.borderPanel.add(new JScrollPane(sales));

        
        generateSales.addActionListener(e -> {
            // sales.setModel(managerView.resultSetToTableModel(null, 
            //     ovf.generateSalesReportBetweenDates(new dateStruct(startDate.getText()), 
            //         new dateStruct(endDate.getText()), true)));


            // managerView.borderPanel.removeAll();
            // managerView.borderPanel.add(flow);
            // managerView.borderPanel.add(new JScrollPane(sales));
            // managerView.myFrame.revalidate();
            // managerView.myFrame.repaint();
        });

        generateExcess.addActionListener(e -> {
            excess.setModel(managerView.resultSetToTableModel(null, 
                ovf.generateExcessReport(// new dateStruct(startDate.getText()), 
                    new dateStruct(endDate.getText()), true)));

                    

            managerView.borderPanel.removeAll();
            managerView.borderPanel.add(flow);
            managerView.borderPanel.add(new JScrollPane(excess));
            managerView.myFrame.revalidate();
            managerView.myFrame.repaint();
        });

       
        managerView.borderPanel.revalidate();
        managerView.borderPanel.repaint();
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
}

