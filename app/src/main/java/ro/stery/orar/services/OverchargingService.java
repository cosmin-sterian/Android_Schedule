package ro.stery.orar.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v4.app.NotificationCompat;

import ro.stery.orar.Contract;
import ro.stery.orar.R;

public class OverchargingService extends IntentService {

    private static final int ID = 2;

    /*@Override
    public void onCreate() {
        super.onCreate();
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Battery Service")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Service running!")
                .setOngoing(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }*/

    public OverchargingService() {
        super("Overcharging Monitor Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null) {
            final String action = intent.getAction();
            if(action.equals(Contract.Overcharging.OVERCHARGING_WARN)) {
                overchargingWarn(intent.getIntExtra("level", -1));
            }
        }
    }

    private void overchargingWarn(int level) {
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Battery level")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText(level + "% left")
                .setOngoing(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID, notification);
    }

}
