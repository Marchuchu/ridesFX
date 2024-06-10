package eus.ehu.ridesfx.businessLogic;

import eus.ehu.ridesfx.domain.*;
import eus.ehu.ridesfx.exceptions.RideAlreadyExistException;
import eus.ehu.ridesfx.exceptions.RideMustBeLaterThanTodayException;
import eus.ehu.ridesfx.exceptions.UnknownUser;

import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Interface that specifies the business logic.
 */

public interface BlFacade {
    /**
     * This method retrieves the rides from two locations on a given date
     *
     * @param from the origin location of a ride
     * @param to   the destination location of a ride
     * @param date the date of the ride
     * @return collection of rides
     */
    List<Ride> getRides(String from, String to, Date date);

    public List<Alerts> getAlerts();

    public List<Alerts> getAlerts(User u);

    void cancelAlert(Alerts alert);
    User getCurrentUser();

    void createAlert(String from, String to, Date date, String email);
    List<Alerts> getAlertsByUser(User user);

    /**
     * This method retrieves from the database the dates a month for which there are events
     *
     * @param from the origin location of a ride
     * @param to   the destination location of a ride
     * @param date of the month for which days with rides want to be retrieved
     * @return collection of rides
     */
    public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);

    public void addUser(User user);

    public Alerts createNewAlert(String from, String to, Date date, String email);

    /**
     * This method retrieves from the database the dates in a month for which there are events
     *
     * @param date of the month for which days with events want to be retrieved
     * @return collection of dates
     */
    public Vector<Date> getEventsMonth(Date date);

    //ArrayList<User> getCurrentUser();

    //ArrayList<User> getCurrentUser(User u);

//    public User getCurrentUser();

    void setCurrentUser(User user);

    public boolean containsUser(User u);

    void setCurrentDriver(Driver driver);

    Ride createRide(String text, String text1, Date date, int inputSeats, float price, String email) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;


    /**
     * This method returns all the cities where rides depart
     *
     * @return collection of cities
     */

    public List<String> getDepartCities();

    public List<String> getArrivalCities(String from);

    /**
     * This method returns all the arrival destinations, from all rides that depart from a given city
     *
     * @param from the departure location of a ride
     * @return all the arrival destinations
     */

    public List<String> getDestinationCities(String from);


    List<Date> getDatesWithRides(String value, String value1);

    public boolean signUp(String name, String email, String password, String repeatpassword, String role);

    public boolean logIn(String username, String password) throws UnknownUser;

    //void cancelAlert(TableColumn<String, Integer> alertID);
//    void cancelAlert(Alerts ride);

//    public void createAlert(String from, String to, Date date, Traveler traveler);

    void createRideClick(String from, String to, Date date, int nPlaces, float price, String driverEmail);


    User getDriver(User u);

    void addCitie(String from);

    public void takeRide(Alerts selectedItem, int nP, float p);

    public void sendMessage(String to, String subject, String message);

    List<Alerts>  getAllAlerts();

}