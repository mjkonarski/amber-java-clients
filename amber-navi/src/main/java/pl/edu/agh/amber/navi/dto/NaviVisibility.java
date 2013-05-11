package pl.edu.agh.amber.navi.dto;

import pl.edu.agh.amber.navi.tool.ContinuumMap;

public final class NaviVisibility {

    private final ContinuumMap visibility;

    public NaviVisibility(ContinuumMap visibility) {
        this.visibility = visibility;
    }

    public Double getLengthForAngle(double angle) {
        return visibility.get(angle);
    }
}
