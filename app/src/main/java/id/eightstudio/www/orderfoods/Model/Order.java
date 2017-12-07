package id.eightstudio.www.orderfoods.Model;

/**
 * Created by danbo on 30/11/17.
 */

public class Order {

    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Discount;
    private String KeyUser;

    public Order() {
    }

    public Order(String quantity) {
        Quantity = quantity;
    }

    public Order(String productId, String productName, String quantity, String price, String discount, String keyUser) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
        KeyUser = keyUser;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getKeyUser() {
        return KeyUser;
    }

    public void setKeyUser(String keyUser) {
        KeyUser = keyUser;
    }
}
