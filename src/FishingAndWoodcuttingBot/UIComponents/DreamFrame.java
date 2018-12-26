package FishingAndWoodcuttingBot.UIComponents;


import FishingAndWoodcuttingBot.UIColours;
import FishingAndWoodcuttingBot.UIUtil.VisualTools;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class DreamFrame extends JFrame {


    public DreamFrame() {
        this("");
    }

    public DreamFrame(String title) {
        this(title,null);
    }

    public DreamFrame(String title, BufferedImage logo) {
        setLayout(new BorderLayout());
        setUndecorated(true);
        setTitle(title);
        setBackground(UIColours.BODY_COLOUR);
        getContentPane().setBackground(UIColours.BODY_COLOUR);
        DreamHeader header = new DreamHeader(this,title,logo);
        add(header, BorderLayout.PAGE_START);
    }

    @Override
    public void setSize(Dimension d) {
        setSize(d.width, d.height);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        setShape(new RoundRectangle2D.Double(0,0, width,height, 12,12));
    }

    private class DreamHeader extends DreamPanel {

        private String title;
        private BufferedImage img;
        private DreamLabel logoContainer;
        private DreamLabel titleContainer;
        private DreamPanel left, right;
        private Point initialClick;
        private JFrame frame;

        DreamHeader(JFrame frame, String title, BufferedImage img) {
            this.frame = frame;
            add(left = new DreamPanel(new FlowLayout(FlowLayout.LEFT)), BorderLayout.WEST);
            add(right = new DreamPanel(new FlowLayout(FlowLayout.RIGHT)), BorderLayout.EAST);
            setBorder(new EmptyBorder(3,3,3,3));
            this.title = title;
            this.img = img;
            if (this.img != null) {
                this.img = VisualTools.resize(img, 20, 20, true);
                System.out.println("Adding logo");
                left.add(logoContainer = new DreamLabel());
                logoContainer.setIcon(new ImageIcon(this.img));
            }
            left.add(titleContainer = new DreamLabel(this.title));
            titleContainer.setBorder(new EmptyBorder(0,2,0,2));
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    initialClick = e.getPoint();
                    getComponentAt(initialClick);
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int thisX = frame.getLocation().x;
                    int thisY = frame.getLocation().y;
                    int xMoved = e.getX() - initialClick.x;
                    int yMoved = e.getY() - initialClick.y;
                    int X = thisX + xMoved;
                    int Y = thisY + yMoved;
                    frame.setLocation(X, Y);
                }
            });
        }
    }
}

