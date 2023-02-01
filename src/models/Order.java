package models;

import java.util.ArrayList;
import java.util.Calendar;

public class Order {
    private int tableNumber;
    private double givenAmount;
    private User waiter;
    private Calendar dateOfOrder;
    private ArrayList<Product> orderedProducts;

    public Order(int tableNumber, double givenAmount, User waiter, ArrayList<Product> orderedProducts) {
        this.tableNumber = tableNumber;
        this.givenAmount = givenAmount;
        this.waiter = waiter;
        this.dateOfOrder = Calendar.getInstance();
        this.orderedProducts = orderedProducts;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public double getGivenAmount(){
        return givenAmount;
    }

    public User getWaiter() {
        return waiter;
    }

    public Calendar getDateOfOrder() {
        return dateOfOrder;
    }

    public ArrayList<Product> getOrderedProducts() {
        return orderedProducts;
    }
}
