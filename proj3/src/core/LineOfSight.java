package core;

import tileengine.TETile;
import tileengine.Tileset;

public class LineOfSight {
    private static final int WINDOW_WIDTH = GameSettings.WINDOW_WIDTH;
    private static final int WINDOW_HEIGHT = GameSettings.WINDOW_HEIGHT;
    private static final int SIGHT_RANGE = 8; // Maximum sight range

    static boolean[][] calculateLineOfSight(TETile[][] world,boolean lineOfSightEnabled, int avatarX, int avatarY) {
        boolean[][] visible = new boolean[WINDOW_WIDTH][WINDOW_HEIGHT];

        if (!lineOfSightEnabled) {
            // If line of sight is disabled, all tiles are visible
            for (int x = 0; x < WINDOW_WIDTH; x++) {
                for (int y = 0; y < WINDOW_HEIGHT; y++) {
                    visible[x][y] = true;
                }
            }
            return visible;
        }

        // Avatar can always see its own position
        visible[avatarX][avatarY] = true;

        // Cast rays in all directions
        for (int angle = 0; angle < 360; angle += 2) {
            castRay(world, visible, avatarX, avatarY, angle);
        }

        return visible;
    }

    private static void castRay(TETile[][] world, boolean[][] visible, int startX, int startY, double angle) {
        double radians = Math.toRadians(angle);
        double dx = Math.cos(radians);
        double dy = Math.sin(radians);

        double x = startX + 0.5; // Start from center of tile
        double y = startY + 0.5;

        for (int step = 0; step < SIGHT_RANGE * 2; step++) {
            int tileX = (int) Math.floor(x);
            int tileY = (int) Math.floor(y);

            // Check bounds
            if (tileX < 0 || tileX >= WINDOW_WIDTH || tileY < 0 || tileY >= WINDOW_HEIGHT) {
                break;
            }

            // Mark this tile as visible
            visible[tileX][tileY] = true;

            // Check if we hit a wall
            if (world[tileX][tileY] == Tileset.WALL ||
                    world[tileX][tileY] == Tileset.BRICK_WALL ||
                    world[tileX][tileY] == Tileset.WOOD_WALL) {
                break;
            }

            // Move to next position
            x += dx * 0.5;
            y += dy * 0.5;
        }
    }
}
