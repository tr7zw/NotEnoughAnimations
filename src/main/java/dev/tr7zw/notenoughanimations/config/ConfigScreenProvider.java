package dev.tr7zw.notenoughanimations.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.RotationLock;
import dev.tr7zw.notenoughanimations.versionless.animations.BowAnimation;
import dev.tr7zw.notenoughanimations.versionless.animations.HoldUpModes;
import dev.tr7zw.notenoughanimations.versionless.animations.HoldUpTarget;
import dev.tr7zw.notenoughanimations.versionless.config.Config;
import dev.tr7zw.transition.mc.ComponentProvider;
import dev.tr7zw.transition.mc.ItemUtil;
import dev.tr7zw.trender.gui.client.AbstractConfigScreen;
import dev.tr7zw.trender.gui.client.BackgroundPainter;
import dev.tr7zw.trender.gui.widget.WButton;
import dev.tr7zw.trender.gui.widget.WGridPanel;
import dev.tr7zw.trender.gui.widget.WListPanel;
import dev.tr7zw.trender.gui.widget.WTabPanel;
import dev.tr7zw.trender.gui.widget.WTextField;
import dev.tr7zw.trender.gui.widget.WToggleButton;
import dev.tr7zw.trender.gui.widget.data.Insets;
import dev.tr7zw.trender.gui.widget.icon.ItemIcon;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.client.Minecraft;

public class ConfigScreenProvider {

    public static Screen createConfigScreen(Screen parent) {
        return new CustomConfigScreen(parent).createScreen();
    }

    private static class CustomConfigScreen extends AbstractConfigScreen {

        public CustomConfigScreen(Screen previous) {
            super(ComponentProvider.translatable("text.nea.title"), previous);

            WGridPanel root = new WGridPanel(8);
            root.setInsets(Insets.ROOT_PANEL);
            setRootPanel(root);

            WTabPanel wTabPanel = new WTabPanel();

            // options page
            List<OptionInstance> options = new ArrayList<>();
            options.add(getSplitLine("text.nea.line.animations"));
            options.add(getOnOffOption("text.nea.enable.inworldmaprendering",
                    () -> NEABaseMod.config.enableInWorldMapRendering,
                    b -> NEABaseMod.config.enableInWorldMapRendering = b));
            options.add(getOnOffOption("text.nea.enable.ladderanimation", () -> NEABaseMod.config.enableLadderAnimation,
                    b -> NEABaseMod.config.enableLadderAnimation = b));
            options.add(getOnOffOption("text.nea.enable.rotatetoladder", () -> NEABaseMod.config.enableRotateToLadder,
                    b -> NEABaseMod.config.enableRotateToLadder = b));
            options.add(getDoubleOption("text.nea.ladderAnimationAmplifier", 0.1f, 0.5f, 0.01f,
                    () -> (double) NEABaseMod.config.ladderAnimationAmplifier, (i) -> {
                        NEABaseMod.config.ladderAnimationAmplifier = (float) i;
                    }));
            options.add(getDoubleOption("text.nea.ladderAnimationArmHeight", 1f, 3f, 0.1f,
                    () -> (double) NEABaseMod.config.ladderAnimationArmHeight, (i) -> {
                        NEABaseMod.config.ladderAnimationArmHeight = (float) i;
                    }));
            options.add(getDoubleOption("text.nea.ladderAnimationArmSpeed", 1f, 4f, 0.1f,
                    () -> (double) NEABaseMod.config.ladderAnimationArmSpeed, (i) -> {
                        NEABaseMod.config.ladderAnimationArmSpeed = (float) i;
                    }));
            options.add(getOnOffOption("text.nea.enable.crawling", () -> NEABaseMod.config.enableCrawlingAnimation,
                    b -> NEABaseMod.config.enableCrawlingAnimation = b));
            options.add(
                    getOnOffOption("text.nea.enable.eatdrinkanimation", () -> NEABaseMod.config.enableEatDrinkAnimation,
                            b -> NEABaseMod.config.enableEatDrinkAnimation = b));
            options.add(getOnOffOption("text.nea.enable.rowboatanimation",
                    () -> NEABaseMod.config.enableRowBoatAnimation, b -> NEABaseMod.config.enableRowBoatAnimation = b));
            options.add(getOnOffOption("text.nea.enable.horseanimation", () -> NEABaseMod.config.enableHorseAnimation,
                    b -> NEABaseMod.config.enableHorseAnimation = b));
            options.add(getOnOffOption("text.nea.enable.enableHorseLegAnimation",
                    () -> NEABaseMod.config.enableHorseLegAnimation,
                    b -> NEABaseMod.config.enableHorseLegAnimation = b));
            options.add(getOnOffOption("text.nea.enable.itemSwapAnimation", () -> NEABaseMod.config.itemSwapAnimation,
                    b -> NEABaseMod.config.itemSwapAnimation = b));
            options.add(getOnOffOption("text.nea.enable.petAnimation", () -> NEABaseMod.config.petAnimation,
                    b -> NEABaseMod.config.petAnimation = b));
            options.add(getOnOffOption("text.nea.enable.freezingAnimation", () -> NEABaseMod.config.freezingAnimation,
                    b -> NEABaseMod.config.freezingAnimation = b));
            options.add(getEnumOption("text.nea.enable.bowAnimation", BowAnimation.class,
                    () -> NEABaseMod.config.bowAnimation, b -> NEABaseMod.config.bowAnimation = b));
            options.add(getOnOffOption("text.nea.enable.customBowRotationLock", () -> NEABaseMod.config.customBowRotationLock,
                    b -> NEABaseMod.config.customBowRotationLock = b));

            options.add(getOnOffOption("text.nea.enable.burningAnimation", () -> NEABaseMod.config.burningAnimation,
                    b -> NEABaseMod.config.burningAnimation = b));

            options.add(getSplitLine(""));
            options.add(getSplitLine("text.nea.line.smoothing"));
            options.add(getOnOffOption("text.nea.enable.animationsmoothing",
                    () -> NEABaseMod.config.enableAnimationSmoothing,
                    b -> NEABaseMod.config.enableAnimationSmoothing = b));
            options.add(getOnOffOption("text.nea.disableLegSmoothing", () -> NEABaseMod.config.disableLegSmoothing,
                    b -> NEABaseMod.config.disableLegSmoothing = b));
            options.add(getDoubleOption("text.nea.smoothingSpeed", 0.01f, 1f, 0.1f,
                    () -> (double) NEABaseMod.config.animationSmoothingSpeed, (i) -> {
                        NEABaseMod.config.animationSmoothingSpeed = (float) i;
                    }));

            options.add(getSplitLine(""));
            options.add(getSplitLine("text.nea.line.rotation"));
            options.add(getEnumOption("text.nea.rotationlock", RotationLock.class, () -> NEABaseMod.config.rotationLock,
                    b -> NEABaseMod.config.rotationLock = b));
            options.add(getOnOffOption("text.nea.enable.limitRotationLockToFP",
                    () -> NEABaseMod.config.limitRotationLockToFP, b -> NEABaseMod.config.limitRotationLockToFP = b));
            options.add(getOnOffOption("text.nea.enable.rotationlocking", () -> NEABaseMod.config.enableRotationLocking,
                    b -> NEABaseMod.config.enableRotationLocking = b));

            options.add(getSplitLine(""));
            options.add(getSplitLine("text.nea.line.holdup"));
            options.add(getEnumOption("text.nea.holdUpItemsMode", HoldUpModes.class,
                    () -> NEABaseMod.config.holdUpItemsMode, b -> NEABaseMod.config.holdUpItemsMode = b));
            options.add(getDoubleOption("text.nea.holdUpItemOffset", -0.5f, 0.4f, 0.1f,
                    () -> (double) NEABaseMod.config.holdUpItemOffset, (i) -> {
                        NEABaseMod.config.holdUpItemOffset = (float) i;
                    }));
            options.add(getEnumOption("text.nea.holdUpTarget", HoldUpTarget.class, () -> NEABaseMod.config.holdUpTarget,
                    b -> NEABaseMod.config.holdUpTarget = b));
            options.add(getDoubleOption("text.nea.holdUpCameraOffset", -0.3f, 0.6f, 0.1f,
                    () -> (double) NEABaseMod.config.holdUpCameraOffset, (i) -> {
                        NEABaseMod.config.holdUpCameraOffset = (float) i;
                    }));
            options.add(getOnOffOption("text.nea.enable.holdUpOnlySelf", () -> NEABaseMod.config.holdUpOnlySelf,
                    b -> NEABaseMod.config.holdUpOnlySelf = b));

            options.add(getSplitLine(""));
            options.add(getSplitLine("text.nea.line.fixes"));
            options.add(getOnOffOption("text.nea.enable.tweakElytraAnimation",
                    () -> NEABaseMod.config.tweakElytraAnimation, b -> NEABaseMod.config.tweakElytraAnimation = b));
            options.add(getOnOffOption("text.nea.enable.dontholditemsinbed", () -> NEABaseMod.config.dontHoldItemsInBed,
                    b -> NEABaseMod.config.dontHoldItemsInBed = b));
            options.add(getOnOffOption("text.nea.enable.freezearmsinbed", () -> NEABaseMod.config.freezeArmsInBed,
                    b -> NEABaseMod.config.freezeArmsInBed = b));
            options.add(getOnOffOption("text.nea.enable.offhandhiding", () -> NEABaseMod.config.enableOffhandHiding,
                    b -> NEABaseMod.config.enableOffhandHiding = b));

            options.add(getSplitLine(""));
            options.add(getSplitLine("text.nea.line.fun"));
            options.add(getOnOffOption("text.nea.enable.narutoRunning", () -> NEABaseMod.config.narutoRunning,
                    b -> NEABaseMod.config.narutoRunning = b));
            options.add(getOnOffOption("text.nea.enable.huggingAnimation", () -> NEABaseMod.config.huggingAnimation,
                    b -> NEABaseMod.config.huggingAnimation = b));

            options.add(getSplitLine(""));
            options.add(getSplitLine("text.nea.line.legacy"));
            options.add(getOnOffOption("text.nea.enable.enableInWorldBookRendering",
                    () -> NEABaseMod.config.enableInWorldBookRendering,
                    b -> NEABaseMod.config.enableInWorldBookRendering = b));
            options.add(getOnOffOption("text.nea.enable.fallingAnimation", () -> NEABaseMod.config.fallingAnimation,
                    b -> NEABaseMod.config.fallingAnimation = b));
            options.add(getOnOffOption("text.nea.enable.showlastusedsword", () -> NEABaseMod.config.showLastUsedSword,
                    b -> NEABaseMod.config.showLastUsedSword = b));

            var optionList = createOptionList(options);
            optionList.setGap(-1);
            optionList.setSize(14 * 20, 9 * 20);

            wTabPanel.add(optionList, b -> b.title(ComponentProvider.translatable("text.nea.tab.settings"))
                    .icon(new ItemIcon(Items.FILLED_MAP)));

            List<Entry<ResourceKey<Item>, Item>> items = new ArrayList<>(ItemUtil.getItems());
            items.sort((a, b) -> a.getKey().location().toString().compareTo(b.getKey().location().toString()));

            WListPanel<Entry<ResourceKey<Item>, Item>, WToggleButton> itemList = new WListPanel<Entry<ResourceKey<Item>, Item>, WToggleButton>(
                    items, () -> new WToggleButton(ComponentProvider.EMPTY), (s, l) -> {
                        l.setLabel(s.getValue().getName(s.getValue().getDefaultInstance()));
                        l.setToolip(ComponentProvider.literal(s.getKey().location().toString()));
                        l.setIcon(new ItemIcon(s.getValue()));
                        l.setToggle(NEABaseMod.config.holdingItems.contains(s.getKey().location().toString()));
                        l.setOnToggle(b -> {
                            if (b) {
                                NEABaseMod.config.holdingItems.add(s.getKey().location().toString());
                                NEAnimationsLoader.INSTANCE.animationProvider.refreshEnabledAnimations();
                            } else {
                                NEABaseMod.config.holdingItems.remove(s.getKey().location().toString());
                                NEAnimationsLoader.INSTANCE.animationProvider.refreshEnabledAnimations();
                            }
                            NEAnimationsLoader.INSTANCE.writeConfig();
                        });
                    });
            itemList.setGap(-1);
            itemList.setInsets(new Insets(2, 4));
            WGridPanel itemTab = new WGridPanel(20);
            itemTab.add(itemList, 0, 0, 17, 7);
            WTextField searchField = new WTextField();
            searchField.setChangedListener(s -> {
                itemList.setFilter(e -> e.getKey().location().toString().toLowerCase().contains(s.toLowerCase()));
                itemList.layout();
            });
            itemTab.add(searchField, 0, 7, 17, 1);
            wTabPanel.add(itemTab, b -> b.title(ComponentProvider.translatable("text.nea.tab.holdup"))
                    .icon(new ItemIcon(Items.TORCH)));

            wTabPanel.layout();
            root.add(wTabPanel, 0, 2);

            WButton doneButton = new WButton(CommonComponents.GUI_DONE);
            doneButton.setOnClick(() -> {
                save();
                Minecraft.getInstance().setScreen(previous);
            });
            root.add(doneButton, 0, 27, 6, 2);

            WButton resetButton = new WButton(ComponentProvider.translatable("controls.reset"));
            resetButton.setOnClick(() -> {
                reset();
                root.layout();
            });
            root.add(resetButton, 37, 27, 6, 2);

            root.setBackgroundPainter(BackgroundPainter.VANILLA);

            root.validate(this);
            root.setHost(this);
        }

        @Override
        public void reset() {
            NEABaseMod.config = new Config();
            NEAnimationsLoader.INSTANCE.writeConfig();
        }

        @Override
        public void save() {
            NEAnimationsLoader.INSTANCE.writeConfig();
            NEAnimationsLoader.INSTANCE.animationProvider.refreshEnabledAnimations();
        }

    }

}
