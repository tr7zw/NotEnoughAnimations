package dev.tr7zw.notenoughanimations.config;

public class ConfigUpgrader {

    public static boolean upgradeConfig(Config config) {
        boolean changed = false;
        if (config.configVersion <= 1) {
            config.configVersion = 2;
            changed = true;
        }
        if (config.configVersion <= 2) {
            config.configVersion = 3;
            changed = true;
        }
        if (config.configVersion <= 3) {
            config.configVersion = 4;
            config.dontHoldItemsInBed = true;
            config.freezeArmsInBed = true;
            changed = true;
        }
        if (config.configVersion <= 4) {
            config.configVersion = 5;
            config.holdingItems.add("minecraft:soul_torch");
            config.holdingItems.add("minecraft:soul_lantern");
            changed = true;
        }
        if (config.configVersion <= 5) {
            config.configVersion = 6;
            changed = true;
        }
        if (config.configVersion <= 6) {
            config.configVersion = 7;
            changed = true;
        }
        if (config.configVersion <= 7) {
            config.configVersion = 8;
            config.holdingItems.add("minecraft:recovery_compass");
            changed = true;
        }
        if (config.configVersion <= 8) {
            config.configVersion = 9;
            config.showLastUsedSword = false; // turning this off by default
        }
        // check for more changes here

        return changed;
    }

}
