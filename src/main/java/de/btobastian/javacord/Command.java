package de.btobastian.javacord;

import java.util.LinkedList;

public class Command {

    private String name;
    private String info;

    Command(String name, String info, LinkedList<Command> commands) {
        this.name = name;
        this.info = info;
        commands.add(this);
    }

    public String getName() { return name; }
    public String getInfo() { return info; }

}
