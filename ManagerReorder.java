import javax.swing.*;

public class ManagerReorder extends ManagerViewScreen {
    public ManagerReorder(ManagerView managerView) {super(managerView);}
    
    public void setReorderView() {
        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = managerView.createButton("Back", buttonWidth, buttonHeight);
        JButton export = managerView.createButton("Export to CSV", buttonWidth, buttonHeight);

        JLabel reorder = managerView.createLabel("Inventory", buttonWidth, buttonHeight);

        back.addActionListener(e -> {
            managerView.setHomeView();
        });
        export.addActionListener(e -> {
            System.out.println("Export to csv");
        });

        managerView.myPanels[0][0].add(back);
        managerView.myPanels[0][2].add(reorder);
        managerView.myPanels[4][4].add(export);
    }
}
