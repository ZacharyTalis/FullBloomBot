package de.btobastian.javacord;

import java.io.Serializable;

/**
 * Holds a single time/date log for a solve.
 */
public class TimeLog implements Serializable {

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
        return "Date: " + getDate() + "  |  Time: " + String.valueOf(formatTime(getTime()));
    }

    /**
     * Return a time in seconds as a properly-formatted time.
     * @param seconds the int time in seconds.
     * @return the String time (0:00).
     */
    public static String formatTime(int seconds) {
        String minutes = String.valueOf((int)Math.floor(seconds / 60));
        String secondsDisplay = String.valueOf(seconds % 60);
        if (secondsDisplay.length() == 1) secondsDisplay = "0".concat(secondsDisplay);
        return minutes.concat(":" + secondsDisplay);
    }

}