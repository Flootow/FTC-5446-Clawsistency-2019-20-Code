package org.enumclaw.ftc.teamcode;

import org.enumclaw.ftc.teamcode.InputTiming;

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
