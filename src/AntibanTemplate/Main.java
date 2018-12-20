//package ZenTester;
package AntibanTemplate;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import java.awt.*;

@ScriptManifest(
        category = Category.MISC,
        name = "[PASH] Anti-ban Framework",
        author = "PASH",
        version = 1.0,
        description = "For testing")
public class Main extends AbstractScript {
    // Declare anti-ban instance
    private ZenAntiBan antiban;

    @Override
    public void onStart() {
        // Initialize anti-ban instance
        antiban = new ZenAntiBan(this);
    }

    @Override
    public int onLoop() {
        // Check for random flag (for adding extra customized anti-ban features)
        if(antiban.doRandom())
            log("Script-specific random flag triggered");

        // Call anti-ban (returns a wait time after performing any actions)
        return antiban.antiBan();
    }

    @Override
    // Draw anti-ban info to the screen
    public void onPaint(Graphics g) {
        g.drawString("Anti-Ban Status: " + (antiban.getStatus().equals("") ? "Inactive" : antiban.getStatus()), 10, 100);
    }
}