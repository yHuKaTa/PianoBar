package panels;

import frames.BarFrame;
import models.Product;
import models.User;
import models.UserType;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Font;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PayPanel extends BasePanel {
    private final List<Product> orderedProducts;
    private final JRadioButton cash;
    private final JRadioButton creditCard;
    private final JRadioButton bankTransfer;
    private final JTextField billTextField;
    private final JLabel requestedCashLabel = new JLabel("Дадена сума");
    private JTextField requestedCashTextField;
    private final JLabel refundCashLabel = new JLabel("Ресто");
    private JTextField refundCashTextField;
    private double bill;
    private int discount;
    private JTextField discountTextField = new JTextField("0");

    public PayPanel(BarFrame frame, int tableNumber, User loggedUser, Map<Integer, List<Product>> orders) {
        super(frame);
        this.orderedProducts = help.getOrderedProducts(tableNumber);
        this.bill = help.getBill(tableNumber,discount);

        JLabel title = new JLabel("Изберете начин на плащане:", null, SwingConstants.CENTER);
        title.setBounds(frame.getWidth() / 2 - 200, frame.getHeight() / 2 - 200, 400, 40);
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        add(title);

        cash = new JRadioButton("В брой");
        cash.setFont(new Font("Arial", Font.PLAIN, 12));
        cash.setSelected(true);
        cash.setBounds(frame.getWidth() / 2 - 250, title.getY() + 50, 100, 20);
        cash.addActionListener(e -> {
            if (cash.isSelected()) {
                requestedCashLabel.setVisible(true);
                requestedCashTextField.setVisible(true);
                refundCashLabel.setVisible(true);
                refundCashTextField.setVisible(true);
            }
        });
        add(cash);

        creditCard = new JRadioButton("С кредитна/дебитна карта");
        creditCard.setBounds(frame.getWidth() / 2 - 100, title.getY() + 50, 200, 20);
        creditCard.setFont(new Font("Arial", Font.PLAIN, 12));
        creditCard.addActionListener(e -> {
            if (creditCard.isSelected()){
                requestedCashLabel.setVisible(false);
                requestedCashTextField.setVisible(false);
                refundCashLabel.setVisible(false);
                refundCashTextField.setVisible(false);
            }
        });
        add(creditCard);

        bankTransfer = new JRadioButton("С банков превод");
        bankTransfer.setBounds(frame.getWidth() / 2 + 150, title.getY() + 50, 150, 20);
        bankTransfer.setFont(new Font("Arial", Font.PLAIN, 12));
        bankTransfer.addActionListener(e -> {
            if (bankTransfer.isSelected()) {
                requestedCashLabel.setVisible(false);
                requestedCashTextField.setVisible(false);
                refundCashLabel.setVisible(false);
                refundCashTextField.setVisible(false);
            }
        });
        add(bankTransfer);

        ButtonGroup methodOfPay = new ButtonGroup();
        methodOfPay.add(cash);
        methodOfPay.add(creditCard);
        methodOfPay.add(bankTransfer);

        JLabel amountLabel = new JLabel("Сума за плащане");
        amountLabel.setBounds(frame.getWidth() / 2 - 250, cash.getY() + 50, 100, 20);
        amountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(amountLabel);

        billTextField = new JTextField();
        billTextField.setBounds(frame.getWidth() / 2 - 225, amountLabel.getY() + 50, 50, 20);
        billTextField.setText(String.format("%,.2f", bill));
        billTextField.setHorizontalAlignment(SwingConstants.CENTER);
        billTextField.setEditable(false);
        add(billTextField);

        JLabel discountLabel = new JLabel("Отстъпка [%]");
        discountLabel.setBounds(frame.getWidth() / 2 - 100,creditCard.getY() + 50,100,20);
        discountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(discountLabel);

        discountTextField.setBounds(frame.getWidth() / 2 - 75,discountLabel.getY() + 50,50,20);
        discountTextField.setHorizontalAlignment(SwingConstants.CENTER);
        discountTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (discountTextField.getText().equals("0")){
                    discountTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (discountTextField.getText().equals("")){
                    discountTextField.setText("0");
                }
            }
        });

        discountTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char key = e.getKeyChar();
                if (!((Character.isDigit(key)) || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE))
                    e.consume();
            }
        });

        discountTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!(discountTextField.getText().isEmpty()) && Integer.parseInt(discountTextField.getText()) <= 20) {
                    discount = Integer.parseInt(discountTextField.getText());

                } else {
                    discount = 0;
                }
                if (!(discountTextField.getText().isEmpty()) && Integer.parseInt(discountTextField.getText()) > 20){
                    JOptionPane.showMessageDialog(null,"Не може да правите по-висока отстъпка от 20%!","Твърде висока отстъпка", JOptionPane.ERROR_MESSAGE);
                }
                bill = help.getBill(tableNumber, discount);
                billTextField.setText(String.format("%,.2f", bill));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!(discountTextField.getText().isEmpty()) && Integer.parseInt(discountTextField.getText()) <= 20) {
                    discount = Integer.parseInt(discountTextField.getText());
                } else {
                    discount = 0;
                }
                if (!(discountTextField.getText().isEmpty()) && Integer.parseInt(discountTextField.getText()) > 20){
                    JOptionPane.showMessageDialog(null,"Не може да правите по-висока отстъпка от 20%!","Твърде висока отстъпка", JOptionPane.ERROR_MESSAGE);
                }
                bill = help.getBill(tableNumber, discount);
                billTextField.setText(String.format("%,.2f", bill));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        if (loggedUser.getType() == UserType.WAITRESS) {
            discountTextField.setVisible(false);
            discountLabel.setVisible(false);
        }
        add(discountTextField);

        requestedCashLabel.setBounds(frame.getWidth() / 2, creditCard.getY() + 50, 100, 20);
        requestedCashLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(requestedCashLabel);

        requestedCashTextField = new JTextField();
        requestedCashTextField.setBounds(frame.getWidth() / 2 + 25, requestedCashLabel.getY() + 50, 50, 20);
        requestedCashTextField.setHorizontalAlignment(SwingConstants.CENTER);
        requestedCashTextField.setText("0.00");
        requestedCashTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (requestedCashTextField.getText().equals("0.00")) {
                    requestedCashTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (requestedCashTextField.getText().equals("")) {
                    requestedCashTextField.setText("0.00");
                }
            }
        });
        requestedCashTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char key = e.getKeyChar();
                if (!((Character.isDigit(key)) || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE || key == '.'))
                    e.consume();
            }
        });
        requestedCashTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                    if (billTextField.getText().equals("0.00")) {
                        refundCashTextField.setText("0.00");
                    } else {
                        if (requestedCashTextField.getText().isEmpty()) {
                            refundCashTextField.setText("0.00");
                        } else if ((Double.parseDouble(requestedCashTextField.getText()) - bill) < 0) {
                            refundCashTextField.setText("0.00");
                        } else {
                            refundCashTextField.setText(String.format("%,.2f", Double.parseDouble(requestedCashTextField.getText()) - bill));
                    }
                }
            }


            @Override
            public void removeUpdate(DocumentEvent e) {
                    if (billTextField.getText().equals("0.00")) {
                        refundCashTextField.setText("0.00");
                    } else {
                        if (requestedCashTextField.getText().isEmpty()) {
                            refundCashTextField.setText("0.00");
                        } else if ((Double.parseDouble(requestedCashTextField.getText()) - bill) < 0) {
                            refundCashTextField.setText("0.00");
                        } else {
                            refundCashTextField.setText(String.format("%,.2f", Double.parseDouble(requestedCashTextField.getText()) - bill));
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        add(requestedCashTextField);

        refundCashLabel.setBounds(frame.getWidth() / 2 + 150, bankTransfer.getY() + 50, 100, 20);
        refundCashLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(refundCashLabel);

        refundCashTextField = new JTextField();
        refundCashTextField.setBounds(frame.getWidth() / 2 + 175, refundCashLabel.getY() + 50, 50, 20);
        refundCashTextField.setText("0.00");
        refundCashTextField.setHorizontalAlignment(SwingConstants.CENTER);
        refundCashTextField.setEditable(false);
        add(refundCashTextField);

        JButton backButton = new JButton("Назад");
        backButton.setBounds(frame.getWidth() / 2 - 175, billTextField.getY() + 100, 120, 30);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addActionListener(e -> frame.router.showOrdersPanel(loggedUser,tableNumber,orders));
        add(backButton);

        JButton payButton = new JButton("Приключи");
        payButton.setBounds(frame.getWidth() / 2 + 50, billTextField.getY() + 100, 120, 30);
        payButton.setHorizontalAlignment(SwingConstants.CENTER);
        payButton.addActionListener(e -> {
            if (Objects.isNull(orderedProducts) || orderedProducts.isEmpty() || bill <= 0) {
                JOptionPane.showMessageDialog(null,"Не може да приключите сметка без продукти","Невалидна сметка", JOptionPane.ERROR_MESSAGE);
                frame.router.showOrdersPanel(loggedUser,tableNumber,orders);
            } else if (showQuestionPopup("Желаете ли да приключите сметката?")) {
                if (cash.isSelected()) {
                    if (Double.parseDouble(requestedCashTextField.getText()) - bill < 0) {
                        frame.router.showError("Не достатъчно дадена сума!");
                    } else {
                        help.finishOrder( Double.parseDouble(requestedCashTextField.getText()), loggedUser, orderedProducts, methodOfPay());
                    }
                } else {
                    help.finishOrder( Double.parseDouble(billTextField.getText()), loggedUser, orderedProducts, methodOfPay());
                }
            }
        });
        add(payButton);
    }
    private String methodOfPay() {
        if (cash.isSelected()) {
            return cash.getText();
        } else if (creditCard.isSelected()) {
            return creditCard.getText();
        } else {
            return bankTransfer.getText();
        }
    }
}
