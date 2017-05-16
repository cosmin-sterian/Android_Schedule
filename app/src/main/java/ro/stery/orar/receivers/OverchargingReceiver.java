package ro.stery.orar.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Handler;

import ro.stery.orar.Contract;
import ro.stery.orar.R;
import ro.stery.orar.services.OverchargingService2;

public class OverchargingReceiver extends BroadcastReceiver {

    private static final int ID = 2;
    private static final int warnID = 3;
    private boolean overcharging = false;
    private Notification.Builder mBuilder = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final String action = intent.getAction();
                if(action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    boolean charging = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) == BatteryManager.BATTERY_PLUGGED_AC;

                    if(mBuilder == null) {
                        mBuilder = new Notification.Builder(context);
                    }

                    overchargingWarn(context, level, charging);

                    /*Intent intentService = new Intent(context, OverchargingService2.class);
                    intentService.setAction(Contract.Overcharging.BATTERY_LEVEL);
                    intentService.putExtra(Contract.Overcharging.level, level);
                    intentService.putExtra(Contract.Overcharging.charging, charging);
                    context.startService(intentService);*/
                }
            }
        }

        public OverchargingReceiver() { }

    private void overchargingWarn(Context context, int level, boolean charging) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(charging && level == 100) {
            final Notification notification = new Notification.Builder(context)
                    .setContentTitle("Overcharging")
                    .setContentText("Battery is fully charged!")
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    notificationManager.notify(warnID, notification);
                    overcharging = true;
                    //TODO: Vibrate
                }
            };

            handler.postDelayed(runnable, 2 * 60 * 1000);

        }

        if(overcharging && !charging) {
            //The device was unplugged after being warned
            overcharging = false;
            notificationManager.cancel(warnID);
        }

        String notificationText = level + "% left";

        if(charging)
            notificationText += ", charging";

        Notification notification = mBuilder
                .setContentTitle("Battery level")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText(notificationText)
                .setOngoing(true)
                .build();

        notificationManager.notify(ID, notification);

    }

    }