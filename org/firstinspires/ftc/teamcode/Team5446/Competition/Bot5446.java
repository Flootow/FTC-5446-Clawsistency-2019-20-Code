package org.firstinspires.ftc.teamcode.Team5446.Competition;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

//region -IMU Related Imports-
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
//endregion

import org.enumclaw.ftc.teamcode.MecanumDrive;

public class Bot5446
{
    public DcMotor leftFrontMotor = null;
    public DcMotor leftBackMotor = null;
    public DcMotor rightFrontMotor = null;
    public DcMotor rightBackMotor = null;
    public MecanumDrive mecanumDrive = null;

    public static float leftToRightWheelDistance = 15.0f; //In inches

    public BNO055IMU imu;
    Orientation lastAngles = new Orientation();
    double globalAngle = 0.0;


    public DcMotor leftIntake = null;
    public DcMotor rightIntake = null;
//    public CRServo leftIntake = null;
//    public CRServo grabServo = null;
    public DcMotor leftLift = null;
    public DcMotor rightLift = null;
    public Servo grabber = null;
    public Servo leftStoneRotator = null;
    public Servo rightStoneRotator = null;
    //public Servo levelStoneRotator = null;

    public Servo leftPlate = null;
    public Servo rightPlate = null;



    public void init(HardwareMap hardwareMap)
    {
        leftFrontMotor  = hardwareMap.dcMotor.get("LeftFront");
        leftBackMotor   = hardwareMap.dcMotor.get("LeftBack");
        rightFrontMotor = hardwareMap.dcMotor.get("RightFront");
        rightBackMotor  = hardwareMap.dcMotor.get("RightBack");
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        rightBackMotor.setDirection(DcMotor.Direction.FORWARD);

        PrepareMotors();

        mecanumDrive = new MecanumDrive(leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor);

        leftIntake = hardwareMap.dcMotor.get("LeftIntake");
        rightIntake = hardwareMap.dcMotor.get("RightIntake");
        leftIntake.setDirection(DcMotor.Direction.FORWARD);
        rightIntake.setDirection(DcMotor.Direction.REVERSE);

        //region -IMU-
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        //endregion

        leftLift = hardwareMap.dcMotor.get("LeftLift");
        rightLift = hardwareMap.dcMotor.get("RightLift");

        leftStoneRotator = hardwareMap.servo.get("LeftStone");
        rightStoneRotator = hardwareMap.servo.get("RightStone");
        //levelStoneRotator = hardwareMap.servo.get("LevelStone");
        leftStoneRotator.setDirection(Servo.Direction.REVERSE);
        rightStoneRotator.setDirection(Servo.Direction.FORWARD);
        //levelStoneRotator.setDirection(Servo.Direction.FORWARD);

        grabber = hardwareMap.servo.get("Grabber");
        grabber.setDirection(Servo.Direction.REVERSE);

        leftPlate = hardwareMap.servo.get("LeftPlate");
        rightPlate = hardwareMap.servo.get("RightPlate");
        leftPlate.setDirection(Servo.Direction.FORWARD);
        rightPlate.setDirection(Servo.Direction.REVERSE);

        //region Lift Setup
        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftLift.setDirection(DcMotorSimple.Direction.FORWARD);
        rightLift.setDirection(DcMotorSimple.Direction.REVERSE);
        //endregion

    }

    void PrepareMotors()
    {
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public double GetAngle()
    {
        //Positive values are counter-clockwise
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    public void SetAngle(float angle)
    {
        globalAngle = angle;
    }


    public void stop()
    {
        mecanumDrive.stop();

        leftLift.setPower(0.0f);
        rightLift.setPower(0.0f);

        leftIntake.setPower(0);
        rightIntake.setPower(0);
    }

}