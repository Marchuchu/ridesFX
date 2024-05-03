package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

public class Messenger extends User{

    public Messenger(String email, String name) {
        super(email, name);
    }

}
