package JSWD.Web.model.chatSpecific;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String topic;

    public Topic() {
    }
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable( joinColumns = @JoinColumn(name = "topic_Id"), inverseJoinColumns = @JoinColumn(name = "message_Id") )
    private Collection<Message> messages;


    public Topic(String topic) {
        this.topic = topic;
    }
    public Topic(int id, String topic) {
        this.id = id;
        this.topic = topic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }
}
