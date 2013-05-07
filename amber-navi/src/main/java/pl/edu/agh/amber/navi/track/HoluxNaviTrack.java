package pl.edu.agh.amber.navi.track;

import net.sf.marineapi.nmea.event.SentenceEvent;
import net.sf.marineapi.nmea.event.SentenceListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.sentence.SentenceId;
import net.sf.marineapi.provider.PositionProvider;
import net.sf.marineapi.provider.event.PositionEvent;
import net.sf.marineapi.provider.event.PositionListener;
import pl.edu.agh.amber.navi.agent.NaviPoint;
import pl.edu.agh.amber.navi.tool.SerialPortWrapper;

import java.util.logging.Logger;

public class HoluxNaviTrack extends NaviTrackHelper implements SentenceListener, PositionListener {

    private static final Logger logger = Logger.getLogger(String.valueOf(HoluxNaviTrack.class));

    private final SerialPortWrapper serialPortWrapper;

    private final SentenceReader sentenceReader;

    private final PositionProvider positionProvider;

    public HoluxNaviTrack(SerialPortWrapper serialPortWrapper) {
        this.serialPortWrapper = serialPortWrapper;

        sentenceReader = new SentenceReader(serialPortWrapper.getIs());
        sentenceReader.addSentenceListener(this, SentenceId.GLL);
        sentenceReader.start();

        positionProvider = new PositionProvider(sentenceReader);
        positionProvider.addListener(this);

        sentenceReader.start();
    }

    @Override
    public NaviPoint getCurrentLocation() {
        return null;
    }

    @Override
    public void readingPaused() {
    }

    @Override
    public void readingStarted() {
    }

    @Override
    public void readingStopped() {
    }

    @Override
    public void sentenceRead(SentenceEvent sentenceEvent) {
        logger.finest(String.valueOf(sentenceEvent.getSentence()));
    }

    @Override
    public void providerUpdate(PositionEvent positionEvent) {
        logger.finest(String.valueOf(positionEvent));
    }
}
