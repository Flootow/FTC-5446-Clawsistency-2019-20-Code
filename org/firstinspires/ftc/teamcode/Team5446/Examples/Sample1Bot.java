package org.firstinspires.ftc.teamcode.Team5446.Examples;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.enumclaw.ftc.teamcode.MecanumDrive;

public class Sample1Bot
{
    public DcMotor leftFrontMotor = null;
    public DcMotor leftBackMotor = null;
    public DcMotor rightFrontMotor = null;
    public DcMotor rightBackMotor = null;

    public MecanumDrive mecanumDrive = null;



    public void init(HardwareMap hardwareMap)
    {
        leftFrontMotor  = hardwareMap.dcMotor.get("LeftFront");
        leftBackMotor   = hardwareMap.dcMotor.get("LeftBack");
        rightFrontMotor = hardwareMap.dcMotor.get("RightFront");
        rightBackMotor  = hardwareMap.dcMotor.get("RightBack");

        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);

        mecanumDrive = new MecanumDrive(leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor);
        mecanumDrive.setMode();
    }

    public void stop()
    {
        mecanumDrive.stop();
    }
}
