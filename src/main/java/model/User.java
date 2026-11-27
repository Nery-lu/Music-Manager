package model;

import java.util.*;

public class User {

    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private transient Queue<Music> queue;

    public User(String email, String passwordHash, String name) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.queue = new ArrayDeque<>();
    }

    public User(String email, String passwordHash,String name, int id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.queue = new ArrayDeque<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public Queue<Music> getQueue() {
        return this.queue;
    }


}
