package pl.edu.agh.amber.navi.drive;

import pl.edu.agh.amber.navi.agent.NaviAdjustment;

import java.io.IOException;

/**
 * Provide access to drive system.
 */
public abstract class NaviDriveHelper {

    public abstract void change(double changeAngle, double changeSpeed) throws IOException;

    public abstract void change(NaviAdjustment adjustment) throws IOException;
}
