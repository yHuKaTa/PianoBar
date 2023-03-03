package database;

import java.util.*;

import frames.BarFrame;
import frames.BarRouter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import models.Order;
import models.Product;
import models.User;
import models.UserType;

public class Help {
    public int tableNumber;
    private BarRouter router;
    private final Database database = new Database();
    public final Verification verification = new Verification();
    private ArrayList<Product> products = Objects.requireNonNullElseGet(this.products, ArrayList::new);
    private LinkedHashMap<Integer, ArrayList<Product>> orders = Objects.requireNonNullElseGet(this.orders, LinkedHashMap::new);
    private final LinkedHashMap<Integer, User> workingUsers = Objects.requireNonNullElseGet(this.workingUsers, LinkedHashMap::new);
    private ArrayList<User> users = Objects.requireNonNullElseGet(this.users, ArrayList::new);
    private final ArrayList<User> searchedUsers = new ArrayList<>(users);
    private LinkedHashMap<Integer, ArrayList<Order>> history = Objects.requireNonNullElseGet(this.history, LinkedHashMap::new);

    public Help() {
        this.orders = database.getOrders(orders);
        this.history = database.getHistory(history);
        this.users = database.importUsers(users);
        this.products = database.getProducts(products);
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
    public ArrayList<Product> getOrderedProducts(int tableNumber) {
        return orders.get(tableNumber);
    }
    public void loginSelect(String pin, BarFrame frame) {
        router = frame.router;
        if (verification.loggedUser(pin, users).getType() == UserType.MANAGER) {
            router.showManagersHomePanel(verification.loggedUser(pin, users), orders);
        } else if (verification.loggedUser(pin, users).getType() == UserType.OWNER) {
            router.showOwnersPanel(verification.loggedUser(pin, users), orders);
        } else if (verification.loggedUser(pin, users).getType() == UserType.WAITRESS) {
            router.showTablesPanel(verification.loggedUser(pin, users), orders);
        } else {
            frame.router.showError("Грешна парола. Моля въведете вашата парола отново!");
        }
    }

    public void showBackScreen(BarFrame frame, User loggedUser, Map<Integer, ArrayList<Product>> orders) {
        router = frame.router;
        if (loggedUser.getType() == UserType.MANAGER) {
            router.showManagersHomePanel(loggedUser, orders);
        } else if (loggedUser.getType() == UserType.OWNER) {
            router.showOwnersPanel(loggedUser, orders);
        } else {
            router.showLoginPanel();
        }
    }

    public void isWorkingOnTable(int tableNumber, User loggedUser){
        if (verification.isTableOpened(tableNumber, workingUsers, loggedUser, orders)) {
            router.showOrdersPanel(loggedUser, tableNumber, orders);
        } else {
            router.showError("Нямате достъп! Има отворена сметка на друг потребител.");
        }
    }

    public void reserveTheTable(int tableNumber, User loggedUser) {
        if (loggedUser.getType() == UserType.WAITRESS) {
            if (Objects.isNull(workingUsers.get(tableNumber))) {
                workingUsers.put(tableNumber, loggedUser);
            } else {
                workingUsers.replace(tableNumber, loggedUser);
            }
        }
    }

    public void getSearchedUsers(String searchedUser) {
        searchedUsers.clear();
        for (User user : users) {
            if (user.getUserName().toLowerCase().contains(searchedUser.toLowerCase())) {
                searchedUsers.add(user);
            } else if (user.getPinCode().contains(searchedUser)) {
                searchedUsers.add(user);
            } else if (user.getPhoneNumber().contains(searchedUser)) {
                searchedUsers.add(user);
            }
        }
    }

    public void modifyUser(String pinOfUser, User loggedUser) {
        String name;
        String pin;
        String phoneNumber;
        for (User user : users) {
            if (pinOfUser.equals(user.getPinCode())) {
                String[] options = {"Име", "Тип потребител", "Пин", "Телефон"};
                String[] userType = {UserType.WAITRESS.label, UserType.MANAGER.label};
                switch (JOptionPane.showOptionDialog(null, "Какво ще се променя", "Избери какво ще се променя",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1])) {
                    case 0:
                        name = JOptionPane.showInputDialog(null, "Въведи ново име");
                        if (Objects.isNull(name)) {
                            break;
                        } else if (name.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Не може да се въведе празен ред", "Въведи име", JOptionPane.ERROR_MESSAGE);
                            break;
                        } else {
                            if (verification.isNameDubt(name,users)) {
                                JOptionPane.showMessageDialog(null, "Има дублиране на имена", "Дублиране на имена", JOptionPane.ERROR_MESSAGE);
                            } else {
                                user.setUserName(name);
                            }
                            break;
                        }
                    case 1:
                        if (pinOfUser.equals(loggedUser.getPinCode())){
                            JOptionPane.showMessageDialog(null, "Не може да променяте типа си", "Вие сте собственик", JOptionPane.ERROR_MESSAGE);
                        } else {
                            user.setType(JOptionPane.showOptionDialog(null, "Избери тип потребител", "Тип потребител",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, userType, options[0]));
                        }
                        break;
                    case 2:
                        pin = JOptionPane.showInputDialog(null, "Въведи нов пин код:");
                        if (Objects.isNull(pin)) {
                            break;
                        } else if (pin.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Не може да се въведе празен ред", "Въведи пин", JOptionPane.ERROR_MESSAGE);
                            break;
                        } else {
                            if (verification.isPinDubt(pin,users)) {
                                JOptionPane.showMessageDialog(null, "Има дублиране на пин кодове", "Дублиране на пин", JOptionPane.ERROR_MESSAGE);
                            } else {
                                user.setPinCode(pin);
                            }
                            break;
                        }
                    case 3:
                        phoneNumber = JOptionPane.showInputDialog(null, "Въведи нов телефонен номер:");
                        if (Objects.isNull(phoneNumber)) {
                            break;
                        } else if (phoneNumber.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Не може да се въведе празен ред", "Въведи телефонен номер", JOptionPane.ERROR_MESSAGE);
                            break;
                        } else {
                            if (verification.isPhoneNumberDubt(phoneNumber, users)) {
                                JOptionPane.showMessageDialog(null, "Има дублиране на телефонни номера", "Дублиране на телефонен номер", JOptionPane.ERROR_MESSAGE);
                            } else {
                                user.setPhoneNumber(phoneNumber);
                            }
                            break;
                        }
                }
            }
        }
    }

    public void removeUser(String pinOfUser, User loggedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (pinOfUser.equals(users.get(i).getPinCode())) {
                int result = JOptionPane.showConfirmDialog(null,"Сигурни ли сте в изтриването на служителя?");
                if (result == JOptionPane.YES_OPTION) {
                    if (verification.isDeletingHimSelf(pinOfUser, loggedUser)) {
                        JOptionPane.showMessageDialog(null, "Не може да изтриете себе си", "Непозволено действие", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        users.remove(i);
                        break;
                    }
                } else {
                    return;
                }
            }
        }
    }

    public DefaultTableModel fetchUsers(DefaultTableModel userTableModel) {
        userTableModel.setRowCount(0);
        if (searchedUsers.size() == users.size()) {
            for (User user : users) {
                String[] rows = new String[4];
                rows[0] = user.getUserName();
                rows[1] = user.getType().label;
                rows[2] = user.getPinCode();
                rows[3] = user.getPhoneNumber();
                userTableModel.addRow(rows);
            }
        } else {
            for (User user : searchedUsers) {
                String[] rows = new String[4];
                rows[0] = user.getUserName();
                rows[1] = user.getType().label;
                rows[2] = user.getPinCode();
                rows[3] = user.getPhoneNumber();
                userTableModel.addRow(rows);
            }
        }
        return userTableModel;
    }

    public void addProductToOrder(int table, String order) {
        ArrayList<Product> orderedProducts = orders.get(table);
        Product orderedProduct = null;
        int dub = 0;
        int i;
        int count = 0;
        for (i = 0; i < orderedProducts.size(); i++) {
            if (orderedProducts.get(i).getBrandName().equals(order)) {
                count++;
                dub = i;
            }
        }
        for (Product product : products) {
            if (product.getBrandName().equals(order)) {
                if (count == 0) {
                    orderedProduct = product.clone(product);
                    orderedProduct.setQuantity(product.getServedQuantity());
                    break;
                }
            }
        }
        if (orderedProducts.isEmpty()) {
            orderedProducts.add(orderedProduct);
        } else {
            switch (count) {
                case 0: {
                    orderedProducts.add(orderedProduct);
                    break;
                }
                case 1: {
                    orderedProducts.get(dub).increaseQuantity();
                    orderedProducts.get(dub).setLastOrderedQuantity(orderedProducts.get(dub).getQuantity());
                    break;
                }
                case 2: {
                    orderedProducts.remove(dub);
                    break;
                }
            }
        }
        decreaseProduct(order);
        orders.replace(table, orderedProducts);
        for (Product product : orders.get(table)) {
            if (order.equals(product.getBrandName())) {
                product.setCanDecrease(true);
                break;
            }
        }
    }

    private void decreaseProduct(String orderedProduct) {
        for (Product product : products) {
            if (product.getBrandName().equals(orderedProduct)) {
                product.decreaseQuantity();
                break;
            }
        }
    }

    public void removeProductFromOrder(int tableNumber, String product) {
        int productNumber = 0;
            for (int i = 0; i < orders.get(tableNumber).size(); i++) {
                if (product.equalsIgnoreCase(orders.get(tableNumber).get(i).getBrandName())) {
                    productNumber = i;
                    if (!orders.get(tableNumber).get(i).isCanDecrease() || (orders.get(tableNumber).get(i).getLastOrderedQuantity() != 0 && orders.get(tableNumber).get(i).getLastOrderedQuantity() == orders.get(tableNumber).get(i).getLastConsumedQuantity())) {
                        router.showError("Не може да понижавате количество! Вече е консумирано.");
                        break;
                    } else if (orders.get(tableNumber).get(i).isCanDecrease() && (orders.get(tableNumber).get(i).getQuantity() > orders.get(tableNumber).get(i).getLastConsumedQuantity())) {
                        if (orders.get(tableNumber).get(i).getQuantity() <= orders.get(tableNumber).get(i).getServedQuantity()) {
                          orders.get(tableNumber).remove(i);
                          returnProductFromOrder(product);
                        } else {
                            orders.get(tableNumber).get(i).decreaseQuantity();
                            orders.get(tableNumber).get(i).setLastOrderedQuantity(orders.get(tableNumber).get(i).getQuantity());
                            returnProductFromOrder(product);
                        }
                    break;
                }
            }
        }
    }

    private void returnProductFromOrder(String product) {
        for (Product product1 : products){
            if (product1.getBrandName().equalsIgnoreCase(product)){
                product1.increaseQuantity();
                break;
            }
        }
    }

    public void lockDecreasingOfProducts() {
            for (Map.Entry<Integer, ArrayList<Product>> order : orders.entrySet()) {
                for (Product product : order.getValue()) {
                    product.setLastConsumedQuantity(product.getQuantity());
                    product.setCanDecrease(false);

                }
            }
    }

    public DefaultTableModel fetchOrderedProducts(DefaultTableModel ordersTable) {
        if (ordersTable == null) {
            ordersTable = new DefaultTableModel();
        }
        for (Map.Entry<Integer, ArrayList<Product>> order : orders.entrySet()) {
            if (order.getKey() == tableNumber) {
                ordersTable.setRowCount(0);
                for (Product product : order.getValue()) {
                    String[] row = new String[3];
                    row[0] = product.getBrandName();
                    row[1] = String.format("%,.2f", (product.getQuantity() * 1000)) + " " + product.getMeasure();
                    row[2] = String.format("%,.2f", product.getPrice()) + " лв.";
                    ordersTable.addRow(row);
                }
            }
        }
        return ordersTable;
    }

    public double getBill(int tableNumber, int discount) {
        double bill = 0.00;
        for (Product orderedProduct : orders.get(tableNumber)) {
            bill += orderedProduct.getPrice();
        }
        return bill - ((discount * bill) / 100);
    }

    private Order generateOrder(int tableNumber, double givenAmount, User waiter, ArrayList<Product> orderedProducts) {
        return new Order(tableNumber, givenAmount, waiter, orderedProducts);
    }

    public void moveOrder(int oldTableNumber, int newTableNumber) {
        ArrayList<Product> movingOrder = new ArrayList<>(orders.get(oldTableNumber));
        orders.replace(oldTableNumber, new ArrayList<>());
        orders.replace(newTableNumber, movingOrder);
    }

    public void finishOrder(int tableNumber, double givenAmount, User waiter, ArrayList<Product> orderedProducts) {
        history.get(tableNumber).add(generateOrder(tableNumber, givenAmount, waiter, orderedProducts));
        workingUsers.remove(tableNumber);
        orders.replace(tableNumber, new ArrayList<>());
        router.showLoginPanel();
    }
}
