package panels;

import database.Help;
import models.Product;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ButtonsPanel extends JPanel {
    private final User loggedUser;
    private final int tableNumber;
    private final Help help;
    private List<JButton> buttons;
    private final List<Product> products;
    private final JButton backCategory = new JButton("Върни назад");
    private int buttonX;
    private int buttonY;
    private DefaultTableModel orderTableModel;
    public ButtonsPanel(User loggedUser, int tableNumber, List<Product> products, Help help, DefaultTableModel defaultTableModel) {
        super();
        this.loggedUser = loggedUser;
        this.tableNumber = tableNumber;
        this.products = products;
        this.help = help;
        this.orderTableModel = defaultTableModel;
        this.setLayout(null);
        this.createButtons(getProductTypeButtons());
    }

    private void removeButtons(){
        if (buttons != null) {
            for (JButton button : buttons) {
                this.remove(button);
            }
            buttons.clear();
        }
    }
    private JButton returnButtonCategory(int x, int y){
        backCategory.setBounds(x,y,124,40);
        backCategory.addActionListener(e -> {
            removeButtons();
            this.remove(backCategory);
            createButtons(getProductTypeButtons());
        });
        return backCategory;
    }
    private List<JButton> getProductTypeButtons(){
        removeButtons();
        if (Objects.isNull(buttons)) {
            buttons = new ArrayList<>();
        }
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
                        this.remove(backCategory);
                    } else if (selectedButton.equalsIgnoreCase(product.getSubtype())) {
                        createButtons(getProductButtons(selectedButton));
                        this.remove(backCategory);
                    }
                }
                this.add(returnButtonCategory(buttonX,buttonY));
            });
            buttonX+=130;
            this.add(buttons.get(i));
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
