package org.firstinspires.ftc.teamcode.Team5446.Testing.Other;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Reach Test 5446", group = "Team 5446")
@Disabled
public class ReachTest extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    public CRServo leftReach = null;
    public CRServo rightReach = null;

    @Override
    public void init()
    {
        leftReach = hardwareMap.crservo.get("LeftReach");
        rightReach = hardwareMap.crservo.get("RightReach");
        leftReach.setDirection(DcMotorSimple.Direction.FORWARD);
        rightReach.setDirection(DcMotorSimple.Direction.FORWARD);

    }

    @Override
    public void init_loop()
    {

    }

    @Override
    public void start()
    {

    }

    @Override
    public void loop()
    {
        telemetry.addData("Status", "Running: " + runtime.toString());
        leftReach.setPower(gamepad1.left_stick_y);
        rightReach.setPower(gamepad1.left_stick_y);
        telemetry.update();
    }

    @Override
    public void stop()
    {
        leftReach.setPower(0.0f);
        rightReach.setPower(0.0f);
    }
}
