package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Alerts")

public class Alerts {

    private String from;
    private String to;
    private Date date;

    @ManyToOne
    private Traveler traveler;

    @Id
    private String email;

    public Alerts(String from, String to, Date date) {

        this.from = from;
        this.to = to;
        this.date = date;

    }

    public Alerts(String from, String to, Date date, String email) {

        this.from = from;
        this.to = to;
        this.date = date;
        this.email = email;
    }

    public Alerts(String from, String to, Date date, String email, Traveler traveler) {

        this.from = from;
        this.to = to;
        this.date = date;
        this.email = email;
        this.traveler = traveler;
    }


    public Alerts() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JoinColumn
    public Alerts getAlert() {
        return this;
    }


    public void setAlert(Alerts alert) {
        this.from = alert.getFrom();
        this.to = alert.getTo();
        this.date = alert.getDate();
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Traveler getTraveler() {
        return traveler;
    }

    public void setTraveler(Traveler traveler) {
        this.traveler = traveler;
    }

    @Override
    public String toString() {
        return "MyAlert{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", date=" + date +
                '}';
    }


}
