package de.btobastian.javacord;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

import javax.activation.CommandMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Bot for saving mini-crossword times
 */
public class CrosswordBot {

    //              User            Date  TimeLog
    private HashMap<User, HashMap<String, TimeLog>> times = new HashMap<>();

    private String send;
    private String[] userInput;

    private static final String BOT_TOKEN = "NDM1NTYwNjI3NjA0MjkxNTk1.DbbMDg.0uUYi9IFWJhSV3wRpQxWBap6HYk";
    private final String ERROR_TIME = "Proper syntax - \"!time 0:00\"";
    private final String BREAK = "-----";
    private final LinkedList<Command> publicCommands = new LinkedList<>();

    /**
     * Initial code shamelessly stolen from the GitHub docs.
     * @param token the String token used for the bot to connect to Discord.
     */
    private CrosswordBot(String token) {
        // See "How to get the token" below
        DiscordAPI api = Javacord.getApi(token, true);
        // connect
        api.connect(new FutureCallback<DiscordAPI>() {
            @Override
            public void onSuccess(DiscordAPI api) {
                // register listener
                api.registerListener(new MessageCreateListener() {
                    @Override
                    public void onMessageCreate(DiscordAPI api, Message message) {

                        userInput = message.getContent().split(" ");
                        send = "";


                        // Set all commands
                        Command COMMAND_HELP = new Command("!cbhelp", "Get all of the commands from CrosswordBot.", publicCommands);
                        Command COMMAND_PING = new Command("!ping", "Check to see if CrosswordBot is alive.", publicCommands);
                        Command COMMAND_TIME = new Command("!time", "Enter your time for the current puzzle.", publicCommands);
                        Command COMMAND_MTT = new Command("!mytimetoday", "Display your time for the current puzzle.", publicCommands);
                        Command COMMAND_MT = new Command("!mytimes", "Get all of your times.", publicCommands);
                        Command COMMAND_ATT = new Command("!alltimestoday", "Get all of this current puzzle's times.", publicCommands);
                        Command COMMAND_AT = new Command("!alltimes", "Get all of the times ever recorded.", publicCommands);
                        Command COMMAND_BTT = new Command("!besttimetoday", "Display the best time for the current puzzle.", publicCommands);
                        Command COMMAND_BTAT = new Command("!besttimealltime", "Display the best time for any puzzle.", publicCommands);


                        ///// !ping /////
                        if (check(COMMAND_PING)) {
                            add("Yes yes, I'm here.");
                        }


                        ///// TODO: Use message.addCustomEmojiReaction() to use :thanks:
                        ///// TODO: no need for "!time"
                        ///// COMMAND_TIME /////
                        if (check(COMMAND_TIME)) {
                            if (userInput.length == 2) {

                                String[] timeSplit = userInput[1].split(":");
                                if (timeSplit.length == 2 && timeSplit[1].length() == 2) {

                                    try {
                                        int minutes = Integer.parseInt(timeSplit[0]);
                                        int seconds = Integer.parseInt(timeSplit[1]);

                                        // make new times key/value for new user
                                        if (!times.keySet().contains(message.getAuthor())) {
                                            HashMap<String, TimeLog> newList = new HashMap<>();
                                            times.put(message.getAuthor(), newList);
                                        }

                                        //see if time is being overridden
                                        boolean overrideTime = false;
                                        if (times.get(message.getAuthor()).keySet().contains(getDate()))
                                            overrideTime = true;

                                        // add time
                                        times.get(message.getAuthor()).put(getDate(),
                                                new TimeLog(seconds+minutes*60,
                                                getDate()));

                                        // react to !time message
                                        // (Idea from Sarexicus)
                                        message.addUnicodeReaction("\uD83D\uDC4D");

                                        // Confirmation message
                                        if (overrideTime) add("Time overridden for " + message.getAuthor().getName() +
                                                ".");
                                        else add("Time recorded for " + message.getAuthor().getName() + ".");
                                    } catch (NumberFormatException exc) {
                                        add(ERROR_TIME);
                                    }
                                } else add(ERROR_TIME);
                            } else add(ERROR_TIME);
                        }


                        ///// COMMAND_MTT /////
                        if (check(COMMAND_MTT)) {
                            if (times.keySet().contains(message.getAuthor())) {

                                if (times.get(message.getAuthor()).keySet().contains(getDate())) {
                                    add("Time for " + message.getAuthor().getName() + ":");
                                    add(times.get(message.getAuthor()).get(getDate()).toString());
                                } else add("You haven't submitted a time today.");
                            } else add("You haven't submitted any times.");
                        }


                        ///// COMMAND_MT /////
                        if (check(COMMAND_MT)) {
                            if (times.keySet().contains(message.getAuthor())) {

                                add("Times for " + message.getAuthor().getName() + ":");
                                for (String date : times.get(message.getAuthor()).keySet()) {
                                    add(times.get(message.getAuthor()).get(date).toString());
                                }
                            } else add("You haven't submitted any times.");
                        }


                        ///// COMMAND_ATT /////
                        if (check(COMMAND_ATT)) {

                            if (times.keySet().size() > 0) {

                                boolean displayError = true;

                                for (User user : times.keySet()) {
                                    if (times.get(user).keySet().contains(getDate())) {
                                        add(BREAK);
                                        displayError = false;
                                        add("Time for " + user.getName() + ":");
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
                                for (User user : times.keySet()) {

                                    add("Times for " + user.getName() + ":");
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
                                User bestUser = null;
                                int bestTime = -1;

                                for (User user : times.keySet()) {

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
                                    add("Best time today is from " + bestUser.getName() + ":");
                                    add(times.get(bestUser).get(getDate()).toString());
                                }
                            } else add("No times submitted, ever.");
                        }


                        ///// COMMAND_BTAT /////
                        if (check(COMMAND_BTAT)) {

                            User bestUser = null;
                            int bestTime = -1;
                            String bestDate = "";

                            if (times.keySet().size() > 0) {

                                for (User user : times.keySet()) {

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
                                    add("Best time of all time is from " + bestUser.getName() + ":");
                                    add(times.get(bestUser).get(bestDate).toString());
                                } catch (NullPointerException exc) {
                                    System.out.print("Can't fetch properly.");
                                }


                            } else add("No times submitted, ever.");
                        }


                        ///// COMMAND_HELP /////
                        if (check(COMMAND_HELP)) {
                            String pm = "";
                            for (Command command : publicCommands) {
                                pm = pm.concat(command.getName()+" ~ "+command.getInfo()+"\n");
                            }
                            message.getAuthor().sendMessage(pm);
                        }


                        //send out final message
                        if (!send.equals("")) message.reply(send);


                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private boolean check(Command check) {
        try {
            return userInput[0].equalsIgnoreCase(check.getName());
        } catch (IndexOutOfBoundsException exc) {
            System.out.print("input() improperly called (userInput not split).");
            return false;
        }
    }

    private void add(String message) {
        send += message + "\n";
    }

    private String getDate() {
        // https://stackoverflow.com/questions/2942857/how-to-convert-current-date-into-string-in-java
        return new SimpleDateFormat("MM-dd-yyyy").format(new Date());
    }

    public static String formatTime(int seconds) {
        String minutes = String.valueOf((int)Math.floor(seconds / 60));
        String secondsDisplay = String.valueOf(seconds % 60);
        if (secondsDisplay.length() == 1) secondsDisplay = "0".concat(secondsDisplay);
        return minutes.concat(":" + secondsDisplay);
    }

    public static void main(String args[]) {

        CrosswordBot bot = new CrosswordBot(BOT_TOKEN);


    }

}
