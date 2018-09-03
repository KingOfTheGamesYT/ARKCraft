package com.arkcraft.registry;

import com.arkcraft.ARKCraft;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = ARKCraft.MODID)
public class BlockRegistryHandler {

	@SubscribeEvent
	public static void handleBlockRegistry(RegistryEvent.Register<Block> registerEvent) {
		IForgeRegistry<Block> registry = registerEvent.getRegistry();
	}
}
