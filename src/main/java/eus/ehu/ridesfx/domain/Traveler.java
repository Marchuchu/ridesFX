package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eus.ehu.ridesfx.domain.Alerts;

@Entity
public class Traveler extends User {


    private String repPassword;

    @OneToMany (fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Alerts> alertsList = new ArrayList<Alerts>();


    public Traveler(String email, String name, String password, String repPassword) {
        super(email, name, password);
        this.repPassword = repPassword;
    }

    public Traveler(String email, String name, String password) {
        super(email, name, password);
    }

    public Traveler() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Traveler other = (Traveler) obj;
        if (!(super.email.equals(other.email)))
            return false;
        if (!(super.getPassword().equals(repPassword)))
            return false;
        return true;
    }

    public String getPassword() {
        return repPassword;
    }

    public void setPassword(String repPassword) {
        this.repPassword = repPassword;
    }

    public List<Alerts> getAlertsList() {
        return alertsList;
    }

    public void setAlertsList(List<Alerts> alertsList) {
        this.alertsList = alertsList;
    }

    public Alerts addAlert(String from, String to, Date date) {
        Alerts alert = new Alerts(from, to, date);
        alertsList.add(alert);
        return alert;
    }



    public String toString() {
        return getName();
    }


}
