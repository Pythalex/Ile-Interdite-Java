package game.main.view;

import game.main.model.Case;
import game.main.model.Game;
import game.main.model.Ile;
import game.main.model.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class GUInterface extends JPanel implements Observer{

    /// ATTRIBUTES

    private Game master;

    private JFrame window;

    private Player playerPlaying;

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

    /// CONSTRUCTORS

    public GUInterface(Game master){
        super();

        this.master = master;

        int size = 700;

        setPreferredSize(new Dimension(size, size));

        window = new JFrame("Ile Interdite");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(this);

        setSize(window.getHeight(), window.getHeight());

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

    public void displayMessage(String msg){
        System.out.println(msg);
        repaint();
    }

    /**
     * Actually does nothing for the GUInterface, just repaints
     * the panel.
     */
    public void displayState(Ile isle){
        repaint();
    }

    public void displayState(Player p){
        playerPlaying = p;
        System.out.println("Player " + p.name + "(" + p.x + "," + p.y + ") Keys : " +
                (p.keys[0] ? "W-" : "") + (p.keys[1] ? "F-" : "") + (p.keys[2] ? "E-" : "") +
                (p.keys[3] ? "A-" : "") + "  Artifacts : " +
                (p.artifact[0] ? "W-" : "") + (p.artifact[1] ? "F-" : "") + (p.artifact[2] ? "E-" : "") +
                (p.artifact[3] ? "A-" : ""));
        repaint();
    }

    public void displayError(Exception err){
        System.err.println(err);
        repaint();
    }

    @Override
    public void update(){
        repaint();
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
