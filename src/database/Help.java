package database;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import frames.BarFrame;
import frames.BarRouter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import models.*;

public class Help {
    public int tableNumber;
    private BarRouter router;
    public Verification verification = new Verification();
    private List<Product> products = new ArrayList<>();
    private final List<Product> searchedProducts;
    private Map<Integer, List<Product>> orders = new LinkedHashMap<>();
    private final Map<Integer, User> workingUsers = new HashMap<>();
    private List<User> users = new ArrayList<>();
    private final List<User> searchedUsers;
    private Map<Integer, List<Order>> histories = new LinkedHashMap<>();

    public Help() {
        Database database = new Database();
        this.orders = database.getOrders(orders);
        this.histories = database.getHistory(histories);
        this.users = database.importUsers(users);
        this.products = database.getProducts(products);
        this.searchedUsers = new ArrayList<>(users);
        this.searchedProducts = new ArrayList<>(products);
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getOrderedProducts(int tableNumber) {
        return orders.get(tableNumber);
    }

    public void loginSelect(String pin, BarFrame frame) {
        router = frame.router;
        switch (verification.loggedUser(pin, users).getType()) {
            case WAITRESS : {
                router.showTablesPanel(verification.loggedUser(pin, users), orders);
                break;
            }
            case MANAGER : {
                router.showManagersHomePanel(verification.loggedUser(pin, users), orders, histories);
                break;
            }
            case OWNER : {
                router.showOwnersPanel(verification.loggedUser(pin, users), orders, histories);
                break;
            }
            default : {
                frame.router.showError("Грешна парола. Моля въведете вашата парола отново!");
            }
        }
    }

    public void showBackScreen(BarFrame frame, User loggedUser, Map<Integer, List<Product>> orders) {
        router = frame.router;
        if (loggedUser.getType() == UserType.MANAGER) {
            router.showManagersHomePanel(loggedUser, orders, histories);
        } else if (loggedUser.getType() == UserType.OWNER) {
            router.showOwnersPanel(loggedUser, orders, histories);
        } else {
            router.showLoginPanel();
        }
    }

    public void isWorkingOnTable(int tableNumber, User loggedUser) {
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

    public void showHistory(User loggedUser, int tableNumber) {
        router.showOrderedProductPanel(loggedUser, orders, histories, tableNumber);
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

    public void addUser(String nameOfUser, int userType, String pinOfUser, String phoneNumber) {
        int count = 0;
        if (nameOfUser.isEmpty() || nameOfUser.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Не може да създадете потребител без име!", "Въведете име", JOptionPane.ERROR_MESSAGE);
            count++;
        } else if (nameOfUser.trim().length() < 3) {
            JOptionPane.showMessageDialog(null, "Минималното количество символи е 3! Въведи ново име", "Твърде кратъко име", JOptionPane.ERROR_MESSAGE);
            count++;
        } else if (verification.isNameDubt(nameOfUser.trim(), users)) {
            JOptionPane.showMessageDialog(null, "Съществува дублиране на имена!", "Дублиране на имена", JOptionPane.ERROR_MESSAGE);
            count++;
        } else if (pinOfUser.isEmpty() || pinOfUser.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Не може да създадете потребител без пин код!", "Въведете пин код", JOptionPane.ERROR_MESSAGE);
            count++;
        } else if (pinOfUser.trim().length() < 4) {
            JOptionPane.showMessageDialog(null, "Минималното количество символи е 4! Въведи нов пин код", "Твърде кратък пин код", JOptionPane.ERROR_MESSAGE);
            count++;
        } else if (verification.isPinDubt(pinOfUser.trim(), users)) {
            JOptionPane.showMessageDialog(null, "Съществува дублиране на пин кодове!", "Дублиране на пин код", JOptionPane.ERROR_MESSAGE);
            count++;
        } else if (phoneNumber.isEmpty() || phoneNumber.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Не може да създадете потребител без телефонен номер!", "Въведете телефонен номер", JOptionPane.ERROR_MESSAGE);
            count++;
        } else if (phoneNumber.trim().length() < 4) {
            JOptionPane.showMessageDialog(null, "Минималното количество символи е 4! Въведи нов телефонен номер", "Твърде кратък телефонен номер", JOptionPane.ERROR_MESSAGE);
            count++;
        } else if (verification.isPhoneNumberDubt(phoneNumber, users)) {
            JOptionPane.showMessageDialog(null, "Съществува дублиране на телефонни номера!", "Дублиране на телефони", JOptionPane.ERROR_MESSAGE);
            count++;
        }
        if (count == 0) {
            if (userType == 1) {
                users.add(new User(nameOfUser, pinOfUser, phoneNumber, UserType.MANAGER));
                searchedUsers.add(new User(nameOfUser, pinOfUser, phoneNumber, UserType.MANAGER));
            } else {
                users.add(new User(nameOfUser, pinOfUser, phoneNumber, UserType.WAITRESS));
                searchedUsers.add(new User(nameOfUser, pinOfUser, phoneNumber, UserType.WAITRESS));
            }
            count--;
        }
        if (count == -1) {
            JOptionPane.showMessageDialog(null, "Успешно създаден нов потребител!", "Добавен потребител", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void modifyUser(String pinOfUser, User loggedUser) {
        String name;
        String pin;
        String phoneNumber;
        int count = 0;
        if (pinOfUser == null || pinOfUser.isEmpty()){
            JOptionPane.showMessageDialog(null, "Няма избран потребител!", "Невалиден избор", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (User user : users) {
            if (pinOfUser.equals(user.getPinCode())) {
                count++;
                String[] options = {"Име", "Тип потребител", "Пин", "Телефон"};
                String[] userType = {UserType.WAITRESS.label, UserType.MANAGER.label};
                switch (JOptionPane.showOptionDialog(null, "Какво ще се променя", "Избери какво ще се променя",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1])) {
                    case 0:
                        name = JOptionPane.showInputDialog(null, "Въведи ново име");
                        if (name == null) {
                            return;
                        }
                        name = name.replaceAll("[^a-zA-Zа-яА-Я ]", "").trim();
                        if (name.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Не може да се въведе празен ред", "Въведи име", JOptionPane.ERROR_MESSAGE);
                        } else if (name.length() < 3) {
                            JOptionPane.showMessageDialog(null, "Минималното количество символи е 3! Въведи ново име", "Твърде кратъко име", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (verification.isNameDubt(name, users)) {
                                JOptionPane.showMessageDialog(null, "Има дублиране на имена", "Дублиране на имена", JOptionPane.ERROR_MESSAGE);
                            } else {
                                user.setUserName(name);
                            }
                        }
                        break;
                    case 1:
                        if (pinOfUser.equals(loggedUser.getPinCode()) || user.getType() == UserType.OWNER) {
                            JOptionPane.showMessageDialog(null, "Не може да променяте типа си", "Вие сте собственик", JOptionPane.ERROR_MESSAGE);
                        } else {
                            UserType type = verification.getUserType(JOptionPane.showOptionDialog(null, "Избери тип потребител", "Тип потребител",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, userType, options[0]));
                            if (type != null) {
                                user.setType(type);
                            }
                        }
                        break;
                    case 2:
                        pin = JOptionPane.showInputDialog(null, "Въведи нов пин код:");
                        if (pin == null) {
                            return;
                        }
                        pin = pin.replaceAll("[^0-9]", "").trim();
                        if (pin.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Не може да се въведе празен ред", "Въведи пин", JOptionPane.ERROR_MESSAGE);
                        } else if (pin.length() < 4) {
                            JOptionPane.showMessageDialog(null, "Минималното количество символи е 4! Въведи нов пин код", "Твърде кратък пин код", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (verification.isPinDubt(pin, users)) {
                                JOptionPane.showMessageDialog(null, "Има дублиране на пин кодове", "Дублиране на пин", JOptionPane.ERROR_MESSAGE);
                            } else {
                                user.setPinCode(pin);
                            }
                        }
                        break;
                    case 3:
                        phoneNumber = JOptionPane.showInputDialog(null, "Въведи нов телефонен номер:");
                        if (phoneNumber == null) {
                            return;
                        }
                        phoneNumber = phoneNumber.replaceAll("[^0-9 /-]", "").trim();
                        if (phoneNumber.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Не може да се въведе празен ред", "Въведи телефонен номер", JOptionPane.ERROR_MESSAGE);
                        } else if (phoneNumber.length() < 4) {
                            JOptionPane.showMessageDialog(null, "Минималното количество символи е 4! Въведи нов телефонен номер", "Твърде кратък телефонен номер", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (verification.isPhoneNumberDubt(phoneNumber, users)) {
                                JOptionPane.showMessageDialog(null, "Има дублиране на телефонни номера", "Дублиране на телефонен номер", JOptionPane.ERROR_MESSAGE);
                            } else {
                                user.setPhoneNumber(phoneNumber);
                            }
                        }
                        break;
                }
            }
        }
        if (count == 0) {
            JOptionPane.showMessageDialog(null, "Няма избран потребител!", "Невалиден избор", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeUser(String pinOfUser, User loggedUser) {
        if (pinOfUser == null || pinOfUser.isEmpty() || users.stream().noneMatch(user -> (user.getPinCode().equalsIgnoreCase(pinOfUser)))) {
            JOptionPane.showMessageDialog(null, "Не сте избрали потребител", "Невалиден избор", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int i = 0;
        for (User user : users) {
            i++;
            if (pinOfUser.equals(user.getPinCode())) {
                int result = JOptionPane.showConfirmDialog(null, "Сигурни ли сте в изтриването на служителя?");
                if (result == JOptionPane.YES_OPTION) {
                    if (verification.isDeletingHimSelf(pinOfUser, loggedUser)) {
                        JOptionPane.showMessageDialog(null, "Не може да изтриете себе си", "Непозволено действие", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        users.remove(i-1);
                        searchedUsers.remove(i-1);
                        JOptionPane.showMessageDialog(null, "Успешно изтрит потребител!", "Изтрит е!", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
                else {
                    return;
                }
            }
        }
    }

    public DefaultTableModel fetchUsers(DefaultTableModel userTableModel) {
        userTableModel.setRowCount(0);
        if (searchedUsers.size() == users.size()) {
            for (User user : users) {
                String[] cols = new String[4];
                cols[0] = user.getUserName();
                cols[1] = user.getType().label;
                cols[2] = user.getPinCode();
                cols[3] = user.getPhoneNumber();
                userTableModel.addRow(cols);
            }
        } else {
            for (User user : searchedUsers) {
                String[] cols = new String[4];
                cols[0] = user.getUserName();
                cols[1] = user.getType().label;
                cols[2] = user.getPinCode();
                cols[3] = user.getPhoneNumber();
                userTableModel.addRow(cols);
            }
        }
        return userTableModel;
    }

    public void addProductToOrder(int table, String order) {
        List<Product> orderedProducts = orders.get(table);
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
        for (int i = 0; i < orders.get(tableNumber).size(); i++) {
            if (product.equalsIgnoreCase(orders.get(tableNumber).get(i).getBrandName())) {
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
        for (Product product1 : products) {
            if (product1.getBrandName().equalsIgnoreCase(product)) {
                product1.increaseQuantity();
                break;
            }
        }
    }

    public void lockDecreasingOfProducts() {
        for (Map.Entry<Integer, List<Product>> order : orders.entrySet()) {
            for (Product product : order.getValue()) {
                product.setLastConsumedQuantity(product.getQuantity());
                product.setCanDecrease(false);

            }
        }
    }

    public DefaultTableModel fetchOrderedProducts(DefaultTableModel ordersTable) {
        for (Map.Entry<Integer, List<Product>> order : orders.entrySet()) {
            if (order.getKey() == tableNumber) {
                ordersTable.setRowCount(0);
                for (Product product : order.getValue()) {
                    String[] cols = new String[3];
                    cols[0] = product.getBrandName();
                    cols[1] = String.format("%,.2f", (product.getQuantity() * 1000)) + " " + product.getMeasure();
                    cols[2] = String.format("%,.2f", product.getTotalPrice()) + " лв.";
                    ordersTable.addRow(cols);
                }
            }
        }
        return ordersTable;
    }

    public double getBill(int tableNumber, int discount) {
        double bill = 0.00;
        for (Product orderedProduct : orders.get(tableNumber)) {
            orderedProduct.setDiscount(discount);
            bill += orderedProduct.getTotalPrice();
        }
        return bill;
    }

    private Order generateOrder(double givenAmount, User waiter, List<Product> orderedProducts, String methodOfPay) {
        return new Order(givenAmount, waiter, orderedProducts, methodOfPay);
    }

    public boolean moveOrder(int oldTableNumber, int newTableNumber) {
        if (!orders.get(newTableNumber).isEmpty()) {
            return false;
        }
        List<Product> movingOrder = new ArrayList<>(orders.get(oldTableNumber));
        orders.replace(oldTableNumber, new ArrayList<>());
        orders.replace(newTableNumber, movingOrder);
        return true;
    }

    public void finishOrder(double givenAmount, User waiter, List<Product> orderedProducts, String methodOfPay) {
        histories.get(tableNumber).add(generateOrder(givenAmount, waiter, orderedProducts, methodOfPay));
        workingUsers.remove(tableNumber);
        orders.replace(tableNumber, new ArrayList<>());
        router.showLoginPanel();
    }

    public DefaultTableModel fetchOrdersHistory(DefaultTableModel ordersHistoryTable, int tableNumber) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        ordersHistoryTable.setRowCount(0);
        for (int i = 0; i < histories.get(tableNumber).size(); i++) {
            String[] cols = new String[6];
            cols[0] = String.valueOf(i + 1);
            cols[1] = format.format(histories.get(tableNumber).get(i).getDateOfOrder().getTime());
            cols[2] = histories.get(tableNumber).get(i).getWaiter().getUserName();
            cols[3] = histories.get(tableNumber).get(i).getMethodOfPay();
            cols[4] = String.format("%,.2f", histories.get(tableNumber).get(i).getTotalPrice()) + " лв.";
            cols[5] = String.format("%,.2f", histories.get(tableNumber).get(i).getGivenAmount()) + " лв.";
            ordersHistoryTable.addRow(cols);
        }
        return ordersHistoryTable;
    }

    public DefaultTableModel fetchProductsOfOrdersHistory(DefaultTableModel productsHistoryTable, int selectedOrder, int tableNumber) {
        for (Map.Entry<Integer, List<Order>> orders : histories.entrySet()) {
            if (orders.getKey() == tableNumber) {
                productsHistoryTable.setRowCount(0);
                for (Product product : orders.getValue().get(selectedOrder).getOrderedProducts()) {
                    String[] row = new String[3];
                    row[0] = product.getBrandName();
                    row[1] = String.format("%,.2f", (product.getQuantity() * 1000)) + " " + product.getMeasure();
                    row[2] = String.format("%,.2f", product.getTotalPrice()) + " лв.";
                    productsHistoryTable.addRow(row);
                }
                break;
            }
        }
        return productsHistoryTable;
    }

    public DefaultTableModel fetchDeliveryProducts(DefaultTableModel productsTableModel) {
        productsTableModel.setRowCount(0);
        if (products.size() == searchedProducts.size()) {
            for (Product product : products) {
                String[] cols = new String[5];
                cols[0] = product.getType().label;
                cols[1] = product.getSubtype();
                cols[2] = product.getBrandName();
                cols[3] = String.format("%,.2f", product.getPrice()) + " лв.";
                cols[4] = String.format("%,.2f", (product.getQuantity() * 1000)) + " " + product.getMeasure();
                productsTableModel.addRow(cols);
            }
        } else {
            for (Product product : searchedProducts) {
                String[] cols = new String[5];
                cols[0] = product.getType().label;
                cols[1] = product.getSubtype();
                cols[2] = product.getBrandName();
                cols[3] = String.format("%,.2f", product.getPrice()) + " лв.";
                cols[4] = String.format("%,.2f", (product.getQuantity() * 1000)) + " " + product.getMeasure();
                productsTableModel.addRow(cols);
            }
        }
        return productsTableModel;
    }

    public void getSearchedProducts(String searchedProduct) {
        searchedProducts.clear();
        for (Product product : products) {
            if (product.getType().label.toLowerCase().contains(searchedProduct.toLowerCase())) {
                searchedProducts.add(product);
            } else if (product.getSubtype().toLowerCase().contains(searchedProduct.toLowerCase())) {
                searchedProducts.add(product);
            } else if (product.getBrandName().toLowerCase().contains(searchedProduct.toLowerCase())) {
                searchedProducts.add(product);
            } else if (String.valueOf(product.getPrice()).contains(searchedProduct)) {
                searchedProducts.add(product);
            } else if (String.valueOf(product.getQuantity()).contains(searchedProduct)) {
                searchedProducts.add(product);
            }
        }
    }

    public void deleteProduct(String nameOfProduct) {
        int count = 0;
        int i = 0;
        if (nameOfProduct == null || nameOfProduct.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Не сте избрали артикул!", "Невалиден избор", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showConfirmDialog(null, "Сигурни ли сте в изтриването на артикула?");
        if (result == JOptionPane.YES_OPTION) {
            for (Product product : products) {
                i++;
                if (product.getBrandName().equalsIgnoreCase(nameOfProduct)) {
                    count++;
                    products.remove(i - 1);
                    searchedProducts.remove(i - 1);
                    break;
                }
            }
            if (count == 0) {
                JOptionPane.showMessageDialog(null, "Не сте избрали артикул!", "Невалиден избор", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void modifyProduct(String nameOfProduct) {
        int count = 0;
        String subCategory;
        String brandName;
        String totalQuantity;
        String price;
        if (nameOfProduct == null || nameOfProduct.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Не сте избрали артикул!", "Невалиден избор", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (Product product : products) {
            if (nameOfProduct.equalsIgnoreCase(product.getBrandName())) {
                count++;
                String[] options = {"Категория", "Подкатегория", "Име", "Общо количество", "Цена"};
                String[] productType = {ProductType.ALCOHOLIC.label, ProductType.NONALCOHOLIC.label, ProductType.FOOD.label, ProductType.COCKTAIL.label};
                switch (JOptionPane.showOptionDialog(null, "Какво ще се променя", "Избери какво ще се променя",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null)) {
                    case 0: {
                        ProductType type = verification.getProductType(JOptionPane.showOptionDialog(null, "Избери тип категория", "Тип категория",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, productType, options[3]));
                        if (type != null) {
                            product.setType(type);
                        }
                        break;
                    }
                    case 1: {
                        subCategory = JOptionPane.showInputDialog(null, "Въведи ново име на подкатегория: (без числа и символи)");
                        if (Objects.isNull(subCategory)) {
                            break;
                        }
                        subCategory = Objects.requireNonNull(subCategory).replaceAll("[^a-zA-Zа-яА-Я ]", "").trim();
                        if (subCategory.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Не може да се въведе празен ред", "Въведи име на подкатегория", JOptionPane.ERROR_MESSAGE);
                        } else if (subCategory.length() < 3) {
                            JOptionPane.showMessageDialog(null, "Минималното количество символи е 3! Въведи ново име на подкатегория", "Твърде кратъко име на подкатегория", JOptionPane.ERROR_MESSAGE);
                        } else {
                            product.setSubtype(subCategory);
                        }
                        break;
                    }
                    case 2: {
                        brandName = JOptionPane.showInputDialog(null, "Въведи ново име на подкатегория: (без числа и символи)");
                        if (Objects.isNull(brandName)) {
                            break;
                        }
                        brandName = Objects.requireNonNull(brandName).replaceAll("[^a-zA-Zа-яА-Я ]", "").trim();
                        if (brandName.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Не може да се въведе празен ред", "Въведи име на продукт", JOptionPane.ERROR_MESSAGE);
                        } else if (brandName.length() < 3) {
                            JOptionPane.showMessageDialog(null, "Минималното количество символи е 3! Въведи ново име на продукт", "Твърде кратъко име на продукт", JOptionPane.ERROR_MESSAGE);
                        } else {
                            product.setBrandName(brandName);
                        }
                        break;
                    }
                    case 3: {
                        totalQuantity = JOptionPane.showInputDialog(null, "Въведи ново общо количество: (само числа в л/кг.)");
                        if (Objects.isNull(totalQuantity)) {
                            break;
                        }
                        totalQuantity = Objects.requireNonNull(totalQuantity).replaceAll("[^(\\d+)||(\\.\\d+)$]", "").trim();
                        if (totalQuantity.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Не може да се въведе празен ред", "Въведи сервирано количество на продукт", JOptionPane.ERROR_MESSAGE);
                        } else if (Double.parseDouble(totalQuantity) < 1) {
                            JOptionPane.showMessageDialog(null, "Минималното количество е 1 л/кг! Въведи ново количество на продукт", "Твърде малко количество на продукт", JOptionPane.ERROR_MESSAGE);
                        } else {
                            product.setQuantity(Double.parseDouble(totalQuantity));
                        }
                        break;
                    }
                    case 4: {
                        price = JOptionPane.showInputDialog(null, "Въведи нова цена:");
                        if (Objects.isNull(price)) {
                            break;
                        }
                        price = Objects.requireNonNull(price).replaceAll("[^(\\d+)||(\\.\\d+)$]", "").trim();
                        if (price.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Не може да се въведе празен ред", "Въведи цена на продукт", JOptionPane.ERROR_MESSAGE);
                        } else {
                            product.setPrice(Double.parseDouble(price));
                        }
                        break;
                    }
                }
            }
        }
        if(count == 0) {
            JOptionPane.showMessageDialog(null, "Няма избран продукт!", "Невалиден избор", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deliveryOfProduct(String nameOfProduct) {
        int count = 0;
        double quantity = 0;
        if (nameOfProduct == null || nameOfProduct.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Не сте избрали артикул!", "Невалиден избор", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showConfirmDialog(null, "Сигурни ли сте в добавяне на количество към артикула?");
        if (result == JOptionPane.YES_OPTION) {
            for (Product product : products) {
                if (product.getBrandName().equalsIgnoreCase(nameOfProduct)) {
                    count++;
                    String quantityString = JOptionPane.showInputDialog(null, "Въведи количество, което ще се добавя. Минимумът е 10 мл/гр. и се закръгля през 10 мл/гр");
                    if (quantityString == null) {
                        return;
                    }
                    String str = quantityString.replaceAll("[^0-9.]","").trim();
                    if (str.isEmpty() || Double.parseDouble(str) <= 0) {
                        return;
                    } else {
                        quantity = (Double.parseDouble(str)/ 1000);
                        if (quantity < 0.01) {
                            JOptionPane.showMessageDialog(null, "Изисква се минимално количество от 10 мл/гр и се закръгля през 10 мл/гр!", "Грешка", JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            int mod = JOptionPane.showConfirmDialog(null, String.format("Сигурни ли сте в добавяне на %s към количеството на артикула", str.concat(" " + product.getMeasure())));
                            if (mod == JOptionPane.YES_OPTION) {
                                count++;
                                product.addQuantity(quantity);
                                break;
                            } else {
                                return;
                            }
                        }
                    }
                }
            }
            if (count == 0) {
                JOptionPane.showMessageDialog(null, "Не сте избрали артикул!", "Невалиден избор", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isNewProductOk(int productType, String subType, String brandName, String servedQuantity, String price) {
        if (!Objects.nonNull(verification.getProductType(productType))) {
            JOptionPane.showMessageDialog(null, "Не сте избрали правилна категория продукт!", "Невалиден избор", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (subType.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Не може да се въведе празен ред", "Въведи подкатегория на продукт", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (brandName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Не може да се въведе празен ред", "Въведи име на продукт", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (verification.isProductNameDubt(brandName, products)) {
            JOptionPane.showMessageDialog(null, "Не може да въведете дублиращ продукт", "Въведи ново име на продукт", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (servedQuantity.isEmpty() || !Pattern.matches("^(\\d+(\\.\\d+)?)", servedQuantity)) {
            JOptionPane.showMessageDialog(null, "Моля въведете правилно количество на продукт, което ще се сервира. (0.05 = 50мл/гр)", "Въведи ново количество", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (price.isEmpty() || !Pattern.matches("^(\\d+(\\.\\d+)?)", price)) {
            JOptionPane.showMessageDialog(null, "Моля въведете правилна цена за продукт", "Въведи нова цена", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    public void addNewProduct(int productType, String subType, String brandName, double servedQuantity, double price, boolean isLiquid) {
        Product product = new Product(verification.getProductType(productType), subType, brandName, servedQuantity, 0.00, price, isLiquid);
        products.add(product);
        searchedProducts.add((product));
    }
}
