package org.firstinspires.ftc.teamcode.Team5446.Competition;

import android.renderscript.ScriptGroup;
import org.firstinspires.ftc.teamcode.Team5446.Competition.InputTiming;

import java.util.HashMap;

public class InputTimingManager
{
    HashMap<String, InputTiming> inputToTimings = new HashMap();

    public InputTiming getTiming(String inputName) {
        if (!inputToTimings.containsKey(inputName))
        {
            inputToTimings.put(inputName, new InputTiming());
        }

        return inputToTimings.get(inputName);
    }
}
