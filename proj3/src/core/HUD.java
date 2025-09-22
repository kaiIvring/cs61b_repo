package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;

import java.awt.*;

public class HUD {
    private static final int WINDOW_WIDTH = GameSettings.WINDOW_WIDTH;
    private static final int WINDOW_HEIGHT = GameSettings.WINDOW_HEIGHT;
    public static void drawHUD(TETile[][] world, String title, boolean lineOfSightEnabled, boolean showPaths,int avatarX, int avatarY) {
        // Get mouse position
        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();

        // Convert mouse coordinates to tile coordinates
        int tileX = (int) Math.floor(mouseX);
        int tileY = (int) Math.floor(mouseY);

        // Get tile information
        String tileInfo = getTileInfo(world, tileX, tileY);

        // Draw semi-transparent background for HUD
        StdDraw.setPenColor(new Color(0, 0, 0, 150)); // Semi-transparent black
        StdDraw.filledRectangle(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT - 1.5, WINDOW_WIDTH / 2.0, 1.0);

        // Draw title
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 16));
        StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT - 1, title);

        // Draw controls
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 12));
        StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT - 2, "WASD: Move | T: Toggle LOS | P: Toggle Paths | :Q: Save & Quit | ESC: Quit without saving");

        // Draw tile information at mouse position
        if (tileX >= 0 && tileX < WINDOW_WIDTH && tileY >= 0 && tileY < WINDOW_HEIGHT) {
            // Draw background for tile info
            StdDraw.setPenColor(new Color(0, 0, 0, 200));
            StdDraw.filledRectangle(mouseX + 1.5, mouseY + 1.5, 3.0, 0.8);

            // Draw tile info text
            StdDraw.setPenColor(Color.YELLOW);
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 12));
            StdDraw.text(mouseX + 1.5, mouseY + 1.5, tileInfo);
        }

        // Draw avatar position info
        StdDraw.setPenColor(new Color(0, 0, 0, 150));
        StdDraw.filledRectangle(8.0, 1.5, 8.0, 1.0);
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 10));
        StdDraw.text(8.0, 1.5, "Avatar: (" + avatarX + ", " + avatarY + ")");

        // Draw line of sight status
        StdDraw.setPenColor(new Color(0, 0, 0, 150));
        StdDraw.filledRectangle(WINDOW_WIDTH - 8.0, 1.5, 8.0, 1.0);
        StdDraw.setPenColor(lineOfSightEnabled ? Color.GREEN : Color.RED);
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 10));
        StdDraw.text(WINDOW_WIDTH - 8.0, 2.5, "LOS: " + (lineOfSightEnabled ? "ON" : "OFF"));
        StdDraw.text(WINDOW_WIDTH - 8.0, 2.0, "Press T to toggle");

        // Draw path display status
        StdDraw.setPenColor(new Color(0, 0, 0, 150));
        StdDraw.filledRectangle(WINDOW_WIDTH - 8.0, 0.5, 8.0, 1.0);
        StdDraw.setPenColor(showPaths ? Color.YELLOW : Color.GRAY);
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 10));
        StdDraw.text(WINDOW_WIDTH - 8.0, 0.5, "Paths: " + (showPaths ? "ON" : "OFF"));
        StdDraw.text(WINDOW_WIDTH - 8.0, 0.0, "Press P to toggle");

        StdDraw.show();
    }

    private static String getTileInfo(TETile[][] world, int x, int y) {
        if (x < 0 || x >= WINDOW_WIDTH || y < 0 || y >= WINDOW_HEIGHT) {
            return "Out of bounds";
        }

        TETile tile = world[x][y];
        if (tile == null) {
            return "Null tile";
        }

        // Get tile description
        String description = tile.description();
        if (description == null || description.isEmpty()) {
            description = "Unknown";
        }

        // Add coordinates
        return description + " (" + x + ", " + y + ")";
    }
}
