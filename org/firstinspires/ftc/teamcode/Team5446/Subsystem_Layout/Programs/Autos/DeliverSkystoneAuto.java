package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Programs.Autos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.enumclaw.ftc.teamcode.CurveDrivingRelated.Pose;
import org.enumclaw.ftc.teamcode.CurveDrivingRelated.StrafingParameters;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Bot;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.StoneManipulator;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools.CurveDrivingTool;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools.PositionCalculatorTool;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools.VuforiaServiceTool;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "'New Bot' Skystone Deliver Test Blue", group = "Team 5446")
public class DeliverSkystoneAuto extends OpMode {

    Bot bot = null;

    String autoStage = null;
    boolean startingStage = true;
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime timer = new ElapsedTime();
    boolean blueSide = true;

    float skystoneInchesFromWall = 4; //4, 12, 20 (+24 for rest)
    private final float botDiagonal = (float)(9.0f/Math.sqrt(2));

    @Override
    public void init()
    {
        telemetry.addData("Status", "Initializing");
        telemetry.update();
        bot = new Bot();
        bot.init(hardwareMap);

        bot.positionCalculatorTool = new PositionCalculatorTool();
        bot.positionCalculatorTool.init(bot.driveBase, bot.imuService);
        if (blueSide)
            bot.positionCalculatorTool.resetPose(new VectorF(9, 40), 0);
        else
            bot.positionCalculatorTool.resetPose(new VectorF(144-9, 40), 180-0);


        bot.curveDrivingTool = new CurveDrivingTool();
        bot.curveDrivingTool.init(bot.driveBase, bot.positionCalculatorTool);

        bot.vuforiaServiceTool = new VuforiaServiceTool();
        bot.vuforiaServiceTool.init(hardwareMap);

        autoStage = "Drive_To_See_Skystone";

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
        bot.positionCalculatorTool.updateBotPosition();

        if (autoStage == "Drive_To_See_Skystone")
        {
            if (startingStage)
            {
                bot.curveDrivingTool.resetCurveDrive();
                bot.vuforiaServiceTool.activate();
                startingStageDone();
            }

            bot.vuforiaServiceTool.update();

            if (blueSide)
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(9, 40), 0),
                        new Pose(new VectorF(28, 40), 0),
                        0.5f, 0.45f, false,
                        new StrafingParameters());
            }
            else
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(144-9, 40), 180-0),
                        new Pose(new VectorF(144-28, 40), 180-0),
                        0.5f, 0.45f, false,
                        new StrafingParameters());
            }

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.driveBase.mecanumDrive.stop();
                nextStage("Detect_Skystone");
            }
        }
        else if (autoStage == "Detect_Skystone")
        {
            if (startingStage)
            {
                timer.reset();
                startingStageDone();
            }

            bot.vuforiaServiceTool.update();

            if (timer.milliseconds() / 1000f > 1.0f || bot.vuforiaServiceTool.getSkystoneCordinates() != null) {
                if (bot.vuforiaServiceTool.getSkystoneCordinates() != null) {
                    if ((bot.vuforiaServiceTool.getSkystoneCordinates().get(1) < 0 && blueSide) || (bot.vuforiaServiceTool.getSkystoneCordinates().get(1) > 0 && !blueSide)) {
                        skystoneInchesFromWall = 20;
                    } else {
                        skystoneInchesFromWall = 12;
                    }
                }
                else {
                    skystoneInchesFromWall = 4;
                }
                bot.vuforiaServiceTool.deActivate();

                switch((int)skystoneInchesFromWall)
                {
                    case 4: nextStage("Prepare_For_Strafing_Into_Skystone");
                        break;
                    case 12: nextStage("Prepare_For_Strafing_Into_Skystone");
                        break;
                    case 20: nextStage("Prepare_For_Curving_Into_Skystone");
                        break;
                }
            }
        }
        //------------------------------------------------------------------------------------
        // Bottom 2 Skystone Paths
        //------------------------------------------------------------------------------------
        else if (autoStage == "Prepare_For_Strafing_Into_Skystone")
        {
            if (startingStage)
            {
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            if (blueSide)
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(28, 40), 90),
                        new Pose(new VectorF(28, (skystoneInchesFromWall+24)+15), 0),
                        0.0f, 0.60f, false,
                        new StrafingParameters(-90, 0.60f));
            }
            else
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(144-28, 40), 180-90),
                        new Pose(new VectorF(144-28, (skystoneInchesFromWall+24)+17), 180-0),
                        0.0f, 0.60f, false,
                        new StrafingParameters(180-(-90), 0.60f));
            }

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.driveBase.mecanumDrive.stop();
                nextStage("Strafing_Into_Skystone");
            }
        }
        else if (autoStage == "Strafing_Into_Skystone")
        {
            if (startingStage)
            {
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            if (blueSide)
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(28, (skystoneInchesFromWall+24)+15), 0),
                        new Pose(new VectorF(47, (skystoneInchesFromWall+24)+15), 0),
                        0.0f, 0.80f, false,
                        new StrafingParameters(-90, 0.40f));
            }
            else
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(144-28, (skystoneInchesFromWall+24)+17), 180-0),
                        new Pose(new VectorF(144-47, (skystoneInchesFromWall+24)+17), 180-0),
                        0.0f, 0.80f, false,
                        new StrafingParameters(180-(-90), 0.40f));
            }

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.driveBase.mecanumDrive.stop();
                nextStage("Drive_Into_Skystone");
            }
        }
        else if (autoStage == "Drive_Into_Skystone")
        {
            if (startingStage)
            {
                bot.stoneIntake.setPower(-0.60f);
                bot.stoneManipulator.setRotation(StoneManipulator.Preset.ALLOW_INTAKE);
                bot.stoneManipulator.setGrabberActivity(false);
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            if (blueSide)
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(47, (skystoneInchesFromWall+24)+15), -90),
                        new Pose(new VectorF(47, (skystoneInchesFromWall+24)+15-6), -90),
                        0f, 0.50f, false,
                        new StrafingParameters(-90, 0.40f));
            }
            else
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(144-47, (skystoneInchesFromWall+24)+17), 180-(-90)),
                        new Pose(new VectorF(144-47, (skystoneInchesFromWall+24)+17-6), 180-(-90)),
                        0f, 0.50f, false,
                        new StrafingParameters(180-(-90), 0.40f));
            }

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.driveBase.mecanumDrive.stop();
                nextStage("Strafe_Back_Into_The_Open");
            }
        }
        else if (autoStage == "Strafe_Back_Into_The_Open")
        {
            if (startingStage)
            {
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            if (blueSide)
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(47, (skystoneInchesFromWall+24)+15-6), 0),
                        new Pose(new VectorF(33, (skystoneInchesFromWall+24)+15-6), 0),
                        0f, 0.80f, false,
                        new StrafingParameters(-90, 0.40f));
            }
            else
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(144-47, (skystoneInchesFromWall+24)+17-6), 180-0),
                        new Pose(new VectorF(144-33, (skystoneInchesFromWall+24)+17-6), 180-0),
                        0f, 0.80f, false,
                        new StrafingParameters(180-(-90), 0.40f));
            }

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.driveBase.mecanumDrive.stop();
                nextStage("Backward_To_Bridge");
            }
        }
        else if (autoStage == "Backward_To_Bridge")
        {
            if (startingStage)
            {
                bot.stoneIntake.setPower(0f);
                bot.stoneManipulator.setRotation(StoneManipulator.Preset.LOWEST);
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            if (blueSide)
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(33, (skystoneInchesFromWall+24)+15-6), 90),
                        new Pose(new VectorF(33, 72), 90),
                        0f, -1.0f, true,
                        new StrafingParameters());
            }
            else
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(144-33, (skystoneInchesFromWall+24)+17-6), 180-90),
                        new Pose(new VectorF(144-33, 72), 180-90),
                        0f, -1.0f, true,
                        new StrafingParameters());
            }

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.stoneManipulator.setGrabberActivity(true);
                bot.driveBase.mecanumDrive.stop();
                nextStage("Place_Skystone_1");
            }
        }
        //------------------------------------------------------------------------------------
        // Top Skystone Path
        //------------------------------------------------------------------------------------
        else if (autoStage == "Prepare_For_Curving_Into_Skystone")
        {
            if (startingStage)
            {
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            if (blueSide)
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(28, 40), 90),
                        new Pose(new VectorF(28, 60), 0),
                        0.0f, 0.50f, false,
                        new StrafingParameters(0, 0.60f));
            }
            else
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(144-28, 40), 180-90),
                        new Pose(new VectorF(144-28, 60), 180-0),
                        0.0f, 0.50f, false,
                        new StrafingParameters(180-(0), 0.60f));
            }

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.driveBase.mecanumDrive.stop();
                nextStage("Curving_Into_Skystone");
            }
        }
        else if (autoStage == "Curving_Into_Skystone")
        {
            if (startingStage)
            {
                bot.stoneIntake.setPower(-0.70f);
                bot.stoneManipulator.setRotation(StoneManipulator.Preset.ALLOW_INTAKE);
                bot.stoneManipulator.setGrabberActivity(false);
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            if (blueSide)
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(28, 60), 0),
                        new Pose(new VectorF(47 - 1 , 48 + 1), -50),
                        0.3f, 0.45f, false,
                        new StrafingParameters());
            }
            else
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(144-28, 60), 180-0),
                        new Pose(new VectorF(144-(47 - 1) , 48 + 1), 180-(-50)),
                        0.3f, 0.45f, false,
                        new StrafingParameters());
            }

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.driveBase.mecanumDrive.stop();
                nextStage("Wait_To_Pick_Up_Skystone_1");
            }
        }
        else if (autoStage == "Wait_To_Pick_Up_Skystone_1")
        {
            if (startingStage)
            {
                timer.reset();
                startingStageDone();
            }

            if (timer.milliseconds() / 1000f > 2.0f)
            {
                nextStage("Turning_To_Bridge");
            }
        }

        else if (autoStage == "Turning_To_Bridge")
        {
            if (startingStage)
            {
                bot.stoneIntake.setPower(0f);
                bot.stoneManipulator.setRotation(StoneManipulator.Preset.LOWEST);
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            if (blueSide)
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(47 - 3 , 48 + 3), 130),
                        new Pose(new VectorF(33, 72), 90),
                        0.5f, -0.65f, true,
                        new StrafingParameters());
            }
            else
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(144-(47 - 3) , 48 + 3), 180-130),
                        new Pose(new VectorF(144-33, 72), 180-90),
                        0.5f, -0.65f, true,
                        new StrafingParameters());
            }

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.stoneManipulator.setGrabberActivity(true);
                bot.driveBase.mecanumDrive.stop();
                nextStage("Place_Skystone_1");
            }
        }
        //------------------------------------------------------------------------------------
        // Two Skystone Paths merge back together
        //------------------------------------------------------------------------------------
        else if (autoStage == "Place_Skystone_1")
        {
            if (startingStage)
            {
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            if (blueSide)
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(33, 72), 90),
                        new Pose(new VectorF(42, 100), 45),
                        0.5f, -0.7f, false,
                        new StrafingParameters());
            }
            else
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(144-33, 72), 180-90),
                        new Pose(new VectorF(144-42, 100), 180-45),
                        0.5f, -0.7f, false,
                        new StrafingParameters());
            }

            if (bot.curveDrivingTool.getCurveDriveProgress() >= 0.65f)
            {
                bot.stoneManipulator.setRotation(StoneManipulator.Preset.LEVEL1);
            }

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.driveBase.mecanumDrive.stop();
                nextStage("Placing_Process");
            }
        }
        else if (autoStage == "Placing_Process")
        {
            if (startingStage)
            {
                timer.reset();
                startingStageDone();
            }

            if (timer.milliseconds() / 1000f > 0.3f)
            {
                bot.stoneManipulator.setGrabberActivity(false);
            }

            if (timer.milliseconds() / 1000f > 1.0f)
            {
                nextStage("To_Skybridge_After_Skystone_1");
            }
        }

        else if (autoStage == "To_Skybridge_After_Skystone_1")
        {
            if (startingStage)
            {
                bot.stoneManipulator.setRotation(StoneManipulator.Preset.LOWEST);
                bot.stoneManipulator.setGrabberActivity(true);
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            if (blueSide)
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(41, 99), -135),
                        new Pose(new VectorF(33, 72), -90),
                        0.5f, 0.80f, false,
                        new StrafingParameters());
            }
            else
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(144-41, 99), 180-(-135)),
                        new Pose(new VectorF(144-33, 72), 180-(-90)),
                        0.5f, 0.80f, false,
                        new StrafingParameters());
            }

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.driveBase.mecanumDrive.stop();
                nextStage("Next_Stage");
            }
        }





        else if (autoStage == "EXAMPLE_DRIVE")
        {
            if (startingStage)
            {
                bot.curveDrivingTool.resetCurveDrive();
                startingStageDone();
            }

            if (blueSide)
            {
                bot.curveDrivingTool.curveDrive(
                        new Pose(new VectorF(0, 0), 0),
                        new Pose(new VectorF(0, 0), 0),
                        0.5f, 0.24f, false,
                        new StrafingParameters());
            }
            else
            {

            }

            if (bot.curveDrivingTool.isCurveDriveDone())
            {
                bot.driveBase.mecanumDrive.stop();
                nextStage("Next_Stage");
            }
        }
        else if (autoStage == "EXAMPLE_TIMER")
        {
            if (startingStage)
            {
                timer.reset();
                startingStageDone();
            }

            //Do Stuff

            if (timer.milliseconds() / 1000f > 1.0f)
            {
                nextStage("Next_Stage");
            }
        }

        telemetry.addData("Status", "Running: " + runtime.toString());
        telemetry.addData("Auto Stage", autoStage);
        telemetry.addData("Bot Position", bot.positionCalculatorTool.getBotPosition());
        telemetry.addData("Bot Heading", bot.positionCalculatorTool.getBotDirection());
        telemetry.addData("Curve Progress", bot.curveDrivingTool.getCurveDriveProgress());
        telemetry.update();
    }

    @Override
    public void stop()
    {
        bot.vuforiaServiceTool.stop();
        bot.stop();
    }
}
