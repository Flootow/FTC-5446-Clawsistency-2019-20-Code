package org.enumclaw.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MecanumDrive {
    protected DcMotor leftFrontMotor;
    protected DcMotor leftBackMotor;
    protected DcMotor rightFrontMotor;
    protected DcMotor rightBackMotor;

    public MecanumDrive(DcMotor leftFrontMotor,
                        DcMotor leftBackMotor,
                        DcMotor rightFrontMotor,
                        DcMotor rightBackMotor) {
        this.leftFrontMotor = leftFrontMotor;
        this.leftBackMotor = leftBackMotor;
        this.rightFrontMotor = rightFrontMotor;
        this.rightBackMotor = rightBackMotor;
    }

    public void setMode() {
        if (leftFrontMotor.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if (leftBackMotor.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if (rightFrontMotor.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if (rightBackMotor.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public boolean isMode() {
        return ((leftFrontMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION)
                && (leftFrontMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION)
                && (rightFrontMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION)
                && (rightBackMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION));
    }

    /**
     * Runs the motors
     * @param forward the left joystick y move value where 1,0 is full forward. call with -gamepad1.left_stick_y
     * @param turn the right joystick x move value where 1,0 is full left. call with gamepad1.right_stick_x
     * @param strafe the left joystick x move value where 1,0 is full left. call with gamepad1.left_stick_x
     */
    public void run(double forward, double turn, double strafe, boolean isSlowed) {

        // check that we are set up correctly to run the motor
        if (!isMode()) {
            throw new IllegalStateException("MecanumDrive.run(): motor cannot be in RUN_TO_POSITION mode. Forgot setMode()?");
        }

        // reduce speed?
        double driveSpeed = (isSlowed ? .5f : 1f);

        // eg: Run wheels in mecanum mode
        leftFrontMotor.setPower(driveSpeed *(forward + turn + strafe));
        leftBackMotor.setPower(driveSpeed * (forward + turn - strafe));
        rightFrontMotor.setPower(driveSpeed * (forward - turn - strafe));
        rightBackMotor.setPower(driveSpeed * (forward - turn + strafe));
    }

    public void stop() {
        leftFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightFrontMotor.setPower(0);
        rightBackMotor.setPower(0);
    }
}
