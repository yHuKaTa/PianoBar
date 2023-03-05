package panels;

import frames.BarFrame;
import models.Order;
import models.Product;
import models.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;


public class OwnerHomePanel extends BasePanel{

    public OwnerHomePanel(BarFrame frame, User loggedUser, Map<Integer, ArrayList<Product>> orders, Map<Integer, ArrayList<Order>> histories) {
        super(frame);

        JButton tablesButton = new JButton("Поръчки");
        tablesButton.setBounds(frame.getWidth() / 2 - 75,frame.getHeight() / 4 - 60,150,40);
        tablesButton.addActionListener(e -> frame.router.showTablesPanel(loggedUser, orders));
        add(tablesButton);

        JButton deliveryButton = new JButton("Доставки");
        deliveryButton.setBounds(frame.getWidth() / 2 - 75, tablesButton.getY() + 70, 150,40);
        deliveryButton.addActionListener(e -> frame.router.showDeliveryPanel(loggedUser, orders));
        add(deliveryButton);

        JButton historyButton = new JButton("История");
        historyButton.setBounds(frame.getWidth() / 2 - 75, deliveryButton.getY() + 70, 150,40);
        historyButton.addActionListener(e -> frame.router.showHistoryPanel(loggedUser, orders, histories));
        add(historyButton);

        JButton usersButton = new JButton("Персонал");
        usersButton.setBounds(frame.getWidth() / 2 - 75, historyButton.getY() + 70, 150,40);
        usersButton.addActionListener(e -> frame.router.showUsersPanel(loggedUser, orders));
        add(usersButton);

        JButton backButton = new JButton("Назад");
        backButton.setBounds(frame.getWidth() / 2 - 75,usersButton.getY() + 140,150,40);
        backButton.addActionListener(e -> frame.router.showLoginPanel());
        add(backButton);

    }
}
