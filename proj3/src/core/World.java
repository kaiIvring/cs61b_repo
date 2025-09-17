package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.*;

public class World {
    // build your own world!

    /**
     * Generates a pseudo-random world consisting of rectangular rooms and L-shaped hallways.
     * Floors and walls are visually distinct from empty space; no floors touch the outer border.
     */
    // the (0,0) coordinate is the bottom-left corner of the world
    public static TETile[][] generateWorld(int width, int height, long seed) {
        if (width < 5 || height < 5) {
            throw new IllegalArgumentException("World too small");
        }

        Random r = new Random(seed);
        // fill the world with nothing
        TETile[][] tiles = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }

        // define a rectangle class
        class Rect {
            // coordinates and dimensions
            // x, y represents the bottom-left corner of the rectangle
            int x, y, w, h;
            Rect(int x, int y, int w, int h) { 
                this.x = x; this.y = y; this.w = w; this.h = h; 
            }
            // center coordinates
            int cx() { return x + w / 2; }
            int cy() { return y + h / 2; }
            // check if the rectangles intersect
            boolean intersects(Rect other) {
                // intersects if the rectangles overlap
                return x < other.x + other.w + 1 && x + w + 1 > other.x &&
                       y < other.y + other.h + 1 && y + h + 1 > other.y;
            }
        }

        // generate the rooms
        int targetRooms = Math.max(10, (width * height) / 150);
        int maxRooms = targetRooms + 8;
        int attempts = 0;
        List<Rect> rooms = new ArrayList<>();
        // try to generate rooms until we have the maximum number of rooms or we have tried too many times
        while (rooms.size() < maxRooms && attempts < maxRooms * 25) {
            attempts++;
            // rw = random width of the room
            // rw & rh will grow as the world gets bigger
            int rw = 3 + r.nextInt(Math.max(3, Math.min(10, Math.max(3, width / 6))));
            int rh = 3 + r.nextInt(Math.max(3, Math.min(10, Math.max(3, height / 6))));
            // use width-rw-2 to make sure that rooms won't be clipped off the edge of the world
            int rx = 1 + r.nextInt(Math.max(1, width - rw - 2));
            int ry = 1 + r.nextInt(Math.max(1, height - rh - 2));
            Rect rect = new Rect(rx, ry, rw, rh);
            boolean ok = true;
            // check if the room intersects with any existing rooms
            for (Rect existing : rooms) {
                if (rect.intersects(existing)) {
                    ok = false;
                    break;
                }
            }
            // if the room intersects with any existing rooms, try again
            if (!ok) { 
                continue; 
            }
            // otherwise, add the room to the list of rooms
            rooms.add(rect);
            // fill the room with floors
            for (int x = rect.x; x < rect.x + rect.w; x++) {
                for (int y = rect.y; y < rect.y + rect.h; y++) {
                    tiles[x][y] = Tileset.FLOOR;
                }
            }
        }

        // if there are less than 2 rooms, generate a single room
        if (rooms.size() < 2) {
            int rw = Math.max(3, width / 4);
            int rh = Math.max(3, height / 4);
            int rx = Math.max(1, (width - rw) / 2);
            int ry = Math.max(1, (height - rh) / 2);
            for (int x = rx; x < rx + rw; x++) {
                for (int y = ry; y < ry + rh; y++) {
                    tiles[x][y] = Tileset.FLOOR;
                }
            }
            rooms.add(new Rect(rx, ry, rw, rh));
        }

        // === Prim Minimum Spanning Tree Connection ===
        int n = rooms.size();
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int d = Math.abs(rooms.get(i).cx() - rooms.get(j).cx())
                    + Math.abs(rooms.get(i).cy() - rooms.get(j).cy());
                dist[i][j] = dist[j][i] = d;
            }
        }

        // whether a room is in the tree
        boolean[] inTree = new boolean[n];
        // for every room that is not in the tree, the minimum distance to the tree
        int[] minDist = new int[n];
        // the parent of the room
        int[] parent = new int[n];
        // initialize the minimum distance to the tree to infinity
        Arrays.fill(minDist, Integer.MAX_VALUE);

        // build the MST using Prim's algorithm
        minDist[0] = 0;
        for (int k = 0; k < n; k++) {
            int u = -1;
            // find the room that is not in the tree and has the minimum distance to the tree
            for (int i = 0; i < n; i++) {
                if (!inTree[i] && (u == -1 || minDist[i] < minDist[u])) {
                    u = i;
                }
            }
            // add the room to the tree
            inTree[u] = true;
            // update the minimum distance to the tree for the rooms that are not in the tree
            for (int v = 0; v < n; v++) {
                // use the previous room added to the tree to update the minimum distance to the tree
                if (!inTree[v] && dist[u][v] < minDist[v]) {
                    minDist[v] = dist[u][v];
                    parent[v] = u;
                }
            }
        }

        // carve the corridors along the MST edges
        for (int v = 1; v < n; v++) {
            Rect a = rooms.get(v);
            Rect b = rooms.get(parent[v]);
            carveLCorridor(tiles, width, height, a.cx(), a.cy(), b.cx(), b.cy(), r);
        }

        // add the walls
        addWalls(tiles);
        return tiles;
    }

        // carve an L-shaped corridor
        private static void carveLCorridor(TETile[][] tiles, int width, int height,
                                        int x0, int y0, int x1, int y1, Random r) {
            x0 = Math.max(1, Math.min(width - 2, x0));
            y0 = Math.max(1, Math.min(height - 2, y0));
            x1 = Math.max(1, Math.min(width - 2, x1));
            y1 = Math.max(1, Math.min(height - 2, y1));

            boolean horizontalFirst = r.nextBoolean();
            if (horizontalFirst) {
                carveLineX(tiles, x0, x1, y0);
                carveLineY(tiles, y0, y1, x1);
            } else {
                carveLineY(tiles, y0, y1, x0);
                carveLineX(tiles, x0, x1, y1);
            }
        }

    // carve a horizontal line
    private static void carveLineX(TETile[][] tiles, int x0, int x1, int y) {
        int sx = x0 <= x1 ? 1 : -1;
        for (int x = x0; x != x1 + sx; x += sx) {
            tiles[x][y] = Tileset.FLOOR;
        }
    }

    // carve a vertical line
    private static void carveLineY(TETile[][] tiles, int y0, int y1, int x) {
        int sy = y0 <= y1 ? 1 : -1;
        for (int y = y0; y != y1 + sy; y += sy) {
            tiles[x][y] = Tileset.FLOOR;
        }
    }

    // add the walls
    private static void addWalls(TETile[][] tiles) {
        int w = tiles.length;
        int h = tiles[0].length;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (tiles[x][y] == Tileset.FLOOR) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int nx = x + dx, ny = y + dy;
                            if (nx < 0 || nx >= w || ny < 0 || ny >= h) { continue; }
                            if (tiles[nx][ny] == Tileset.NOTHING) {
                                tiles[nx][ny] = Tileset.WALL;
                            }
                        }
                    }
                }
            }
        }
        // add the walls to the outer edges
        int maxX = w - 1, maxY = h - 1;
        for (int x = 0; x < w; x++) {
            if (tiles[x][0] == Tileset.FLOOR) tiles[x][0] = Tileset.WALL;
            if (tiles[x][maxY] == Tileset.FLOOR) tiles[x][maxY] = Tileset.WALL;
        }
        for (int y = 0; y < h; y++) {
            if (tiles[0][y] == Tileset.FLOOR) tiles[0][y] = Tileset.WALL;
            if (tiles[maxX][y] == Tileset.FLOOR) tiles[maxX][y] = Tileset.WALL;
        }
    }

}
