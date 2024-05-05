package JSWD.Web.model.security.user;

import JSWD.Web.model.Image;
import JSWD.Web.model.Message;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user_information")
public class UserInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "employee_messege", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "message_id"))
    private List<Message> messages;

    @OneToOne
    @JoinColumn(name = "image", referencedColumnName = "id")
    private Image imagedata;


    public UserInformation() {
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
