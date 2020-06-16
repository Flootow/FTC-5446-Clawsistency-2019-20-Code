package org.firstinspires.ftc.teamcode.Team5446.Competition;

import org.enumclaw.ftc.teamcode.CurveDrivingRelated.Pose;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public final class Math5446
{
    //region LogicHelp (boolTo01, sign, Clamp)
    /**
     * @return 1 for true, 0 for false
     */
    public static int boolTo01(boolean bool)
    {
        return bool ? 1 : 0;
    }

    /**
     * @return 1 for positive, -1 for negative, 0 for zero
     */
    public static int sign(float number)
    {
        if (number > 0)
            return 1;
        else if (number < 0)
            return -1;
        else
            return 0;
    }

    public static float Clamp(float number, float min, float max)
    {
        float output = number;
        if (output < min)
        {
            output = min;
        }
        if (output > max)
        {
            output = max;
        }
        return output;
    }
    //endregion

    //region Lerp
    /**
     * Lerp (Linear Interpolation)
     * @param a Point a (x,y), start (t=0)
     * @param b Point b (x,y), end (t=1)
     * @param t Time, [0,1]
     * @return Point between (a) and (b) at (t) progress
     */
    public static VectorF Lerp2D(VectorF a, VectorF b, float t)
    {
        return new VectorF(Lerp(a.get(0), b.get(0), t), Lerp(a.get(1), b.get(1), t));
    }

    /**
     * Lerp (Linear Interpolation)
     * @param a starting value (t=0)
     * @param b ending value (t=1)
     * @param t Time, [0,1]
     * @return Value between (a) and (b) at (t) progress
     */
    public static float Lerp(float a, float b, float t)
    {
        return a + (t * (b - a));
    }
    //endregion Lerp

    //region Bezier Curve
    /**
     * Bezier Curve (Curve path for robot movement)
     * @param fourPoints Four points of a bezier curve
     * @param progress From 0 to 1
     * @return A point on the bezier curve
     */
    public static VectorF CurveDrivingTarget(List<VectorF> fourPoints, float progress)
    {
        List<VectorF> p = fourPoints;
        VectorF a1 = Math5446.Lerp2D(p.get(0), p.get(1), progress);
        VectorF a2 = Math5446.Lerp2D(p.get(1), p.get(2), progress);
        VectorF a3 = Math5446.Lerp2D(p.get(2), p.get(3), progress);

        VectorF b1 = Math5446.Lerp2D(a1, a2, progress);
        VectorF b2 = Math5446.Lerp2D(a2, a3, progress);

        VectorF c1 = Math5446.Lerp2D(b1, b2, progress);
        return c1;
    }

    public static VectorF CurveDrivingTarget(VectorF start, float startTangent, VectorF end, float endTangent, float curveStrength, float progress)
    {
        float projectedPointDistance = curveStrength * Distance(start, end);
        List<VectorF> fourpoints = new ArrayList<VectorF>();
        fourpoints.add(start);
        fourpoints.add(new VectorF((float) (start.get(0) + projectedPointDistance * Math.cos(Math.toRadians(startTangent))), (float) (start.get(1) + projectedPointDistance * Math.sin(Math.toRadians(startTangent)))));
        fourpoints.add(new VectorF((float) (end.get(0) - projectedPointDistance * Math.cos(Math.toRadians(endTangent))), (float) (end.get(1) - projectedPointDistance * Math.sin(Math.toRadians(endTangent)))));
        fourpoints.add(end);
        return CurveDrivingTarget(fourpoints, progress);
    }

    public static VectorF CurveDrivingTarget(Pose start, Pose end, float curveStrength, float progress)
    {
        return CurveDrivingTarget(start.position, start.direction, end.position, end.direction, curveStrength, progress);
    }
    //endregion

    //region Common Formulas/Calculations (Distance, RotatePoint)
    public static float Distance(VectorF a, VectorF b)
    {
        return (float) Math.sqrt(Math.pow(b.get(0) - a.get(0), 2) + Math.pow(b.get(1) - a.get(1), 2));
    }

    public static VectorF RotatePoint(VectorF point, float radians)
    {
        double pointInPolarRadians = Math.atan2(point.get(1),point.get(0));
        double pointInPolarDistance = point.magnitude();

        pointInPolarRadians += radians;
        return new VectorF((float) (Math.cos(pointInPolarRadians) * pointInPolarDistance), (float) (Math.sin(pointInPolarRadians) * pointInPolarDistance));
    }
    //endregion

}
