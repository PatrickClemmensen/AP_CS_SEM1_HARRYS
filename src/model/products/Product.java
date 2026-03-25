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

    /**
     * Creates a new product with a price and category.
     *
     * @param price     the price of the product
     * @param category  the {@link Category} of the product
     */
    Product(double price, Category category) {
        this.price = price;
        this.category = category;
    }

    /**
     * Returns the price of this product.
     *
     * @return the price as a double
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the category of this product.
     *
     * @return the {@link Category} of this product
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns a list of all products categorized as services
     *
     * @return an {@link ArrayList} of products with {@link Category#SERVICE}
     */
    public static ArrayList<Product> getServices() {
        ArrayList<Product> result = new ArrayList<>();
        for (Product p : values()) {
            if (p.category == Category.SERVICE) result.add(p);
        }
        return result;
    }

    /**
     * Returns a list of all products categorized as retail products.
     *
     * @return an {@link ArrayList} of products with {@link Category#RETAIL}
     */
    public static ArrayList<Product> getRetailProducts() {
        ArrayList<Product> result = new ArrayList<>();
        for (Product p : values()) {
            if (p.category == Category.RETAIL) result.add(p);
        }
        return result;
    }
}