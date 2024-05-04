package JSWD.Web.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "UserDetails")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Employee_messege", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "message_id"))
    private List<Message> messages;

    @OneToOne
    @JoinColumn(name = "image", referencedColumnName = "id")
    private Image imagedata;


    public UserDetails() {
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public boolean isEmpty() {
        return this.id == 0;
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

    public void addMessageToMessages(Message message) {
        this.messages.add(message);
    }


}
