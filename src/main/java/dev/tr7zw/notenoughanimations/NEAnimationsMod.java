package dev.tr7zw.notenoughanimations;

import dev.tr7zw.transliterationlib.api.event.APIEvent;
import net.fabricmc.api.ModInitializer;

public class NEAnimationsMod implements ModInitializer {
	@Override
	public void onInitialize() {
		APIEvent.LOADED.register(() -> {
			new RotationFixer().enable();
			new ArmTransformer().enable();
			new HeldItemHandler().enable();
		});
	}

}
