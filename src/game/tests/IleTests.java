package game.tests;

import game.main.Case;
import game.main.Ile;

import static org.junit.Assert.*;
import org.junit.Test;

public class IleTests{

    @Test
    public void toStringTests(){
        Ile isle = new Ile(null, 2, 2);
        assertEquals(isle.toString(),
                "|(0, 0)-D-N|(1, 0)-D-N|\n" +
                        "--------------------\n" +
                        "|(0, 1)-D-N|(1, 1)-D-N|");
    }

}