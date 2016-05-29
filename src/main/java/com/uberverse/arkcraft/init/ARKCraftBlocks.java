package com.uberverse.arkcraft.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.BlockFlashlight;

public class ARKCraftBlocks {
	
	public static BlockFlashlight block_flashlight;

	public static Map<String, Block> allBlocks = new HashMap<String, Block>();

	public static Map<String, Block> getAllBlocks()
	{
		return allBlocks;
	}

	public static void init()
	{
		block_flashlight = new BlockFlashlight();
		GameRegistry.registerBlock(block_flashlight, "block_flashlight");
	}

	@SuppressWarnings("unused")
	private static Block registerBlock(Block block, String name)
	{
		block.setCreativeTab(ARKCraft.tabARK);
		return registerBlockNoTab(block, name);
	}

	private static Block registerBlockNoTab(Block block, String name)
	{
		GameRegistry.registerBlock(block, name);
		block.setUnlocalizedName(name);
		allBlocks.put(name, block);
		return block;
	}

	@SuppressWarnings("unused")
	private static Block registerBlockWithItemBlock(Block block, Class<? extends ItemBlock> itemBlock, String name)
	{
		block.setCreativeTab(ARKCraft.tabARK);
		GameRegistry.registerBlock(block, itemBlock, name);
		block.setUnlocalizedName(name);
		allBlocks.put(name, block);
		return block;
	}

	protected static Block getRegisteredBlock(String name)
	{
		return (Block) Block.blockRegistry.getObject(new ResourceLocation(name));
	}

}
