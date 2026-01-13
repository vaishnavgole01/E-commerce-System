import java.util.*;

// Abstract Product class
abstract class Product {
    protected String id;
    protected String name;
    protected double price;
    protected String description;
    protected int stockQuantity;
    
    public Product(String id, String name, double price, String description, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
    }
    
    // Abstract method - each product type calculates discount differently
    public abstract double calculateDiscount();
    
    // Concrete method
    public double getFinalPrice() {
        return price - calculateDiscount();
    }
    
    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public int getStockQuantity() { return stockQuantity; }
    
    public void setStockQuantity(int quantity) { 
        this.stockQuantity = quantity; 
    }
    
    public void displayInfo() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Price: ₹" + price);
        System.out.println("Description: " + description);
        System.out.println("Stock: " + stockQuantity);
        System.out.println("Discount: ₹" + calculateDiscount());
        System.out.println("Final Price: ₹" + getFinalPrice());
    }
}

// Electronics Product
class ElectronicsProduct extends Product {
    private String brand;
    private int warrantyMonths;
    
    public ElectronicsProduct(String id, String name, double price, String description, 
                             int stockQuantity, String brand, int warrantyMonths) {
        super(id, name, price, description, stockQuantity);
        this.brand = brand;
        this.warrantyMonths = warrantyMonths;
    }
    
    @Override
    public double calculateDiscount() {
        // Electronics get 10% discount
        return price * 0.10;
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Type: Electronics");
        System.out.println("Brand: " + brand);
        System.out.println("Warranty: " + warrantyMonths + " months");
        System.out.println("-".repeat(40));
    }
}

// Clothing Product
class ClothingProduct extends Product {
    private String size;
    private String color;
    private String material;
    
    public ClothingProduct(String id, String name, double price, String description, 
                          int stockQuantity, String size, String color, String material) {
        super(id, name, price, description, stockQuantity);
        this.size = size;
        this.color = color;
        this.material = material;
    }
    
    @Override
    public double calculateDiscount() {
        // Clothing gets 15% discount
        return price * 0.15;
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Type: Clothing");
        System.out.println("Size: " + size);
        System.out.println("Color: " + color);
        System.out.println("Material: " + material);
        System.out.println("-".repeat(40));
    }
}

// Shopping Cart Item
class CartItem {
    private Product product;
    private int quantity;
    
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getItemTotal() {
        return product.getFinalPrice() * quantity;
    }
}

// Shopping Cart
class ShoppingCart {
    private List<CartItem> items;
    private double totalAmount;
    
    public ShoppingCart() {
        items = new ArrayList<>();
        totalAmount = 0;
    }
    
    public void addItem(Product product, int quantity) {
        // Check if product already in cart
        for(CartItem item : items) {
            if(item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                calculateTotal();
                return;
            }
        }
        
        // Add new item
        items.add(new CartItem(product, quantity));
        calculateTotal();
    }
    
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
        calculateTotal();
    }
    
    public void updateQuantity(String productId, int quantity) {
        for(CartItem item : items) {
            if(item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                calculateTotal();
                return;
            }
        }
    }
    
    private void calculateTotal() {
        totalAmount = 0;
        for(CartItem item : items) {
            totalAmount += item.getItemTotal();
        }
    }
    
    public void displayCart() {
        System.out.println("\n=== SHOPPING CART ===");
        if(items.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }
        
        System.out.printf("%-15s %-20s %-10s %-10s %-12s\n", 
                         "Product ID", "Name", "Price", "Qty", "Total");
        System.out.println("-".repeat(70));
        
        for(CartItem item : items) {
            Product p = item.getProduct();
            System.out.printf("%-15s %-20s ₹%-9.2f %-10d ₹%-11.2f\n", 
                            p.getId(), p.getName(), p.getFinalPrice(), 
                            item.getQuantity(), item.getItemTotal());
        }
        
        System.out.println("-".repeat(70));
        System.out.printf("Total Amount: ₹%.2f\n", totalAmount);
    }
    
    public double getTotalAmount() { return totalAmount; }
    public List<CartItem> getItems() { return items; }
}

// Order class
class Order {
    private static int orderCounter = 1000;
    private String orderId;
    private Date orderDate;
    private ShoppingCart cart;
    private double finalAmount;
    
    public Order(ShoppingCart cart) {
        this.orderId = "ORD" + (orderCounter++);
        this.orderDate = new Date();
        this.cart = cart;
        this.finalAmount = calculateFinalAmount();
    }
    
    private double calculateFinalAmount() {
        double total = cart.getTotalAmount();
        // Add 18% GST
        return total * 1.18;
    }
    
    public void displayOrder() {
        System.out.println("\n=== ORDER DETAILS ===");
        System.out.println("Order ID: " + orderId);
        System.out.println("Order Date: " + orderDate);
        
        cart.displayCart();
        
        System.out.println("\nOrder Summary:");
        System.out.println("Subtotal: ₹" + cart.getTotalAmount());
        System.out.println("GST (18%): ₹" + (cart.getTotalAmount() * 0.18));
        System.out.println("Final Amount: ₹" + finalAmount);
        System.out.println("Thank you for your order!");
    }
}

public class Main {
    static Scanner sc = new Scanner(System.in);
    static ShoppingCart cart = new ShoppingCart();
    static List<Product> products = new ArrayList<>();

    public static void main(String[] args) {

        // Sample products
        products.add(new ElectronicsProduct(
                "E101", "Laptop", 60000,
                "High performance laptop", 10,
                "Dell", 24
        ));

        products.add(new ElectronicsProduct(
                "E102", "Smartphone", 25000,
                "Android smartphone", 15,
                "Samsung", 12
        ));

        products.add(new ClothingProduct(
                "C201", "T-Shirt", 1200,
                "Cotton casual wear", 50,
                "L", "Black", "Cotton"
        ));

        products.add(new ClothingProduct(
                "C202", "Jeans", 2200,
                "Denim jeans", 30,
                "32", "Blue", "Denim"
        ));

        int choice;

        do {
            System.out.println("\n=== E-COMMERCE SYSTEM ===");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Update Cart");
            System.out.println("5. Checkout");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1 -> viewProducts();
                case 2 -> addToCart();
                case 3 -> cart.displayCart();
                case 4 -> updateCart();
                case 5 -> checkout();
                case 6 -> System.out.println("Thank you for using E-Commerce System!");
                default -> System.out.println("Invalid choice! Try again.");
            }

        } while (choice != 6);
    }

    // ================= METHODS =================

    static void viewProducts() {
        System.out.println("\n=== AVAILABLE PRODUCTS ===");
        for (Product p : products) {
            p.displayInfo();
        }
    }

    static void addToCart() {
        System.out.print("Enter Product ID: ");
        String id = sc.next();

        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();

        for (Product p : products) {
            if (p.getId().equalsIgnoreCase(id)) {
                cart.addItem(p, qty);
                System.out.println("Product added to cart!");
                return;
            }
        }
        System.out.println("Product not found!");
    }

    static void updateCart() {
        System.out.println("\n1. Remove Item");
        System.out.println("2. Update Quantity");
        System.out.print("Enter choice: ");
        int ch = sc.nextInt();

        System.out.print("Enter Product ID: ");
        String id = sc.next();

        if (ch == 1) {
            cart.removeItem(id);
            System.out.println("Item removed from cart.");
        } else if (ch == 2) {
            System.out.print("Enter new quantity: ");
            int qty = sc.nextInt();
            cart.updateQuantity(id, qty);
            System.out.println("Quantity updated.");
        } else {
            System.out.println("Invalid option!");
        }
    }

    static void checkout() {
        if (cart.getItems().isEmpty()) {
            System.out.println("Cart is empty! Add items before checkout.");
            return;
        }

        Order order = new Order(cart);
        order.displayOrder();
    }
}
