package pl.edu.agh.amber.navi.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ConsoleHelper {

    private static final List<KeyListener> LISTENERS = new LinkedList<KeyListener>();

    private static final List<KeyEvent> EVENTS = Collections.synchronizedList(new LinkedList<KeyEvent>());

    private static final Thread NOTIFIER = new Thread() {
        @Override
        public void run() {
            KeyEvent event;
            try {
                while (true) {
                    synchronized (EVENTS) {
                        while (EVENTS.isEmpty()) {
                            EVENTS.wait();
                        }
                        event = EVENTS.remove(0);
                    }
                    for (KeyListener listener : LISTENERS) {
                        if (listener.keyCode == event.keyCode) {
                            listener.keyPressed();
                        }
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted");
            }
        }
    };

    public static void addListener(KeyListener listener) {
        LISTENERS.add(listener);
    }

    public static void removeListener(KeyListener listener) {
        LISTENERS.remove(listener);
    }

    public static void main() {
        int c;
        NOTIFIER.start();
        try {
            enableRAWMode();
            do {
                c = System.in.read();
                synchronized (EVENTS) {
                    EVENTS.add(new KeyEvent(c));
                    EVENTS.notifyAll();
                }
            } while (c != 'q');
            System.out.println("Bye");
        } catch (Exception e0) {
            System.err.println("Error during reading from keyboard");
            e0.printStackTrace();
        } finally {
            NOTIFIER.interrupt();
            try {
                disableRAWMode();
            } catch (Exception e1) {
                System.err.println("Error during resetting console");
                e1.printStackTrace();
            }
        }

    }


    public static void enableRAWMode() throws IOException, InterruptedException {
        stty("-g");
        // set the console to be character-buffered instead of line-buffered
        stty("-icanon min 1");
        // disable character echoing
        stty("-echo");
    }

    public static void disableRAWMode() throws IOException, InterruptedException {
        // enable character echoing
        stty("echo");
    }

    private static String stty(final String args) throws IOException, InterruptedException {
        String cmd = "stty " + args + " < /dev/tty";

        return exec(new String[]{
                "sh",
                "-c",
                cmd
        });
    }

    private static String exec(final String[] cmd) throws IOException, InterruptedException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        Process p = Runtime.getRuntime().exec(cmd);
        int c;
        InputStream in = p.getInputStream();

        while ((c = in.read()) != -1) {
            bout.write(c);
        }

        in = p.getErrorStream();

        while ((c = in.read()) != -1) {
            bout.write(c);
        }

        p.waitFor();

        return new String(bout.toByteArray());
    }
}
