package pl.edu.agh.amber.navi.track;

import gnu.io.SerialPort;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.util.Position;
import net.sf.marineapi.provider.PositionProvider;
import net.sf.marineapi.provider.event.PositionEvent;
import net.sf.marineapi.provider.event.PositionListener;
import pl.edu.agh.amber.navi.dto.NaviPoint;

import java.io.IOException;

public class HoluxNaviTrack extends NaviTrackHelper implements PositionListener {

    private final SerialPort serialPort;

    private final SentenceReader sentenceReader;

    private final PositionProvider positionProvider;

    public HoluxNaviTrack(SerialPort serialPort) throws IOException {
        this.serialPort = serialPort;

        sentenceReader = new SentenceReader(serialPort.getInputStream());

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
