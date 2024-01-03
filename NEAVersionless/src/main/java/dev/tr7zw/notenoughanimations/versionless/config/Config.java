package dev.tr7zw.notenoughanimations.versionless.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import dev.tr7zw.notenoughanimations.versionless.RotationLock;
import dev.tr7zw.notenoughanimations.versionless.animations.BowAnimation;
import dev.tr7zw.notenoughanimations.versionless.animations.HoldUpModes;

public class Config {

    public int configVersion = 10;
    public float animationSmoothingSpeed = 0.5f;
    public Set<String> holdingItems = new HashSet<>(
            Arrays.asList("minecraft:clock", "minecraft:compass", "minecraft:torch", "minecraft:lantern",
                    "minecraft:soul_torch", "minecraft:soul_lantern", "minecraft:recovery_compass"));
    public boolean enableAnimationSmoothing = true;
    public boolean enableInWorldMapRendering = true;
    public boolean enableOffhandHiding = true;
    public boolean enableRotationLocking = true;
    public boolean enableLadderAnimation = true;
    public float ladderAnimationAmplifier = 0.35f;
    public float ladderAnimationArmHeight = 1.7f;
    public float ladderAnimationArmSpeed = 2f;
    public boolean enableRotateToLadder = true;
    public boolean enableEatDrinkAnimation = true;
    public boolean enableRowBoatAnimation = true;
    public boolean enableHorseAnimation = true;
    public boolean dontHoldItemsInBed = true;
    public boolean freezeArmsInBed = true;
    public RotationLock rotationLock = RotationLock.NONE;
    public boolean showLastUsedSword = false;
    public Set<String> sheathSwords = new HashSet<>(Arrays.asList("minecraft:wooden_sword", "minecraft:stone_sword",
            "minecraft:golden_sword", "minecraft:iron_sword", "minecraft:diamond_sword", "minecraft:netherite_sword"));
    public boolean enableCrawlingAnimation = true;
    public HoldUpModes holdUpItemsMode = HoldUpModes.CONFIG;
    public float holdUpItemOffset = 0;
    public boolean itemSwapAnimation = true;
    public boolean tweakElytraAnimation = true;
    public boolean petAnimation = true;
    public boolean fallingAnimation = false;
    public boolean freezingAnimation = true;
    public boolean huggingAnimation = false;
    public boolean narutoRunning = false;
    public boolean enableInWorldBookRendering = false;
    public boolean disableLegSmoothing = false;
    public BowAnimation bowAnimation = BowAnimation.VANILLA;

}
