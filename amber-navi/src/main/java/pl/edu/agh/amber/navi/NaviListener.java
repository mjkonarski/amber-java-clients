package pl.edu.agh.amber.navi;

import pl.edu.agh.amber.navi.dto.NaviMovement;
import pl.edu.agh.amber.navi.dto.NaviPoint;
import pl.edu.agh.amber.navi.dto.NaviVisibility;

public interface NaviListener {

    public void visibilityChanged(NaviVisibility range);

    public void locationChanged(NaviPoint location);

    public void movementChanged(NaviMovement movement);
}
