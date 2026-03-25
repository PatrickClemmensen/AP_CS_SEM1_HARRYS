package model.payments;

import java.time.LocalDate;

/**
 * Represents a credit payment made at Harry's Salon.
 * <p>
 *     A credit payment is not settled immediately and must be
 *     manually marked as settled when the customer pays their debt.
 * </p>
 */
public class CreditPayment extends Payment {
    private boolean isSettled;

    /**
     * Creates a new credit payment with a total amount and payment date.
     * The payment is initially marked as not settled.
     *
     * @param totalAmount the total amount owed by the customer
     * @param date        the date the credit was registered
     */
    public CreditPayment(double totalAmount, LocalDate date) {
        super(totalAmount, date);
        isSettled = false;
    }

    /**
     * Returns the payment status for a credit payment.
     *
     * @return {@link PaymentStatus#CREDIT}
     */
    @Override
    public PaymentStatus getPaymentStatus() { return PaymentStatus.CREDIT;}

    /**
     * Returns whether this credit payment has been settled.
     *
     * @return true if the payment has been settled, false otherwise
     */
    public boolean isSettled() {
        return isSettled;
    }

    /**
     * Sets the settled status of this credit payment.
     *
     * @param settled true if the payment has been settled, false otherwise
     */
    public void setSettled(boolean settled){
        this.isSettled = settled;
    }

    /**
     * Returns the settled status as a string.
     *
     * @return "true" if the payment has been settled, "false" otherwise
     */
    @Override
    public String getSettledString() { return String.valueOf(isSettled()); }
}
