package ro.stery.orar.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import ro.stery.orar.Contract;
import ro.stery.orar.R;

public class OverchargingService extends IntentService {

    private static final int ID = 2;
    private static final int warnID = 3;
    private boolean overcharging = false;

    public OverchargingService() {
        super("Overcharging Monitor Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null) {
            final String action = intent.getAction();
            if(action.equals(Contract.Overcharging.BATTERY_LEVEL)) {
                overchargingWarn(intent.getIntExtra(Contract.Overcharging.level, -1),
                                intent.getBooleanExtra(Contract.Overcharging.charging, false));
            }
        }
    }

    private void overchargingWarn(int level, boolean charging) {
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(charging && level == 100) {
            final Notification notification = new Notification.Builder(this)
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

        } else {

            if(overcharging) {
                overcharging = false;
                notificationManager.cancel(warnID);
            }

            String notificationText = level + "% left";

            if(charging)
                notificationText += ", charging";

            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Battery level")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentText(notificationText)
                    .setOngoing(true)
                    .build();

            notificationManager.notify(ID, notification);

        }
    }

}
