package org.enumclaw.ftc.teamcode.CurveDrivingRelated;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

public class CurveDriveListEntry {
    public Pose startWaypoint;
    public Pose endWaypoint;

    public float curveStrength;
    public float power;
    public boolean forwardDrive;
    public boolean curveChained;
    public StrafingParameters strafingParameters;

    public CurveDriveListEntry(Pose startWaypoint, Pose endWaypoint, float curveStrength, float power, boolean curveChained, StrafingParameters strafingParameters)
    {
        this.startWaypoint = startWaypoint;
        this.endWaypoint = endWaypoint;
        this.curveStrength = curveStrength;
        this.power = power;
        this.forwardDrive = forwardDrive;
        this.curveChained = curveChained;
        this.strafingParameters = strafingParameters;
    }
}
