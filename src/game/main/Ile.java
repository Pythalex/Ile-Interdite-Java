package game.main;

import java.util.ArrayList;
import java.util.Comparator;
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
	 * @param n The number of cases to flood
	 */
	public void floodCases(int n) {

		if (n < 0 || n > width*height)
			return;

		Random rdmGen = new Random();
		List<Integer> chosen = new ArrayList<>();
		while (n > 0){
			int rand = Math.abs(rdmGen.nextInt() % (width*height));
			if (!chosen.contains(rand)) {
				chosen.add(rand);
				n--;
			}
		}

		for (int i: chosen){
			cases[i].flood();
		}
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

