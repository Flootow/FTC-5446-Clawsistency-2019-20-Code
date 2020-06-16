package org.firstinspires.ftc.teamcode.Team5446.Examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

//@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Sample1Auto", group = "Team 5446")
//@Disabled
public class Sample1Auto extends LinearOpMode
{
    Sample1Bot bot = null;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode()
    {
        bot = new Sample1Bot();
        bot.init(hardwareMap);

        waitForStart();

        //Do Auto
        waitInSeconds(1.5f);

        bot.stop();
    }

    //You can make your own functions :) , they are useful when you need to do something repeatedly and for organization
    void waitInSeconds(float seconds)
    {
        timer.reset();
        while (opModeIsActive() && timer.milliseconds() < (1000 * seconds)) {
            //Put opMNodeIsActive() in every loop so the program can be stopped by the driver phone if needed
            // wait
        }
    }
}
