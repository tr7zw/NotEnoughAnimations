package dev.tr7zw.notenoughanimations.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Config {

    public int configVersion = 5;
    public float animationSmoothingSpeed = 20f;
    public Set<String> holdingItems = new HashSet<>(
            Arrays.asList("minecraft:clock", "minecraft:compass", "minecraft:torch", "minecraft:lantern", "minecraft:soul_torch", "minecraft:soul_lantern"));
    public boolean enableAnimationSmoothing = true;
    public boolean enableInWorldMapRendering = true;
    public boolean enableOffhandHiding = true;
    public boolean enableRotationLocking = true;
    public boolean enableLadderAnimation = true;
    public boolean enableRotateToLadder = true;
    public boolean enableEatDrinkAnimation = true;
    public boolean enableRowBoatAnimation = true;
    public boolean enableHorseAnimation = true;
    public boolean dontHoldItemsInBed = true;
    public boolean freezeArmsInBed = true;
    public boolean keepBodyRotatedWithHead = false;
    public boolean showLastUsedSword = true;
    public Set<String> sheathSwords = new HashSet<>(Arrays.asList("minecraft:wooden_sword", "minecraft:stone_sword",
            "minecraft:golden_sword", "minecraft:iron_sword", "minecraft:diamond_sword", "minecraft:netherite_sword"));
    public boolean enableCrawlingAnimation = true;

}
