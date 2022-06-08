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

import dev.tr7zw.config.CustomConfigScreen;
import dev.tr7zw.notenoughanimations.config.Config;
import dev.tr7zw.notenoughanimations.config.ConfigUpgrader;
import dev.tr7zw.notenoughanimations.logic.AnimationProvider;
import dev.tr7zw.notenoughanimations.logic.HeldItemHandler;
import dev.tr7zw.notenoughanimations.logic.PlayerTransformer;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;

public abstract class NEAnimationsLoader {

    public static final Logger LOGGER = LogManager.getLogger("NotEnoughAnimations");
    public static NEAnimationsLoader INSTANCE;
    public static Config config;
    private final File settingsFile = new File("config", "notenoughanimations.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public PlayerTransformer playerTransformer;
    public HeldItemHandler heldItemHandler;
    public AnimationProvider animationProvider;

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
        playerTransformer = new PlayerTransformer();
        heldItemHandler = new HeldItemHandler();
        animationProvider = new AnimationProvider();
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
                List<OptionInstance<?>> options = new ArrayList<>();
                options.add(getOnOffOption("text.nea.enable.animationsmoothing", () -> config.enableAnimationSmoothing,
                        (b) -> config.enableAnimationSmoothing = b));
                options.add(getOnOffOption("text.nea.disableLegSmoothing", () -> config.disableLegSmoothing,
                        (b) -> config.disableLegSmoothing = b));
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
                options.add(getOnOffOption("text.nea.enable.rotatetoladder", () -> config.enableRotateToLadder,
                        (b) -> config.enableRotateToLadder = b));
                options.add(getDoubleOption("text.nea.ladderAnimationAmplifier", 0.1f, 0.5f, 0.01f,
                        () -> (double) config.ladderAnimationAmplifier, (i) -> {
                            config.ladderAnimationAmplifier = i.floatValue();
                        }));
                options.add(getDoubleOption("text.nea.ladderAnimationArmHeight", 1f, 3f, 0.1f,
                        () -> (double) config.ladderAnimationArmHeight, (i) -> {
                            config.ladderAnimationArmHeight = i.floatValue();
                        }));
                options.add(getDoubleOption("text.nea.ladderAnimationArmSpeed", 1f, 4f, 0.1f,
                        () -> (double) config.ladderAnimationArmSpeed, (i) -> {
                            config.ladderAnimationArmSpeed = i.floatValue();
                        }));
                options.add(getOnOffOption("text.nea.enable.crawling", () -> config.enableCrawlingAnimation,
                        (b) -> config.enableCrawlingAnimation = b));
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
                options.add(getEnumOption("text.nea.rotationlock", RotationLock.class, () -> config.rotationLock, (b) -> config.rotationLock = b));
                options.add(getOnOffOption("text.nea.enable.showlastusedsword", () -> config.showLastUsedSword,
                        (b) -> config.showLastUsedSword = b));
                options.add(getOnOffOption("text.nea.enable.holdupallitems", () -> config.holdUpAllItems,
                        (b) -> config.holdUpAllItems = b));
                options.add(getOnOffOption("text.nea.enable.itemSwapAnimation", () -> config.itemSwapAnimation,
                        (b) -> config.itemSwapAnimation = b));
                options.add(getOnOffOption("text.nea.enable.tweakElytraAnimation", () -> config.tweakElytraAnimation,
                        (b) -> config.tweakElytraAnimation = b));
                options.add(getOnOffOption("text.nea.enable.petAnimation", () -> config.petAnimation,
                        (b) -> config.petAnimation = b));
                options.add(getOnOffOption("text.nea.enable.fallingAnimation", () -> config.fallingAnimation,
                        (b) -> config.fallingAnimation = b));
                options.add(getOnOffOption("text.nea.enable.freezingAnimation", () -> config.freezingAnimation,
                        (b) -> config.freezingAnimation = b));
                options.add(getOnOffOption("text.nea.enable.huggingAnimation", () -> config.huggingAnimation,
                        (b) -> config.huggingAnimation = b));
                options.add(getOnOffOption("text.nea.enable.narutoRunning", () -> config.narutoRunning,
                        (b) -> config.narutoRunning = b));
                options.add(getOnOffOption("text.nea.enable.enableInWorldBookRendering", () -> config.enableInWorldBookRendering,
                        (b) -> config.enableInWorldBookRendering = b));
                

                getOptions().addSmall(options.toArray(new OptionInstance[0]));

            }

            @Override
            public void save() {
                writeConfig();
                animationProvider.refreshEnabledAnimations();
            }

            @Override
            public void reset() {
                config = new Config();
                writeConfig();
            }

        };

        return screen;
    }


}
