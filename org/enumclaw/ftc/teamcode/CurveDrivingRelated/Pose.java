package org.enumclaw.ftc.teamcode.CurveDrivingRelated;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

public class Pose {
    public VectorF position;
    public float direction;

    public Pose(VectorF position, float direction)
    {
        this.position = position;
        this.direction = direction;
    }
}
