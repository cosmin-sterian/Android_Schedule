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

    @Override
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
    }

    public class OverchargingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final String action = intent.getAction();
                if(action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    float result = level/(float)scale;

                    Intent intentService = new Intent(context, OverchargingService.class);
                    intentService.setAction(Contract.Overcharging.OVERCHARGING_WARN);
                    intentService.putExtra("level", result);
                    startService(intentService);

                    //overchargingWarn(result);

                    Notification notification = new Notification.Builder(context)
                            .setContentTitle("Receiver")
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentText("Broadcast Receiver running")
                            .setOngoing(true)
                            .build();
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(10, notification);
                }
            }
        }

        public OverchargingReceiver() { }

    }

    public OverchargingService() {
        super("Overcharging Monitor Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null) {
            final String action = intent.getAction();
            if(action.equals(Contract.Overcharging.OVERCHARGING_WARN)) {
                overchargingWarn(intent.getFloatExtra("level", 0));
            }
        }
    }

    private void overchargingWarn(float level) {
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
