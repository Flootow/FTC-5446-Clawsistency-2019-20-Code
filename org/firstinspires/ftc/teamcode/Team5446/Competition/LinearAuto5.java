package org.firstinspires.ftc.teamcode.Team5446.Competition;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto Plate Grabber - 5446", group = "Team 5446")
@Disabled
public class LinearAuto5 extends LinearAutonomous5446
{
    @Override
    public void runOpMode()
    {
        programMode = ProgramMode.BUILD_PLATE;
        super.runOpMode();
    }
}
