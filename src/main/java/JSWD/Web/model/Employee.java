package JSWD.Web.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;

import java.sql.Blob;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    private String name;
    private String gender;
    private String department;
    private Date dob;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Employee_messege", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "message_id"))
    private List<Message> messages;

    @OneToOne
    @JoinColumn(name = "image", referencedColumnName = "id")
    private Image imagedata;

    public Employee() {
    }

    public Employee(String name, String gender, String department, Date dob) {
        this.name = name;
        this.gender = gender;
        this.department = department;
        this.dob = dob;
    }

    public Employee(int id, String name, String gender, String department, Date dob) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.department = department;
        this.dob = dob;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getDepartment() {
        return department;
    }

    public Date getDob() {
        return dob;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public boolean isEmpty() {
        return this.id == 0 && this.name == null;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Image getImagedata() {
        return imagedata;
    }

    public void setImagedata(Image imagedata) {
        this.imagedata = imagedata;
    }
    public void addMessageToMessages(Message message){
        this.messages.add(message);
    }
}
