package pl.edu.agh.amber.navi.dto;

import pl.edu.agh.amber.navi.eye.hokuyo.MapPoint;
import pl.edu.agh.amber.navi.tool.ContinuumMap;

import java.util.List;

public final class NaviVisibility {

    private final ContinuumMap visibility;

    public NaviVisibility(ContinuumMap visibility) {
        this.visibility = visibility;
    }

    public NaviVisibility(List<MapPoint> points) {
        this.visibility = new ContinuumMap();
        for (MapPoint point : points) {
            visibility.put(point.getAngle(), point.getDistance());
        }
    }

    public double getLengthForAngle(double angle) {
        return visibility.get(angle);
    }

    public List<Double> getAnglesForLength(double length) {
        return visibility.getAngles(length);
    }
}
