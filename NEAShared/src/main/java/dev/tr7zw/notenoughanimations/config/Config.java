package dev.tr7zw.notenoughanimations.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Config {

    public int configVersion = 4;
    public float animationSmoothingSpeed = 20f;
    public Set<String> holdingItems = new HashSet<>(
            Arrays.asList("minecraft:clock", "minecraft:compass", "minecraft:torch", "minecraft:lantern"));
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
    public boolean showLastUsedToolsOnBack = false;
    public Set<String> backTools = new HashSet<>(Arrays.asList("minecraft:wooden_axe", "minecraft:stone_axe",
            "minecraft:golden_axe", "minecraft:iron_axe", "minecraft:diamond_axe", "minecraft:netherite_axe",
            "minecraft:wooden_pickaxe", "minecraft:stone_pickaxe", "minecraft:golden_pickaxe", "minecraft:iron_pickaxe",
            "minecraft:diamond_pickaxe", "minecraft:netherite_pickaxe", "minecraft:wooden_shovel",
            "minecraft:stone_shovel", "minecraft:golden_shovel", "minecraft:iron_shovel", "minecraft:diamond_shovel",
            "minecraft:netherite_shovel", "minecraft:wooden_hoe",
            "minecraft:stone_hoe", "minecraft:golden_hoe", "minecraft:iron_hoe", "minecraft:diamond_hoe",
            "minecraft:netherite_hoe"));
    public boolean enableCrawlingAnimation = true;

}
