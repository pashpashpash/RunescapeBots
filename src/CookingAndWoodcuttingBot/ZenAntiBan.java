package CookingAndWoodcuttingBot;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.Menu;
import org.dreambot.api.wrappers.widgets.message.Message;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class holds all of my anti-ban ideas.
 *
 * @author Zenarchist
 */
public class ZenAntiBan {
    public int ANTIBAN_RATE = 65; // This is the frequency rate for anti-ban actions (in % terms - 100% = frequent, 0% = never
    public int MIN_WAIT_NO_ACTION = 50; // This is the minimum time to wait if no action was taken
    public int MAX_WAIT_NO_ACTION = 100; // This is the maximum time to wait if no action was taken
    private AbstractScript s; // Script
    private String STATUS = "Idling"; // Current anti-ban status
    private Skill[] STATS_TO_CHECK = { Skill.HITPOINTS, Skill.WOODCUTTING, Skill.FISHING, Skill.MAGIC }; // This is used for determining which stats to randomly check
    public int MIN_WAIT_BETWEEN_EVENTS = 10; // In seconds
    private long LAST_EVENT = 0L; // Last time an antiban event was triggered
    private long LAST_IDLE; // Last time we idled for a while
    private boolean DO_RANDOM = false; // This is a generic flag for randomly doing something early in a script for anti-patterning
    private int MAX_RUNTIME_MINUTES = -1; // This is the maximum amount of time the script should run for (used for calculating progressive lag multiplier + max duration)
    private long START_TIME = 0L; // Time the script was started
    // Stat widget coordinates (in order of DB API listing for Skill array)
    public final Point[] STAT_WIDGET = {
            new Point(550, 210), // Attack
            new Point(550, 270), // Defence
            new Point(550, 240), // Strength
            new Point(612, 210), // Hits
            new Point(550, 304), // Ranged
            new Point(550, 336), // Prayer
            new Point(350, 370), // Magic
            new Point(367, 304), // Cooking
            new Point(676, 368), // Woodcut
            new Point(613, 369), // Fletching
            new Point(677, 273), // Fishing
            new Point(676, 336), // Firemaking
            new Point(614, 337), // Crafting
            new Point(677, 240), // Smithing
            new Point(677, 209), // Mining
            new Point(613, 271), // Herblore
            new Point(614, 240), // Agility
            new Point(614, 304), // Thieving
            new Point(614, 401), // Slayer
            new Point(676, 400), // Farming
            new Point(550, 400), // Runecrafting
            new Point(613, 432), // Hunter
            new Point(550, 432), // Construction
    };
    private final Point STATS_WIDGET = new Point(577, 186); // Stats menu
    private final Point INVENTORY_WIDGET = new Point(643, 185); // Inventory menu
    private final Point COMBAT_WIDGET = new Point(543, 186); // Combat style menu
    private final Point MAGIC_WIDGET = new Point(742, 186); // Magic menu
    // Chatbot variables
    private boolean USE_CHATBOT = false; // Whether or not to use the ChatBot
    private int CHATBOT_RATE_MINS = 5; // Minimum amount of minutes between chatbot replies
    private String CHATBOT_STATE = "x"; // This is used for continuing conversations (not that it matters - chatbot is dumb as hell)
    private long LAST_CHATBOT_REPLY = 0L; // Last time the chatbot replied
    private int CHATBOT_REPLIES = 0; // How many times the chatbot has replied to player messages
    private final String VALID_CHARACTERS = "abcdefghijklmnopqrstuvwxyz"; // Valid characters for chatbot
    private long LAST_PERSONAL_REPLY = 0L; // Last time we replied to a 'bot' flagged message or a message addressed to us
    private String CHATBOT_KEY = "xxx"; // CleverBot API key

    // Constructs a new Anti-Ban class with the given script
    public ZenAntiBan(AbstractScript script) {
        this.s = script;
        // Set last idle to now to avoid idling early into the script
        LAST_IDLE = System.currentTimeMillis();
        START_TIME = System.currentTimeMillis();
    }

    // Returns the wait time for when the antiban system does nothing
    private int doNothing() {
        return rh(MIN_WAIT_NO_ACTION, MAX_WAIT_NO_ACTION);
    }

    // Sets the stats to check during random antiban events
    public void setStatsToCheck(Skill... skills) {
        STATS_TO_CHECK = skills;
    }

    // Returns the sleep time after performing an anti-ban check
    public int antiBan() {
        setStatus("");
        if(ANTIBAN_RATE == 0 || System.currentTimeMillis() - LAST_EVENT <= r(MIN_WAIT_BETWEEN_EVENTS * 1000, MIN_WAIT_BETWEEN_EVENTS * 2000))
            return doNothing();

        // If we have moved the mouse outside of the screen, wait a moment before performing the ban action
        if(s.getMouse().getX() == -1 && s.getMouse().getY() == -1)
            s.sleep(1000, 2000);

        // Calculate overall random anti-ban intervention rate (%)
        int rp = r(0, 100);
        if(rp < ANTIBAN_RATE) {
            // Calculate event-specific activation rate (%)
            rp = r(0, 100);
            // Calculate event ID
            int event = r(0, 14);
            // Handle specified event
            switch(event) {
                case 0: { // Examine random entity
                    if(rp < 25) { // 25% chance
                        int r = r(1, 3);
                        Entity e = s.getGameObjects().closest(o -> o != null && !o.getName().equals("null") && r(1, 2) != 1);
                        if (e == null || r == 2) {
                            e = s.getNpcs().closest(n -> n != null && !n.getName().equals("null"));
                            if (e == null || r == 3) {
                                e = s.getGroundItems().closest(i -> i != null && !i.getName().equals("null"));
                                if (e == null)
                                    return doNothing();
                            }
                        }

                        setStatus("Examining entity (" + e.getName() + ")");
                        s.getMouse().move(e);

                        if(r(0, 100) < 99) { // 99% chance of clicking examine
                            // Open right-click menu and find Examine option
                            Menu menu = new Menu(s.getClient());
                            rh(1, 100);
                            menu.open();
                            s.sleep(rh(250, 1000));
                            if (menu.contains("Examine"))
                                menu.clickAction("Examine", e);
                            else
                            if(menu.contains("Cancel"))
                                menu.clickAction("Cancel");
                        }

                        LAST_EVENT = System.currentTimeMillis();
                        return rh(250, 3000);
                    }
                }
                case 1: { // Check random stat
                    if(rp < 10) { // 10% chance
                        if (s.getTabs().getOpen() != Tab.STATS)
                            openStats();
                        int x = r(0, 25);
                        int y = r(0, 15);
                        int skill = -1;
                        long t = System.currentTimeMillis();
                        while (skill == -1 && System.currentTimeMillis() - t <= 500) {
                            int r = r(0, Skill.values().length - 1);
                            for (Skill s : STATS_TO_CHECK) {
                                if (s.getName().equals(Skill.values()[r].getName()))
                                    skill = r;
                            }
                        }

                        setStatus("Checking EXP (" + Skill.values()[skill].getName() + ")");
                        Point p = STAT_WIDGET[skill];
                        p.setLocation(p.getX() + x, p.getY() + y);
                        s.getMouse().move(p);

                        LAST_EVENT = System.currentTimeMillis();
                        return rh(2000, 5000);
                    }
                }
                case 2: { // Type something random
                    if(rp < 1) { // 1% chance
                        String str = "";
                        int r = r(1, 2);
                        for(int i = 0; i < r; i++)
                            str += VALID_CHARACTERS.charAt(r(0, VALID_CHARACTERS.length()));
                        setStatus("Typing random keys (" + str + ")");
                        s.getKeyboard().type(str);
                        LAST_EVENT = System.currentTimeMillis();
                        return rh(500, 3000);
                    }
                }
                case 3: { // Move mouse to random location (and sometimes click)
                    if(rp < 10) { // 10% chance
                        int r = r(0, 100);
                        int x = r(0, 760);
                        int y = r(0,500);
                        setStatus("Moving mouse (" + x + "," + y + ")");
                        s.getMouse().move(new Point(x, y));
                        if(r < 5) // 10% chance of right-clicking
                            s.getMouse().click(true);
                        else
                        if(r < 5) // 5% chance of left-clicking
                            s.getMouse().click();

                        LAST_EVENT = System.currentTimeMillis();
                        return rh(500, 3000);
                    }
                }
                case 4: { // Walk to random location
                    if(rp < 1) { // 1% chance
                        int x = s.getLocalPlayer().getX() - 15;
                        int y = s.getLocalPlayer().getY() - 15;
                        int x2 = r(0, 30);
                        int y2 = r(0, 30);
                        Area a = new Area(x, y, x2, y2);
                        Tile t = a.getRandomTile();
                        setStatus("Walking to random tile (" + t.getX() + "," + t.getY() + ")");
                        s.getWalking().walk(t);
                        LAST_EVENT = System.currentTimeMillis();
                        return rh(500, 3000);
                    }
                }
                case 5: { // Chop random tree
                    if(rp < 1) { // 1% chance
                        GameObject obj = s.getGameObjects().closest(o -> o != null && !o.getName().equals("null") && r(1, 2) != 1 && o.hasAction("Chop down") && s.getLocalPlayer().distance(o) < 5);
                        if(obj == null)
                            return doNothing();

                        setStatus("Chopping random tree (" + obj.getName() + ")");
                        if(r(1, 2) != 1)
                            obj.interact("Chop down");
                        else
                            obj.interactForceLeft("Chop down");

                        LAST_EVENT = System.currentTimeMillis();
                        return rh(500, 3000);
                    }
                }
                case 6: { // Click random entity
                    if(rp < 1) { // 1% chance
                        Entity e = s.getGameObjects().closest(o -> o != null && !o.getName().equals("null")  && r(1, 2) != 1 && s.getLocalPlayer().distance(o) < 5);
                        int r = r(1, 3);
                        if(e == null || r == 2) {
                            e = s.getNpcs().closest(n -> n != null && !n.getName().equals("null"));
                            if(e == null || r == 3) {
                                e = s.getGroundItems().closest(i -> i != null && !i.getName().equals("null"));
                                if(e == null)
                                    return doNothing();
                            }
                        }

                        if(e instanceof NPC && s.getCombat().isInMultiCombat())
                            break;

                        setStatus("Clicking random entity (" + e.getName() + ")");
                        s.getMouse().move(e);
                        s.sleep(rh(0, 50));
                        if(r(0, 100) < 25)
                            s.getMouse().click(true);
                        else
                            s.getMouse().click();

                        LAST_EVENT = System.currentTimeMillis();
                        return rh(500, 3000);
                    }
                }
                case 7: { // Just idle for a while
                    if(rp < 5) { // 5% chance
                        if (System.currentTimeMillis() - LAST_IDLE >= 300000) { // Only allow idling to occur every 5+ minutes
                            int idle = r(60000, 120000);
                            setStatus("Idling for " + (idle / 1000) + " seconds");
                            if (r(0, 100) < 99)
                                s.getMouse().moveMouseOutsideScreen();
                            // Disable dismiss & autologin solvers temporarily
                            s.getRandomManager().disableSolver(RandomEvent.LOGIN);
                            s.getRandomManager().disableSolver(RandomEvent.DISMISS);
                            // Sleep for the calculated time
                            s.sleep(idle);
                            // Enable dismiss & autologin solvers and resume script as normal
                            s.getRandomManager().enableSolver(RandomEvent.LOGIN);
                            s.getRandomManager().enableSolver(RandomEvent.DISMISS);
                            LAST_IDLE = System.currentTimeMillis();
                            return 1;
                        }
                    }
                }
                case 8: { // Open inventory or stats
                    if(rp < 25) { // 25% chance
                        if(s.getTabs().getOpen() != Tab.INVENTORY && s.getInventory().getEmptySlots() > 0)
                            setStatus("Opening inventory");
                        if (openInventory()) {
                            LAST_EVENT = System.currentTimeMillis();
                            s.sleep(50, 100);
                            s.getMouse().moveMouseOutsideScreen();
                            return rh(500, 1000);
                        }
                    } else
                    if(rp > 75) { // 25% chance
                        if(s.getTabs().getOpen() != Tab.STATS)
                            setStatus("Opening stats");
                        if(openStats()) {
                            LAST_EVENT = System.currentTimeMillis();
                            s.sleep(50, 100);
                            s.getMouse().moveMouseOutsideScreen();
                            return rh(500, 1000);
                        }
                    }
                }
                case 9: { // Open combat menu (only if we are training melee stats)
                    boolean meleeStats = false;
                    for(Skill s : STATS_TO_CHECK)
                        if(s.equals(Skill.ATTACK) || s.equals(Skill.STRENGTH) || s.equals(Skill.DEFENCE))
                            meleeStats = true;

                    if(!meleeStats)
                        break;

                    if(rp < 5) { // 5% chance
                        if (s.getTabs().getOpen() != Tab.COMBAT)
                            setStatus("Opening combat menu");
                        openCombat();
                        s.sleep(50, 100);
                        s.getMouse().moveMouseOutsideScreen();
                        LAST_EVENT = System.currentTimeMillis();
                        return rh(500, 1000);
                    }
                }
                case 10: { // Moving mouse off-screen for a moment
                    if(rp < 50) { // 50% chance
                        if(s.getMouse().getX() == -1 && s.getMouse().getY() == -1)
                            return doNothing();

                        setStatus("Moving mouse off-screen");
                        s.getMouse().moveMouseOutsideScreen();
                        LAST_EVENT = System.currentTimeMillis();
                        return rh(5000, 8000);
                    }
                }
                case 11: { // Open magic menu
                    if(rp < 1) { // 1% chance
                        if(s.getTabs().getOpen() != Tab.MAGIC) {
                            setStatus("Opening magic menu");
                            openMagic();
                            s.sleep(50, 100);
                            s.getMouse().moveMouseOutsideScreen();
                        }
                    }
                }
                case 12: { // Examine random inventory item
                    if(rp < 1) { // 1% chance
                        if (openInventory())
                            s.sleep(10, 250);
                        for(Item i : s.getInventory().all(it -> it != null)) {
                            if(i != null && r(1, 3) == 2) {
                                setStatus("Examining item (" + i.getName() + ")");
                                s.getMouse().move(i.getDestination());
                                s.sleep(rh(0, 50));
                                // Open right-click menu and find Examine option
                                Menu menu = new Menu(s.getClient());
                                menu.open();
                                s.sleep(rh(250, 1000));
                                if (menu.contains("Examine"))
                                    menu.clickAction("Examine");
                                else
                                if(menu.contains("Cancel"))
                                    menu.clickAction("Cancel");

                                break;
                            }
                        }
                    }
                }
                case 13: { // Move camera randomly
                    if(rp < 30) { // 30% chance
                        print("Moving camera");
                        Area a = new Area(s.getLocalPlayer().getX() - 10, s.getLocalPlayer().getY() - 10, s.getLocalPlayer().getX() + 10, s.getLocalPlayer().getY() + 10);
                        s.getCamera().rotateToTile(a.getRandomTile());
                        return doNothing();
                    }
                }
                case 14: { // Do early flag
                    if(rp < 5) { // 5% chance
                        DO_RANDOM = true;
                        return doNothing();
                    }
                }
                default:
                    return doNothing();
            }

        }

        return doNothing();
    }

    // Returns whether or not the DO_RANDOM flag has been triggerd
    public boolean doRandom() {
        if(DO_RANDOM) {
            DO_RANDOM = false;
            return true;
        }

        return false;
    }

    // Get antiban status
    public String getStatus() {
        return STATUS;
    }

    // Print to the console if debug is enabled
    private void print(Object o) {
        s.log("[AntiBan] " + o.toString());
    }

    // Returns the chatbot reply count
    public int getChatBotReplyCount() {
        return CHATBOT_REPLIES;
    }

    // Returns a chat reply from Cleverbot
    public String getChatbotReply(String phrase) {
        // If message contains spam, ignore it
        if(containsSpam(phrase))
            return null;

        // If message does not contain any letters, ignore it
        boolean containsLetter = false;
        for(char c : VALID_CHARACTERS.toCharArray()) {
            if(phrase.contains("" + c))
                containsLetter = true;
        }

        if(!containsLetter)
            return null;

        String returner = null;
        phrase = phrase.toLowerCase().trim().replaceAll("[^\\w\\s]", "").replaceAll(s.getLocalPlayer().getName().toLowerCase(), "");
        phrase = phrase.replaceAll("/", "");

        try {
            // Fetch URL result for chatbot API call
            URL url = new URL("https://www.cleverbot.com/getreply?key=" + CHATBOT_KEY + "&input=" + phrase.toLowerCase().trim() + "&cs=" + CHATBOT_STATE + "&callback=ProcessReply");
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = bufferedReader.readLine();
            String T1 = "\"output\":";
            String T2 = "\",\"";
            // Scrape relevant data from JSON output
            String reply = line.substring(line.indexOf(T1) + T1.length());
            reply = reply.substring(0, reply.indexOf(T2)).replaceAll("\"", "").replaceAll("\\.", "");
            String cleverbotState = line.substring(20, line.indexOf("\",\""));
            // Remove any special characters and inappropriate words from the bot reply
            reply = reply.toLowerCase().replaceAll("cleverbot", "");
            reply = reply.toLowerCase().replaceAll("robot", "");
            reply = reply.toLowerCase().replaceAll("name", "deal");
            reply = reply.toLowerCase().replaceAll("how old", "");
            reply = reply.toLowerCase().replaceAll("age", "deal");
            reply = reply.replaceAll("[^\\w\\s]","");

            // Randomly mix up certain punctuation
            if(r(1, 5) == 3)
                reply = reply.replaceAll("'", "");
            if (reply.equalsIgnoreCase("what?") && r(1, 6) == 2)
                reply = reply + "!";

            // If reply does not contain any letters at all, then ignore it
            boolean letterFound = false;
            for(char c : VALID_CHARACTERS.toCharArray()) {
                if(reply.contains("" + c))
                    letterFound = true;
            }

            if(!letterFound)
                return null;

            returner = reply;
            CHATBOT_STATE = cleverbotState;
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            print("ERROR: Failed to get reply from chatbot!");
        }

        return returner;
    }

    // Checks to see if the given message should be replied to
    public boolean checkPlayerMessage(Message message) {
        // Ignore spam
        if(!USE_CHATBOT || containsSpam(message.getMessage()))
            return false;

        long now = System.currentTimeMillis();

        // If script has just started, do not reply to chat
        if(now - START_TIME <= 60000)
            return false;

        String text = message.getMessage().toLowerCase().trim();
        String user = message.getUsername();
        Player speaker = null;

        // Check local players to find if the Speaker exists as a Player entity
        for(Player p : s.getPlayers().all()) {
            if(p != null && p.getName().equalsIgnoreCase(user))
                speaker = p;
        }

        // If the speaker player is null, or the speaker is us, or the speaker is far away, then do not reply
        if(speaker == null || s.getLocalPlayer().equals(speaker) || s.getLocalPlayer().distance(speaker) > 15)
            return false;

        int chatLimitMins = CHATBOT_RATE_MINS;
        // If we have replied within the last X minutes and this chat does not contain our player name or the word bot, don't reply
        if(now - LAST_CHATBOT_REPLY <= r(chatLimitMins, chatLimitMins * 2) && !containsPlayerName(text) && !containsBotWord(text)) {
            //print("We cannot reply for 5-10 more minutes");
            return false;
        }

        // If this chat DOES contain the word bot or our player name, and we have not replied to a similar message in the last 20-45 seconds, then reply.
        if(containsPlayerName(text) || containsBotWord(text)) {
            if(now - LAST_PERSONAL_REPLY <= r(20000, 45000)) {
                return false;
            }

            LAST_PERSONAL_REPLY = now;
        }

        // Get reply from chatbot and type it
        String chatbot = getChatbotReply(message.getMessage());
        if(chatbot != null && !chatbot.equals("")) {
            LAST_CHATBOT_REPLY = System.currentTimeMillis();
            s.log("[ChatBot] Reply: " + chatbot + ", Phrase: " + message.getMessage() + ", From: " + message.getUsername());
            s.sleep(rh(3000, 5000));
            s.getKeyboard().type(chatbot, true, true);
            CHATBOT_REPLIES++;
            return true;
        }

        // No reply was given, return false
        return false;
    }

    // Returns true if any of these botting-related words are found in the given sentence
    public boolean containsBotWord(String phrase) {
        return phrase.contains("bot") || phrase.contains("macro") || phrase.contains("script") || phrase.contains("auto");
    }

    // Returns true if any of these spam-related wrods are found in the given sentence
    public boolean containsSpam(String phrase) {
        String spam = phrase.replaceAll(" ", "").toLowerCase().trim();
        // If message is selling OSRS gp or advertising a clan, ignore it
        if (spam.contains("sell") || spam.contains("usd") || spam.contains("gp") || spam.contains("fee") || spam.contains("free") || spam.contains("join") || phrase.equals("") || phrase.length() <= 1)
            return true;

        return false;
    }

    // Returns true if the player's name is found in the given string
    public boolean containsPlayerName(String phrase) {
        // Check if phrase straight-up contains our name
        boolean containsName = phrase.contains(s.getLocalPlayer().getName());

        // Check if this chat straight-up contains our username
        if(containsName)
            return true;

        // Check for spam
        if(containsSpam(phrase))
            return false;

        // Removes all numbers from our player's name
        String name = s.getLocalPlayer().getName().replaceAll("[^A-Za-z]", "");
        String[] nameWords = splitCamelCase(name);// Splits our name up by capital letters (for nicknames/shorthand - eg. ZenArchist becomes Zen,Archist)
        String[] spaceWords = s.getLocalPlayer().getName().toLowerCase().split(" "); // Splits our name up by spaces (eg. Zen Archist becomes Zen,Archist)

        // Cycle through camelcase name words
        if(nameWords != null && nameWords.length > 1) {
            for (String s : nameWords) {
                if (s.equals("") || s.length() <= 2)
                    continue;

                if (phrase.toLowerCase().contains(s.toLowerCase()))
                    containsName = true;
            }
        }

        // Cycle through spaced name words
        if(spaceWords != null && spaceWords.length > 1) {
            for(String s : spaceWords) {
                if(s.equals("") || s.length() <= 2)
                    continue;

                if(phrase.toLowerCase().contains(s.toLowerCase()))
                    containsName = true;
            }
        }

        // If we have found a chat flag, let the user know in the debug console
        if(containsName)
            print("[ChatBot] Flag: " + phrase + " = " + containsName);

        return containsName;
    }

    // Converts the given string into split words (ie. BitcoinMan becomes Bitcoin Man)
    private static String[] splitCamelCase(String s) {
        String split = s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                ","
        );

        return split.split(",");
    }

    // Allows an external class to set the anti-ban status
    public void setStatus(String status) {
        STATUS = status;
        if(!status.equals(""))
            print(status);
    }

    // Returns a random number
    public int r(int x, int y) {
        return Calculations.random(x, y+1);
    }

    // Returns a random number with the human lag element added to the minimum wait time
    public int rh(int x, int y) {
        //return r(x + getHumanLag(), y + RAND + getHumanLag());
        return r(x + (int)(x*getLagMultiplier()), y + (int)(y*getLagMultiplier()));
    }

    // Returns the lag multiplier (increases as script runs longer)
    public double getLagMultiplier() {
        double minutesRunning = (double) ((System.currentTimeMillis() - START_TIME) / 60000);
        double percent = (minutesRunning / (double)(MAX_RUNTIME_MINUTES == -1 ? 480 : MAX_RUNTIME_MINUTES));

        if(percent > 1.0)
            percent = 1.0D;

        return percent;
    }

    // This method opens the stats menu
    public boolean openStats() {
        if (s.getTabs().getOpen() != Tab.STATS) {
            // Sometimes use hot keys, sometimes use mouse
            if (Calculations.random(1, 3) == 2)
                s.getSkills().open();
            else {
                int x = (int) STATS_WIDGET.getX() + r(0, 10);
                int y = (int) STATS_WIDGET.getY() + r(0, 10);
                s.getMouse().move(new Point(x, y));
                s.sleep(0, 50);
                s.getMouse().click();
            }

            s.sleep(50, 250);
        }

        return s.getTabs().getOpen() == Tab.STATS;
    }

    // Opens the  combat menu then waits for a second
    public boolean openCombat() {
        if (s.getTabs().getOpen() != Tab.COMBAT) {
            // Sometimes use hot keys, sometimes use mouse
            if (Calculations.random(1, 3) == 2)
                s.getTabs().open(Tab.COMBAT);
            else {
                int x = (int) COMBAT_WIDGET.getX() + Calculations.random(0, 10);
                int y = (int) COMBAT_WIDGET.getY() + Calculations.random(0, 10);
                s.getMouse().move(new Point(x, y));
                s.sleep(0, 50);
                s.getMouse().click();
            }

            s.sleep(50, 250);
        }

        return s.getTabs().getOpen() == Tab.COMBAT;
    }

    // This method opens the magic menu
    public boolean openMagic() {
        if (s.getTabs().getOpen() != Tab.MAGIC) {
            // Sometimes use hot keys, sometimes use mouse
            if (Calculations.random(1, 3) == 2)
                s.getTabs().open(Tab.MAGIC);
            else {
                int x = (int) MAGIC_WIDGET.getX() + r(0, 10);
                int y = (int) MAGIC_WIDGET.getY() + r(0, 10);
                s.getMouse().move(new Point(x, y));
                s.getMouse().click();
            }

            s.sleep(50, 250);
        }

        return s.getTabs().getOpen() == Tab.MAGIC;
    }

    // This method opens the inventory
    public boolean openInventory() {
        if (s.getTabs().getOpen() != Tab.INVENTORY) {
            // Sometimes use hot keys, sometimes use mouse
            if (Calculations.random(1, 3) == 2)
                s.getTabs().open(Tab.INVENTORY);
            else {
                int x = (int) INVENTORY_WIDGET.getX() + r(0, 10);
                int y = (int) INVENTORY_WIDGET.getY() + r(0, 10);
                s.getMouse().move(new Point(x, y));
                s.getMouse().click();
            }

            s.sleep(50, 250);
        }

        return s.getTabs().getOpen() == Tab.INVENTORY;
    }
}
