package JSWD.Web.model.comunication;

import JSWD.Web.model.chatSpecific.Message;

public class JsonPayload {
    private Message message;
    private String topic;

    public JsonPayload() {
    }
    public JsonPayload(Message message, String topic) {
        this.message = message;
        this.topic = topic;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
