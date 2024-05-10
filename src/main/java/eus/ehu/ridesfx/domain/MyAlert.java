package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "ALERTS") // Renames the table to avoid using a reserved keyword
@DiscriminatorColumn(name = "Alerts", discriminatorType = DiscriminatorType.STRING)

public class MyAlert extends Ride{

//    @Column(name = "From")
//    private String from;
//
//    @Column(name = "To")
//    private String to;
//
//    @Column(name = "Date")
//    private Date date;

    @Id
    private String email;



    public MyAlert(String from, String to, Date date) {
        super(from, to, date);
    }

    public MyAlert(String from, String to, Date date, String email) {
        super(from, to, date);
        this.email = email;
    }



    public MyAlert() {

    }
//
//    public String getFrom() {
//        return from;
//    }
//
//    public void setFrom(String from) {
//        this.from = from;
//    }
//
//    public String getTo() {
//        return to;
//    }
//
//    public void setTo(String to) {
//        this.to = to;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }

//    @Override
//    public String toString() {
//        return "MyAlert{" +
//                "from='" + from + '\'' +
//                ", to='" + to + '\'' +
//                ", date=" + date +
//                '}';
//    }



}
