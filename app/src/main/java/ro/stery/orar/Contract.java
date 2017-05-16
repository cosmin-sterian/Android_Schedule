package ro.stery.orar;

/**
 * Created by Stery on 14/05/2017.
 */

public interface Contract {

    interface Overcharging {
        String BATTERY_LEVEL = "ro.stery.battery_level";
        String level = "level";
        String charging = "charging";

        long[] pattern = { 0,165,243,209,235,124,100,107,86,325,119,98,103,88,113,140,109,97,264,114,128,198,
                3000,165,243,209,235,124,100,107,86,325,119,98,103,88,113,140,109,97,264,114,128,198,0};
    }

}
