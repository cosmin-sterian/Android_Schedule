package ro.stery.orar;

/**
 * Created by Stery on 14/05/2017.
 */

public interface Contract {

    interface Overcharging {
        //String OVERCHARGING_WARN = "ro.stery.overcharging_warn";
        String BATTERY_LEVEL = "ro.stery.battery_level";
        String level = "level";
        String charging = "charging";
    }

}
