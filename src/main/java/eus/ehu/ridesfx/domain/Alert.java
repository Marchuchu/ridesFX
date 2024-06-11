package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usamos IDENTITY para generar el ID automáticamente
    private Long id;

    @ManyToOne
    @JoinColumn(name = "traveler_id") // Define la columna de unión para el viajero
    private User user;

    @Column(name = "alert_from", nullable = false)
    private String from;

    @Column(name = "alert_to", nullable = false)
    private String to;

    @Temporal(TemporalType.DATE)
    @Column(name = "alert_date", nullable = false)
    private Date date;

    public Alert() {
    }

    public Alert(String from, String to, Date date) {
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public Alert(String from, String to, Date date, User traveler) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.user = traveler;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
