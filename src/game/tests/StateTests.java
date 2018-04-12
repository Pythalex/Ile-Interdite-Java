package game.tests;

import game.model.State;

import static org.junit.Assert.*;
import org.junit.Test;

public class StateTests{

    @Test
    public static void toStringTest(){
        State d = State.dry;
        State f = State.flooded;
        State s = State.submerged;

        assertEquals(d.toString(), "D");
        assertEquals(f.toString(), "F");
        assertEquals(s.toString(), "S");
    }

}