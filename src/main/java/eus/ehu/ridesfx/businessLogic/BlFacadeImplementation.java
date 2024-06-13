package eus.ehu.ridesfx.businessLogic;

import eus.ehu.ridesfx.configuration.Config;
import eus.ehu.ridesfx.dataAccess.DataAccess;
import eus.ehu.ridesfx.domain.*;
import eus.ehu.ridesfx.exceptions.RideAlreadyExistException;
import eus.ehu.ridesfx.exceptions.RideMustBeLaterThanTodayException;
import eus.ehu.ridesfx.exceptions.UnknownUser;
import eus.ehu.ridesfx.utils.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Vector;


/**
 * Implements the business logic as a web service.
 */
public class BlFacadeImplementation implements BlFacade {

    DataAccess dbManager;
    Config config = Config.getInstance();
    User currentUser;

    public BlFacadeImplementation() {
        System.out.println("Creating BlFacadeImplementation instance");
        boolean initialize = config.getDataBaseOpenMode().equals("initialize");
        dbManager = new DataAccess(initialize);
        if (initialize)
            dbManager.initializeDB();

    }

    @Override
    public List<Ride> getRides(String origin, String destination, Date date) {
        List<Ride> events = dbManager.getRides(origin, destination, date);
        return events;
    }

    public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail) throws RideMustBeLaterThanTodayException, RideAlreadyExistException {
        Ride ride = dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
        return ride;
    }

    @Override
    public void createRideClick(String from, String to, Date date, int nPlaces, float price, String driverEmail) {
        dbManager.createRideClick(from, to, date, nPlaces, price, driverEmail);
    }

    @Override
    public void takeRide(Alert selectedItem, int nP, float p) {
        dbManager.takeRide(selectedItem, nP, p, this.currentUser);

    }

    @Override
    public List<Alert> getAlerts() {
        List<Alert> events = dbManager.getAlerts(this.currentUser);
        return events;
    }

    @Override
    public List<Alert> getAlerts(User u) {
        List<Alert> events = dbManager.getAlerts(u);
        return events;
    }

    @Override
    public List<Alert> getAllAlerts() {
        List<Alert> events = dbManager.getAllAlerts();
        return events;
    }

    @Override
    public void cancelAlert(Alert alert) {
        User currentUser = alert.getUser();
        dbManager.cancelAlert(currentUser, alert.getId());
    }

    @Override
    public void createAlert(String from, String to, Date date, String email) {
        dbManager.createAlert(from, to, date, email);
    }

    @Override
    public List<Alert> getAlertsByUser(User user) {
        return dbManager.getAlerts(user);
    }

    @Override
    public Alert createNewAlert(String from, String to, Date date, String email) {

        return dbManager.addAlert(from, to, date, email);

    }

    @Override
    public User getCurrentUser() {
        return this.currentUser;
    }

    public User getUserByEmail(String email) {
        return dbManager.getUserByEmail(email);
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void addUser(User u) {
        dbManager.addUser(u);
    }

    @Override
    public boolean containsUser(User u) {

        return dbManager.containsUser(u);

    }

    @Override
    public User getDriver(User u) {

        return dbManager.getDriver(u);

    }

    @Override
    public void setCurrentDriver(Driver driver) {
        this.currentUser = driver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
        List<Date> dates = dbManager.getThisMonthDatesWithRides(from, to, date);
        return dates;
    }

    /**
     * This method invokes the data access to retrieve the dates a month for which there are events
     *
     * @param date of the month for which days with events want to be retrieved
     * @return collection of dates
     */

    public Vector<Date> getEventsMonth(Date date) {
        Vector<Date> dates = dbManager.getEventsMonth(date);
        return dates;
    }

    @Override
    public List<String> getDepartCities() {
        List<String> departLocations = dbManager.getDepartCities();
        return departLocations;

    }

    @Override
    public List<String> getArrivalCities(String from) {

        List<String> arrivalLocations = dbManager.getArrivalCities(from);
        return arrivalLocations;

    }

    @Override
    public void addCitie(String c) {
        try {
            dbManager.addCitie(c);

            List<String> departLocations = dbManager.getDepartCities();
            if (!departLocations.contains(c)) {
                departLocations.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(StringUtils.translate("BIFacadeImplementation.ErrorToAddCitie") + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getDestinationCities(String from) {
        List<String> targetCities = dbManager.getArrivalCities(from);
        return targetCities;
    }

    @Override
    public List<Date> getDatesWithRides(String value, String value1) {
        List<Date> dates = dbManager.getDatesWithRides(value, value1);
        return dates;
    }

    @Override
    public boolean signUp(String name, String email, String password, String repeatpassword, String role) {
        return dbManager.signUp(name, email, password, repeatpassword, role);
    }


    @Override
    public boolean logIn(String email, String password) throws UnknownUser {

        User driver = dbManager.logIn(email, password);

        this.currentUser = driver;

        if ((driver != null)) {
            return true;
        } else {
            return false;

        }


    }

    @Override
    public void sendMessage(String from, String to, String subject, String message) {

        dbManager.sendMessage(dbManager.getUserByEmail(from), dbManager.getUserByEmail(to), subject, message);

    }

    @Override

    public List<Message> getAllMessages(){

        return dbManager.getMessages();

    }

    public List<Message> getAllMessagesFromUser(User u) {
        return dbManager.getMessagesFromUser(u);
    }

    public List<Message> getAllMessagesToUser(User u) {
        return dbManager.getMessagesToUser(u);
    }

}