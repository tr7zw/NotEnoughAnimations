//#if FORGE
//$$package dev.tr7zw.notenoughanimations;
//$$
//$$import net.minecraftforge.api.distmarker.Dist;
//$$import net.minecraftforge.fml.DistExecutor;
//$$import net.minecraftforge.fml.common.Mod;
//$$
//$$@Mod("notenoughanimations")
//$$public class NEABootstrap {
//$$
//$$	public NEABootstrap() {
//$$		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> NEAnimationsMod::new);
//$$	}
//$$	
//$$}
//#elseif NEOFORGE
//$$package dev.tr7zw.notenoughanimations;
//$$
//$$import net.neoforged.api.distmarker.Dist;
//$$import net.neoforged.fml.DistExecutor;
//$$import net.neoforged.fml.common.Mod;
//$$
//$$@Mod("notenoughanimations")
//$$public class NEABootstrap {
//$$
//$$	public NEABootstrap() {
//$$		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> NEAnimationsMod::new);
//$$	}
//$$	
//$$}
//#endif