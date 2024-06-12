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
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.HashMap;
import java.util.Enumeration;
import java.util.Hashtable;


public class PokemonGraphics extends AbstractToolAndApplication {

    private static String version = "Version 1.0 (Susana Figueroa)";
    private static String heading = "Pokemon TM";
    private static String displayPanelTitle, infoPanelTitle;
    private static char VT_FILL = ' ';  // fill character for virtual terminal (random access mode)

    public static Dimension preferredTextAreaDimension = new Dimension(1000, 250);
    private static Insets textAreaInsets = new Insets(4, 4, 4, 4);

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
    private JPanel displayPanel;
    private JPanel logPanel;
    private JScrollPane logAccepterScrollPane;
    private JTextArea logDisplay;
    private JButton fontButton;
    private Font defaultFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    public HashMap<Integer, String> pokemonNames = new HashMap<Integer, String>();
    public HashMap<Integer, String> pokemonTypes = new HashMap<Integer, String>();
    public HashMap<Integer, String> pokemonStatus = new HashMap<Integer, String>();

    public static int POKEMON_1_STATUS;     // Registro de estado del primer pokemon
    public static int POKEMON_1_COMMAND;    // Registro de comandos del primer pokemon
    public static int POKEMON_2_STATUS;     // Registro de estado del segundo pokemon
    public static int POKEMON_2_COMMAND;    // Registro de comandos del segundo pokemon

    private int[] atkInfo = new int[]{1, 0, 5, 0, 0, 21, 21, 11, 8, 13, 13, 11};
    private int[] defInfo = new int[]{4, 1, 5, 0, 0, 20, 20, 12, 7, 10, 11, 13};

    private int intCommand;                 // Numero del comando a ejecutar
    private int intStatus;                  // Numero del estado
    
    String pathPokemons = "./src/images/pokemon/";
    private int poke_def_x = 165;
    private int poke_def_y = 35;
    private int poke_atk_x = 30;
    private int poke_atk_y = 95;

    public PokemonGraphics(String title, String heading) {
        super(title, heading);
        simulator = this;
    }

    public PokemonGraphics() {
        this(heading + ", " + version, heading);
    }

    public static void main(String[] args) {
        new PokemonGraphics(heading + " stand-alone, " + version, heading).go();
    }

    @Override
    public String getName() {
        return "Pokemon TM";
    }

    private void initializePokemon(){
        pokemonTypes.put(0, "Planta");
        pokemonTypes.put(1, "Fuego");
        pokemonTypes.put(2, "Agua");

        pokemonStatus.put(0, "Saludable");
        pokemonStatus.put(1, "Envenenado");
        pokemonStatus.put(2, "Dormido");
        pokemonStatus.put(3, "Desmayado");

        pokemonNames.put(1, "Bulbasaur");
        pokemonNames.put(2, "Ivysaur");
        pokemonNames.put(3, "Venusaur");
        pokemonNames.put(4, "Charmander");
        pokemonNames.put(5, "Charmeleon");
        pokemonNames.put(6, "Charizard");
        pokemonNames.put(7, "Squirtle");
        pokemonNames.put(8, "Wartortle");
        pokemonNames.put(9, "Blastoise");
        pokemonNames.put(10, "Caterpie");
        pokemonNames.put(11, "Metapod");
        pokemonNames.put(12, "Butterfree"); 
        pokemonNames.put(13, "Weedle"); 
        pokemonNames.put(14, "Kakuna"); 
        pokemonNames.put(15, "Beedrill"); 
        pokemonNames.put(16, "Pidgey"); 
        pokemonNames.put(17, "Pidgeotto"); 
        pokemonNames.put(18, "Pidgeot"); 
        pokemonNames.put(19, "Rattata"); 
        pokemonNames.put(20, "Raticate"); 
        pokemonNames.put(21, "Spearow"); 
        pokemonNames.put(22, "Fearow"); 
        pokemonNames.put(23, "Ekans"); 
        pokemonNames.put(24, "Arbok"); 
        pokemonNames.put(25, "Pikachu"); 
        pokemonNames.put(26, "Raichu"); 
        pokemonNames.put(27, "Sandshrew"); 
        pokemonNames.put(28, "Sandslash"); 
        pokemonNames.put(29, "Nidoran"); 
        pokemonNames.put(30, "Nidorina"); 
        pokemonNames.put(31, "Nidoqueen"); 
        pokemonNames.put(32, "Nidoran"); 
        pokemonNames.put(33, "Nidorino"); 
        pokemonNames.put(34, "Nidoking"); 
        pokemonNames.put(35, "Clefairy"); 
        pokemonNames.put(36, "Clefable");
        pokemonNames.put(37, "Vulpix"); 
        pokemonNames.put(38, "Ninetales");
        pokemonNames.put(39, "Jigglypuff");
        pokemonNames.put(40, "Wigglytuff");
        pokemonNames.put(41, "Zubat");
        pokemonNames.put(42, "Golbat");
        pokemonNames.put(43, "Oddish");
        pokemonNames.put(44, "Gloom");
        pokemonNames.put(45, "Vileplume");
        pokemonNames.put(46, "Paras");
        pokemonNames.put(47, "Parasect");
        pokemonNames.put(48, "Venonat");
        pokemonNames.put(49, "Venomoth");
        pokemonNames.put(50, "Diglett");
        pokemonNames.put(51, "Dugtrio");
        pokemonNames.put(52, "Meowth");
        pokemonNames.put(53, "Persian");
        pokemonNames.put(54, "Psyduck");
        pokemonNames.put(55, "Golduck");
        pokemonNames.put(56, "Mankey");
        pokemonNames.put(57, "Primeape");
        pokemonNames.put(58, "Growlithe");
        pokemonNames.put(59, "Arcanine");
        pokemonNames.put(60, "Poliway");
        pokemonNames.put(61, "Poliwhirl");
        pokemonNames.put(62, "Poliwrath");
        pokemonNames.put(63, "Abra");
        pokemonNames.put(64, "Kadabra");
        pokemonNames.put(65, "Alakazam");
        pokemonNames.put(66, "Machop");
        pokemonNames.put(67, "Machoke");
        pokemonNames.put(68, "Machamp");
        pokemonNames.put(69, "Bellsprout");
        pokemonNames.put(70, "Weepinbell");
        pokemonNames.put(71, "Victreebel");
        pokemonNames.put(72, "Tentacool");
        pokemonNames.put(73, "Tentacruel");
        pokemonNames.put(74, "Geodude");
        pokemonNames.put(75, "Graveler");
        pokemonNames.put(76, "Golem");
        pokemonNames.put(77, "Ponyta");
        pokemonNames.put(78, "Rapidash");
        pokemonNames.put(79, "Slowpoke");
        pokemonNames.put(80, "Slowbro");
        pokemonNames.put(81, "Magnemite");
        pokemonNames.put(82, "Magneton");
        pokemonNames.put(83, "Farfetch'd");
        pokemonNames.put(84, "Doduo");
        pokemonNames.put(85, "Dodrio");
        pokemonNames.put(86, "Seel");
        pokemonNames.put(87, "Dewgong");
        pokemonNames.put(88, "Grimer");
        pokemonNames.put(89, "Muk");
        pokemonNames.put(90, "Shellder");
        pokemonNames.put(91, "Cloyster");
        pokemonNames.put(92, "Gastly");
        pokemonNames.put(93, "Haunter");
        pokemonNames.put(94, "Gengar");
        pokemonNames.put(95, "Onix");
        pokemonNames.put(96, "Drowzee");
        pokemonNames.put(97, "Hypno");
        pokemonNames.put(98, "Krabby");
        pokemonNames.put(99, "Kingler");
        pokemonNames.put(100, "Voltorb");
    }

    // Se definen los valores de los registros
    protected void initializePreGUI() {
        POKEMON_1_STATUS = Memory.memoryMapBaseAddress; //0xffff0000; // keyboard Ready in low-order bit
        POKEMON_1_COMMAND = Memory.memoryMapBaseAddress + 4; //0xffff0004; // keyboard character in low-order byte
        POKEMON_2_STATUS = Memory.memoryMapBaseAddress + 8; //0xffff0008; // display Ready in low-order bit
        POKEMON_2_COMMAND = Memory.memoryMapBaseAddress + 12; //0xffff000c; // display character in low-order byte
        displayPanelTitle = "DISPLAY";
        infoPanelTitle = "Combat Log";
        initializePokemon();
    }

    // Se vigilan los cambios a las direcciones de los registros
    protected void addAsObserver() {
        addAsObserver(POKEMON_1_COMMAND, POKEMON_1_COMMAND);
        addAsObserver(POKEMON_2_COMMAND, POKEMON_2_COMMAND);
        addAsObserver(POKEMON_1_STATUS, POKEMON_1_STATUS);
        addAsObserver(POKEMON_2_STATUS, POKEMON_2_STATUS);

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
        JSplitPane general = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildDisplay(), buildInfo());
        keyboardAndDisplay.add(general);
        return keyboardAndDisplay;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //  Rest of the protected methods.  These all override do-nothing methods inherited from
    //  the abstract superclass.
    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void processRISCVUpdate(Observable memory, AccessNotice accessNotice) {
        MemoryAccessNotice notice = (MemoryAccessNotice) accessNotice;
        
        if (notice.getAddress() == POKEMON_1_COMMAND && notice.getAccessType() == AccessNotice.WRITE) {
            intCommand = notice.getValue();
            getCommand(intCommand, 1);
        } else if (notice.getAddress() == POKEMON_2_COMMAND && notice.getAccessType() == AccessNotice.WRITE) {
            intCommand = notice.getValue();
            getCommand(intCommand, 2);
        } else if (notice.getAddress() == POKEMON_1_STATUS && notice.getAccessType() == AccessNotice.WRITE) {
            intStatus = notice.getValue();
            getStatus(intStatus, 1);
        } else if (notice.getAddress() == POKEMON_2_STATUS && notice.getAccessType() == AccessNotice.WRITE) {
            intStatus = notice.getValue();
            getStatus(intStatus, 2);
        }
    }

    private void getCommand(int intCommand, int intPokemon) {
        int command = (int) (intCommand & 0x000000FF);
        if (command == 1){
            logDisplay.append("El pokemon " + intPokemon + " ha usado su ataque " + command + "\n");
        } else if (command == 2){
            logDisplay.append("El pokemon " + intPokemon + " ha usado su ataque " + command + "\n");
        } else if (command == 3){
            logDisplay.append("El pokemon " + intPokemon + " ha usado su ataque " + command + "\n");
        } else if (command == 4){
            logDisplay.append("El pokemon " + intPokemon + " ha usado su ataque " + command + "\n");
        }
    }

    private void getStatus(int intStatus, int intPokemon) {
        int status = (int) (intStatus & 0x000000FF);
        int id = (int) (intStatus & 0x0000FF00) >> 8;
        System.out.println("Pokemon " + intPokemon + " id: " + id);
        String nombre = pokemonNames.get(id);
        if (id > 0 && intPokemon == 1 && atkInfo[0] != id){
            atkInfo[0] = id;
            logDisplay.append("Enhorabuena! " + nombre + " ha evolucionado!\n");
            refreshDisplay();
        } else if (id > 0 && intPokemon == 2 && defInfo[0] != id){
            defInfo[0] = id;
            logDisplay.append("Enhorabuena! " + nombre + " ha evolucionado!\n");
            refreshDisplay();
        }
        if (status == 1){
            logDisplay.append("Oh no! " + nombre + " esta envenando!\n");
        } else if (status == 2){
            logDisplay.append("Oh no! " + nombre + " esta dormido!\n");
        } else if (status == 3){
            logDisplay.append("Oh no! " + nombre + " esta se ha desmayado!\n");
        }
    }

    @Override
    protected void initializePostGUI() {
        logDisplay.requestFocusInWindow();
    }

    /**
     * Method to reset counters and display when the Reset button selected.
     * Overrides inherited method that does nothing.
     */
    protected void reset() {
        displayRandomAccessMode = false;
        logDisplay.setText("Se ha iniciado una nueva batalla pokemon\n");
        ((TitledBorder) displayPanel.getBorder()).setTitle(displayPanelTitle);
        refreshDisplay();
        logDisplay.requestFocusInWindow();
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
        }
    }

    @Override
    protected JComponent getHelpComponent() {
        final String helpContent =
                "Pokemon TM\n\n" +
                "Simulador de batalla Pokemon\n" + 
                "Sigue siendo un prototipo!";
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
                        final String title = "Pokemon TM";
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

    private BufferedImage mergeImages(){
        BufferedImage combined;
        try {
            String idAtk = "back/" + String.format("%03d", atkInfo[0]) + ".png";
            String idDef = "front/" + String.format("%03d", defInfo[0]) + ".png";
            BufferedImage background = ImageIO.read(new File(pathPokemons, "grass_background.png"));
            BufferedImage pokemon_atacante = ImageIO.read(new File(pathPokemons, idAtk));
            BufferedImage pokemon_defensor = ImageIO.read(new File(pathPokemons, idDef));

            // create the new image, canvas size is the max. of both image sizes
            int w = background.getWidth();
            int h = background.getHeight();
            combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

            // paint both images, preserving the alpha channels
            Graphics g = combined.getGraphics();
            g.drawImage(background, 0, 0, null);
            g.drawImage(pokemon_defensor, poke_def_x, poke_def_y, null);
            g.drawImage(pokemon_atacante, poke_atk_x, poke_atk_y, null);
            g.dispose();
            // // Save as new image
            ImageIO.write(combined, "PNG", new File(pathPokemons, "temp.png"));
        } catch (IOException ex) {
            combined = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        }
        return combined;
    }

    private JLabel addImage(){
        BufferedImage image = mergeImages();
        JLabel picLabel = new JLabel(new ImageIcon(image));
        return picLabel;
    }

    private JPanel buildDisplay(){
        displayPanel = new JPanel(new FlowLayout());
        TitledBorder tb = new TitledBorder(displayPanelTitle);
        tb.setTitleJustification(TitledBorder.CENTER);
        displayPanel.setBorder(tb);
        displayPanel.add(infoAtkPokemon(), BorderLayout.WEST);
        displayPanel.add(addImage(), BorderLayout.CENTER);
        displayPanel.add(infoDefPokemon(), BorderLayout.EAST);
        return displayPanel;
    }

    private JPanel infoAtkPokemon(){
        JPanel infoPanel = new JPanel(new GridLayout(12,1));
        String nombre = pokemonNames.get(atkInfo[0]);
        infoPanel.add(new JLabel("Info " + nombre));
        infoPanel.add(new JLabel("Id :" + atkInfo[0]));
        infoPanel.add(new JLabel("Tipo: " + pokemonTypes.get(atkInfo[1])));
        infoPanel.add(new JLabel("Nivel: " + atkInfo[2]));
        infoPanel.add(new JLabel("Exp: " + atkInfo[3]));
        infoPanel.add(new JLabel("Estado: " + pokemonStatus.get(atkInfo[4])));
        infoPanel.add(new JLabel("Hp: " + atkInfo[5] + " / " + atkInfo[6]));
        infoPanel.add(new JLabel("Ataque Fisico: " + atkInfo[7]));
        infoPanel.add(new JLabel("Defensa Fisica: " + atkInfo[8]));
        infoPanel.add(new JLabel("Ataque Especial: " + atkInfo[9]));
        infoPanel.add(new JLabel("Defensa Especial: " + atkInfo[10]));
        infoPanel.add(new JLabel("Velocidad: " + atkInfo[11]));

        return infoPanel; 
    }

    private JPanel infoDefPokemon(){
        JPanel infoPanel = new JPanel(new GridLayout(12,1));
        String nombre = pokemonNames.get(defInfo[0]);
        infoPanel.add(new JLabel("Info " + nombre));
        infoPanel.add(new JLabel("Id :" + defInfo[0]));
        infoPanel.add(new JLabel("Tipo: " + pokemonTypes.get(defInfo[1])));
        infoPanel.add(new JLabel("Nivel: " + defInfo[2]));
        infoPanel.add(new JLabel("Exp: " + defInfo[3]));
        infoPanel.add(new JLabel("Estado: " + pokemonStatus.get(defInfo[4])));
        infoPanel.add(new JLabel("Hp: " + defInfo[5] + " / " + defInfo[6]));
        infoPanel.add(new JLabel("Ataque Fisico: " + defInfo[7]));
        infoPanel.add(new JLabel("Defensa Fisica: " + defInfo[8]));
        infoPanel.add(new JLabel("Ataque Especial: " + defInfo[9]));
        infoPanel.add(new JLabel("Defensa Especial: " + defInfo[10]));
        infoPanel.add(new JLabel("Velocidad: " + defInfo[11]));

        return infoPanel; 
    }

    private void refreshDisplay(){
        displayPanel.removeAll();
        displayPanel.add(infoAtkPokemon(), BorderLayout.WEST);
        displayPanel.add(addImage(), BorderLayout.CENTER);
        displayPanel.add(infoDefPokemon(), BorderLayout.EAST);
        displayPanel.revalidate();
        displayPanel.repaint();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // UI components and layout for lower part of GUI, where simulated keyboard is located.
    private JComponent buildInfo() {
        logPanel = new JPanel(new BorderLayout());
        logDisplay = new JTextArea("Se ha iniciado una nueva batalla pokemon\n");
        logDisplay.setEditable(false);
        logDisplay.setFont(defaultFont);
        logDisplay.setMargin(textAreaInsets);
        logAccepterScrollPane = new JScrollPane(logDisplay);
        logAccepterScrollPane.setPreferredSize(new Dimension(1000, 100));
        logPanel.add(logAccepterScrollPane);
        TitledBorder tb = new TitledBorder(infoPanelTitle);
        tb.setTitleJustification(TitledBorder.CENTER);
        logPanel.setBorder(tb);
        return logPanel;
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
}