package org.firstinspires.ftc.teamcode.Team5446.Testing.Other;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Team5446.Competition.InputTiming;
import org.firstinspires.ftc.teamcode.Team5446.Competition.InputTimingManager;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Input Timing Test", group = "Team 5446")
@Disabled
public class InputTimingTest extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    InputTimingManager inputTimingManager = new InputTimingManager();


    @Override
    public void init()
    {


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
        float leftStickY = gamepad1.left_stick_y;
        InputTiming timing = inputTimingManager.getTiming("Left_Stick_Y");
        timing.UpdateInput(leftStickY);
        telemetry.addData("Negative", timing.SignDuration(-1));
        telemetry.addData("Zero", timing.SignDuration(0));
        telemetry.addData("Positive", timing.SignDuration(1));
        telemetry.update();
    }

    @Override
    public void stop()
    {

    }
}
