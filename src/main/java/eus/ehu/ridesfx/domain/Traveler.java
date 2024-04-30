package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

@Entity
public class Traveler extends Messenger {


    private String repPassword;


    public Traveler(String email, String name, String password, String repPassword) {
        super(email, name, password);
        this.repPassword = repPassword;
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

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public String toString() {
        return getName();
    }


}
