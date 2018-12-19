package WillowCutter;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.tabs.Tabs;

import java.awt.*;

@ScriptManifest(
        author = "PASH",
        description = "Willow Trees with Antiban",
        category = Category.WOODCUTTING,
        version = 1.0,
        name = "[PASH] WillowCutter + Antiban"
)


public class Main extends AbstractScript {
    Area storeArea = new Area(2947, 3217, 2949, 3214, 0);
    Area treeArea = new Area(2959, 3199, 2973, 3191, 0);

    @Override
    public void onStart(){
        log("hi");
        getSkills().open();
        sleep(Calculations.random(1000, 2000));
        getSkills().hoverSkill(Skill.WOODCUTTING);
        log("ANTIBAN: hovered over skill.");
        sleep(Calculations.random(1000, 4000));
        if (!getTabs().isOpen(org.dreambot.api.methods.tabs.Tab.INVENTORY)) {
            getTabs().openWithMouse(org.dreambot.api.methods.tabs.Tab.INVENTORY);
            sleep(Calculations.random(1000, 1500));
            log("ANTIBAN: Set tab  to inventory.");
        }
    }

    @Override
    public int onLoop(){
        if(Calculations.random(0,20) == 14) {
            if(!getWalking().isRunEnabled() && getWalking().getRunEnergy() > 30) {
                getWalking().toggleRun();
            }
            if(Calculations.random(0,10) == 7 && getTabs().isOpen(org.dreambot.api.methods.tabs.Tab.INVENTORY)) {
                getSkills().open();
                sleep(Calculations.random(1000, 2000));
                getSkills().hoverSkill(Skill.WOODCUTTING);
                log("ANTIBAN: hovered over skill.");
                sleep(Calculations.random(1000, 4000));
                if (!getTabs().isOpen(org.dreambot.api.methods.tabs.Tab.INVENTORY)) {
                    getTabs().openWithMouse(org.dreambot.api.methods.tabs.Tab.INVENTORY);
                    sleep(Calculations.random(1000, 1500));
                    log("ANTIBAN: Set tab to inventory.");
                }
            }
        }


        GameObject tree = getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Willow"));
        getMouse().move(new Point(Calculations.random(0, 765), Calculations.random(0, 503))); //antiban
        log("ANTIBAN: moving mouse to random area");
        if(!getInventory().isFull()) {
            if(treeArea.contains(getLocalPlayer())) {
                if (Calculations.random(0, 20) == 7) {//antiban
                    getCamera().rotateToEntity(getGameObjects().all(gameObject -> gameObject != null && gameObject.getName().equals("Willow")).get(1));
                    log("ANTIBAN: turned camera to another entity");

                    log("ANTIBAN: running to another area");
                    Area nearbyArea = new Area (2959 + Calculations.random(-3, 3), 3199 + (Calculations.random(-3, 4)), 2973 + (Calculations.random(-4, 3)), 3191 + (Calculations.random(-4, 4)), 0);
                    if(getWalking().walk(nearbyArea.getRandomTile())) {
                        sleep(Calculations.random(3000, 10000));
                    }
                } else if (tree != null && tree.interact("Chop down")) {
                    getMouse().move(new Point(Calculations.random(0, 765), Calculations.random(0, 503))); //antiban
                    int countLog = getInventory().count("Willow logs");
                    sleepUntil(() -> getInventory().count("Willow logs") > countLog, Calculations.random(12000, 16000));
                }
            } else {
                if(getWalking().walk(treeArea.getRandomTile())) {
                    sleep(Calculations.random(3000, 5500));
                }
            }
        }
        if(getInventory().isFull()) {
            if(storeArea.contains(getLocalPlayer())) {
                log("Player in store area");
                getMouse().move(new Point(Calculations.random(0, 765), Calculations.random(0, 503))); //antiban
                log("ANTIBAN: moving mouse to random area");
                NPC shop = getNpcs().closest("Shop assistant");
                if(shop != null && shop.isOnScreen()){
                    shop.interact("Trade");
                    sleepUntil(() -> getShop().isOpen(), Calculations.random(2000, 5500));
                    if(getShop().isOpen﻿()) {
                        log("Shop opened!!");
                        getShop().sellFifty(1519);
                        log("sold");
                        if(sleepUntil(() -> !getInventory().isFull(), Calculations.random(3500, 6500))) {
                            if (getShop().close()) {
                                sleepUntil(() -> !getShop().isOpen(), Calculations.random(5000, 7000));
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
        return 600;
    }

    @Override
    public void onExit(){
        super.onExit();
    }

    @Override
    public void onPaint(Graphics graphics) {
        super.onPaint(graphics);
    }
}
