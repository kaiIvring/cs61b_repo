package gameoflife;

import net.sf.saxon.expr.instruct.WithParam;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;


public class RenderingEngineTest {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 50;
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        for (int x = 0; x < WIDTH; x++) {
            world[x][0] = Tileset.FLOWER;
        }

        ter.renderFrame(world);
    }
}
