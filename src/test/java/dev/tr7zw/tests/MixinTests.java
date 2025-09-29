package dev.tr7zw.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import net.minecraft.SharedConstants;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.server.Bootstrap;

public class MixinTests {

    @BeforeAll
    public static void setup() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    public void testMixins() {
        Objenesis objenesis = new ObjenesisStd();
        objenesis.newInstance(ItemInHandLayer.class);
        objenesis.newInstance(ItemInHandRenderer.class);
        objenesis.newInstance(LevelRenderer.class);
        objenesis.newInstance(RemotePlayer.class);
        objenesis.newInstance(PlayerModel.class);
        //#if MC >= 12109
        objenesis.newInstance(net.minecraft.client.renderer.entity.player.AvatarRenderer.class);
        //#else
        //$$objenesis.newInstance(net.minecraft.client.renderer.entity.player.PlayerRenderer.class);
        //#endif
        objenesis.newInstance(LevelRenderer.class);
    }

    //    @Test
    //    public void langTests() throws Throwable {
    //        Language lang = TestUtil.loadDefault("/assets/notenoughanimations/lang/en_us.json");
    //        NEAnimationsLoader.INSTANCE = new TestMod();
    //        NEAnimationsLoader.config = new Config();
    //        CustomConfigScreen screen = (CustomConfigScreen) ConfigScreenProvider.createConfigScreen(null);
    //        List<OptionInstance<?>> options = TestUtil.bootStrapCustomConfigScreen(screen);
    //        assertNotEquals(screen.getTitle().getString(), lang.getOrDefault(screen.getTitle().getString()));
    //        for (OptionInstance<?> option : options) {
    //            Set<String> keys = TestUtil.getKeys(option, true);
    //            for (String key : keys) {
    //                System.out.println(key + " " + lang.getOrDefault(key));
    //                assertNotEquals(key, lang.getOrDefault(key));
    //            }
    //        }
    //    }

}