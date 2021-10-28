package dev.tr7zw.notenoughanimations;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.tr7zw.notenoughanimations.config.Config;
import dev.tr7zw.notenoughanimations.config.ConfigUpgrader;
import dev.tr7zw.notenoughanimations.logic.ArmTransformer;
import dev.tr7zw.notenoughanimations.logic.HeldItemHandler;
import dev.tr7zw.notenoughanimations.logic.RotationFixer;

public abstract class NEAnimationsLoader {

    public static final Logger LOGGER = LogManager.getLogger("NotEnoughAnimations");
    public static NEAnimationsLoader INSTANCE;
    public static Config config;
    private final File settingsFile = new File("config", "notenoughanimations.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public ArmTransformer armTransformer;
    public RotationFixer rotationFixer;
    public HeldItemHandler heldItemHandler;

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
        enable();
    }

    private void enable() {
        rotationFixer = new RotationFixer();
        armTransformer = new ArmTransformer();
        heldItemHandler = new HeldItemHandler();
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
