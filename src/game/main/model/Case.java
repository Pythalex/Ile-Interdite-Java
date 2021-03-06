package game.main.model;

import java.util.ArrayList;
import java.util.List;

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

	// ref of player currently on the case
	public List<Player> players;

	/**
	 * Creates a case on a given island.
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
		players = new ArrayList<>();
	}

	/**
	 * Creates a case on a given island.
	 * Same as first constructor but takes an
	 * event as argument.
	 * @param master The isle master
	 * @param x		 The x position
	 * @param y		 The y position
	 * @param e		 The event on the case
	 */
	public Case(Ile master, int x, int y, Event e){
		this(master, x, y);
		this.event = e;
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
	 * Dries the case.
	 */
	public void dry(){
		state = State.dry;
	}

	/**
	 * Indicates whether the case is dry.
	 * @return state == dry
	 */
	public boolean isDry()		 { return state == State.dry; }

	/**
	 * Indicates whether the case is flooded.
	 * @return state == flooded
	 */
	public boolean isFlooded() 	 { return state == State.flooded; }

	/**
	 * Indicates whether the case is submerged.
	 * @return state == submerged
	 */
	public boolean isSubmerged() { return state == State.submerged; }

	@Override
	/**
	 * Returns the case object in a string format:
	 * (x, y)-state-event
	 * where state and event are in toString format.
	 */
	public String toString(){
		return String.format("(%d, %d)-%s-%s", x, y, state, event);
	}
}

