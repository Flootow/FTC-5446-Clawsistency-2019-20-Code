package org.firstinspires.ftc.teamcode.Team5446.Testing.Other;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Team5446.Competition.Math5446;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Stone Rotator Test", group = "Team 5446")
@Disabled
public class StoneRotatorTest extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    public Servo leftStoneRotator = null;
    public Servo rightStoneRotator = null;
    public Servo levelStoneRotator = null;
    float position = 0.15f;

    @Override
    public void init()
    {
        leftStoneRotator = hardwareMap.servo.get("LeftStone");
        rightStoneRotator = hardwareMap.servo.get("RightStone");
        levelStoneRotator = hardwareMap.servo.get("LevelStone");
        leftStoneRotator.setDirection(Servo.Direction.FORWARD);
        rightStoneRotator.setDirection(Servo.Direction.REVERSE);
        levelStoneRotator.setDirection(Servo.Direction.REVERSE);
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
        position += ((1.0f/60.0f) * 0.5f * gamepad1.left_stick_y);
        position = Math5446.Clamp(position, 0, 1);
        telemetry.addData("Position", position);
        leftStoneRotator.setPosition(position);
        rightStoneRotator.setPosition(position);
        levelStoneRotator.setPosition(position);
        telemetry.update();
    }

    @Override
    public void stop()
    {
        leftStoneRotator.setPosition(leftStoneRotator.getPosition());
        rightStoneRotator.setPosition(rightStoneRotator.getPosition());
        levelStoneRotator.setPosition(levelStoneRotator.getPosition());
    }
}
