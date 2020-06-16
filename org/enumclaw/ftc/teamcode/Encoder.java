package org.enumclaw.ftc.teamcode;

/**
 * Created by Enumclaw Robotics Club on 11/26/2016
 */

public class Encoder {
    /**
     * Motor Types that we have encoder info for
     */
    public enum MotorTypes {
        NXT,
        TETRIX,
        ANDYMARK60,
        ANDYMARK40,
        ANDYMARK20,
    }

    /**
     * Encoder clicks in a single revolution of the motor in motor type order
     */
    private final static double clicksByMotorType[] = {360, 1440, 1680, 1120, 560};

    public static double circumference(double diameter) {
        return (diameter * Math.PI);
    }

    /**
     * @return Encoder clicks in a single revolution of the motor (based on manufacturer)
     */
    public static double clicksByMotorType(Encoder.MotorTypes motorType) {
        return clicksByMotorType[motorType.ordinal()];
    }

    /**
     * @return Clicks based on revolutions for motor type
     */
    public static int clicksByRevolutions(Encoder.MotorTypes motorType,
                                          double revolutions) {
        return (int)(clicksByMotorType[motorType.ordinal()] * revolutions);
    }

    /**
     * @return clicks based on encoder
     */
    public static int clicksByInch(Encoder.MotorTypes motorType,
                                   double wheelDiameter,
                                   double gearingRatio) {
        return (int)(clicksByMotorType[motorType.ordinal()] / ((wheelDiameter * Math.PI) * gearingRatio));
    }

    /**
     * clicks based on motor type, wheel and gearing for a target distance
     * @param distanceInches Distance in inches eg 112.5
     * @param motorType MotorType
     * @param wheelDiameter diameter of the wheel in inches
     * @param gearingRatio gearing ratio between the motor and the wheel
     * @return clicks based on motor type, wheel and gearing for a target distance
     */
    public static int clicksByDistance(Encoder.MotorTypes motorType,
                                       double distanceInches,
                                       double wheelDiameter,
                                       double gearingRatio) {
        return (int)(clicksByMotorType[motorType.ordinal()] *  (distanceInches / (wheelDiameter * gearingRatio * Math.PI)));
    }

    /**
     * Clicks for a motor type, wheel and gearing based on a given angle
     * @param angleDegrees angle in degrees. eg 90
     * @param motorType MotorType
     * @param wheelDiameter diameter of the wheel in inches
     * @param gearingRatio gearing ratio between the motor and the wheel
     * @return Clicks for a motor type, wheel and gearing based on a given angle
     */
    public static int clicksByAngle(Encoder.MotorTypes motorType,
                                    double angleDegrees,
                                    double wheelDiameter,
                                    double gearingRatio) {
        return (int)(clicksByMotorType[motorType.ordinal()] * (wheelDiameter * gearingRatio * Math.PI) * (angleDegrees / 360d));
    }

} // class
