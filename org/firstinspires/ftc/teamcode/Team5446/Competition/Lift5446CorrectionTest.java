package org.firstinspires.ftc.teamcode.Team5446.Competition;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Lift Correction 2 5446", group = "Team 5446")
//@Disabled
public class Lift5446CorrectionTest extends OpMode {
    Bot5446 bot = null;
    private ElapsedTime runtime = new ElapsedTime();

    float maxPower = 0.5f;

    int targetLiftPosition;
    int choosingTargetLiftPosition;
    float averageLiftPosition;
    float averagePower = 0.50f;
    float syncErrorCorrectionMultiplier = 0.005f;
    float toleranceDistance = 200;

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
        bot.leftLift.setTargetPosition(0);
        bot.rightLift.setTargetPosition(0);
        bot.leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bot.rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bot.leftLift.setPower(maxPower);
        bot.rightLift.setPower(maxPower);
    }

    @Override
    public void loop()
    {
        telemetry.addData("Status", "Running: " + runtime.toString());

        choosingTargetLiftPosition += 100 * (gamepad1.left_stick_y);
        if (gamepad1.b)
        {
            targetLiftPosition = choosingTargetLiftPosition;
        }

        maxPower += 0.02f * gamepad1.right_stick_y;
        maxPower = Math5446.Clamp(maxPower, 0, 1);


        telemetry.addData("", "Press B to set target");
        telemetry.addData("", "Change target with left stick Y");
        telemetry.addData("Left Stick Y", gamepad1.left_stick_y);
        telemetry.addData("Target", choosingTargetLiftPosition);
        telemetry.addData("", "Change max power with right stick Y");
        telemetry.addData("Max Power", maxPower);
        telemetry.addData("", "");

        bot.leftLift.setTargetPosition(targetLiftPosition);
        bot.rightLift.setTargetPosition(targetLiftPosition);


        averageLiftPosition = (bot.leftLift.getCurrentPosition() + bot.rightLift.getCurrentPosition()) / 2.0f;
        int syncError = bot.leftLift.getCurrentPosition() - bot.rightLift.getCurrentPosition();

        int leftLeftover = Math.abs(targetLiftPosition - bot.leftLift.getCurrentPosition());
        int rightLeftover = Math.abs(targetLiftPosition - bot.rightLift.getCurrentPosition());

        /*
        if (leftLeftover < rightLeftover)
        {
            bot.leftLift.setPower(Math5446.Clamp(maxPower - syncErrorCorrectionMultiplier * Math.abs(syncError), 0, 1));
            bot.rightLift.setPower(maxPower);
        }
        else if (rightLeftover < leftLeftover)
        {
            bot.leftLift.setPower(maxPower);
            bot.rightLift.setPower(Math5446.Clamp(maxPower - syncErrorCorrectionMultiplier * Math.abs(syncError), 0, 1));
        }
        else
        {
            bot.leftLift.setPower(maxPower);
            bot.rightLift.setPower(maxPower);
        }
        */

        bot.rightLift.setPower(maxPower);
        bot.leftLift.setPower(maxPower);

        /*
        //Max Speed (Note: Negative encoder values are higher up)
        float leftPower = averagePower + syncErrorCorrectionMultiplier * -syncError;
        float rightPower = averagePower + syncErrorCorrectionMultiplier * syncError;
        //Direction and Dampening to reach target

        //TODO Your not incorporating error below, being overwritten

        leftPower *= Math5446.Clamp((targetLiftPosition - bot.leftLift.getCurrentPosition()) / toleranceDistance, -1, 1);
        rightPower *= Math5446.Clamp((targetLiftPosition - bot.rightLift.getCurrentPosition()) / toleranceDistance, -1, 1);

        bot.leftLift.setPower(leftPower);
        bot.rightLift.setPower(rightPower);

        telemetry.addData("SyncErrorPower", Math5446.Clamp(syncErrorCorrectionMultiplier * syncError, -1, 1));
        telemetry.addData("Average LiftPos", averageLiftPosition);
        */
        telemetry.addData("Raw Lift Error", bot.leftLift.getCurrentPosition() - bot.rightLift.getCurrentPosition());
        telemetry.addData("Left LiftPos", bot.leftLift.getCurrentPosition());
        telemetry.addData("Right LiftPos", bot.rightLift.getCurrentPosition());

        telemetry.update();
    }

    @Override
    public void stop()
    {
        bot.stop();
    }
}
