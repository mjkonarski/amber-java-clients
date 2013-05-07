package pl.edu.agh.amber.navi;

import pl.edu.agh.amber.navi.dto.NaviMovement;
import pl.edu.agh.amber.navi.dto.NaviPoint;
import pl.edu.agh.amber.navi.dto.NaviVisibility;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class NaviHelper {

    private final List<NaviListener> listeners = Collections.synchronizedList(new LinkedList<NaviListener>());

    public void addListener(NaviListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeListener(NaviListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    protected void notifyVisibilityChange(NaviVisibility range) {
        synchronized (listeners) {
            for (NaviListener listener : listeners) {
                listener.visibilityChanged(range);
            }
        }
    }

    protected void notifyLocationChange(NaviPoint location) {
        synchronized (listeners) {
            for (NaviListener listener : listeners) {
                listener.locationChanged(location);
            }
        }
    }

    protected void notifyMovementChange(NaviMovement movement) {
        synchronized (listeners) {
            for (NaviListener listener : listeners) {
                listener.movementChanged(movement);
            }
        }
    }
}
