package org.firstinspires.ftc.teamcode.Team5446.Testing.Drive_And_Position;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Calibrate IMU", group = "Team 5446")
@Disabled
public class CalibrateIMU extends LinearOpMode
{
    MecanumBot bot = null;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode()
    {
        bot = new MecanumBot();
        bot.init(hardwareMap);

        // Wait until we're told to go
        telemetry.log().add("Waiting for start...");
        while (!isStarted()) {
            telemetry.update();
            idle();
        }
        telemetry.log().add("...started...");

        while (opModeIsActive()) {

            telemetry.addData("calib", bot.imu.getCalibrationStatus().toString());

            if (bot.imu.isAccelerometerCalibrated())
            {
                telemetry.addData("IMU Calibrated", "True");
                telemetry.log().add("Press A to write the current");
                telemetry.log().add("calibration data to a file");

                if (gamepad1.a) {

                    // Get the calibration data
                    BNO055IMU.CalibrationData calibrationData = bot.imu.readCalibrationData();

                    // Save the calibration data to a file. You can choose whatever file
                    // name you wish here, but you'll want to indicate the same file name
                    // when you initialize the IMU in an opmode in which it is used. If you
                    // have more than one IMU on your robot, you'll of course want to use
                    // different configuration file names for each.
                    String filename = "5446IMUCalibration.json";
                    File file = AppUtil.getInstance().getSettingsFile(filename);
                    ReadWriteFile.writeFile(file, calibrationData.serialize());
                    telemetry.log().add("saved to '%s'", filename);

                    telemetry.log().add("Calibration File Updated");
                    // Wait for the button to be released
                    while (gamepad1.a) {
                        telemetry.update();
                        idle();
                    }
                }
            }
            else
            {
                telemetry.addData("IMU Calibrated", "False");
            }
            telemetry.update();
        }
    }
}

/*
telemetry.addData("IMU Position In Meters", bot.imu.getPosition().toString());
Position tpos = bot.imu.getPosition();
tpos.toUnit(DistanceUnit.INCH);
telemetry.addData("IMU Position In Inches", tpos.toString());
telemetry.addData("IMU Rotation", bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
 */
