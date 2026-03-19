package model.payments;

import java.time.LocalDate;

public class CreditPayment extends Payment {
    private boolean isSettled;

    public CreditPayment(LocalDate date, double price, boolean isSettled) {
        super(date, price);
        this.isSettled = isSettled;
    }

    public boolean isSettled() {
        return isSettled;
    }
}
