package model.products;

import java.util.ArrayList;

/**
 * Enum of products in Harry's.
 * <p>
 *     Each product has the following attributes:
 *     <ul>
 *         <li>String name - name of the product</li>
 *         <li>double price - price of the product</li>
 *         <li>{@link Category} enum - describes if product is a service or retail-product</li>
 *     </ul>
 * </p>
 */
public enum Product {

    // Services
    MENS_HAIRCUT(250.0, Category.SERVICE),
    WOMENS_HAIRCUT(350.0, Category.SERVICE),

    // Retail
    SHAMPOO(45.0, Category.RETAIL),
    HAIRBRUSH(89.0, Category.RETAIL);

    private final double price;
    private final Category category;

    Product(double price, Category category) {
        this.price = price;
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public static ArrayList<Product> getServices() {
        ArrayList<Product> result = new ArrayList<>();
        for (Product p : values()) {
            if (p.category == Category.SERVICE) result.add(p);
        }
        return result;
    }

    public static ArrayList<Product> getRetailProducts() {
        ArrayList<Product> result = new ArrayList<>();
        for (Product p : values()) {
            if (p.category == Category.RETAIL) result.add(p);
        }
        return result;
    }
}