package org.firstinspires.ftc.teamcode.Team5446.Competition;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.enumclaw.ftc.teamcode.Encoder;

public class LinearAutonomous5446 extends LinearOpMode
{
    Bot5446 bot = null;
    ElapsedTime timer = new ElapsedTime();

    public enum ProgramMode
    {
        DRIVE_BASE_TEST,
        AUTO,
        LEFT_FAR,
        LEFT_NEAR,
        RIGHT_FAR,
        RIGHT_NEAR,
        BUILD_PLATE;
    }
    ProgramMode programMode;


    @Override
    public void runOpMode()
    {
        bot = new Bot5446();
        bot.init(hardwareMap);

        waitForStart();
        //region waitForStartAlternative();
        //(Use as backup) A Bug with our phones causes a chance to disconnect while waiting for start if no constant updates are given. This is only a workaround!
//        while (!opModeIsActive() && !isStopRequested())
//        {
//            telemetry.addData("status", "waiting for start command...");
//            telemetry.update();
//        }
        //endregion

        if (programMode == ProgramMode.DRIVE_BASE_TEST)
        {
            bot.leftFrontMotor.setPower(1.0f);
            waitInSeconds(1.5f);
            bot.leftFrontMotor.setPower(0.0f);
            bot.leftBackMotor.setPower(1.0f);
            waitInSeconds(1.5f);
            bot.leftBackMotor.setPower(0.0f);
            bot.rightFrontMotor.setPower(1.0f);
            waitInSeconds(1.5f);
            bot.rightFrontMotor.setPower(0.0f);
            bot.rightBackMotor.setPower(1.0f);
            waitInSeconds(1.5f);
            bot.rightBackMotor.setPower(0.0f);
        }


        if (programMode == ProgramMode.LEFT_NEAR || programMode == ProgramMode.RIGHT_NEAR)
        {
            DriveStraight(0.5f, 20, 0);
            waitInSeconds(0.5f);
        }
        if (programMode == ProgramMode.LEFT_FAR)
        {
            DriveStraight(0.5f, 20, 0);
            waitInSeconds(0.5f);
            Rotate(-90);
            waitInSeconds(0.5f);
            DriveStraight(0.5f, 24, -90);
        }
        else if (programMode == ProgramMode.RIGHT_FAR)
        {
            DriveStraight(0.5f, 20, 0);
            waitInSeconds(0.5f);
            Rotate(90);
            waitInSeconds(0.5f);
            DriveStraight(0.5f, 24, 90);
        }

        if (programMode == ProgramMode.BUILD_PLATE)
        {
            DriveStraight(0.4f, -48, 0);
            waitInSeconds(0.3f);
            //bot.plateGrabber.setPower(-0.75f);
            waitInSeconds(2.0f);
            //bot.plateGrabber.setPower(0.0f);
            DriveStraight(0.4f, 48, 0);
        }

        bot.stop();
    }

    void waitInSeconds(float seconds)
    {
        timer.reset();
        while (opModeIsActive() && timer.milliseconds() < (1000 * seconds))
        {
            updateMyTelemetry();
        }
    }

    void updateMyTelemetry()
    {
        telemetry.addData("Angle", bot.GetAngle());
        telemetry.addData("LeftFront", bot.leftFrontMotor.getCurrentPosition());
        telemetry.addData("LeftBack", bot.leftBackMotor.getCurrentPosition());
        telemetry.addData("RightFront", bot.rightFrontMotor.getCurrentPosition());
        telemetry.addData("RightBack", bot.rightBackMotor.getCurrentPosition());
        telemetry.update();
    }

    void DriveStraight(float power, float inches, float heading)
    {
        power = Math.abs(power);
        float startingPosition = clicksToInches(bot.leftFrontMotor.getCurrentPosition());
        float targetPosition = startingPosition + inches;
        float correctionPower = 0.06f;
        float currentPosition = startingPosition;

        if (inches > 0)
        {
            while (opModeIsActive() && currentPosition < targetPosition)
            {
                currentPosition = clicksToInches(bot.leftFrontMotor.getCurrentPosition());
                bot.mecanumDrive.run(power, correctionPower * (bot.GetAngle() - heading), 0, false);
            }
        }
        else if (inches < 0)
        {
            while (opModeIsActive() && currentPosition > targetPosition)
            {
                currentPosition = clicksToInches(bot.leftFrontMotor.getCurrentPosition());
                bot.mecanumDrive.run(-power, correctionPower * (bot.GetAngle() - heading), 0, false);
            }
        }
        bot.stop();
    }

    void Rotate(float heading)
    {
        float power = 0.5f;
        float tolerance = 10;

        while (opModeIsActive() && Math.abs(heading - bot.GetAngle()) > tolerance)
        {
            bot.mecanumDrive.run(0, -power * Math5446.sign((float)(heading - bot.GetAngle())), 0, false);
            updateMyTelemetry();
        }
        bot.stop();
    }

    float clicksToInches(int clicks)
    {
        return (clicks / Encoder.clicksByInch(Encoder.MotorTypes.ANDYMARK40, 4.0f, 1.5f));
    }
}
