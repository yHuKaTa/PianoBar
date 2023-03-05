package panels;

import frames.BarFrame;
import models.Product;
import models.User;
import models.UserType;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;

public class DeliveryPanel extends BasePanel {
    private final User loggedUser;
    private DefaultTableModel productsTableModel = new DefaultTableModel();
    private int currentlySelectedRow;
    private String nameOfProduct;

    public DeliveryPanel(BarFrame frame, User loggedUser, Map<Integer, ArrayList<Product>> orders) {
        super(frame);
        this.loggedUser = loggedUser;

        String[] cols = {"Категория", "Подкатегория", "Име", "Цена", "Количество"};
        productsTableModel = help.fetchDeliveryProducts(productsTableModel);
        productsTableModel.setColumnIdentifiers(cols);

        JTable table = new JTable(productsTableModel){
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
                nameOfProduct = String.valueOf(table.getValueAt(currentlySelectedRow, 2));
            }
        });

        JScrollPane tablePane = new JScrollPane(table);
        tablePane.setBounds(0, 0, frame.getWidth(), 450);
        add(tablePane);

        JLabel searchProductLabel = new JLabel("Търсене");
        searchProductLabel.setBounds(15, tablePane.getHeight() + 20, 100, 30);
        searchProductLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(searchProductLabel);

        JTextField searchProductTextField = new JTextField();
        searchProductTextField.setBounds(120, tablePane.getHeight() + 20, frame.getWidth() - 180, 30);
        searchProductTextField.setHorizontalAlignment(SwingConstants.LEFT);
        searchProductTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                help.getSearchedProducts(searchProductTextField.getText());
                productsTableModel = help.fetchDeliveryProducts(productsTableModel);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                help.getSearchedProducts(searchProductTextField.getText());
                productsTableModel = help.fetchDeliveryProducts(productsTableModel);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        add(searchProductTextField);


        JButton backButton = new JButton("Назад");
        backButton.setBounds(30, frame.getHeight() - 80, 120, 30);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addActionListener(e -> help.showBackScreen(frame, loggedUser, orders));
        add(backButton);

        if (loggedUser.getType() == UserType.OWNER) {
            JButton deleteButton = new JButton("Изтрий");
            deleteButton.setBounds(backButton.getX() + backButton.getWidth() + 30, frame.getHeight() - 80, 120, 30);
            deleteButton.setHorizontalAlignment(SwingConstants.CENTER);
            deleteButton.addActionListener(e -> {
                help.deleteProduct(nameOfProduct);
                productsTableModel = help.fetchDeliveryProducts(productsTableModel);
            });
            add(deleteButton);
        }

    }
}
