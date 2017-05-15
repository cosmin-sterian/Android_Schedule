package ro.stery.orar.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import ro.stery.orar.Contract;
import ro.stery.orar.services.OverchargingService;

public class OverchargingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final String action = intent.getAction();
                if(action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    boolean charging = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) == BatteryManager.BATTERY_PLUGGED_AC;

                    Intent intentService = new Intent(context, OverchargingService.class);
                    intentService.setAction(Contract.Overcharging.BATTERY_LEVEL);
                    intentService.putExtra(Contract.Overcharging.level, level);
                    intentService.putExtra(Contract.Overcharging.charging, charging);
                    context.startService(intentService);
                }
            }
        }

        public OverchargingReceiver() { }

    }