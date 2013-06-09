package pl.edu.agh.amber.navi.track;

import pl.edu.agh.amber.navi.NaviHelper;
import pl.edu.agh.amber.navi.dto.NaviPoint;

/**
 * Provides information from tracking system like GPS or StarGazer.
 */
public abstract class NaviTrackHelper extends NaviHelper {

    private final Object locationLock = new Object();

    private NaviPoint location;

    public NaviPoint getLocation() {
        synchronized (locationLock) {
            return location;
        }
    }

    @Override
    protected void notifyLocationChange(NaviPoint location) {
        synchronized (locationLock) {
            this.location = location;
        }
        super.notifyLocationChange(location);
    }
}
