package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Team5446.Competition.Math5446;

import java.util.HashMap;

public class StoneManipulator {

    public Servo leftStoneRotator = null;
    public Servo rightStoneRotator = null;
    public Servo stoneGrabber = null;

    private boolean holdingStone = false;
    private float grabberHold = 0.40f;
    private float grabberOpen = 0.65f;

    public enum Preset
    {
        LOWEST(0.17f),
        ALLOW_INTAKE(0.30f),
        LEVEL1(0.85f),
        LEVEL2(0.70f),
        LEVEL3(0.65f),
        GROUND(0.90f);

        public final float value;
        private Preset(float value)
        {
            this.value = value;
        }
    }

    public void init(HardwareMap hardwareMap)
    {
        leftStoneRotator = hardwareMap.servo.get("LeftStone");
        rightStoneRotator = hardwareMap.servo.get("RightStone");
        stoneGrabber = hardwareMap.servo.get("Grabber"); //StoneGrabber prefered

        leftStoneRotator.setDirection(Servo.Direction.REVERSE);
        rightStoneRotator.setDirection(Servo.Direction.FORWARD);
        stoneGrabber.setDirection(Servo.Direction.REVERSE);
    }

    //region -Rotation Functions-
    public void setRotation(Preset preset)
    {
        setRotation(preset.value);
    }

    public void setRotation(float rotation)
    {
        rotation = Math5446.Clamp(rotation, 0, 1);
        leftStoneRotator.setPosition(rotation);
        rightStoneRotator.setPosition(rotation);
    }
    //endregion

    //region -Grabber Functions-
    public void setGrabberActivity(boolean hold)
    {
        holdingStone = hold;
        updateGrabber();
    }

    public void toggleGrabberActivity()
    {
        holdingStone = holdingStone ? false : true;
        updateGrabber();
    }

    private void updateGrabber()
    {
        stoneGrabber.setPosition(holdingStone ? grabberHold : grabberOpen);
    }
    //endregion

    public void stop()
    {
        //Servos go to last called setPosition()
    }
}
