package ro.stery.orar.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Vibrator;

import ro.stery.orar.Contract;
import ro.stery.orar.R;

public class OverchargingReceiver extends BroadcastReceiver {

    public static final int ID = 2;
    public static final int warnID = 3;
    public static boolean overcharging = false;
    private Notification.Builder mBuilder = null;
    public static Vibrator v = null;
    public static boolean vibratorShouldContinue = false;
    private final int vibrationWaitTime = 2 * 1000;
    private final int timeBeforeWarning = 10 * 1000;
    public static Thread delayThread = null;
    public static Thread vibrationThread = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                boolean charging = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) == BatteryManager.BATTERY_PLUGGED_AC;

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

            delayThread = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(timeBeforeWarning);

                        if(vibratorShouldContinue) {
                            notificationManager.notify(warnID, notification);
                            overchargingVibrate();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            };
            delayThread.start();

        }

        if (overcharging && !charging) {
            //The device was unplugged
            overcharging = false;
            notificationManager.cancel(warnID);
            vibratorShouldContinue = false;
            v.cancel();
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
        long aux = 0;
        for(long p : Contract.Overcharging.pattern)
            aux+=p;

        final long sum = aux;

        vibrationThread = new Thread() {
            @Override
            public void run() {
                while (vibratorShouldContinue) {
                    v.vibrate(Contract.Overcharging.pattern, -1);
                    try {
                        Thread.sleep(vibrationWaitTime + sum);
                        v.cancel();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        vibrationThread.start();

    }

}