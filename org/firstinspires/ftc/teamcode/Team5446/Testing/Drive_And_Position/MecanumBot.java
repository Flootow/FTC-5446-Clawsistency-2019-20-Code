package org.firstinspires.ftc.teamcode.Team5446.Testing.Drive_And_Position;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.enumclaw.ftc.teamcode.MecanumDrive;

public class MecanumBot
{
    public DcMotor leftFrontMotor = null;
    public DcMotor leftBackMotor = null;
    public DcMotor rightFrontMotor = null;
    public DcMotor rightBackMotor = null;
    public MecanumDrive mecanumDrive = null;

    public BNO055IMU imu;


    public void init(HardwareMap hardwareMap)
    {
//        leftFrontMotor  = hardwareMap.dcMotor.get("LeftFront");
//        leftBackMotor   = hardwareMap.dcMotor.get("LeftBack");
//        rightFrontMotor = hardwareMap.dcMotor.get("RightFront");
//        rightBackMotor  = hardwareMap.dcMotor.get("RightBack");
//
//        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
//        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
//
//        mecanumDrive = new MecanumDrive(leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor);
//        mecanumDrive.setMode();

        //region -IMU-
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        parameters.calibrationDataFile = "5446IMUCalibration.json";

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        //endregion
    }

    public void stop()
    {
        //mecanumDrive.stop();
    }
}
