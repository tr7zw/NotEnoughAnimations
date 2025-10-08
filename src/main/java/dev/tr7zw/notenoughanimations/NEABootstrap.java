//#if FORGE
//$$package dev.tr7zw.notenoughanimations;
//$$
//$$import net.minecraftforge.api.distmarker.Dist;
//$$import net.minecraftforge.fml.DistExecutor;
//$$import net.minecraftforge.fml.common.Mod;
//$$import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//$$import dev.tr7zw.transition.loader.ModLoaderUtil;
//$$
//$$@Mod("notenoughanimations")
//$$public class NEABootstrap {
//$$
//$$	public NEABootstrap(FMLJavaModLoadingContext context) {
//$$            ModLoaderUtil.setModLoadingContext(context);
//$$		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> new NEAnimationsMod().onInitializeClient());
//$$	}
//$$    public NEABootstrap() {
//$$        this(FMLJavaModLoadingContext.get());
//$$    }
//$$	
//$$}
//#elseif NEOFORGE
//$$package dev.tr7zw.notenoughanimations;
//$$
//$$import net.neoforged.api.distmarker.Dist;
//$$import net.neoforged.fml.loading.FMLEnvironment;
//$$import net.neoforged.fml.common.Mod;
//$$import dev.tr7zw.transition.loader.ModLoaderEventUtil;
//$$
//$$@Mod("notenoughanimations")
//$$public class NEABootstrap {
//$$
//$$	public NEABootstrap() {
//#if MC < 12109
//$$        if(FMLEnvironment.dist == Dist.CLIENT) {
//#else
//$$        if(FMLEnvironment.getDist() == Dist.CLIENT) {
//#endif
//$$                    ModLoaderEventUtil.registerClientSetupListener(() -> new NEAnimationsMod().onInitializeClient());
//$$            }
//$$	}
//$$	
//$$}
//#endif