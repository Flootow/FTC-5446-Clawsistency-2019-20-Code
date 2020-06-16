package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Programs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Bot;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "'New Bot' Drivebase Teleop", group = "Team 5446")
public class JustDriveBase extends OpMode {

    Bot bot = null;
    @Override
    public void init()
    {
        telemetry.addData("Status", "Initializing");
        telemetry.update();

        bot = new Bot();
        bot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
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
        telemetry.addData("Program", "Running");
        bot.driveBase.mecanumDrive.run(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, false);
        telemetry.update();
    }

    @Override
    public void stop()
    {

    }
}
