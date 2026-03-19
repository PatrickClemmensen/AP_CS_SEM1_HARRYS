package model.payments;

import model.products.Product;
import java.time.LocalDate;
import java.util.ArrayList;


public abstract class Payment {
    private LocalDate paymentDate;
    private ArrayList<Product> products;

    public Payment(LocalDate paymentDate, double price) {
        this.paymentDate = paymentDate;
        this.products = new ArrayList<>;
    }

    public ArrayList<Product> getProducts() {
        return null
    }

    public void addProduct() {
    }

    public double getTotalAmount() {
        return null
    }

    public LocalDate getPaymentDate() {
        return paymentDate
    }









}
