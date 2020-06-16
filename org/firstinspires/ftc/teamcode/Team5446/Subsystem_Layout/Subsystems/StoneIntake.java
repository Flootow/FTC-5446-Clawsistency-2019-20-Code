package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class StoneIntake {

    public DcMotor leftIntake = null;
    public DcMotor rightIntake = null;
    public DistanceSensor distanceSensor = null;

    public void init(HardwareMap hardwareMap)
    {
        leftIntake = hardwareMap.dcMotor.get("LeftIntake");
        rightIntake = hardwareMap.dcMotor.get("RightIntake");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "Distance");
        leftIntake.setDirection(DcMotor.Direction.FORWARD);
        rightIntake.setDirection(DcMotor.Direction.REVERSE);
    }

    /**
     * @param power -1.0 pulls stones, 1.0 ejects stones
     */
    public void setPower(float power)
    {
        leftIntake.setPower(power);
        rightIntake.setPower(power);
    }

    public void stop()
    {
        leftIntake.setPower(0);
        rightIntake.setPower(0);
    }

    public boolean blockInside()
    {
        //Values obtained in testing
        //Stone Inside : 0 to 1.3 inches, error can go up to 1.9
        //Stone Outside : 4.5 inches (More than stone width) to 9.0 inches (Farthest seen inside of robot), plexiglass causes this range
        return ((distanceSensor.getDistance(DistanceUnit.INCH) < 2.0f) ? true : false);
    }
}
