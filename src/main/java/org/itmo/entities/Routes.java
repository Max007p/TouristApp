package org.itmo.entities;

public class Routes {
    private String name;
    private String start;
    private String finish;
    private int duration;

    public Routes(String name, String start, String finish, int duration) {
        this.name = name;
        this.start = start;
        this.finish = finish;
        this.duration = duration;
    }

    public Routes(String start, String finish, int duration) {
        this(null, start, finish, duration);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
