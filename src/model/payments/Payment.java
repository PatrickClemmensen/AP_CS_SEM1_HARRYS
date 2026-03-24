package model.payments;

import model.products.Product;
import java.time.LocalDate;
import java.util.ArrayList;


public abstract class Payment {
    private LocalDate paymentDate;
    private ArrayList<Product> products;
    private double totalAmount;
    private PaymentStatus paymentStatus;

    public Payment(double totalAmount, LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        this.products = new ArrayList<>();
        this.totalAmount = totalAmount;
        this.paymentStatus = PaymentStatus.CREDIT;
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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }









}
