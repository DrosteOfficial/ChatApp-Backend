package JSWD.Web.model.security.user;

import JSWD.Web.model.chatSpecific.Image;
import JSWD.Web.model.chatSpecific.Message;
import JSWD.Web.model.chatSpecific.Topic;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_information")
public class UserInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( joinColumns = @JoinColumn(name = "userInformation_id"), inverseJoinColumns = @JoinColumn(name = "topic_Id") )
    private Set<Topic> topics;
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
    public boolean isEmpty() {
        return this.id == 0;
    }
    public Image getImagedata() {
        return imagedata;
    }
    public void setImagedata(Image imagedata) {
        this.imagedata = imagedata;
    }

    public Set<Topic> getTopics() {
        return topics;
    }
    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }
}
