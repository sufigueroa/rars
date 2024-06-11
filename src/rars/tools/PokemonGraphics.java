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


public class PokemonGraphics extends AbstractToolAndApplication {

    private static String version = "Version 1.0";
    private static String heading = "Pokemon TM";
    private static String displayPanelTitle, keyboardPanelTitle;
    private static char VT_FILL = ' ';  // fill character for virtual terminal (random access mode)

    public static Dimension preferredTextAreaDimension = new Dimension(1000, 250);
    private static Insets textAreaInsets = new Insets(4, 4, 4, 4);

    public static int POKEMON_1_STATUS;    // keyboard Ready in low-order bit
    public static int POKEMON_1_COMMAND;       // keyboard character in low-order byte
    public static int POKEMON_2_STATUS; // display Ready in low-order bit
    public static int POKEMON_2_COMMAND;    // display character in low-order byte
    // These are used to track instruction counts to simulate driver delay of Transmitter Data
    private boolean countingInstructions;
    private int instructionCount;
    private int transmitDelayInstructionCountLimit;
    private int currentDelayInstructionLimit;

    // Should the transmitted character be displayed before the transmitter delay period?
    // If not, hold onto it and print at the end of delay period.
    private int intCommand;
    private int intStatus;
    private boolean displayAfterDelay = true;

    // Whether or not display position is sequential (JTextArea append)
    // or random access (row, column).  Supports new random access feature. DPS 17-July-2014
    private boolean displayRandomAccessMode = false;
    private int rows, columns;
    private DisplayResizeAdapter updateDisplayBorder;
    private PokemonGraphics simulator;

    // Major GUI components
    private JPanel keyboardAndDisplay;
    private JScrollPane displayScrollPane;
    private JTextArea display;
    private JPanel displayPanel, displayOptions;
    private JPanel keyboardPanel;
    private JScrollPane keyAccepterScrollPane;
    private JTextArea keyEventAccepter;
    private JButton fontButton;
    private Font defaultFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    public static final int EXTERNAL_INTERRUPT_KEYBOARD = 0x00000040;
    public static final int EXTERNAL_INTERRUPT_DISPLAY = 0x00000080;

    /**
     * Simple constructor, likely used to run a stand-alone keyboard/display simulator.
     *
     * @param title   String containing title for title bar
     * @param heading String containing text for heading shown in upper part of window.
     */
    public PokemonGraphics(String title, String heading) {
        super(title, heading);
        simulator = this;
    }

    /**
     * Simple constructor, likely used by the RARS Tools menu mechanism
     */
    public PokemonGraphics() {
        this(heading + ", " + version, heading);
    }

    /**
     * Main provided for pure stand-alone use.  Recommended stand-alone use is to write a
     * driver program that instantiates a PokemonGraphics object then invokes its go() method.
     * "stand-alone" means it is not invoked from the RARS Tools menu.  "Pure" means there
     * is no driver program to invoke the application.
     */
    public static void main(String[] args) {
        new PokemonGraphics(heading + " stand-alone, " + version, heading).go();
    }

    @Override
    public String getName() {
        return "Pokemon TM";
    }

    // Set the MMIO addresses.  Prior to MARS 3.7 these were final because
    // address space was final as well.  Now we will get MMIO base address
    // each time to reflect possible change in memory configuration. DPS 6-Aug-09
    protected void initializePreGUI() {
        POKEMON_1_STATUS = Memory.memoryMapBaseAddress; //0xffff0000; // keyboard Ready in low-order bit
        POKEMON_1_COMMAND = Memory.memoryMapBaseAddress + 4; //0xffff0004; // keyboard character in low-order byte
        POKEMON_2_STATUS = Memory.memoryMapBaseAddress + 8; //0xffff0008; // display Ready in low-order bit
        POKEMON_2_COMMAND = Memory.memoryMapBaseAddress + 12; //0xffff000c; // display character in low-order byte
        displayPanelTitle = "DISPLAY: Store to Transmitter Data " + Binary.intToHexString(POKEMON_2_COMMAND);
        keyboardPanelTitle = "Combat Log";
    }



    protected void addAsObserver() {
        // Set transmitter Control ready bit to 1, means we're ready to accept display character.
        // updateMMIOControl(POKEMON_2_STATUS, readyBitSet(POKEMON_2_STATUS));
        // We want to be an observer only of reads from POKEMON_1_COMMAND and writes to POKEMON_2_COMMAND.
        // Use the Globals.memory.addObserver() methods instead of inherited method to achieve this.
        addAsObserver(POKEMON_1_COMMAND, POKEMON_1_COMMAND);
        addAsObserver(POKEMON_2_COMMAND, POKEMON_2_COMMAND);
        addAsObserver(POKEMON_1_STATUS, POKEMON_1_STATUS);
        addAsObserver(POKEMON_2_STATUS, POKEMON_2_STATUS);
        // We want to be notified of each instruction execution, because instruction count is the
        // basis for delay in re-setting (literally) the POKEMON_2_STATUS register.  SPIM does
        // this too.  This simulates the time required for the display unit to process the
        // POKEMON_2_COMMAND.
        addAsObserver(Memory.textBaseAddress, Memory.textLimitAddress);
    }

    /**
     * Method that constructs the main display area.  It is organized vertically
     * into two major components: the display and the keyboard.  The display itself
     * is a JTextArea and it echoes characters placed into the low order byte of
     * the Transmitter Data location, 0xffff000c.  They keyboard is also a JTextArea
     * places each typed character into the Receive Data location 0xffff0004.
     *
     * @return the GUI component containing these two areas
     */
    protected JComponent buildMainDisplayArea() {
        keyboardAndDisplay = new JPanel(new BorderLayout());
        JSplitPane both = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildDisplay(), buildInfo());
        both.setResizeWeight(0.3);
        keyboardAndDisplay.add(both);
        return keyboardAndDisplay;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //  Rest of the protected methods.  These all override do-nothing methods inherited from
    //  the abstract superclass.
    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void processRISCVUpdate(Observable memory, AccessNotice accessNotice) {
        MemoryAccessNotice notice = (MemoryAccessNotice) accessNotice;
        System.out.println("Cambio en Memoria: " + notice.getAddress() + " valor: " + notice.getValue());
        
        if (notice.getAddress() == POKEMON_1_COMMAND && notice.getAccessType() == AccessNotice.WRITE) {
            intCommand = notice.getValue();
            getCommand(intCommand, 1);
            this.countingInstructions = true;
            this.instructionCount = 0;
        } else if (notice.getAddress() == POKEMON_2_COMMAND && notice.getAccessType() == AccessNotice.WRITE) {
            intCommand = notice.getValue();
            getCommand(intCommand, 2);
            this.countingInstructions = true;
            this.instructionCount = 0;
        } else if (notice.getAddress() == POKEMON_1_STATUS && notice.getAccessType() == AccessNotice.WRITE) {
            intStatus = notice.getValue();
            getStatus(intStatus, 1);
            this.countingInstructions = true;
            this.instructionCount = 0;
        } else if (notice.getAddress() == POKEMON_2_STATUS && notice.getAccessType() == AccessNotice.WRITE) {
            intStatus = notice.getValue();
            getStatus(intStatus, 2);
            this.countingInstructions = true;
            this.instructionCount = 0;
        }
    }

    private void getCommand(int intCommand, int intPokemon) {
        int command = (int) (intCommand & 0x000000FF);
        if (command == 53){
            display.append("" + (char) intCommand);
        }
    }

    private void getStatus(int intStatus, int intPokemon) {
        int status = (int) (intStatus & 0x000000FF);
        if (status == 1){
            keyEventAccepter.append("Oh no! El pokemon " + intPokemon + " esta envenando!\n");
        } else if (status == 2){
            keyEventAccepter.append("Oh no! El pokemon " + intPokemon + " esta dormido!\n");
        }
    }

    @Override
    protected void initializePostGUI() {
        keyEventAccepter.requestFocusInWindow();
    }


    /**
     * Method to reset counters and display when the Reset button selected.
     * Overrides inherited method that does nothing.
     */
    protected void reset() {
        displayRandomAccessMode = false;
        initializeDisplay(displayRandomAccessMode);
        keyEventAccepter.setText("");
        ((TitledBorder) displayPanel.getBorder()).setTitle(displayPanelTitle);
        displayPanel.repaint();
        keyEventAccepter.requestFocusInWindow();
        updateMMIOControl(POKEMON_2_STATUS, readyBitSet(POKEMON_2_STATUS));
    }


    // The display JTextArea (top half) is initialized either to the empty
    // string, or to a string filled with lines of spaces. It will do the
    // latter only if the program has sent the BELL character (Ascii 7) to
    // the transmitter.  This sets the caret (cursor) to a specific (x,y) position
    // on a text-based virtual display.  The lines of spaces is necessary because
    // the caret can only be placed at a position within the current text string.
    private void initializeDisplay(boolean randomAccess) {
        String initialText = "";
        if (randomAccess) {
            Dimension textDimensions = getDisplayPanelTextDimensions();
            columns = (int) textDimensions.getWidth();
            rows = (int) textDimensions.getHeight();
            repaintDisplayPanelBorder();
            char[] charArray = new char[columns];
            Arrays.fill(charArray, VT_FILL);
            String row = new String(charArray);
            StringBuffer str = new StringBuffer(row);
            for (int i = 1; i < rows; i++) {
                str.append("\n" + row);
            }
            initialText = str.toString();
        }
        display.setText(initialText);
        display.setCaretPosition(0);
    }

    // Update display window title with current text display capacity (columns and rows)
    // This will be called when window resized or font changed.
    private void repaintDisplayPanelBorder() {
        Dimension size = this.getDisplayPanelTextDimensions();
        int cols = (int) size.getWidth();
        int rows = (int) size.getHeight();
        int caretPosition = display.getCaretPosition();
        String stringCaretPosition;
        // display position as stream or 2D depending on random access
        if (displayRandomAccessMode) {
            if (((caretPosition + 1) % (columns + 1) != 0)) {
                stringCaretPosition = "(" + (caretPosition % (columns + 1)) + "," + (caretPosition / (columns + 1)) + ")";
            } else if (((caretPosition + 1) % (columns + 1) == 0) && ((caretPosition / (columns + 1)) + 1 == rows)) {
                stringCaretPosition = "(" + (caretPosition % (columns + 1) - 1) + "," + (caretPosition / (columns + 1)) + ")";
            } else {
                stringCaretPosition = "(0," + ((caretPosition / (columns + 1)) + 1) + ")";
            }
        } else {
            stringCaretPosition = "" + caretPosition;
        }
        String title = displayPanelTitle + ", cursor " + stringCaretPosition + ", area " + cols + " x " + rows;
        ((TitledBorder) displayPanel.getBorder()).setTitle(title);
        displayPanel.repaint();
    }


    // Calculate text display capacity of display window. Text dimensions are based
    // on pixel dimensions of window divided by font size properties.
    private Dimension getDisplayPanelTextDimensions() {
        Dimension areaSize = display.getSize();
        int widthInPixels = (int) areaSize.getWidth();
        int heightInPixels = (int) areaSize.getHeight();
        FontMetrics metrics = getFontMetrics(display.getFont());
        int rowHeight = metrics.getHeight();
        int charWidth = metrics.charWidth('m');
        // Estimate number of columns/rows of text that will fit in current window with current font.
        // I subtract 1 because initial tests showed slight scroll otherwise.
        return new Dimension(widthInPixels / charWidth - 1, heightInPixels / rowHeight - 1);
    }


    // Trigger recalculation and update of display text dimensions when window resized.
    private class DisplayResizeAdapter extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            getDisplayPanelTextDimensions();
            repaintDisplayPanelBorder();
        }
    }

    @Override
    protected JComponent getHelpComponent() {
        final String helpContent =
                "Pokemon TM\n\n";
        JButton help = new JButton("Help");
        help.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JTextArea ja = new JTextArea(helpContent);
                        ja.setRows(30);
                        ja.setColumns(60);
                        ja.setLineWrap(true);
                        ja.setWrapStyleWord(true);
                        final JDialog d;
                        final String title = "Simulador de batalla Pokemon";
                        // The following is necessary because there are different JDialog constructors for Dialog and
                        // Frame and theWindow is declared a Window, superclass for both.
                        d = (theWindow instanceof Dialog) ? new JDialog((Dialog) theWindow, title, false)
                                : new JDialog((Frame) theWindow, title, false);
                        d.setSize(ja.getPreferredSize());
                        d.getContentPane().setLayout(new BorderLayout());
                        d.getContentPane().add(new JScrollPane(ja), BorderLayout.CENTER);
                        JButton b = new JButton("Close");
                        b.addActionListener(
                                new ActionListener() {
                                    public void actionPerformed(ActionEvent ev) {
                                        d.setVisible(false);
                                        d.dispose();
                                    }
                                });
                        JPanel p = new JPanel(); // Flow layout will center button.
                        p.add(b);
                        d.getContentPane().add(p, BorderLayout.SOUTH);
                        d.setLocationRelativeTo(theWindow);
                        d.setVisible(true);
                    }
                });
        return help;
    }


    //////////////////////////////////////////////////////////////////////////////////////
    //  Private methods defined to support the above.
    //////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////
    // UI components and layout for upper part of GUI, where simulated display is located.
    private JComponent buildDisplayimagen(){
        displayPanel = new JPanel(new FlowLayout());
        TitledBorder tb = new TitledBorder(displayPanelTitle);
        tb.setTitleJustification(TitledBorder.CENTER);
        displayPanel.setBorder(tb);

        
        // ImageIcon image = new ImageIcon(this.getClass().getResource("./src/images/dratini.png"));
        // // ImageIcon image = new ImageIcon("../../images/dratini.png");
        // JLabel imageLabel = new JLabel(image); 
        // imageLabel.setVisible(true);

        // displayPanel.add(imageLabel);
        return displayPanel;
    }

    private JComponent buildDisplay() {
        displayPanel = new JPanel(new BorderLayout());
        TitledBorder tb = new TitledBorder(displayPanelTitle);
        tb.setTitleJustification(TitledBorder.CENTER);
        displayPanel.setBorder(tb);
        display = new JTextArea();
        display.setFont(defaultFont);
        display.setEditable(false);
        display.setMargin(textAreaInsets);
        updateDisplayBorder = new DisplayResizeAdapter();
        // 	To update display of size in the Display text area when window or font size changes.
        display.addComponentListener(updateDisplayBorder);
        // 	To update display of caret position in the Display text area when caret position changes.
        display.addCaretListener(
                new CaretListener() {
                    public void caretUpdate(CaretEvent e) {
                        simulator.repaintDisplayPanelBorder();
                    }
                }
        );

        // 2011-07-29: Patrik Lundin, patrik@lundin.info
        // Added code so display autoscrolls.
        DefaultCaret caret = (DefaultCaret) display.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        // end added autoscrolling

        displayScrollPane = new JScrollPane(display);
        displayScrollPane.setPreferredSize(preferredTextAreaDimension);

        displayPanel.add(displayScrollPane);
        displayOptions = new JPanel();
        return displayPanel;
    }


    //////////////////////////////////////////////////////////////////////////////////////
    // UI components and layout for lower part of GUI, where simulated keyboard is located.
    private JComponent buildInfo() {
        keyboardPanel = new JPanel(new BorderLayout());
        keyEventAccepter = new JTextArea("Se inicia la pelea\n");
        keyEventAccepter.setEditable(false);
        keyEventAccepter.setFont(defaultFont);
        keyEventAccepter.setMargin(textAreaInsets);
        keyAccepterScrollPane = new JScrollPane(keyEventAccepter);
        keyAccepterScrollPane.setPreferredSize(new Dimension(1000, 100));
        keyboardPanel.add(keyAccepterScrollPane);
        TitledBorder tb = new TitledBorder(keyboardPanelTitle);
        tb.setTitleJustification(TitledBorder.CENTER);
        keyboardPanel.setBorder(tb);
        return keyboardPanel;
    }

    ////////////////////////////////////////////////////////////////////
    // update the MMIO Control register memory cell. We will delegate.
    private void updateMMIOControl(int addr, int intValue) {
        updateMMIOControlAndData(addr, intValue, 0, 0, true);
    }


    /////////////////////////////////////////////////////////////////////
    // update the MMIO Control and Data register pair -- 2 memory cells. We will delegate.
    private void updateMMIOControlAndData(int controlAddr, int controlValue, int dataAddr, int dataValue) {
        updateMMIOControlAndData(controlAddr, controlValue, dataAddr, dataValue, false);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // This one does the work: update the MMIO Control and optionally the Data register as well
    // NOTE: last argument TRUE means update only the MMIO Control register; FALSE means update both Control and Data.
    private void updateMMIOControlAndData(int controlAddr, int controlValue, int dataAddr, int dataValue, boolean controlOnly) {
        if (!this.isBeingUsedAsATool || (this.isBeingUsedAsATool && connectButton.isConnected())) {
            Globals.memoryAndRegistersLock.lock();
            try {
                try {
                    Globals.memory.setRawWord(controlAddr, controlValue);
                    if (!controlOnly) Globals.memory.setRawWord(dataAddr, dataValue);
                } catch (AddressErrorException aee) {
                    System.out.println("Tool author specified incorrect MMIO address!" + aee);
                    System.exit(0);
                }
            } finally {
                Globals.memoryAndRegistersLock.unlock();
            }
            // HERE'S A HACK!!  Want to immediately display the updated memory value in MARS
            // but that code was not written for event-driven update (e.g. Observer) --
            // it was written to poll the memory cells for their values.  So we force it to do so.

            if (Globals.getGui() != null && Globals.getGui().getMainPane().getExecutePane().getTextSegmentWindow().getCodeHighlighting()) {
                Globals.getGui().getMainPane().getExecutePane().getDataSegmentWindow().updateValues();
            }
        }
    }


    /////////////////////////////////////////////////////////////////////
    // Return value of the given MMIO control register after ready (low order) bit set (to 1).
    // Have to preserve the value of Interrupt Enable bit (bit 1)
    private static boolean isReadyBitSet(int mmioControlRegister) {
        try {
            return (Globals.memory.get(mmioControlRegister, Memory.WORD_LENGTH_BYTES) & 1) == 1;
        } catch (AddressErrorException aee) {
            System.out.println("Tool author specified incorrect MMIO address!" + aee);
            System.exit(0);
        }
        return false; // to satisfy the compiler -- this will never happen.
    }


    /////////////////////////////////////////////////////////////////////
    // Return value of the given MMIO control register after ready (low order) bit set (to 1).
    // Have to preserve the value of Interrupt Enable bit (bit 1)
    private static int readyBitSet(int mmioControlRegister) {
        try {
            return Globals.memory.get(mmioControlRegister, Memory.WORD_LENGTH_BYTES) | 1;
        } catch (AddressErrorException aee) {
            System.out.println("Tool author specified incorrect MMIO address!" + aee);
            System.exit(0);
        }
        return 1; // to satisfy the compiler -- this will never happen.
    }

    /////////////////////////////////////////////////////////////////////
    //  Return value of the given MMIO control register after ready (low order) bit cleared (to 0).
    // Have to preserve the value of Interrupt Enable bit (bit 1). Bits 2 and higher don't matter.
    private static int readyBitCleared(int mmioControlRegister) {
        try {
            return Globals.memory.get(mmioControlRegister, Memory.WORD_LENGTH_BYTES) & 2;
        } catch (AddressErrorException aee) {
            System.out.println("Tool author specified incorrect MMIO address!" + aee);
            System.exit(0);
        }
        return 0; // to satisfy the compiler -- this will never happen.
    }

}