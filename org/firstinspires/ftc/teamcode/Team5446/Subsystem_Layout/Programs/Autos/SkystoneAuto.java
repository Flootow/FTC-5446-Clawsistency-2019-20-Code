//package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Programs.Autos;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//import org.enumclaw.ftc.teamcode.CurveDrivingRelated.Pose;
//import org.enumclaw.ftc.teamcode.CurveDrivingRelated.StrafingParameters;
//import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
//import org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Bot;
//
//public class SkystoneAuto extends OpMode {
//    Bot bot = null;
//    String autoStage = null;
//    boolean startingStage = true;
//    boolean blueSide;
//
//    @Override
//    public void init()
//    {
//        bot.init(hardwareMap);
//        bot.positionCalculatorTool.init(bot.driveBase, bot.imuService);
//        bot.curveDrivingTool.init(bot.driveBase, bot.positionCalculatorTool);
//
//        autoStage = "Find Skystone Step 1";
//        bot.positionCalculatorTool.resetPose(new VectorF(0,0), 0);
//    }
//
//    private void nextStage(String stage)
//    {
//        autoStage = stage;
//        startingStage = true;
//    }
//
//    private void startingStageDone() { startingStage = false; }
//
//    @Override
//    public void init_loop()
//    {
//
//    }
//
//    @Override
//    public void start()
//    {
//
//    }
//
//    @Override
//    public void loop()
//    {
//        if (autoStage == "Find Skystone Step 1")
//        {
//            if (startingStage)
//            {
//                startingStageDone();
//            }
//
//            if (blueSide)
//            {
//                bot.curveDrivingTool.curveDrive(
//                        bot.positionCalculatorTool.getBotPose(),
//                        new Pose(new VectorF(24, 0), 0),
//                        0.5f, 0.6f, false,
//                        new StrafingParameters());
//            }
//            else
//            {
//                bot.curveDrivingTool.curveDrive(
//                        bot.positionCalculatorTool.getBotPose(),
//                        new Pose(new VectorF(144-24, 0), 180-0),
//                        0.5f, 0.6f, false,
//                        new StrafingParameters());
//            }
//
//
//            if (bot.curveDrivingTool.isCurveDriveDone())
//            {
//                nextStage("Step2");
//            }
//        }
//
//        else if (autoStage == "Find Skystone Step 2")
//        {
//            if (startingStage)
//            {
//                //Code
//                startingStageDone();
//            }
//
//
//
//            if (bot.curveDrivingTool.isCurveDriveDone())
//            {
//                stop();
//            }
//        }
//
//        telemetry.addData("autoStage", autoStage);
//        telemetry.update();
//
//    }
//
//    @Override
//    public void stop()
//    {
//        bot.stop();
//    }
//}
