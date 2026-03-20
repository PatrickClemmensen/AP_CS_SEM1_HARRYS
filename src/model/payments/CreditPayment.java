package model.payments;

import java.time.LocalDate;

public class CreditPayment extends Payment {
    private boolean isSettled;

    public CreditPayment(double totalAmount, LocalDate date) {
        super(date);
        isSettled = false;
        setTotalAmount(totalAmount);
    }

    public boolean isSettled() {
        return isSettled;
    }

    public void setSettled(boolean settled){
        this.isSettled = settled;
    }
}
