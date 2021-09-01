package org.itmo.entities;

import java.sql.Date;

public class ModuleInfo {
    private String name;
    private Boolean toilet;
    private String manufacturer;
    private int floors;
    private int seats;
    private Date constructDate;
    private String codeModel;

    public ModuleInfo(String name, Boolean toilet, String manufacturer, int floors, int seats, Date constructDate, String codeModel) {
        this.name = name;
        this.toilet = toilet;
        this.manufacturer = manufacturer;
        this.floors = floors;
        this.seats = seats;
        this.constructDate = constructDate;
        this.codeModel = codeModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getToilet() {
        return toilet;
    }

    public void setToilet(Boolean toilet) {
        this.toilet = toilet;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Date getConstructDate() {
        return constructDate;
    }

    public void setConstructDate(Date constructDate) {
        this.constructDate = constructDate;
    }

    public String getCodeModel() {
        return codeModel;
    }

    public void setCodeModel(String codeModel) {
        this.codeModel = codeModel;
    }
}
