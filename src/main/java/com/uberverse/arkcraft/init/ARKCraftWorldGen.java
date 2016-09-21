package com.uberverse.arkcraft.init;

import com.uberverse.arkcraft.common.gen.WorldGeneratorBushes;
import com.uberverse.arkcraft.common.gen.resource.CrystalResourceGenerator;
import com.uberverse.arkcraft.common.gen.resource.MetalResourceGenerator;
import com.uberverse.arkcraft.common.gen.resource.ObsidianResourceGenerator;
import com.uberverse.arkcraft.common.gen.resource.OilResourceGenerator;
import com.uberverse.arkcraft.common.gen.resource.RockResourceGenerator;

import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ARKCraftWorldGen
{
	public static void init()
	{
		register(new RockResourceGenerator());
		register(new CrystalResourceGenerator());
		register(new ObsidianResourceGenerator());
		register(new MetalResourceGenerator());
		register(new WorldGeneratorBushes());
		register(new OilResourceGenerator());
	}

	private static void register(IWorldGenerator generator)
	{
		register(generator, 0);
	}

	private static void register(IWorldGenerator generator, int weight)
	{
		GameRegistry.registerWorldGenerator(generator, weight);
	}
}
