package dev.tr7zw.notenoughanimations;

import dev.tr7zw.transliterationlib.api.TRansliterationLib;
import net.minecraftforge.fml.common.Mod;

@Mod("notenoughanimations")
public class NEAnimationsModForge extends NEAnimationsLoader {

	public NEAnimationsModForge() {
		onEnable();
		if (TRansliterationLib.transliteration != null) {
		    TRansliterationLib.transliteration.getModLoaderWrapper().disableForgeServerDisplayTest();
		}
	}
	
}
