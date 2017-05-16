package ro.stery.orar.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Vibrator;
import android.widget.Toast;

import ro.stery.orar.R;

public class OverchargingReceiver extends BroadcastReceiver {

    public static final int ID = 2;
    public static final int warnID = 3;
    private boolean overcharging = false;
    private Notification.Builder mBuilder = null;
    private Vibrator v = null;
    private boolean vibratorShouldContinue = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                boolean charging = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) == BatteryManager.BATTERY_PLUGGED_AC;
                //| (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) == BatteryManager.BATTERY_PLUGGED_USB);

                if (mBuilder == null) {
                    mBuilder = new Notification.Builder(context);
                }

                if(v == null) {
                    v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                }

                overchargingWarn(context, level, charging);
            }
        }
    }

    public OverchargingReceiver() {
    }

    private void overchargingWarn(final Context context, int level, boolean charging) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (charging && level > 80) {
            final Notification notification = new Notification.Builder(context)
                    .setContentTitle("Overcharging")
                    .setContentText("Battery is fully charged!")
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            vibratorShouldContinue = true;
            overcharging = true;

            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10 * 1000);

                        if(vibratorShouldContinue) {
                            notificationManager.notify(warnID, notification);
                            overchargingVibrate();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }.start();


            /*Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    notificationManager.notify(warnID, notification);
                    overcharging = true;
                    //TODO: Vibrate
                    overchargingVibrate();
                }
            };

            //handler.postDelayed(runnable, 2 * 60 * 1000);
            handler.postDelayed(runnable, 5 * 1000);*/



        }

        if (overcharging && !charging) {
            //The device was unplugged
            overcharging = false;
            notificationManager.cancel(warnID);
            vibratorShouldContinue = false;
            v.cancel();
            Toast.makeText(context, "Disabled shouldContinue asd", Toast.LENGTH_SHORT).show();
        }

        String notificationText = level + "% left";

        if (charging)
            notificationText += ", charging";

        Notification notification = mBuilder
                .setContentTitle("Battery level")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText(notificationText)
                .setOngoing(true)
                .build();

        notificationManager.notify(ID, notification);

    }

    public void overchargingVibrate() {
        final long[] pattern = { 0,165,243,209,235,124,100,107,86,325,119,98,103,88,113,140,109,97,264,114,128,198,
                           3000,165,243,209,235,124,100,107,86,325,119,98,103,88,113,140,109,97,264,114,128,198,0};
        long aux = 0;
        for(long p : pattern)
            aux+=p;

        final long sum = aux;

        Thread vibrationThread = new Thread() {
            @Override
            public void run() {
                while (vibratorShouldContinue) {
                    v.vibrate(pattern, -1);
                    try {
                        Thread.sleep(10 * 1000 + sum);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        vibrationThread.start();

    }

}