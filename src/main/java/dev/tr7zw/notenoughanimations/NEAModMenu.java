//#if FABRIC
package dev.tr7zw.notenoughanimations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import dev.tr7zw.notenoughanimations.config.ConfigScreenProvider;

public class NEAModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            return ConfigScreenProvider.createConfigScreen(parent);
        };
    }

}
//#endif