package FishingAndWoodcuttingBot.UIComponents;
import FishingAndWoodcuttingBot.UIColours;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class DreamTextField extends JTextField {

    public DreamTextField() {
        setOpaque(true);
        setBackground(UIColours.TEXTFIELD_COLOR);
        setCaretColor(UIColours.TEXT_COLOR);
        setForeground(UIColours.TEXT_COLOR);
        setBorder(new CompoundBorder(new LineBorder(UIColours.BUTTON_COLOUR.brighter()), new EmptyBorder(2,2,2,2)));
    }
}
