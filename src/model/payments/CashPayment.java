package model.payments;

import java.time.LocalDate;

/**
 * Represents a cash payment made at Harry's Salon.
 * <P>
 *     A cash payment is considered settled immediately upon creation.
 * </P>
 */
public class CashPayment extends Payment {
    /**
     * Creates a new cash payment with a total amount and payment date.
     *
     * @param totalAmount the total amount is paid in cash
     * @param date        the date the payment was made
     */
    public CashPayment(double totalAmount,LocalDate date) {
        super(totalAmount, date);
    }

    /**
     * Returns the payment status for a cash payment.
     *
     * @return {@link PaymentStatus#CASH}
     */
    @Override
    public PaymentStatus getPaymentStatus() { return PaymentStatus.CASH;}

}
