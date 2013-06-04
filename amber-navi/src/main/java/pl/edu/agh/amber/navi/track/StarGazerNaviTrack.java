package pl.edu.agh.amber.navi.track;

import gnu.io.SerialPort;
import pl.edu.agh.amber.navi.dto.NaviPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class StarGazerNaviTrack extends NaviTrackHelper {

    private final SerialPort serialPort;

    private final PrintStream out;

    private final BufferedReader in;

    public StarGazerNaviTrack(SerialPort serialPort) throws IOException {
        this.serialPort = serialPort;

        out = new PrintStream(serialPort.getOutputStream());
        in = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
    }

    @Override
    public void run() {
        out.println("#CalcStart");
        try {
            // probably it will be "!CalcStart"
            System.err.println(in.readLine());
            while (true) {
                String line = in.readLine();
                if (line.matches("\\^[FIZ].*(\\|[\\+\\-][0-9]{1,4}\\.[0-9]{1,2}){3}\\|[0-9]{1,4}\\.[0-9]{1,2}")) {
                    String[] chunks = line.split("|");
                    double horizontal = Double.parseDouble(chunks[2]);
                    double vertical = Double.parseDouble(chunks[3]);
                    notifyLocationChange(new NaviPoint(horizontal, vertical));
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("#CalcStop");
        try {
            // probably it will be "!CalcStop"
            System.err.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
