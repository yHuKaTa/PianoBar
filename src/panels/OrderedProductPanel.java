package panels;

import frames.BarFrame;
import models.Order;
import models.Product;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class OrderedProductPanel extends HistoryPanel{
    private int selectedOrder;
    private String workingUser;
    private DefaultTableModel ordersTableModel = new DefaultTableModel();
    private DefaultTableModel productsTableModel = new DefaultTableModel();
    public OrderedProductPanel(BarFrame frame, User loggedUser, Map<Integer, List<Product>> orders, Map<Integer, List<Order>> histories, int tableNumber) {
        super(frame, loggedUser, orders, histories);

        JLabel waiterLabel = new JLabel(" Маса: " + tableNumber);
        waiterLabel.setBounds((frame.getWidth()/2)-115,5, 220, 30);
        waiterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(waiterLabel);

        String[] cols = {"№ на поръчка", "Дата и час", "Сервитьор", "Метод на плащане", "Сума", "Взети пари"};
        ordersTableModel.setColumnIdentifiers(cols);
        ordersTableModel = help.fetchOrdersHistory(ordersTableModel, tableNumber);
        JTable ordersTable = new JTable(ordersTableModel){
            @Override
            public boolean isCellEditable(int i, int b) {
                return false;
            }
        };

        ordersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int currentlySelectedRow = ordersTable.getSelectedRow();
                workingUser = String.valueOf(ordersTable.getValueAt(currentlySelectedRow,2));
                waiterLabel.setText(("Сервитьор: " + workingUser + " Маса: " + tableNumber));
                if (ordersTable.getValueAt(currentlySelectedRow,0) == null) {
                    ordersTableModel = help.fetchOrdersHistory(ordersTableModel,tableNumber);
                    frame.router.showError("Не сте избрали поръчка!");
                }
                selectedOrder = Integer.parseInt((String) ordersTable.getValueAt(currentlySelectedRow,0)) - 1;
                if (selectedOrder < 0) {
                    frame.router.showError("Не сте избрали поръчка!");
                } else {
                    JScrollPane productsTablePanel = productsTablePane(selectedOrder, tableNumber);
                    remove(productsTablePanel);
                    add(productsTablePanel);
                }
            }
        });

        JScrollPane ordersTablePane = new JScrollPane(ordersTable);
        ordersTablePane.setBounds(0,45,frame.getWidth(),frame.getHeight() - 430);
        add(ordersTablePane);
    }

    private JScrollPane productsTablePane(int selectedOrder, int tableNumber) {
        String[] cols = {"Продукт", "Количество", "Цена"};
        productsTableModel.setColumnIdentifiers(cols);
        productsTableModel = help.fetchProductsOfOrdersHistory(productsTableModel, selectedOrder, tableNumber);

        JTable productsTable = new JTable(productsTableModel){
            @Override
            public boolean isCellEditable(int i, int b) {
                return false;
            }
        };

        JScrollPane productsTablePane = new JScrollPane(productsTable);
        productsTablePane.setBounds(0,220,frame.getWidth(),frame.getHeight() - 430);
        return productsTablePane;
    }
}
