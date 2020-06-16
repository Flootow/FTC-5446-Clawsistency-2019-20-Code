package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class PlateGrabber {

    public Servo leftPlate = null;
    public Servo rightPlate = null;

    private boolean down = false;

    public void init(HardwareMap hardwareMap)
    {
        leftPlate = hardwareMap.servo.get("LeftPlate");
        rightPlate = hardwareMap.servo.get("RightPlate");
        leftPlate.setDirection(Servo.Direction.FORWARD);
        rightPlate.setDirection(Servo.Direction.REVERSE);
    }

    public boolean isDown()
    {
        return down;
    }

    public void setPosition(boolean down)
    {
        this.down = down;
        updatePosition();
    }

    public void togglePosition()
    {
       this.down = down ? false : true;
       updatePosition();
    }

    private void updatePosition()
    {
        if (down)
        {
            leftPlate.setPosition(1.0f);
            rightPlate.setPosition(1.0f);
        }
        else
        {
            leftPlate.setPosition(0.0f);
            rightPlate.setPosition(0.0f);
        }
    }

    public void stop()
    {

    }
}
