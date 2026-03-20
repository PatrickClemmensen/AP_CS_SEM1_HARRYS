package model.payments;

import java.time.LocalDate;

public class CashPayment extends Payment {

    public CashPayment(double totalAmount, LocalDate date) {
        super(date);
        setTotalAmount(totalAmount);
    }


}
