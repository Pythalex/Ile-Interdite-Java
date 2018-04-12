package game.tests;

public class EventTests{

    @Test
    public static void toStringTest(){
        Case c = new Case(null, 0, 0);
        assert(c.toString() == "(0, 0)-");
    } 

}