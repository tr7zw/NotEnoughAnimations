package dev.tr7zw.notenoughanimations.config;

public class ConfigUpgrader {

    public static boolean upgradeConfig(Config config) {
        boolean changed = false;
        if(config.configVersion <= 1) {
            config.configVersion = 2;
            changed = true;
        }
        // check for more changes here
        
        return changed;
    }
    
}
