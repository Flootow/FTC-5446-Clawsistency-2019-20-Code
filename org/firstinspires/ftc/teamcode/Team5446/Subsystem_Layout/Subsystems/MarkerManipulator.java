package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class MarkerManipulator {

    public Servo servo = null;

    public void init(HardwareMap hardwareMap)
    {
        servo = hardwareMap.servo.get("Marker");
        servo.setDirection(Servo.Direction.FORWARD);
    }

    public void holdMarker(boolean holding)
    {
        servo.setPosition((holding) ? 0f : 1f);
    }

    public void stop()
    {
        //Nothing
    }
}
