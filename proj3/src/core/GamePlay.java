package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GamePlay {
    private static final int WINDOW_WIDTH = 60;
    private static final int WINDOW_HEIGHT = 40;
    // Avatar position
    private static int avatarX = 0;
    private static int avatarY = 0;
    private static final String SAVE_FILE = "src/save.txt";

    public static void startNewWorld() {
        // Prompt user for seed input
        long seed = promptForSeed();

        // Generate a new world with the provided seed
        TETile[][] world = World.generateWorld(WINDOW_WIDTH, WINDOW_HEIGHT, seed);

        // Place avatar on first floor tile
        placeAvatarOnFloor(world);

        // Display the world with avatar movement
        playWorld(world, seed, "New World Generated (Seed: " + seed + ")");
    }

    private static long promptForSeed() {
        StringBuilder seedInput = new StringBuilder();
        boolean inputComplete = false;

        while (!inputComplete) {
            // Clear screen
            StdDraw.clear(Color.BLACK);

            // Draw prompt
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 24));
            StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT * 0.7, "Enter Random Seed:");

            // Draw current input
            StdDraw.setFont(new Font("Monaco", Font.PLAIN, 20));
            String displayText = seedInput.length() == 0 ? "Type numbers and press Enter..." : seedInput.toString();
            StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT * 0.5, displayText);

            // Draw instructions
            StdDraw.setFont(new Font("Monaco", Font.PLAIN, 14));
            StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT * 0.3, "Type numbers and press 's' to confirm");
            StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT * 0.25, "Press Backspace to delete, Escape to cancel");

            StdDraw.show();

            // Handle key input
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();

                if (key == 's' || key == 'S') {
                    // Enter pressed - try to parse the seed
                    if (seedInput.length() > 0) {
                        try {
                            long seed = Long.parseLong(seedInput.toString());
                            inputComplete = true;
                            return seed;
                        } catch (NumberFormatException e) {
                            // Invalid number, show error and continue
                            showError("Invalid number format. Please try again.");
                            seedInput.setLength(0);
                        }
                    }
                } else if (key == '\b') {
                    // Backspace pressed
                    if (seedInput.length() > 0) {
                        seedInput.setLength(seedInput.length() - 1);
                    }
                } else if (key == 27) {
                    // Escape pressed - return to menu
                    return System.currentTimeMillis(); // Use current time as default
                } else if (Character.isDigit(key)) {
                    // Add digit to input
                    seedInput.append(key);
                }
            }

            StdDraw.pause(50);
        }

        return System.currentTimeMillis(); // Fallback
    }

    private static void showError(String message) {
        // Show error message briefly
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.RED);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 2.0, message);
        StdDraw.show();
        StdDraw.pause(2000); // Show for 2 seconds
    }

    public static void loadWorld() {
        // Load world from save file
        try {
            String[] saveData = loadFromFile();
            if (saveData == null) {
                showError("No save file found!");
                return;
            }

            long seed = Long.parseLong(saveData[0]);
            avatarX = Integer.parseInt(saveData[1]);
            avatarY = Integer.parseInt(saveData[2]);

            // Generate the same world with the saved seed
            TETile[][] world = World.generateWorld(WINDOW_WIDTH, WINDOW_HEIGHT, seed);

            // Place avatar at saved position
            if (isValidPosition(world, avatarX, avatarY)) {
                world[avatarX][avatarY] = Tileset.AVATAR;
            } else {
                placeAvatarOnFloor(world);
            }

            // Display the world with avatar movement
            playWorld(world, seed, "Loaded World (Seed: " + seed + ")");

        } catch (Exception e) {
            showError("Failed to load save file: " + e.getMessage());
        }
    }

    private static void playWorld(TETile[][] world, long seed, String title) {
        TERenderer ter = new TERenderer();
        ter.initialize(WINDOW_WIDTH, WINDOW_HEIGHT);

        boolean playing = true;
        boolean waitingForQ = false;
        while (playing) {
            // Render the world
            ter.renderFrame(world);

            // Draw HUD overlay
            drawHUD(world, title);

            // Handle key input
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());

                if (waitingForQ) {
                    if (key == 'q' || key == 'Q') {
                        saveToFile(seed, avatarX, avatarY);
                        playing = false;
                    }
                    waitingForQ = false;
                } else {
                    switch (key) {
                        case 'w':
                            moveAvatar(world, 0, 1);
                            break;
                        case 'a':
                            moveAvatar(world, -1, 0);
                            break;
                        case 's':
                            moveAvatar(world, 0, -1);
                            break;
                        case 'd':
                            moveAvatar(world, 1, 0);
                            break;
                        case ':':
                            // Wait for 'q' after ':'
                            waitingForQ = true;
                            break;
                        case 27: // Escape
                            playing = false;
                            break;
                    }
                }
            }
            StdDraw.pause(50);
        }
    }
    
    private static void drawHUD(TETile[][] world, String title) {
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
        StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT - 2, "WASD: Move | :Q: Save & Quit | ESC: Quit without saving");
        
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

    private static void moveAvatar(TETile[][] world, int dx, int dy) {
        int newX = avatarX + dx;
        int newY = avatarY + dy;

        // Check bounds
        if (newX < 0 || newX >= WINDOW_WIDTH || newY < 0 || newY >= WINDOW_HEIGHT) {
            return;
        }

        // Check if new position is valid (floor)
        if (isValidPosition(world, newX, newY)) {
            // Clear old position
            world[avatarX][avatarY] = Tileset.FLOOR_TILE;

            // Move avatar
            avatarX = newX;
            avatarY = newY;
            world[avatarX][avatarY] = Tileset.AVATAR;
        }
    }

    public static boolean isValidPosition(TETile[][] world, int x, int y) {
        if (x < 0 || x >= WINDOW_WIDTH || y < 0 || y >= WINDOW_HEIGHT) {
            return false;
        }
        TETile tile = world[x][y];
        return tile == Tileset.FLOOR_TILE;
    }

    private static void placeAvatarOnFloor(TETile[][] world) {
        // Find first floor tile and place avatar there
        for (int x = 0; x < WINDOW_WIDTH; x++) {
            for (int y = 0; y < WINDOW_HEIGHT; y++) {
                if (world[x][y] == Tileset.FLOOR_TILE) {
                    avatarX = x;
                    avatarY = y;
                    world[x][y] = Tileset.AVATAR;
                    return;
                }
            }
        }
        // Fallback: place at center if no floor found
        avatarX = WINDOW_WIDTH / 2;
        avatarY = WINDOW_HEIGHT / 2;
        world[avatarX][avatarY] = Tileset.AVATAR;
    }

    public static void saveToFile(long seed, int x, int y) {
        try {
            FileWriter writer = new FileWriter(SAVE_FILE);
            writer.write(seed + "\n");
            writer.write(x + "\n");
            writer.write(y + "\n");
            writer.close();
        } catch (IOException e) {
            showError("Failed to save: " + e.getMessage());
        }
    }

    public static String[] loadFromFile() {
        try {
            if (!Files.exists(Paths.get(SAVE_FILE))) {
                return null;
            }

            String content = new String(Files.readAllBytes(Paths.get(SAVE_FILE)));
            String[] lines = content.trim().split("\n");

            if (lines.length >= 3) {
                return lines;
            }
        } catch (IOException e) {
            showError("Failed to load: " + e.getMessage());
        }
        return null;
    }
}
