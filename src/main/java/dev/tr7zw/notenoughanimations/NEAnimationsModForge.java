package dev.tr7zw.notenoughanimations;

import dev.tr7zw.transliterationlib.api.event.APIEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("notenoughanimations")
public class NEAnimationsModForge {

	public NEAnimationsModForge() {
		APIEvent.LOADED.register(() -> {
			new RotationFixer().enable();
			new ArmTransformer().enable();
		});
	}
	
}
