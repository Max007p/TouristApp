package org.itmo.entities;

public class PaidPassengers {
    private int id;
    private String passengerUser;
    private int idWay;
    private Boolean isPayed = false;
    private int tableNumber;
    private String customerName;
    private String passport;

    public PaidPassengers(int id, String passengerUser, int idWay, Boolean isPayed, int tableNumber, String customerName, String passport) {
        this.id = id;
        this.passengerUser = passengerUser;
        this.idWay = idWay;
        this.isPayed = isPayed;
        this.tableNumber = tableNumber;
        this.customerName = customerName;
        this.passport = passport;
    }

    public PaidPassengers(String passengerUser, int idWay, Boolean isPayed, int tableNumber, String customerName, String passport) {
        this(-1, passengerUser, idWay, isPayed, tableNumber, customerName, passport);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassengerUser() {
        return passengerUser;
    }

    public void setPassengerUser(String passengerUser) {
        this.passengerUser = passengerUser;
    }

    public int getIdWay() {
        return idWay;
    }

    public void setIdWay(int idWay) {
        this.idWay = idWay;
    }

    public Boolean getPayed() {
        return isPayed;
    }

    public void setPayed(Boolean payed) {
        isPayed = payed;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setPasspoer(String passport) {this.passport = passport; }

    public String getPassport() { return passport; }
}
