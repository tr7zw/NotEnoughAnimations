package dev.tr7zw.notenoughanimations;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import dev.tr7zw.notenoughanimations.config.ConfigScreenProvider;
import dev.tr7zw.notenoughanimations.logic.AnimationProvider;
import dev.tr7zw.notenoughanimations.logic.HeldItemHandler;
import dev.tr7zw.notenoughanimations.logic.PlayerTransformer;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.config.Config;
import dev.tr7zw.notenoughanimations.versionless.config.ConfigUpgrader;
import dev.tr7zw.transition.loader.ModLoaderEventUtil;
import dev.tr7zw.transition.loader.ModLoaderUtil;

public abstract class NEAnimationsLoader extends NEABaseMod {

    public static NEAnimationsLoader INSTANCE;
    public PlayerTransformer playerTransformer;
    public HeldItemHandler heldItemHandler;
    public AnimationProvider animationProvider;
    private boolean lateInitCompleted = false;

    protected NEAnimationsLoader() {
        INSTANCE = this;
        ModLoaderUtil.disableDisplayTest();
        ModLoaderUtil.registerConfigScreen(ConfigScreenProvider::createConfigScreen);
        ModLoaderEventUtil.registerClientSetupListener(this::onEnable);
    }

    public void onEnable() {
        if (settingsFile.exists()) {
            try {
                config = gson.fromJson(new String(Files.readAllBytes(settingsFile.toPath()), StandardCharsets.UTF_8),
                        Config.class);
            } catch (Exception ex) {
                System.out.println("Error while loading config! Creating a new one!");
                ex.printStackTrace();
            }
        }
        if (config == null) {
            config = new Config();
            setupConfig();
            writeConfig();
        } else {
            if (ConfigUpgrader.upgradeConfig(config)) {
                writeConfig();
            }
        }
        enable();
    }

    private void enable() {
        playerTransformer = new PlayerTransformer();
        heldItemHandler = new HeldItemHandler();
        animationProvider = new AnimationProvider();
    }

    private void lateInit() {
        animationProvider.refreshEnabledAnimations(); // refresh once after the game is loaded, so all other mods are
                                                      // done initializing
    }

    public void clientTick() {
        // run this code later, so all other mods are done loading
        if (!lateInitCompleted) {
            lateInitCompleted = true;
            lateInit();
        }
        playerTransformer.nextTick();
    }

}
