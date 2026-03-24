package model.payments;

import model.products.Product;
import java.time.LocalDate;
import java.util.ArrayList;


public abstract class Payment {
    private LocalDate paymentDate;
    private double totalAmount;

    public Payment(double totalAmount, LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        this.totalAmount = totalAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public abstract PaymentStatus getPaymentStatus();

    public String getSettledString() { return "null"; }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }









}
