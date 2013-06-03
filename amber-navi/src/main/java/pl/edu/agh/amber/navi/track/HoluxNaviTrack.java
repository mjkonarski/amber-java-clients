package pl.edu.agh.amber.navi.track;

import gnu.io.SerialPort;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.util.Position;
import net.sf.marineapi.provider.HeadingProvider;
import net.sf.marineapi.provider.PositionProvider;
import net.sf.marineapi.provider.SatelliteInfoProvider;
import net.sf.marineapi.provider.event.*;
import pl.edu.agh.amber.navi.dto.NaviPoint;

import java.io.IOException;

public class HoluxNaviTrack extends NaviTrackHelper {

    private final SerialPort serialPort;

    private final SentenceReader sentenceReader;

    private final PositionProvider positionProvider;

    private final SatelliteInfoProvider satelliteInfoProvider;

    private final HeadingProvider headingProvider;

    private class PositionListenerImpl implements PositionListener {

        @Override
        public void providerUpdate(PositionEvent positionEvent) {
            Position position = positionEvent.getPosition();
            notifyLocationChange(new NaviPoint(position.getLongitude(), position.getLatitude()));
        }
    }

    private class SatelliteInfoListenerImpl implements SatelliteInfoListener {

        @Override
        public void providerUpdate(SatelliteInfoEvent evt) {
            System.err.println(evt);
        }
    }

    private class HeadingListenerImpl implements HeadingListener {

        @Override
        public void providerUpdate(HeadingEvent evt) {
            System.err.println(evt);
        }
    }

    public HoluxNaviTrack(SerialPort serialPort) throws IOException {
        this.serialPort = serialPort;

        sentenceReader = new SentenceReader(serialPort.getInputStream());

        positionProvider = new PositionProvider(sentenceReader);
        positionProvider.addListener(new PositionListenerImpl());

        satelliteInfoProvider = new SatelliteInfoProvider(sentenceReader);
        satelliteInfoProvider.addListener(new SatelliteInfoListenerImpl());

        headingProvider = new HeadingProvider(sentenceReader);
        headingProvider.addListener(new HeadingListenerImpl());

        sentenceReader.start();
    }
}
