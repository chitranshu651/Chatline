package Login_Signup;

import javafx.scene.image.Image;

import java.io.File;
import java.io.Serializable;

public class User implements Serializable {
    private String First;
    private String Last;
    private String Email;
    private String Username;
    private String Password;
    private String salt;
    private String Status;
    private String avatar;
    private Image img;
    private File pic;
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public File getPic() {
        return pic;
    }

    public void setPic(File pic) {
        this.pic = pic;
    }

    public User(String avatar, String first, String last, String email, String username, String password, String salt, String status) {
        First = first;
        Last = last;
        Email = email;
        Username = username;
        Password = password;
        this.salt = salt;
        Status = status;
        this.avatar = avatar;
    }

    public String getFirst() {
        return First;
    }

    public String getLast() {
        return Last;
    }

    public String getEmail() {
        return Email;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public String getSalt() {
        return salt;
    }

    public String getStatus() {
        return Status;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


}
