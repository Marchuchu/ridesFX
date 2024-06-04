package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Alerts")

public class Alerts {

    @ManyToOne
    @Id
    @GeneratedValue
    private Traveler traveler;

    @Column(name = "alert_from")
    private String from;

    @Column(name = "alert_to")
    private String to;

    // JPA date is a keyword, name it something else
    // change column name
    @Temporal(TemporalType.DATE)
    @Column(name = "alert_date")
    private Date date;


    public Alerts(String from, String to, Date date) {

        this.from = from;
        this.to = to;
        this.date = date;
    }

    public Alerts(String from, String to, Date date, Traveler traveler) {

        this.from = from;
        this.to = to;
        this.date = date;
        this.traveler = traveler;
    }


    public Alerts() {

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
