package hokuyocomm;

import java.util.Scanner;

/**
 * @author Nick Schulz
 */
public class HokuyoComm {

    public static void main(String[] args) {
        Serial.getAvailablePorts();

        System.out.print("Please enter port name to connect to: ");
        Scanner sc = new Scanner(System.in);
        String port = sc.nextLine();
        System.out.println();

        Serial.open(port, 750000, 8, 1, 0, 1000);

        SCIP scip = new SCIP();

        scip.laserOn();
        scip.singleScan(0);
        scip.laserOff();

        Serial.close();

        /*
        while (SCIP.queue.size() > 0) {
            MapPoint laserData = (MapPoint) SCIP.queue.remove();
            System.out.println(laserData.xValue() + "," + laserData.yValue());
        }
        */
    }
}
