package org.enumclaw.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MecanumDrive5446 {
    protected DcMotor leftFrontMotor;
    protected DcMotor leftBackMotor;
    protected DcMotor rightFrontMotor;
    protected DcMotor rightBackMotor;
    protected List<DcMotor> motorList = new ArrayList<DcMotor>();

    public MecanumDrive5446(DcMotor leftFrontMotor,
                            DcMotor leftBackMotor,
                            DcMotor rightFrontMotor,
                            DcMotor rightBackMotor) {
        this.leftFrontMotor = leftFrontMotor;
        this.leftBackMotor = leftBackMotor;
        this.rightFrontMotor = rightFrontMotor;
        this.rightBackMotor = rightBackMotor;

        if (leftFrontMotor != null && leftBackMotor != null && rightFrontMotor != null && leftBackMotor != null)
        {
            motorList.add(leftFrontMotor);
            motorList.add(leftBackMotor);
            motorList.add(rightFrontMotor);
            motorList.add(rightBackMotor);
        }
    }

    public void setMode(DcMotor.RunMode mode) {
        for (DcMotor motor : motorList) {
            motor.setMode(mode);
        }
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        for (DcMotor motor : motorList) {
            motor.setZeroPowerBehavior(behavior);
        }
    }

    public boolean isMode(DcMotor.RunMode mode) {
        boolean output = true;
        //If any of the motors do not match the mode, return false
        for (DcMotor motor : motorList) {
            if (motor.getMode() != mode) {
                output = false;
                break;
            }
        }
        return output;
    }

    public void run(double forward, double turn, double strafe, float powerCoefficient, boolean directionSafe) {

        if (isMode(DcMotor.RunMode.RUN_TO_POSITION)) {
            throw new IllegalStateException("MecanumDrive.run(): motor cannot be in RUN_TO_POSITION mode. Forgot setMode()?");
        }

        double lf = powerCoefficient * (forward + turn + strafe);
        double lb = powerCoefficient * (forward + turn - strafe);
        double rf = powerCoefficient * (forward - turn - strafe);
        double rb = powerCoefficient * (forward - turn + strafe);

        double maxPower = Math.max(Math.max(Math.max(Math.abs(lf), Math.abs(lb)), Math.abs(rf)), Math.abs(rb));
        if (directionSafe && maxPower > 1d) {
            lf /= maxPower;
            lb /= maxPower;
            rf /= maxPower;
            rb /= maxPower;
        }

        leftFrontMotor.setPower(lf);
        leftBackMotor.setPower(lb);
        rightFrontMotor.setPower(rf);
        rightBackMotor.setPower(rb);
    }

    public void stop() {
        for (DcMotor motor : motorList) {
            motor.setPower(0);
        }
    }
}
