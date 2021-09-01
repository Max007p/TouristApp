package org.itmo.entities;

public class Bus {
    private int number;
    private int totalRun;
    private String status;
    private String codeModel;

    public Bus(int number, int totalRun, String status, String codeModel) {
        this.number = number;
        this.totalRun = totalRun;
        this.status = status;
        this.codeModel = codeModel;
    }

    public Bus(int totalRun, String status, String codeModel) {
        this(-1, totalRun, status, codeModel);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalRun() {
        return totalRun;
    }

    public void setTotalRun(int totalRun) {
        this.totalRun = totalRun;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCodeModel() {
        return codeModel;
    }

    public void setCodeModel(String codeModel) {
        this.codeModel = codeModel;
    }
}
