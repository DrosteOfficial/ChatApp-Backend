package JSWD.Web.model.chatSpecific;

import JSWD.Web.model.security.user.UserInformation;
import jakarta.persistence.*;

@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imageData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private UserInformation userInformation;

    public Image(int id) {
        this.id = id;
    }

    public Image(int id, UserInformation userInformation) {
        this.id = id;
        this.userInformation = userInformation;
    }

    public Image(String imageData) {
        this.imageData = imageData;
    }

    public Image(int id, UserInformation userInformation, String imageData) {
        this.id = id;
        this.userInformation = userInformation;
        this.imageData = imageData;
    }

    public Image() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public UserInformation getEmployee() {
        return userInformation;
    }

    public void setEmployee(UserInformation userInformation) {
        this.userInformation = userInformation;
    }
}
