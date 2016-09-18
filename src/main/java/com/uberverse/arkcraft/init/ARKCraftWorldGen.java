package com.uberverse.arkcraft.init;

import com.uberverse.arkcraft.wip.oregen.RockResourceGenerator;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ARKCraftWorldGen
{
	public static void init()
	{
		GameRegistry.registerWorldGenerator(new RockResourceGenerator(), 0);
	}
}
