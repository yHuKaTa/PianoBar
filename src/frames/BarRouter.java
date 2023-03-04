package frames;

import models.Order;
import models.Product;
import models.User;
import panels.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class BarRouter{
    private final BarFrame frame;

    public BarRouter(BarFrame frame){
        this.frame = frame;
    }
    public void showLoginPanel(){
        LoginPanel loginPanel = new LoginPanel(frame);
        frame.setContentPane(loginPanel);
        frame.validate();
    }
    public void showManagersHomePanel(User loggedUser, Map<Integer, ArrayList<Product>> orders, Map<Integer, ArrayList<Order>> histories){
        ManagersHomePanel adminPanel = new ManagersHomePanel(frame, loggedUser, orders, histories);
        frame.setContentPane(adminPanel);
        frame.validate();
    }
    public void showOwnersPanel(User loggedUser, Map<Integer, ArrayList<Product>> orders, Map<Integer, ArrayList<Order>> histories){
        OwnerHomePanel ownerHomePanel = new OwnerHomePanel(frame, loggedUser, orders, histories);
        frame.setContentPane(ownerHomePanel);
        frame.validate();
    }
    public void showTablesPanel(User loggedUser, Map<Integer, ArrayList<Product>> orders){
        TablesPanel tablesPanel = new TablesPanel(frame, loggedUser, orders);
        frame.setContentPane(tablesPanel);
        frame.validate();
    }
    public void showOrdersPanel(User loggedUser, int tableNumber, Map<Integer, ArrayList<Product>> orders){
        OrdersPanel ordersPanel = new OrdersPanel(frame, loggedUser, tableNumber, orders);
        frame.setContentPane(ordersPanel);
        frame.validate();
    }
    public void showPayPanel(int tableNumber, User loggedUser,  Map<Integer, ArrayList<Product>> orders){
        PayPanel payPanel = new PayPanel(frame,tableNumber,loggedUser, orders);
        frame.setContentPane(payPanel);
        frame.validate();
    }
    public void showHistoryPanel(User loggedUser, Map<Integer, ArrayList<Product>> orders,Map<Integer, ArrayList<Order>> histories){
        HistoryPanel historyPanel = new HistoryPanel(frame,loggedUser, orders, histories);
        frame.setContentPane(historyPanel);
        frame.validate();
    }
    public void showOrderedProductPanel(User loggedUser, Map<Integer, ArrayList<Product>> orders,Map<Integer, ArrayList<Order>> histories, int tableNumber) {
        OrderedProductPanel orderedProductPanel = new OrderedProductPanel(frame, loggedUser,orders, histories, tableNumber);
        frame.setContentPane(orderedProductPanel);
        frame.validate();
    }
    public void showUsersPanel(User loggedUser, Map<Integer, ArrayList<Product>> orders){
        UsersPanel usersPanel = new UsersPanel(frame,loggedUser, orders);
        frame.setContentPane(usersPanel);
        frame.validate();
    }
    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
