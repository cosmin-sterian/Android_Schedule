package ro.stery.orar.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import ro.stery.orar.Contract;
import ro.stery.orar.R;
import ro.stery.orar.services.OverchargingService;

public class OverchargingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final String action = intent.getAction();
                if(action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    int result = level / scale;

                    Intent intentService = new Intent(context, OverchargingService.class);
                    intentService.setAction(Contract.Overcharging.OVERCHARGING_WARN);
                    intentService.putExtra("level", result);
                    context.startService(intentService);

                    //overchargingWarn(result);

                    /*Notification notification = new Notification.Builder(context)
                            .setContentTitle("Receiver")
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentText("Broadcast Receiver running")
                            .setOngoing(true)
                            .build();
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(10, notification);*/
                }
            }
        }

        public OverchargingReceiver() { }

    }