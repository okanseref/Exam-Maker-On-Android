package tr.STD17011042.OkanSeref;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String username,surname,password,email,phone,bdate;

    private byte[] image;

    public User(String username, String surname, String password, String email, String phone, String bdate, byte[] image) {
        this.username = username;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.bdate = bdate;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getBdate() {
        return bdate;
    }

    public byte[] getImage() {
        return image;
    }
}
