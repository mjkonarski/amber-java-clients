package pl.edu.agh.amber.stargazer;

import com.google.protobuf.ExtensionRegistry;
import pl.edu.agh.amber.common.AmberClient;
import pl.edu.agh.amber.common.AmberProxy;
import pl.edu.agh.amber.common.proto.CommonProto;
import pl.edu.agh.amber.stargazer.proto.StarGazerProto;

import java.util.logging.Logger;

public class StarGazerProxy extends AmberProxy {

    private final static int DEVICE_TYPE = 3;

    private int synNum = 100;
    private final ExtensionRegistry extensionRegistry;

    /**
     * Instantiates StarGazer proxy which is the basic object used to
     * communication with robot's StarGazer sensor.
     *
     * @param amberClient {@link AmberClient} instance
     * @param deviceID    ID given to particular StarGazer instance (most times this should
     *                    be 0)
     */
    public StarGazerProxy(AmberClient amberClient, int deviceID) {
        super(DEVICE_TYPE, deviceID, amberClient, Logger.getLogger(String.valueOf(StarGazerProxy.class)));

        logger.info("Starting and registering StarGazerProxy.");

        extensionRegistry = ExtensionRegistry.newInstance();
        StarGazerProto.registerAllExtensions(extensionRegistry);
    }

    @Override
    public void handleDataMsg(CommonProto.DriverHdr header, CommonProto.DriverMsg message) {
        logger.fine("Handling data message");

        // paoolo TODO this!
    }

    @Override
    public ExtensionRegistry getExtensionRegistry() {
        return extensionRegistry;
    }

    private synchronized int getNextSynNum() {
        return synNum++;
    }

    private void fillStructure(StarGazerData starGazerData, CommonProto.DriverMsg message) {
        StarGazerProto.SensorData sensorData = message.getExtension(StarGazerProto.sensorData);

        starGazerData.setXAxis(sensorData.getXAxis());
        starGazerData.setYAxis(sensorData.getYAxis());
        starGazerData.setAngle(sensorData.getAngel());

        starGazerData.setAvailable();
    }
}
