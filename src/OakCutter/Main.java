package OakCutter;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.NPC;

import java.awt.*;

@ScriptManifest(
        author = "PASH",
        description = "Oak Trees",
        category = Category.WOODCUTTING,
        version = 1.5,
        name = "PASH"
)
public class Main extends AbstractScript {


    Area bankArea = new Area(3092, 3243, 3095, 3245, 0);
    Area treeArea = new Area(3095, 3294, 3105, 3281, 0);


    @Override
    public void onStart(){
        log("hi");
    }


    @Override
    public int onLoop(){
        GameObject tree = getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Oak"));

        if(!getInventory().isFull()) {
            if(treeArea.contains(getLocalPlayer())) {
                if(tree != null && tree.interact("Chop down")) {
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

                //NPC banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));
                if(getBank().openClosest()) {
                    log("got into bank");
                    if(sleepUntil(() -> getBank().isOpen(), 9000)) {
                        log("got into bank forsure");
                        if(getBank().depositAllExcept(item -> item != null && item.getName().contains("Rune axe"))) {
                            log("deposited");
                            if(sleepUntil(() -> !getInventory().isFull(), 8000)) {
                                if (getBank().close()) {
                                    sleepUntil(() -> !getBank().isOpen(), 8000);
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
