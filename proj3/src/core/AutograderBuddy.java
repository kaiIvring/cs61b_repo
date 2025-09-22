package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AutograderBuddy {
    private static final int WINDOW_WIDTH = 70;
    private static final int WINDOW_HEIGHT = 60;
    private static int avatarX = 0;
    private static int avatarY = 0;

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {
        if (input == null || input.length() == 0) {
            throw new IllegalArgumentException("input is empty");
        }

        String s = input.trim();
        int index = 0;
        
        // Parse the input string
        if (s.charAt(index) == 'n' || s.charAt(index) == 'N') {
            // New world command
            index++;
            StringBuilder seedStr = new StringBuilder();
            while (index < s.length() && Character.isDigit(s.charAt(index))) {
                seedStr.append(s.charAt(index));
                index++;
            }
            
            if (seedStr.length() == 0) {
                throw new IllegalArgumentException("missing seed");
            }
            
            long seed = Long.parseLong(seedStr.toString());
            TETile[][] world = World.generateWorld(WINDOW_WIDTH, WINDOW_HEIGHT, seed);
            
            // Place avatar on first floor tile
            placeAvatarOnFloor(world);

            // Process remaining commands
            return processCommands(world, s.substring(index), seed, avatarX, avatarY);
            
        } else if (s.charAt(index) == 'l' || s.charAt(index) == 'L') {
            // Load world command
            index++;
            String[] saveData = GamePlay.loadFromFile();
            if (saveData == null) {
                throw new IllegalArgumentException("No save file found");
            }
            
            long seed = Long.parseLong(saveData[0]);
            avatarX = Integer.parseInt(saveData[1]);
            avatarY = Integer.parseInt(saveData[2]);
            
            TETile[][] world = World.generateWorld(WINDOW_WIDTH, WINDOW_HEIGHT, seed);
            world[avatarX][avatarY] = Tileset.AVATAR;
            
            // Process remaining commands
            return processCommands(world, s.substring(index), seed, avatarX, avatarY);
            
        } else {
            throw new IllegalArgumentException("input must start with 'n' or 'l'");
        }
    }
    
    private static TETile[][] processCommands(TETile[][] world, String commands, long seed, int avatarX, int avatarY) {
        int index = 0;
        
        while (index < commands.length()) {
            char command = Character.toLowerCase(commands.charAt(index));
            
            switch (command) {
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
                    // Check for :q save command
                    if (index + 1 < commands.length() && 
                        Character.toLowerCase(commands.charAt(index + 1)) == 'q') {
                        GamePlay.saveToFile(seed, avatarX, avatarY);
                        index++; // Skip the 'q'
                    }
                    break;
                default:
                    // Ignore unknown commands
                    break;
            }
            index++;
        }
        
        return world;
    }

    private static void moveAvatar(TETile[][] world, int dx, int dy) {
        int newX = avatarX + dx;
        int newY = avatarY + dy;

        // Check bounds
        if (newX < 0 || newX >= WINDOW_WIDTH || newY < 0 || newY >= WINDOW_HEIGHT) {
            return;
        }

        // Check if new position is valid (floor)
        if (GamePlay.isValidPosition(world, newX, newY)) {
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

    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOWER.character()
                || t.character() == Tileset.FLOOR_TILE.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character()
                || t.character() == Tileset.BRICK_WALL.character()
                || t.character() == Tileset.WOOD_WALL.character();
    }
}
