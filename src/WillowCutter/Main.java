package WillowCutter;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.methods.container.impl.Shop;


import java.awt.*;

@ScriptManifest(
        author = "PASH",
        description = "Willow Trees",
        category = Category.WOODCUTTING,
        version = 2.0,
        name = "PASH"
)



public class Main extends AbstractScript {
    Area storeArea = new Area(2947, 3217, 2949, 3214, 0);
    Area treeArea = new Area(2959, 3199, 2973, 3191, 0);

    @Override
    public void onStart(){
        log("hi");
    }

    @Override
    public int onLoop(){
        GameObject tree = getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Willow"));
        if(!getInventory().isFull()) {
            if(treeArea.contains(getLocalPlayer())) {
                if(tree != null && tree.interact("Chop down")) {
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
                    sleep(Calculations.random(7000, 15000));
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
