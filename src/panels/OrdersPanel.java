package panels;

import frames.BarFrame;
import models.Product;
import models.User;
import models.UserType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class OrdersPanel extends TablesPanel{
    private final int tableNumber;
    private DefaultTableModel orderTableModel = new DefaultTableModel();
    private int currentlySelectedRow;
    private String nameOfProduct;

    public OrdersPanel(BarFrame frame, User loggedUser, int tableNumber, Map<Integer, List<Product>> orders){
        super(frame,loggedUser, orders);
        this.tableNumber = tableNumber;
        List<Product> products = help.getProducts();

        JLabel waiterLabel = new JLabel("Сервитьор: " + loggedUser.getUserName() + " Маса: " + tableNumber);
        waiterLabel.setBounds((frame.getWidth()/2)-115,5, 220, 30);
        waiterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(waiterLabel);

        if (loggedUser.getType() == UserType.OWNER || loggedUser.getType() == UserType.MANAGER){
        JButton moveOrder = new JButton("Премести поръчка");
        moveOrder.setBounds(frame.getWidth() - 300, 5, 150, 30);
        moveOrder.setHorizontalAlignment(SwingConstants.CENTER);
        moveOrder.addActionListener(e -> {
            if (showQuestionPopup("Желаете ли да преместите поръчката на друга маса?")) {
                help.moveOrder(this.tableNumber,help.verification.isValidNewTableNumber());
                orderTableModel = help.fetchOrderedProducts(orderTableModel);
                repaint();
            }
        }
        );
        add(moveOrder);
        }

        JButton closeOrder = new JButton("Приключи");
        closeOrder.setBounds(frame.getWidth() - 145,5, 120, 30);
        closeOrder.setHorizontalAlignment(SwingConstants.CENTER);
        closeOrder.addActionListener(e -> {frame.router.showPayPanel(tableNumber,loggedUser, orders);

        });
        add(closeOrder);

        String[] cols = {"Продукт", "Количество", "Цена"};
        orderTableModel.setColumnIdentifiers(cols);
        orderTableModel = help.fetchOrderedProducts(orderTableModel);

        JTable table = new JTable(orderTableModel){
            @Override
            public boolean isCellEditable(int i, int b) {
                return false;
            }

        };

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                currentlySelectedRow = table.getSelectedRow();
                nameOfProduct = String.valueOf(table.getValueAt(currentlySelectedRow,0));
            }
        });

        JScrollPane tablePane = new JScrollPane(table);
        tablePane.setBounds(frame.getWidth()/2,45,frame.getWidth()/2,frame.getHeight() - 215);
        add(tablePane);

        ButtonsPanel buttons = new ButtonsPanel(loggedUser,tableNumber, products,help,orderTableModel);
        JScrollPane buttonsPanel = new JScrollPane(buttons);
        buttonsPanel.setBounds(0,45,frame.getWidth()/2,frame.getHeight()/2+85);
        buttonsPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(buttonsPanel);

        JButton removeFromOrder = new JButton("Намали количество");
        removeFromOrder.setBounds(140,5, 150, 30);
        removeFromOrder.setHorizontalAlignment(SwingConstants.CENTER);
        removeFromOrder.addActionListener(e -> {
            if (nameOfProduct == null) {
                frame.router.showError("Не сте избрали продукт!");
            } else {
                help.removeProductFromOrder(tableNumber, nameOfProduct);
                orderTableModel = help.fetchOrderedProducts(orderTableModel);
            }
            nameOfProduct = null;
            }
        );
        add(removeFromOrder);
    }
}
