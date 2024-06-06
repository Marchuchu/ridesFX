package eus.ehu.ridesfx.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Message {

    @Id
    private int id;
    private String from;
    private String to;
    private String message;
    private String subject;

    public Message(String from, String to, String message, String subject) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.subject = subject;
    }

    public Message(int id,String from, String message, String subject) {
        this.id = id;
        this.from = from;
        this.message = message;
        this.subject = subject;
    }

    public Message() {

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }

}
