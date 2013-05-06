package pl.edu.agh.amber.navi.track;

import pl.edu.agh.amber.ninedof.NinedofProxy;
import pl.edu.agh.amber.stargazer.StarGazerProxy;

public class AmberNaviTrack extends NaviTrackHelper {

    private final StarGazerProxy starGazerProxy;

    private final NinedofProxy ninedofProxy;

    public AmberNaviTrack(StarGazerProxy starGazerProxy, NinedofProxy ninedofProxy) {
        this.starGazerProxy = starGazerProxy;
        this.ninedofProxy = ninedofProxy;
    }
}
