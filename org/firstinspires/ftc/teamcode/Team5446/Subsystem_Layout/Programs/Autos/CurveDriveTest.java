package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Programs.Autos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.enumclaw.ftc.teamcode.CurveDrivingRelated.CurveDriveListEntry;
import org.enumclaw.ftc.teamcode.CurveDrivingRelated.Pose;
import org.enumclaw.ftc.teamcode.CurveDrivingRelated.StrafingParameters;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Bot;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools.CurveDrivingTool;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools.PositionCalculatorTool;

import java.util.ArrayList;
import java.util.List;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "'New Bot' Curve Test", group = "Team 5446")
public class CurveDriveTest extends OpMode {

    Bot bot = null;
    String autoStage = null;
    boolean startingStage = true;
    boolean blueSide = true;

    /*
    List<CurveDriveListEntry> autoPath1 = new ArrayList<CurveDriveListEntry>();
    private void createPathing()
    {
        //0: Place A to Place B
        autoPath1.add(new CurveDriveListEntry(
                bot.positionCalculatorTool.getBotPose(),
                new Pose(new VectorF(24, 0), 0),
                0.5f, 0.6f, false,
                new StrafingParameters()
        ));
        //1: Place B to Place C
        autoPath1.add(new CurveDriveListEntry(
                new Pose(bot.positionCalculatorTool.getBotPosition(), 90),
                new Pose(new VectorF(24, 24), 90),
                0.0f, 0.6f, false,
                new StrafingParameters(0, 0.2f)
        ));
    }
    */

    @Override
    public void init()
    {
        bot = new Bot();
        bot.init(hardwareMap);

        bot.positionCalculatorTool = new PositionCalculatorTool();
        bot.positionCalculatorTool.init(bot.driveBase, bot.imuService);
        bot.positionCalculatorTool.resetPose(new VectorF(0,0), 0);

        bot.curveDrivingTool = new CurveDrivingTool();
        bot.curveDrivingTool.init(bot.driveBase, bot.positionCalculatorTool);

        autoStage = "Step1";
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
        bot.positionCalculatorTool.updateBotPosition();

        if (autoStage == "Step1")
        {
            if (startingStage)
            {
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            bot.curveDrivingTool.curveDrive(
                    new Pose(new VectorF(0, 0), 0),
                    new Pose(new VectorF(24, 24), 90),
                    0.5f, 0.5f, false,
                    new StrafingParameters());

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.driveBase.mecanumDrive.stop();
                nextStage("Step2");
            }
        }

        else if (autoStage == "Step2")
        {
            if (startingStage)
            {
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            bot.curveDrivingTool.curveDrive(
                    new Pose(new VectorF(24, 24), -90),
                    new Pose(new VectorF(0, 0), -180),
                    0.5f, -0.5f, false,
                    new StrafingParameters());

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.driveBase.mecanumDrive.stop();
                nextStage("Step3");
            }
        }
        else if (autoStage == "Step3")
        {
            if (startingStage)
            {
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            bot.curveDrivingTool.curveDrive(
                    new Pose(new VectorF(0, 0), 90),
                    new Pose(new VectorF(0, 24), 90),
                    0.0f, 0.6f, false,
                    new StrafingParameters(45, 0.3f));

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.stop();
                nextStage("Step4");
            }
        }
        else if (autoStage == "Step4")
        {
            if (startingStage)
            {
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            bot.curveDrivingTool.curveDrive(
                    new Pose(new VectorF(0, 24), -90),
                    new Pose(new VectorF(0, 0), -90),
                    0.0f, 0.6f, false,
                    new StrafingParameters(0, 0.3f));

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.stop();
                nextStage("Done");
            }
        }

        telemetry.addData("Auto Stage", autoStage);
        telemetry.addData("Bot Position", bot.positionCalculatorTool.getBotPosition());
        telemetry.addData("Bot Heading", bot.positionCalculatorTool.getBotDirection());
        telemetry.addData("Curve Progress", bot.curveDrivingTool.getCurveDriveProgress());

        telemetry.update();

    }

    @Override
    public void stop()
    {
        bot.stop();
    }
}
