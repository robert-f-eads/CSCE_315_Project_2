import javax.swing.*;

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

        JLabel trends = managerView.createLabel("Trends", buttonWidth, buttonHeight);
        JLabel startDateLabel = managerView.createLabel("Start Date", buttonWidth, buttonHeight);
        JLabel endDateLabel = managerView.createLabel("End Date", buttonWidth, buttonHeight);
        HintTextField startDate = new HintTextField("yyyy-mm-dd", buttonWidth, buttonHeight);
        HintTextField endDate = new HintTextField("yyyy-mm-dd", buttonWidth, buttonHeight);

        back.addActionListener(e -> {
            managerView.myFrame.dispose();
            new ManagerView();
        });
        generate.addActionListener(e -> {
            System.out.println("Generate trends");
        });

        managerView.myPanels[0][0].add(back);
        managerView.myPanels[0][2].add(trends);
        managerView.myPanels[1][0].add(startDateLabel);
        managerView.myPanels[1][2].add(startDate);
        managerView.myPanels[2][0].add(endDateLabel);
        managerView.myPanels[2][2].add(endDate);
        managerView.myPanels[2][4].add(generate);
        managerView.myPanels[3][0].add(week);
        managerView.myPanels[3][1].add(week2);
        managerView.myPanels[3][2].add(month);
        managerView.myPanels[3][3].add(quarter);
        managerView.myPanels[3][4].add(year);
    }
}
