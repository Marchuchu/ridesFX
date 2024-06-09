package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Alerts")
public class Alerts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usamos IDENTITY para generar el ID automáticamente
    private Long id;

    @ManyToOne
    @JoinColumn(name = "traveler_id") // Define la columna de unión para el viajero
    private Traveler traveler;

    @Column(name = "alert_from", nullable = false)
    private String from;

    @Column(name = "alert_to", nullable = false)
    private String to;

    @Temporal(TemporalType.DATE)
    @Column(name = "alert_date", nullable = false)
    private Date date;

    public Alerts() {
    }

    public Ride getRideFromAlerts(Alerts alert) {
        String from = alert.getFrom();
        String to = alert.getTo();
        Date date = alert.getDate();
        return new Ride(from, to, date);
    }

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

    public Long getId() {
        return id;
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
