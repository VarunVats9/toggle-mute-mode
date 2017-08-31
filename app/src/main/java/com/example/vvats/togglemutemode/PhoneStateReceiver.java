package com.example.vvats.togglemutemode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import static android.media.AudioManager.STREAM_RING;

/**
 * Created by vvats on 30/08/17.
 */
public class PhoneStateReceiver extends BroadcastReceiver {

    private static int TOGGLE = 1;
    private static int STREAM_MIN_VOLUME = 0;
    private static final Handler handler = new Handler();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        try {
            final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            final AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Toast.makeText(context, "Ringing State", Toast.LENGTH_SHORT).show();
                final Runnable runnableCode = new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Handlers", "Called on main thread");
                        if (TOGGLE % 2 == 0) {
                            Toast.makeText(context, "Increasing Volume to Max Value : " + audioManager.getStreamMaxVolume(STREAM_RING), Toast.LENGTH_SHORT).show();
                            audioManager.setStreamVolume(STREAM_RING, audioManager.getStreamMaxVolume(STREAM_RING), AudioManager.FLAG_PLAY_SOUND);
                        } else {
                            Toast.makeText(context, "Decreasing Volume to Min Value : " + STREAM_MIN_VOLUME, Toast.LENGTH_SHORT).show();
                            audioManager.setStreamVolume(STREAM_RING, STREAM_MIN_VOLUME, AudioManager.FLAG_PLAY_SOUND);
                        }
                        TOGGLE++;
                        // Repeat this the same runnable code block again another 5 seconds
                        // 'this' is referencing the Runnable object
                        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                            handler.postDelayed(this, 5000);
                        } else {
                            handler.removeCallbacksAndMessages(null);
                        }
                    }
                };
                // Start the initial runnable task by posting through the handler
                handler.post(runnableCode);
            }

            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                handler.removeCallbacksAndMessages(null);
                Toast.makeText(context, "Call Picked Up State", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Decreasing Volume to Min Value : " + STREAM_MIN_VOLUME, Toast.LENGTH_SHORT).show();
                audioManager.setStreamVolume(STREAM_RING, STREAM_MIN_VOLUME, AudioManager.FLAG_PLAY_SOUND);
            }

            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                handler.removeCallbacksAndMessages(null);
                Toast.makeText(context, "Idle State", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Decreasing Volume to Min Value : " + STREAM_MIN_VOLUME, Toast.LENGTH_SHORT).show();
                audioManager.setStreamVolume(STREAM_RING, STREAM_MIN_VOLUME, AudioManager.FLAG_PLAY_SOUND);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
