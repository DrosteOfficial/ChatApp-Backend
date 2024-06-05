package JSWD.Web.model.comunication;

import JSWD.Web.model.chatSpecific.Message;

public class JsonPayload {
    private  String regularToken;
    private Message message;
    private String topic;

    public JsonPayload() {
    }
    public JsonPayload(Message message, String topic, String regularToken) {
        this.message = message;
        this.topic = topic;
        this.regularToken = regularToken;
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

    public String getRegularToken() {
        return regularToken;
    }


    public void setRegularToken(String regularToken) {
        this.regularToken = regularToken;
    }
}
