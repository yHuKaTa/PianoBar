package panels;

import frames.BarFrame;
import models.Product;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Map;


public class OwnerHomePanel extends BasePanel{
    private DefaultTableModel userTableModel;

    public OwnerHomePanel(BarFrame frame, User loggedUser, Map<Integer, ArrayList<Product>> orders) {
        super(frame);

        JButton tablesButton = new JButton("Поръчки");
        tablesButton.setBounds(frame.getWidth() / 2 - 75,frame.getHeight() / 4 - 20,150,40);
        tablesButton.addActionListener(e -> frame.router.showTablesPanel(loggedUser, orders));
        add(tablesButton);

        JButton deliveryButton = new JButton("Доставки");
        deliveryButton.setBounds(frame.getWidth() / 2 - 75, tablesButton.getY() + 70, 150,40);
//        Напиши екран за доставки
//        deliveryButton.addActionListener();
        add(deliveryButton);

        JButton usersButton = new JButton("Персонал");
        usersButton.setBounds(frame.getWidth() / 2 - 75, tablesButton.getY() + 140, 150,40);
        usersButton.addActionListener(e -> frame.router.showUsersPanel(loggedUser, orders));
        add(usersButton);

        JButton backButton = new JButton("Назад");
        backButton.setBounds(frame.getWidth() / 2 - 75,deliveryButton.getY() + 210,150,40);
        backButton.addActionListener(e -> frame.router.showLoginPanel());
        add(backButton);

    }
}
