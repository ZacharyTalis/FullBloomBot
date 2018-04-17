package de.btobastian.javacord;

public class TimeLog {

    private int time;
    private String date;

    public TimeLog(int time, String date) {
        this.time = time;
        this.date = date;
    }

    public int getTime() { return time; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return "Date: " + getDate() + "  |  Time: " + String.valueOf(CrosswordBot.formatTime(getTime()));
    }

}