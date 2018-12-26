package FishingAndWoodcuttingBot.UIComponents;

import FishingAndWoodcuttingBot.UIColours;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class DreamTabbedPane extends JTabbedPane{

    public DreamTabbedPane() {
        setFocusable(false);
        UIManager.put("TabbedPane.background", UIColours.BUTTON_COLOUR);
        UIManager.put("TabbedPane.foreground", UIColours.TEXT_COLOR);
        UIManager.put("TabbedPane.highlight", UIColours.BODY_COLOUR);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.selected", UIColours.BODY_COLOUR);

        setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                shadow = UIColours.BODY_COLOUR;
                darkShadow = UIColours.BODY_COLOUR;
                lightHighlight = UIColours.BODY_COLOUR;
                //focus = new Color(64,255,136);
            }

        });

    }
}
