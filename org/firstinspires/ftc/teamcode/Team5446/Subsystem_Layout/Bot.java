package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout;

import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.DriveBase;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.IMUService;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.Lift;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.MarkerManipulator;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.PlateGrabber;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.SoundService;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.StoneManipulator;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems.StoneIntake;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools.VuforiaServiceTool;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools.CurveDrivingTool;
import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Tools.PositionCalculatorTool;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Bot {
    public DriveBase driveBase = null;
    public Lift lift = null;
    public PlateGrabber plateGrabber = null;
    public StoneManipulator stoneManipulator = null;
    public StoneIntake stoneIntake = null;
    public IMUService imuService = null;
    public SoundService soundService = null;
    public MarkerManipulator markerManipulator = null;

    //Tools, need to be initialized themselves in each program
    public PositionCalculatorTool positionCalculatorTool = null;
    public CurveDrivingTool curveDrivingTool = null;
    public VuforiaServiceTool vuforiaServiceTool = null;

    public void init(HardwareMap hardwareMap)
    {
        driveBase = new DriveBase();
        lift = new Lift();
        plateGrabber = new PlateGrabber();
        stoneManipulator = new StoneManipulator();
        stoneIntake = new StoneIntake();
        imuService = new IMUService();
        soundService = new SoundService();
        markerManipulator = new MarkerManipulator();

        driveBase.init(hardwareMap);
        lift.init(hardwareMap);
        plateGrabber.init(hardwareMap);
        stoneManipulator.init(hardwareMap);
        stoneIntake.init(hardwareMap);
        imuService.init(hardwareMap);
        soundService.init(hardwareMap);
        markerManipulator.init(hardwareMap);
    }

    public void stop()
    {
        driveBase.stop();
        lift.stop();
        plateGrabber.stop(); //Not Needed
        stoneManipulator.stop(); //Not Needed
        stoneIntake.stop();
        imuService.stop(); //Not Needed
        soundService.stop(); //Not Needed
        markerManipulator.stop(); //Not Needed
    }


}
