package game.tests;

import game.main.model.Ile;

import static org.junit.Assert.*;
import org.junit.Test;

public class IleTests{

    Ile isle = new Ile(null, 2, 2);

    @Test
    public void toStringTests(){

        assertEquals(isle.toString(), "|(0, 0)-D-N|(1, 0)-D-N|\n" +
                        "--------------------\n" +
                        "|(0, 1)-D-N|(1, 1)-D-N|");
    }

    @Test
    public void initiateCasesTests(){
        isle.initiateCases();
        isle.floodCase(0);
        assertEquals(isle.toString(), "|(0, 0)-F-N|(1, 0)-D-N|\n" +
                        "--------------------\n" +
                        "|(0, 1)-D-N|(1, 1)-D-N|");
        isle.floodCase(1);
        assertEquals(isle.toString(), "|(0, 0)-F-N|(1, 0)-F-N|\n" +
                        "--------------------\n" +
                        "|(0, 1)-D-N|(1, 1)-D-N|");
    }

    @Test
    public void floodCasesTests(){
        // submerge case 4
        isle.floodCase(3);
        isle.floodCase(3);

        assertEquals(isle.toString(), "|(0, 0)-D-N|(1, 0)-D-N|\n" +
                        "--------------------\n" +
                        "|(0, 1)-D-N|(1, 1)-S-N|");

        isle.floodCase(2);

        assertEquals(isle.toString(), "|(0, 0)-D-N|(1, 0)-D-N|\n" +
                        "--------------------\n" +
                        "|(0, 1)-F-N|(1, 1)-S-N|");
    }

    @Test
    public void playerCanMoveTests(){
        assertEquals(isle.playerCanMove(0, 0), true);
        assertEquals(isle.playerCanMove(1, 0), true);
        assertEquals(isle.playerCanMove(0, 1), true);
        assertEquals(isle.playerCanMove(1, 1), true);

        isle.cases[0].flood();
        isle.cases[0].flood();
        isle.cases[1].flood();

        assertEquals(isle.playerCanMove(0, 0), false);
        assertEquals(isle.playerCanMove(1, 0), true);
        assertEquals(isle.playerCanMove(0, 1), true);
        assertEquals(isle.playerCanMove(1, 1), true);

        isle.cases[0].dry();
        assertEquals(isle.playerCanMove(0, 0), true);
    }

}