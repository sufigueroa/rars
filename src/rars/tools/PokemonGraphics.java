package rars.tools;

import rars.Globals;
import rars.riscv.hardware.*;
import rars.util.Binary;
import rars.venus.util.AbstractFontSettingDialog;

import javax.swing.*;
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


public class PokemonGraphics extends AbstractToolAndApplication {

    private static String version = "Version 1.0";
    private static String heading = "Pokemon TM";
    private static String displayPanelTitle, keyboardPanelTitle;
    private static char VT_FILL = ' ';  // fill character for virtual terminal (random access mode)

    public static Dimension preferredTextAreaDimension = new Dimension(400, 200);
    private static Insets textAreaInsets = new Insets(4, 4, 4, 4);

    public static int RECEIVER_CONTROL;    // keyboard Ready in low-order bit
    public static int RECEIVER_DATA;       // keyboard character in low-order byte
    public static int TRANSMITTER_CONTROL; // display Ready in low-order bit
    public static int TRANSMITTER_DATA;    // display character in low-order byte
    // These are used to track instruction counts to simulate driver delay of Transmitter Data
    private boolean countingInstructions;
    private int instructionCount;
    private int transmitDelayInstructionCountLimit;
    private int currentDelayInstructionLimit;

    // Should the transmitted character be displayed before the transmitter delay period?
    // If not, hold onto it and print at the end of delay period.
    private int intWithCharacterToDisplay;
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
        RECEIVER_CONTROL = Memory.memoryMapBaseAddress; //0xffff0000; // keyboard Ready in low-order bit
        RECEIVER_DATA = Memory.memoryMapBaseAddress + 4; //0xffff0004; // keyboard character in low-order byte
        TRANSMITTER_CONTROL = Memory.memoryMapBaseAddress + 8; //0xffff0008; // display Ready in low-order bit
        TRANSMITTER_DATA = Memory.memoryMapBaseAddress + 12; //0xffff000c; // display character in low-order byte
        displayPanelTitle = "DISPLAY: Store to Transmitter Data " + Binary.intToHexString(TRANSMITTER_DATA);
        keyboardPanelTitle = "KEYBOARD: Characters typed here are stored to Receiver Data " + Binary.intToHexString(RECEIVER_DATA);
    }


    /**
     * Override the inherited method, which registers us as an Observer over the static data segment
     * (starting address 0x10010000) only.
     * <p>
     * When user enters keystroke, set RECEIVER_CONTROL and RECEIVER_DATA using the action listener.
     * When user loads word (lw) from RECEIVER_DATA (we are notified of the read), then clear RECEIVER_CONTROL.
     * When user stores word (sw) to TRANSMITTER_DATA (we are notified of the write), then clear TRANSMITTER_CONTROL, read TRANSMITTER_DATA,
     * echo the character to display, wait for delay period, then set TRANSMITTER_CONTROL.
     * <p>
     * If you use the inherited GUI buttons, this method is invoked when you click "Connect" button on Tool or the
     * "Assemble and Run" button on a Rars-based app.
     */
    protected void addAsObserver() {
        // Set transmitter Control ready bit to 1, means we're ready to accept display character.
        updateMMIOControl(TRANSMITTER_CONTROL, readyBitSet(TRANSMITTER_CONTROL));
        // We want to be an observer only of reads from RECEIVER_DATA and writes to TRANSMITTER_DATA.
        // Use the Globals.memory.addObserver() methods instead of inherited method to achieve this.
        addAsObserver(RECEIVER_DATA, RECEIVER_DATA);
        addAsObserver(TRANSMITTER_DATA, TRANSMITTER_DATA);
        // We want to be notified of each instruction execution, because instruction count is the
        // basis for delay in re-setting (literally) the TRANSMITTER_CONTROL register.  SPIM does
        // this too.  This simulates the time required for the display unit to process the
        // TRANSMITTER_DATA.
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
        // Changed arrangement of the display and keyboard panels from GridLayout(2,1)
        // to BorderLayout to hold a JSplitPane containing both panels.  This permits user
        // to apportion the relative sizes of the display and keyboard panels within
        // the overall frame.  Will be convenient for use with the new random-access
        // display positioning feature.  Previously, both the display and the keyboard
        // text areas were equal in size and there was no way for the user to change that.
        // DPS 17-July-2014
        keyboardAndDisplay = new JPanel(new BorderLayout());
        JSplitPane both = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildDisplay(), buildKeyboard());
        both.setResizeWeight(0.5);
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
        // If the program has just read (loaded) the receiver (keyboard) data register,
        // then clear the Ready bit to indicate there is no longer a keystroke available.
        // If Ready bit was initially clear, they'll get the old keystroke -- serves 'em right
        // for not checking!
        if (notice.getAddress() == RECEIVER_DATA && notice.getAccessType() == AccessNotice.READ) {
            updateMMIOControl(RECEIVER_CONTROL, readyBitCleared(RECEIVER_CONTROL));
        }
        // The program has just written (stored) the transmitter (display) data register.  If transmitter
        // Ready bit is clear, device is not ready yet so ignore this event -- serves 'em right for not checking!
        // If transmitter Ready bit is set, then clear it to indicate the display device is processing the character.
        // Also start an intruction counter that will simulate the delay of the slower
        // display device processing the character.
        if (isReadyBitSet(TRANSMITTER_CONTROL) && notice.getAddress() == TRANSMITTER_DATA && notice.getAccessType() == AccessNotice.WRITE) {
            updateMMIOControl(TRANSMITTER_CONTROL, readyBitCleared(TRANSMITTER_CONTROL));
            intWithCharacterToDisplay = notice.getValue();
            if (!displayAfterDelay) displayCharacter(intWithCharacterToDisplay);
            this.countingInstructions = true;
            this.instructionCount = 0;
        }
        // We have been notified of an instruction execution.
        // If we are in transmit delay period, increment instruction count and if limit
        // has been reached, set the transmitter Ready flag to indicate the program
        // can write another character to the transmitter data register.  If the Interrupt-Enabled
        // bit had been set by the program, generate an interrupt!
        if (this.countingInstructions &&
                notice.getAccessType() == AccessNotice.READ && Memory.inTextSegment(notice.getAddress())) {
            this.instructionCount++;
            if (this.instructionCount >= this.transmitDelayInstructionCountLimit) {
                if (displayAfterDelay) displayCharacter(intWithCharacterToDisplay);
                this.countingInstructions = false;
                int updatedTransmitterControl = readyBitSet(TRANSMITTER_CONTROL);
                updateMMIOControl(TRANSMITTER_CONTROL, updatedTransmitterControl);
                if (updatedTransmitterControl != 1) {
                    InterruptController.registerExternalInterrupt(EXTERNAL_INTERRUPT_DISPLAY);
                }
            }
        }
    }

    private static final char CLEAR_SCREEN = 12; // ASCII Form Feed
    private static final char SET_CURSOR_X_Y = 7; // ASCII Bell  (ding ding!)

    // Method to display the character stored in the low-order byte of
    // the parameter.  We also recognize two non-printing characters:
    //  Decimal 12 (Ascii Form Feed) to clear the display
    //  Decimal  7 (Ascii Bell) to place the cursor at a specified (X,Y) position.
    //             of a virtual text terminal.  The position is specified in the high
    //             order 24 bits of the transmitter word (X in 20-31, Y in 8-19).
    //             Thus the parameter is the entire word, not just the low-order byte.
    // Once the latter is performed, the display mode changes to random
    // access, which has repercussions for the implementation of character display.
    private void displayCharacter(int intWithCharacterToDisplay) {
        char characterToDisplay = (char) (intWithCharacterToDisplay & 0x000000FF);
        if (characterToDisplay == CLEAR_SCREEN) {
            initializeDisplay(displayRandomAccessMode);
        } else if (characterToDisplay == SET_CURSOR_X_Y) {
            // First call will activate random access mode.
            // We're using JTextArea, where caret has to be within text.
            // So initialize text to all spaces to fill the JTextArea to its
            // current capacity.  Then set caret.  Subsequent character
            // displays will replace, not append, in the text.
            if (!displayRandomAccessMode) {
                displayRandomAccessMode = true;
                initializeDisplay(displayRandomAccessMode);
            }
            // For SET_CURSOR_X_Y, we need data from the rest of the word.
            // High order 3 bytes are split in half to store (X,Y) value.
            // High 12 bits contain X value, next 12 bits contain Y value.
            int x = (intWithCharacterToDisplay & 0xFFF00000) >>> 20;
            int y = (intWithCharacterToDisplay & 0x000FFF00) >>> 8;
            // If X or Y values are outside current range, set to range limit.
            if (x < 0) x = 0;
            if (x >= columns) x = columns - 1;
            if (y < 0) y = 0;
            if (y >= rows) y = rows - 1;
            // display is a JTextArea whose character positioning in the text is linear.
            // Converting (row,column) to linear position requires knowing how many columns
            // are in each row.  I add one because each row except the last ends with '\n' that
            // does not count as a column but occupies a position in the text string.
            // The values of rows and columns is set in initializeDisplay().
            display.setCaretPosition(y * (columns + 1) + x);
        } else {
            if (displayRandomAccessMode) {
                try {
                    int caretPosition = display.getCaretPosition();
                    // if caret is positioned at the end of a line (at the '\n'), skip over the '\n'
                    if ((caretPosition + 1) % (columns + 1) == 0) {
                        caretPosition++;
                        display.setCaretPosition(caretPosition);
                    }
                    display.replaceRange("" + characterToDisplay, caretPosition, caretPosition + 1);
                } catch (IllegalArgumentException e) {
                    // tried to write off the end of the defined grid.
                    display.setCaretPosition(display.getCaretPosition() - 1);
                    display.replaceRange("" + characterToDisplay, display.getCaretPosition(), display.getCaretPosition() + 1);
                }
            } else {
                display.append("" + characterToDisplay);
            }
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
        updateMMIOControl(TRANSMITTER_CONTROL, readyBitSet(TRANSMITTER_CONTROL));
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
            //             if ( caretPosition == rows*(columns+1)+1) {
            //                stringCaretPosition = "(0,0)";
            //             }
            //             else if ( (caretPosition+1) % (columns+1) == 0) {
            //                stringCaretPosition = "(0,"+((caretPosition/(columns+1))+1)+")";
            //             }
            //             else {
            //                stringCaretPosition = "("+(caretPosition%(columns+1))+","+(caretPosition/(columns+1))+")";
            //             }
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
                        // TODO: potentially implement method 2
                        // Make the Help dialog modeless (can remain visible while working with other components).
                        // Unfortunately, JOptionPane.showMessageDialog() cannot be made modeless.  I found two
                        // workarounds:
                        //  (1) Use JDialog and the additional work that requires
                        //  (2) create JOptionPane object, get JDialog from it, make the JDialog modeless
                        // Solution 2 is shorter but requires Java 1.6.  Trying to keep MARS at 1.5.  So we
                        // do it the hard way.  DPS 16-July-2014
                        final JDialog d;
                        final String title = "Simulating the Keyboard and Display";
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
                        // This alternative technique is simpler than the above but requires java 1.6!  DPS 16-July-2014
                        //       JOptionPane theStuff = new JOptionPane(new JScrollPane(ja),JOptionPane.INFORMATION_MESSAGE,
                        //            JOptionPane.DEFAULT_OPTION, null, new String[]{"Close"} );
                        //       JDialog theDialog = theStuff.createDialog(theWindow, "Simulating the Keyboard and Display");
                        //       theDialog.setModal(false);
                        //       theDialog.setVisible(true);
                        // The original code. Cannot be made modeless.
                        //       JOptionPane.showMessageDialog(theWindow, new JScrollPane(ja),
                        //           "Simulating the Keyboard and Display", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
        return help;
    }


    //////////////////////////////////////////////////////////////////////////////////////
    //  Private methods defined to support the above.
    //////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////
    // UI components and layout for upper part of GUI, where simulated display is located.
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

        //font button to display font
        fontButton = new JButton("Font");
        fontButton.setToolTipText("Select the font for the display panel");
        fontButton.addActionListener(new FontChanger());
        displayOptions.add(fontButton);
        displayPanel.add(displayOptions, BorderLayout.SOUTH);
        return displayPanel;
    }


    //////////////////////////////////////////////////////////////////////////////////////
    // UI components and layout for lower part of GUI, where simulated keyboard is located.
    private JComponent buildKeyboard() {
        keyboardPanel = new JPanel(new BorderLayout());
        keyEventAccepter = new JTextArea();
        keyEventAccepter.setEditable(true);
        keyEventAccepter.setFont(defaultFont);
        keyEventAccepter.setMargin(textAreaInsets);
        keyAccepterScrollPane = new JScrollPane(keyEventAccepter);
        keyAccepterScrollPane.setPreferredSize(preferredTextAreaDimension);
        keyEventAccepter.addKeyListener(new KeyboardKeyListener());
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

    ///////////////////////////////////////////////////////////////////////////////////
    //
    //  Class to grab keystrokes going to keyboard echo area and send them to MMIO area
    //

    private class KeyboardKeyListener implements KeyListener {
        public void keyTyped(KeyEvent e) {
            int updatedReceiverControl = readyBitSet(RECEIVER_CONTROL);
            updateMMIOControlAndData(RECEIVER_CONTROL, updatedReceiverControl, RECEIVER_DATA, e.getKeyChar() & 0x00000ff);
            if (updatedReceiverControl != 1) {
                InterruptController.registerExternalInterrupt(EXTERNAL_INTERRUPT_KEYBOARD);
            }
        }


        /* Ignore key pressed event from the text field. */
        public void keyPressed(KeyEvent e) {
        }

        /* Ignore key released event from the text field. */
        public void keyReleased(KeyEvent e) {
        }
    }

    /**
     * Font dialog for the display panel
     * Almost all of the code is used from the SettingsHighlightingAction
     * class.
     */

    private class FontSettingDialog extends AbstractFontSettingDialog {
        private boolean resultOK;

        public FontSettingDialog(Frame owner, String title, Font currentFont) {
            super(owner, title, true, currentFont);
        }

        private Font showDialog() {
            resultOK = true;
            // Because dialog is modal, this blocks until user terminates the dialog.
            this.setVisible(true);
            return resultOK ? getFont() : null;
        }

        protected void closeDialog() {
            this.setVisible(false);
            // Update display text dimensions based on current font and size. DPS 22-July-2014
            updateDisplayBorder.componentResized(null);
        }

        private void performCancel() {
            resultOK = false;
        }

        // Control buttons for the dialog.
        protected Component buildControlPanel() {
            Box controlPanel = Box.createHorizontalBox();
            JButton okButton = new JButton("OK");
            okButton.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            apply(getFont());
                            closeDialog();
                        }
                    });
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            performCancel();
                            closeDialog();
                        }
                    });
            JButton resetButton = new JButton("Reset");
            resetButton.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            reset();
                        }
                    });
            controlPanel.add(Box.createHorizontalGlue());
            controlPanel.add(okButton);
            controlPanel.add(Box.createHorizontalGlue());
            controlPanel.add(cancelButton);
            controlPanel.add(Box.createHorizontalGlue());
            controlPanel.add(resetButton);
            controlPanel.add(Box.createHorizontalGlue());
            return controlPanel;
        }

        // Change the font for the keyboard and display
        protected void apply(Font font) {
            display.setFont(font);
            keyEventAccepter.setFont(font);
        }

    }

    private class FontChanger implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            FontSettingDialog fontDialog = new FontSettingDialog(null, "Select Text Font", display.getFont());
            Font newFont = fontDialog.showDialog();
        }
    }


}