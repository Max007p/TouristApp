package org.itmo.entities;

import javafx.scene.control.CheckBox;

import java.sql.Date;

public class Voyage {
    private Date startDate;
    private Date finishDate;
    private int passengers;
    private int ticketCost;
    private String routeName;
    private int busNumber;
    private Boolean doneFlag;
    private Date factFinish;
    private int idWay;

    public Voyage(Date startDate, Date finishDate, int passengers, int ticketCost, String routeName, int busNumber, boolean doneFlag, Date factFinish, int idWay) {
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.passengers = passengers;
        this.ticketCost = ticketCost;
        this.routeName = routeName;
        this.busNumber = busNumber;
        this.doneFlag = doneFlag;
        this.factFinish = factFinish;
        this.idWay = idWay;
    }

    public Voyage(Date startDate, Date finishDate, int passengers, int ticketCost, String routeName, int busNumber, boolean doneFlag, Date factFinish) {
        this(startDate, finishDate, passengers, ticketCost, routeName, busNumber, doneFlag, factFinish, -1);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public int getTicketCost() {
        return ticketCost;
    }

    public void setTicketCost(int ticketCost) {
        this.ticketCost = ticketCost;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public boolean isDoneFlag() {
        return doneFlag;
    }

    public void setDoneFlag(boolean doneFlag) {
        this.doneFlag = doneFlag;
    }

    public Date getFactFinish() {
        return factFinish;
    }

    public void setFactFinish(Date factFinish) {
        this.factFinish = factFinish;
    }

    public int getIdWay() {
        return idWay;
    }

    public void setIdWay(int idWay) {
        this.idWay = idWay;
    }
}
