package pl.edu.agh.amber.navi.tool;

public abstract class KeyListener {

    public final int keyCode;

    public KeyListener(int keyCode) {
        this.keyCode = keyCode;
    }

    public abstract void keyPressed();
}
