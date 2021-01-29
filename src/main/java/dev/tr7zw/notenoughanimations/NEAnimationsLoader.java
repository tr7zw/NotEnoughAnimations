package dev.tr7zw.notenoughanimations;

import dev.tr7zw.transliterationlib.api.TRansliterationLib;
import dev.tr7zw.transliterationlib.api.event.APIEvent;

public abstract class NEAnimationsLoader {

	public void onEnable() {
		if (TRansliterationLib.transliteration != null) {
			enable();
		} else {
			APIEvent.LOADED.register(() -> {
				enable();
			});
		}
	}
	
	private void enable() {
		new RotationFixer().enable();
		new ArmTransformer().enable();
		new HeldItemHandler().enable();
	}
	
}
