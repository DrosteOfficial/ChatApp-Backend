package JSWD.Web.model;

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
    private UserDetails userDetails;

    public Image(int id) {
        this.id = id;
    }

    public Image(int id, UserDetails userDetails) {
        this.id = id;
        this.userDetails = userDetails;
    }

    public Image(String imageData) {
        this.imageData = imageData;
    }

    public Image(int id, UserDetails userDetails, String imageData) {
        this.id = id;
        this.userDetails = userDetails;
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

    public UserDetails getEmployee() {
        return userDetails;
    }

    public void setEmployee(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}