package dev.tr7zw.notenoughanimations.config;

import java.util.ArrayList;
import java.util.List;

import dev.tr7zw.config.CustomConfigScreen;
import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.RotationLock;
import dev.tr7zw.notenoughanimations.versionless.animations.BowAnimation;
import dev.tr7zw.notenoughanimations.versionless.animations.HoldUpModes;
import dev.tr7zw.notenoughanimations.versionless.config.Config;
import net.minecraft.client.gui.screens.Screen;
//spotless:off 
//#if MC >= 11900
import net.minecraft.client.OptionInstance;
//#else
//$$ import net.minecraft.client.Option;
//#endif
//spotless:on

public class ConfigScreenProvider {

    public static Screen createConfigScreen(Screen parent) {
        return new CustomConfigScreen(parent, "text.nea.title") {

            @Override
            public void initialize() {
                List<Object> options = new ArrayList<>();
                Config config = NEABaseMod.config;
                options.add(getOnOffOption("text.nea.enable.animationsmoothing", () -> config.enableAnimationSmoothing,
                        (b) -> config.enableAnimationSmoothing = b));
                options.add(getOnOffOption("text.nea.disableLegSmoothing", () -> config.disableLegSmoothing,
                        (b) -> config.disableLegSmoothing = b));
                options.add(getDoubleOption("text.nea.smoothingSpeed", 10f, 30f, 0.1f,
                        () -> (double) config.animationSmoothingSpeed, (i) -> {
                            config.animationSmoothingSpeed = i.floatValue();
                        }));
                options.add(getOnOffOption("text.nea.enable.inworldmaprendering",
                        () -> config.enableInWorldMapRendering, (b) -> config.enableInWorldMapRendering = b));
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
                options.add(getEnumOption("text.nea.rotationlock", RotationLock.class, () -> config.rotationLock,
                        (b) -> config.rotationLock = b));
                options.add(getOnOffOption("text.nea.enable.showlastusedsword", () -> config.showLastUsedSword,
                        (b) -> config.showLastUsedSword = b));
                options.add(getEnumOption("text.nea.holdUpItemsMode", HoldUpModes.class, () -> config.holdUpItemsMode,
                        (b) -> config.holdUpItemsMode = b));
                options.add(getDoubleOption("text.nea.holdUpItemOffset", -0.5f, 0.4f, 0.1f,
                        () -> (double) config.holdUpItemOffset, (i) -> {
                            config.holdUpItemOffset = i.floatValue();
                        }));
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
                options.add(getOnOffOption("text.nea.enable.enableInWorldBookRendering",
                        () -> config.enableInWorldBookRendering, (b) -> config.enableInWorldBookRendering = b));
                options.add(getEnumOption("text.nea.enable.bowAnimation", BowAnimation.class, () -> config.bowAnimation,
                        (b) -> config.bowAnimation = b));

                // spotless:off
                //#if MC >= 11900
                getOptions().addSmall(options.toArray(new OptionInstance[0]));
                //#else
                //$$getOptions().addSmall(options.toArray(new Option[0]));
                //#endif
                // spotless:on

            }

            @Override
            public void save() {
                NEAnimationsLoader.INSTANCE.writeConfig();
                NEAnimationsLoader.INSTANCE.animationProvider.refreshEnabledAnimations();
            }

            @Override
            public void reset() {
                NEABaseMod.config = new Config();
                NEAnimationsLoader.INSTANCE.writeConfig();
            }

        };
    }

}
