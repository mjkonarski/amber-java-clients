package pl.edu.agh.amber.navi.app.test;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import net.sf.marineapi.nmea.event.SentenceEvent;
import net.sf.marineapi.nmea.event.SentenceListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.provider.HeadingProvider;
import net.sf.marineapi.provider.PositionProvider;
import net.sf.marineapi.provider.SatelliteInfoProvider;
import net.sf.marineapi.provider.event.*;
import pl.edu.agh.amber.navi.NaviConfig;
import pl.edu.agh.amber.navi.tool.SerialPortHelper;

import java.io.IOException;
import java.util.Arrays;

public class NaviHoluxTrackTest {

    private static class PositionListenerImpl implements PositionListener {

        @Override
        public void providerUpdate(PositionEvent evt) {
            System.err.println("Position:\t" + evt);
        }
    }

    private static class SatelliteInfoListenerImpl implements SatelliteInfoListener {

        @Override
        public void providerUpdate(SatelliteInfoEvent evt) {
            System.err.println("Satellite:\t" +
                    "gpsFixStatus[" + evt.getGpsFixStatus() + "] " +
                    "gpsMode[" + evt.getGpsMode() + "] " +
                    "horizontalPrecision[" + evt.getHorizontalPrecision() + "] " +
                    "verticalPrecision[" + evt.getVerticalPrecision() + "] " +
                    "satelliteIds[" + Arrays.toString(evt.getSatelliteIds()) + "]");
        }
    }

    private static class HeadingListenerImpl implements HeadingListener {

        @Override
        public void providerUpdate(HeadingEvent evt) {
            System.err.println("Heading:\t" + evt.getHeading());
        }
    }

    private static class SentenceListenerImpl implements SentenceListener {
        @Override
        public void readingPaused() {
            System.err.println("Reading paused");
        }

        @Override
        public void readingStarted() {
            System.err.println("Reading started");
        }

        @Override
        public void readingStopped() {
            System.err.println("Reading stopped");
        }

        @Override
        public void sentenceRead(SentenceEvent event) {
            System.err.println("Read:\t\tt[" + event.getTimeStamp() + "] s[" + event.getSentence() + "]");
        }
    }

    public static void main(String[] args) throws PortInUseException, UnsupportedCommOperationException, NoSuchPortException,
            IOException, InterruptedException {

        String holuxPortName = NaviConfig.getHoluxPortName();
        SerialPort holuxSerialPort = SerialPortHelper.getHoluxSerialPort(holuxPortName);

        SentenceReader sentenceReader = new SentenceReader(holuxSerialPort.getInputStream());
        sentenceReader.addSentenceListener(new SentenceListenerImpl());

        PositionProvider positionProvider = new PositionProvider(sentenceReader);
        positionProvider.addListener(new PositionListenerImpl());

        SatelliteInfoProvider satelliteInfoProvider = new SatelliteInfoProvider(sentenceReader);
        satelliteInfoProvider.addListener(new SatelliteInfoListenerImpl());

        HeadingProvider headingProvider = new HeadingProvider(sentenceReader);
        headingProvider.addListener(new HeadingListenerImpl());

        sentenceReader.start();
    }
}
