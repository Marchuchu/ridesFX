package eus.ehu.ridesfx.domain;

import jakarta.persistence.Id;

public class Traveler extends Messenger{

    private String password;

    @Id
    private String email;

    public Traveler(String name, String password){
        super(name);
        this.password = password;
    }

    public Traveler(){

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

        public String toString() {
        return getName();
    }



}
