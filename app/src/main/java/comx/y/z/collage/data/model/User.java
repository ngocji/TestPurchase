package comx.y.z.collage.data.model;

import androidx.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Keep
public class User {
    public String name;
    public String des;
    public String image;
    public String link;
    public String screen;
    public String pack;
    public Integer number;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String des, String image, String link, String screen, String pack, Integer number) {
        this.name = name;
        this.des = des;
        this.image = image;
        this.link = link;
        this.screen = screen;
        this.pack = pack;
        this.number = number;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }
    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}

