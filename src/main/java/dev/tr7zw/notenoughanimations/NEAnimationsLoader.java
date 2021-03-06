package dev.tr7zw.notenoughanimations;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.tr7zw.notenoughanimations.config.Config;
import dev.tr7zw.notenoughanimations.config.ConfigUpgrader;
import dev.tr7zw.notenoughanimations.logic.ArmTransformer;
import dev.tr7zw.notenoughanimations.logic.HeldItemHandler;
import dev.tr7zw.notenoughanimations.logic.RotationFixer;
import dev.tr7zw.transliterationlib.api.TRansliterationLib;
import dev.tr7zw.transliterationlib.api.event.APIEvent;

public abstract class NEAnimationsLoader {

    public static NEAnimationsLoader INSTANCE;
    public static Config config;
    private final File settingsFile = new File("config", "notenoughanimations.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void onEnable() {
        INSTANCE = this;
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
            writeConfig();
        } else {
            if(ConfigUpgrader.upgradeConfig(config)) {
                writeConfig();
            }
        }
        if (TRansliterationLib.transliteration != null) {
            enable();
        } else {
            APIEvent.LOADED.register(() -> {
                enable();
            });
        }
    }

    private void enable() {
        new RotationFixer().enable();
        new ArmTransformer().enable();
        new HeldItemHandler().enable();
    }

    public void writeConfig() {
        if (settingsFile.exists())
            settingsFile.delete();
        try {
            Files.write(settingsFile.toPath(), gson.toJson(config).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
