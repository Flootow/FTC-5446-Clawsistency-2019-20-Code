package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.enumclaw.ftc.teamcode.MecanumDrive;
import org.enumclaw.ftc.teamcode.MecanumDrive5446;

import java.util.List;

public class DriveBase {

    public DcMotor leftFrontMotor = null;
    public DcMotor leftBackMotor = null;
    public DcMotor rightFrontMotor = null;
    public DcMotor rightBackMotor = null;
    public MecanumDrive mecanumDrive = null;

    public void init(HardwareMap hardwareMap) {

        leftFrontMotor = hardwareMap.dcMotor.get("LeftFront");
        leftBackMotor = hardwareMap.dcMotor.get("LeftBack");
        rightFrontMotor = hardwareMap.dcMotor.get("RightFront");
        rightBackMotor = hardwareMap.dcMotor.get("RightBack");
        mecanumDrive = new MecanumDrive(leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor);

        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        rightBackMotor.setDirection(DcMotor.Direction.FORWARD);

        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //mecanumDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //mecanumDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //mecanumDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void stop()
    {
        mecanumDrive.stop();
    }

}
