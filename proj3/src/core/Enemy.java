package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

public class Enemy {
    private static final int WINDOW_WIDTH = GameSettings.WINDOW_WIDTH;
    private static final int WINDOW_HEIGHT = GameSettings.WINDOW_HEIGHT;

    int x, y;
    int targetX, targetY;
    List<int[]> path;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.path = new ArrayList<>();
    }

    public static void initializeEnemies(TETile[][] world, java.util.List<Enemy> enemies, int avatarX, int avatarY) {
        enemies.clear();

        // Find floor tiles to place entities
        List<int[]> floorTiles = new ArrayList<>();
        for (int x = 0; x < WINDOW_WIDTH; x++) {
            for (int y = 0; y < WINDOW_HEIGHT; y++) {
                if (world[x][y] == Tileset.FLOOR_TILE && !(x == avatarX && y == avatarY)) {
                    floorTiles.add(new int[]{x, y});
                }
            }
        }

        if (floorTiles.isEmpty()) {
            return;
        }

        Random rand = new Random();
        int[] pos = floorTiles.get(rand.nextInt(floorTiles.size()));

        // add one enemy
        Enemy enemy = new Enemy(pos[0], pos[1]);
        enemies.add(enemy);


        // Place enemy in world
        world[pos[0]][pos[1]] = Tileset.ENEMY;
    }

    public static void loadEnemiesFromFile(String[] saveData, java.util.List<Enemy> enemies) {
        enemies.clear();

        if (saveData.length >= 4) {
            try {
                int numEnemies = Integer.parseInt(saveData[3]);
                if (numEnemies > 0 && saveData.length >= 6) {
                    int x = Integer.parseInt(saveData[4]);
                    int y = Integer.parseInt(saveData[5]);
                    enemies.add(new Enemy(x, y));
                }
            } catch (NumberFormatException e) {
                // If parsing fails, initialize with default enemies
                enemies.clear();
            }
        }
    }

    public static void updateEnemies(TETile[][] world, java.util.List<Enemy> enemies,int avatarX, int avatarY) {
        for (Enemy enemy : enemies) {
            // Update target to avatar position
            enemy.targetX = avatarX;
            enemy.targetY = avatarY;

            // Calculate path to avatar using BFS
            enemy.path = findPath(world, enemy.x, enemy.y, enemy.targetX, enemy.targetY);

            // Move entity along path
            if (!enemy.path.isEmpty()) {
                int[] nextPos = enemy.path.get(0);
                enemy.path.remove(0);

                // Check if next position is valid and not occupied by avatar
                if (GamePlay.isValidPosition(world, nextPos[0], nextPos[1]) &&
                        !(nextPos[0] == avatarX && nextPos[1] == avatarY)) {

                    // Clear old position
                    if (world[enemy.x][enemy.y] == Tileset.ENEMY) {
                        world[enemy.x][enemy.y] = Tileset.FLOOR_TILE;
                    }

                    // Move to new position
                    enemy.x = nextPos[0];
                    enemy.y = nextPos[1];
                }
            }

            // Always ensure enemy is displayed at its current position
            world[enemy.x][enemy.y] = Tileset.ENEMY;
        }
    }

    public static List<int[]> findPath(TETile[][] world, int startX, int startY, int targetX, int targetY) {

        if (startX == targetX && startY == targetY) {
            return new ArrayList<>();
        }

        // BFS pathfinding algorithm

        // every element is an array: {x, y} represents a coordinate in the world
        Queue<int[]> queue = new LinkedList<>();
        // every element is a map: "x, y"(current) -> {x, y}(parent)
        Map<String, int[]> parent = new HashMap<>();
        // every element is a String: "x, y"
        Set<String> visited = new HashSet<>();

        queue.offer(new int[]{startX, startY});
        visited.add(startX + "," + startY);

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // Up, Right, Down, Left

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (x == targetX && y == targetY) {
                // reached target
                // Reconstruct path
                List<int[]> path = new ArrayList<>();
                String key = targetX + "," + targetY;

                while (parent.containsKey(key)) {
                    String[] parts = key.split(",");
                    int px = Integer.parseInt(parts[0]);
                    int py = Integer.parseInt(parts[1]);
                    path.add(0, new int[]{px, py});
                    int[] par = parent.get(key);
                    key = par[0] + "," + par[1];
//                    int[] pos = parent.get(key);
//                    path.add(0, pos);
//                    key = pos[0] + "," + pos[1];
                }
                return path;
            }

            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                String newKey = newX + "," + newY;

                if ((newX == targetX && newY == targetY || GamePlay.isValidPosition(world, newX, newY)) && !visited.contains(newKey)) {
                    visited.add(newKey);
                    queue.offer(new int[]{newX, newY});
                    parent.put(newKey, new int[]{x, y});
                }
            }
        }

        return new ArrayList<>(); // No path found
    }

    public static void clearPaths(TETile[][] world) {
        // Clear all PATH tiles and restore to FLOOR
        for (int x = 0; x < WINDOW_WIDTH; x++) {
            for (int y = 0; y < WINDOW_HEIGHT; y++) {
                if (world[x][y] == Tileset.PATH) {
                    world[x][y] = Tileset.FLOOR_TILE;
                }
            }
        }
    }

    public static void drawEnemyPaths(TETile[][] world, java.util.List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (!enemy.path.isEmpty()) {
                // Draw path using PATH tileset
                for (int[] pos : enemy.path) {
                    // Only draw path if it's not occupied by enemy or avatar
                    if (world[pos[0]][pos[1]] == Tileset.FLOOR_TILE) {
                        world[pos[0]][pos[1]] = Tileset.PATH;
                    }
                }
            }
        }
    }
}
