package model.payments;

import java.time.LocalDate;


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
    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }
}
