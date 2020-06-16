package org.firstinspires.ftc.teamcode.Team5446.Competition;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Manual Lift 5446", group = "Team 5446")
public class ManualLift5446 extends OpMode {
    Bot5446 bot = null;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init()
    {
        telemetry.addData("Status", "Initializing");
        bot = new Bot5446();
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
        bot.rightLift.setPower(0.5f * gamepad1.right_stick_y);
        bot.leftLift.setPower(0.5f * gamepad1.left_stick_y);

        telemetry.addData("LeftLift-Position", bot.leftLift.getCurrentPosition());
        telemetry.addData("RightLift-Position", bot.rightLift.getCurrentPosition());

        telemetry.update();
    }

    @Override
    public void stop()
    {
        bot.stop();
    }
}
