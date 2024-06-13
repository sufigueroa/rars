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

    public static int BATTLE_STATUS;     // Registro de estado del primer pokemon
    public static int POKEMON_1_STATUS;     // Registro de estado del primer pokemon
    public static int POKEMON_1_COMMAND;    // Registro de comandos del primer pokemon
    public static int POKEMON_2_STATUS;     // Registro de estado del segundo pokemon
    public static int POKEMON_2_COMMAND;    // Registro de comandos del segundo pokemon

    private int[] atkInfo = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] defInfo = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private int intCommand;                 // Numero del comando a ejecutar
    private int intStatus;                  // Numero del estado
    private int battleInitialize = 0;           // La batalla esta inicializada?
    
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
        pokemonTypes.put(0, "Normal");
        pokemonTypes.put(1, "Fuego");
        pokemonTypes.put(2, "Agua");
        pokemonTypes.put(3, "Electrico");
        pokemonTypes.put(4, "Planta");
        pokemonTypes.put(5, "Hielo");
        pokemonTypes.put(6, "Pelea");
        pokemonTypes.put(7, "Veneno");
        pokemonTypes.put(8, "Bicho");
        pokemonTypes.put(9, "Dragon");
        pokemonTypes.put(10, "Volador");
        pokemonTypes.put(11, "Tierra");
        pokemonTypes.put(12, "Roca");
        pokemonTypes.put(13, "Fantasma");
        pokemonTypes.put(14, "Psiquico");

        pokemonStatus.put(0, "Saludable");
        pokemonStatus.put(1, "Envenenado");
        pokemonStatus.put(2, "Dormido");
        pokemonStatus.put(3, "Paralizado");
        pokemonStatus.put(4, "Quemado");
        pokemonStatus.put(5, "Congelado");
        pokemonStatus.put(6, "Confundido");
        pokemonStatus.put(7, "Desmayado");

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
        pokemonNames.put(101, "Electrode");
        pokemonNames.put(102, "Exeggcute");
        pokemonNames.put(103, "Exeggutor");
        pokemonNames.put(104, "Cubone");
        pokemonNames.put(105, "Marowak");
        pokemonNames.put(106, "Hitmonlee");
        pokemonNames.put(107, "Hitmonchan");
        pokemonNames.put(108, "Lickitung");
        pokemonNames.put(109, "Koffing");
        pokemonNames.put(110, "Weezing");
        pokemonNames.put(111, "Rhyhorn");
        pokemonNames.put(112, "Rhydon");
        pokemonNames.put(113, "Chansey");
        pokemonNames.put(114, "Tangela");
        pokemonNames.put(115, "Kangaskhan");
        pokemonNames.put(116, "Horsea");
        pokemonNames.put(117, "Seadra");
        pokemonNames.put(118, "Goldeen");
        pokemonNames.put(119, "Seaking");
        pokemonNames.put(120, "Staryu");
        pokemonNames.put(121, "Starmie");
        pokemonNames.put(122, "Mr.Mime");
        pokemonNames.put(123, "Scyther");
        pokemonNames.put(124, "Jynx");
        pokemonNames.put(125, "Electabuzz");
        pokemonNames.put(126, "Magmar");
        pokemonNames.put(127, "Pinsir");
        pokemonNames.put(128, "Tauros");
        pokemonNames.put(129, "Magikarp");
        pokemonNames.put(130, "Gyarados");
        pokemonNames.put(131, "Lapras");
        pokemonNames.put(132, "Ditto");
        pokemonNames.put(133, "Eevee");
        pokemonNames.put(134, "Vaporeon");
        pokemonNames.put(135, "Jolteon");
        pokemonNames.put(136, "Flareon");
        pokemonNames.put(137, "Porygon");
        pokemonNames.put(138, "Omanyte");
        pokemonNames.put(139, "Omastar");
        pokemonNames.put(140, "Kabuto");
        pokemonNames.put(141, "Kabutops");
        pokemonNames.put(142, "Aerodactyl");
        pokemonNames.put(143, "Snorlax");
        pokemonNames.put(144, "Articuno");
        pokemonNames.put(145, "Zapdos");
        pokemonNames.put(146, "Moltres");
        pokemonNames.put(147, "Dratini");
        pokemonNames.put(148, "Dragonair");
        pokemonNames.put(149, "Dragonite");
        pokemonNames.put(150, "Mewtwo");
        pokemonNames.put(151, "Mew");
    }

    // Se definen los valores de los registros
    protected void initializePreGUI() {
        BATTLE_STATUS = Memory.memoryMapBaseAddress; //0xffff0000; // keyboard Ready in low-order bit
        POKEMON_1_STATUS = Memory.memoryMapBaseAddress + 4; //0xffff0000; // keyboard Ready in low-order bit
        POKEMON_1_COMMAND = Memory.memoryMapBaseAddress + 8; //0xffff0004; // keyboard character in low-order byte
        POKEMON_2_STATUS = Memory.memoryMapBaseAddress + 12; //0xffff0008; // display Ready in low-order bit
        POKEMON_2_COMMAND = Memory.memoryMapBaseAddress + 16; //0xffff000c; // display character in low-order byte
        displayPanelTitle = "DISPLAY";
        infoPanelTitle = "Combat Log";
        initializePokemon();
    }

    // Se vigilan los cambios a las direcciones de los registros
    protected void addAsObserver() {
        addAsObserver(BATTLE_STATUS, BATTLE_STATUS);
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

        if (notice.getAddress() == BATTLE_STATUS && notice.getAccessType() == AccessNotice.WRITE) {
            intCommand = notice.getValue();
            if ((intCommand & 0x00000001) == 1){
                battleInitialize = 1;
                refreshDisplay();
            }
        } else if (notice.getAddress() == POKEMON_1_COMMAND && notice.getAccessType() == AccessNotice.WRITE) {
            intCommand = notice.getValue();
            getCommand(intCommand, 1);
        } else if (notice.getAddress() == POKEMON_2_COMMAND && notice.getAccessType() == AccessNotice.WRITE) {
            intCommand = notice.getValue();
            getCommand(intCommand, 2);
        } else if (notice.getAddress() == POKEMON_1_STATUS && notice.getAccessType() == AccessNotice.WRITE) {
            intStatus = notice.getValue();
            setPokemon(intStatus, 1);
    
            int command = (int) (intStatus & 0xF0000000) >> 28;
            if (command == 1){
                setExperience(intStatus, 1);
            } else if (command == 2){
                setHealth(intStatus, 1);
            }
        } else if (notice.getAddress() == POKEMON_2_STATUS && notice.getAccessType() == AccessNotice.WRITE) {
            intStatus = notice.getValue();
            setPokemon(intStatus, 2);

            int command = (int) (intStatus & 0xF0000000) >> 28;
            if (command == 1){
                setExperience(intStatus, 2);
            } else if (command == 2){
                setHealth(intStatus, 2);
            }
        }
    }

    private void setPokemon(int address, int intPokemon) {
        int[] pokemon;
        if (intPokemon == 1){
            pokemon = atkInfo;
        } else {
            pokemon = defInfo;
        }
        try {
            pokemon[0] = Globals.memory.getWordNoNotify(address);
            pokemon[1] = Globals.memory.getWordNoNotify(address + 4);
            pokemon[2] = Globals.memory.getWordNoNotify(address + 8);
            pokemon[3] = Globals.memory.getWordNoNotify(address + 12);
            pokemon[4] = Globals.memory.getWordNoNotify(address + 16);
            pokemon[5] = Globals.memory.getWordNoNotify(address + 20);
            pokemon[6] = Globals.memory.getWordNoNotify(address + 24);
            pokemon[7] = Globals.memory.getWordNoNotify(address + 28);
            pokemon[8] = Globals.memory.getWordNoNotify(address + 32);
            pokemon[9] = Globals.memory.getWordNoNotify(address + 36);
            pokemon[10] = Globals.memory.getWordNoNotify(address + 40);
            pokemon[11] = Globals.memory.getWordNoNotify(address + 44);
            refreshDisplay();
        } catch (AddressErrorException aee) {
            System.out.println("Tool author specified incorrect MMIO address!" + aee);
            System.exit(0);
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

    private void setExperience(int intExp, int intPokemon) {
        int id      = (int) (intExp & 0x03FC0000) >> 18;
        int type    = (int) (intExp & 0x0003C000) >> 14;
        int level   = (int) (intExp & 0x00003F80) >> 7;
        int exp     = (int) (intExp & 0x0000007F) >> 0;
        String nombre = pokemonNames.get(id);

        int[] pokemon;
        if (id < 1){
            return;
        }
        if (intPokemon == 1){
            pokemon = atkInfo;
        } else {
            pokemon = defInfo;
        }
        
        if (pokemon[2] != level){
            logDisplay.append("Enhorabuena! " + pokemonNames.get(pokemon[0]) + " ha subido de nivel!\n");
        }
        if (pokemon[0] != id){
            logDisplay.append("Enhorabuena! " + pokemonNames.get(pokemon[0]) + " ha evolucionado a " + nombre + "!\n");
        }

        pokemon[0] = id;
        pokemon[1] = type;
        pokemon[2] = level;
        pokemon[3] = exp;
        refreshDisplay();
    }

    private void setHealth(int intHealth, int intPokemon) {
        int status = (int) (intHealth & 0x00000700) >> 8;
        int hp = (int) (intHealth & 0x000000FF) >> 0;

        int[] pokemon;
        if (intPokemon == 1){
            pokemon = atkInfo;
        } else {
            pokemon = defInfo;
        }
        if (pokemon[0] < 1){
            return;
        }
        
        String nombre = pokemonNames.get(pokemon[0]);

        if (pokemon[4] != status){
            if (status == 1){
                logDisplay.append("Nooooo! " + pokemonNames.get(pokemon[0]) + " ha sido envenenado!\n");
            } else if (status == 2){
                logDisplay.append("ZzzZZzzz... " + pokemonNames.get(pokemon[0]) + " se ha quedado dormido!\n");
            } if (status == 3){
                logDisplay.append("OHHHH! " + pokemonNames.get(pokemon[0]) + " se ha desmayado!\n");
            }
        } else if (pokemon[4] == 1 && pokemon[5] > hp){
            logDisplay.append("Oh no! " + pokemonNames.get(pokemon[0]) + " ha perdido vida por envenenamiento!\n");
        }

        pokemon[4] = status;
        pokemon[5] = hp;
        refreshDisplay();
    }

    private void setStats(int intStats, int intPokemon) {
        int atkPhy = (int) (intStatus & 0x1FE00000) >> 24;
        int defPhy = (int) (intStatus & 0x001FE000) >> 21;
        int atkSpe = (int) (intStatus & 0x00001FE0) >> 14;
        int defSpe = (int) (intStatus & 0x00000000) >> 8;
        int vel = (int) (intStatus & 0x00000000) >> 6;
    }

    private void getStatus(int intStatus, int intPokemon) {
        // int id = (int) (intStatus & 0xFE000000) >> 25;
        // int tipo = (int) (intStatus & 0x01C00000) >> 22;
        // int nivel = (int) (intStatus & 0x003F8000) >> 15;
        // int exp = (int) (intStatus & 0x00007F00) >> 8;
        // int estado = (int) (intStatus & 0x000000C0) >> 6;
        // int hp = (int) (intStatus & 0x00000000) >> 12;
        // int hpTotal = (int) (intStatus & 0x00000000) >> 9;

        int id = (int) (intStatus & 0x7F000000) >> 24;
        int tipo = (int) (intStatus & 0x00E00000) >> 21;
        int nivel = (int) (intStatus & 0x001FC000) >> 14;
        int exp = (int) (intStatus & 0x00003F80) >> 8;
        int estado = (int) (intStatus & 0x000000C0) >> 6;
        int hp = (int) (intStatus & 0x0000003F) >> 0;

        System.out.println("Pokemon " + intPokemon + " id: " + id);
        String nombre = pokemonNames.get(id);
        if (id > 0 && intPokemon == 1 && atkInfo[0] != id){
            atkInfo[0] = id;
            atkInfo[1] = tipo;
            atkInfo[2] = nivel;
            atkInfo[3] = exp;
            atkInfo[4] = estado;
            atkInfo[5] = hp;
            atkInfo[6] = hp;
            logDisplay.append("Enhorabuena! " + nombre + " ha evolucionado!\n");
            refreshDisplay();
        } else if (id > 0 && intPokemon == 2 && defInfo[0] != id){
            defInfo[0] = id;
            defInfo[1] = tipo;
            defInfo[2] = nivel;
            defInfo[3] = exp;
            defInfo[4] = estado;
            defInfo[5] = hp;
            defInfo[6] = hp;
            logDisplay.append("Enhorabuena! " + nombre + " ha evolucionado!\n");
            refreshDisplay();
        }
        if (estado == 1){
            logDisplay.append("Oh no! " + nombre + " esta envenando!\n");
        } else if (estado == 2){
            logDisplay.append("Oh no! " + nombre + " esta dormido!\n");
        } else if (estado == 3){
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
        battleInitialize = 0;
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
            BufferedImage background = ImageIO.read(new File(pathPokemons, "grass_background.png"));

            // create the new image, canvas size is the max. of both image sizes
            int w = background.getWidth();
            int h = background.getHeight();
            combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

            // paint both images, preserving the alpha channels
            Graphics g = combined.getGraphics();
            g.drawImage(background, 0, 0, null);
            if (battleInitialize > 0 && atkInfo[0] > 0){
                String idAtk = "back/" + String.format("%03d", atkInfo[0]) + ".png";
                BufferedImage pokemon_atacante = ImageIO.read(new File(pathPokemons, idAtk));
                g.drawImage(pokemon_atacante, poke_atk_x, poke_atk_y, null);
            }
            if (battleInitialize > 0 && defInfo[0] > 0){
                String idDef = "front/" + String.format("%03d", defInfo[0]) + ".png";
                BufferedImage pokemon_defensor = ImageIO.read(new File(pathPokemons, idDef));
                g.drawImage(pokemon_defensor, poke_def_x, poke_def_y, null);
            }
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
        refreshDisplay();
        return displayPanel;
    }

    private JPanel infoPokemonEmpty(){
        JPanel infoPanel = new JPanel(new GridLayout(13,1));
        JLabel nameLabel = new JLabel(" ");
        nameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        infoPanel.add(nameLabel);
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel(" "));

        return infoPanel; 
    }

    private JPanel infoPokemon(int[] pokemon){
        JPanel infoPanel = new JPanel(new GridLayout(13,1));
        String nombre = "";
        if (pokemon[0] > 0){
            nombre = pokemonNames.get(pokemon[0]);
        }
        JLabel nameLabel = new JLabel("Info " + nombre);
        nameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        infoPanel.add(nameLabel);
        infoPanel.add(new JLabel("Id :" + pokemon[0]));
        infoPanel.add(new JLabel("Tipo: " + pokemonTypes.get(pokemon[1])));
        infoPanel.add(new JLabel("Nivel: " + pokemon[2]));
        infoPanel.add(new JLabel("Exp: " + pokemon[3]));
        infoPanel.add(new JLabel("Estado: " + pokemonStatus.get(pokemon[4])));
        infoPanel.add(new JLabel("Hp: " + pokemon[5] + " / " + pokemon[6]));
        infoPanel.add(new JLabel("Ataque Fisico: " + pokemon[7]));
        infoPanel.add(new JLabel("Defensa Fisica: " + pokemon[8]));
        infoPanel.add(new JLabel("Ataque Especial: " + pokemon[9]));
        infoPanel.add(new JLabel("Defensa Especial: " + pokemon[10]));
        infoPanel.add(new JLabel("Velocidad: " + pokemon[11]));

        return infoPanel; 
    }

    private void refreshDisplay(){
        displayPanel.removeAll();
        if (battleInitialize == 1){
            displayPanel.add(infoPokemon(atkInfo), BorderLayout.WEST);
        } else {
            displayPanel.add(infoPokemonEmpty(), BorderLayout.WEST);
        }
        displayPanel.add(Box.createHorizontalStrut(50));
        displayPanel.add(addImage(), BorderLayout.CENTER);
        displayPanel.add(Box.createHorizontalStrut(50));
        if (battleInitialize == 1){
            displayPanel.add(infoPokemon(defInfo), BorderLayout.EAST);
        } else {
            displayPanel.add(infoPokemonEmpty(), BorderLayout.WEST);
        }
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
        logAccepterScrollPane.setPreferredSize(new Dimension(700, 100));
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