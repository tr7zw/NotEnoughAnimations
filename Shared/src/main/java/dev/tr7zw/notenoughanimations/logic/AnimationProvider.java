package dev.tr7zw.notenoughanimations.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.animations.BodyPart;
import dev.tr7zw.notenoughanimations.animations.PoseOverwrite;
import dev.tr7zw.notenoughanimations.animations.fullbody.CrawlingAnimation;
import dev.tr7zw.notenoughanimations.animations.fullbody.FallingAnimation;
import dev.tr7zw.notenoughanimations.animations.fullbody.FreezingAnimation;
import dev.tr7zw.notenoughanimations.animations.fullbody.LadderAnimation;
import dev.tr7zw.notenoughanimations.animations.fullbody.PassengerAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.BoatAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.EatDrinkAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.HorseAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.HugAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.ItemSwapAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.LookAtItemAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.MapHoldingAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.NarutoRunningAnimation;
import dev.tr7zw.notenoughanimations.animations.hands.PetAnimation;
import dev.tr7zw.notenoughanimations.animations.vanilla.DeathAnimation;
import dev.tr7zw.notenoughanimations.animations.vanilla.ElytraAnimation;
import dev.tr7zw.notenoughanimations.animations.vanilla.RiptideAnimation;
import dev.tr7zw.notenoughanimations.animations.vanilla.SleepAnimation;
import dev.tr7zw.notenoughanimations.animations.vanilla.SwimAnimation;
import dev.tr7zw.notenoughanimations.animations.vanilla.VanillaShieldAnimation;
import dev.tr7zw.notenoughanimations.animations.vanilla.VanillaSingleHandedAnimation;
import dev.tr7zw.notenoughanimations.animations.vanilla.VanillaTwoHandedAnimation;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

public class AnimationProvider {

    private Set<BasicAnimation> basicAnimations = new HashSet<>();
    private Set<BasicAnimation> enabledBasicAnimations = new HashSet<>(); 
    private Set<PoseOverwrite> enabledPoseOverwrites = new HashSet<>(); 
    private boolean dumpPrios = false;
    
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
    
    public void preUpdate(AbstractClientPlayer livingEntity, PlayerModel<AbstractClientPlayer> playerModel) {
        for(PoseOverwrite po : enabledPoseOverwrites) {
            po.updateState(livingEntity, (PlayerData) livingEntity, playerModel);
        }
    }
    
    private void loadAnimations() {
        addAnimation(new CrawlingAnimation());
        addAnimation(new VanillaSingleHandedAnimation());
        addAnimation(new VanillaTwoHandedAnimation());
        addAnimation(new ItemSwapAnimation());
        addAnimation(new LookAtItemAnimation());
        addAnimation(new SleepAnimation());
        addAnimation(new MapHoldingAnimation());
        addAnimation(new BoatAnimation());
        addAnimation(new HorseAnimation());
        addAnimation(new LadderAnimation());
        addAnimation(new EatDrinkAnimation());
        addAnimation(new VanillaShieldAnimation());
        addAnimation(new PassengerAnimation());
        addAnimation(new RiptideAnimation());
        addAnimation(new DeathAnimation());
        addAnimation(new ElytraAnimation());
        addAnimation(new SwimAnimation());
        addAnimation(new PetAnimation());
        addAnimation(new FallingAnimation());
        addAnimation(new FreezingAnimation());
        addAnimation(new HugAnimation());
        addAnimation(new NarutoRunningAnimation());
    }

    public void addAnimation(BasicAnimation animation) {
        basicAnimations.add(animation);
    }
    
    public void refreshEnabledAnimations() {
        enabledBasicAnimations.clear();
        enabledPoseOverwrites.clear();
        for(BasicAnimation basicAnimation : basicAnimations) {
            if(basicAnimation.isEnabled()) {
                enabledBasicAnimations.add(basicAnimation);
                if(basicAnimation instanceof PoseOverwrite) {
                    enabledPoseOverwrites.add((PoseOverwrite) basicAnimation);
                }
            }
        }
        if(dumpPrios) {
            List<BasicAnimation> list = new ArrayList<>(basicAnimations);
            list.sort((a,b) -> Integer.compare(a.getPriority(null, null), b.getPriority(null, null)));
            for(BasicAnimation an : list) {
                System.out.println(an.getPriority(null, null) + " " + an.getClass().getSimpleName());
            }
        }
    }
    
}
