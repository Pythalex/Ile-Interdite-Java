package game.tests;

import game.main.model.Event;

import static org.junit.Assert.*;
import org.junit.Test;

public class EventTests{

    Event none = Event.None;
    Event heli = Event.Helicopter;
    Event elmW = Event.Element_water;
    Event elmF = Event.Element_fire;
    Event elmE = Event.Element_earth;
    Event elmA = Event.Element_air;

    @Test
    public void toStringTest(){

        assertEquals(this.none.toString(), "N");
        assertEquals(this.heli.toString(), "H");
        assertEquals(this.elmW.toString(), "EW");
    }

}