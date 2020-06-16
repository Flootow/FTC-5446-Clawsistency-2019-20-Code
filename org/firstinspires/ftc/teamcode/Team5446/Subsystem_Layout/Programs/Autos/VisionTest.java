package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Programs.Autos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.enumclaw.ftc.teamcode.CurveDrivingRelated.Pose;
import org.enumclaw.ftc.teamcode.CurveDrivingRelated.StrafingParameters;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Bot;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools.VuforiaServiceTool;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "'New Bot' Vision Test", group = "Team 5446")
public class VisionTest extends OpMode {

    Bot bot = null;

    String autoStage;
    boolean startingStage = true;

    @Override
    public void init()
    {
        telemetry.addData("Status", "Initializing");
        telemetry.update();
        bot = new Bot();
        bot.init(hardwareMap);
        bot.vuforiaServiceTool = new VuforiaServiceTool();
        bot.vuforiaServiceTool.init(hardwareMap);

        autoStage = "Step1";
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    private void nextStage(String stage)
    {
        autoStage = stage;
        startingStage = true;
    }

    private void startingStageDone() { startingStage = false; }

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
        if (autoStage == "Step1")
        {
            if (startingStage)
            {
                bot.vuforiaServiceTool.activate();
                startingStageDone();
            }

            bot.vuforiaServiceTool.update();
            telemetry.addData("Skystone Cords",bot.vuforiaServiceTool.getSkystoneCordinates());
        }
        telemetry.update();
    }

    @Override
    public void stop()
    {
        bot.vuforiaServiceTool.deActivate();
        bot.stop();
    }
}
