package org.firstinspires.ftc.teamcode.Team5446.Competition;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.enumclaw.ftc.teamcode.Encoder;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.Team5446.Competition.Math5446;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp Mecanum 5446", group = "Team 5446")
//@Disabled
public class TeleOp5446 extends OpMode
{

    Bot5446 bot = null;
    SoundService soundService = null;
    InputTimingManager inputTimingManager = new InputTimingManager();
    private ElapsedTime runtime = new ElapsedTime();

    boolean armPresetChanged = false;
    float targetLiftPosition = 0;
    float targetReachPosition = 0;
    VectorF botPosition = new VectorF(0,0);
    float wristPosition;
    double targetBotAngle;
    float stoneRotation = 0.15f;
    float stoneRotationManualOffset = 0.0f;
    float stoneRotationWristOffset = 0.0f;
    int armPreset = 1;
    float grabberPosition = 1.0f;

    @Override
    public void init()
    {
        telemetry.addData("Status", "Initializing");
        bot = new Bot5446();
        bot.init(hardwareMap);
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
        botPosition = new VectorF(0,0);
        wristPosition = 0.5f;
//        bot.leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        bot.rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        bot.leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        bot.rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //stoneRotation = (float) bot.leftStoneRotator.getPosition();

    }

    @Override
    public void loop()
    {
        telemetry.addData("Status", "Running: " + runtime.toString());

        //region -Operating Drive Base (Player 1)(Left and Right Sticks)-

        //Driving
        double driveSpeedForward = gamepad1.left_stick_y;
        double driveSpeedRotate =  gamepad1.left_stick_x;
        double driveSpeedStrafe = gamepad1.right_stick_x;
        bot.mecanumDrive.run(-driveSpeedForward, driveSpeedStrafe, driveSpeedRotate, gamepad1.right_bumper); //MecanumDrive class might have a flaw with forward speed being reversed.

        //Driving Sounds
        inputTimingManager.getTiming("Driving_Fast").UpdateInput((Math.abs(driveSpeedForward) > 0.6f) ? 1 : 0);
        if (inputTimingManager.getTiming("Driving_Fast").SignDuration(1) == 10)
        {
            soundService.PlaySkystoneSound(4, 1.0f, 0); //4 or 12 for soundIndex
        }
        telemetry.addData("Driving_Fast_Time", inputTimingManager.getTiming("Driving_Fast").SignDuration(1));

        //Bot Position Calculating
        VectorF botTranslation = Math5446.RotatePoint(mecanumTranslation(bot.rightFrontMotor, bot.rightBackMotor, bot.leftFrontMotor, bot.leftBackMotor, 4.0f), (float) Math.toRadians(bot.GetAngle()));
        botPosition.add(botTranslation);
        telemetry.addData("Bot Position", botPosition.toString());
        telemetry.addData("Bot Rotation", bot.GetAngle());

        //endregion

        //region -Operating Lift (Player 2)(Left Bumper - Left Trigger)-
        float liftClickSpeed = 120.0f;
        float liftMotorSpeed = 0.80f;
        int liftClickTolerance = 200;
        targetLiftPosition += liftClickSpeed * (Math5446.boolTo01(gamepad2.left_bumper) - gamepad2.left_trigger);
        //Left Lift
        if (Math.abs(bot.leftLift.getCurrentPosition() - targetLiftPosition) > liftClickTolerance)
            bot.leftLift.setPower((liftMotorSpeed * -Math5446.sign(bot.leftLift.getCurrentPosition() - targetLiftPosition)) * Math5446.Clamp(((Math.abs(bot.leftLift.getCurrentPosition() - targetLiftPosition) - liftClickTolerance) / (liftClickTolerance)), 0, 1));
        else
            bot.leftLift.setPower(0);
        //Right Lift
        if (Math.abs(bot.rightLift.getCurrentPosition() - targetLiftPosition) > liftClickTolerance)
            bot.rightLift.setPower((liftMotorSpeed * -Math5446.sign(bot.rightLift.getCurrentPosition() - targetLiftPosition)) * Math5446.Clamp(((Math.abs(bot.rightLift.getCurrentPosition() - targetLiftPosition) - liftClickTolerance) / (liftClickTolerance)), 0, 1));
        else
            bot.rightLift.setPower(0);

        inputTimingManager.getTiming("Lift_Input").UpdateInput(Math5446.boolTo01(gamepad2.left_bumper) - gamepad2.left_trigger);
        if (inputTimingManager.getTiming("Lift_Input").SignDuration(1) == 5)
        {
            soundService.PlaySkystoneSound(1, 1.0f, 0);
        }
        else if (inputTimingManager.getTiming("Lift_Input").SignDuration(-1) == 5)
        {
            soundService.PlaySkystoneSound(2, 1.0f, 0);
        }

        telemetry.addData("LeftLift", bot.leftLift.getCurrentPosition());
        telemetry.addData("RightLift", bot.rightLift.getCurrentPosition());

//        telemetry.addData("TargetLift", targetLiftPosition);
//        telemetry.addData("LeftLift-Position", bot.leftLift.getCurrentPosition());
//        telemetry.addData("RightLift-Position", bot.rightLift.getCurrentPosition());
//        telemetry.addData("LeftLift-Power", 0.35f * -Math5446.sign(bot.leftLift.getCurrentPosition() - targetLiftPosition));
//        telemetry.addData("RightLift-Power", 0.35f * -Math5446.sign(bot.rightLift.getCurrentPosition() - targetLiftPosition));


        //endregion

        //region -Operating Intake (Player 1)(Right Trigger - Right Bumper)-
        bot.leftIntake.setPower(1.0f * (gamepad1.right_trigger - Math5446.boolTo01(gamepad1.right_bumper)));
        bot.rightIntake.setPower(1.0f * (gamepad1.right_trigger - Math5446.boolTo01(gamepad1.right_bumper)));
        //endregion

        //region -Operating Grabber (Player 2)(B-X)-
        if (gamepad2.b)
        {
            grabberPosition = 0.65f; //Open
        }
        else if(gamepad2.x)
        {
            grabberPosition = 0.40f; //Close
        }
        //grabberPosition += 0.01f * (Math5446.boolTo01(gamepad2.b) - Math5446.boolTo01(gamepad2.x));
        //grabberPosition = Math5446.Clamp(grabberPosition, 0, 1);
        telemetry.addData("Grabber Position", grabberPosition);
        bot.grabber.setPosition(grabberPosition);
        //endregion

        //region -Operating Stone Rotator (Player 2)(Whole Dpad, both joysticks vertical)-

        stoneRotationManualOffset += ((1.0f/60.0f) * 1.5f * (gamepad2.right_stick_y));
        stoneRotationManualOffset = Math5446.Clamp(stoneRotationManualOffset, -0.4f, 0.4f);
        stoneRotationWristOffset += ((1.0f/60.0f) * 1.0f * (gamepad2.left_stick_y));
        stoneRotationWristOffset = Math5446.Clamp(stoneRotationWristOffset, -0.3f, 0.3f);
        stoneRotation = Math5446.Clamp(stoneRotation, 0, 1);
        telemetry.addData("Stone Rotation", stoneRotation);

        inputTimingManager.getTiming("Dpad_Vertical").UpdateInput(Math5446.boolTo01(gamepad2.dpad_up) - Math5446.boolTo01(gamepad2.dpad_down));
        if (inputTimingManager.getTiming("Dpad_Vertical").SignDuration(1) == 1 && armPreset < 6)
        {
            armPreset++;
            armPresetChanged = true;
        }
        else if (inputTimingManager.getTiming("Dpad_Vertical").SignDuration(-1) == 1 && armPreset > 1)
        {
            armPreset--;
            armPresetChanged = true;
        }
        else if (gamepad2.dpad_right) {
            armPreset = 1;
            armPresetChanged = true;
            stoneRotationManualOffset = 0;
        }
        else if (gamepad2.dpad_left) {
            armPreset = 4;
            armPresetChanged = true;
            stoneRotationManualOffset = 0;
        }

        //Close grabber when preset changes, but have a delay so it doesn't wack built tower or go inward before in position to grab stone from intake
        inputTimingManager.getTiming("armPreset_Change").UpdateInput(Math5446.boolTo01(armPresetChanged));
        if (inputTimingManager.getTiming("armPreset_Change").SignDuration(0) == 5)
        {
            grabberPosition = 0.40f; //Close
        }
        telemetry.addData("armPresetTime", inputTimingManager.getTiming("armPreset_Change").SignDuration(0));
        armPresetChanged = false;
        //End of above comment

        if (armPreset == 1)
            stoneRotation = 0.1f; //Grab Intake OldValue: 0.13
        if (armPreset == 2)
            stoneRotation = 0.25f; //Allow Intake OldValue: 0.25
        if (armPreset == 3)
            stoneRotation = 0.85f; //Plate Level 1 OldValue: 0.85
        if (armPreset == 4)
            stoneRotation = 0.7f; //Plate Level 2 OldValue: 0.70
        if (armPreset == 5)
            stoneRotation = 0.65f; //Plate Level 3 OldValue: 0.65
        if (armPreset == 6)
            stoneRotation = 1.0f; //Pick Up Capstone OldValue: 0.90

        bot.leftStoneRotator.setPosition(Math5446.Clamp(stoneRotation + stoneRotationManualOffset, 0, 1));
        bot.rightStoneRotator.setPosition(Math5446.Clamp(stoneRotation + stoneRotationManualOffset, 0, 1));
        //bot.levelStoneRotator.setPosition(Math5446.Clamp(stoneRotation + stoneRotationManualOffset + stoneRotationWristOffset, 0, 1));

        //Light Saber sound when stone rotator flips to building side
        inputTimingManager.getTiming("Stone_Rotator_Side").UpdateInput((stoneRotation > 0.30f) ? 1 : 0);
        if (inputTimingManager.getTiming("Stone_Rotator_Side").SignDuration(1) == 1)
        {
            soundService.PlaySkystoneSound(8, 1.0f, 0);
        }

        telemetry.addData("Left Stone Rotator Raw", bot.leftStoneRotator.getPosition());
        telemetry.addData("Right Stone Rotator Raw", bot.rightStoneRotator.getPosition());
        //telemetry.addData("Level Stone Rotator Raw", bot.levelStoneRotator.getPosition());

        //endregion

        //region Plate Grabber (Player 1) (Position: X=1, B=0)
        if (gamepad1.x)
        {
            //Down
            bot.leftPlate.setPosition(1.0f);
            bot.rightPlate.setPosition(1.0f);
        }
        if (gamepad1.b)
        {
            //Up
            bot.leftPlate.setPosition(0.0f);
            bot.rightPlate.setPosition(0.0f);
        }

        inputTimingManager.getTiming("Plate_Grabber_Down").UpdateInput(Math5446.boolTo01(gamepad1.x));
        if (inputTimingManager.getTiming("Plate_Grabber_Down").SignDuration(1) == 1)
        {
            soundService.PlaySkystoneSound(14, 1.0f, 0);
        }

        //endregion

        InputTiming timingDpadUp = inputTimingManager.getTiming("Dpad_Left");
        timingDpadUp.UpdateInput(Math5446.boolTo01(gamepad1.dpad_left));
        if (gamepad1.dpad_left)
        {
            if (timingDpadUp.SignDuration(1) == 1)
            {
                soundService.PlaySkystoneSound(3,1.0f,0);
            }
        }

        telemetry.update();
    }

    @Override
    public void stop()
    {
        bot.stop();
    }


    //region mecanumTranslation(...)
    float oldRF = 0;
    float oldRB = 0;
    float oldLF = 0;
    float oldLB = 0;

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

        //Math from http://robotsforroboticists.com/drive-kinematics/, divisor changed
        return new VectorF((newLF + newRF + newLB + newRB) * (wheelDiameter / 16), (-newLF + newRF + newLB - newRB) * (wheelDiameter / 16));

    }
    //endregion

    float clicksToInches(int clicks)
    {
        return (clicks / Encoder.clicksByInch(Encoder.MotorTypes.ANDYMARK40, 4.0f, 1.5f));
    }

}
