package JSWD.Web.model;

import jakarta.persistence.*;
import org.springframework.context.annotation.Lazy;

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
    private Employee employee;

    public Image(int id) {
        this.id = id;
    }

    public Image(int id, Employee employee) {
        this.id = id;
        this.employee = employee;
    }

    public Image(String imageData) {
        this.imageData = imageData;
    }

    public Image(int id, Employee employee, String imageData) {
        this.id = id;
        this.employee = employee;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}