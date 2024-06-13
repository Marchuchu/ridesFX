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

    Ride createRide(String text, String text1, Date date, int inputSeats, float price, String email) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;

    void createRideClick(String from, String to, Date date, int nPlaces, float price, String driverEmail);

    void takeRide(Alert selectedItem, int nP, float p);

    List<Alert> getAlerts();

    List<Alert> getAlerts(User u);

    List<Alert> getAllAlerts();

    void cancelAlert(Alert alert);

    void createAlert(String from, String to, Date date, String email);

    List<Alert> getAlertsByUser(User user);

    Alert createNewAlert(String from, String to, Date date, String email);

    User getCurrentUser();

    void setCurrentUser(User user);

    void addUser(User user);

    boolean containsUser(User u);

    User getDriver(User u);

    User getUserByEmail(String email);

    void setCurrentDriver(Driver driver);

    /**
     * This method retrieves from the database the dates a month for which there are events
     *
     * @param from the origin location of a ride
     * @param to   the destination location of a ride
     * @param date of the month for which days with rides want to be retrieved
     * @return collection of rides
     */
    public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);

    /**
     * This method retrieves from the database the dates in a month for which there are events
     *
     * @param date of the month for which days with events want to be retrieved
     * @return collection of dates
     */
    public Vector<Date> getEventsMonth(Date date);

    /**
     * This method returns all the cities where rides depart
     *
     * @return collection of cities
     */

    List<String> getDepartCities();

    List<String> getArrivalCities(String from);

    void addCitie(String from);

    /**
     * This method returns all the arrival destinations, from all rides that depart from a given city
     *
     * @param from the departure location of a ride
     * @return all the arrival destinations
     */

    List<String> getDestinationCities(String from);

    List<Date> getDatesWithRides(String value, String value1);

    boolean signUp(String name, String email, String password, String repeatpassword, String role);

    boolean logIn(String username, String password) throws UnknownUser;

    void sendMessage(String from, String to, String subject, String message);

    List<Message> getAllMessages();


    List<Message> getAllMessagesFromUser(User u);

    List<Message> getAllMessagesToUser(User u);
}