package pl.edu.agh.amber.navi.track;

import pl.edu.agh.amber.navi.dto.NaviPoint;
import pl.edu.agh.amber.stargazer.StarGazerProxy;

public class AmberNaviTrack extends NaviTrackHelper {

    private final StarGazerProxy starGazerProxy;

    public AmberNaviTrack(StarGazerProxy starGazerProxy) {
        this.starGazerProxy = starGazerProxy;
    }

    @Override
    protected void notifyLocationChange(NaviPoint location) {
        // paoolo TODO: get location from starGazer device
        super.notifyLocationChange(location);
    }
}
