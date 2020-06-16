package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Team5446.Competition.Math5446;

public class Lift {

    public DcMotor leftLift = null;
    public DcMotor rightLift = null;

    public final float manualLiftClickSpeed = 120.0f;
    private float maxLiftMotorSpeed = 0.75f;
    private int liftClickTolerance = 170;
    private int target = 0;


    public enum LiftSide
    {
        LEFT,
        RIGHT;
    }

    public void init(HardwareMap hardwareMap)
    {
        leftLift = hardwareMap.dcMotor.get("LeftLift");
        rightLift = hardwareMap.dcMotor.get("RightLift");

        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftLift.setDirection(DcMotorSimple.Direction.FORWARD);
        rightLift.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void goToTarget(int target)
    {
        this.target = (int)Math5446.Clamp(target, -6000, 100);
        leftLift.setPower(maxLiftMotorSpeed * liftDirection(leftLift) * liftSlowdown(leftLift));
        rightLift.setPower(maxLiftMotorSpeed * liftDirection(rightLift) * liftSlowdown(rightLift));

//        if (Math.abs(leftLift.getCurrentPosition() - target) > liftClickTolerance)
//            leftLift.setPower(liftDirection(leftLift) * liftSlowdown(leftLift));
//        else
//            leftLift.setPower(0);
//        if (Math.abs(rightLift.getCurrentPosition() - target) > liftClickTolerance)
//            rightLift.setPower(liftDirection(rightLift) * liftSlowdown(rightLift));
//        else
//            leftLift.setPower(0);
    }

    private float liftDirection(DcMotor liftSide)
    {
        return -Math5446.sign(liftSide.getCurrentPosition() - target);
    }

    private float liftSlowdown(DcMotor liftSide)
    {
        return Math5446.Clamp(((Math.abs(liftSide.getCurrentPosition() - target) - liftClickTolerance) / (liftClickTolerance)), 0, 1);
    }

    public void stop()
    {
        leftLift.setPower(0);
        rightLift.setPower(0);
    }
}
