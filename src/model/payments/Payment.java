package model.payments;

import model.products.Product;
import java.time.LocalDate;
import java.util.ArrayList;


public abstract class Payment {
    private LocalDate paymentDate;
    private ArrayList<Product> products;
    private double totalAmount;

    public Payment(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        this.products = new ArrayList<>();
        this.totalAmount = 0;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public double getTotalAmount() {
        return totalAmount;
    }


    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }









}
