package org.firstinspires.ftc.teamcode.Team5446.Testing.Other;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.Team5446.Competition.Math5446;
import org.firstinspires.ftc.teamcode.Team5446.Competition.VuforiaService;

import java.util.ArrayList;
import java.util.List;

public class AnAutoMathTest extends OpMode {

    ElapsedTime runtime = new ElapsedTime();

    public enum ProgramMode
    {
        NOTHING,
        TEST_1,
        TEST_VISION;
    }
    ProgramMode programMode;

    public enum SkystonePosition
    {
        LEFT,
        MIDDLE,
        RIGHT,
        UNKNOWN;
    }
    SkystonePosition skystonePosition = SkystonePosition.UNKNOWN;

    List<VectorF> fourPoints = new ArrayList<VectorF>();
    VuforiaService vuforiaService = null;

    @Override
    public void init()
    {
        if (programMode == ProgramMode.TEST_VISION)
        {
            vuforiaService = new VuforiaService();
            vuforiaService.init(hardwareMap);
        }
    }

    @Override
    public void init_loop()
    {

    }

    @Override
    public void start()
    {
        fourPoints.add(new VectorF(9,108));
        fourPoints.add(new VectorF(25.155f, 108));
        fourPoints.add(new VectorF(22.845f, 120));
        fourPoints.add(new VectorF(48-9, 120));
    }

    @Override
    public void loop()
    {
        telemetry.addData("Status", "Running: " + runtime.toString());

        if (programMode == ProgramMode.TEST_1)
        {
            telemetry.addData("ProgramMode", "Test_1");
            float progress = Math5446.Clamp((float)(runtime.seconds() / 15.0f), 0, 1);
            VectorF outputPosition1 = Math5446.CurveDrivingTarget(fourPoints, progress);
            VectorF outputPosition2 = Math5446.CurveDrivingTarget(new VectorF(9,108),0, new VectorF(48-9,120), 0, 0.5f, progress);
            telemetry.addData("Bezier Outcome 1", outputPosition1.toString());
            telemetry.addData("Bezier Outcome 2", outputPosition2.toString());
        }

        if (programMode == ProgramMode.TEST_VISION)
        {
            vuforiaService.activate();
            vuforiaService.update();
            VectorF skystoneCords;
            if (vuforiaService.getSkystoneCordinates() != null)
            {
                skystoneCords = vuforiaService.getSkystoneCordinates();
                telemetry.addData("Skystone Visible", skystoneCords.toString());
                float SkystoneRightOffset = skystoneCords.get(1);

                if (SkystoneRightOffset < -3.0f)
                {
                    skystonePosition = SkystonePosition.LEFT;
                }
                else if (Math.abs(SkystoneRightOffset) <= 3.0f)
                {
                    skystonePosition = SkystonePosition.MIDDLE;
                }
                else if (SkystoneRightOffset > 3.0f)
                {
                    skystonePosition = SkystonePosition.RIGHT;
                }
                telemetry.addData("Skystone Position", skystonePosition.toString());
            }
            else
            {
                telemetry.addData("", "Skystone Not Visible");
            }
        }

        telemetry.update();
    }

    @Override
    public void stop()
    {

    }
}
