package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools;

import org.enumclaw.ftc.teamcode.CurveDrivingRelated.Pose;
import org.enumclaw.ftc.teamcode.CurveDrivingRelated.StrafingParameters;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.Team5446.Competition.Math5446;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.DriveBase;
import org.enumclaw.ftc.teamcode.CurveDrivingRelated.CurveDriveListEntry;

public class CurveDrivingTool {

    private DriveBase driveBase = null;
    private PositionCalculatorTool positionCalculatorTool = null;

    private float curveDriveProgress = 0f;
    private boolean theStart = false;
    private boolean curveDriveDone = false;

    public void init(DriveBase driveBase, PositionCalculatorTool positionCalculatorTool)
    {
        this.driveBase = driveBase;
        this.positionCalculatorTool = positionCalculatorTool;
    }

    public void resetCurveDrive()
    {
        curveDriveProgress = 0f;
        curveDriveDone = false;
    }

    public float getCurveDriveProgress() { return curveDriveProgress; }
    public boolean isCurveDriveDone() { return curveDriveDone; }

    public void curveDrive(Pose startWaypoint, Pose endWaypoint, float curveStrength, float power, boolean curveChained, StrafingParameters strafingParameters)
    {
        curveDrive(new CurveDriveListEntry(startWaypoint, endWaypoint, curveStrength, power, curveChained, strafingParameters));
    }

    public void curveDrive(CurveDriveListEntry curveDriveListEntry)
    {
        CurveDriveListEntry cd = curveDriveListEntry;
        float progressIncrease = 0.08f;
        float curveDrivingTolerance = 7.0f;
        float endTolerance = 3.0f;

        if (cd.curveChained)
        {
            endTolerance = curveDrivingTolerance;
        }

        //Computations about where the bot should travel to in it's short term goal
        VectorF target = Math5446.CurveDrivingTarget(cd.startWaypoint, cd.endWaypoint, cd.curveStrength, curveDriveProgress);
        VectorF botToTarget = Math5446.RotatePoint(target.subtracted(positionCalculatorTool.getBotPose().position), (float) (-Math.toRadians(positionCalculatorTool.getBotPose().direction)));
        float botToTargetDistance = botToTarget.magnitude();
        botToTarget = new VectorF(botToTarget.get(0), botToTarget.get(1), 0);
        botToTarget.normalized3D();

        //Extend target on curve if too close
        if (botToTargetDistance < curveDrivingTolerance) {
            curveDriveProgress += progressIncrease;
            curveDriveProgress = Math5446.Clamp(curveDriveProgress, 0, 1);

            boolean atEndOfPath = (curveDriveProgress == 1 && botToTargetDistance < endTolerance);
            if (cd.strafingParameters.active)
            {
                if (atEndOfPath && Math.abs(positionCalculatorTool.getBotDirection() - cd.strafingParameters.endHeading) < 10)
                {
                    curveDriveDone = true;
                }
            }
            else
            {
                if (atEndOfPath) {
                    curveDriveDone = true;
                }
            }

        }

        VectorF driveVector = new VectorF(botToTarget.get(0), botToTarget.get(1));

        //Slow down when approaching end of curve
        float speedReduction = Math5446.Clamp((float)(Math.pow(botToTargetDistance / curveDrivingTolerance,2)), 0, 1);

        //Only used if strafing parameters active, turnSpeed used for heading correction
        float turnSpeed = (float) Math5446.Clamp(0.06f * (float)(positionCalculatorTool.getBotPose().direction - cd.strafingParameters.endHeading), -cd.strafingParameters.maxTurnPower, cd.strafingParameters.maxTurnPower);

        //Reduce Forward Speed the sharper we have to turn (Currently with 0 coefficent, partially due to possibility of incorrect calculations)
        float slowForTurning = (float) (Math.abs(0.0f * Math.atan2(botToTarget.get(1), botToTarget.get(0))));


        if (cd.strafingParameters.active)
        {
            driveVector = new VectorF(Math.abs(cd.power) * driveVector.get(0), Math.abs(cd.power) * driveVector.get(1));
            //driveBase.mecanumDrive.run(driveVector.get(0), turnSpeed, -driveVector.get(1), speedReduction, true);
            driveBase.mecanumDrive.run(driveVector.get(0) * speedReduction, turnSpeed, -driveVector.get(1) * speedReduction, false);
        }
        else
        {
            //Correct Forward Power if driving backwards
            if (cd.power > 0)
            {
                driveVector = new VectorF(Math5446.Clamp(driveVector.get(0) - slowForTurning, 0, 1), driveVector.get(1));
            }
            else if (cd.power < 0)
            {
                driveVector = new VectorF(Math5446.Clamp(driveVector.get(0) + slowForTurning, -1, 0), driveVector.get(1));
            }
            driveVector = new VectorF(Math.abs(cd.power) * driveVector.get(0), -cd.power * driveVector.get(1));
            //driveBase.mecanumDrive.run(Math.abs(cd.power) * driveVector.get(0), -cd.power * driveVector.get(1), 0, speedReduction, true);
            driveBase.mecanumDrive.run(Math.abs(cd.power) * driveVector.get(0) * speedReduction, 0.5f * driveVector.get(1) * speedReduction, 0, false);
        }
    }


}
