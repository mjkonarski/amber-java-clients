package pl.edu.agh.amber.navi.drive;

import pl.edu.agh.amber.navi.dto.NaviMovement;

import java.io.IOException;

public class DummyNaviDrive extends NaviDriveHelper {

    @Override
    public void change(NaviMovement movement) throws IOException {
        // nothing to do here
    }
}
