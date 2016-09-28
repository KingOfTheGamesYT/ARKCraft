package com.uberverse.arkcraft.init;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.entity.EntityDodo;
import com.uberverse.arkcraft.common.handlers.EntityHandler;

import net.minecraft.world.biome.BiomeGenBase;

public class ARKCraftEntities
{
	public static void init()
	{
		EntityHandler.registerEntityEgg(EntityDodo.class, ARKCraft.MODID + ".dodo", BiomeGenBase.beach,
				BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.birchForest, BiomeGenBase.extremeHills);
	}
}
