package CookingAndWoodcuttingBot;//package ZenTester;

import AntibanTemplate.ZenAntiBan;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

import java.awt.*;

@ScriptManifest(
        category = Category.MISC,
        name = "[PASH] Fishing & Woodcutting Bot (Robust Anti-ban and Anti-mod)",
        author = "PASH",
        version = 1.0,
        description = "Alternating activities every 20ish minutes is a good call to avoid ban detection. This script fishes Shrimps for 20 Min, then cuts Trees for 20 min, then breaks on Lumbridge Bridge for 4 minutes before log out. All with robust anti-ban and anti-mod. Recommend taking a 20-60 minute break before running script again.")
public class Main extends AbstractScript {
    // Declare anti-ban instance
    private ZenAntiBan antiban;
    private int State = 0;
    private long START_TIME = 0L; // Time the script was started

    //Fishing areas
    Area fishingStoreArea = new Area(2947, 3217, 2949, 3214, 0);
    Area fishingSpotArea = new Area(2997, 3159, 2993, 3145, 0);


    //Woodcutting areas
    Area woodCuttingStoreArea = new Area(2947, 3217, 2949, 3214, 0);
    Area woodCuttingTreeArea_Oaks = new Area(2977, 3207, 2989, 3200, 0);
    Area woodCuttingTreeArea_Trees = new Area(2959, 3199, 2973, 3191, 0);


    //Hangout area
    Area hangoutArea = new Area(3243, 3226, 3247, 3225, 0);


    @Override
    public void onStart() {
        // Initialize anti-ban instance
        antiban = new ZenAntiBan(this);
        START_TIME = System.currentTimeMillis(); //sets startTime
        log("START TIME : " + START_TIME);
    }

    @Override
    public int onLoop() {
        checkForMods(); //Logs out if mod present, should probably change to just switch worlds.
        if(antiban.doRandom()) {         // Check for random flag (for adding extra customized anti-ban features)
            log("Script-specific random flag triggered");
            if (!getWalking().isRunEnabled() && getWalking().getRunEnergy() > 30) {
                getWalking().toggleRun();
            }
            if (Calculations.random(0, 10) == 7 && getTabs().isOpen(org.dreambot.api.methods.tabs.Tab.INVENTORY)) {
                getSkills().open();
                sleep(Calculations.random(1000, 2000));
                getSkills().hoverSkill(Skill.WOODCUTTING);
                log("ANTI-BAN: Hovered over skill.");
                sleep(Calculations.random(1000, 4000));
                if (!getTabs().isOpen(org.dreambot.api.methods.tabs.Tab.INVENTORY)) {
                    getTabs().openWithMouse(org.dreambot.api.methods.tabs.Tab.INVENTORY);
                    sleep(Calculations.random(1000, 1500));
                    log("ANTI-BAN: Set tab to inventory.");
                }
            }
        }

        if(State == 0) { //start script with fishing.
            log("Fishing time!");
            fishingTime();
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
        g.drawString("Anti-Ban Status: " + (antiban.getStatus().equals("") ? "Inactive" : antiban.getStatus()), 10, 100);
    }
    public void fishingTime(){
        long NOW_TIME = System.currentTimeMillis(); //sets nowTime
        if(NOW_TIME - START_TIME >= 1320000 + Calculations.random(-10000, 11111)) { //We've been fishing for longer than 22 mins = 1320000 + Calculations.random(-10000, 11111)
            finishFishing();
        } else {
            log("Doing fishing");
            doFishing();
        }
    }

    public void woodCuttingTime() {
        long NOW_TIME = System.currentTimeMillis(); //sets nowTime
        if(NOW_TIME - START_TIME >= 2442000 + Calculations.random(-12000, 11111)) { //We've been woodcutting for longer than 20.7ish mins
            finishWoodCutting();
        } else {
            doWoodCutting();
        }
    }
    public void hangoutTime(){
        long NOW_TIME = System.currentTimeMillis(); //sets nowTime
        if(NOW_TIME - START_TIME >= 2748000 + Calculations.random(-1000, 1000)) { //2748000We
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
                NPC fishingSpot = getNpcs().closest("Fishing spot");
                if (Calculations.random(0, 1000) == 7) {//Antiban
                    log("ANTIBAN: Running to another area");
                    Area nearbyArea = new Area (2959 + Calculations.random(-3, 3), 3199 + (Calculations.random(-3, 4)), 2973 + (Calculations.random(-4, 3)), 3191 + (Calculations.random(-4, 4)), 0);
                    if(getWalking().walk(nearbyArea.getRandomTile())) {
                        sleep(Calculations.random(3000, 10000));
                    }
                } else if (fishingSpot != null) {
                    if(fishingSpot.interact("Bait")){
                        getMouse().move(new Point(Calculations.random(0, 765), Calculations.random(0, 503))); //anti-ban
                        int countHerring = getInventory().count("Raw herring");
                        int countSardine = getInventory().count("Raw sardine");
                        sleepUntil(() -> getInventory().count("Raw herring") > countHerring || getInventory().count("Raw sardine") > countSardine, Calculations.random(12000, 16000));
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
            log("Inventory full, selling fish");
            sellFishing();
        }
    }

    public void finishFishing() {
        log("finishingFishing");
        if(getInventory().count("Raw herring") > 0 || getInventory().count("Raw sardine") > 0) {
            sellFishing();
        }
        if(getInventory().count("Raw herring") == 0 && getInventory().count("Raw sardine") == 0) {
            State = 1; //update state
        }
    }
    public void sellFishing() {
        if(fishingStoreArea.contains(getLocalPlayer())) {
            log("Player in store area");
            getMouse().move(new Point(Calculations.random(0, 765), Calculations.random(0, 503))); //antiban
            log("ANTIBAN: Moving mouse to random area");
            NPC shop = getNpcs().closest("Shop assistant");
            if (shop != null && shop.isOnScreen()) {
                shop.interact("Trade");
                sleepUntil(() -> getShop().isOpen(), Calculations.random(2000, 5500));
                if (getShop().isOpen()) {
                    log("Shop Opened!! ");
                    getShop().sellFifty(345);
                    sleep(Calculations.random(100,500));
                    getShop().sellFifty(327);
                    log("Sold Fish");
                    if (sleepUntil(() -> !getInventory().isFull(), Calculations.random(3500, 6500))) {
                        if (getShop().close()) {
                            sleepUntil(() -> !getShop().isOpen(), Calculations.random(5000, 7000));
                        }
                    }
                }
            }
        } else {
            if(getWalking().walk(fishingStoreArea.getRandomTile())) {
                sleep(Calculations.random(3000, 5500));
            }
        }
    }

    public void doWoodCutting() {
        getMouse().move(new Point(Calculations.random(0, 765), Calculations.random(0, 503))); //antiban
        log("ANTIBAN: moving mouse to random area");
        if(!getInventory().isFull()) {
            if(woodCuttingTreeArea_Oaks.contains(getLocalPlayer())) {
                GameObject tree = getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Oak"));
                if (Calculations.random(0, 1000) == 7) {//antiban
                    getCamera().rotateToEntity(getGameObjects().all(gameObject -> gameObject != null && gameObject.getName().equals("Oak")).get(1));
                    log("ANTIBAN: Turned camera to another entity");

                    log("ANTIBAN: Running to another area");
                    Area nearbyArea = new Area (2959 + Calculations.random(-3, 3), 3199 + (Calculations.random(-3, 4)), 2973 + (Calculations.random(-4, 3)), 3191 + (Calculations.random(-4, 4)), 0);
                    if(getWalking().walk(nearbyArea.getRandomTile())) {
                        sleep(Calculations.random(3000, 10000));
                    }
                } else if (tree != null && tree.interact("Chop down")) {
                    getMouse().move(new Point(Calculations.random(0, 765), Calculations.random(0, 503))); //antiban
                    int countLog = getInventory().count("Oak logs");
                    sleepUntil(() -> getInventory().count("Oak logs") > countLog, Calculations.random(12000, 16000));
                    sleep(Calculations.random(1000, 3000));
                }
            } else {
                if(getWalking().walk(woodCuttingTreeArea_Oaks.getRandomTile())) {
                    sleep(Calculations.random(3000, 5500));
                }
            }
        }
        if(getInventory().isFull()) { //sell if full
            sellWoodCutting();
        }
    }
    public void finishWoodCutting() {
        if(getInventory().count("Oak Logs") > 0) {
            sellWoodCutting();
        }
        if(getInventory().count("Oak Logs") == 0){
            State = 2; //update state
        }
    }
    public void sellWoodCutting() {
        if(woodCuttingStoreArea.contains(getLocalPlayer())) {
            log("Player in store area");
            NPC shop = getNpcs().closest("Shop assistant");
            if(shop != null && shop.isOnScreen()){
                shop.interact("Trade");
                sleepUntil(() -> getShop().isOpen(), Calculations.random(2000, 5500));
                if(getShop().isOpen()) {
                    log("Shop Opened!!");
                    getShop().sellFifty(1521);
                    log("Sold Oak Logs");
                    if(sleepUntil(() -> !getInventory().isFull(), Calculations.random(3500, 6500))) {
                        if (getShop().close()) {
                            sleepUntil(() -> !getShop().isOpen(), Calculations.random(5000, 7000));
                        }
                    }
                }
            }
        }
        else {
            if(getWalking().walk(woodCuttingStoreArea.getRandomTile())) {
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
            log("We just found a JMod! Logged out, quickly... Time: " + System.currentTimeMillis());
            getTabs().logout(); //might want to change this to switch worlds instead..
            stop();
            onExit();
        }
    }

}