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


class PrettyJLabel extends JLabel{
    public PrettyJLabel(String string, int fontSize){
        super(string);
        this.setFont(new Font(Font.MONOSPACED, Font.BOLD, fontSize));
    }
}


public class PokemonGraphics extends AbstractToolAndApplication {

    private static String version = "Version 1.0 (Susana Figueroa)";
    private static String heading = "Pokemon TM";
    private static String displayPanelTitle, infoPanelTitle;
    private static char VT_FILL = ' ';  // fill character for virtual terminal (random access mode)

    private static Insets textAreaInsets = new Insets(4, 4, 4, 4);

    // Whether or not display position is sequential (JTextArea append)
    // or random access (row, column).  Supports new random access feature. DPS 17-July-2014
    private boolean displayRandomAccessMode = false;
    private int rows, columns;
    private DisplayResizeAdapter updateDisplayBorder;
    private PokemonGraphics simulator;

    // Major GUI components
    private JPanel pokemonDisplay;
    private JScrollPane displayScrollPane;
    private JTextArea display;
    private JPanel displayPanel;
    private JPanel logPanel;
    private JScrollPane logAccepterScrollPane;
    private JTextArea logDisplay;
    private JButton fontButton;
    private Font defaultFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    JPanel infoLeftPanel;
    JPanel infoRightPanel;
    JPanel infoDisplayPanel;
    JLabel picLabel;

    public HashMap<Integer, String> pokemonNames = new HashMap<Integer, String>();
    public HashMap<Integer, String> pokemonTypes = new HashMap<Integer, String>();
    public HashMap<Integer, String> pokemonStatus = new HashMap<Integer, String>();
    public HashMap<Integer, String> pokemonBackgrounds = new HashMap<Integer, String>();
    public HashMap<Integer, String> pokemonWeather = new HashMap<Integer, String>();
    public HashMap<Integer, String> pokemonMoves = new HashMap<Integer, String>();

    public static int BATTLE_STATUS;        // Registro de estado de la batalla
    public static int BATTLE_COMMAND;       // Registro de comando de la batalla
    public static int POKEMON_1_STATUS;     // Registro de estado del primer pokemon
    public static int POKEMON_1_COMMAND;    // Registro de comandos del primer pokemon
    public static int POKEMON_2_STATUS;     // Registro de estado del segundo pokemon
    public static int POKEMON_2_COMMAND;    // Registro de comandos del segundo pokemon

    private int[] atkInfo = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[][] atkMoves = new int[4][6];
    private int[] defInfo = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[][] defMoves = new int[4][6];

    // Battle information
    private int idBattle = 0;
    private int idTurn = 0;
    private int idBackground = 1;
    private int idWeather = 0;
    private int durationWeather = 0;
    private int battleInitialize = 0;           // La batalla esta inicializada?
    private int battleFinished = 0;           // La batalla esta inicializada?
    private int winner = 0;           // La batalla esta inicializada?

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
        pokemonBackgrounds.put(0, "No hay fondo");
        pokemonBackgrounds.put(1, "Pastizal");
        pokemonBackgrounds.put(2, "Pasto");
        pokemonBackgrounds.put(3, "Bosque");
        pokemonBackgrounds.put(4, "Agua");
        pokemonBackgrounds.put(5, "Tierra");
        pokemonBackgrounds.put(6, "Roca");
        pokemonBackgrounds.put(7, "Dojo");
        pokemonBackgrounds.put(8, "Psiquico");
        pokemonBackgrounds.put(9, "Cueva");
        pokemonBackgrounds.put(10, "Playa");
        pokemonBackgrounds.put(11, "Fantasma");
        
        pokemonWeather.put(0, " ");
        pokemonWeather.put(1, "Soleado");
        pokemonWeather.put(2, "Lluvioso");
        pokemonWeather.put(3, "Tormenta de Arena");
        pokemonWeather.put(4, "Granizo");

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
        pokemonTypes.put(15, "Oscuro");

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

        pokemonMoves.put(1, "Absorb");
        pokemonMoves.put(2, "Acid");
        pokemonMoves.put(3, "Acid Armor");
        pokemonMoves.put(4, "Agility");
        pokemonMoves.put(5, "Amnesia");
        pokemonMoves.put(6, "Aurora Beam");
        pokemonMoves.put(7, "Barrage");
        pokemonMoves.put(8, "Barrier");
        pokemonMoves.put(9, "Bide");
        pokemonMoves.put(10, "Bind");
        pokemonMoves.put(11, "Bite");
        pokemonMoves.put(12, "Blizzard");
        pokemonMoves.put(13, "Body Slam");
        pokemonMoves.put(14, "Bone Club");
        pokemonMoves.put(15, "Bonemerang");
        pokemonMoves.put(16, "Bubble");
        pokemonMoves.put(17, "Bubble Beam");
        pokemonMoves.put(18, "Clamp");
        pokemonMoves.put(19, "Comet Punch");
        pokemonMoves.put(20, "Confuse Ray");
        pokemonMoves.put(21, "Confusion");
        pokemonMoves.put(22, "Constrict");
        pokemonMoves.put(23, "Conversion");
        pokemonMoves.put(24, "Counter");
        pokemonMoves.put(25, "Crabhammer");
        pokemonMoves.put(26, "Cut");
        pokemonMoves.put(27, "Defense Curl");
        pokemonMoves.put(28, "Dig");
        pokemonMoves.put(29, "Disable");
        pokemonMoves.put(30, "Dizzy Punch");
        pokemonMoves.put(31, "Double Kick");
        pokemonMoves.put(32, "Double Slap");
        pokemonMoves.put(33, "Double Team");
        pokemonMoves.put(34, "Double-Edge");
        pokemonMoves.put(35, "Dragon Rage");
        pokemonMoves.put(36, "Dream Eater");
        pokemonMoves.put(37, "Drill Peck");
        pokemonMoves.put(38, "Earthquake");
        pokemonMoves.put(39, "Egg Bomb");
        pokemonMoves.put(40, "Ember");
        pokemonMoves.put(41, "Explosion");
        pokemonMoves.put(42, "Fire Blast");
        pokemonMoves.put(43, "Fire Punch");
        pokemonMoves.put(44, "Fire Spin");
        pokemonMoves.put(45, "Fissure");
        pokemonMoves.put(46, "Flamethrower");
        pokemonMoves.put(47, "Flash");
        pokemonMoves.put(48, "Fly");
        pokemonMoves.put(49, "Focus Energy");
        pokemonMoves.put(50, "Fury Attack");
        pokemonMoves.put(51, "Fury Swipes");
        pokemonMoves.put(52, "Glare");
        pokemonMoves.put(53, "Growl");
        pokemonMoves.put(54, "Growth");
        pokemonMoves.put(55, "Guillotine");
        pokemonMoves.put(56, "Gust");
        pokemonMoves.put(57, "Harden");
        pokemonMoves.put(58, "Haze");
        pokemonMoves.put(59, "Headbutt");
        pokemonMoves.put(60, "High Jump Kick");
        pokemonMoves.put(61, "Horn Attack");
        pokemonMoves.put(62, "Horn Drill");
        pokemonMoves.put(63, "Hydro Pump");
        pokemonMoves.put(64, "Hyper Beam");
        pokemonMoves.put(65, "Hyper Fang");
        pokemonMoves.put(66, "Hypnosis");
        pokemonMoves.put(67, "Ice Beam");
        pokemonMoves.put(68, "Ice Punch");
        pokemonMoves.put(69, "Jump Kick");
        pokemonMoves.put(70, "Karate Chop");
        pokemonMoves.put(71, "Kinesis");
        pokemonMoves.put(72, "Leech Life");
        pokemonMoves.put(73, "Leech Seed");
        pokemonMoves.put(74, "Leer");
        pokemonMoves.put(75, "Lick");
        pokemonMoves.put(76, "Light Screen");
        pokemonMoves.put(77, "Lovely Kiss");
        pokemonMoves.put(78, "Low Kick");
        pokemonMoves.put(79, "Meditate");
        pokemonMoves.put(80, "Mega Drain");
        pokemonMoves.put(81, "Mega Kick");
        pokemonMoves.put(82, "Mega Punch");
        pokemonMoves.put(83, "Metronome");
        pokemonMoves.put(84, "Mimic");
        pokemonMoves.put(85, "Minimize");
        pokemonMoves.put(86, "Mirror Move");
        pokemonMoves.put(87, "Mist");
        pokemonMoves.put(88, "Night Shade");
        pokemonMoves.put(89, "Pay Day");
        pokemonMoves.put(90, "Peck");
        pokemonMoves.put(91, "Petal Dance");
        pokemonMoves.put(92, "Pin Missile");
        pokemonMoves.put(93, "Poison Gas");
        pokemonMoves.put(94, "Poison Powder");
        pokemonMoves.put(95, "Poison Sting");
        pokemonMoves.put(96, "Pound");
        pokemonMoves.put(97, "Psybeam");
        pokemonMoves.put(98, "Psychic");
        pokemonMoves.put(99, "Psywave");
        pokemonMoves.put(100, "Quick Attack");
        pokemonMoves.put(101, "Rage");
        pokemonMoves.put(102, "Razor Leaf");
        pokemonMoves.put(103, "Razor Wind");
        pokemonMoves.put(104, "Recover");
        pokemonMoves.put(105, "Reflect");
        pokemonMoves.put(106, "Rest");
        pokemonMoves.put(107, "Roar");
        pokemonMoves.put(108, "Rock Slide");
        pokemonMoves.put(109, "Rock Throw");
        pokemonMoves.put(110, "Rolling Kick");
        pokemonMoves.put(111, "Sand Attack");
        pokemonMoves.put(112, "Scratch");
        pokemonMoves.put(113, "Screech");
        pokemonMoves.put(114, "Seismic Toss");
        pokemonMoves.put(115, "Self-Destruct");
        pokemonMoves.put(116, "Sharpen");
        pokemonMoves.put(117, "Sing");
        pokemonMoves.put(118, "Skull Bash");
        pokemonMoves.put(119, "Sky Attack");
        pokemonMoves.put(120, "Slam");
        pokemonMoves.put(121, "Slash");
        pokemonMoves.put(122, "Sleep Powder");
        pokemonMoves.put(123, "Sludge");
        pokemonMoves.put(124, "Smog");
        pokemonMoves.put(125, "Smokescreen");
        pokemonMoves.put(126, "Soft-Boiled");
        pokemonMoves.put(127, "Solar Beam");
        pokemonMoves.put(128, "Sonic Boom");
        pokemonMoves.put(129, "Spike Cannon");
        pokemonMoves.put(130, "Splash");
        pokemonMoves.put(131, "Spore");
        pokemonMoves.put(132, "Stomp");
        pokemonMoves.put(133, "Strength");
        pokemonMoves.put(134, "String Shot");
        pokemonMoves.put(135, "Struggle");
        pokemonMoves.put(136, "Stun Spore");
        pokemonMoves.put(137, "Submission");
        pokemonMoves.put(138, "Substitute");
        pokemonMoves.put(139, "Super Fang");
        pokemonMoves.put(140, "Supersonic");
        pokemonMoves.put(141, "Surf");
        pokemonMoves.put(142, "Swift");
        pokemonMoves.put(143, "Swords Dance");
        pokemonMoves.put(144, "Tackle");
        pokemonMoves.put(145, "Tail Whip");
        pokemonMoves.put(146, "Take Down");
        pokemonMoves.put(147, "Teleport");
        pokemonMoves.put(148, "Thrash");
        pokemonMoves.put(149, "Thunder");
        pokemonMoves.put(150, "Thunder Punch");
        pokemonMoves.put(151, "Thunder Shock");
        pokemonMoves.put(152, "Thunder Wave");
        pokemonMoves.put(153, "Thunderbolt");
        pokemonMoves.put(154, "Toxic");
        pokemonMoves.put(155, "Transform");
        pokemonMoves.put(156, "Tri Attack");
        pokemonMoves.put(157, "Twineedle");
        pokemonMoves.put(158, "Vine Whip");
        pokemonMoves.put(159, "Vise Grip");
        pokemonMoves.put(160, "Water Gun");
        pokemonMoves.put(161, "Waterfall");
        pokemonMoves.put(162, "Whirlwind");
        pokemonMoves.put(163, "Wing Attack");
        pokemonMoves.put(164, "Withdraw");
        pokemonMoves.put(165, "Wrap");
    }

    // Se definen los valores de los registros
    protected void initializePreGUI() {
        BATTLE_STATUS = Memory.memoryMapBaseAddress;            //0xffff0000; 
        BATTLE_COMMAND = Memory.memoryMapBaseAddress + 4;        //0xffff0004;
        POKEMON_1_STATUS = Memory.memoryMapBaseAddress + 8;     //0xffff0008;
        POKEMON_1_COMMAND = Memory.memoryMapBaseAddress + 12;   //0xffff000C;
        POKEMON_2_STATUS = Memory.memoryMapBaseAddress + 16;    //0xffff0010;
        POKEMON_2_COMMAND = Memory.memoryMapBaseAddress + 20;   //0xffff0014;
        displayPanelTitle = "DISPLAY";
        infoPanelTitle = "Combat Log";
        initializePokemon();
    }

    // Se vigilan los cambios a las direcciones de los registros
    protected void addAsObserver() {
        addAsObserver(BATTLE_COMMAND, BATTLE_COMMAND);
        addAsObserver(POKEMON_1_COMMAND, POKEMON_1_COMMAND);
        addAsObserver(POKEMON_2_COMMAND, POKEMON_2_COMMAND);
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
        pokemonDisplay = new JPanel(new BorderLayout());
        JSplitPane general = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildDisplay(), buildInfo());
        pokemonDisplay.add(general);
        return pokemonDisplay;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //  Rest of the protected methods.  These all override do-nothing methods inherited from
    //  the abstract superclass.
    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void processRISCVUpdate(Observable memory, AccessNotice accessNotice) {
        MemoryAccessNotice notice = (MemoryAccessNotice) accessNotice;

        if (notice.getAddress() == BATTLE_COMMAND && notice.getAccessType() == AccessNotice.WRITE) {
            intCommand = notice.getValue();
            int command = intCommand & 0x0000000F;
            getCommandBattle(command);
            refreshStatusBattle();
        } else if (notice.getAddress() == POKEMON_1_COMMAND && notice.getAccessType() == AccessNotice.WRITE) {
            intCommand = notice.getValue();
            int command = intCommand & 0x0000000F;
            getCommandPokemon(command, 1, POKEMON_1_STATUS, atkInfo);
        } else if (notice.getAddress() == POKEMON_2_COMMAND && notice.getAccessType() == AccessNotice.WRITE) {
            intCommand = notice.getValue();
            int command = intCommand & 0x0000000F;
            getCommandPokemon(command, 2, POKEMON_2_STATUS, defInfo);
        }
    }

    private void refreshStatusBattle(){
        int battleStatusValue = 0;
        battleStatusValue = battleStatusValue + battleInitialize;
        battleStatusValue = battleStatusValue + (battleFinished << 1);
        battleStatusValue = battleStatusValue + (winner << 2);
        battleStatusValue = battleStatusValue + (idBackground << 3);
        battleStatusValue = battleStatusValue + (idWeather << 6);
        battleStatusValue = battleStatusValue + (durationWeather << 9);
        battleStatusValue = battleStatusValue + (idTurn << 12);
        battleStatusValue = battleStatusValue + (idBattle << 18);
        setMemory(BATTLE_STATUS, battleStatusValue);
    }

    private void initializeBattle(){
        battleFinished = 0;
        winner = 0;
        idBattle = idBattle + 1;
        idTurn = idTurn + 1;
        battleInitialize = 1;
        logDisplay.append("Se ha iniciado una nueva batalla pokemon\n");
        refreshDisplay();
    }

    private void setMove(int address, int intPokemon, int numMov){
        int[][] pokeMoves;
        int[] pokemon;
        if (intPokemon == 1){
            pokeMoves = atkMoves;
            pokemon = atkInfo;
        } else {
            pokeMoves = defMoves;
            pokemon = defInfo;
        }
        if (battleInitialize == 1 && pokeMoves[numMov][0] > 0){
            String moveName = pokemonMoves.get(pokeMoves[numMov][0]);
            String pokeName = pokemonNames.get(pokemon[0]);
            logDisplay.append(pokeName + " ha olvidado " + moveName + "\n");
        }
        try {
            pokeMoves[numMov][0] = Globals.memory.getWordNoNotify(address);
            pokeMoves[numMov][1] = Globals.memory.getWordNoNotify(address + 4);
            pokeMoves[numMov][2] = Globals.memory.getWordNoNotify(address + 8);
            pokeMoves[numMov][3] = Globals.memory.getWordNoNotify(address + 12);
            pokeMoves[numMov][4] = Globals.memory.getWordNoNotify(address + 16);
            pokeMoves[numMov][5] = Globals.memory.getWordNoNotify(address + 20);
        } catch (AddressErrorException aee) {
            System.out.println("Tool author specified incorrect MMIO address!" + aee);
            System.exit(0);
        }
        if (battleInitialize == 1){
            String moveName = pokemonMoves.get(pokeMoves[numMov][0]);
            String pokeName = pokemonNames.get(pokemon[0]);
            logDisplay.append(pokeName + " ha aprendido " + moveName + "\n");
        }
    }

    private void useMove(int intPokemon, int numMov){
        int[][] pokeMoves;
        int[] pokemon;
        if (intPokemon == 1){
            pokeMoves = atkMoves;
            pokemon = atkInfo;
        } else {
            pokeMoves = defMoves;
            pokemon = defInfo;
        }
        if (battleInitialize == 1 && pokeMoves[numMov][0] > 0){
            logDisplay.append(pokemonNames.get(pokemon[0]) + " ha usado " + pokemonMoves.get(pokeMoves[numMov][0]) + "!!\n");
        }
    }

    private void setPokemon(int address, int intPokemon){
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
            refreshCentralPanel();
        } catch (AddressErrorException aee) {
            System.out.println("Tool author specified incorrect MMIO address!" + aee);
            System.exit(0);
        }
    }

    private void getCommandBattle(int command){
        if (command == 1){                  // Iniciar Batalla
            initializeBattle();
        } else if (command == 2 && battleInitialize == 0){
            idBackground = (intCommand & 0x000000F0) >> 4;
            refreshDisplay();
        } else if (command == 3){           // Setear Clima
            idWeather = (intCommand & 0x00000070) >> 4;
            durationWeather = (intCommand & 0x00000380) >> 7;
            if (battleInitialize > 0){
                logDisplay.append("El clima " + pokemonWeather.get(idWeather) + " ha comenzado. Turnos restantes: " + durationWeather + "\n");
            }
            refreshCentralPanel();
        } else if (command == 4){           // Finalizar Turno
            idTurn = idTurn + 1;
            if (atkInfo[4] == 1){
                logDisplay.append("El pokemon " + pokemonNames.get(atkInfo[0]) + " ha perdido vida por envenenamiento.\n");
            }
            if (defInfo[4] == 1){
                logDisplay.append("El pokemon " + pokemonNames.get(defInfo[0]) + " ha perdido vida por envenenamiento.\n");
            }
            if (idWeather != 0){
                durationWeather = durationWeather - 1;
                if (durationWeather < 1){
                    logDisplay.append("Clima " + pokemonWeather.get(idWeather) + " ha finalizado.\n");
                    idWeather = 0;
                } else {
                    logDisplay.append("Clima " + pokemonWeather.get(idWeather) + ". Turnos restantes: " + durationWeather + "\n");
                }
            }
            refreshCentralPanel();
        } else if (command == 5){           // Finalizar Batalla
            battleFinished = 1;
            winner = (intCommand & 0x00000010) >> 4;
            logDisplay.append("La batalla " + idBattle + " ha finalizado.\n");
            if (winner == 1){
                logDisplay.append("Has ganado!!!\n");
            } else {
                logDisplay.append("Has perdido :(\n");
            }
            refreshCentralPanel();
        }
    }

    private void getCommandPokemon(int command, int intPokemon, int regAddr, int[] pokemon) {
        if (command == 1){                  // Setear pokemon 
            if (battleInitialize == 1){
                System.out.println("La batalla ya comenzo! No se puede inicializar los pokemones\n");
            } else {
                try {
                    int addressPokemon = Globals.memory.getWordNoNotify(regAddr);
                    setPokemon(addressPokemon, intPokemon);
                } catch (AddressErrorException aee) {
                }
            }
        } else if (command == 2){           // Actualizar estado
            int newStatus = (intCommand & 0x00000070) >> 4;
            if (newStatus != pokemon[4]){
                pokemon[4] = newStatus;
                if (newStatus == 0){
                    logDisplay.append("El pokemon " + pokemonNames.get(pokemon[0]) + " ha sido curado! :D\n");
                } else if (newStatus == 1){
                    logDisplay.append("El pokemon " + pokemonNames.get(pokemon[0]) + " ha sido envenenado! :o\n");
                } else if (newStatus == 2){
                    logDisplay.append("ZzzZZzz... El pokemon " + pokemonNames.get(pokemon[0]) + " se ha quedado dormido.\n");
                } else if (newStatus == 3){
                    logDisplay.append("El pokemon " + pokemonNames.get(pokemon[0]) + " ha quedado paralizado..\n");
                } else if (newStatus == 4){
                    logDisplay.append("El pokemon " + pokemonNames.get(pokemon[0]) + " se ha quemado!!!\n");
                } else if (newStatus == 5){
                    logDisplay.append("El pokemon " + pokemonNames.get(pokemon[0]) + " esta congelado.\n");
                } else if (newStatus == 6){
                    logDisplay.append("El pokemon " + pokemonNames.get(pokemon[0]) + " esta confundido.\n");
                } else if (newStatus == 7){
                    logDisplay.append("El pokemon " + pokemonNames.get(pokemon[0]) + " se ha desmayado! :(\n");
                }
                refreshCentralPanel();
            }
        } else if (command == 3){           // Actualizar la experiencia
            pokemon[3] = (intCommand & 0x000007F0) >> 4;
            refreshCentralPanel();
        } else if (command == 4){

        } else if (command == 5){       // Evolucionar pokemon
            int idEvolution = (intCommand & 0x00000FF0) >> 4;
            logDisplay.append("El pokemon " + pokemonNames.get(pokemon[0]) + " ha evolucionado a " + pokemonNames.get(idEvolution) + "!\n");
            pokemon[0] = idEvolution;
            refreshDisplay();
        } else if (command == 6){       // Setear movimiento
            int numMov = (intCommand & 0x00000030) >> 4;    // numero de movimiento
            try {
                int addressMov= Globals.memory.getWordNoNotify(regAddr);
                setMove(addressMov, intPokemon, numMov);
            } catch (AddressErrorException aee) {
            }

        } else if (command == 7){       // Usar movimiento
            int numMov = (intCommand & 0x00000030) >> 4;        // numero de movimiento
            useMove(intPokemon, numMov);
        } else if (command == 8){

        }
    }

    @Override
    protected void initializePostGUI() {
        logDisplay.requestFocusInWindow();
    }

    private void resetBattle(){
        battleInitialize = 0;
        idBattle = 0;
        idTurn = 0;
        idWeather = 0;
        durationWeather = 0;
        battleFinished = 0; 
        winner = 0; 
    }

    protected void reset() {
        displayRandomAccessMode = false;
        resetBattle();
        logDisplay.setText("");
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
            String idBack = "background/" + String.format("%03d", idBackground) + ".png";
            BufferedImage background = ImageIO.read(new File(pathPokemons, idBack));

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
        } catch (IOException ex) {
            combined = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        }
        return combined;
    }

    private JLabel addImage(){
        BufferedImage image = mergeImages();
        picLabel = new JLabel(new ImageIcon(image));
        return picLabel;
    }

    private JPanel buildDisplay(){
        displayPanel = new JPanel(new BorderLayout());
        TitledBorder tb = new TitledBorder(displayPanelTitle);
        tb.setTitleJustification(TitledBorder.CENTER);
        displayPanel.setBorder(tb);
        refreshDisplay();
        return displayPanel;
    }

    private JPanel infoPokemonEmpty(){
        JPanel infoPanel = new JPanel(new GridLayout(13,1));
        JLabel nameLabel = new JLabel(" ");
        nameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
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

    private JPanel infoPokemon(int[] pokemon, int intPokemon){
        JPanel info = new JPanel(new GridLayout(1,2));
        JPanel infoPanel = new JPanel(new GridLayout(13,1));
        String nombre = "";
        if (pokemon[0] > 0){
            nombre = pokemonNames.get(pokemon[0]);
        }
        JLabel nameLabel = new JLabel("Info " + nombre);
        nameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        infoPanel.add(nameLabel);
        infoPanel.add(new PrettyJLabel("Id :" + pokemon[0], 11));
        infoPanel.add(new PrettyJLabel("Tipo: " + pokemonTypes.get(pokemon[1]), 11));
        infoPanel.add(new PrettyJLabel("Nivel: " + pokemon[2], 11));
        infoPanel.add(new PrettyJLabel("Exp: " + pokemon[3], 11));
        infoPanel.add(new PrettyJLabel("Estado: " + pokemonStatus.get(pokemon[4]), 11));
        infoPanel.add(new PrettyJLabel("Hp: " + pokemon[5] + " / " + pokemon[6], 11));
        infoPanel.add(new PrettyJLabel("Ataque Fisico: " + pokemon[7], 11));
        infoPanel.add(new PrettyJLabel("Defensa Fisica: " + pokemon[8], 11));
        infoPanel.add(new PrettyJLabel("Ataque Especial: " + pokemon[9], 11));
        infoPanel.add(new PrettyJLabel("Defensa Especial: " + pokemon[10], 11));
        infoPanel.add(new PrettyJLabel("Velocidad: " + pokemon[11], 11));

        JPanel buttonPanel = new JPanel();
        // buttonPanel.setBorder(BorderFactory.createEmptyBorder(100, 10, 50, 30));
        JButton movButton = new JButton("Ver Movimientos");
        // movButton.setPreferredSize(new Dimension(150, 40)); 

        if (intPokemon == 1){
            if (battleInitialize == 1){
                info.add(infoPanel, BorderLayout.WEST);
                movButton.addActionListener(new showMoves(1));
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(100, 30, 50, 10));
                buttonPanel.add(movButton, BorderLayout.CENTER);
                info.add(buttonPanel);
            } else {
                info.add(infoPokemonEmpty(), BorderLayout.WEST);
                info.add(Box.createHorizontalStrut(10));
            }
        } else if (intPokemon == 2){
            if (battleInitialize == 1){
                movButton.addActionListener(new showMoves(2));
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(100, 10, 50, 30));
                buttonPanel.add(movButton, BorderLayout.CENTER);
                info.add(buttonPanel);
                info.add(infoPanel, BorderLayout.EAST);
            } else {
                info.add(Box.createHorizontalStrut(10));
                info.add(infoPokemonEmpty(), BorderLayout.EAST);
            }
        }
        info.revalidate();
        info.repaint();

        return info; 
    }

    private JPanel buildBattleInfoDisplay(){
        JPanel infoPanel = new JPanel(new GridLayout(2,1));
        JPanel infoBattlePanel = new JPanel(new GridLayout(1,4));
        JPanel weatherPanel = new JPanel(new GridLayout(1,2));

        JLabel battleLabel = new PrettyJLabel("Batalla: " + idBattle, 14);
        infoBattlePanel.add(battleLabel);

        JLabel turnLabel = new PrettyJLabel("Turno: " + idTurn, 14);
        infoBattlePanel.add(turnLabel);

        JLabel backgroundLabel = new PrettyJLabel("Lugar: " + pokemonBackgrounds.get(idBackground), 14);
        infoBattlePanel.add(backgroundLabel);

        JLabel weatherLabel = new PrettyJLabel("Clima: " + pokemonWeather.get(idWeather), 14);
        weatherPanel.add(weatherLabel);
        weatherPanel.add(new PrettyJLabel("  " + " o".repeat(durationWeather), 12));
        infoBattlePanel.add(weatherPanel);

        infoPanel.add(infoBattlePanel);
        infoPanel.add(new JLabel(" "));
        return infoPanel;
    }

    private JPanel buildCentralPanel(){
        infoDisplayPanel = new JPanel(new GridLayout(1,3));
        infoLeftPanel = infoPokemon(atkInfo, 1);
        infoDisplayPanel.add(infoLeftPanel);
        infoDisplayPanel.add(addImage(), BorderLayout.CENTER);
        infoRightPanel = infoPokemon(defInfo, 2);
        infoDisplayPanel.add(infoRightPanel);
        return infoDisplayPanel;
    }

    private JPanel refreshSidePanels(){
        infoDisplayPanel = new JPanel(new GridLayout(1,3));
        infoLeftPanel = infoPokemon(atkInfo, 1);
        infoDisplayPanel.add(infoLeftPanel);
        infoDisplayPanel.add(picLabel, BorderLayout.CENTER);
        infoRightPanel = infoPokemon(defInfo, 2);
        infoDisplayPanel.add(infoRightPanel);
        return infoDisplayPanel;
    }

    private void refreshCentralPanel(){
        displayPanel.removeAll();
        displayPanel.add(buildBattleInfoDisplay(), BorderLayout.NORTH);
        displayPanel.add(refreshSidePanels());
        displayPanel.revalidate();
        displayPanel.repaint();
    }

    private void refreshDisplay(){
        displayPanel.removeAll();
        displayPanel.add(buildBattleInfoDisplay(), BorderLayout.NORTH);
        displayPanel.add(buildCentralPanel());
        displayPanel.revalidate();
        displayPanel.repaint();
    }

    private JComponent buildInfo() {
        logPanel = new JPanel(new BorderLayout());
        logDisplay = new JTextArea("");
        logDisplay.setEditable(false);
        logDisplay.setFont(defaultFont);
        logDisplay.setMargin(textAreaInsets);
        DefaultCaret caret = (DefaultCaret)logDisplay.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        logAccepterScrollPane = new JScrollPane(logDisplay);
        logAccepterScrollPane.setPreferredSize(new Dimension(900, 100));
        logPanel.add(logAccepterScrollPane);
        TitledBorder tb = new TitledBorder(infoPanelTitle);
        tb.setTitleJustification(TitledBorder.CENTER);
        logPanel.setBorder(tb);
        return logPanel;
    }

    private void setMemory(int addr, int value){
        try {
            Globals.memory.setRawWord(addr, value);
        } catch (AddressErrorException aee) {
            System.out.println("Me equivoque :(" + aee);
            System.exit(0);
        }
    }

    private class showMoves implements ActionListener {
        public int intPokemon;

        public showMoves(int intPokemon){
            this.intPokemon = intPokemon;
        }

        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String text = button.getText();
            JFrame frame;

            if (intPokemon == 1){
                frame = showMovesPanel.show(1, atkInfo, atkMoves, pokemonMoves, pokemonTypes);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } else if (intPokemon == 2){
                frame = showMovesPanel.show(2, defInfo, defMoves, pokemonMoves, pokemonTypes);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        }
    }

    private class showMovesPanel {

        public static JFrame show(int intPokemon, int[] pokemon, int[][] moves, HashMap<Integer, String> pokemonMoves, HashMap<Integer, String> pokemonTypes){
            // Frame Setup
            JFrame frame = new JFrame();
            frame.setSize(400, 500);
            ImageIcon img = new ImageIcon("./src/images/pokemon/dratini.png");
            frame.setIconImage(img.getImage());
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            if (intPokemon == 1){
                frame.setTitle("Movimientos Pokemon Atacante");
                panel.add(new PrettyJLabel("Movimientos Atacante", 16), BorderLayout.NORTH);
            } else {
                frame.setTitle("Movimientos Pokemon Defensor");
                panel.add(new PrettyJLabel("Movimientos Defensor", 16), BorderLayout.NORTH);
            }


            // Setear movimientos
            JPanel movPanel = new JPanel(new GridLayout(4,1));
            movPanel.add(movesPanel(intPokemon, 0, pokemon, moves, pokemonMoves, pokemonTypes));
            movPanel.add(movesPanel(intPokemon, 1, pokemon, moves, pokemonMoves, pokemonTypes));
            movPanel.add(movesPanel(intPokemon, 2, pokemon, moves, pokemonMoves, pokemonTypes));
            movPanel.add(movesPanel(intPokemon, 3, pokemon, moves, pokemonMoves, pokemonTypes));

            panel.add(movPanel);
            frame.add(panel);

            return frame;
        }    
        
        private static JPanel movesPanel (int intPokemon, int movInt, int[] pokemon, int[][] moves, HashMap<Integer, String> pokemonMoves, HashMap<Integer, String> pokemonTypes){
            JPanel mov = new JPanel(new GridLayout(4,2));
            mov.add(new PrettyJLabel("Movimiento " + movInt, 12));
            mov.add(new PrettyJLabel(" ", 12));
            if (moves[movInt][0] == 0){
                mov.add(new PrettyJLabel("Nombre: ", 10));
                mov.add(new PrettyJLabel("Tipo: ", 10));
                mov.add(new PrettyJLabel("Categoria: ", 10));
                mov.add(new PrettyJLabel("Poder: ", 10));
                mov.add(new PrettyJLabel("Precision: ", 10));
                mov.add(new PrettyJLabel("PPs: ", 10));
            } else {
                mov.add(new PrettyJLabel("Nombre: " + pokemonMoves.get(moves[movInt][0]), 10));
                mov.add(new PrettyJLabel("Tipo: " + pokemonTypes.get(moves[movInt][1]), 10));
                mov.add(new PrettyJLabel("Categoria: " + moves[movInt][2], 10));
                mov.add(new PrettyJLabel("Poder: " + moves[movInt][3], 10));
                mov.add(new PrettyJLabel("Precision: " + moves[movInt][4], 10));
                mov.add(new PrettyJLabel("PPs: " + moves[movInt][5], 10));
            }
            return mov;
        } 
    }
}