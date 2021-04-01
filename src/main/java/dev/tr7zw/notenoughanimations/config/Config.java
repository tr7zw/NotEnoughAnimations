package dev.tr7zw.notenoughanimations.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Config {

    public int configVersion = 1;
    public float animationSmoothingSpeed = 20f;
    public Set<String> holdingItems = new HashSet<>(Arrays.asList("minecraft:clock", "minecraft:compass", "minecraft:torch", "minecraft:lantern"));
    public boolean enableAnimationSmoothing = true;
    public boolean enableInWorldMapRendering = true;
    public boolean enableOffhandHiding = true;
    public boolean enableRotationLocking = true;
    
}
