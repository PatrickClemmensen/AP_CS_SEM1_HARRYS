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
        return null;
    }

    public void addProduct() {
    }

    public double getTotalAmount() {
        return 0.0;
    }
    public void setTotalAmount(double amount){
        this.totalAmount = amount;
    }
    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }









}
