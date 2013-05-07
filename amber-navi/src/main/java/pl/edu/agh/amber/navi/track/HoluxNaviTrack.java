package pl.edu.agh.amber.navi.track;

import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.util.Position;
import net.sf.marineapi.provider.PositionProvider;
import net.sf.marineapi.provider.event.PositionEvent;
import net.sf.marineapi.provider.event.PositionListener;
import pl.edu.agh.amber.navi.dto.NaviPoint;
import pl.edu.agh.amber.navi.tool.SerialPortWrapper;

public class HoluxNaviTrack extends NaviTrackHelper implements PositionListener {

    private final SentenceReader sentenceReader;

    private final PositionProvider positionProvider;

    public HoluxNaviTrack(SerialPortWrapper serialPortWrapper) {
        sentenceReader = new SentenceReader(serialPortWrapper.getIs());

        positionProvider = new PositionProvider(sentenceReader);
        positionProvider.addListener(this);

        sentenceReader.start();
    }

    @Override
    public void providerUpdate(PositionEvent positionEvent) {
        Position position = positionEvent.getPosition();
        notifyLocationChange(new NaviPoint(position.getLongitude(), position.getLatitude()));
    }
}
