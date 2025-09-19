import core.AutograderBuddy;
import edu.princeton.cs.algs4.StdDraw;
import org.junit.jupiter.api.Test;
import tileengine.TERenderer;
import tileengine.TETile;

public class WorldGenTests {
    @Test
    public void basicTest() {
        // put different seeds here to test different worlds
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n123s");

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(tiles);
        StdDraw.pause(3000); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicInteractivityTest() {
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n123swasdwasd");

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(tiles);
        StdDraw.pause(3000); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicSaveTest() {
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n123swasd:q");

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(tiles);
        StdDraw.pause(3000); // pause for 5 seconds so you can see the output

        tiles = AutograderBuddy.getWorldFromInput("lwasddds");
        ter.renderFrame(tiles);
        StdDraw.pause(3000); // pause for 5 seconds so you can see the output
    }
}
