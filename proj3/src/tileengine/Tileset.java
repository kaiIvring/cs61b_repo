package tileengine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.GRAY, "you", "src/image/mario.png",0);
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray, "wall", "src/image/brick_wall.png", 1);
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing", 3);
    public static final TETile GROUND = new TETile('G', null, null, "ground", "src/image/living_tissue.png", 14);
    public static final TETile FLOOR_TILE = new TETile('F', null, null, "floor", "src/image/floor.png", 15);
    // Enemy and path tiles
    public static final TETile ENEMY = new TETile('E', Color.red, Color.black, "enemy", "src/image/devil1.png", 17);
    public static final TETile PATH = new TETile('*', Color.red, Color.BLACK, "path", "src/image/Fire_20_16x16.png", 18);
    // apple tile
    public static final TETile APPLE = new TETile('A', Color.RED, Color.BLACK, "apple", "src/image/apple.png", 19);


    // unused tiles
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black, "floor", 2);
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass", 4);
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water", 5);
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower", 6);
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door", 7);
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door", 8);
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand", 9);
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain", 10);
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree", 11);

    public static final TETile CELL = new TETile('█', Color.white, Color.black, "cell", 12);

    public static final TETile WOOD_WALL = new TETile('W', null, null, "wooden wall", "src/image/wood_wall.png", 13);
    public static final TETile BRICK_WALL = new TETile('B', null, null, "brick wall", "src/image/brick_wall.png", 16);
    

}