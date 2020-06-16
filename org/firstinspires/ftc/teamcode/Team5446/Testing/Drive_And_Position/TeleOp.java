package org.firstinspires.ftc.teamcode.Team5446.Testing.Drive_And_Position;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp: Position", group = "Team 5446")
@Disabled
public class TeleOp extends OpMode {

    MecanumBot bot = null;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init()
    {
        telemetry.addData("Status", "Initializing");
        bot = new MecanumBot();
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
        bot.imu.startAccelerationIntegration(new Position(DistanceUnit.METER, 0, 0, 0,0), bot.imu.getVelocity(), 1000);
    }

    @Override
    public void loop()
    {
        telemetry.addData("Status", "Running: " + runtime.toString());
        //bot.mecanumDrive.run(-gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x, gamepad1.right_bumper);

        if(gamepad1.a)
        {
            bot.imu.startAccelerationIntegration(new Position(DistanceUnit.METER, 0, 0, 0,0), bot.imu.getVelocity(), 20);
        }
        //bot.imu.isAccelerometerCalibrated();
        telemetry.addData("IMU Position", bot.imu.getPosition().toString());
        telemetry.addData("IMU Z Rotation", bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
        telemetry.addData("IMU Rotation", bot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).toString());
        telemetry.addData("IMU Acceleration", bot.imu.getAcceleration().toString());
        telemetry.addData("IMU Magnetic", bot.imu.getMagneticFieldStrength().toString());
        telemetry.addData("IMU Linear Acceleration", bot.imu.getLinearAcceleration());
        telemetry.addData("IMU Velocity", bot.imu.getVelocity().toString());

        telemetry.update();
    }

    @Override
    public void stop()
    {
        bot.stop();
    }
}
