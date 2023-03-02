package panels;

import frames.BarFrame;
import models.Product;
import models.User;
import models.UserType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class TablesPanel extends BasePanel implements ActionListener {
    JButton tableButton;
    String selectedTable;
    private final User loggedUser;
    private final Map<Integer, ArrayList<Product>> orders;
    public TablesPanel(BarFrame frame, User loggedUser, Map<Integer, ArrayList<Product>> orders) {
        super(frame);
        this.loggedUser = loggedUser;
        this.orders = orders;

        JButton backButton = new JButton("Назад");
        backButton.setBounds(10,5, 120, 30);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addActionListener(e -> {
            help.lockDecreasingOfProducts();
            help.showBackScreen(frame, loggedUser, orders);
        });
        add(backButton);

            int buttonX = 45;
            int buttonY = frame.getHeight() - 160;
            for (Map.Entry<Integer, ArrayList<Product>> order : orders.entrySet()){
                if (order.getKey() == 11){
                    buttonX = 45;
                    buttonY = frame.getHeight() - 100;
                }
                buttonX += 60;
                tableButton = new JButton(Integer.toString(order.getKey()));
                tableButton.addActionListener(this);
                tableButton.setBounds(buttonX, buttonY, 50, 50);
                add(tableButton);
            }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectedTable = ((JButton)e.getSource()).getText();
        help.tableNumber = Integer.parseInt(selectedTable);

        help.isWorkingOnTable(Integer.parseInt(selectedTable), loggedUser);
    }
}
