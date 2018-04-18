package de.btobastian.javacord;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * Implements accessing and saving TimeLog data.
 */
public class Times implements Serializable {

    //              Username        Date  TimeLog
    private HashMap<String, HashMap<String, TimeLog>> log = new HashMap<>();

    private LinkedList<String> keys = new LinkedList<>();
    private LinkedList<HashMap<String, TimeLog>> values = new LinkedList<>();

    private final String KEYSNAME = "data/keys.log";
    private final String VALUESNAME = "data/values.log";

    public Times() {

        try {

            ObjectInputStream readsOne = new ObjectInputStream(new FileInputStream(KEYSNAME));
            //noinspection unchecked
            keys = (LinkedList<String>) readsOne.readObject();
            ObjectInputStream readsTwo = new ObjectInputStream(new FileInputStream(VALUESNAME));
            //noinspection unchecked
            values = (LinkedList<HashMap<String, TimeLog>>) readsTwo.readObject();

            // https://stackoverflow.com/questions/15985266/how-to-iterate-through-two-arraylists-simultaneously
            Iterator<String> keysIter = keys.iterator();
            Iterator<HashMap<String, TimeLog>> valuesIter = values.iterator();
            while (keysIter.hasNext()) {
                log.put(keysIter.next(), valuesIter.next());
            }

        } catch (IOException exc) {
            System.out.println("Times log file not found, or has been corrupted.");
            exc.printStackTrace();
        } catch (ClassNotFoundException exc) {
            System.out.println("times.log file has been corrupted.");
            exc.printStackTrace();
        }

    }

    public Set<String> keySet() {
        return log.keySet();
    }

    public void put(String key, HashMap<String, TimeLog> value) {
        log.put(key, value);
        overwrite();
    }

    public HashMap<String, TimeLog> get(String key) {
        return log.get(key);
    }

    public void overwrite() {

        for (String key : log.keySet()) {
            keys.add(key);
            values.add(log.get(key));
        }


        try {
            ObjectOutputStream writesOne = new ObjectOutputStream(new FileOutputStream(KEYSNAME));
            writesOne.writeObject(keys);
            ObjectOutputStream writesTwo = new ObjectOutputStream(new FileOutputStream(VALUESNAME));
            writesTwo.writeObject(values);
        } catch (IOException exc) {
            System.out.println("times.log not found, or has been corrupted.");
        }
    }

}
