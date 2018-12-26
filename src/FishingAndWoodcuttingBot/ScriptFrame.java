package FishingAndWoodcuttingBot;
import FishingAndWoodcuttingBot.UIComponents.*;

import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;


public class ScriptFrame extends DreamFrame {

    private DreamPanel body, content, bottom;

    public String fishing = "Trout&Salmon";
    public String woodcutting = "Willows";
    public String itemDump = "Rimmington Store";
    public String hangoutSpot = "Lumbridge Bridge";


    public DreamButton startButton = new DreamButton("Start Script!");
    private DreamComboBox fishingComboBox = new DreamComboBox("Shrimps & Anchovies", "Sardines & Herrings","Trout & Salmon", "Lobsters");
    private DreamComboBox woodcuttingComboBox = new DreamComboBox("Trees","Oaks", "Willows" , "Yews");
    private DreamComboBox itemDumpComboBox = new DreamComboBox("Rimmington Store", "Bank");
    private DreamComboBox hangOutSpotComboBox = new DreamComboBox("Lumbridge Bridge", "Falador Bridge");

    public ScriptFrame() throws IOException {
        super("[ PASH ] Fishing & Woodcutting Hybrid [ SAFE ]", ImageIO.read(new URL("https://i.imgur.com/PLhQL1U.png")));
        DreamTabbedPane tPane = new DreamTabbedPane();
        tPane.addTab("", body = new DreamPanel());
        setSize(500,260);
        setLocationRelativeTo(null);
        setLocation(getLocation().x, (getLocation().y + 40));
        add(tPane, BorderLayout.CENTER);
        body.setBorder(new EmptyBorder(7,8,7,8));
        body.add(content = new DreamPanel(), BorderLayout.NORTH);
        GridLayout grid = new GridLayout(0,2);
        grid.setVgap(5);
        content.setLayout(grid);
        content.add(new DreamLabel("Item Dump Location"));
        content.add(itemDumpComboBox);
        content.add(new DreamLabel("Fishing (~30min)"));
        content.add(fishingComboBox);
        content.add(new DreamLabel("Woodcutting (~30min)"));
        content.add(woodcuttingComboBox);
        content.add(new DreamLabel("Hanging out (~7min)"));
        content.add(hangOutSpotComboBox);

        body.add(bottom = new DreamPanel(), BorderLayout.SOUTH);
        GridLayout grid2 = new GridLayout(2,0);
        bottom.setLayout(grid2);

        bottom.add(new DreamLabel("Script runs for ~1 hour before logging out. Don't suicide, take breaks :)"));
        bottom.add(startButton);

    }

    public static void main(String... args) throws IOException {
        ScriptFrame frame = new ScriptFrame();
        frame.setVisible(true);

    }
    public String[] getUserConfig() {
        fishing = fishingComboBox.getSelectedItem().toString();
        woodcutting = woodcuttingComboBox.getSelectedItem().toString();
        itemDump = itemDumpComboBox.getSelectedItem().toString();
        hangoutSpot = hangOutSpotComboBox.getSelectedItem().toString();
        String[] s = new String[4];
        s[0] = itemDump;
        s[1] = fishing;
        s[2] = woodcutting;
        s[3] = hangoutSpot;
        return s;
    }
}
