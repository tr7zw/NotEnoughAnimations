package dev.tr7zw.notenoughanimations.logic;

import java.util.HashSet;
import java.util.Set;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.animations.BodyPart;
import dev.tr7zw.notenoughanimations.animations.fullbody.ClimbingAnimation;
import dev.tr7zw.notenoughanimations.animations.fullbody.CrawlingAnimation;
import dev.tr7zw.notenoughanimations.animations.fullbody.PassengerAnimation;
import dev.tr7zw.notenoughanimations.animations.fullbody.RotationFixerAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.BoatAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.EatDrinkAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.HorseAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.ItemSwapAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.LookAtItemAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.MapHoldingAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.SleepAnimation;
import dev.tr7zw.notenoughanimations.animations.vanilla.VanillaShieldAnimation;
import dev.tr7zw.notenoughanimations.animations.vanilla.VanillaSingleHandedAnimation;
import dev.tr7zw.notenoughanimations.animations.vanilla.VanillaTwoHandedAnimation;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

public class AnimationProvider {

    private Set<BasicAnimation> basicAnimations = new HashSet<>();
    private Set<BasicAnimation> enabledBasicAnimations = new HashSet<>(); 
    
    public AnimationProvider() {
        loadAnimations();
        refreshEnabledAnimations();
    }
    
    public void applyAnimations(AbstractClientPlayer entity, PlayerModel<AbstractClientPlayer> model, float delta, float swing) {
        PlayerData playerData = (PlayerData) entity;
        int[] priorities = new int[BodyPart.values().length];
        BasicAnimation[] animation = new BasicAnimation[priorities.length];
        for(BasicAnimation basicAnimation : enabledBasicAnimations) {
            if(basicAnimation.isValid(entity, playerData)) {
                int prio = basicAnimation.getPriority(entity, playerData);
                if(prio > 0) {
                    for(BodyPart part : basicAnimation.getBodyParts(entity, playerData)) {
                        if(prio > priorities[part.ordinal()]) {
                            priorities[part.ordinal()] = prio;
                            animation[part.ordinal()] = basicAnimation;
                        }
                    }
                }
            }
        }
        
        for(int i = 0; i < priorities.length; i++) {
            if(animation[i] != null) {
                animation[i].prepare(entity, playerData, model, delta, swing);
                animation[i].apply(entity, playerData, model, BodyPart.values()[i], delta, swing);
            }
        }
        for(int i = 0; i < priorities.length; i++) {
            if(animation[i] != null) {
                animation[i].cleanup();
            }
        }
    }
    
    private void loadAnimations() {
        basicAnimations.add(new CrawlingAnimation());
        basicAnimations.add(new VanillaSingleHandedAnimation());
        basicAnimations.add(new VanillaTwoHandedAnimation());
        basicAnimations.add(new ItemSwapAnimation());
        basicAnimations.add(new LookAtItemAnimation());
        basicAnimations.add(new SleepAnimation());
        basicAnimations.add(new MapHoldingAnimation());
        basicAnimations.add(new BoatAnimation());
        basicAnimations.add(new HorseAnimation());
        basicAnimations.add(new ClimbingAnimation());
        basicAnimations.add(new EatDrinkAnimation());
        basicAnimations.add(new VanillaShieldAnimation());
        basicAnimations.add(new PassengerAnimation());
        basicAnimations.add(new RotationFixerAnimation());
    }

    public void refreshEnabledAnimations() {
        enabledBasicAnimations.clear();
        for(BasicAnimation basicAnimation : basicAnimations) {
            if(basicAnimation.isEnabled()) {
                enabledBasicAnimations.add(basicAnimation);
            }
        }
    }
    
}
