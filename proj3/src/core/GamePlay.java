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
import java.util.*;

public class GamePlay {
    private static final int WINDOW_WIDTH = GameSettings.WINDOW_WIDTH;
    private static final int WINDOW_HEIGHT = GameSettings.WINDOW_HEIGHT;
    private static final String SAVE_FILE = GameSettings.SAVE_FILE;
    // Avatar position
    private static int avatarX = 0;
    private static int avatarY = 0;

    // Line of sight system
    private static boolean lineOfSightEnabled = true;

    // Enemy system
    private static boolean showPaths = false;
    private static java.util.List<Enemy> enemies = new ArrayList<>();

    static void startNewWorld() {
        // Prompt user for seed input
        long seed = promptForSeed();

        // Generate a new world with the provided seed
        TETile[][] world = World.generateWorld(WINDOW_WIDTH, WINDOW_HEIGHT, seed);

        // Place avatar on first floor tile
        placeAvatarOnFloor(world);
        
        // Initialize entities
        Enemy.initializeEnemies(world, enemies, avatarX, avatarY);

        // Display the world with avatar movement
        playWorld(world, seed, "New World Generated (Seed: " + seed + ")");
    }


    private static void playWorld(TETile[][] world, long seed, String title) {
        TERenderer ter = new TERenderer();
        ter.initialize(WINDOW_WIDTH, WINDOW_HEIGHT);

        boolean playing = true;
        boolean waitingForQ = false;
        while (playing) {
            // Clear old paths from world
            Enemy.clearPaths(world);
            
            // Update enEnemies
            Enemy.updateEnemies(world, enemies, avatarX, avatarY);
            
            // Draw entity paths if enabled
            if (showPaths) {
                Enemy.drawEnemyPaths(world, enemies);
            }
            
            // Create visibility mask
            boolean[][] visible = LineOfSight.calculateLineOfSight(world, lineOfSightEnabled, avatarX, avatarY);
            
            // Render the world with line of sight
            renderWorldWithLOS(world, visible, ter);

            // Draw HUD overlay
            HUD.drawHUD(world, title, lineOfSightEnabled, showPaths, avatarX, avatarY);

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
                        case 't':
                            // Toggle line of sight
                            lineOfSightEnabled = !lineOfSightEnabled;
                            break;
                        case 'p':
                            // Toggle path display
                            showPaths = !showPaths;
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

     static boolean isValidPosition(TETile[][] world, int x, int y) {
        if (x < 0 || x >= WINDOW_WIDTH || y < 0 || y >= WINDOW_HEIGHT) {
            return false;
        }
        TETile tile = world[x][y];
        return tile == Tileset.FLOOR_TILE;
    }

    private static void renderWorldWithLOS(TETile[][] world, boolean[][] visible, TERenderer ter) {
        if (!lineOfSightEnabled) {
            // If line of sight is disabled, render normally
            ter.renderFrame(world);
            return;
        }

        // Create a copy of the world with hidden tiles replaced
        TETile[][] visibleWorld = new TETile[WINDOW_WIDTH][WINDOW_HEIGHT];

        for (int x = 0; x < WINDOW_WIDTH; x++) {
            for (int y = 0; y < WINDOW_HEIGHT; y++) {
                if (visible[x][y]) {
                    visibleWorld[x][y] = world[x][y];
                } else {
                    // Replace hidden tiles with darkness
                    visibleWorld[x][y] = Tileset.NOTHING;
                }
            }
        }

        ter.renderFrame(visibleWorld);
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
    static void showError(String message) {
        // Show error message briefly
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.RED);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 2.0, message);
        StdDraw.show();
        StdDraw.pause(2000); // Show for 2 seconds
    }

    static void saveToFile(long seed, int x, int y) {
        try {
            FileWriter writer = new FileWriter(SAVE_FILE);
            writer.write(seed + "\n");
            writer.write(x + "\n");
            writer.write(y + "\n");
            writer.write(enemies.size() + "\n"); // Save number of entities
            for (Enemy enemy : enemies) {
                writer.write(enemy.x + "\n");
                writer.write(enemy.y + "\n");
            }
            writer.close();
        } catch (IOException e) {
            GamePlay.showError("Failed to save: " + e.getMessage());
        }
    }

    static String[] loadFromFile() {
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

    static void loadWorld() {
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

            // Load entities from save file
            Enemy.loadEnemiesFromFile(saveData, enemies);

            // Place entities in world
            for (Enemy enemy : enemies) {
                if (isValidPosition(world, enemy.x, enemy.y)) {
                    world[enemy.x][enemy.y] = Tileset.ENEMY;
                }
            }

            // Display the world with avatar movement
            playWorld(world, seed, "Loaded World (Seed: " + seed + ")");

        } catch (Exception e) {
            showError("Failed to load save file: " + e.getMessage());
        }
    }

}
