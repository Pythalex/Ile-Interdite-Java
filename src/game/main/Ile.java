package game.main;


/**
 * Represents the game's isle
 */
public class Ile
{
	// Isle's grid width	
	public int width;

	// Isle's grid height
	public int height;

	// The grid's cases
	public Case[] cases;

	// The master game instance
	public Game gameMaster;

	/**
	 * Creates and returns an isle of a given
	 * width and height.
	 *
	 * @param master The master game instance
	 * @param width The isle's grid width
	 * @param height The isle's grid height
	 */
	public Ile(Game master, int width, int height){
		gameMaster = master;
		this.width = width;
		this.height = height;
		this.cases = new Case[width * height];
	}

	/**
	 * Initiates the grid's cases.
	 */
	public void initiateCases(){
		int x = 0;
		int y = 0;
		for (Case c: cases){
			c = new Case(this, 0, 0);
		}
	}

}

