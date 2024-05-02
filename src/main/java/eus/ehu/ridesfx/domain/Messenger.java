package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

public class Messenger extends User{

    public Messenger(String name, String email) {
        super(email, name);
    }

}
