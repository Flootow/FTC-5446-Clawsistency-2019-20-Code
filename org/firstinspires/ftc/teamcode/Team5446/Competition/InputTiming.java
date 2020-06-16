package org.firstinspires.ftc.teamcode.Team5446.Competition;

public class InputTiming
{

    int duration;
    int sign;

    public void UpdateInput(float value)
    {

        if (Math5446.sign(value) == sign)
        {
            duration++;
        }
        else
        {
            sign = Math5446.sign(value);
            duration = 1;
        }
    }

    public int SignDuration(int sign)
    {
        if (sign == this.sign)
        {
            return duration;
        }
        else
        {
            return 0;
        }
    }

    public int Duration()
    {
        return duration;
    }

    public int Sign()
    {
        return sign;
    }

}
