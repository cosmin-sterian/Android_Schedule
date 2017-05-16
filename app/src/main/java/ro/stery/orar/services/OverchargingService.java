package ro.stery.orar.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import ro.stery.orar.Contract;
import ro.stery.orar.R;
import ro.stery.orar.receivers.OverchargingReceiver;

public class OverchargingService extends Service {

    private final BroadcastReceiver mReceiver = new OverchargingReceiver();
    private final int closedID = 5;

    public OverchargingService() {
    }

    @Override
    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter() {{
            addAction(Intent.ACTION_BATTERY_CHANGED);
            addAction(Intent.ACTION_POWER_CONNECTED);
            addAction(Intent.ACTION_POWER_DISCONNECTED);
        }};
        registerReceiver(mReceiver, intentFilter);

        clearNotifications((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE), closedID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        OverchargingReceiver.overcharging = false;
        OverchargingReceiver.vibratorShouldContinue = false;
        if(OverchargingReceiver.v != null)
            OverchargingReceiver.v.cancel();
        if(OverchargingReceiver.delayThread != null && OverchargingReceiver.delayThread.isAlive())
            OverchargingReceiver.delayThread.interrupt();
        if(OverchargingReceiver.vibrationThread != null && OverchargingReceiver.vibrationThread.isAlive())
            OverchargingReceiver.delayThread.interrupt();

        unregisterReceiver(mReceiver);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("OverchargingService")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Service stopped")
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(closedID, notification);

        clearNotifications(notificationManager, OverchargingReceiver.ID, OverchargingReceiver.warnID);
    }

    public void clearNotifications(NotificationManager notificationManager, int ... IDs) {
        for(int ID : IDs) {
            notificationManager.cancel(ID);
        }
    }
}
