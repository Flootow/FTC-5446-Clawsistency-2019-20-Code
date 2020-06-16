package org.firstinspires.ftc.teamcode.Team5446.Testing.Other;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Team5446.Competition.InputTiming;
import org.firstinspires.ftc.teamcode.Team5446.Competition.InputTimingManager;
import org.firstinspires.ftc.teamcode.Team5446.Competition.Math5446;
import org.firstinspires.ftc.teamcode.Team5446.Competition.SoundService;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Sound Service Test", group = "Team 5446")
@Disabled
public class SoundServiceTeleopTest extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();

    SoundService soundService = null;
    InputTimingManager inputTimingManager = new InputTimingManager();

    @Override
    public void init()
    {
        telemetry.addData("Status", "Initializing");
        soundService = new SoundService();
        soundService.init(hardwareMap);
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
        if (gamepad1.dpad_right)
        {
            soundService.PlaySkystoneSound(3, 1.5f, 0);
        }

        InputTiming timing = inputTimingManager.getTiming("Dpad_Up");
        timing.UpdateInput(Math5446.boolTo01(gamepad1.dpad_left));
        telemetry.addData("Dpad Left + ", timing.SignDuration(1));
        telemetry.addData("Dpad Left 0 ", timing.SignDuration(0));
        if (gamepad1.dpad_left)
        {
            if (timing.SignDuration(1) == 1)
            {
                soundService.PlaySkystoneSound(3,1.0f,0);
            }
        }
        telemetry.update();
    }

    @Override
    public void stop()
    {

    }
}
