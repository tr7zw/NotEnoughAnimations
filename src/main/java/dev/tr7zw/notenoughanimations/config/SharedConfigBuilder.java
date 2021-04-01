package dev.tr7zw.notenoughanimations.config;

import static dev.tr7zw.transliterationlib.api.TRansliterationLib.transliteration;

import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.transliterationlib.api.config.ConfigBuilder;
import dev.tr7zw.transliterationlib.api.config.ConfigBuilder.ConfigEntryBuilder;
import dev.tr7zw.transliterationlib.api.config.ConfigBuilder.ConfigEntryBuilder.ConfigCategory;
import dev.tr7zw.transliterationlib.api.config.WrappedConfigEntry;
import dev.tr7zw.transliterationlib.api.wrapper.WrappedScreen;

public abstract class SharedConfigBuilder {

	protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public WrappedScreen createConfigScreen(WrappedScreen parentScreen) {
		Config config = NEAnimationsLoader.config;
		ConfigBuilder builder = transliteration.getNewConfigBuilder();
		builder.setParentScreen(parentScreen).setTitle(transliteration.getWrapper().getTranslateableText("text.notenoughanimations.title"));
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		
		ConfigCategory category = builder.getOrCreateCategory(transliteration.getWrapper().getTranslateableText("category.notenoughanimations"));
		setupAnimationConfig(entryBuilder, category, config);
		
		builder.setSavingRunnable(() -> {
			NEAnimationsLoader.INSTANCE.writeConfig();
		});
		builder.setTransparentBackground(true);
		
		return builder.build();
	}
	
	public void setupAnimationConfig(ConfigEntryBuilder entryBuilder, ConfigCategory category, Config config) {
		addEntry(category, createBooleanSetting(entryBuilder, "notenoughanimations.enableAnimationSmoothing",
				config.enableAnimationSmoothing, true, n -> config.enableAnimationSmoothing = n));
		addEntry(category, createBooleanSetting(entryBuilder, "notenoughanimations.enableInWorldMapRendering",
                config.enableInWorldMapRendering, true, n -> config.enableInWorldMapRendering = n));
		addEntry(category, createBooleanSetting(entryBuilder, "notenoughanimations.enableOffhandHiding",
                config.enableOffhandHiding, true, n -> config.enableOffhandHiding = n));
		addEntry(category, createBooleanSetting(entryBuilder, "notenoughanimations.enableRotationLocking",
                config.enableRotationLocking, true, n -> config.enableRotationLocking = n));
		
		addEntry(category, createIntSetting(entryBuilder, "notenoughanimations.animationSmoothingSpeed", (int)config.animationSmoothingSpeed, 20, 5, 100,
				n -> config.animationSmoothingSpeed = n));

	}

	public <T extends Enum<?>> WrappedConfigEntry createEnumSetting(ConfigEntryBuilder entryBuilder, String id, Class<T> type, T value,
			T def, Consumer<T> save) {
    	return entryBuilder.startEnumSelector(transliteration.getWrapper().getTranslateableText("text.notenoughanimations.option." + id), type, value)
		        .setDefaultValue(def)
		        .setTooltip(transliteration.getWrapper().getTranslateableText("text.notenoughanimations.option." + id + ".@Tooltip"))
		        .setSaveConsumer(save)
		        .setEnumNameProvider((en) -> (transliteration.getWrapper().getTranslateableText("text.notenoughanimations.option." + id + "." + en.name())))
		        .build();
    }

	public WrappedConfigEntry createBooleanSetting(ConfigEntryBuilder entryBuilder, String id, Boolean value, Boolean def,
			Consumer<Boolean> save) {
    	return entryBuilder.startBooleanToggle(transliteration.getWrapper().getTranslateableText("text.notenoughanimations.option." + id), value)
		        .setDefaultValue(def)
		        .setTooltip(transliteration.getWrapper().getTranslateableText("text.notenoughanimations.option." + id + ".@Tooltip"))
		        .setSaveConsumer(save)
		        .build();
	}

	public WrappedConfigEntry createIntSetting(ConfigEntryBuilder entryBuilder, String id, Integer value, Integer def, Integer min,
			Integer max, Consumer<Integer> save) {
    	return entryBuilder.startIntSlider(transliteration.getWrapper().getTranslateableText("text.notenoughanimations.option." + id), value, min, max)
		        .setDefaultValue(def)
		        .setTooltip(transliteration.getWrapper().getTranslateableText("text.notenoughanimations.option." + id + ".@Tooltip"))
		        .setSaveConsumer(save)
		        .build();
	}

	public abstract WrappedConfigEntry getPreviewEntry();

	public void addEntry(ConfigCategory category, WrappedConfigEntry entry) {
		category.addEntry(entry);
	}

}
