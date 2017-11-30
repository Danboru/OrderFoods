package id.eightstudio.www.orderfoods.Model;

/**
 * Created by danbo on 30/11/17.
 */

public class User {

    private String Name, Password,IsStaff, Phone;

    public User() {
    }

    public User(String name, String password, String isstaff) {
        Name = name;
        Password = password;
        IsStaff = isstaff;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}
