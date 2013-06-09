package pl.edu.agh.amber.navi.eye;

import pl.edu.agh.amber.navi.NaviHelper;
import pl.edu.agh.amber.navi.dto.NaviVisibility;

/**
 * Provides information from eye like ...
 */
public abstract class NaviEyeHelper extends NaviHelper {

    private final Object visibilityLock = new Object();

    private NaviVisibility visibility;

    public NaviVisibility getVisibility() {
        synchronized (visibilityLock) {
            return visibility;
        }
    }

    @Override
    protected void notifyVisibilityChange(NaviVisibility visibility) {
        synchronized (visibilityLock) {
            this.visibility = visibility;
        }
        super.notifyVisibilityChange(visibility);
    }
}
