package org.enumclaw.ftc.teamcode.CurveDrivingRelated;

public class StrafingParameters {
    public boolean active;
    public float endHeading;
    public float maxTurnPower;

    /**
     * Strafing becomes active and has following parameters
     * @param endHeading In degrees
     * @param maxTurnPower
     */
    public StrafingParameters(float endHeading, float maxTurnPower)
    {
        this.active = true;
        this.endHeading = endHeading;
        this.maxTurnPower = maxTurnPower;
    }

    /**
     * No Strafing (Not Active) when no parameters present
     */
    public StrafingParameters()
    {
        this.active = false;
        this.endHeading = 0;
        this.maxTurnPower = 0;
    }
}
