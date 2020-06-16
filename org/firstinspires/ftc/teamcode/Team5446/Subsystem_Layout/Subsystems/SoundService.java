package org.firstinspires.ftc.teamcode.Team5446.Subsystem_Layout.Subsystems;

import android.content.Context;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SoundService {
    // List of available sound resources
    String  sounds[] =  {"ss_alarm", "ss_bb8_down", "ss_bb8_up", "ss_darth_vader", "ss_fly_by",
            "ss_mf_fail", "ss_laser", "ss_laser_burst", "ss_light_saber", "ss_light_saber_long", "ss_light_saber_short",
            "ss_light_speed", "ss_mine", "ss_power_up", "ss_r2d2_up", "ss_roger_roger", "ss_siren", "ss_wookie" };
    boolean soundPlaying = false;
    int soundID;
    Context myApp;
    SoundPlayer.PlaySoundParams params;

    public void init(HardwareMap hardwareMap)
    {
        myApp = hardwareMap.appContext;
        params = new SoundPlayer.PlaySoundParams();
    }

    public void PlaySkystoneSound(int soundIndex, float rate, int loop)
    {
        params.loopControl = loop;
        params.rate = rate;
        params.waitForNonLoopingSoundsToFinish = true;

        if (soundPlaying)
        {
            SoundPlayer.getInstance().stopPlayingAll();
        }

        // Determine Resource IDs for the sounds you want to play, and make sure it's valid.
        if (!soundPlaying) {
            if ((soundID = myApp.getResources().getIdentifier(sounds[soundIndex], "raw", myApp.getPackageName())) != 0) {

                // Signal that the sound is now playing.
                soundPlaying = true;

                // Start playing, and also Create a callback that will clear the playing flag when the sound is complete.
                SoundPlayer.getInstance().startPlaying(myApp, soundID, params, null,
                        new Runnable() {
                            public void run() {
                                soundPlaying = false;
                            }
                        });
            }
        }
    }

    public void stop()
    {

    }
}

