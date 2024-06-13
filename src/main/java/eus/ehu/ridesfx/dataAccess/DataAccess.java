package eus.ehu.ridesfx.dataAccess;

import eus.ehu.ridesfx.configuration.Config;
import eus.ehu.ridesfx.configuration.UtilDate;
import eus.ehu.ridesfx.domain.*;
import eus.ehu.ridesfx.exceptions.RideAlreadyExistException;
import eus.ehu.ridesfx.exceptions.RideMustBeLaterThanTodayException;
import eus.ehu.ridesfx.exceptions.UnknownUser;
import jakarta.persistence.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.ConstraintViolationException;

import java.util.*;

/**
 * Implements the Data Access utility to the objectDb database
 */
public class DataAccess {

    protected EntityManager db;
    protected EntityManagerFactory emf;

    public DataAccess() {
        this.open();
    }

    public DataAccess(boolean initializeMode) {
        this.open(initializeMode);
    }

    public void open() {
        open(false);
    }

    public void open(boolean initializeMode) {
        Config config = Config.getInstance();

        System.out.println("Opening DataAccess instance => isDatabaseLocal: " +
                config.isDataAccessLocal() + " getDataBaseOpenMode: " + config.getDataBaseOpenMode());

        if (config.isDataAccessLocal()) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            try {
                emf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
                System.out.println("EntityManagerFactory created successfully.");
            } catch (Exception e) {
                StandardServiceRegistryBuilder.destroy(registry);
                System.err.println("Error creating EntityManagerFactory: " + e.getMessage());
                e.printStackTrace();
            }

            if (emf != null) {
                db = emf.createEntityManager();
                System.out.println("EntityManager created successfully.");
            } else {
                System.err.println("EntityManagerFactory is null. Cannot create EntityManager.");
            }
        } else {
            System.err.println("DataAccess is not configured for local access.");
        }
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
            System.out.println("EntityManager is closed.");
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("EntityManagerFactory is closed.");
        }
    }

    public void reset() {
        db.getTransaction().begin();
        db.createQuery("DELETE FROM Ride").executeUpdate();
        db.createQuery("DELETE FROM Driver").executeUpdate();
        db.getTransaction().commit();
    }

    public User login(String username, String password) throws UnknownUser {
        TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.name = :username AND u.password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new UnknownUser();
        }
    }

    public User logIn(String email, String password) throws UnknownUser {
        User driver;
        TypedQuery<User> query = db.createQuery("SELECT d FROM User d WHERE d.email =?1 AND d.password =?2",
                User.class);
        query.setParameter(1, email);
        query.setParameter(2, password);

        try {
            driver = query.getSingleResult();
        } catch (Exception e) {
            throw new UnknownUser();
        }
        return driver;

    }

    public boolean signUp(String name, String email, String password, String repeatePassword, String role) {

        User u = new User(email, name, password);

        if (!containsUser(u)) {

            if (role.equals("Driver")) {

                if (email.contains("@")) {

                    if (password.equals(repeatePassword)) {

                        db.getTransaction().begin();
                        Driver driver = new Driver(email, name, password, repeatePassword);
                        db.persist(driver);
                        db.getTransaction().commit();

                        return true;
                    }
                }
            } else if (role.equals("Traveler")) {

                if (email.contains("@")) {

                    if (password.equals(repeatePassword)) {

                        db.getTransaction().begin();
                        Traveler traveler = new Traveler(email, name, password, repeatePassword);
                        db.persist(traveler);
                        db.getTransaction().commit();
                        return true;

                    }

                }

            }

        }

        return false;

    }

    public void addUser(User user) {

        db.getTransaction().begin();
        db.persist(user);
        db.getTransaction().commit();

    }

    public User getD(User u) {
        return db.find(User.class, u.getEmail());
    }

    public boolean containsUser(User u) {

        TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.email =?1",
                User.class);
        query.setParameter(1, u.getEmail());
        List<User> users = query.getResultList();
        if (users.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public List<Ride> getRides(String origin, String destination, Date date) {
        System.out.println(">> DataAccess: getRides origin/dest/date");
        Vector<Ride> res = new Vector<>();

        TypedQuery<Ride> query = db.createQuery("SELECT ride FROM Ride ride "
                + "WHERE ride.date=?1 ", Ride.class);
        query.setParameter(1, date);


        return query.getResultList();
    }

    public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail) throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
        System.out.println(">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverEmail + " date " + date);
        try {
            if (new Date().compareTo(date) > 0) {
                throw new RideMustBeLaterThanTodayException(
                        ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
            }

            db.getTransaction().begin();

            Driver driver = db.find(Driver.class, driverEmail);

            if (driver == null) {
                db.getTransaction().rollback();
                throw new PersistenceException("Driver not found: " + driverEmail);
            }

            if (driver.doesRideExists(from, to, date)) {
                db.getTransaction().rollback();
                throw new RideAlreadyExistException(
                        ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
            }

            // Create new ride
            Ride ride = driver.addRide(from, to, date, nPlaces, price);
            db.persist(ride);
            db.getTransaction().commit();

            return ride;
        } catch (NullPointerException e) {
            db.getTransaction().rollback();
            throw e; // Optional: or handle the exception more gracefully
        } catch (RideAlreadyExistException | RideMustBeLaterThanTodayException e) {
            db.getTransaction().rollback();
            throw e;
        }
    }

    public void takeRide(Alert a, int nP, float p, User u) {
        db.getTransaction().begin();

        User user = db.find(User.class, u.getEmail());

        Driver driver = (Driver) user;

        driver = db.merge(driver);

        Ride ride = driver.addRide(a.getFrom(), a.getTo(), a.getDate(), nP, p);

        db.persist(ride);
        db.merge(driver);

        Alert alertToRemove = db.find(Alert.class, a.getId());
        if (alertToRemove != null) {
            db.remove(alertToRemove);
        }

        db.getTransaction().commit();
    }

    private boolean rideExists(Ride ride) {

        return db.createQuery("SELECT COUNT(r) FROM Ride r WHERE r.fromLocation = :from AND r.toLocation = :to AND r.date = :date AND r.driver = :driver", Long.class)
                .setParameter("from", ride.getFromLocation())
                .setParameter("to", ride.getToLocation())
                .setParameter("date", ride.getDate())
                .setParameter("driver", ride.getDriver())
                .getSingleResult() > 0;
    }

    public void createRideClick(String from, String to, Date date, int nPlaces, float price, String driverEmail) {
        db.getTransaction().begin();
        Driver driver = db.find(Driver.class, driverEmail);
        Ride ride = driver.addRide(from, to, date, nPlaces, price);
        db.persist(ride);
        db.getTransaction().commit();

    }


    /**
     * This method returns all the cities where rides depart
     *
     * @return collection of cities
     */

    public List<String> getDepartCities() {
        TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.fromLocation FROM Ride r ORDER BY r.fromLocation", String.class);
        List<String> cities = query.getResultList();
        return cities;

    }

    /**
     * This method returns all the arrival destinations, from all rides that depart from a given city
     *
     * @param from the departure location of a ride
     * @return all the arrival destinations
     */
    public List<String> getArrivalCities(String from) {
        TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.toLocation FROM Ride r WHERE r.fromLocation=?1 ORDER BY r.toLocation", String.class);
        query.setParameter(1, from);
        List<String> arrivingCities = query.getResultList();
        return arrivingCities;

    }

    public void addCitie(String c) {
        if (c != null) {
            List<String> departLocations = getDepartCities();
            departLocations.add(c);
        }
    }

    public Alert addAlert(String from, String to, Date date, String travelerEmail) {
        db.getTransaction().begin();

        try {
            Traveler traveler = db.find(Traveler.class, travelerEmail);
            if (traveler == null) {
                throw new PersistenceException("Traveler not found: " + travelerEmail);
            }

            Alert alert = new Alert(from, to, date);
            if (alertExists(alert)) {
                throw new PersistenceException("Alert already exists.");
            }

            traveler.addAlert(from, to, date);
            db.persist(traveler);
            db.getTransaction().commit();

            return alert;
        } catch (Exception e) {
            db.getTransaction().rollback();
            throw new PersistenceException("Error adding alert: " + e.getMessage(), e);
        }
    }

    public List<Alert> getAlerts(User user) {

        TypedQuery<Alert> query = db.createQuery("""
                SELECT alerts FROM Alert alerts WHERE alerts.user = :user""", Alert.class);
        query.setParameter("user", user);

        return query.getResultList();
    }

    public Alert getAlert(User user, int alertId) {
        TypedQuery<Alert> query = db.createQuery(
                """
                        SELECT a FROM Alert a WHERE a.id = :alertId AND a.user = :user""", Alert.class);
        query.setParameter("alertId", alertId);
        query.setParameter("user", user);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // or handle as needed
        }
    }

    public List<Alert> getAllAlerts() {

        TypedQuery<Alert> query = db.createQuery("""
                SELECT alerts FROM Alert alerts""", Alert.class);

        return query.getResultList();
    }

    private boolean alertExists(Alert alert) {
        // Método para verificar si una alerta ya existe en la base de datos
        return db.createQuery("""
                        SELECT COUNT(a) FROM Alert a WHERE a.from = :departCity AND a.to = :arrivalCity AND a.date = :date""", Long.class)
                .setParameter("departCity", alert.getFrom())
                .setParameter("arrivalCity", alert.getTo())
                .setParameter("date", alert.getDate())
                .getSingleResult() > 0;
    }

    public Alert getAlert(User user, Long alertId) {
        TypedQuery<Alert> query = db.createQuery("SELECT a FROM Alert a WHERE a.id = :alertId AND a.user = :user", Alert.class);
        query.setParameter("alertId", alertId);
        query.setParameter("user", user);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // or handle as needed
        }
    }

    public void cancelAlert(User user, Long alertId) {
        db.getTransaction().begin();

        try {
            Alert alert = getAlert(user, alertId);

            if (alert == null) {
                throw new IllegalArgumentException("Alert not found or does not belong to the user");
            }

            db.remove(alert);
            db.getTransaction().commit();

        } catch (Exception e) {
            db.getTransaction().rollback();
            throw new PersistenceException("Error canceling alert: " + e.getMessage(), e);
        }
    }

    public void createAlert(String from, String to, Date date, String email) {
        db.getTransaction().begin();
        try {
            User user = db.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();

            Alert alert = new Alert(from, to, date, user);
            db.persist(alert);
            db.getTransaction().commit();
        } catch (Exception e) {
            db.getTransaction().rollback();
            throw new PersistenceException("Error creating alert: " + e.getMessage(), e);
        }
    }

    public Vector<Date> getEventsMonth(Date date) {
        System.out.println(">> DataAccess: getEventsMonth");
        Vector<Date> res = new Vector<>();

        Date firstDayMonthDate = UtilDate.firstDayMonth(date);
        Date lastDayMonthDate = UtilDate.lastDayMonth(date);

        TypedQuery<Date> query = db.createQuery("SELECT DISTINCT ride.date FROM Ride ride WHERE ride.date BETWEEN :firstDay AND :lastDay", Date.class);
        query.setParameter("firstDay", firstDayMonthDate);
        query.setParameter("lastDay", lastDayMonthDate);
        List<Date> dates = query.getResultList();
        for (Date d : dates) {
            System.out.println(d.toString());
            res.add(d);
        }
        return res;
    }

    public boolean checkComboBox(String city) {
        TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.toLocation FROM Ride r WHERE r.fromLocation = :city OR r.toLocation = :city ORDER BY r.toLocation", String.class);
        query.setParameter("city", city);
        return query.getResultList().isEmpty();
    }

    public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
        System.out.println(">> DataAccess: getEventsMonth");
        List<Date> res = new ArrayList<>();

        Date firstDayMonthDate = UtilDate.firstDayMonth(date);
        Date lastDayMonthDate = UtilDate.lastDayMonth(date);


        TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.fromLocation=?1 AND r.toLocation=?2 AND r.date BETWEEN ?3 and ?4", Date.class);

        query.setParameter(1, from);
        query.setParameter(2, to);
        query.setParameter(3, firstDayMonthDate);
        query.setParameter(4, lastDayMonthDate);
        List<Date> dates = query.getResultList();
        for (Date d : dates) {
            res.add(d);
        }
        return res;
    }

    public List<Date> getDatesWithRides(String from, String to) {
        System.out.println(">> DataAccess: getEventsFromTo");
        List<Date> res = new ArrayList<>();

        TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.fromLocation=?1 AND r.toLocation=?2", Date.class);

        query.setParameter(1, from);
        query.setParameter(2, to);
        List<Date> dates = query.getResultList();
        for (Date d : dates) {
            res.add(d);
        }
        return res;
    }

    public void sendMessage(User from, User to, String subject, String message) {

        db.getTransaction().begin();
        db.persist(new Message(from, to, subject, message));
        db.getTransaction().commit();

    }

    public void initializeDB() {
        this.reset();
        db.getTransaction().begin();

        Calendar today = Calendar.getInstance();
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);
        if (month == 12) {
            month = 1;
            year += 1;
        }

        // Create drivers
        Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez", "12345", "12345");
        Driver driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga", "54321", "54321");
        Driver driver3 = new Driver("driver3@gmail.com", "Leyre Martinez", "12345", "12345");

        // Create travelers
        Traveler traveler1 = new Traveler("traveler1@gmail.com", "Jose Antonio", "amorch1", "amorch");
        Traveler traveler2 = new Traveler("traveler2@gmail.com", "Lius Fernando", "54321", "54321");

        // Create Alerts
        Alert alert1 = new Alert("Donostia", "Bilbo", UtilDate.newDate(year, month + 1, 15), traveler1);
        db.persist(alert1);

        // Create rides
        driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month + 1, 15), 4, 7);
        driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month + 1, 15), 4, 7);
        driver1.addRide("Donostia", "Gasteiz", UtilDate.newDate(year, month + 1, 6), 4, 8);
        driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month + 1, 25), 4, 4);
        driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year, month + 1, 7), 4, 8);

        driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month + 1, 15), 3, 3);
        driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month + 1, 25), 2, 5);
        driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year, month + 1, 6), 2, 5);

        driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month + 1, 14), 1, 3);

        Message message1 = new Message(traveler2, driver1, "Doubt", "Hello, can i take my cat with me in the car?");
        Message message3 = new Message(traveler1, driver1, "Doubt", "Hello, can i take my dog with me in the car?");

        db.persist(message1);
        db.persist(message3);
        db.persist(driver1);
        db.persist(driver2);
        db.persist(driver3);
        db.persist(traveler1);
        db.persist(traveler2);

        db.getTransaction().commit();
    }


    public List<Message> getMessages(){
        TypedQuery<Message> query = db.createQuery("SELECT m FROM Message m", Message.class);
        return query.getResultList();
    }

    public User getUserByEmail(String e) {

         return db.find(User.class, e);

    }

    public List<Message> getMessagesFromUser(User u){
        TypedQuery<Message> query = db.createQuery("SELECT m FROM Message m WHERE m.to = :u", Message.class);
        query.setParameter("u", u);
        return query.getResultList();
    }

    public List<Message> getMessagesToUser(User u){

        TypedQuery<Message> query = db.createQuery("SELECT m FROM Message m WHERE m.from = :u", Message.class);
        query.setParameter("u", u);
        return query.getResultList();

    }

}
