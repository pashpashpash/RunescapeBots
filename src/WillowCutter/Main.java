package WillowCutter;
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
        description = "Willow Trees",
        category = Category.WOODCUTTING,
        version = 1.0,
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
