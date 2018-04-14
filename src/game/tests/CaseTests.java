package game.tests;

import game.main.model.Case;
import game.main.model.Event;

import static org.junit.Assert.*;
import org.junit.Test;

public class CaseTests{

    Case c1 = new Case(null, 0, 0);
    Case c2 = new Case(null, 10, 12, Event.Helicopter);

    @Test
    public void toStringTest(){
        assertEquals(c1.toString(), "(0, 0)-D-N");
        assertEquals(c2.toString(), "(10, 12)-D-H");
        c2.flood();
        assertEquals(c2.toString(), "(10, 12)-F-H");
        c2.flood();
        assertEquals(c2.toString(), "(10, 12)-S-H");
        c2.dry();
        assertEquals(c2.toString(), "(10, 12)-D-H");
        c2.flood();
        c2.dry();
        assertEquals(c2.toString(), "(10, 12)-D-H");

    }

}