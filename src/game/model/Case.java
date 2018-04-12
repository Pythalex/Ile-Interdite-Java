package game.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a grid case
 * 
 * A grid has a position on the grid (x, y).
 * A grid can have 
 */
public class Case
{
	// State of the case
	public State state;

	// Event on the case
	public Event event;

	// Position on the grid
	public int x;
	public int y;

	// The isle on which the case is
	public Ile isle;

	/**
	 * Creates a case on a given isle.
	 * Takes the case position on the grid
	 * as argument.
	 * 
	 * @param master The isle master
	 * @param x		 The x position
	 * @param y		 The y position
	 */
	public Case(Ile master, int x, int y){
		this.isle = master;
		this.x = x;
		this.y = y;
		this.state = State.dry; // default state
		this.event = Event.None;  // default event
	}

	/**
	 * Floods the case. If the case was already
	 * flooded, it is submerged.
	 */
	public void flood(){
		switch (state){
			case dry:
				state = State.flooded;
				break;
			case flooded:
			case submerged:
				state = State.submerged;
				break;
		}
	}

	/**
	 * Dries the case up.
	 */
	public void dry(){
		state = State.dry;
	}

	public String toString(){
		return String.format("(%d, %d)-%s-%s", x, y, state, event);
	}
}

