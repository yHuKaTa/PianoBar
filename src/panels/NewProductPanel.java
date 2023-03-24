package panels;

import frames.BarFrame;
import models.Product;
import models.ProductType;
import models.User;
import models.UserType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

public class NewProductPanel extends BasePanel{
    public NewProductPanel(BarFrame frame, User loggedUser, Map<Integer, List<Product>> orders) {
        super(frame);

        JLabel productTypeLabel = new JLabel("Категория продукт:");
        productTypeLabel.setBounds(90, 100, 150,30);
        productTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(productTypeLabel);

        JLabel productSubTypeLabel = new JLabel("Подкатегория продукт:");
        productSubTypeLabel.setBounds(productTypeLabel.getX() + 180, productTypeLabel.getY(), 150,30);
        productSubTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(productSubTypeLabel);

        JLabel brandNameLabel = new JLabel("Име на продукт");
        brandNameLabel.setBounds(productSubTypeLabel.getX() + 220, productSubTypeLabel.getY(), 150, 30);
        brandNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(brandNameLabel);

        String[] opt = {ProductType.ALCOHOLIC.label, ProductType.NONALCOHOLIC.label, ProductType.FOOD.label, ProductType.COCKTAIL.label};

        JComboBox<String> productTypeComboBox = new JComboBox<>(opt);
        productTypeComboBox.setBounds(productTypeLabel.getX(), productTypeLabel.getY()+40, 150,30);
        productTypeComboBox.setSelectedIndex(0);
        add(productTypeComboBox);

        JTextField subTypeTextField = new JTextField();
        subTypeTextField.setBounds(productTypeComboBox.getX() + 180,productSubTypeLabel.getY()+40, 150, 30);
        subTypeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char key = e.getKeyChar();
                if (!((Character.isAlphabetic(key) || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE || key == '.' || key == KeyEvent.VK_SPACE))) {
                    e.consume();
                }
            }
        });
        add(subTypeTextField);

        JTextField brandNameTextField = new JTextField();
        brandNameTextField.setBounds(subTypeTextField.getX() + 180, brandNameLabel.getY() + 40, 250, 30);
        brandNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char key = e.getKeyChar();
                if (!((Character.isAlphabetic(key) || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE || key == '.' || key == KeyEvent.VK_SPACE))) {
                    e.consume();
                }
            }
        });
        add(brandNameTextField);

        JLabel servedQuantityLabel = new JLabel("Сервирано количество:");
        servedQuantityLabel.setBounds(90, productTypeComboBox.getY() + 100, 150, 30);
        servedQuantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(servedQuantityLabel);

        JLabel priceLabel = new JLabel("Единична цена:");
        priceLabel.setBounds(servedQuantityLabel.getX() + 180, productTypeComboBox.getY() + 100, 150, 30);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(priceLabel);

        JLabel isLiquidLabel = new JLabel("Течност ли е?");
        isLiquidLabel.setBounds(priceLabel.getX() + 220, priceLabel.getY(), 150, 30);
        isLiquidLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(isLiquidLabel);

        JTextField servedQuantityTextField = new JTextField();
        servedQuantityTextField.setBounds(servedQuantityLabel.getX(), servedQuantityLabel.getY() + 40, 150, 30);
        servedQuantityTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char key = e.getKeyChar();
                if (!((Character.isDigit(key) || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE || key == '.'))) {
                    e.consume();
                }
            }
        });
        add(servedQuantityTextField);

        JTextField priceTextField = new JTextField();
        priceTextField.setBounds(priceLabel.getX(), priceLabel.getY() + 40, 150, 30);
        priceTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char key = e.getKeyChar();
                if (!((Character.isDigit(key) || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE || key == '.'))) {
                    e.consume();
                }
            }
        });
        add(priceTextField);

        JRadioButton isLiquidRadioButton = new JRadioButton("Да");
        isLiquidRadioButton.setBounds(isLiquidLabel.getX(), isLiquidLabel.getY() + 40, 75, 30);
        isLiquidRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
        isLiquidRadioButton.setFont(new Font("Arial", Font.PLAIN, 12));
        isLiquidRadioButton.setSelected(true);
        add(isLiquidRadioButton);

        JRadioButton isNotLiquidRadioButton = new JRadioButton("Не");
        isNotLiquidRadioButton.setBounds(isLiquidLabel.getX() + 80, isLiquidLabel.getY() + 40, 75, 30);
        isNotLiquidRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
        isNotLiquidRadioButton.setFont(new Font("Arial", Font.PLAIN, 12));
        add(isNotLiquidRadioButton);

        ButtonGroup isLiquidGroup = new ButtonGroup();
        isLiquidGroup.add(isLiquidRadioButton);
        isLiquidGroup.add(isNotLiquidRadioButton);

        JButton backButton = new JButton("Назад");
        backButton.setBounds(100, frame.getHeight() - 180, 120, 30);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addActionListener(e -> frame.router.showDeliveryPanel(loggedUser,orders));
        add(backButton);

        JButton clearButton = new JButton("Изчисти");
        clearButton.setBounds(backButton.getX() + 200, frame.getHeight() - 180, 120, 30);
        clearButton.setHorizontalAlignment(SwingConstants.CENTER);
        clearButton.addActionListener(e -> {
            productTypeComboBox.setSelectedIndex(0);
            subTypeTextField.setText("");
            brandNameTextField.setText("");
            servedQuantityTextField.setText("");
            priceTextField.setText("");
            isLiquidRadioButton.setSelected(true);

        });
        add(clearButton);

        JButton addProductButton = new JButton("Добави продукт");
        addProductButton.setBounds(clearButton.getX() + 195, clearButton.getY(), 150, 30);
        addProductButton.setHorizontalAlignment(SwingConstants.CENTER);
        addProductButton.addActionListener(e -> {
            if (help.isNewProductOk(productTypeComboBox.getSelectedIndex(),
                    subTypeTextField.getText(),
                    brandNameTextField.getText(),
                    servedQuantityTextField.getText(),
                    priceTextField.getText())) {
                help.addNewProduct(productTypeComboBox.getSelectedIndex(),
                        subTypeTextField.getText(),
                        brandNameTextField.getText(),
                        Double.parseDouble(servedQuantityTextField.getText()),
                        Double.parseDouble(priceTextField.getText()),
                        isLiquidRadioButton.isSelected());
                frame.router.showDeliveryPanel(loggedUser,orders);
            } else {
                frame.router.showDeliveryPanel(loggedUser,orders);
            }
        });
        add(addProductButton);
    }

}
