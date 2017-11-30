package id.eightstudio.www.orderfoods.Model;

public class Food {

    private String Name, Image;
    private String Price, Discount, MenuId, Descrption;

    public Food() {
    }

    public Food(String name, String image, String price, String discount, String menuId, String descrption) {
        Name = name;
        Image = image;
        Price = price;
        Discount = discount;
        MenuId = menuId;
        Descrption = descrption;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
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

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getDescrption() {
        return Descrption;
    }

    public void setDescrption(String descrption) {
        Descrption = descrption;
    }
}
