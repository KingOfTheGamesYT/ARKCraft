/**
 * 
 */
package com.uberverse.arkcraft.common.config;

import java.io.File;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.item.tool.ItemToolBase;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ERBF
 * @author Lewis_McReu
 */
public class WeightsConfig
{

	public static Configuration config;
	public static final String GENERAL = Configuration.CATEGORY_GENERAL;

	public static boolean isEnabled;
	public static boolean allowInCreative;
	public static double encumberedSpeed;

	public static void init(File dir)
	{
		File configFile = new File(dir, ARKCraft.MODID + "_weights.cfg");
		if (config == null)
		{
			config = new Configuration(configFile);
		}
		loadConfig();
	}

	@SuppressWarnings("unchecked")
	private static void loadConfig()
	{
		config.load();

		isEnabled = config.getBoolean("enabled", "Setup", false,
				"Enabled the weight system. Will be false until final release.");
		allowInCreative = config.getBoolean("allowInCreative", "Setup", false,
				"Allow weight tracking while in creative mode.");
		encumberedSpeed = config.getFloat("encumberedSpeed", "Setup", (float) 0.2, 0, 1,
				"The speed factor the player will be slowed down.");

		List<Item> itemList = ImmutableList.copyOf(Item.itemRegistry);
		for (Item item : itemList)
		{
			config.getFloat(item.getUnlocalizedName().substring(5), GENERAL, generateWeight(item), 0, 16,
					"Sets the carry weight of item " + item.getUnlocalizedName().substring(5, item.getUnlocalizedName()
							.length()));
		}

		List<Block> blockList = ImmutableList.copyOf(Block.blockRegistry);
		for (Block block : blockList)
		{
			config.getFloat(block.getUnlocalizedName().substring(5, block.getUnlocalizedName().length()), GENERAL,
					generateWeight(block), 0, 16, "Sets the carry weight of block " + block.getUnlocalizedName()
							.substring(5, block.getUnlocalizedName().length()));
		}

		config.save();
	}

	private static float generateWeight(Item item)
	{

		if (item instanceof ItemTool)
		{
			switch (((ItemTool) item).getToolMaterial())
			{
				case WOOD:
					return 2;
				case STONE:
					return 5;
				case IRON:
					return 8;
				case GOLD:
					return 6;
				case EMERALD:
					return 7;
			}
		}
		if (item instanceof ItemToolBase)
		{
			switch (((ItemToolBase) item).material)
			{
				case STONE:
					return 6;
				case METAL:
					return 9;
			}
		}
		if (item instanceof ItemBlock) return generateWeight(((ItemBlock) item).block);
		if (item instanceof ItemArmor)
		{
			switch (((ItemArmor) item).getArmorMaterial())
			{
				case LEATHER:
					return 3;
				case CHAIN:
					return 5;
				case DIAMOND:
					return 7;
				case GOLD:
					return 5;
				case IRON:
					return 8;
			}
		}
		return 2;
	}

	private static float generateWeight(Block block)
	{
		Material m = block.getMaterial();
		if (m == Material.air || m == Material.barrier || m == Material.fire || m == Material.portal) return 0;
		if (m == Material.cactus || m == Material.grass || m == Material.gourd || m == Material.craftedSnow
				|| m == Material.leaves || m == Material.plants || m == Material.glass || m == Material.vine
				|| m == Material.web) return 0.1f;
		if (m == Material.anvil || m == Material.dragonEgg || m == Material.iron) return 8;
		if (m == Material.cake || m == Material.carpet || m == Material.tnt || m == Material.cloth
				|| m == Material.sponge) return 0.5f;
		if (m == Material.circuits || m == Material.sand || m == Material.redstoneLight) return 1;
		if (m == Material.coral || m == Material.lava || m == Material.ice || m == Material.packedIce
				|| m == Material.piston) return 3.5f;
		if (m == Material.rock) return 5;
		// default
		return 2;
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event)
	{
		if (event.modID.equals(ARKCraft.MODID))
		{
			syncConfig(config);
		}
	}

	private void syncConfig(Configuration config)
	{
		loadConfig();
		if (config.hasChanged())
		{
			config.save();
		}
	}

	public static Configuration getConfig()
	{
		return config;
	}
}
