package panels;

import frames.BarFrame;
import models.Product;
import models.User;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
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
        searchUserLabel.setBounds(30,tablePane.getHeight()+20,100,30);
        searchUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(searchUserLabel);

        JTextField searchUserTextField = new JTextField();
        searchUserTextField.setBounds(150,tablePane.getHeight()+20,frame.getWidth()-150,30);
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

        JButton backButton = new JButton("Назад");
        backButton.setBounds(10,frame.getHeight()-80, 120, 30);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.addActionListener(e -> help.showBackScreen(frame, loggedUser, orders));
        add(backButton);

        JButton modifyButton = new JButton("Промени");
        modifyButton.setBounds(backButton.getX() + 230,frame.getHeight()- 80,120,30);
        modifyButton.addActionListener(e -> {
            help.modifyUser(pinOfUser, loggedUser);
            userTableModel = help.fetchUsers(userTableModel);
        });
        add(modifyButton);

        JButton deleteButton = new JButton("Изтрий");
        deleteButton.setBounds(modifyButton.getX() + 230,frame.getHeight()- 80,120,30);
        deleteButton.addActionListener(e -> {
            help.removeUser(pinOfUser, loggedUser);
            help.fetchUsers(userTableModel);
        });
        add(deleteButton);

    }
}
