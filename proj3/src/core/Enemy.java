package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

public class Enemy {
    private static final int WINDOW_WIDTH = GameSettings.WINDOW_WIDTH;
    private static final int WINDOW_HEIGHT = GameSettings.WINDOW_HEIGHT;
    private static final int MOVE_DELAY_FRAMES = GameSettings.ENEMY_MOVE_DELAY_FRAMES; // enemy move delay

    int x, y;
    int targetX, targetY;
    List<int[]> path;
    private int moveCoolDown;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.path = new ArrayList<>();
        this.moveCoolDown = 0;
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

    public static void updateEnemies(TETile[][] world, java.util.List<Enemy> enemies, List<int[]> apples, int avatarX, int avatarY) {
        for (Enemy enemy : enemies) {

            // Update target to avatar position
            enemy.targetX = avatarX;
            enemy.targetY = avatarY;

            // Calculate path to avatar using BFS
            enemy.path = findPath(world, enemy.x, enemy.y, enemy.targetX, enemy.targetY);

            // enemy is still in cool down situation
            // draw the enemy to the world but don't move it
            if (enemy.moveCoolDown > 0) {
                enemy.moveCoolDown--;
                world[enemy.x][enemy.y] = Tileset.ENEMY;
                continue;
            }

            // Move enemy along path
            if (!enemy.path.isEmpty()) {
                int[] nextPos = enemy.path.get(0);
                enemy.path.remove(0);

                if (nextPos[0] == avatarX && nextPos[1] == avatarY) {
                    GamePlay.setPlayerCaught(true);
                } else if (GamePlay.isValidPosition(world, nextPos[0], nextPos[1])
                        || GamePlay.isApple(world, nextPos[0], nextPos[1])) {
                    // Clear old position
                    if (world[enemy.x][enemy.y] == Tileset.ENEMY) {
                        // if old position was apple, replace the old position with apple
                        boolean wasApple = false;
                        for (int[] apple : apples) {
                            if (apple[0] == enemy.x && apple[1] == enemy.y) {
                                wasApple = true;
                                break;
                            }
                        }
                        world[enemy.x][enemy.y] = wasApple ? Tileset.APPLE : Tileset.FLOOR_TILE;
                    }
                    // Move to new position
                    enemy.x = nextPos[0];
                    enemy.y = nextPos[1];
                }
            }

            enemy.moveCoolDown = MOVE_DELAY_FRAMES;

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
                }
                return path;
            }

            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                String newKey = newX + "," + newY;

                // the enemy can reach apple
                if ((newX == targetX && newY == targetY || GamePlay.isValidPosition(world, newX, newY) || GamePlay.isApple(world, newX, newY))
                        && !visited.contains(newKey)) {
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
