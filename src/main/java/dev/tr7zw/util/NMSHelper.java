package dev.tr7zw.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;

//spotless:off
//#if MC >= 11903
import net.minecraft.core.registries.BuiltInRegistries;

import org.joml.Quaternionf;
import com.mojang.math.Axis;
//#else
//$$ import net.minecraft.core.Registry;
//$$ import com.mojang.math.Vector3f;
//#endif
//spotless:on

public class NMSHelper {

    public static final float PI = (float) Math.PI;
    public static final float HALF_PI = (float) (Math.PI / 2);
    public static final float TWO_PI = (float) (Math.PI * 2);

    // spotless:off
	//#if MC >= 11903
	public static Axis XN = f -> new Quaternionf().rotationX(-f);
	public static Axis XP = f -> new Quaternionf().rotationX(f);
	public static Axis YN = f -> new Quaternionf().rotationY(-f);
	public static Axis YP = f -> new Quaternionf().rotationY(f);
	public static Axis ZN = f -> new Quaternionf().rotationZ(-f);
	public static Axis ZP = f -> new Quaternionf().rotationZ(f);
	//#else
	//$$ public static Vector3f XN = new Vector3f(-1.0F, 0.0F, 0.0F);
	//$$ public static Vector3f XP = new Vector3f(1.0F, 0.0F, 0.0F);
	//$$ public static Vector3f YN = new Vector3f(0.0F, -1.0F, 0.0F);
	//$$ public static Vector3f YP = new Vector3f(0.0F, 1.0F, 0.0F);
	//$$ public static Vector3f ZN = new Vector3f(0.0F, 0.0F, -1.0F);
	//$$ public static Vector3f ZP = new Vector3f(0.0F, 0.0F, 1.0F);
	//#endif
	//spotless:on

    public static Item getItem(ResourceLocation key) {
        // spotless:off
    	//#if MC >= 11903
        return BuiltInRegistries.ITEM.get(key);
		//#else
		//$$ return Registry.ITEM.get(key);
		//#endif
		//spotless:on
    }

    public static float getXRot(Entity ent) {
        // spotless:off
    	//#if MC >= 11700
    	return ent.getXRot();
    	//#else
    	//$$ return ent.xRot;
    	//#endif
    	//spotless:on
    }

}
