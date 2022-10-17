import javax.swing.*;
import java.awt.*;

public class ManagerReorder extends ManagerViewScreen {
    private orderViewFunctions ovf;

    public ManagerReorder(ManagerView managerView) {
        super(managerView);
        ovf = new orderViewFunctions();
    }
    
    public void setReorderView() {
        int buttonWidth = 150;
        int buttonHeight = 150;
        JButton back = managerView.createButton("Back", buttonWidth, buttonHeight);
        JButton export = managerView.createButton("Refresh", buttonWidth, buttonHeight);

        JLabel reorder = managerView.createLabel("Inventory", buttonWidth, buttonHeight);

        back.addActionListener(e -> {
            new ManagerView();
            managerView.myFrame.dispose();
        });
        export.addActionListener(e -> {
            System.out.println("Export to csv");
        });

        JPanel flowTop = new JPanel();
        flowTop.setBackground(Color.white);
        flowTop.setPreferredSize(new Dimension(1500, 50));
        flowTop.setMinimumSize(new Dimension(1500, 50));
        flowTop.setMaximumSize(new Dimension(1500,50));
        flowTop.add(back);
        flowTop.add(Box.createRigidArea(new Dimension(1300,0)));

        JPanel flowBot = new JPanel();
        flowBot.setBackground(Color.white);
        flowBot.setPreferredSize(new Dimension(1500, 50));
        flowBot.setMinimumSize(new Dimension(1500, 50));
        flowBot.setMaximumSize(new Dimension(1500,50));
        flowBot.add(Box.createRigidArea(new Dimension(1200,0)));
        flowBot.add(export);

        JTable restock = new JTable(managerView.resultSetToTableModel(null, ovf.generateRestockReport()));

        managerView.borderPanel.add(flowTop, BorderLayout.NORTH);
        managerView.borderPanel.add(new JScrollPane(restock), BorderLayout.CENTER);
        managerView.borderPanel.add(flowBot, BorderLayout.SOUTH);
    }
}
