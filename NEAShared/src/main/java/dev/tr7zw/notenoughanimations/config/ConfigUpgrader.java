package dev.tr7zw.notenoughanimations.config;

public class ConfigUpgrader {

    public static boolean upgradeConfig(Config config) {
        boolean changed = false;
        if(config.configVersion <= 1) {
            config.configVersion = 2;
            changed = true;
        }
        if(config.configVersion <= 2) {
            config.configVersion = 3;
            changed = true;
        }
        if(config.configVersion <= 3) {
            config.configVersion = 4;
            config.dontHoldItemsInBed = true;
            config.freezeArmsInBed = true;
            changed = true;
        }
        if(config.configVersion <= 4) {
            config.configVersion = 5;
            config.holdingItems.add("minecraft:soul_torch");
            config.holdingItems.add("minecraft:soul_lantern");
            changed = true;
        }
        // check for more changes here
        
        return changed;
    }
    
}
