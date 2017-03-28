package com.uberverse.arkcraft.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * @author tom5454
 * */
public class SoundUtil {
	/**
	 * Use this to prevent missing sound crashes
	 * */
	public static void playSound(World world, double x, double y, double z, ResourceLocation soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay){
		SoundEvent s = SoundEvent.REGISTRY.getObject(soundIn);
		if(s != null){
			world.playSound(x, y, z, s, category, volume, pitch, distanceDelay);
		}else{
			/*ResourceLocation newL = new ResourceLocation(soundIn.getResourceDomain(), "sounds/" + soundIn.getResourcePath() + ".ogg");
			s = SoundEvent.REGISTRY.getObject(newL);
			if(s != null){
				world.playSound(x, y, z, s, category, volume, pitch, distanceDelay);
			}else{*/
			System.err.println("Missing sound: " + soundIn.toString());
			//}
		}
	}
}
