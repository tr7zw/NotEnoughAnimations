package dev.tr7zw.notenoughanimations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class NEAModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            return NEAnimationsLoader.INSTANCE.createConfigScreen(parent);
        };
    }

}
