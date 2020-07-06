package FullBloomBot.ZacharyTalis;

import FullBloomBot.ZacharyTalis.entities.message.Message;
import FullBloomBot.ZacharyTalis.listener.message.MessageCreateListener;
import com.google.common.util.concurrent.FutureCallback;

import javax.sound.midi.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Bot for a Full Bloom MIDI experiment.
 */
@SuppressWarnings("SpellCheckingInspection")
public class FullBloomBot {

    // Used for user input parsing/message sending
    private String send;
    private String pm;
    private String[] userInput;

    // Constants
    private static final String BOT_TOKEN = "NzI1NzY5NTYyOTk3MzkxMzkx.XvTk_A.sMIdBTHIKxrR5S7k01DOmTb99I0";
    private final LinkedList<Command> publicCommands = new LinkedList<>();
    private final HashMap<Character, Integer> noteList = new HashMap<>();
    private final HashMap<Character, Integer> noteCount = new HashMap<>();
    private final Integer maxNoteCount = 10;

    /**
     * Constructor/runtime for FullBloomBot.
     * @param token the String token used for the bot to connect to Discord.
     */
    private FullBloomBot(String token) throws MidiUnavailableException {

        ///// Set all commands /////
        final Command COMMAND_HELP = new Command("!ffbhelp",
                "Get all of the commands from FullBloomBot.", publicCommands);
        final Command COMMAND_PING = new Command("!ffbping",
                "Check to see if FullBloomBot is alive.", publicCommands);
        final Command COMMAND_CLEAR = new Command("!ffbclear",
                "Clear notes running.", publicCommands);

        // Used for MIDI functionality
        MidiDevice.Info[] mInfo = MidiSystem.getMidiDeviceInfo();
        int mPort = 4;
        MidiDevice mDevice = MidiSystem.getMidiDevice(mInfo[mPort]);
        mDevice.open();
        final Receiver mReceiver = mDevice.getReceiver();

        //<editor-fold desc="Set all notes in noteList">
        noteList.put('a',44);
        noteList.put('b',45);
        noteList.put('c',47);
        noteList.put('d',49);
        noteList.put('e',50);
        noteList.put('f',52);
        noteList.put('g',54);
        noteList.put('h',56);
        noteList.put('i',57);
        noteList.put('j',59);
        noteList.put('k',61);
        noteList.put('l',62);
        noteList.put('m',64);
        noteList.put('n',66);
        noteList.put('o',68);
        noteList.put('p',69);
        noteList.put('q',71);
        noteList.put('r',73);
        noteList.put('s',74);
        noteList.put('t',76);
        noteList.put('u',78);
        noteList.put('v',80);
        noteList.put('w',81);
        noteList.put('x',83);
        noteList.put('y',85);
        noteList.put('z',86);
        //</editor-fold>

        //<editor-fold desc="Set all notes in noteList">
        noteCount.put('a',0);
        noteCount.put('b',0);
        noteCount.put('c',0);
        noteCount.put('d',0);
        noteCount.put('e',0);
        noteCount.put('f',0);
        noteCount.put('g',0);
        noteCount.put('h',0);
        noteCount.put('i',0);
        noteCount.put('j',0);
        noteCount.put('k',0);
        noteCount.put('l',0);
        noteCount.put('m',0);
        noteCount.put('n',0);
        noteCount.put('o',0);
        noteCount.put('p',0);
        noteCount.put('q',0);
        noteCount.put('r',0);
        noteCount.put('s',0);
        noteCount.put('t',0);
        noteCount.put('u',0);
        noteCount.put('v',0);
        noteCount.put('w',0);
        noteCount.put('x',0);
        noteCount.put('y',0);
        noteCount.put('z',0);
        //</editor-fold>

        // Token is provided by the Discord bot page
        DiscordAPI api = Javacord.getApi(token, true);

        // Begin the connect
        api.connect(new FutureCallback<DiscordAPI>() {
            @Override
            public void onSuccess(DiscordAPI api) {
                // register listener
                api.registerListener(new MessageCreateListener() {
                    @Override
                    public void onMessageCreate (DiscordAPI api, Message message) throws InvalidMidiDataException {


                        // Get message input, and refresh send and pm
                        String mInput = message.getContent().toLowerCase();
                        userInput = mInput.split(" ");
                        send = "";
                        pm = "";


                        // Message input interpretation
                        int i = 0;
                        boolean canPlay = false;
                        if (mInput.charAt(0) != '!'){
                            while (i < mInput.length()) {
                                char mChar = mInput.charAt(i);
                                if (noteList.containsKey(mChar)) {
                                    canPlay = true;
                                    break;
                                }
                                i++;
                            }
                            if (canPlay) {
                                message.addUnicodeReaction("\uD83C\uDFB5");
                                // Clear out all existing notes
                                for (char cChar : noteList.keySet()) {
                                    ShortMessage mMessage = new ShortMessage();
                                    mMessage.setMessage(128, 1, noteList.get(cChar), 100);
                                    mReceiver.send(mMessage, 0);
                                    noteCount.put(cChar, 0);
                                }
                                i = 0;
                                // Play new notes
                                while (i < mInput.length()) {
                                    char mChar = mInput.charAt(i);
                                    if (noteList.containsKey(mChar) && noteCount.get(mChar) <= maxNoteCount) {
                                        ShortMessage mMessage = new ShortMessage();
                                        mMessage.setMessage(144, 1, noteList.get(mChar), 10);
                                        mReceiver.send(mMessage, 0);
                                        noteCount.put(mChar, noteCount.get(mChar) + 1);
                                    }
                                    i++;
                                }
                            }
                        }


                        ///// COMMAND_HELP /////
                        if (check(COMMAND_HELP)) {
                            for (Command command : publicCommands) {
                                pm = pm.concat(command.getName()+" ~ "+command.getInfo()+"\n");
                            }
                        }


                        ///// COMMAND_PING /////
                        if (check(COMMAND_PING)) {
                            pingMessage();
                        }


                        // Clear out all existing notes
                        if (check(COMMAND_CLEAR)) {
                            for (char cChar : noteList.keySet()) {
                                ShortMessage mMessage = new ShortMessage();
                                mMessage.setMessage(128, 1, noteList.get(cChar), 100);
                                mReceiver.send(mMessage, 0);
                            }
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
     */
    private void pingMessage() {
        send += "Yes yes, I'm here." + "\n";
    }

    /**
     * Get the bot up and running.
     * @param args the executable arguments.
     */
    public static void main(String[] args) throws MidiUnavailableException {

        // Create the bot
        @SuppressWarnings("unused") FullBloomBot bot = new FullBloomBot(BOT_TOKEN);

    }

}
