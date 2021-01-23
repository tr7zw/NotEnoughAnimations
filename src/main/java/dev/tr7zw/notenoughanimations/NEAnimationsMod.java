package dev.tr7zw.notenoughanimations;

import net.fabricmc.api.ModInitializer;

public class NEAnimationsMod implements ModInitializer {
	@Override
	public void onInitialize() {
		new RotationFixer().enable();
		new ArmTransformer().enable();
	}

}
