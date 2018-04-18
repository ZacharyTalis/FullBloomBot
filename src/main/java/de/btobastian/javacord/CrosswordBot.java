package de.btobastian.javacord;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Bot for saving Mini Crossword times.
 */
public class CrosswordBot {

    private Times times = new Times();

    private String send;
    private String pm;
    private String[] userInput;

    private static final String BOT_TOKEN = "NDM1NTYwNjI3NjA0MjkxNTk1.DbbMDg.0uUYi9IFWJhSV3wRpQxWBap6HYk";
    private final String ERROR_TIME = "Proper syntax - \"M:SS\"";
    private final String BREAK = "-----";
    private final LinkedList<Command> publicCommands = new LinkedList<>();

    /**
     * Initial code shamelessly stolen from the GitHub docs.
     * @param token the String token used for the bot to connect to Discord.
     */
    private CrosswordBot(String token) {

        ///// Set all commands /////
        final Command COMMAND_HELP = new Command("!cbhelp", "Get all of the commands from CrosswordBot.",
                publicCommands);
        final Command COMMAND_PING = new Command("!ping", "Check to see if CrosswordBot is alive.", publicCommands);
        final Command COMMAND_TIME = new Command("!time", "Enter your time for the current puzzle.", publicCommands);
        final Command COMMAND_MTT = new Command("!mytimetoday", "Display your time for the current puzzle.",
                publicCommands);
        final Command COMMAND_MT = new Command("!mytimes", "Get all of your times.", publicCommands);
        final Command COMMAND_MBT = new Command("!mybesttime", "Display your best time ever.", publicCommands);
        final Command COMMAND_ATT = new Command("!alltimestoday", "Get all of this current puzzle's times.",
                publicCommands);
        final Command COMMAND_AT = new Command("!alltimes", "Get all of the times ever recorded.", publicCommands);
        final Command COMMAND_BTT = new Command("!besttimetoday", "Display the best time for the current puzzle.",
                publicCommands);
        final Command COMMAND_BTAT = new Command("!besttimealltime", "Display the best time for any puzzle.",
                publicCommands);

        // Token is provided by the Discord bot page
        DiscordAPI api = Javacord.getApi(token, true);

        // Begin the connect
        api.connect(new FutureCallback<DiscordAPI>() {
            @Override
            public void onSuccess(DiscordAPI api) {
                // register listener
                api.registerListener(new MessageCreateListener() {
                    @Override
                    public void onMessageCreate (DiscordAPI api, Message message) {


                        // Get message input, and refresh send and pm
                        userInput = message.getContent().split(" ");
                        send = "";
                        pm = "";


                        ///// COMMAND_HELP /////
                        if (check(COMMAND_HELP)) {
                            for (Command command : publicCommands) {
                                //pm = pm.concat(command.getName()+" ~ "+command.getInfo()+"\n");
                                pm = pm.concat(command.getName() + "\n");
                            }
                            message.getAuthor().sendMessage(pm);
                        }


                        ///// !ping /////
                        if (check(COMMAND_PING)) {
                            add("Yes yes, I'm here.");
                        }


                        ///// TODO: Use message.addCustomEmojiReaction() to use :thanks:
                        ///// COMMAND_TIME /////
                        if (check(COMMAND_TIME) || userInput[0].split(":").length == 2) {
                            if ((check(COMMAND_TIME) && userInput.length == 2) ||
                                    (userInput[0].split(":").length == 2)) {

                                String[] timeSplit;

                                if (check(COMMAND_TIME)) {
                                    timeSplit = userInput[1].split(":");
                                } else {
                                    timeSplit = userInput[0].split(":");
                                }

                                if (timeSplit.length == 2 && timeSplit[1].length() == 2) {

                                    try {
                                        int minutes = Integer.parseInt(timeSplit[0]);
                                        int seconds = Integer.parseInt(timeSplit[1]);

                                        // make new times key/value for new user
                                        if (!times.keySet().contains(message.getAuthor().getName())) {
                                            @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
                                            HashMap<String, TimeLog> newList = new HashMap<>();
                                            times.put(message.getAuthor().getName(), newList);
                                            times.overwrite();
                                        }

                                        //see if time is being overridden
                                        boolean overrideTime = false;
                                        if (times.get(message.getAuthor().getName()).keySet().contains(getDate()))
                                            overrideTime = true;

                                        // add time
                                        times.get(message.getAuthor().getName()).put(getDate(),
                                                new TimeLog(seconds + minutes * 60,
                                                        getDate()));
                                        times.overwrite();

                                        // react to !time message
                                        // (Idea from Sarexicus)
                                        message.addUnicodeReaction("\uD83D\uDC4D");

                                        // Confirmation message
                                        if (overrideTime) add("Time overridden for " + message.getAuthor().getName() +
                                                ".");
                                        else {
                                            System.out.println("Time recorded for " + message.getAuthor().getName() +
                                                    ".");
                                            // Uncomment if you'd like to have an initial confirmation message
                                            // add("Time recorded for " + message.getAuthor().getName() + ".");
                                        }
                                    } catch (NumberFormatException exc) {
                                        add(ERROR_TIME);
                                    }
                                } else add(ERROR_TIME);
                            } else add(ERROR_TIME);
                        }


                        ///// COMMAND_MTT /////
                        if (check(COMMAND_MTT)) {
                            if (times.keySet().contains(message.getAuthor().getName())) {

                                if (times.get(message.getAuthor().getName()).keySet().contains(getDate())) {
                                    add("Time for " + message.getAuthor().getName() + ":");
                                    add(times.get(message.getAuthor().getName()).get(getDate()).toString());
                                } else add("You haven't submitted a time today.");
                            } else add("You haven't submitted any times.");
                        }


                        ///// COMMAND_MT /////
                        if (check(COMMAND_MT)) {
                            if (times.keySet().contains(message.getAuthor().getName())) {

                                add("Times for " + message.getAuthor().getName() + ":");
                                for (String date : times.get(message.getAuthor().getName()).keySet()) {
                                    add(times.get(message.getAuthor().getName()).get(date).toString());
                                }
                            } else add("You haven't submitted any times.");
                        }

                        ///// COMMAND_MBT /////
                        if (check(COMMAND_MBT)) {
                            if (times.keySet().contains(message.getAuthor().getName())) {

                                int bestTime = -1;
                                String bestDate = "";

                                for (String date : times.get(message.getAuthor().getName()).keySet()) {
                                    if (bestTime == -1 || bestTime > times.get(message.getAuthor().getName()).get
                                            (date).getTime()) {
                                        bestTime = times.get(message.getAuthor().getName()).get(date).getTime();
                                        bestDate = date;
                                    }
                                }

                                try {
                                    add("Best time for " + message.getAuthor().getName() + " is:");
                                    add(times.get(message.getAuthor().getName()).get(bestDate).toString());
                                } catch (NullPointerException exc) {
                                    System.out.print("Can't fetch properly.");
                                }

                            } else add("You haven't submitted any times.");
                        }


                        ///// COMMAND_ATT /////
                        if (check(COMMAND_ATT)) {

                            if (times.keySet().size() > 0) {

                                boolean displayError = true;

                                for (String user : times.keySet()) {
                                    if (times.get(user).keySet().contains(getDate())) {
                                        add(BREAK);
                                        displayError = false;
                                        add("Time for " + user + ":");
                                        add(times.get(user).get(getDate()).toString());
                                    }
                                }
                                if (displayError) add("No times submitted for today.");
                                else add(BREAK);
                            } else add("No times submitted, ever.");
                        }


                        ///// COMMAND_AT /////
                        if (check(COMMAND_AT)) {

                            if (times.keySet().size() > 0) {

                                add(BREAK);
                                for (String user : times.keySet()) {

                                    add("Times for " + user + ":");
                                    for (String date : times.get(user).keySet()) {
                                        add(times.get(user).get(date).toString());
                                    }
                                    add(BREAK);
                                }
                            } else add("No times submitted, ever.");
                        }


                        ///// COMMAND_BTT /////
                        if (check(COMMAND_BTT)) {

                            if (times.keySet().size() > 0) {

                                boolean displayError = true;
                                String bestUser = null;
                                int bestTime = -1;

                                for (String user : times.keySet()) {

                                    if (times.get(user).keySet().contains(getDate())) {
                                        if (bestTime == -1 || bestTime > times.get(user).get(getDate()).getTime()) {
                                            displayError = false;
                                            bestTime = times.get(user).get(getDate()).getTime();
                                            bestUser = user;
                                        }
                                    }

                                }
                                if (displayError) add("No times submitted for today.");
                                else {
                                    add("Best time today is from " + bestUser + ":");
                                    add(times.get(bestUser).get(getDate()).toString());
                                }
                            } else add("No times submitted, ever.");
                        }


                        ///// COMMAND_BTAT /////
                        if (check(COMMAND_BTAT)) {

                            String bestUser = null;
                            int bestTime = -1;
                            String bestDate = "";

                            if (times.keySet().size() > 0) {

                                for (String user : times.keySet()) {

                                    for (String date : times.get(user).keySet()) {
                                        if (bestTime == -1 || bestTime > times.get(user).get(date).getTime()) {
                                            bestTime = times.get(user).get(date).getTime();
                                            bestUser = user;
                                            bestDate = date;
                                        }
                                    }
                                }

                                try {
                                    assert bestUser != null;
                                    add("Best time of all time is from " + bestUser + ":");
                                    add(times.get(bestUser).get(bestDate).toString());
                                } catch (NullPointerException exc) {
                                    System.out.print("Can't fetch properly.");
                                }

                            } else add("No times submitted, ever.");
                        }


                        // Send out final message and/or PM
                        if (!send.equals("")) message.reply(send);
                        if (!pm.equals("")) message.getAuthor().sendMessage(pm);


                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Check to see if the first word in user input is for the given command.
     * @param check the command to check for.
     * @return whether or not the user input is what you were checking for.
     */
    private boolean check(Command check) {
        try {
            return userInput[0].equalsIgnoreCase(check.getName());
        } catch (IndexOutOfBoundsException exc) {
            System.out.print("input() improperly called (userInput not split).");
            return false;
        }
    }

    /**
     * Add a string message to the reply (followed by a newline).
     * @param message the message to add onto the reply.
     */
    private void add(String message) {
        send += message + "\n";
    }

    /**
     * Get the date (used as a key for the nested HashMap in times).
     * @return the date.
     */
    private String getDate() {
        // https://stackoverflow.com/questions/2942857/how-to-convert-current-date-into-string-in-java
        return new SimpleDateFormat("MM-dd-yyyy").format(new Date());
    }

    /**
     * Get the bot up and running.
     * @param args the executable arguments.
     */
    public static void main(String args[]) {

        // Create the bot
        CrosswordBot bot = new CrosswordBot(BOT_TOKEN);


    }

}
