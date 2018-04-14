package game.main.model;

/**
 * Represents a case event.
 * An event can be a Helicopter, elements, nothing ...
 */
public enum Event
{
	None,
	Helicopter,
	Element_water,
	Element_fire,
	Element_earth,
	Element_air;

	@Override
	/**
	 * Returns a string representing the event
	 * as a capital letter.
	 */
	public String toString(){
		switch (this){
			case None:
				return "N";
			case Helicopter:
				return "H";
			case Element_water:
				return "EW";
			case Element_fire:
				return "EF";
			case Element_earth:
				return "EE";
			case Element_air:
				return "EA";
			default:
				return "N/A";
		}
	}
}
