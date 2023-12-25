package dev.tr7zw.notenoughanimations.versionless;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.tr7zw.notenoughanimations.versionless.animations.BowAnimation;
import dev.tr7zw.notenoughanimations.versionless.config.Config;

public class NEABaseMod {

    public static final Logger LOGGER = LogManager.getLogger("NotEnoughAnimations");
    public static Config config;
    protected final File settingsFile = new File("config", "notenoughanimations.json");
    protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public NEABaseMod() {
        super();
    }

    protected void setupConfig() {
        try {
            Class clientClass = Class.forName("dev.tr7zw.firstperson.FirstPersonModelCore");
            // Firstperson is installed, change some default settings to fit
            config.bowAnimation = BowAnimation.CUSTOM_V1;
        } catch (Throwable ex) {
            // ignored
        }
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