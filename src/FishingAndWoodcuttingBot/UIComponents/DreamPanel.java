package FishingAndWoodcuttingBot.UIComponents;

import FishingAndWoodcuttingBot.UIColours;

import javax.swing.*;
import java.awt.*;

public class DreamPanel extends JPanel {

    public DreamPanel() {
        this(new BorderLayout());
    }


    public DreamPanel(LayoutManager manager) {
        setLayout(manager);
        setBackground(UIColours.BODY_COLOUR);
    }
}
