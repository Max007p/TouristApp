package org.itmo.entities;

public class CitiesRoutes {
    private String town;
    private String routeName;
    private int sequence;

    public CitiesRoutes(String town, String routeName, int sequence) {
        this.town = town;
        this.routeName = routeName;
        this.sequence = sequence;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
