package model.payments;

import java.time.LocalDate;

/**
 * Abstract base class representing a payment at the salon
 * <P>
 *     A payment can either be a {@link CashPayment} or a {@link CreditPayment}
 *     Subclasses must implement {@link #getPaymentStatus()} to define
 *     thier specific payment type
 * </P>
 */
public abstract class Payment {
    private LocalDate paymentDate;
    private double totalAmount;

    /**
     * Creates a new payment with a total amount and payment date.
     *
     * @param totalAmount the total amount of the payment
     * @param paymentDate the date the payment was registered
     */
    public Payment(double totalAmount, LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        this.totalAmount = totalAmount;
    }

    /**
     * Returns the total amount of this payment.
     *
     * @return the total amount as a double
     */
    public double getTotalAmount() {
        return totalAmount;
    }
    /**
     * Returns the payment status of this payment
     *
     * @return the {@link PaymentStatus} of this payment
     */
    public abstract PaymentStatus getPaymentStatus();

    /**
     * Returns the settled status of this payment as a string.
     * <P>
     *     Returns "null" by default. Overridden by {@link CreditPayment}
     *     to return the actual settled status.
     * </P>
     *
     * @return "null" for cash payments, "true" or "false" for credit payments
     */
    public String getSettledString() { return "null"; }

    /**
     * Returns the date this payment was registered.
     *
     * @return the payment date as a {@link LocalDate}
     */
    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }
}
