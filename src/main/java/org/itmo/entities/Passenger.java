package org.itmo.entities;

public class Passenger {
    private String phoneNumber;
    private String email;
    private String name;
    private String username;
    private String passport;

    public Passenger(String phoneNumber, String email, String name, String username, String passport) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.name = name;
        this.username = username;
        this.passport = passport;
    }

    public Passenger(Passenger user) {
        this(user.getPhoneNumber(), user.getEmail(), user.getName(), user.getUsername(), user.getPassport());
    }

    public Passenger(User user) {
        this(null, null, null, user.getUsername(), null);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
}
