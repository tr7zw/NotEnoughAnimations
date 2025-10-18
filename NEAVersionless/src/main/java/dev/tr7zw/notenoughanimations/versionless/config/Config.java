package dev.tr7zw.notenoughanimations.versionless.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import dev.tr7zw.notenoughanimations.versionless.RotationLock;
import dev.tr7zw.notenoughanimations.versionless.animations.BowAnimation;
import dev.tr7zw.notenoughanimations.versionless.animations.HoldUpModes;
import dev.tr7zw.notenoughanimations.versionless.animations.HoldUpTarget;

public class Config {

    public int configVersion = 10;
    public float animationSmoothingSpeed = 0.1f;
    public Set<String> holdingItems = new HashSet<>(Arrays.asList("minecraft:clock", "minecraft:compass",
            "minecraft:torch", "minecraft:lantern", "minecraft:soul_torch", "minecraft:soul_lantern",
            "minecraft:recovery_compass", "quad-mstv-mtv:acacia_torch", "quad-mstv-mtv:bamboo_torch",
            "quad-mstv-mtv:birch_torch", "quad-mstv-mtv:cherry_torch", "quad-mstv-mtv:crimson_torch",
            "quad-mstv-mtv:dark_oak_torch", "quad-mstv-mtv:pale_oak_torch", "quad-mstv-mtv:jungle_torch",
            "quad-mstv-mtv:mangrove_torch", "quad-mstv-mtv:spruce_torch", "quad-mstv-mtv:warped_torch",
            "quad-mstv-mtv:acacia_soul_torch", "quad-mstv-mtv:bamboo_soul_torch", "quad-mstv-mtv:birch_soul_torch",
            "quad-mstv-mtv:cherry_soul_torch", "quad-mstv-mtv:crimson_soul_torch", "quad-mstv-mtv:dark_oak_soul_torch",
            "quad-mstv-mtv:pale_oak_soul_torch", "quad-mstv-mtv:jungle_soul_torch", "quad-mstv-mtv:mangrove_soul_torch",
            "quad-mstv-mtv:spruce_soul_torch", "quad-mstv-mtv:warped_soul_torch"));
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
    public boolean enableHorseLegAnimation = false;
    public boolean dontHoldItemsInBed = true;
    public boolean freezeArmsInBed = true;
    public RotationLock rotationLock = RotationLock.NONE;
    public boolean limitRotationLockToFP = true;
    public boolean showLastUsedSword = false;
    public Set<String> sheathSwords = new HashSet<>(Arrays.asList("minecraft:wooden_sword", "minecraft:stone_sword",
            "minecraft:golden_sword", "minecraft:iron_sword", "minecraft:diamond_sword", "minecraft:netherite_sword"));
    public boolean enableCrawlingAnimation = true;
    public HoldUpModes holdUpItemsMode = HoldUpModes.CONFIG;
    public HoldUpTarget holdUpTarget = HoldUpTarget.CAMERA;
    public float holdUpCameraOffset = 0.1f;
    public boolean holdUpOnlySelf = false;
    public float holdUpItemOffset = 0;
    public boolean itemSwapAnimation = true;
    public boolean tweakElytraAnimation = true;
    public boolean petAnimation = true;
    public boolean fallingAnimation = false;
    public boolean freezingAnimation = true;
    public boolean huggingAnimation = false;
    public boolean narutoRunning = false;
    public boolean disableLegSmoothing = false;
    public BowAnimation bowAnimation = BowAnimation.VANILLA;
    public boolean customBowRotationLock = false;
    public boolean clampCrossbowAnimations = false;
    public boolean burningAnimation = true;
    public Set<String> hideItemsForTheseBows = new HashSet<>();
}
