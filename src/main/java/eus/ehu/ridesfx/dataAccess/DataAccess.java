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

    public void reset() {
        db.getTransaction().begin();
        db.createQuery("DELETE FROM Ride").executeUpdate();
        db.createQuery("DELETE FROM Driver").executeUpdate();
        db.getTransaction().commit();
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
                throw new RideMustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
            }

            db.getTransaction().begin();

            Driver driver = db.find(Driver.class, driverEmail);

            if (driver.doesRideExists(from, to, date)) {
                db.getTransaction().commit();
                throw new RideAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
            } else {

                //create new ride

                Ride ride = driver.addRide(from, to, date, nPlaces, price);
                db.persist(ride);
                db.getTransaction().commit();

                ////

                //quiero añadir la nueva cioudad al combo box de la clase QueryRidesController


            }
            Ride ride = driver.addRide(from, to, date, nPlaces, price);
            //next instruction can be obviated
            db.persist(driver);
            db.getTransaction().commit();

            return ride;
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            db.getTransaction().commit();
            return null;
        }


    }


    public void initializeDB() {
        this.reset();
        db.getTransaction().begin();

        try {
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
            Driver driver3 = new Driver("driver3@gmail.com", "Test driver", "12345", "12345");

            // Create travelers
            Traveler traveler1 = new Traveler("traveler1@gmail.com", "Jose Antonio", "amorch1", "amorch");
            Traveler traveler2 = new Traveler("traveler2@gmail.com", "Lius Fernando", "54321", "54321");

            // Create Alerts
            Alerts alert1 = new Alerts("Donostia", "Bilbo", UtilDate.newDate(year, month + 1, 15), traveler1);
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

            db.persist(driver1);
            db.persist(driver2);
            db.persist(driver3);
            db.persist(traveler1);
            db.persist(traveler2);

            db.getTransaction().commit();

            System.out.println("Db initialized");
        } catch (ConstraintViolationException e) {
            System.err.println("Constraint violation: " + e.getConstraintName());
            e.printStackTrace();
            db.getTransaction().rollback();
        } catch (PersistenceException e) {
            System.err.println("Persistence exception: " + e.getMessage());
            e.printStackTrace();
            db.getTransaction().rollback();
        } catch (Exception e) {
            System.err.println("General exception: " + e.getMessage());
            e.printStackTrace();
            db.getTransaction().rollback();
        }
    }

    private boolean alertExists(Alerts alert) {
        // Método para verificar si una alerta ya existe en la base de datos
        return db.createQuery("SELECT COUNT(a) FROM Alerts a WHERE a.from = :departCity AND a.to = :arrivalCity AND a.date = :date", Long.class)
                .setParameter("departCity", alert.getFrom())
                .setParameter("arrivalCity", alert.getTo())
                .setParameter("date", alert.getDate())
                .getSingleResult() > 0;
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

    public Alerts addAlert(String from, String to, Date date, String travelerEmail) {
        db.getTransaction().begin();

        try {
            Traveler traveler = db.find(Traveler.class, travelerEmail);
            if (traveler == null) {
                throw new PersistenceException("Traveler not found: " + travelerEmail);
            }

            Alerts alert = new Alerts(from, to, date);
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

    public Ride addRide(String from, String to, Date date, int availableSeats, float price, String driverEmail) throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
        if (date.before(UtilDate.today())) {
            throw new RideMustBeLaterThanTodayException("Ride must be later than today.");
        }

        db.getTransaction().begin();

        try {
            Driver driver = db.find(Driver.class, driverEmail);
            if (driver == null) {
                throw new PersistenceException("Driver not found: " + driverEmail);
            }

            Ride ride = new Ride(from, to, date, availableSeats, price, driver);
            if (rideExists(ride)) {
                throw new RideAlreadyExistException("Ride already exists.");
            }

            driver.addRide(from, to, date, availableSeats, price);
            db.persist(driver);
            db.getTransaction().commit();

            return ride;
        } catch (RideAlreadyExistException e) {
            db.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            db.getTransaction().rollback();
            throw new PersistenceException("Error adding ride: " + e.getMessage(), e);
        }
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

    public void addCitie(String c) {
        if (c != null) {
            List<String> departLocations = getDepartCities();
            departLocations.add(c);
        }
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

    public void addUser(User user) {

        db.getTransaction().begin();
        db.persist(user);
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

    public void takeRide(Ride r, int nP, float p) {

        db.getTransaction().begin();
        Driver driver = db.find(Driver.class, r.getDriver().getEmail());
        driver.addRide(r.getFromLocation(), r.getToLocation(), r.getDate(), nP, p);
        db.persist(r);
        db.getTransaction().commit();

    }

    public User getD(User u) {
        return db.find(User.class, u.getEmail());
    }

    private boolean rideExists(Ride ride) {
        // Método para verificar si un ride ya existe en la base de datos
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

    public void sendMessage(String to, String subject, String message) {

        db.getTransaction().begin();
        db.persist(new Message(to, subject, message));
        db.getTransaction().commit();

    }

    public void cancelAlert(Alerts r) {

        db.getTransaction().begin();
        db.remove(r);
        db.getTransaction().commit();

    }

}
