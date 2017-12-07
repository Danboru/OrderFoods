package id.eightstudio.www.orderfoods.Model;

/**
 * Created by danbo on 30/11/17.
 */

public class User {

    private String Name, TanggalLahir, BulanLahir, TahunLahir, Password, IsStaff, Phone;

    public User() {
    }

    public User(String name, String tanggalLahir, String bulanLahir, String tahunLahir, String password, String isStaff) {
        Name = name;
        TanggalLahir = tanggalLahir;
        BulanLahir = bulanLahir;
        TahunLahir = tahunLahir;
        Password = password;
        IsStaff = isStaff;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTanggalLahir() {
        return TanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        TanggalLahir = tanggalLahir;
    }

    public String getBulanLahir() {
        return BulanLahir;
    }

    public void setBulanLahir(String bulanLahir) {
        BulanLahir = bulanLahir;
    }

    public String getTahunLahir() {
        return TahunLahir;
    }

    public void setTahunLahir(String tahunLahir) {
        TahunLahir = tahunLahir;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
