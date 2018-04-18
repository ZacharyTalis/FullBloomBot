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

    private final String KEYSPATH = "data/keys.log";
    private final String VALUESPATH = "data/values.log";

    public Times() {

        try {

            ObjectInputStream readsOne = new ObjectInputStream(new FileInputStream(KEYSPATH));
            //noinspection unchecked
            keys = (LinkedList<String>) readsOne.readObject();
            ObjectInputStream readsTwo = new ObjectInputStream(new FileInputStream(VALUESPATH));
            //noinspection unchecked
            values = (LinkedList<HashMap<String, TimeLog>>) readsTwo.readObject();

            // https://stackoverflow.com/questions/15985266/how-to-iterate-through-two-arraylists-simultaneously
            Iterator<String> keysIter = keys.iterator();
            Iterator<HashMap<String, TimeLog>> valuesIter = values.iterator();
            while (keysIter.hasNext()) {
                log.put(keysIter.next(), valuesIter.next());
            }

        } catch (IOException exc) {
            System.out.println("info.log and/or keys.log not found, or have been corrupted.");
            exc.printStackTrace();
        } catch (ClassNotFoundException exc) {
            System.out.println("info.log and/or keys.log have been corrupted.");
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
            ObjectOutputStream writesOne = new ObjectOutputStream(new FileOutputStream(KEYSPATH));
            writesOne.writeObject(keys);
            ObjectOutputStream writesTwo = new ObjectOutputStream(new FileOutputStream(VALUESPATH));
            writesTwo.writeObject(values);
        } catch (IOException exc) {
            System.out.println("times.log not found, or has been corrupted.");
        }
    }

}
