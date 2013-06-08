package pl.edu.agh.amber.navi.app;

import pl.edu.agh.amber.navi.tool.ConsoleHelper;
import pl.edu.agh.amber.navi.tool.KeyListener;

public class ConsoleMain {

    public static void main(String[] args) {
        KeyListener listener;
        listener = new KeyListener('a') {
            @Override
            public void keyPressed() {
                System.err.println("Speed up");
            }
        };
        ConsoleHelper.addListener(listener);
        listener = new KeyListener('z') {
            @Override
            public void keyPressed() {
                System.err.println("Speed down");
            }
        };
        ConsoleHelper.addListener(listener);
        listener = new KeyListener('k') {
            @Override
            public void keyPressed() {
                System.err.println("Turn left");
            }
        };
        ConsoleHelper.addListener(listener);
        listener = new KeyListener('l') {
            @Override
            public void keyPressed() {
                System.err.println("Turn right");
            }
        };
        ConsoleHelper.addListener(listener);
        ConsoleHelper.main();
    }
}
