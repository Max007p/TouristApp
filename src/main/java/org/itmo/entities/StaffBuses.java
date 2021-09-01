package org.itmo.entities;

public class StaffBuses {
    private int tableNumber;
    private int busNumber;
    private int shift;

    public StaffBuses(int tableNumber, int busNumber, int shift) {
        this.tableNumber = tableNumber;
        this.busNumber = busNumber;
        this.shift = shift;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }
}
