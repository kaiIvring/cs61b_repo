package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_HEIGHT = 50;
    private TERenderer ter;
    private Random random;

    /**
     * Fills the 2D array of tiles with NOTHING tiles.
     * @param tiles
     */
    public void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    // build your own world!

    /**
     * Generates a pseudo-random world consisting of rectangular rooms and L-shaped hallways.
     * Floors and walls are visually distinct from empty space; no floors touch the outer border.
     */
    public static TETile[][] generateWorld(int width, int height, long seed) {
        if (width < 5 || height < 5) {
            throw new IllegalArgumentException("World too small");
        }

        Random r = new Random(seed);
        TETile[][] tiles = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }

        class Rect {
            int x, y, w, h;
            Rect(int x, int y, int w, int h) { this.x = x; this.y = y; this.w = w; this.h = h; }
            int cx() { return x + w / 2; }
            int cy() { return y + h / 2; }
            boolean intersects(Rect other) {
                return x < other.x + other.w + 1 && x + w + 1 > other.x &&
                       y < other.y + other.h + 1 && y + h + 1 > other.y;
            }
        }

        int targetRooms = Math.max(10, (width * height) / 150);
        int maxRooms = targetRooms + 8;
        int attempts = 0;
        List<Rect> rooms = new ArrayList<>();
        while (rooms.size() < maxRooms && attempts < maxRooms * 25) {
            attempts++;
            int rw = 3 + r.nextInt(Math.max(3, Math.min(10, Math.max(3, width / 6))));
            int rh = 3 + r.nextInt(Math.max(3, Math.min(10, Math.max(3, height / 6))));
            int rx = 1 + r.nextInt(Math.max(1, width - rw - 2));
            int ry = 1 + r.nextInt(Math.max(1, height - rh - 2));
            Rect rect = new Rect(rx, ry, rw, rh);
            boolean ok = true;
            for (Rect existing : rooms) {
                if (rect.intersects(existing)) { ok = false; break; }
            }
            if (!ok) { continue; }
            rooms.add(rect);
            for (int x = rect.x; x < rect.x + rect.w; x++) {
                for (int y = rect.y; y < rect.y + rect.h; y++) {
                    tiles[x][y] = Tileset.FLOOR;
                }
            }
        }

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

        Collections.shuffle(rooms, r);
        for (int i = 1; i < rooms.size(); i++) {
            Rect a = rooms.get(i);
            Rect best = null;
            int bestDist = Integer.MAX_VALUE;
            for (int j = 0; j < i; j++) {
                Rect b = rooms.get(j);
                int d = Math.abs(a.cx() - b.cx()) + Math.abs(a.cy() - b.cy());
                if (d < bestDist) { bestDist = d; best = b; }
            }
            if (best != null) {
                carveLCorridor(tiles, width, height, a.cx(), a.cy(), best.cx(), best.cy(), r);
            }
        }

        addWalls(tiles);
        return tiles;
    }

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

    private static void carveLineX(TETile[][] tiles, int x0, int x1, int y) {
        int sx = x0 <= x1 ? 1 : -1;
        for (int x = x0; x != x1 + sx; x += sx) {
            tiles[x][y] = Tileset.FLOOR;
        }
    }

    private static void carveLineY(TETile[][] tiles, int y0, int y1, int x) {
        int sy = y0 <= y1 ? 1 : -1;
        for (int y = y0; y != y1 + sy; y += sy) {
            tiles[x][y] = Tileset.FLOOR;
        }
    }

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
