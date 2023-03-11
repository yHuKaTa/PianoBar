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


public class OrdersPanel extends TablesPanel{
    private final int tableNumber;
    private final User loggedUser;
    private int buttonX;
    private int buttonY;
    private final JButton backCategory = new JButton("Върни назад");
    private final List<JButton> buttons = new ArrayList<>();
    private final List<Product> products;
    private final JScrollPane buttonsPane = new JScrollPane();
    private DefaultTableModel orderTableModel = new DefaultTableModel();
    private int currentlySelectedRow;
    private String nameOfProduct;

    public OrdersPanel(BarFrame frame, User loggedUser, int tableNumber, Map<Integer, List<Product>> orders){
        super(frame,loggedUser, orders);
        this.tableNumber = tableNumber;
        this.loggedUser = loggedUser;
        this.products = help.getProducts();

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

        buttonsPane.setBounds(0,45,frame.getWidth()/2,frame.getHeight()/2+85);
        buttonsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(buttonsPane);

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
        createButtons(getProductTypeButtons());
    }
    private void removeButtons(){
        if (buttons != null) {
            for (JButton button : buttons) {
                buttonsPane.remove(button);
            }
            buttons.clear();
        }
    }
    private JButton returnButtonCategory(int x, int y){
        backCategory.setBounds(x,y,124,40);
        backCategory.addActionListener(e -> {
            removeButtons();
            buttonsPane.remove(backCategory);
            createButtons(getProductTypeButtons());
        });
        return backCategory;
    }
    private List<JButton> getProductTypeButtons(){
        removeButtons();
        for (int i = 0; i < products.size(); i++) {
                if (i == 0){
                    JButton firstButton = new JButton(products.get(i).getType().label);
                    buttons.add(firstButton);
                }
                else if (products.get(i).getType() != products.get(i-1).getType()){
                    JButton button = new JButton(products.get(i).getType().label);
                    buttons.add(button);
                }
        }
        return buttons;
    }
    private void createButtons(List<JButton> buttons){
        buttonX = 0;
        buttonY = 0;
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setBounds(buttonX,buttonY,124,40);
            buttons.get(i).addActionListener(e -> {
                String selectedButton = ((JButton) e.getSource()).getText();
                for (Product product:products){
                    if (selectedButton.equalsIgnoreCase(product.getType().label)){
                        createButtons(getSubCategories(selectedButton));
                        buttonsPane.remove(backCategory);
                    } else if (selectedButton.equalsIgnoreCase(product.getSubtype())) {
                        createButtons(getProductButtons(selectedButton));
                        buttonsPane.remove(backCategory);
                    }
                }
                buttonsPane.add(returnButtonCategory(buttonX,buttonY));
            });
            buttonX+=130;
            buttonsPane.add(buttons.get(i));
            if ((i-2) % 3 == 0){
                buttonY+=45;
                buttonX = 0;
            }
        }
        repaint();
    }
    private List<JButton> getProductButtons(String subtype){
        removeButtons();
        for (Product product : products) {
            if (product.getSubtype().equals(subtype)) {
                JButton button = new JButton(product.getBrandName());
                button.addActionListener(e -> {
                    help.addProductToOrder(tableNumber, ((JButton) e.getSource()).getText());
                    help.reserveTheTable(tableNumber, loggedUser);
                    orderTableModel = help.fetchOrderedProducts(orderTableModel);
                });
                buttons.add(button);
            }
        }
        return buttons;
    }
    private List<JButton> getSubCategories(String type){
        removeButtons();
        for (int i = 0; i < products.size(); i++) {
            if (i == 0 && products.get(i).getType().label.equals(type)){
                JButton firstButton = new JButton(products.get(i).getSubtype());
                buttons.add(firstButton);
            }
            else if (i != 0 && !(products.get(i).getSubtype().equalsIgnoreCase(products.get(i-1).getSubtype())) && products.get(i).getType().label.equals(type)){
                JButton button = new JButton(products.get(i).getSubtype());
                buttons.add(button);
            }
        }
        return buttons;
    }
}
