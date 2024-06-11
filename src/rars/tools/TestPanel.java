package rars.tools;

import rars.Globals;
import rars.riscv.hardware.*;
import rars.util.Binary;
import rars.venus.util.AbstractFontSettingDialog;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Observable;
import java.util.Random;
import java.io.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class TestPanel extends AbstractToolAndApplication {

    private static String version = "Version 1.0 (Susana Figueroa)";
    private static String heading = "Test";
    private static String displayPanelTitle, infoPanelTitle;
    private static char VT_FILL = ' ';  // fill character for virtual terminal (random access mode)

    public static Dimension preferredTextAreaDimension = new Dimension(1000, 250);
    private static Insets textAreaInsets = new Insets(4, 4, 4, 4);
    private TestPanel simulator;

    // Major GUI components
    private JPanel display;

    public TestPanel(String title, String heading) {
        super(title, heading);
        simulator = this;
    }

    public TestPanel() {
        this(heading + ", " + version, heading);
    }

    @Override
    public String getName() {
        return "Test";
    }    

    protected JComponent buildMainDisplayArea() {
        display = new JPanel(new BorderLayout());
        try {
            BufferedImage myPicture = ImageIO.read(new File("./src/images/dratini.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            display.add(picLabel);
        } catch (IOException ex) {
            // handle exception...
        }
        return display;
    }
}