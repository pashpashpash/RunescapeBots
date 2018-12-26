package FishingAndWoodcuttingBot.UIComponents;

import FishingAndWoodcuttingBot.UIColours;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class DreamComboBox<T> extends JComboBox<T> {

    private DefaultComboBoxModel<T> model;


    public DreamComboBox(T... objects) {
        setBorder(new LineBorder(UIColours.BUTTON_COLOUR.brighter()));
        setOpaque(true);
        setFocusable(false);
        setBackground(UIColours.COMBOBOX_COLOR);
        setForeground(UIColours.TEXT_COLOR);
        setUI(new BasicComboBoxUI() {
                  @Override
                  protected JButton createArrowButton() {
                      return new DreamButton("\u2193");
                  }
        });
        setModel(model = new DefaultComboBoxModel<>());
        for (T object : objects) {
            model.addElement(object);
        }
    }
}


