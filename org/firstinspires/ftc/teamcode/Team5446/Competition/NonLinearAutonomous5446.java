package org.firstinspires.ftc.teamcode.Team5446.Competition;

import com.qualcomm.hardware.motors.NeveRest40Gearmotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.enumclaw.ftc.teamcode.Encoder;
import org.enumclaw.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;


import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class NonLinearAutonomous5446 extends OpMode
{
    Bot5446 bot = null;
    VuforiaService vuforiaService = null;
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime timer = new ElapsedTime();

    public boolean parkingClose;

    public boolean blueSide;
    float skystoneInchesFromWall = 4; //4, 12, 20?
    VectorF buildingPosition = new VectorF(0, 0);


    public enum ProgramMode
    {
        AUTO0,
        AUTO1,
        AUTO2,
        AUTO3,
        AUTO4,
        AUTO5,
        AUTO6,
        AUTO7,
        AUTO8,
        AUTO_VISION;
    }
    ProgramMode programMode;

    int autoStage = 0;
    float curveDriveProgress = 0.0f;
    boolean curveDriveDone = false;

    public void init()
    {
        telemetry.addData("Status", "Initializing");
        telemetry.update();
        bot = new Bot5446();
        bot.init(hardwareMap);
        if (programMode == ProgramMode.AUTO_VISION || programMode == ProgramMode.AUTO8)
        {
            vuforiaService = new VuforiaService();
            vuforiaService.init(hardwareMap);
        }
        if (programMode == ProgramMode.AUTO4)
        {
            bot.leftPlate.setPosition(0.0f);
            bot.rightPlate.setPosition(0.0f);
        }
        if (programMode == ProgramMode.AUTO5)
        {
            bot.leftPlate.setPosition(0.0f);
            bot.rightPlate.setPosition(0.0f);
        }

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
        //region -Inside Start()-
        botPosition = new VectorF(0,0);

        if (programMode == ProgramMode.AUTO0)
        {
            botPosition = new VectorF(9,48);
        }
        if (programMode == ProgramMode.AUTO3)
        {
            botPosition = new VectorF(9, 60);
            bot.SetAngle(0);
        }

        if (programMode == ProgramMode.AUTO5)
        {
            botPosition = new VectorF(9, 108);
            bot.SetAngle(0);
        }

        if (programMode == ProgramMode.AUTO4)
        {
            botPosition = new VectorF(9, 108);
            bot.SetAngle(180);
        }

        if (programMode == ProgramMode.AUTO6)
        {
            botPosition = new VectorF(144-9, 108);
            bot.SetAngle(0);
        }
        if (programMode == ProgramMode.AUTO7)
        {
            botPosition = new VectorF(0,0);
            bot.SetAngle(0);
        }
        if (programMode == ProgramMode.AUTO8)
        {
            botPosition = new VectorF(9, 40);
            bot.SetAngle(0);
        }

        starting = true;
        autoStage = 1;
        //endregion
    }

    VectorF botPosition;
    List<VectorF> fourPoints = new ArrayList<VectorF>();
    float drivingProgress;
    boolean starting;


    @Override
    public void loop()
    {
        telemetry.addData("Status", "Running: " + runtime.toString());

        //region -AUTO 0-
        if (programMode == ProgramMode.AUTO0)
        {


            VectorF botTranslation = Math5446.RotatePoint(mecanumTranslation(bot.rightFrontMotor, bot.rightBackMotor, bot.leftFrontMotor, bot.leftBackMotor, 4.0f), (float) Math.toRadians(bot.GetAngle()));
            botPosition.add(botTranslation);

            if (autoStage == 1)
            {
                float progressIncrease = 0.04f;

                float curveDrivingTolerance = 9.0f;
                if (starting)
                {
                    drivingProgress = 0.0f;

                    fourPoints = new ArrayList<VectorF>();
                    fourPoints.add(botPosition); //Bot Position
                    fourPoints.add(new VectorF(40,48)); //Bot Position + Vector matching bots Z orientation
                    fourPoints.add(new VectorF(20,72)); //End Position + Vector matching desired tangent line
                    fourPoints.add(new VectorF(45,72)); //End Position

                    starting = false;
                }

                //X : Forward, Y: Sideways
                VectorF botToTarget = Math5446.RotatePoint(Math5446.CurveDrivingTarget(fourPoints, drivingProgress).subtracted(botPosition),(float) ((-Math.PI/180.0f) * bot.GetAngle()));
                float botToTargetDistance = botToTarget.magnitude();
                botToTarget = new VectorF(botToTarget.get(0), botToTarget.get(1), 0);
                botToTarget.normalized3D();
                float speedReduction = Math5446.Clamp(botToTargetDistance/curveDrivingTolerance, 0, 1);

                bot.mecanumDrive.run(botToTarget.get(0),botToTarget.get(1) ,0, false);
                if (Math5446.Distance(botPosition, Math5446.CurveDrivingTarget(fourPoints, drivingProgress)) < curveDrivingTolerance)
                {
                    drivingProgress += progressIncrease;
                    drivingProgress = Math5446.Clamp(drivingProgress, 0, 1);
                }
                if (Math5446.Distance(botPosition, Math5446.CurveDrivingTarget(fourPoints, drivingProgress)) > curveDrivingTolerance)
                {
                    float testDrivePower = 0.3f;
                    VectorF driveVector = new VectorF(0,0);
                    if (botToTarget.get(0) > 0)
                    {
                        driveVector = new VectorF(Math5446.Clamp((float)(botToTarget.get(0) - Math.abs(0.7f * Math.atan2(botToTarget.get(1), botToTarget.get(0)))), 0, 1), driveVector.get(1));
                    }
                    else if (botToTarget.get(0) < 0)
                    {
                        driveVector = new VectorF(Math5446.Clamp((float)(botToTarget.get(0) + Math.abs(0.7f * Math.atan2(botToTarget.get(1), botToTarget.get(0)))), -1, 0), driveVector.get(1));
                    }
                    driveVector = new VectorF(driveVector.get(0), botToTarget.get(1));
                    bot.mecanumDrive.run(testDrivePower * driveVector.get(0) * speedReduction,-testDrivePower * driveVector.get(1) * speedReduction ,0, false);
                }


            }
        }
        //endregion

        //region -AUTO 1-
        if (programMode == ProgramMode.AUTO1)
        {
            UpdateBotPosition();

            if (autoStage == 1)
            {
                float progressIncrease = 0.04f;

                float curveDrivingTolerance = 9.0f;
                float endTolerance = 3.0f;
                if (starting)
                {
                    drivingProgress = 0.0f;

                    starting = false;
                }

                VectorF target = Math5446.CurveDrivingTarget(new VectorF(0,0), 0, new VectorF(30, 12), 45, 0.5f, drivingProgress);

                //Computations regarding where target is in relation with bot
                VectorF botToTarget = Math5446.RotatePoint(target.subtracted(botPosition),(float) ((-Math.PI/180.0f) * bot.GetAngle()));
                float botToTargetDistance = botToTarget.magnitude();
                botToTarget = new VectorF(botToTarget.get(0), botToTarget.get(1), 0);
                botToTarget.normalized3D();

                //Extend target on curve if too close
                if (botToTargetDistance < curveDrivingTolerance)
                {
                    drivingProgress += progressIncrease;
                    drivingProgress = Math5446.Clamp(drivingProgress, 0, 1);
                    if (drivingProgress == 1 && botToTargetDistance < endTolerance)
                    {
                        starting = true;
                        autoStage++;
                    }
                }

                float testDrivePower = 0.5f;
                VectorF driveVector = new VectorF(botToTarget.get(0), botToTarget.get(1));
                float speedReduction = Math5446.Clamp((float) (Math.pow(botToTargetDistance/curveDrivingTolerance, 2)), 0, 1);

                //Reduce Forward Speed the sharper we have to turn
                float slowForTurning = (float) (Math.abs(0.0f * Math.atan2(botToTarget.get(1), botToTarget.get(0))));
                if (driveVector.get(0) > 0)
                {
                    driveVector = new VectorF(Math5446.Clamp(driveVector.get(0) - slowForTurning, 0, 1), driveVector.get(1));
                }
                else if (driveVector.get(0) < 0)
                {
                    driveVector = new VectorF(Math5446.Clamp(driveVector.get(0) + slowForTurning, -1, 0), driveVector.get(1));
                }
                driveVector = new VectorF(testDrivePower * driveVector.get(0) * speedReduction, -testDrivePower * driveVector.get(1) * speedReduction);
                bot.mecanumDrive.run(driveVector.get(0),0.5f * driveVector.get(1) ,0, false);

                if (drivingProgress > 0.60f)
                {
                    bot.rightIntake.setPower(-1.0f);
                    bot.leftIntake.setPower(-1.0f);
                }

                telemetry.addData("Drive Progress", drivingProgress);
                telemetry.addData("Target", target.toString());


            }

            if (autoStage == 2)
            {
                if (starting)
                {
                    timer.reset();
                }
                if (timer.milliseconds() * 3000 > 1)
                {
                    bot.stop();
                }
            }
        }
        //endregion

        //region -AUTO 2-
        if (programMode == ProgramMode.AUTO2)
        {
            VectorF botTranslation = Math5446.RotatePoint(mecanumTranslation(bot.rightFrontMotor, bot.rightBackMotor, bot.leftFrontMotor, bot.leftBackMotor, 4.0f), (float) Math.toRadians(bot.GetAngle()));
            botPosition.add(botTranslation);

            VectorF targetPosition = new VectorF(24,0);

            VectorF botToTarget = Math5446.RotatePoint(targetPosition.subtracted(botPosition),(float) ((-Math.PI/180.0f) * bot.GetAngle()));
            botToTarget = new VectorF(botToTarget.get(0), botToTarget.get(1), 0);
            botToTarget.normalized3D();
            float testDrivePower = 0.5f;
            bot.mecanumDrive.run(testDrivePower * botToTarget.get(0),-testDrivePower * botToTarget.get(1) ,0, false);

            telemetry.addData("Bot Position", botPosition.toString());
            telemetry.addData("BotToTarget", botToTarget.toString());
        }
        //endregion

        //region -AUTO 3-
        if (programMode == ProgramMode.AUTO3)
        {
            UpdateBotPosition();
            telemetry.addData("Bot Position", botPosition.toString());

            //BLUE SIDE OF FIELD
            float constBotDiagonal = (float) (9.0f/Math.sqrt(2));
            float constDistanceToWall = 9;
            float constStoneLineCornerX = 47;
            float constStoneLineCornerY = 48; //Can be 48-49 due to field tolerance
            float constSkybridgeSafeX = 36;
            float constSkybridgeSafeY = 72;
            float constBuildPlateCornerX = 47.25f;
            float constBuildPlateCornerY = 105.5f;

            telemetry.addData("AutoStage", autoStage);
            telemetry.addData("Curve Progress", curveDriveProgress);

            //region -Stage 1: Drive Ahead-
            if (autoStage == 1)
            {
                //Drive Ahead
                CurveDrive(new VectorF(constDistanceToWall, 60), 0,
                        new VectorF(24, 60), 0,
                        0.5f, starting, 0.5f, true);

                if (starting)
                {
                    ArmRotation(0.13f);
                    starting = false;
                }

                if (curveDriveDone)
                {
                    autoStage++;
                    starting = true;
                    bot.mecanumDrive.stop();
                }
            }
            //endregion

            //region -Stage 2: To Top Stone, Intake-
            if (autoStage == 2)
            {
                //Turn Into Topmost Stone, Raise Arm and activate intake
                CurveDrive(new VectorF(24,60), 0,
                        new VectorF(constStoneLineCornerX - constBotDiagonal + 2, constStoneLineCornerY + constBotDiagonal - 2), -55,
                        0.5f, starting, 0.5f, false);

                if (starting)
                {
                    ArmRotation(0.25f);
                    IntakeActive(true);
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Stage 3: Intake Time-
            if (autoStage == 3)
            {
                //Wait small while to grasp stone

                if (starting)
                {
                    timer.reset();
                    starting = false;
                }

                telemetry.addData("Waiting", timer.milliseconds() / 1000.0f);

                if (timer.milliseconds() / 1000.0f > 3.0f)
                {
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Stage 4: To Skybridge, Grabbing Block-
            if (autoStage == 4)
            {
                //Curve backwards to below skybridge, Lower Arm, Grab Block, stop intake at end
                CurveDrive(new VectorF(constStoneLineCornerX - constBotDiagonal, constStoneLineCornerY + constBotDiagonal), 135,
                        new VectorF(constSkybridgeSafeX, constSkybridgeSafeY), 90,
                        0.5f, starting, -0.5f, true);

                if (starting)
                {
                    ArmRotation(0.13f);
                    GrabberActive(true);
                    timer.reset();
                    starting = false;
                }

                if (curveDriveDone)
                {
                    IntakeActive(false);
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Stage 5: To Buildplate, Moving Arm-
            if (autoStage == 5)
            {
                //Curve backwards to build plate, position arm to deploy
                CurveDrive(new VectorF(constSkybridgeSafeX, constSkybridgeSafeY), 90,
                        new VectorF(constBuildPlateCornerX, constBuildPlateCornerY), 45,
                        0.5f, starting, -0.5f, false);

                if (starting)
                {
                    starting = false;
                }

                if (curveDriveProgress >= 0.3f)
                {
                    ArmRotation(0.83f);
                }

                if (curveDriveDone)
                {
                    autoStage++;
                    starting = true;
                }

            }
            //endregion

            //region -Stage 6: Drop Stone-
            if (autoStage == 6)
            {
                //Wait for neccessary time to drop stone
                if (starting)
                {
                    timer.reset();
                    GrabberActive(false);
                    starting = false;
                }

                if (timer.milliseconds() / 1000.0f > 1.0f)
                {
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Stage 7: To Skybridge, Resetting Arm
            if (autoStage == 7)
            {
                //Curve Forward to Skybridge, reset arm
                CurveDrive(new VectorF(constBuildPlateCornerX, constBuildPlateCornerY), -135,
                        new VectorF(constSkybridgeSafeX, constSkybridgeSafeY), -90,
                        0.35f, starting, 0.5f, true);

                if (starting)
                {
                    ArmRotation(0.13f);
                    starting = false;
                }

                if (curveDriveDone)
                {
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Stage 8: Forward 1 Stone Width-
            if (autoStage == 8)
            {
                //Drive Forward 8 Inches (1 Stone Width)
                CurveDrive(new VectorF(constSkybridgeSafeX, constSkybridgeSafeY), -90,
                        new VectorF(constSkybridgeSafeX, constSkybridgeSafeY - 8.0f), -90,
                        0.5f, starting, 0.5f, true);

                if (starting)
                {
                    ArmRotation(0.25f);
                    starting = false;
                }

                if (curveDriveDone)
                {
                    autoStage++;
                    starting = true;
                }
            }
            //endregion


            if (autoStage == 9)
            {
                bot.stop();
            }
        }
        //endregion

        //region -AUTO 4 : Blue Build Plate-
        if (programMode == ProgramMode.AUTO4)
        {
            UpdateBotPosition();
            telemetry.addData("Bot Position", botPosition.toString());
            telemetry.addData("AutoStage", autoStage);
            telemetry.addData("Curve Progress", curveDriveProgress);
            telemetry.addData("", "AUTO4 Running");

            //region -Go to build plate-
            if (autoStage == 1)
            {
                //Starting with front facing wall
                CurveDrive(new VectorF(9,108), 0,
                        new VectorF(48-4, 120), 0,
                        0.5f, starting, -0.5f, false);

                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Grab Build Plate-
            if (autoStage == 2)
            {
                if (starting)
                {
                    timer.reset();
                    starting = false;
                }

                telemetry.addData("Waiting", timer.milliseconds() / 1000.0f);

                if (timer.milliseconds() / 1000.0f > 1.0f)
                {
                    bot.mecanumDrive.run(-0.3f, 0.03f * (bot.GetAngle() - 180), 0, false);
                }

                if (timer.milliseconds() / 1000.0f > 1.0f)
                {
                    bot.mecanumDrive.run(0.0f, 0.03f * (bot.GetAngle() - 180), 0, false);
                    bot.leftPlate.setPosition(1.0f);
                    bot.rightPlate.setPosition(1.0f);
                }

                if (timer.milliseconds() / 1000.0f > 3.0f) //1.5f
                {
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Bring Build Plate Towards Building Zone-
            if (autoStage == 3)
            {
                CurveDrive(new VectorF(49-4,120), 180,
                        new VectorF(17f, 100), 270,
                        0.5f, starting, 0.4f, false);

                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Back Build Plate into Building Zone-
            if (autoStage == 4)
            {
                CurveDrive(new VectorF(17f,108), 90,
                        new VectorF(17f, 117), 90,
                        0.5f, starting, -0.5f, false);

                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Raise Plate Grabber-
            if (autoStage == 5)
            {
                if (starting)
                {
                    timer.reset();
                    bot.leftPlate.setPosition(0.0f);
                    bot.rightPlate.setPosition(0.0f);
                    starting = false;
                }

                telemetry.addData("Waiting", timer.milliseconds() / 1000.0f);

                if (timer.milliseconds() / 1000.0f > 0.7f)
                {
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Parking Under Bridge-
            if (autoStage == 6)
            {
                if (parkingClose)
                {
                    //EndX is 12
                    CurveDrive(new VectorF(17, 117), 270,
                            new VectorF(12-3, 72), 270,
                            0.5f, starting, 0.5f, false);
                }
                else
                {
                    //EndX is 36
                    CurveDrive(new VectorF(17, 117), 270,
                            new VectorF(36-3, 72), 270,
                            0.5f, starting, 0.5f, false);
                }


                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            if (autoStage == 7)
            {
                bot.stop();
            }
        }

        //endregion

        //region -Auto 5-
        if (programMode == ProgramMode.AUTO5)
        {
            UpdateBotPosition();
            telemetry.addData("Bot Position", botPosition.toString());
            telemetry.addData("AutoStage", autoStage);
            telemetry.addData("Curve Progress", curveDriveProgress);
            telemetry.addData("", "AUTO5 Running");
            telemetry.addData("Angle", bot.GetAngle());

            if (autoStage == 1)
            {
                //Starting with front facing wall
                CurveDrive(new VectorF(9,108), 0,
                        new VectorF(96-9, 120), 0,
                        0.5f, starting, 0.5f, false);

                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }

            if (autoStage == 2)
            {
                if (starting)
                {
                    timer.reset();
                    starting = false;
                    bot.leftPlate.setPosition(1.0f);
                    bot.rightPlate.setPosition(1.0f);
                }

                telemetry.addData("Waiting", timer.milliseconds() / 1000.0f);

                if (timer.milliseconds() / 1000.0f > 2.0f)
                {
                    autoStage++;
                    starting = true;
                }
            }

            if (autoStage == 3)
            {
                CurveDrive(new VectorF(96-9,120), 0,
                        new VectorF(9, 108), 0,
                        0.5f, starting, -0.5f, false);

                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }

            if (autoStage == 4)
            {
                bot.stop();
            }
        }
        //endregion

        //region -AUTO 6 : Red Build Plate-
        if (programMode == ProgramMode.AUTO6)
        {
            UpdateBotPosition();
            telemetry.addData("Bot Position", botPosition.toString());
            telemetry.addData("AutoStage", autoStage);
            telemetry.addData("Curve Progress", curveDriveProgress);
            telemetry.addData("", "AUTO6 Running");

            //region -Go to build plate-
            if (autoStage == 1)
            {
                //Starting with front facing wall
                CurveDrive(new VectorF(144-9,108), 180,
                        new VectorF(144-(48-4), 120), 180,
                        0.5f, starting, -0.5f, false);

                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Grab Build Plate-
            if (autoStage == 2)
            {
                if (starting)
                {
                    timer.reset();
                    starting = false;
                }

                telemetry.addData("Waiting", timer.milliseconds() / 1000.0f);

                if (timer.milliseconds() / 1000.0f > 1.0f)
                {
                    bot.mecanumDrive.run(-0.3f, 0.03f * (bot.GetAngle() - 0), 0, false);
                }

                if (timer.milliseconds() / 1000.0f > 1.0f)
                {
                    bot.mecanumDrive.run(0.0f, 0.03f * (bot.GetAngle() - 0), 0, false);
                    bot.leftPlate.setPosition(1.0f);
                    bot.rightPlate.setPosition(1.0f);
                }

                if (timer.milliseconds() / 1000.0f > 3.0f) //1.5f
                {
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Bring Build Plate Towards Building Zone-
            if (autoStage == 3)
            {
                CurveDrive(new VectorF(144-(49-4),120), 0,
                        new VectorF(144-17, 100), -90,
                        0.5f, starting, 0.4f, false);

                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Back Build Plate into Building Zone-
            if (autoStage == 4)
            {
                CurveDrive(new VectorF(144-17,108), 90,
                        new VectorF(144-17, 117), 90,
                        0.5f, starting, -0.5f, false);

                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Raise Plate Grabber-
            if (autoStage == 5)
            {
                if (starting)
                {
                    timer.reset();
                    bot.leftPlate.setPosition(0.0f);
                    bot.rightPlate.setPosition(0.0f);
                    starting = false;
                }

                telemetry.addData("Waiting", timer.milliseconds() / 1000.0f);

                if (timer.milliseconds() / 1000.0f > 0.7f)
                {
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Parking Under Bridge-
            if (autoStage == 6)
            {
                if (parkingClose)
                {
                    //EndX is 12
                    CurveDrive(new VectorF(144-17, 117), -90,
                            new VectorF(144-(12-3), 72), -90,
                            0.5f, starting, 0.5f, false);
                }
                else
                {
                    //EndX is 36
                    CurveDrive(new VectorF(144-17, 117), -90,
                            new VectorF(144-(36-3), 72), -90,
                            0.5f, starting, 0.5f, false);
                }


                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            if (autoStage == 7)
            {
                bot.stop();
            }
        }
        //endregion

        //region -AUTO 7-
        if (programMode == ProgramMode.AUTO7) {
            UpdateBotPosition();
            telemetry.addData("Bot Position", botPosition.toString());
            telemetry.addData("AutoStage", autoStage);
            telemetry.addData("Curve Progress", curveDriveProgress);

            if (autoStage == 1) {
                //Drive Forward
                CurveDriveStrafe(new VectorF(0, 0), 0,
                        new VectorF(48, 0), 0,
                        0.5f, starting, 0.3f, false, 0, 0.3f);

                if (starting) {
                    starting = false;
                }

                if (curveDriveDone) {
                    autoStage++;
                    starting = true;
                    bot.mecanumDrive.stop();
                }
            }

            if (autoStage == 2) {
                //Drive To The Left
                CurveDriveStrafe(new VectorF(48, 0), 90,
                        new VectorF(48, -96), 90,
                        0.5f, starting, 0.5f, false, 0, 0.3f);

                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone) {
                    autoStage++;
                    starting = true;
                    bot.mecanumDrive.stop();
                }
            }

            if (autoStage == 3) {
                bot.mecanumDrive.stop();
            }
        }
        //endregion

        //region -AUTO 8 : Picks Skystone-
        if (programMode == ProgramMode.AUTO8) {

            UpdateBotPosition();
            telemetry.addData("AutoStage", autoStage);
            telemetry.addData("Bot Position", botPosition.toString());
            telemetry.addData("AutoStage", autoStage);
            telemetry.addData("Curve Progress", curveDriveProgress);

            //region -Drive Forward to see skystone-
            if (autoStage == 1) {
                CurveDrive(new VectorF(9, 40), 0,
                        new VectorF(28, 40), 0,
                        0.5f, starting, 0.25f, false);

                if (starting) {
                    starting = false;
                    vuforiaService.activate();
                }

                vuforiaService.update();

                if (curveDriveDone) {
                    autoStage++;
                    starting = true;
                    bot.mecanumDrive.stop();
                }

            }
            //endregion

            //region -Wait to see skystone-
            if (autoStage == 2) {
                vuforiaService.update();
                if (starting) {
                    timer.reset();
                    starting = false;
                }

                telemetry.addData("Waiting", timer.milliseconds() / 1000.0f);

                if (timer.milliseconds() / 1000.0f > 1.0f || vuforiaService.getSkystoneCordinates() != null) {
                    if (vuforiaService.getSkystoneCordinates() != null) {
                        if ((vuforiaService.getSkystoneCordinates().get(1) < 0 && blueSide) || (vuforiaService.getSkystoneCordinates().get(1) > 0 && !blueSide)) {
                            skystoneInchesFromWall = 20;
                        } else {
                            skystoneInchesFromWall = 12;
                        }
                    } else {
                        skystoneInchesFromWall = 4;
                    }
                    vuforiaService.deActivate();
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Position and rotate robot to prepare strafing to the skystone or prepare for slight turn into top skystone-
            if (autoStage == 3) {
                telemetry.addData("Skystone From Wall", skystoneInchesFromWall);

                if (skystoneInchesFromWall <= 12)
                {
                    CurveDriveStrafe(new VectorF(28, 40), 90,
                            new VectorF(28, (skystoneInchesFromWall + 24) + 14), 0,
                            0.0f, starting, 0.4f, false, -90, 0.6f);
                }
                else
                {
                    CurveDriveStrafe(new VectorF(28, 40), 90,
                            new VectorF(28, 60), 0,
                            0.0f, starting, 0.4f, false, 0, 0.3f);
                }

                if (starting) {
                    IntakeActive(true);
                    starting = false;
                }

                if (curveDriveDone) {
                    autoStage++;
                    starting = true;
                    bot.mecanumDrive.stop();
                    ArmRotation(0.25f);
                    GrabberActive(false); //Open Arm
                }
            }
            //endregion

            //region -(Stafe to the skystone or turn into the top skystone) and slightly raise arm-
            if (autoStage == 4) {
                telemetry.addData("Skystone From Wall", skystoneInchesFromWall);

                if (skystoneInchesFromWall <= 12)
                {
                    CurveDriveStrafe(new VectorF(30, (skystoneInchesFromWall + 24) + 14), 0,
                            new VectorF(47, (skystoneInchesFromWall + 24) + 14), 0,
                            0.0f, starting, 0.6f, false, -90, 0.4f);

                    if (curveDriveDone) {
                        autoStage++;
                        starting = true;
                        bot.mecanumDrive.stop();
                    }
                }
                else
                {
                    //Turn Into Topmost Stone, Raise Arm and activate intake
                    CurveDrive(new VectorF(24,60), 0,
                            new VectorF(47 - (float)(9.0f/Math.sqrt(2)) + 3.5f, 48 + (float)(9.0f/Math.sqrt(2)) - 3.5f), -50,
                            0.3f, starting, 0.5f, false);

                    if (curveDriveDone) {
                        autoStage = 16; //For now, end the program
                        starting = true;
                        bot.mecanumDrive.stop();
                    }
                }

                if (starting) {
                    ArmRotation(0.25f);
                    GrabberActive(false); //Open Arm
                    starting = false;
                }
            }
            //endregion

            //region -Drive into skystone-
            if (autoStage == 5) {
                telemetry.addData("Skystone From Wall", skystoneInchesFromWall);

                CurveDriveStrafe(new VectorF(47, (skystoneInchesFromWall + 24) + 14), -90,
                        new VectorF(47, (skystoneInchesFromWall + 24) + 14 - 6), -90,
                        0.0f, starting, 0.4f, false, -90, 0.4f);

                if (starting) {
                    starting = false;
                }

                if (curveDriveDone) {
                    autoStage++;
                    starting = true;
                    bot.mecanumDrive.stop();
                }
            }
            //endregion

            //region -Stafe back to the open-
            if (autoStage == 6) {
                CurveDriveStrafe(new VectorF(47, (skystoneInchesFromWall + 24) + 14 - 6), -90,
                        new VectorF(36-3, (skystoneInchesFromWall + 24) + 14 - 6), -90,
                        0.0f, starting, 0.6f, false, -90, 0.4f);

                if (starting) {
                    starting = false;
                }

                if (curveDriveDone) {
                    autoStage++;
                    starting = true;
                    bot.mecanumDrive.stop();
                }
            }
            //endregion

            //region -Drive forward to the field side with the buildplate, lower arm and grasp block-
            if (autoStage == 7) {
                CurveDriveStrafe(new VectorF(36-3, (skystoneInchesFromWall + 24) + 14 - 6), 90,
                        new VectorF(36-3, 120), 90,
                        0.0f, starting, 1.0f, true, -90, 0.4f);

                if (starting) {
                    ArmRotation(0.13f);
                    IntakeActive(false);
                    starting = false;
                }

                if (curveDriveProgress >= 0.50f)
                {
                    GrabberActive(true);
                }

                if (curveDriveDone) {
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Rotate so back with plate grabbers is facing the build plate-
            if (autoStage == 8) {
                CurveDriveStrafe(new VectorF(36-3, 120), 90,
                        new VectorF(48-4, 120), 90,
                        0.0f, starting, 0.4f, false, -180, 0.6f);

                if (starting) {
                    starting = false;
                }

                if (curveDriveDone) {
                    autoStage++;
                    starting = true;
                    bot.mecanumDrive.stop();
                }
            }
            //endregion

            //region -Run into the build plate-
            if (autoStage == 9) {
                autoStage++;
                starting = true;
                bot.mecanumDrive.stop();
            }
            //endregion

            //region -Grab build plate and position arm-
            if (autoStage == 10) {
                if (starting) {
                    timer.reset();
                    ArmRotation(0.83f);
                    PlateGrabber(true);
                    starting = false;
                }

                telemetry.addData("Waiting", timer.milliseconds() / 1000.0f);

                if (timer.milliseconds() / 1000.0f > 0.6f) {
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Drive Build plate towards build zone on a curve-
            if (autoStage == 11) {
                CurveDrive(new VectorF(48-4,120), 180,
                        new VectorF(17f, 100), 270,
                        0.5f, starting, 0.4f, false);

                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region Back up robot to make sure build plate is in build zone
            if (autoStage == 12) {
                CurveDrive(new VectorF(17f,100), 90,
                        new VectorF(17f, 117), 90,
                        0.5f, starting, -0.5f, false);

                if (starting)
                {
                    starting = false;
                }

                if (curveDriveDone)
                {
                    bot.mecanumDrive.run(0,0,0,false);
                    autoStage++;
                    starting = true;
                }
            }
            //endregion

            //region -Place stone, record position for later blocks-
            if (autoStage == 13) {
                if (starting) {
                    timer.reset();
                    GrabberActive(false);
                    starting = false;
                }


                if (timer.milliseconds() / 1000.0f > 1.0f) {
                    buildingPosition = botPosition;
                    starting = true;
                    autoStage++;
                }

            }
            //endregion

            //region -Reset arm and raise build plate grabber-
            if (autoStage == 14)
            {
                if (starting)
                {
                    GrabberActive(true);
                    ArmRotation(0.13f);
                    timer.reset();
                    starting = false;
                    PlateGrabber(false);
                }

                if (timer.milliseconds() / 1000.0f > 1.2f)
                {
                    autoStage++;
                    starting = true;
                }
            }
            //endregion


            if (autoStage == 15)
            {
                bot.stop();
            }
        }
        //endregion

        //region -AUTO VISION-
        if (programMode == ProgramMode.AUTO_VISION)
        {
            vuforiaService.activate();
            vuforiaService.update();
            VectorF skystoneCords;
            if (vuforiaService.getSkystoneCordinates() != null)
            {
                skystoneCords = vuforiaService.getSkystoneCordinates();
                telemetry.addData("Skystone Visible", skystoneCords.toString());
                telemetry.addData("Skystone Sideways", skystoneCords.get(1));
            }
            else
            {
                skystoneCords = null;
                telemetry.addData("", "Skystone Not Visible");
            }
        }
        //endregion



        telemetry.update();
    }

    @Override
    public void stop()
    {
        bot.stop();
        if (programMode == ProgramMode.AUTO_VISION || programMode == ProgramMode.AUTO8)
        {
            vuforiaService.deActivate();
        }
    }

    //region -Reusable Functions-


    void GrabberActive(boolean holding)
    {
        bot.grabber.setPosition((holding) ? 0.40f : 0.65f);
    }


    void IntakeActive(boolean active)
    {
        if (active)
        {
            bot.rightIntake.setPower(-0.8f);
            bot.leftIntake.setPower(-0.8f);
        }
        else
        {
            bot.rightIntake.setPower(0.0f);
            bot.leftIntake.setPower(0.0f);
        }
    }

    void PlateGrabber(boolean down)
    {
        float plateGrabberPos = Math5446.boolTo01(down);
        bot.leftPlate.setPosition(plateGrabberPos);
        bot.rightPlate.setPosition(plateGrabberPos);
    }

    void ArmRotation(float rotation)
    {
        bot.leftStoneRotator.setPosition(rotation);
        bot.rightStoneRotator.setPosition(rotation);
        //bot.levelStoneRotator.setPosition(rotation);
    }


    void CurveDrive(VectorF start, float startTangent, VectorF end, float endTangent, float curveStrength, boolean theStart, float power, boolean curveChained)
    {
        float progressIncrease = 0.04f;
        float curveDrivingTolerance = 7.0f;
        float endTolerance = 3.0f;

        if (theStart)
        {
            if (curveChained)
            {
                endTolerance = curveDrivingTolerance;
            }
            curveDriveProgress = 0.0f;
            curveDriveDone = false;
        }

        VectorF target = Math5446.CurveDrivingTarget(start, startTangent, end, endTangent, curveStrength, curveDriveProgress);

        //Computations regarding where target is in relation with bot
        VectorF botToTarget = Math5446.RotatePoint(target.subtracted(botPosition), (float) ((-Math.PI / 180.0f) * bot.GetAngle()));
        float botToTargetDistance = botToTarget.magnitude();
        botToTarget = new VectorF(botToTarget.get(0), botToTarget.get(1), 0);
        botToTarget.normalized3D();

        //Extend target on curve if too close
        if (botToTargetDistance < curveDrivingTolerance) {
            curveDriveProgress += progressIncrease;
            curveDriveProgress = Math5446.Clamp(curveDriveProgress, 0, 1);
            if (curveDriveProgress == 1 && botToTargetDistance < endTolerance) {
                curveDriveDone = true;
            }
        }

        VectorF driveVector = new VectorF(botToTarget.get(0), botToTarget.get(1));

        float speedReduction = Math5446.Clamp((float)(Math.pow(botToTargetDistance / curveDrivingTolerance,2)), 0, 1);

        //Reduce Forward Speed the sharper we have to turn (Currently with 0 coefficent)
        float slowForTurning = (float) (Math.abs(0.0f * Math.atan2(botToTarget.get(1), botToTarget.get(0))));

        if (power > 0) {
            driveVector = new VectorF(Math5446.Clamp(driveVector.get(0) - slowForTurning, 0, 1), driveVector.get(1));
        }
        else if (power < 0) {
            driveVector = new VectorF(Math5446.Clamp(driveVector.get(0) + slowForTurning, -1, 0), driveVector.get(1));
        }

        //Drive the robot
        driveVector = new VectorF(Math.abs(power) * driveVector.get(0) * speedReduction, -power * driveVector.get(1) * speedReduction);
        bot.mecanumDrive.run(driveVector.get(0), 0.5f * driveVector.get(1), 0, false);
    }


    void CurveDriveStrafe(VectorF start, float startTangent, VectorF end, float endTangent, float curveStrength, boolean theStart, float power, boolean curveChained, float endHeading, float maxTurnPower) {
        float progressIncrease = 0.04f;
        float curveDrivingTolerance = 4.0f;
        float endTolerance = 3.0f;

        if (theStart) {
            if (curveChained) {
                endTolerance = curveDrivingTolerance;
            }
            curveDriveProgress = 0.0f;
            curveDriveDone = false;
        }

        VectorF target = Math5446.CurveDrivingTarget(start, startTangent, end, endTangent, curveStrength, curveDriveProgress);

        //Computations regarding where target is in relation with bot
        VectorF botToTarget = Math5446.RotatePoint(target.subtracted(botPosition), (float) ((-Math.PI / 180.0f) * bot.GetAngle()));
        float botToTargetDistance = botToTarget.magnitude();
        botToTarget = new VectorF(botToTarget.get(0), botToTarget.get(1), 0);
        botToTarget.normalized3D();

        //Extend target on curve if too close
        if (botToTargetDistance < curveDrivingTolerance) {
            curveDriveProgress += progressIncrease;
            curveDriveProgress = Math5446.Clamp(curveDriveProgress, 0, 1);
            if (curveDriveProgress == 1 && botToTargetDistance < endTolerance && Math.abs(bot.GetAngle() - endHeading) < 10 ) {
                curveDriveDone = true;
            }
            telemetry.addData("Angle Error", Math.abs(bot.GetAngle() - endHeading));
        }

        VectorF driveVector = new VectorF(botToTarget.get(0), botToTarget.get(1));

        float speedReduction = Math5446.Clamp((float) (Math.pow(botToTargetDistance / curveDrivingTolerance, 2)), 0, 1);

        float turnSpeed = (float) Math5446.Clamp(0.06f * (float)(bot.GetAngle() - endHeading), -maxTurnPower, maxTurnPower);

        //Drive the robot
        driveVector = new VectorF(Math.abs(power) * driveVector.get(0) * speedReduction, Math.abs(power) * driveVector.get(1) * speedReduction);
        bot.mecanumDrive.run(driveVector.get(0), turnSpeed, -driveVector.get(1), false);
    }

    void UpdateBotPosition()
    {
        VectorF botTranslation = Math5446.RotatePoint(mecanumTranslation(bot.rightFrontMotor, bot.rightBackMotor, bot.leftFrontMotor, bot.leftBackMotor, 4.0f), (float) Math.toRadians(bot.GetAngle()));
        botPosition.add(botTranslation);
    }


    void Heading(float heading)
    {
        float tolerance = 5;
        float power = 1;
        float appliedPower = power * Math5446.sign((float) (heading - bot.GetAngle()));
        
        bot.mecanumDrive.run(0, appliedPower,0,false); //Negative power for counter-clockwise (Greater heading value)
    }

    //region mecanumTranslation(...)
    float oldRF;
    float oldRB;
    float oldLF;
    float oldLB;

    /**
     *
     * @param rightFront
     * @param rightBack
     * @param leftFront
     * @param leftBack
     * @param wheelDiameter
     * @return
     */
    VectorF mecanumTranslation(DcMotor rightFront, DcMotor rightBack, DcMotor leftFront, DcMotor leftBack, float wheelDiameter)
    {
        // Right/Left   Front/Back
        float rawRF = clicksToInches(rightFront.getCurrentPosition());
        float rawRB = clicksToInches(rightBack.getCurrentPosition());
        float rawLF = clicksToInches(leftFront.getCurrentPosition());
        float rawLB = clicksToInches(leftBack.getCurrentPosition());

        float newRF = rawRF - oldRF;
        float newRB = rawRB - oldRB;
        float newLF = rawLF - oldLF;
        float newLB = rawLB - oldLB;

        oldRF = rawRF;
        oldRB = rawRB;
        oldLF = rawLF;
        oldLB = rawLB;

        //Math is from http://robotsforroboticists.com/drive-kinematics/ , Divisor Changed
        VectorF translation = new VectorF((newLF + newRF + newLB + newRB) * (wheelDiameter / 16), (-newLF + newRF + newLB - newRB) * (wheelDiameter / 16));
        //Correcting Math mistakes and constant errors
        translation = new VectorF(translation.get(0), 0.75f * translation.get(1));
        return translation;
        //return new VectorF((newLF + newRF + newLB + newRB) * (wheelDiameter / 16), (-newLF + newRF + newLB - newRB) * (wheelDiameter / 16));
    }
    //endregion

    //region inches <--> clicks
    int inchesToClicks(double inch)
    {
        return Encoder.clicksByDistance(Encoder.MotorTypes.ANDYMARK40, inch, 4.0f, 1.5f); //ANDYMARK40 has 1140 ticksPerRevolution
    }

    float clicksToInches(int clicks)
    {
        return (clicks / Encoder.clicksByInch(Encoder.MotorTypes.ANDYMARK40, 4.0f, 1.5f));
    }
    //endregion

    //endregion
}
