package eus.ehu.ridesfx.businessLogic;

import eus.ehu.ridesfx.configuration.Config;
import eus.ehu.ridesfx.dataAccess.DataAccess;
import eus.ehu.ridesfx.domain.*;
import eus.ehu.ridesfx.exceptions.RideAlreadyExistException;
import eus.ehu.ridesfx.exceptions.RideMustBeLaterThanTodayException;
import eus.ehu.ridesfx.exceptions.UnknownUser;
import javafx.scene.control.TableColumn;

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

    public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail) throws RideMustBeLaterThanTodayException, RideAlreadyExistException {
        Ride ride = dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
        return ride;
    }

    @Override
    public List<Ride> getRides(String origin, String destination, Date date) {
        List<Ride> events = dbManager.getRides(origin, destination, date);
        return events;
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
    public User getCurrentUser() {
        return this.currentUser;
    }



    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void setCurrentDriver(Driver driver) {
        this.currentUser = driver;
    }

    public void addCitie(String c){
        List<String> departLocations = dbManager.getDepartCities();
        departLocations.add(c);
        dbManager.addCitie(c);

    }

    public void addUser(User u){
        dbManager.addUser(u);
    }

    @Override
    public MyAlert createAlert(String from, String to, Date date, String email) {

        return dbManager.addAlert(from, to, date, email);

    }

    public List<String> getDepartCities() {
        List<String> departLocations = dbManager.getDepartCities();
        return departLocations;

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
    public boolean checkComboBox(String city){

        return dbManager.checkComboBox(city);

    }

    @Override
    public void updateComboBox(String city) {


    }

    @Override
    public void takeRide(Ride selectedItem, int nP, float p) {
        dbManager.takeRide(selectedItem, nP, p );
    }

    @Override
    public void createRideClick(String from, String to, Date date, int nPlaces, float price, String driverEmail) {
        dbManager.createRideClick(from, to, date, nPlaces, price, driverEmail);
    }

    @Override
    public User getDriver(User u){

        return dbManager.getD(u);

    }

    @Override

    public boolean containsUser(User u){

        return dbManager.containsUser(u);

    }


    @Override
    public void cancelAlert(Ride r) {
        dbManager.cancelAlert(r);
    }


}