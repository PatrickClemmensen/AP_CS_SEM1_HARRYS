package model.payments;

import java.time.LocalDate;

public class CreditPayment extends Payment {
    private boolean isSettled;

    public CreditPayment(LocalDate date) {
        super(date);
        isSettled = false;
    }

    public boolean isSettled() {
        return isSettled;
    }

    public void setSettled(boolean settled){
        this.isSettled = settled;
    }
}
