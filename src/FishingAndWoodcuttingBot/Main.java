package FishingAndWoodcuttingBot;//package ZenTester;


import AntibanTemplate.ZenAntiBan;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;


import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;
import java.io.IOException;

@ScriptManifest(
        category = Category.MISC,
        name = "[PASH] Fishing & WC Hybrid [SAFE]",
        author = "PASH",
        version = 1.0,
        description = "Alternates between fishing, woodcutting, and hanging out in Lumbridge.")
public class Main extends AbstractScript {
    // Declare anti-ban instance
    private ZenAntiBan antiban;
    private int State = 0;
    private long START_TIME = 0L; // Time the script was started
    private long NOW_TIME_CACHED = 0L;
    private long FISHING_LENGTH = 0L;
    private long WOODCUTTING_LENGTH = 0L;
    private long CHILLING_LENGTH = 0L;
    private DecimalFormat numberFormat = new DecimalFormat("#.00");
    private int numLogsCut = 0;
    private int numFishesCaught = 0;

    String itemDump = "store"; //sell, bank
    String fishingType = "shrimps"; //shrimps sardines, salmons, or lobsters
    String woodCuttingType = "trees"; //trees, oaks, willows, or yews

//    Area storeArea = new Area(2947, 3217, 2949, 3214, 0);
    Area storeArea = new Area(2947, 3217, 2949, 3214, 0); // defaults rimmington
    Area bankArea = new Area(3026, 3237, 3046, 3234, 0);
    Tile bankTile = new Tile(3045, 3235, 0);


    //Fishing Globals
    Area fishingSpotArea = new Area(2997, 3159, 2993, 3145, 0); //defaults to sardines
    int fishingItem1 = 345; //defaults to herrings
    int fishingItem2 = 327; //defaults to sardines
    String fishingAction = "Bait"; //defaults to bait
    String fishingSpotType = "Fishing spot"; //defaults to normal fishing spot

    //Woodcutting Globals
    Area woodCuttingArea = new Area(2977, 3207, 2989, 3200, 0); //defaults to oaks for now
    int woodCuttingItemID = 1521; //defaults to oak logs
    int woodCuttingTreeID = 1751; //defaults to oak trees

    //WC Locations to choose from
    Area treesArea = new Area(2921, 3233, 2938, 3223, 0);
    Area oaksArea = new Area(2977, 3207, 2989, 3200, 0);
    Area willowsArea = new Area(2959, 3198, 2974, 3192, 0);
    Area yewsArea = new Area(3049, 3272, 3057, 3270, 0);


    //Fishing Locations to choose from
    Area shrimpsFishingSpot = new Area(2997, 3159, 2993, 3145, 0);
    Area sardineFishingSpot = new Area(2997, 3159, 2993, 3145, 0);
    Area salmonFishingSpot = new Area(3100, 3435, 3111, 3424, 0);
    Area lobsterFishingSpot = new Area(2924, 3180, 2925, 3174, 0);

    //ports
    Area sarim = new Area(3026, 3232, 3029, 3216, 0);
    Area karam = new Area(2958, 3146, 2948, 3150, 0);
    Area rimmy = new Area(2922, 3508, 2990, 3186, 0);


    //items
    int shrimps = 317;
    int anchovies = 321;
    int sardines = 327;
    int herrings = 345;
    int trouts = 335;
    int salmons = 331;
    int lobsters = 377;


    int trees = 1511;
    int oaks = 1521;
    int willows = 1519;
    int yews = 1515;

    //Objects & Actions
    int treeObject = 1276;
    int oakObject = 1751;
    int willowObject = 1756;
    int yewsObject = 1753;

    String smallNetAction = "Small Net";
    String baitAction = "Bait";
    String lureAction = "Lure";
    String cageAction = "Cage";
    String normalFishingSpotType = "Fishing spot";
    String rodFishingSpotType = "Rod fishing spot";

    private boolean inKaramja = false;

    //Hangout area
    String chillingSpot = "lumbridge"; //lumbridge, falador
    Area hangoutArea = new Area(3243, 3226, 3247, 3225, 0);
    Area lumbridge = new Area(3243, 3226, 3247, 3225, 0);
    Area falador = new Area(2990, 3386, 2991, 3381, 0);


    public Boolean scriptStart = false;
    private final Image bg = getImage("https://i.imgur.com/PLhQL1U.png");

    private Image getImage(String url) {
        try
        {
            return ImageIO.read(new URL(url)).getScaledInstance(64, 64,Image.SCALE_DEFAULT);
        }
        catch (IOException e) {}
        return null;
    }

    @Override
    public void onStart() {
        startingMenu();

        log("Starting script! " + fishingType + " | " + woodCuttingType);
        // Initialize anti-ban instance
        antiban = new ZenAntiBan(this);
        START_TIME = System.currentTimeMillis(); //sets startTime
        log("START TIME : " + START_TIME);
        NOW_TIME_CACHED = System.currentTimeMillis(); //sets nowTimeCached

        FISHING_LENGTH = 1920000 + Calculations.random(-300000, 300000); // 32 min +/-5 minutes
        WOODCUTTING_LENGTH = 1920000 + Calculations.random(-300000, 300000);
        CHILLING_LENGTH = 420000 + Calculations.random(-60000, 60000); // 7 min +/-1 minute
        if(chillingSpot.equals("lumbridge")) {
            hangoutArea = lumbridge;
        } else if(chillingSpot.equals("falador")) {
            hangoutArea = falador;
        }
        if(fishingType.equals("shrimps")) {
            fishingSpotArea = shrimpsFishingSpot;
            storeArea = storeArea; //if I introduce bank later, need to change logic here
            fishingItem1 = shrimps;
            fishingItem2 = anchovies;
            fishingAction = smallNetAction;
            fishingSpotType = normalFishingSpotType;
        } else if(fishingType.equals("sardines")) {
            fishingSpotArea = sardineFishingSpot;
            storeArea = storeArea; //if I introduce bank later, need to change logic here
            fishingItem1 = sardines;
            fishingItem2 = herrings;
            fishingAction = baitAction;
            fishingSpotType = normalFishingSpotType;
        } else if(fishingType.equals("salmons")) {
            fishingSpotArea = salmonFishingSpot;
            storeArea = storeArea;
            fishingItem1 = trouts;
            fishingItem2 = salmons;
            fishingAction = lureAction;
            fishingSpotType = rodFishingSpotType;
        } else if(fishingType.equals("lobsters")) {
            fishingSpotArea = lobsterFishingSpot;
            storeArea = storeArea;
            fishingItem1 = lobsters;
            fishingItem2 = lobsters;
            fishingAction = cageAction;
            fishingSpotType = normalFishingSpotType;
        }
        if(woodCuttingType.equals("trees")) {
            woodCuttingArea = treesArea;
            storeArea = storeArea; //rimmington
            woodCuttingItemID = trees; //change this later
            woodCuttingTreeID = treeObject;
        } else if(woodCuttingType.equals("oaks")) {
            woodCuttingArea = oaksArea;
            storeArea = storeArea; //rimmington
            woodCuttingItemID = oaks;
            woodCuttingTreeID = oakObject;
        } else if(woodCuttingType.equals("willows")) {
            woodCuttingArea = willowsArea;
            storeArea = storeArea; //rimmington
            woodCuttingItemID = willows;
            woodCuttingTreeID = willowObject;
        } else if(woodCuttingType.equals("yews")) {
            woodCuttingArea = yewsArea;
            storeArea = storeArea; //rimmington
            woodCuttingItemID = yews;
            woodCuttingTreeID = yewsObject;
        }

    }

    @Override
    public int onLoop() {
        checkForMods(); //Logs out if mod present, should probably change to just switch worlds.
        if(antiban.doRandom()) {         // Check for random flag (for adding extra customized anti-ban features)
            log("Script-specific random flag triggered");
            if (!getWalking().isRunEnabled() && getWalking().getRunEnergy() > 30) {
                getWalking().toggleRun();
            }
        }

        if(State == 0) { //start script with fishing.
            log("Fishing time!");
            if(fishingType == "lobsters") {
                lobsterFishingTime();
            } else {
                fishingTime();
            }
        } else if(State == 1) { //do woodcutting
            log("WoodCutting Time!!");
            woodCuttingTime();
        } else if(State == 2) { //hang out in Lumbridge for like 4 minutes
            log("Hangout Time!!");
            hangoutTime();
        } else if (State == 3) {
            log("Exit Time!!");
            getTabs().logout(); //might want to change this to switch worlds instead..
            sleep(Calculations.random(1000,3000));
            stop();
            onExit();

        }


        return antiban.antiBan();         // Call anti-ban (returns a wait time after performing any actions)
    }

    @Override
    // Draw anti-ban info to the screen
    public void onPaint(Graphics g) {

        if(Calculations.random(1,100)==7){
            NOW_TIME_CACHED = System.currentTimeMillis();
        }
        double numMinutes = ((NOW_TIME_CACHED - START_TIME)/1000);
        numMinutes = numMinutes/60;

        g.drawString("Anti-Ban Status : " + (antiban.getStatus().equals("") ? "Inactive" : antiban.getStatus()), 94, 287);
        g.drawString("Script has been running for : " + numberFormat.format(numMinutes) + " minutes.", 94, 307);

        if(numFishesCaught > 0) {
            g.drawString("F : " + numFishesCaught, 466, 287);
        }
        if(numLogsCut > 0) {
            g.drawString("L : " + numLogsCut, 466, 307);
        }

        g.drawImage(bg, 20, 260, null);
    }
    public void fishingTime(){
        long NOW_TIME = System.currentTimeMillis(); //sets nowTime
        if(NOW_TIME - START_TIME >= FISHING_LENGTH) { //We've been fishing for longer than 32ish minutes)
            finishFishing();
        } else {
            log("Doing fishing");
            doFishing();
        }
    }
    public void woodCuttingTime() {
        long NOW_TIME = System.currentTimeMillis(); //sets nowTime
        if(NOW_TIME - START_TIME >= FISHING_LENGTH + WOODCUTTING_LENGTH) { //We've been woodcutting for longer than 32ish mins
            finishWoodCutting();
        } else {
            doWoodCutting();
        }
    }
    public void hangoutTime(){
        long NOW_TIME = System.currentTimeMillis(); //sets nowTime
        if(NOW_TIME - START_TIME >= FISHING_LENGTH + WOODCUTTING_LENGTH + CHILLING_LENGTH) { //2748000We
            // 've been standing in lumby for longer than 4 mins = 2748000 + Calculations.random(-1000, 1000)
            getTabs().logout(); //might want to change this to switch worlds instead..
            sleep(Calculations.random(500,1000));
            State = 3; //update state
        } else {
            goHangOut();

            sleep(Calculations.random(1000,1500)); //continues chilling
        }
    }

    public void doFishing() {
        if(!getInventory().isFull()) {
            if(fishingSpotArea.contains(getLocalPlayer())) {
                NPC fishingSpot = getNpcs().closest(fs -> fs.hasAction(fishingAction));
                if (Calculations.random(0, 1000) == 7) {//Antiban
                    log("ANTIBAN: Running to another area");
                    if(getWalking().walk(fishingSpotArea.getRandomTile())) {
                        sleep(Calculations.random(3000, 10000));
                    }
                } else if (fishingSpot != null) {
                    if(fishingSpot.interact(fishingAction)){
                        getMouse().move(new Point(Calculations.random(0, 765), Calculations.random(0, 503))); //anti-ban
                        int countHerring = getInventory().count(fishingItem2);
                        int countSardine = getInventory().count(fishingItem1);
                        if(Calculations.random(0, 27)==7) {
                            sleepUntil(() -> getInventory().count(fishingItem2) > countHerring || getInventory().count(fishingItem1) > countSardine, Calculations.random(12000, 16000));
                        }
                        sleepUntil(() -> !getLocalPlayer().isAnimating(), Calculations.random(12000, 16000));

                        sleep(Calculations.random(1000, 3000));
                    }

                }
            } else {
                if(getWalking().walk(fishingSpotArea.getRandomTile())) {
                    sleep(Calculations.random(3000, 5500));
                }
            }
        }
        if(getInventory().isFull()) { //sell if full
            if(itemDump == "bank") {
                log("Inventory full, banking fish");
                bankFishing();
            } else {
                log("Inventory full, selling fish");
                sellFishing();
            }

        }
    }

    public void finishFishing() {
        if(getInventory().count(fishingItem1) > 0 || getInventory().count(fishingItem2) > 0) {
            log("Finishing Fishing");
            if(itemDump == "bank") {
                bankFishing();
            } else {
                sellFishing();
            }
        }
        if(getInventory().count(fishingItem1) == 0 && getInventory().count(fishingItem2) == 0) {
            log("Finished Fishing");
            State = 1; //update state
        }
    }
    public void sellFishing() {
        Area fuckupArea = new Area(3060, 3362, 3073,3326,0);
        Area northArea = new Area (3076, 3362, 3082, 3356, 0);
        Area southArea = new Area (3061, 3315, 3067, 3310, 0);
        if(storeArea.contains(getLocalPlayer())) {
            log("Player in store area");
            getMouse().move(new Point(Calculations.random(0, 765), Calculations.random(0, 503))); //antiban
            log("ANTIBAN: Moving mouse to random area");
            NPC shop = getNpcs().closest("Shop assistant");
            if (shop != null && shop.isOnScreen()) {
                shop.interact("Trade");
                sleepUntil(() -> getShop().isOpen(), Calculations.random(2000, 5500));
                if (getShop().isOpen()) {
                    log("Shop Opened!! ");
                    getShop().sellFifty(fishingItem1);
                    sleep(Calculations.random(400,600));
                    getShop().sellFifty(fishingItem2);
                    sleep(Calculations.random(400,600));
                    if (sleepUntil(() -> !getInventory().isFull(), Calculations.random(3500, 6500))) {
                        if (getShop().close()) {
                            sleepUntil(() -> !getShop().isOpen(), Calculations.random(5000, 7000));
                        }
                    }
                }
            }
        } else if(fuckupArea.contains(getLocalPlayer())) {
            if(getInventory().isFull()) {
                if(getWalking().walk(southArea.getRandomTile())) {
                    log("heading to south area");
                    sleep(Calculations.random(3000, 5500));
                }
            } else {
                if(getWalking().walk(northArea.getRandomTile())) {
                    log("heading to north area");
                    sleep(Calculations.random(3000, 5500));
                }
            }
        } else {
            if(getWalking().walk(storeArea.getRandomTile())) {

                sleep(Calculations.random(3000, 5500));
            }
        }
    }


    public void bankFishing() {
        if(bankArea.contains(getLocalPlayer())) {
            log("got to bank area");
            if(bankTile.distance(getLocalPlayer()) > 4) {
                if(getWalking().walk(bankTile)) {
                    sleep(Calculations.random(3000, 5500));
                }
            }
            else if(!getDepositBox().isOpen()) {
                if(getDepositBox().open()) {
                    sleep(Calculations.random(1000,3000));
                }
            } else {
                int numfishes1 = getInventory().count(fishingItem1);
                int numfishes2 = 0;
                if(fishingItem1 != fishingItem2) {
                    numfishes2 = getInventory().count(fishingItem2);
                }

                numFishesCaught = numFishesCaught + numfishes1 + numfishes2;
                if (getDepositBox().depositAll(item -> item != null && item.getID() == fishingItem1)) {
                    log("Deposited");
                    if (getDepositBox().depositAll(item -> item != null && item.getID() == fishingItem2)) {
                        if(sleepUntil(() -> !getInventory().isFull(), Calculations.random(7500, 8500))) {
                            if (getDepositBox().close()) {
                                sleepUntil(() -> !getDepositBox().isOpen(), Calculations.random(7500, 8500));
                            }
                        }
                    }
                }
            }
        } else {
            log("Walking to bank");
            if(getWalking().walk(bankTile)) {
                sleep(Calculations.random(3000, 5500));
            }
        }
    }

    public void bankWoodCutting() {
        if(bankArea.contains(getLocalPlayer())) {
            log("got to bank area");
            if(bankTile.distance(getLocalPlayer()) > 4) {
                if(getWalking().walk(bankTile)) {
                    sleep(Calculations.random(3000, 5500));
                }
            }
            else if(!getDepositBox().isOpen()) {
                getDepositBox().open();
            } else {
                int numItems = getInventory().count(woodCuttingItemID);
                log("items in inv = " + numItems);
                numLogsCut = numLogsCut + numItems;
                log("numLogsCut = " + numLogsCut);
                if (getDepositBox().depositAll(item -> item != null && item.getID() == woodCuttingItemID)) {
                    log("Deposited");
                    if(sleepUntil(() -> !getInventory().isFull(), Calculations.random(7500, 8500))) {
                        if (getDepositBox().close()) {
                            sleepUntil(() -> !getDepositBox().isOpen(), Calculations.random(7500, 8500));
                        }
                    }
                }
            }
        } else {
            if(getWalking().walk(bankTile)) {
                sleep(Calculations.random(3000, 5500));
            }
        }
    }


    public void doWoodCutting() {
        if(!getInventory().isFull()) {
            if(woodCuttingArea.contains(getLocalPlayer())) {
                GameObject tree = getGameObjects().closest(gameObject -> gameObject != null && gameObject.getID() == woodCuttingTreeID);
                if(woodCuttingType == "yews" && tree == null) { //hope world if chopping yews
                    getWorldHopper().hopWorld(getWorlds().getRandomWorld(w -> w != null && w.isF2P() && w.getMinimumLevel() < 100));
                    sleep(Calculations.random(8000, 14000));
                }
                if (Calculations.random(0, 1000) == 7) {//antiban
                    GameObject nearbyTree = getGameObjects().all(gameObject -> gameObject != null && gameObject.getID() == woodCuttingTreeID).get(1);
                    if(nearbyTree != null) {
                        getCamera().rotateToEntity(nearbyTree);
                        log("ANTIBAN: Turned camera to another entity");
                    }
                } else if (tree != null && tree.interact("Chop down")) {
                    getMouse().move(new Point(Calculations.random(0, 765), Calculations.random(0, 503))); //antiban
                    int countLog = getInventory().count(woodCuttingItemID);
                    if(Calculations.random(0, 27) == 7) {
                        sleepUntil(() -> getInventory().count(woodCuttingItemID) > countLog, Calculations.random(12000, 16000));
                    }
                    sleepUntil(() -> !getLocalPlayer().isAnimating(), Calculations.random(12000, 16000));
                    sleep(Calculations.random(1000, 3000));
                }
            } else {
                if(getWalking().walk(woodCuttingArea.getRandomTile())) {
                    sleep(Calculations.random(3000, 5500));
                }
            }
        }
        if(getInventory().isFull()) { //sell if full
            if(itemDump == "bank") {
                bankWoodCutting();
            } else {
                sellWoodCutting();
            }
        }
    }
    public void finishWoodCutting() {
        if(getInventory().count(woodCuttingItemID) > 0) {
            log("Finishing Woodcutting");
            if(itemDump == "bank") {
                bankWoodCutting();
            } else {
                sellWoodCutting();
            }
        }
        if(getInventory().count(woodCuttingItemID) == 0){
            State = 2; //update state
        }
    }
    public void sellWoodCutting() {
        if(storeArea.contains(getLocalPlayer())) {
            log("Player in store area");
            NPC shop = getNpcs().closest("Shop assistant");
            if(shop != null && shop.isOnScreen()){
                shop.interact("Trade");
                sleepUntil(() -> getShop().isOpen(), Calculations.random(2000, 5500));
                if(getShop().isOpen()) {
                    log("Shop Opened!!");
                    getShop().sellFifty(woodCuttingItemID);
                    log("Sold Oak Logs");
                    if(sleepUntil(() -> !getInventory().isFull(), Calculations.random(3500, 6500))) {
                        if (getShop().close()) {
                            sleepUntil(() -> !getShop().isOpen(), Calculations.random(5000, 7000));
                            if(getInventory().count(woodCuttingItemID) > 0) { //store is full, switch worlds
                                getWorldHopper().hopWorld(getWorlds().getRandomWorld(w -> w != null && w.isF2P() && w.getMinimumLevel() < 100));
                                sleep(Calculations.random(8000, 14000));
                            }
                        }
                    }
                }
            }
        }
        else {
            if(getWalking().walk(storeArea.getRandomTile())) {
                sleep(Calculations.random(4000, 8000));
            }
        }
    }

    public void goHangOut(){
        if(hangoutArea.contains(getLocalPlayer())) {
            sleep(Calculations.random(1000, 1433));
        } else {
            if(getWalking().walk(hangoutArea.getRandomTile())) {
                sleep(Calculations.random(3000, 5555));
            }
        }
    }

    public void checkForMods() {
        if (!getPlayers().all(f -> f != null && f.getName().contains("Mod")).isEmpty()) {
            log("We just found a JMod! Switched worlds, quickly... Time: " + System.currentTimeMillis());
            getWorldHopper().hopWorld(getWorlds().getRandomWorld(w -> w != null && w.isF2P() && w.getMinimumLevel() < 100));
            sleep(Calculations.random(8000, 14000));
        }
    }

    //other helpers
    public void configureScript(String itemDumpConfig, String fishing, String woodcutting, String hangOutConfig) {
        if(itemDumpConfig.equals("Rimmington Store")) {
            itemDump = "sell";
        } else if(itemDumpConfig.equals("Bank")) {
            itemDump = "bank";
        }

        log("Fishing:" + fishing + " WC: " + woodcutting);
        if(fishing.equals("Shrimps & Anchovies")) {
            fishingType = "shrimps"; //shrimps, sardines, salmons, or lobsters
        } else if(fishing.equals("Sardines & Herrings")) {
            fishingType = "sardines"; //shrimps, sardines, salmons, or lobsters

        } else if(fishing.equals("Trout & Salmon")) {
            fishingType = "salmons"; //shrimps, sardines, salmons, or lobsters
        } else if(fishing.equals("Lobsters")) {
            fishingType = "lobsters"; //shrimps, sardines, salmons, or lobsters
        }
        if(woodcutting.equals("Trees")) {
            woodCuttingType = "trees"; //trees, oaks, willows, or yews
        } else if(woodcutting.equals("Oaks")) {
            woodCuttingType = "oaks"; //trees, oaks, willows, or yews
        } else if(woodcutting.equals("Willows")) {
            woodCuttingType = "willows"; //trees, oaks, willows, or yews
        } else if (woodcutting.equals("Yews")) {
            woodCuttingType = "yews"; //done
        }

        if(hangOutConfig.equals("Lumbridge Bridge")) {
            chillingSpot = "lumbridge";
        } else if(hangOutConfig.equals("Falador Bridge")) {
            chillingSpot = "falador";
        }
    }


    public Boolean scriptIsOn() {
        if(scriptStart == true) {
            return true;
        }
        sleep(100);
        return false;
    }

    public void startingMenu() {
        //before starting script, open window.
        try {
            ScriptFrame frame = new ScriptFrame();
            frame.setVisible(true);
            frame.startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String[] userConfig = frame.getUserConfig();
                    configureScript(userConfig[0], userConfig[1], userConfig[2], userConfig[3]);
                    frame.setVisible(false);
                    scriptStart = true;
                }
            } );
            while(!scriptIsOn()){
                if(scriptStart) {
                    break;
                }
            }
        } catch (IOException ioe) {
            log("Had trouble opening window");
        }
    }

    public void lobsterFishingTime(){
        long NOW_TIME = System.currentTimeMillis(); //sets nowTime
        if(NOW_TIME - START_TIME >= FISHING_LENGTH) { //We've been fishing for longer than 32ish minutes)
            finishLobsterFishing();
        } else {
            log("Doing lobster fishing");
            doLobsterFishing();
        }
    }
    public void finishLobsterFishing() {
        Area boat1 = new Area(3032, 3214, 3036, 3221, 1);
        Area boat2 = new Area(2953, 3143, 2960, 3139, 1);
        if(getInventory().count(fishingItem1) > 0) {
            log("finishing lobster fishing");
            if(karam.contains(getLocalPlayer()) || boat1.contains(getLocalPlayer()) || boat2.contains(getLocalPlayer())) { //player on boat/in karamja
                takeBoat();
            } else if((getLocalPlayer().getGridX() == 6208 && getLocalPlayer().getGridY() == 6208)) { //player in transit
                sleep(Calculations.random(200,500));
            } else if(karam.getRandomTile().distance(getLocalPlayer()) < 64 && !rimmy.contains(getLocalPlayer())) { //player in karamja
                if(getWalking().walk(karam.getRandomTile())) {
                    sleep(Calculations.random(3000, 5500));
                }
            } else { //player on mainland,
                if(itemDump == "bank") {
                    log("banking lobsters");
                    bankFishing();
                } else {
                    log("selling lobsters");
                    sellFishing();
                }
            }
        }
        if(getInventory().count(fishingItem1) == 0){
            if(karam.contains(getLocalPlayer()) || boat1.contains(getLocalPlayer()) || boat2.contains(getLocalPlayer())) { //player on boat/in karamja
                takeBoat();
            } else if((getLocalPlayer().getGridX() == 6208 && getLocalPlayer().getGridY() == 6208)) { //player in transit
                sleep(Calculations.random(200,500));
            } else if(karam.getRandomTile().distance(getLocalPlayer()) < 64 && !rimmy.contains(getLocalPlayer())) { //player in karamja
                if(getWalking().walk(karam.getRandomTile())) {
                    sleep(Calculations.random(3000, 5500));
                }
            } else { //player on mainland
                log("Finished Lobster Fishing");
                State = 1; //update state
            }
        }
    }
    public void doLobsterFishing(){
        Area boat1 = new Area(3032, 3214, 3036, 3221, 1);
        Area boat2 = new Area(2953, 3143, 2960, 3139, 1);
        if(getInventory().isFull()) {
            if(karam.contains(getLocalPlayer()) || boat1.contains(getLocalPlayer()) || boat2.contains(getLocalPlayer())) { //player on boat/in karamja
                takeBoat();
            } else if((getLocalPlayer().getGridX() == 6208 && getLocalPlayer().getGridY() == 6208)) { //player in transit
                sleep(Calculations.random(200,500));
            } else if(karam.getRandomTile().distance(getLocalPlayer()) < 64 && !rimmy.contains(getLocalPlayer())) { //player in karamja
                if(getWalking().walk(karam.getRandomTile())) {
                    sleep(Calculations.random(3000, 5500));
                }
            } else { //player on mainland
                if(itemDump == "bank") {
                    log("");
                    bankFishing();
                } else {
                    sellFishing();
                }
            }
        } else {
            if(sarim.contains(getLocalPlayer()) || boat1.contains(getLocalPlayer()) || boat2.contains(getLocalPlayer())) { //player on boat or in port
                takeBoat();
            } else if(karam.getRandomTile().distance(getLocalPlayer()) < 64 && !rimmy.contains(getLocalPlayer())) { //player in karamja
                log("player is in karamja with non-full inventory.");
                doFishing();
            } else {
                if(getWalking().walk(sarim.getRandomTile())) {
                    sleep(Calculations.random(3000, 5500));
                }
            }
        }
    }

    public void takeBoat() {
        log("taking boat");
        Area boat1 = new Area(3032, 3214, 3036, 3221, 1);
        if(sarim.contains(getLocalPlayer())) {
            NPC sailor = getNpcs().closest("Seaman Lorris");
            getCamera().rotateToEntity(sailor);
            if (sailor != null && sailor.isOnScreen()) {
                if(!getDialogues().inDialogue()) {
                    sailor.interact("Pay-fare");
                    sleepUntil(() -> getDialogues().inDialogue(), Calculations.random(1000, 1500));
                } else {
                    if(getDialogues().canContinue()) {
                        getDialogues().continueDialogue();
                        sleep(Calculations.random(500,800));
                    } else {
                        getDialogues().clickOption(getDialogues().getOptions()[0]);
                        sleepUntil(() -> !getDialogues().inDialogue(), Calculations.random(2000, 3000));
                    }
                }
            }
        } else if (karam.contains(getLocalPlayer())){
            NPC sailor = getNpcs().closest("Customs officer");
            getCamera().rotateToEntity(sailor);
            if (sailor != null && sailor.isOnScreen()) {
                if(!getDialogues().inDialogue()) {
                    sailor.interact("Pay-fare");
                    sleepUntil(() -> getDialogues().inDialogue(), Calculations.random(1000, 1500));
                } else {
                    if(getDialogues().canContinue()) {
                        getDialogues().continueDialogue();
                        sleep(Calculations.random(1000,1500));
                    } else {
                        getDialogues().clickOption(getDialogues().getOptions()[0]);
                        sleepUntil(() -> !getDialogues().inDialogue(), Calculations.random(2000, 3000));
                    }
                }
            }
        } else {
            if(karam.getRandomTile().distance(getLocalPlayer()) > 100) { //player in port
                if(getWalking().walk(boat1.getRandomTile())) {
                    sleep(Calculations.random(1000, 5000));
                }
            }
            if(getLocalPlayer().getGridX() != 6208 && getLocalPlayer().getGridY() != 6208) { //not in transit
                getCamera().rotateToEntity(getLocalPlayer());
                log("standing on the boat, gotta press on plank");
                Tile t = new Tile(2956, 3144, 1);
                Tile t2 = new Tile(3031, 3217, 1);
                if (t.distance(getLocalPlayer()) < 40) {
                    if (getMap().interact(t)) {
                        sleepUntil(() -> sarim.contains(getLocalPlayer()) || karam.contains(getLocalPlayer()), Calculations.random(4000, 6000));
                    }
                } else {
                    if (getMap().interact(t2)) {
                        sleepUntil(() -> sarim.contains(getLocalPlayer()) || karam.contains(getLocalPlayer()), Calculations.random(4000, 6000));
                    }
                }
            }
        }

    }


}