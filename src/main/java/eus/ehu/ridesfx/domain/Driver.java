package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;

@Entity
public class Driver extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String password;
    private String repPassword;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Ride> rides = new Vector<Ride>();


    public Driver(String email, String name) {

        super(name, email);
        
    }

    public Driver(String email, String name, String password) {
        super(name, email);
        this.password = password;

    }

    public Driver(String email, String name, String password, String repeatePassword) {
        super(email, name, password);
        this.repPassword = repeatePassword;

    }

    public Driver() {

    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }


    //public String toString() {        return email + ";" + rides;    }

    /**
     * This method creates a new ride for the driver
     *
     * @param
     * @param
     * @return
     */
    public Ride addRide(String from, String to, Date date, int nPlaces, float price) {
        Ride ride = new Ride(from, to, date, nPlaces, price, this);
        rides.add(ride);
        return ride;
    }



    /**
     * This method checks if the ride already exists for that driver
     *
     * @param from the origin location
     * @param to   the destination location
     * @param date the date of the ride
     * @return true if the ride exists and false in other case
     */
    public boolean doesRideExists(String from, String to, Date date) {
        for (Ride r : rides)
            if ((java.util.Objects.equals(r.getFromLocation(), from)) && (java.util.Objects.equals(r.getToLocation(), to)) && (java.util.Objects.equals(r.getDate(), date)))
                return true;

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Driver other = (Driver) obj;
        if (!(super.email.equals(other.email)))
            return false;
        return true;
    }

//    public Ride removeRide(String from, String to, Date date) {
//        boolean found = false;
//        int index = 0;
//        Ride r = null;
//        while (!found && index <= rides.size()) {
//            r = rides.get(++index);
//            if ((java.util.Objects.equals(r.getFromLocation(), from)) && (java.util.Objects.equals(r.getToLocation(), to)) && (java.util.Objects.equals(r.getDate(), date)))
//                found = true;
//        }
//
//        if (found) {
//            rides.remove(index);
//            return r;
//        } else return null;
//    }

    public Ride removeRide(String from, String to, Date date) {
        boolean found = false;
        int index = 0;
        Ride r = null;
        while (!found && index < rides.size()) {
            r = rides.get(index);
            if ((java.util.Objects.equals(r.getFromLocation(), from)) && (java.util.Objects.equals(r.getToLocation(), to)) && (java.util.Objects.equals(r.getDate(), date)))
                found = true;
            else
                index++;
        }

        if (found) {
            rides.remove(index);
            return r;
        } else
            return null;
    }


}
