package panels;

import frames.BarFrame;
import models.Order;
import models.Product;
import models.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class HistoryPanel extends BasePanel implements ActionListener {
    private User loggedUser;
    public HistoryPanel(BarFrame frame, User loggedUser, Map<Integer, ArrayList<Product>> orders,Map<Integer, ArrayList<Order>> histories) {
        super(frame);
        this.loggedUser = loggedUser;
        JButton backButton = new JButton("Назад");
        backButton.setBounds(10,5, 120, 30);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addActionListener(e -> help.showBackScreen(frame, loggedUser, orders));
        add(backButton);

        int buttonX = 45;
        int buttonY = frame.getHeight() - 160;
        for (Map.Entry<Integer, ArrayList<Order>> ordersHistory : histories.entrySet()){
            if (ordersHistory.getKey() == 11){
                buttonX = 45;
                buttonY = frame.getHeight() - 100;
            }
            buttonX += 60;
            JButton tableButton = new JButton(Integer.toString(ordersHistory.getKey()));
            tableButton.addActionListener(this);
            tableButton.setBounds(buttonX, buttonY, 50, 50);
            add(tableButton);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedTable = ((JButton) e.getSource()).getText();
        help.showHistory(loggedUser, Integer.parseInt(selectedTable));
    }
}