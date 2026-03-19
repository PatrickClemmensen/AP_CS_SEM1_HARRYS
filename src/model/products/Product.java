package model.products;

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

    public enum Category {
        SERVICE,
        RETAIL
    }
}