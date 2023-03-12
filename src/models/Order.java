package models;

import java.util.Calendar;
import java.util.List;

public class Order {
    private final double givenAmount;
    private final User waiter;
    private final Calendar dateOfOrder;
    private final List<Product> orderedProducts;
    private final String methodOfPay;

    public Order(double givenAmount, User waiter, List<Product> orderedProducts, String methodOfPay) {
        this.givenAmount = givenAmount;
        this.waiter = waiter;
        this.dateOfOrder = Calendar.getInstance();
        this.orderedProducts = orderedProducts;
        this.methodOfPay = methodOfPay;
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

    public List<Product> getOrderedProducts() {
        return orderedProducts;
    }

    public Double getTotalPrice() {
        double totalPrice = 0;
        for (Product product : this.orderedProducts) {
            totalPrice += product.getTotalPrice();
        }
        return totalPrice;
    }
    public String getMethodOfPay() {
        return this.methodOfPay;
    }
}
