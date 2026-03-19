package model.payments;

import java.time.LocalDate;

public class CashPayment extends Payment {

    public CashPayment(LocalDate date, double price) {
        super(date, price);
    }


}
