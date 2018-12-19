package YewCutter;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.NPC;

import java.awt.*;

@ScriptManifest(
        author = "PASH",
        description = "Yew Trees with Antiban",
        category = Category.WOODCUTTING,
        version = 1.0,
        name = "[PASH] YewCutter + Antiban"
)

public class Main extends AbstractScript {
    Area bankArea = new Area(3185, 3445, 3181, 3435, 0);
    Area treeArea = new Area(3203, 3506, 3223, 3498, 0);

    @Override
    public void onStart(){
        log("hi");
        getSkills().open();
        sleep(Calculations.random(1000, 2000));
        getSkills().hoverSkill(Skill.WOODCUTTING);
        log("ANTIBAN: hovered over skill.");
        log("swag");
        sleep(Calculations.random(1000, 4000));
        if (!getTabs().isOpen(org.dreambot.api.methods.tabs.Tab.INVENTORY)) {
            getTabs().openWithMouse(org.dreambot.api.methods.tabs.Tab.INVENTORY);
            sleep(Calculations.random(1000, 1500));
            log("ANTIBAN: Set tab to inventory.");
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
                log("ANTIBAN: hovering over skill.");
                sleep(Calculations.random(1000, 4000));
                if (!getTabs().isOpen(org.dreambot.api.methods.tabs.Tab.INVENTORY)) {
                    getTabs().openWithMouse(org.dreambot.api.methods.tabs.Tab.INVENTORY);
                    sleep(Calculations.random(1000, 1500));
                    log("ANTIBAN: Set tab to inventory.");
                }
            }
        }
        GameObject tree = getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Yew"));
        if(!getInventory().isFull()) {
            if(treeArea.contains(getLocalPlayer())) {
                if (Calculations.random(0, 20) == 7) {//antiban
//                    getCamera().rotateToEntity(getGameObjects().all(gameObject -> gameObject != null && gameObject.getName().equals("Yew")).get(1));
//                    log("ANTIBAN: turned camera to another entity");

                    log("ANTIBAN: running to another area");
                    Area nearbyArea = new Area (3203 + Calculations.random(-3, 3), 3506 + (Calculations.random(-3, 4)), 3223 + (Calculations.random(-4, 3)), 3498 + (Calculations.random(-4, 4)), 0);
                    if(getWalking().walk(nearbyArea.getRandomTile())) {
                        sleep(Calculations.random(3000, 10000));
                    }
                } else if(tree != null && tree.interact("Chop down")) {
                    getMouse().move(new Point(Calculations.random(0, 765), Calculations.random(0, 503))); //antiban
                    log("Chopping the yew");
                    int countLog = getInventory().count("Logs");
                    sleepUntil(() -> getInventory().count("Logs") > countLog, 12000);
                }
            } else {
                if(getWalking().walk(treeArea.getRandomTile())) {
                    sleep(Calculations.random(3000, 5500));
                }
            }

        }
        if(getInventory().isFull()) {
            if(bankArea.contains(getLocalPlayer()) && !getBank().isOpen() ) {
                log("got to bank area");
                getMouse().move(new Point(Calculations.random(0, 765), Calculations.random(0, 503))); //antiban
                log("ANTIBAN: moving mouse to random area");
                //NPC banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));
                if(getBank().openClosest()) {
                    log("Got into bank");
                    if(sleepUntil(() -> getBank().isOpen(),  Calculations.random(7000, 9000))) {
                        if(getBank().depositAllExcept(item -> item != null && item.getName().contains("Rune axe"))) {
                            log("Deposited");
                            if(sleepUntil(() -> !getInventory().isFull(), Calculations.random(5500, 6500))) {
                                if (getBank().close()) {
                                    sleepUntil(() -> !getBank().isOpen(), Calculations.random(7500, 8500));
                                }
                            }
                        }
                    }
                }

            } else {
                if(getWalking().walk(bankArea.getRandomTile())) {
                    sleep(Calculations.random(3000, 6000));
                }
            }
        }
        return Calculations.random(500, 800);
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
