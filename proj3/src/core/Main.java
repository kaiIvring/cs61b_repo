package core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;

public class Main {
    private static final int WINDOW_WIDTH = 60;
    private static final int WINDOW_HEIGHT = 40;
    public static void main(String[] args) {
        // Show main menu
        // Initialize StdDraw
        StdDraw.setCanvasSize(WINDOW_WIDTH * 16, WINDOW_HEIGHT * 16);
        StdDraw.setXscale(0, WINDOW_WIDTH);
        StdDraw.setYscale(0, WINDOW_HEIGHT);
        StdDraw.enableDoubleBuffering();

        while (true) {
            // Clear screen
            StdDraw.clear(Color.BLACK);

            // Draw title
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
            StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT * 0.8, "CS61B Proj3");
            StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT * 0.75, "World Generator");

            // Draw menu options
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
            StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT * 0.5, "N - New World");
            StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT * 0.4, "L - Load World");
            StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT * 0.3, "Q - Quit");

            // Draw instructions
            StdDraw.setFont(new Font("Monaco", Font.PLAIN, 12));
            StdDraw.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT * 0.15, "Press a key to select an option");

            StdDraw.show();

            // Wait for key press
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());

                switch (key) {
                    case 'n':
                        GamePlay.startNewWorld();
                        break;
                    case 'l':
                        GamePlay.loadWorld();
                        break;
                    case 'q':
                        System.exit(0);
                        break;
                    default:
                        // Invalid key, continue loop
                        break;
                }
            }
            // Small delay to prevent excessive CPU usage
            StdDraw.pause(50);
        }
    }

}
