package game.main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the game's isle
 */
public class Ile
{
	// Isle's grid width	
	public int width;
	// Isle's grid height
	public int height;

	// Some datas about the grid
	public int numberOfCases     = 0; // width * height
	public int numberOfSubmerged = 0;

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
		this.numberOfCases = width * height;
		this.cases = new Case[width * height];
		initiateCases();
	}

	/**
	 * Initiates the grid's cases.
	 * Every case will be dry and without event.
	 */
	public void initiateCases(){
		for (int j = 0; j < height; j++){
			for (int i = 0; i < width; i++){
				cases[j * width + i] = new Case(this, i, j);
			}
		}
	}

	/**
	 * Floods n cases where n has to be in [0, width*height]
	 * Prioritizes the non-submerged cases.
	 * @param n The number of cases to flood
	 */
	public void floodCases(int n) {

		if (n < 0 || n > numberOfCases)
			return;

		Random rdmGen = new Random();
		List<Integer> chosen = new ArrayList<>();

		boolean alreadyChosen;
		boolean allSubmerged = numberOfSubmerged == numberOfCases;
		boolean isSubmerged;

		while (n > 0){

			int rand = Math.abs(rdmGen.nextInt() % (numberOfCases));

			// Prevent choosing the same case twice and
			alreadyChosen = chosen.contains(rand);
			// prioritized dry/flooded cases over submerged ones
			isSubmerged   = cases[rand].isSubmerged();

			if ( !( alreadyChosen || (isSubmerged && !allSubmerged) ) ) {

				// the case is selected to flood
				chosen.add(rand);
				n--;

				// the case is flooded
				cases[rand].flood();
				if (cases[rand].isSubmerged()) {
					numberOfSubmerged++;
					// update allSubmerged boolean
					allSubmerged = numberOfSubmerged == numberOfCases;
				}
			}
		}

	}

	/**
	 * Indicates whether the player can move to
	 * (x, y).
	 * If the position given is invalid, false is
	 * returned.
	 * @param x the target x position
	 * @param y the target y position
	 */
	public boolean playerCanMove(int x, int y){
		if (x < 0 || x >= width || y < 0 || y >= height)
			return false;

		return !cases[y * width + x].isSubmerged();
	}

	/**
	 * Finds and returns the first case satisfying the
	 * Event query. If nothing is found, returns null
	 * @param e the event criteria.
	 * @return the case's ref
	 */
	public Case foundCaseByEvent(Event e){
		for (Case c: cases){
			if (c.event == e)
				return c;
		}
		return null;
	}

	@Override
	/**
	 * Returns a string representing the isle current state.
	 */
	public String toString(){
		StringBuilder str = new StringBuilder();
		for (int j = 0; j < height; j++){
			for (int i = 0; i < width; i++){
				str.append("|" + cases[j * width + i]);
				if (i == width - 1)
					str.append("|");
			}
			if (j < height - 1)
				str.append("\n" + new String(new char[width]).replace("\0", "----------") + "\n");
		}
		return str.toString();
	}

}

