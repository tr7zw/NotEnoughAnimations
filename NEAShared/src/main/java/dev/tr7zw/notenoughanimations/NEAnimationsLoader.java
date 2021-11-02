package dev.tr7zw.notenoughanimations;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.notenoughanimations.config.Config;
import dev.tr7zw.notenoughanimations.config.ConfigUpgrader;
import dev.tr7zw.notenoughanimations.config.CustomConfigScreen;
import dev.tr7zw.notenoughanimations.logic.ArmTransformer;
import dev.tr7zw.notenoughanimations.logic.HeldItemHandler;
import dev.tr7zw.notenoughanimations.logic.RotationFixer;
import net.minecraft.client.Option;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

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
    
    public Screen createConfigScreen(Screen parent) {
        CustomConfigScreen screen = new CustomConfigScreen(parent, "text.nea.title") {

            @Override
            public void initialize() {
                List<Option> options = new ArrayList<>();
                options.add(getOnOffOption("text.nea.enable.animationsmoothing", () -> config.enableAnimationSmoothing,
                        (b) -> config.enableAnimationSmoothing = b));
                options.add(getDoubleOption("text.nea.smoothingSpeed", 10f, 30f, 0.1f,
                        () -> (double) config.animationSmoothingSpeed, (i) -> {
                            config.animationSmoothingSpeed = i.floatValue();
                        }));
                options.add(getOnOffOption("text.nea.enable.inworldmaprendering", () -> config.enableInWorldMapRendering,
                        (b) -> config.enableInWorldMapRendering = b));
                options.add(getOnOffOption("text.nea.enable.offhandhiding", () -> config.enableOffhandHiding,
                        (b) -> config.enableOffhandHiding = b));
                options.add(getOnOffOption("text.nea.enable.rotationlocking", () -> config.enableRotationLocking,
                        (b) -> config.enableRotationLocking = b));
                options.add(getOnOffOption("text.nea.enable.ladderanimation", () -> config.enableLadderAnimation,
                        (b) -> config.enableLadderAnimation = b));
                options.add(getOnOffOption("text.nea.enable.eatdringanimation", () -> config.enableEatDrinkAnimation,
                        (b) -> config.enableEatDrinkAnimation = b));
                options.add(getOnOffOption("text.nea.enable.rowboatanimation", () -> config.enableRowBoatAnimation,
                        (b) -> config.enableRowBoatAnimation = b));
                options.add(getOnOffOption("text.nea.enable.horseanimation", () -> config.enableHorseAnimation,
                        (b) -> config.enableHorseAnimation = b));
                options.add(getOnOffOption("text.nea.enable.dontholditemsinbed", () -> config.dontHoldItemsInBed,
                        (b) -> config.dontHoldItemsInBed = b));
                options.add(getOnOffOption("text.nea.enable.freezearmsinbed", () -> config.freezeArmsInBed,
                        (b) -> config.freezeArmsInBed = b));
                options.add(getOnOffOption("text.nea.enable.lockbodyheadrotation", () -> config.keepBodyRotatedWithHead,
                        (b) -> config.keepBodyRotatedWithHead = b));
                options.add(getOnOffOption("text.nea.enable.showlastusedsword", () -> config.showLastUsedSword,
                        (b) -> config.showLastUsedSword = b));
                options.add(getOnOffOption("text.nea.enable.showlastusedtool", () -> config.showLastUsedToolsOnBack,
                        (b) -> config.showLastUsedToolsOnBack = b));
                

                getOptions().addSmall(options.toArray(new Option[0]));

            }

            @Override
            public void save() {
                writeConfig();
            }

        };

        return screen;
    }


}
