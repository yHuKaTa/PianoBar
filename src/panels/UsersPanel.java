package panels;

import frames.BarFrame;
import models.Product;
import models.User;
import models.UserType;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;

public class UsersPanel extends BasePanel{
    private DefaultTableModel userTableModel = new DefaultTableModel();
    private int currentlySelectedRow;
    private String pinOfUser;
    public UsersPanel(BarFrame frame, User loggedUser, Map<Integer, ArrayList<Product>> orders) {
        super(frame);
        String[] cols = {"Име", "Тип потребител", "Пин", "Телефон"};
        userTableModel = help.fetchUsers(userTableModel);
        userTableModel.setColumnIdentifiers(cols);

        JTable table = new JTable(userTableModel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                currentlySelectedRow = table.getSelectedRow();
                pinOfUser = String.valueOf(table.getValueAt(currentlySelectedRow,2));
            }
        });

        JScrollPane tablePane = new JScrollPane(table);
        tablePane.setBounds(0,0,frame.getWidth(),300);
        add(tablePane);

        JLabel searchUserLabel = new JLabel("Търсене");
        searchUserLabel.setBounds(15,tablePane.getHeight()+10,100,30);
        searchUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(searchUserLabel);

        JTextField searchUserTextField = new JTextField();
        searchUserTextField.setBounds(120,tablePane.getHeight()+10,frame.getWidth()-180,30);
        searchUserTextField.setHorizontalAlignment(SwingConstants.LEFT);
        searchUserTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                help.getSearchedUsers(searchUserTextField.getText());
                userTableModel = help.fetchUsers(userTableModel);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                help.getSearchedUsers(searchUserTextField.getText());
                userTableModel = help.fetchUsers(userTableModel);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        add(searchUserTextField);

        JLabel userNameLabel = new JLabel("Име");
        userNameLabel.setBounds(40,searchUserTextField.getY()+50,100,30);
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(userNameLabel);

        JTextField userNameTextField = new JTextField();
        userNameTextField.setBounds(20, searchUserTextField.getY() + 100, 150, 30);
        userNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char key = e.getKeyChar();
                if (!((Character.isLetter(key)) || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE || key == KeyEvent.VK_SPACE))
                    e.consume();
            }
        });
        add(userNameTextField);

        JLabel userTypeLabel = new JLabel("Тип потребител");
        userTypeLabel.setBounds(userNameLabel.getX() + 200,searchUserTextField.getY()+50,100,30);
        userTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(userTypeLabel);

        String[] options = new String[2];
        options[0] = UserType.WAITRESS.label;
        options[1] = UserType.MANAGER.label;
        JComboBox<String> userTypeComboBox = new JComboBox<>(options);
        userTypeComboBox.setBounds(userNameTextField.getX() + 200, searchUserTextField.getY() + 100, 150, 30);
        add(userTypeComboBox);

        JLabel userPinLabel = new JLabel("ПИН");
        userPinLabel.setBounds(userTypeLabel.getX() + 200,searchUserTextField.getY()+50,100,30);
        userPinLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(userPinLabel);

        JTextField userPinTextField = new JTextField();
        userPinTextField.setBounds(userTypeComboBox.getX() + 200, searchUserTextField.getY() + 100, 150, 30);
        userPinTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char key = e.getKeyChar();
                if (!((Character.isDigit(key)) || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE))
                    e.consume();
            }
        });
        add(userPinTextField);

        JLabel userPhoneNumberLabel = new JLabel("Телефон");
        userPhoneNumberLabel.setBounds(userPinLabel.getX() + 200,searchUserTextField.getY()+50,100,30);
        userPhoneNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(userPhoneNumberLabel);

        JTextField userPhoneNumberTextField = new JTextField();
        userPhoneNumberTextField.setBounds(userPinTextField.getX() + 200, searchUserTextField.getY() + 100, 150, 30);
        userPhoneNumberTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char key = e.getKeyChar();
                if (!((Character.isDigit(key)) || key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE))
                    e.consume();
            }
        });
        add(userPhoneNumberTextField);

        JButton backButton = new JButton("Назад");
        backButton.setBounds(30,frame.getHeight()-80, 120, 30);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addActionListener(e -> help.showBackScreen(frame, loggedUser, orders));
        add(backButton);

        JButton modifyButton = new JButton("Промени");
        modifyButton.setBounds(backButton.getX() + 200,frame.getHeight()- 80,120,30);
        modifyButton.addActionListener(e -> {
            if (pinOfUser == null) {
                frame.router.showError("Не сте избрали потребител");
            } else {
            help.modifyUser(pinOfUser, loggedUser);
            }
            userTableModel = help.fetchUsers(userTableModel);
        });
        add(modifyButton);

        JButton deleteButton = new JButton("Изтрий");
        deleteButton.setBounds(modifyButton.getX() + 200,frame.getHeight() - 80,120,30);
        deleteButton.addActionListener(e -> {
            help.removeUser(pinOfUser, loggedUser);
            help.fetchUsers(userTableModel);
        });
        add(deleteButton);

        JButton addButton = new JButton("Добави");
        addButton.setBounds(deleteButton.getX() + 200,frame.getHeight() - 80,120,30);
        addButton.addActionListener(e -> {
            help.addUser(userNameTextField.getText(),userTypeComboBox.getSelectedIndex(),userPinTextField.getText(),userPhoneNumberTextField.getText());
            help.fetchUsers(userTableModel);
            repaint();
        });
        add(addButton);
    }

}
