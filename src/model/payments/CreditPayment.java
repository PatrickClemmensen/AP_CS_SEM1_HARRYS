package model.payments;

import java.time.LocalDate;

public class CreditPayment extends Payment {
    private boolean isSettled;

    public CreditPayment(double totalAmount, LocalDate date) {
        super(totalAmount, date);
        isSettled = false;
    }

    @Override
    public PaymentStatus getPaymentStatus() { return PaymentStatus.CREDIT;}

    public boolean isSettled() {
        return isSettled;
    }

    public void setSettled(boolean settled){
        this.isSettled = settled;
    }

    @Override
    public String getSettledString() { return String.valueOf(isSettled()); }
}
