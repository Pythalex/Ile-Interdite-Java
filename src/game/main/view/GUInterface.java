package game.main.view;

import game.main.controller.PlayerController;
import game.main.model.*;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Scanner;

public class GUInterface extends JPanel implements Observer{

    /// ATTRIBUTES

    private Game master;

    private JFrame window;
    private JTextArea textArea;
    private JScrollPane textAreaScroller;

    // resources
    private Color colorCaseStroke = Color.BLACK;
    private Color colorCase = Color.WHITE;
    private Color colorCaseFlood = new Color(125, 221, 215);
    private Color colorCaseSubmerged = new Color(0, 86, 114);

    private BufferedImage heliportSprite;
    private BufferedImage artifactWaterSprite;
    private BufferedImage artifactFireSprite;
    private BufferedImage artifactEarthSprite;
    private BufferedImage artifactAirSprite;

    // buttons
    private JButton upButton;
    private JButton upLeftButton;
    private JButton leftButton;
    private JButton downLeftButton;
    private JButton downButton;
    private JButton downRightButton;
    private JButton rightButton;
    private JButton upRightButton;
    private JButton moveToButton;
    private JButton simpleDryButton;
    private JButton doubleDryButton;
    private JButton passButton;
    private JButton artifactButton;

    /// CONSTRUCTORS

    public GUInterface(Game master){
        super();

        this.master = master;

        int size = 700;

        window = new JFrame("Ile Interdite");

        // will define the grid settings for the widgets placements.
        GridBagConstraints gridRules = new GridBagConstraints();

        // ---------- Game canvas -------------------------

        window.setLayout(new GridBagLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gridRules.gridx = 0;
        gridRules.gridy = 0;

        window.add(this, gridRules);

        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        setSize(new Dimension(size, size));

        // ---------- Buttons and text --------------------

        JPanel UIContainer = new JPanel();
        UIContainer.setLayout(new GridLayout(2, 1, 0, 100));

        // buttons

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new GridBagLayout());

        GridBagConstraints buttonRules = new GridBagConstraints();
        // button padding
        buttonRules.ipadx = 5;
        buttonRules.ipady = 15;

        // distribute space
        buttonRules.weightx = 1.0;
        buttonRules.weighty = 1.0;

        // move up
        buttonRules.gridx = 1;
        buttonRules.gridy = 0;
        upButton = new JButton("↑");
        // can't give a function callback directly, must use this annoying workaround
        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "moveup";
            }
        });
        buttonContainer.add(upButton, buttonRules);

        // move up left
        buttonRules.gridx = 0;
        buttonRules.gridy = 0;
        upLeftButton = new JButton("↖");
        upLeftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "moveupleft";
            }
        });
        buttonContainer.add(upLeftButton, buttonRules);

        // move left
        buttonRules.gridx = 0;
        buttonRules.gridy = 1;
        leftButton = new JButton("←");
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "moveleft";
            }
        });
        buttonContainer.add(leftButton, buttonRules);

        // move down left
        buttonRules.gridx = 0;
        buttonRules.gridy = 2;
        downLeftButton = new JButton("↙");
        downLeftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "movedownleft";
            }
        });
        buttonContainer.add(downLeftButton, buttonRules);

        // move down
        buttonRules.gridx = 1;
        buttonRules.gridy = 2;
        downButton = new JButton("↓");
        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "movedown";
            }
        });
        buttonContainer.add(downButton, buttonRules);

        // move down right
        buttonRules.gridx = 2;
        buttonRules.gridy = 2;
        downRightButton = new JButton("↘");
        downRightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "movedownright";
            }
        });
        buttonContainer.add(downRightButton, buttonRules);

        // move right
        buttonRules.gridx = 2;
        buttonRules.gridy = 1;
        rightButton = new JButton("→");
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "moveright";
            }
        });
        buttonContainer.add(rightButton, buttonRules);

        // move up right
        buttonRules.gridx = 2;
        buttonRules.gridy = 0;
        upRightButton = new JButton("↗");
        upRightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "moveupright";
            }
        });
        buttonContainer.add(upRightButton, buttonRules);

        // move to button - used by pilot
        buttonRules.gridx = 1;
        buttonRules.gridy = 1;
        moveToButton = new JButton("\uD83D\uDEE7"); // airplane symbol
        moveToButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "moveto";
            }
        });
        buttonContainer.add(moveToButton, buttonRules);

        // pass
        buttonRules.gridx = 0;
        buttonRules.gridy = 4;
        passButton = new JButton("Pass");
        passButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "pass";
            }
        });
        buttonContainer.add(passButton, buttonRules);

        // dry
        buttonRules.gridx = 1;
        buttonRules.gridy = 4;
        simpleDryButton = new JButton("Dry");
        simpleDryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "dry";
            }
        });
        buttonContainer.add(simpleDryButton, buttonRules);

        // double dry
        buttonRules.gridx = 1;
        buttonRules.gridy = 5;
        doubleDryButton = new JButton("Double Dry");
        doubleDryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "doubledry";
            }
        });
        buttonContainer.add(doubleDryButton, buttonRules);

        // get artifact
        buttonRules.gridx = 2;
        buttonRules.gridy = 4;
        artifactButton = new JButton("Artifact");
        artifactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                master.currentPlayer.pc.actionBuffer = "getartifact";
            }
        });
        buttonContainer.add(artifactButton, buttonRules);

        UIContainer.add(buttonContainer);

        // text

        textArea = new JTextArea(15, 25);
        textArea.setEditable(false);
        // makes auto new line if lines are too long
        textArea.setLineWrap(true);

        textAreaScroller = new JScrollPane(textArea);

        // add the scrollpane to the screen container
        UIContainer.add(textAreaScroller);

        gridRules.gridx = 1;
        gridRules.gridy = 0;

        window.add(UIContainer, gridRules);

        // ------------ Tweaks ----------------------------

        window.setVisible(true);
        window.setResizable(false);
        window.pack();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                repaint();
            }
        });

        loadResources();
    }

    /// METHODS
    @Override
    public void update(){
        repaint();
    }

    /**
     * Displays the message to the screen.
     * @param msg the message to display
     */
    public void message(String msg){
        textArea.append(msg + "\n");
        // this makes the text area auto scroll when filled
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * NOT USED BECAUSE SWING SEEMS TO DESTROY THE INTERFACE (PANELS ARE
     * BADLY RESIZED) WHEN RECALCULATING WIDGETS PLACEMENTS.
     */
    public void adaptKeyToPlayerClass(CharacterClasses chClass){

        // common behavior
        hideDiagonallyMoveButtons();
        moveToButton.setEnabled(false);
        doubleDryButton.setEnabled(false);

        switch (chClass){
            case Default:
                break;
            case Pilot:
                moveToButton.setEnabled(true);
                break;
            case Engineer:
                doubleDryButton.setEnabled(true);
                break;
            case Explorer:
                showDiagonallyMoveButtons();
                break;
            case Diver:
                break;
            default:
                break;
        }
    }

    /**
     * Sets the visibility of all buttons allowing
     * diagonal movements to false.
     */
    public void hideDiagonallyMoveButtons(){
        upLeftButton.setEnabled(false);
        downLeftButton.setEnabled(false);
        upRightButton.setEnabled(false);
        downRightButton.setEnabled(false);
    }

    /**
     * Sets the visibility of all buttons allowing
     * diagonal movements to true.
     */
    public void showDiagonallyMoveButtons(){
        upLeftButton.setEnabled(true);
        downLeftButton.setEnabled(true);
        upRightButton.setEnabled(true);
        downRightButton.setEnabled(true);
    }

    /**
     * Asks a position to the player.
     * @return a 2D coordinate
     */
    public Pair<Integer, Integer> askPosition(){
        Pair<Integer, Integer> pos = new Pair<>(0, 0);
        boolean valid = false;
        while (!valid){
            String position = null;
            while (position == null) {
                position = JOptionPane.showInputDialog(window,
                        "Give a position to go as x;y", null);
            }
            String[] coordinates = position.split(";");

            // If given string is in the format x;y
            if (coordinates.length == 2 && isInteger(coordinates[0]) &&
                    isInteger(coordinates[1])){
                valid = true;
                pos = new Pair<>(Integer.parseInt(coordinates[0]),
                        Integer.parseInt(coordinates[1]));
            } else {
                JOptionPane.showMessageDialog(window,
                        "You have not given a correct position.",
                        "Position format error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return pos;
    }

    public boolean isInteger(String s){
        Scanner sc = new Scanner(s.trim());
        return sc.hasNextInt();
    }

    /// DRAW METHODS

    /**
     * Draws the isle's cases' states and event.
     * @param g the panel graphics
     */
    public void drawCases(Graphics g){

        Ile isle = master.isle;

        int colWidth = getHeight() / isle.height;
        int rowWidth = colWidth;

        for (int j = 0; j < isle.height; j++){
            for (int i = 0; i < isle.width; i++){

                Case c = isle.cases[j * isle.width + i];

                switch (c.state){
                    case dry:
                        g.setColor(colorCase);
                        break;
                    case flooded:
                        g.setColor(colorCaseFlood);
                        break;
                    case submerged:
                    default:
                        g.setColor(colorCaseSubmerged);
                        break;
                }

                g.fillRect(i * colWidth, j * rowWidth, i * colWidth + colWidth,
                        j * rowWidth + rowWidth);

                switch (c.event){
                    case Helicopter:
                        g.drawImage(heliportSprite, i * colWidth, j * rowWidth, null);
                        break;
                    case Element_water:
                        g.drawImage(artifactWaterSprite, i * colWidth, j * rowWidth, null);
                        break;
                    case Element_fire:
                        g.drawImage(artifactFireSprite, i * colWidth, j * rowWidth, null);
                        break;
                    case Element_earth:
                        g.drawImage(artifactEarthSprite, i * colWidth, j * rowWidth, null);
                        break;
                    case Element_air:
                        g.drawImage(artifactAirSprite, i * colWidth, j * rowWidth, null);
                        break;
                    case None:
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Draws the isle's grid.
     * @param g the panel graphics
     */
    public void drawGrid(Graphics g){

        Ile isle = master.isle;

        g.setColor(colorCaseStroke);

        int colWidth = getHeight() / isle.height;
        int rowWidth = colWidth;

        for (int col = 0; col < isle.width; col++)
            g.drawLine(col * colWidth, 0, col * colWidth, getHeight() - 1);
        for (int row = 0; row < isle.height; row++)
            g.drawLine(0, row * rowWidth, getWidth() - 1, row * rowWidth);
    }

    /**
     * Draws the isle's grid and the cases' states and event.
     * @param g the panel graphics
     */
    public void drawIsle(Graphics g){
        drawCases(g);
        drawGrid(g);
    }

    /**
     * Draws the players at their position.
     * @param g the panel graphics
     */
    public void drawPlayers(Graphics g){

        Ile isle = master.isle;
        Player[] players = master.players;

        int colWidth = getHeight() / isle.height;
        int rowWidth = colWidth;

        int radius = rowWidth / 3;

        g.setColor(Color.GREEN);


        int pX, pY;
        int margin = colWidth / 20;
        for (Case c: isle.cases) {

            int i = 0;

            for (Player p : c.players) {

                switch (master.getPlayerCase(p).players.size()) {
                    case 1:
                        pX = p.x * colWidth + colWidth / 2;
                        pY = p.y * rowWidth + rowWidth / 2;
                        break;
                    case 2:
                        if (i == 0) {
                            pX = p.x * colWidth + colWidth / 6 + margin;
                            pY = p.y * rowWidth + rowWidth / 6 + margin;
                        } else {
                            pX = p.x * colWidth + 2 * colWidth / 3 + colWidth / 6 - margin;
                            pY = p.y * rowWidth + 2 * rowWidth / 3 + rowWidth / 6 - margin;
                        }
                        break;
                    case 3:
                        if (i == 0) {
                            pX = p.x * colWidth + colWidth / 6 + margin;
                            pY = p.y * rowWidth + rowWidth / 6 + margin;
                        } else if (i == 1) {
                            pX = p.x * colWidth + colWidth / 3 + colWidth / 6;
                            pY = p.y * rowWidth + rowWidth / 3 + rowWidth / 6;
                        } else {
                            pX = p.x * colWidth + 2 * colWidth / 3 + colWidth / 6 - margin;
                            pY = p.y * rowWidth + 2 * rowWidth / 3 + rowWidth / 6 - margin;
                        }
                        break;
                    case 4:
                    default:
                        if (i == 0) {
                            pX = p.x * colWidth + colWidth / 6 + margin;
                            pY = p.y * rowWidth + rowWidth / 6 + margin;
                        } else if (i == 1) {
                            pX = p.x * colWidth + 2 * colWidth / 3 + colWidth / 6 - margin;
                            pY = p.y * rowWidth + rowWidth / 6 + margin;
                        } else if (i == 2) {
                            pX = p.x * colWidth + colWidth / 6 + margin;
                            pY = p.y * rowWidth + 2 * rowWidth / 3 + rowWidth / 6 - margin;
                        } else {
                            pX = p.x * colWidth + 2 * colWidth / 3 + colWidth / 6 - margin;
                            pY = p.y * rowWidth + 2 * rowWidth / 3 + rowWidth / 6 - margin;
                        }
                        break;
                }

                g.fillOval(pX - radius / 2, pY - radius / 2, radius, radius);
                i++;
            }
        }
    }

    /**
     * Clear the panel with white.
     * @param g the panel graphics
     */
    public void clear(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    /**
     * Draws everything.
     * @param g the panel graphics
     */
    public void paintComponent(Graphics g){
        clear(g);
        drawIsle(g);
        drawPlayers(g);
        adaptKeyToPlayerClass(master.currentPlayer.chClass);
    }

    // Load resources

    public void loadResources(){
        String root = "resources" + File.separator;
        heliportSprite = loadImage(root + "heliport.png");
        artifactWaterSprite = loadImage(root + "artifactWater.png");
        artifactFireSprite = loadImage(root + "artifactFire.png");
        artifactEarthSprite = loadImage(root + "artifactEarth.png");
        artifactAirSprite = loadImage(root + "artifactAir.png");
    }

    public BufferedImage loadImage(String path){
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e){
            System.err.print("Error when loading " + path + ":");
            e.printStackTrace(System.err);
            System.exit(1);
        }
        return null;
    }
}
