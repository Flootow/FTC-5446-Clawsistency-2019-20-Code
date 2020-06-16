package org.firstinspires.ftc.teamcode.Team5446.Examples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

//@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Sample1TeleOp", group = "Team 5446")
public class Sample1TeleOp extends OpMode
{
    Sample1Bot bot = null;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init()
    {
        telemetry.addData("Status", "Initializing");
        bot = new Sample1Bot();
        bot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
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
        bot.mecanumDrive.run(-gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x, gamepad1.right_bumper);
        telemetry.update();
    }

    @Override
    public void stop()
    {
        bot.stop();
    }
}
