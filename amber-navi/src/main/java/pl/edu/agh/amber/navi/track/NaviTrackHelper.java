package pl.edu.agh.amber.navi.track;

import pl.edu.agh.amber.navi.agent.NaviPoint;

/**
 * Provides information from tracking system like GPS or StarGazer.
 */
public abstract class NaviTrackHelper {

    public abstract NaviPoint getCurrentLocation();
}
