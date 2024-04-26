package JSWD.Web.model;

import jakarta.persistence.*;
import org.springframework.context.annotation.Lazy;

@Entity
@Table (name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;


    @Lob
    private String imageData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Image(String imageData) {
        this.imageData = imageData;
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