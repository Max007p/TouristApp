package org.itmo.entities;

import java.time.Year;
import java.util.List;

public class Staff {
    private int tableNumber;
    private int experience = 0;
    private String category;
    private String mobileNumber;
    private int birthYear;
    private String fullName;
    private String role;
    private String username;

    public Staff(int tableNumber, int experience, String category, String mobileNumber, int birthYear, String fullName, String role, String username) {
        this.tableNumber = tableNumber;
        this.experience = experience;
        this.category = category;
        this.mobileNumber = mobileNumber;
        this.birthYear = birthYear;
        this.fullName = fullName;
        this.role = role;
        this.username = username;
    }

    public Staff(Staff user) {
        this(user.getTableNumber(), user.getExperience(), user.getCategory(), user.getMobileNumber(), user.getBirthYear(), user.getFullName(), user.getRole(), user.getUsername());
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
