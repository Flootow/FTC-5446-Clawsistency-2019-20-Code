package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.enumclaw.ftc.teamcode.CurveDrivingRelated.Pose;
import org.enumclaw.ftc.teamcode.Encoder;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.Team5446.Competition.Math5446;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.DriveBase;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.IMUService;

public class PositionCalculatorTool {

    private DriveBase driveBase = null;
    private IMUService imuService = null;
    private VectorF botPosition = new VectorF(0, 0);

    public void init(DriveBase driveBase, IMUService imuService)
    {
        this.driveBase = driveBase;
        this.imuService = imuService;
    }

    public void resetPose(VectorF position, float angle)
    {
        botPosition = position;
        this.imuService.SetAngle(angle);
    }

    public void resetPose(Pose pose)
    {
        resetPose(pose.position, pose.direction);
    }

    //region mecanumTranslation(...)
    private float oldRF;
    private float oldRB;
    private float oldLF;
    private float oldLB;

    /**
     *
     * @param rightFront
     * @param rightBack
     * @param leftFront
     * @param leftBack
     * @param wheelDiameter
     * @return
     */
    VectorF mecanumTranslation(DcMotor leftFront, DcMotor leftBack,
                               DcMotor rightFront, DcMotor rightBack, float wheelDiameter)
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
        VectorF translation = new VectorF(
                (newLF + newRF + newLB + newRB) * (wheelDiameter / 16),
                (-newLF + newRF + newLB - newRB) * (wheelDiameter / 16));
        //Correcting Math mistakes and constant errors
        translation = new VectorF(translation.get(0), 0.75f * -translation.get(1));
        return translation;
    }
    //endregion

    public void updateBotPosition()
    {
        VectorF botTranslation = Math5446.RotatePoint(
                mecanumTranslation(driveBase.rightFrontMotor, driveBase.rightBackMotor,
                driveBase.leftFrontMotor, driveBase.leftBackMotor, 4.0f),
                (float) Math.toRadians(imuService.GetAngle()));

        botPosition.add(botTranslation);
    }

    public VectorF getBotPosition()
    {
        return botPosition;
    }

    public float getBotDirection() { return imuService.GetAngle(); }

    public Pose getBotPose()
    {
        return new Pose(botPosition, imuService.GetAngle());
    }



    public VectorF botToTarget(VectorF target)
    {
        return Math5446.RotatePoint(target.subtracted(botPosition), (float) ((-Math.PI / 180.0f) * imuService.GetAngle()));
    }

    float clicksToInches(int clicks)
    {
        return (clicks / Encoder.clicksByInch(Encoder.MotorTypes.ANDYMARK40, 4.0f, 1.5f));
    }


}
