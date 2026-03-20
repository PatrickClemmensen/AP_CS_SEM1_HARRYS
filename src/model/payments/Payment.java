package model.payments;

import model.products.Product;
import java.time.LocalDate;
import java.util.ArrayList;


public abstract class Payment {
    private LocalDate paymentDate;
    private ArrayList<Product> products;
    private double totalAmount;

    public Payment(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        this.products = new ArrayList<>();
        this.totalAmount = 0;
    }

    public ArrayList<Product> getProducts() {
        return null;
    }

    public void addProduct() {
    }

    public double getTotalAmount() {
        double total = 0.0;
        for(Product p : products){
            total += p.getPrice();

        }
        return total;
    }

    /**
     * @deprecated Temporary setter used during CSV deserialization.
     * Will be removed when getTotalAmount() calculates from List<Product>.
     */
    @Deprecated
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }









}
