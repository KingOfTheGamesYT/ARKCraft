package com.arkcraft.init;

import com.arkcraft.common.gen.WorldGeneratorBushes;
import com.arkcraft.common.gen.resource.CrystalResourceGenerator;
import com.arkcraft.common.gen.resource.MetalResourceGenerator;
import com.arkcraft.common.gen.resource.ObsidianResourceGenerator;
import com.arkcraft.common.gen.resource.OilResourceGenerator;
import com.arkcraft.common.gen.resource.RockResourceGenerator;
import com.arkcraft.common.gen.resource.SmallRockResourceGenerator;

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
		register(new SmallRockResourceGenerator());
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
