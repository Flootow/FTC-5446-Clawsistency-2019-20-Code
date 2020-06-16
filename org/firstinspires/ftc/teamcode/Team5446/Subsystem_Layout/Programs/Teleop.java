package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Programs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.enumclaw.ftc.teamcode.CurveDrivingRelated.Pose;
import org.enumclaw.ftc.teamcode.CurveDrivingRelated.StrafingParameters;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.Team5446.Competition.Math5446;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Bot;
import org.enumclaw.ftc.teamcode.InputTimingManager;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.StoneManipulator;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools.CurveDrivingTool;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools.PositionCalculatorTool;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "'New Bot' Teleop", group = "Team 5446")
public class Teleop extends OpMode {

    Bot bot = null;
    private ElapsedTime runtime = new ElapsedTime();
    InputTimingManager inputTimingManager = new InputTimingManager();

    private int armPreset = 0;
    private boolean armPresetChanged = false;

    int targetLiftPosition = 0;
    private float stoneRotationPreset = StoneManipulator.Preset.LOWEST.value;
    private float stoneRotationOffset = 0f;
    private float driveSlowDown = 1.0f;

    private float markerPosition = 0.5f;

    private DriveMode driveMode = DriveMode.OPERATED;
    private enum DriveMode
    {
        OPERATED,
        BUILD_ASSISTED;
    }
    Pose buildingPose = new Pose(new VectorF(0,0), 0);
    Pose startToBuildingPose = new Pose(new VectorF(0,0), 0);

    @Override
    public void init()
    {
        telemetry.addData("Status", "Initializing");
        telemetry.update();
        bot = new Bot();
        bot.init(hardwareMap);
        bot.positionCalculatorTool = new PositionCalculatorTool();
        bot.positionCalculatorTool.init(bot.driveBase, bot.imuService);
        bot.curveDrivingTool = new CurveDrivingTool();
        bot.curveDrivingTool.init(bot.driveBase, bot.positionCalculatorTool);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void init_loop()
    {

    }

    @Override
    public void start()
    {

    }

    @Override
    public void loop()
    {
        telemetry.addData("Status", "Running: " + runtime.toString());

        //region -DriveBase (Moving: Gamepad1 Sticks, Slowdown: Left Bumper/Trigger)-
        bot.positionCalculatorTool.updateBotPosition();

        //Driving

        if (driveMode == DriveMode.OPERATED)
        {
            bot.driveBase.mecanumDrive.run(-gamepad1.left_stick_y * driveSlowDown, gamepad1.right_stick_x * driveSlowDown,
                    gamepad1.left_stick_x * driveSlowDown, false);
        }
        else if (driveMode == DriveMode.BUILD_ASSISTED)
        {
            bot.curveDrivingTool.curveDrive(
                    startToBuildingPose,
                    buildingPose,
                    0.5f, 0.6f, false,
                    new StrafingParameters(buildingPose.direction, 0.40f));
        }

        //Slow down control
        driveSlowDown += 0.05f * (gamepad1.left_trigger - Math5446.boolTo01(gamepad1.left_bumper));
        driveSlowDown = Math5446.Clamp(driveSlowDown, 0.1f, 1);
        telemetry.addData("Drive Slowdown", driveSlowDown);

        if (gamepad1.y)
        {
            buildingPose = bot.positionCalculatorTool.getBotPose();
        }
        if (gamepad1.b)
        {
            startToBuildingPose = bot.positionCalculatorTool.getBotPose();
            driveMode = DriveMode.BUILD_ASSISTED;
            bot.curveDrivingTool.resetCurveDrive();
        }
        //Any driving effort detection
        inputTimingManager.getTiming("Drive_Detection").UpdateInput((Math.abs(gamepad1.left_stick_y) >= 0.2f || Math.abs(gamepad1.left_stick_x) >= 0.2f) ? 1f : 0f);
        if (inputTimingManager.getTiming("Drive_Detection").SignDuration(1) >= 1)
        {
            driveMode = DriveMode.OPERATED;
        }

        //Drive Fast Sound
        inputTimingManager.getTiming("Drive_Fast").UpdateInput((Math.abs(gamepad1.left_stick_y) >= 0.7f) ? 1f : 0f);
        if (inputTimingManager.getTiming("Drive_Fast").SignDuration(1) == 10)
        {
            bot.soundService.PlaySkystoneSound(4, 1.0f, 0);
        }

        //Robot Position
        telemetry.addData("Position", bot.positionCalculatorTool.getBotPosition().toString());
        //endregion

        //region -Marker Manipulator (Gamepad2 Right Bumper/Trigger)-
        markerPosition += 0.05f * (Math5446.boolTo01(gamepad2.right_bumper) - gamepad2.right_trigger);
        markerPosition = Math5446.Clamp(markerPosition, 0f, 1f);
        bot.markerManipulator.servo.setPosition(markerPosition);
        telemetry.addData("Marker Position", markerPosition);
        //endregion

        //region -Lift (Gamepad2 Left Bumper/Trigger)-
        targetLiftPosition += bot.lift.manualLiftClickSpeed * (Math5446.boolTo01(gamepad2.left_bumper) - gamepad2.left_trigger);
        bot.lift.goToTarget(targetLiftPosition);

        //BB8 sounds for lift going up and down
        inputTimingManager.getTiming("Lift_Input").UpdateInput(Math5446.boolTo01(gamepad2.left_bumper) - gamepad2.left_trigger);
        if (inputTimingManager.getTiming("Lift_Input").SignDuration(1) == 5)
        {
            bot.soundService.PlaySkystoneSound(1, 1.0f, 0);
        }
        else if (inputTimingManager.getTiming("Lift_Input").SignDuration(-1) == 5)
        {
            bot.soundService.PlaySkystoneSound(2, 1.0f, 0);
        }

        telemetry.addData("Lift Target", targetLiftPosition);
        telemetry.addData("LeftLift", bot.lift.leftLift.getCurrentPosition());
        telemetry.addData("RightLift", bot.lift.rightLift.getCurrentPosition());
        //endregion

        //region -Intake (Gampad1 right trigger/bumper)-
        bot.stoneIntake.setPower(gamepad1.right_trigger - Math5446.boolTo01(gamepad1.right_bumper));
        //endregion

        //region -Plate Grabber (Gamepad1 X)-
        //Toggle plate grabber with gamepad1.x
        inputTimingManager.getTiming("Plate_Grabber_Input").UpdateInput(Math5446.boolTo01(gamepad1.x));
        if (inputTimingManager.getTiming("Plate_Grabber_Input").SignDuration(1) == 1)
        {
            bot.plateGrabber.togglePosition();
            bot.soundService.PlaySkystoneSound(14, 1.0f, 0);
        }
        //endregion

        //region -Stone Grabber (Gamepad2 X)-
        //Toggle stone grabber with gamepad2.x
        inputTimingManager.getTiming("Stone_Grabber_Input").UpdateInput(Math5446.boolTo01(gamepad2.x));
        if (inputTimingManager.getTiming("Stone_Grabber_Input").SignDuration(1) == 1)
        {
            bot.stoneManipulator.toggleGrabberActivity();
        }
        //endregion

        //region -Stone Rotation (Gamepad 2 Dpad & RightStickY)-
        //Adjust rotation manually
        armPresetChanged = false;
        stoneRotationOffset += (1.5f/60f) * gamepad2.right_stick_y;
        stoneRotationOffset = Math5446.Clamp(stoneRotationOffset, -1, 1);

        //Change armPreset number
        inputTimingManager.getTiming("Dpad_Vertical").UpdateInput(Math5446.boolTo01(gamepad2.dpad_up) - Math5446.boolTo01(gamepad2.dpad_down));
        if (inputTimingManager.getTiming("Dpad_Vertical").SignDuration(1) == 1)
        {
            armPreset++;
            armPresetChanged = true;
        }
        else if(inputTimingManager.getTiming("Dpad_Vertical").SignDuration(-1) == 1)
        {
            armPreset--;
            armPresetChanged = true;
        }

        if (gamepad2.dpad_left)
        {
            armPreset = 4;
            armPresetChanged = true;
        }
        else if (gamepad2.dpad_right)
        {
            armPreset = 1;
            armPresetChanged = true;
        }

        armPreset = (int) Math5446.Clamp(armPreset, 1, 6);

        if (armPresetChanged)
        {
            stoneRotationOffset = 0f;
        }
        //Close the grabber some time after the preset is changed
        inputTimingManager.getTiming("armPreset_Change").UpdateInput(Math5446.boolTo01(armPresetChanged));
        if (inputTimingManager.getTiming("armPreset_Change").SignDuration(0) == 5)
        {
            bot.stoneManipulator.setGrabberActivity(true);
        }

        //armPreset --> rotation Value
        switch (armPreset)
        {
            case 1: stoneRotationPreset = StoneManipulator.Preset.LOWEST.value;
                break;
            case 2: stoneRotationPreset = StoneManipulator.Preset.ALLOW_INTAKE.value;
                break;
            case 3: stoneRotationPreset = StoneManipulator.Preset.LEVEL3.value;
                break;
            case 4: stoneRotationPreset = StoneManipulator.Preset.LEVEL2.value;
                break;
            case 5: stoneRotationPreset = StoneManipulator.Preset.LEVEL1.value;
                break;
            case 6: stoneRotationPreset = StoneManipulator.Preset.GROUND.value;
                break;
        }

        //Swing arm to position
        bot.stoneManipulator.setRotation(stoneRotationPreset + stoneRotationOffset);
        telemetry.addData("Stone Rotation", stoneRotationPreset + stoneRotationOffset);

        //Lightsaber sound when arm swings from intake to placement side
        inputTimingManager.getTiming("Arm_Rotation_Side").UpdateInput(stoneRotationPreset > StoneManipulator.Preset.ALLOW_INTAKE.value ? 1f : 0f);
        if (inputTimingManager.getTiming("Arm_Rotation_Side").SignDuration(1) == 1)
        {
            bot.soundService.PlaySkystoneSound(8, 1.0f, 0);
        }
        //endregion

        telemetry.update();
    }

    @Override
    public void stop()
    {
        bot.stop();
    }
}
