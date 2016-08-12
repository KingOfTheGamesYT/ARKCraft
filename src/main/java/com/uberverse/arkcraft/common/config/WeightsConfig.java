/**
 * 
 */
package com.uberverse.arkcraft.common.config;

import java.io.File;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.item.engram.Engram;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ERBF
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
		
		isEnabled = config.getBoolean("enabled", "Setup", false, "Enabled the weight system. Will be false until final release.");
		allowInCreative = config.getBoolean("allowInCreative", "Setup", false, "Allow weight tracking while in creative mode.");
		encumberedSpeed = config.getFloat("encumberedSpeed", "Setup", (float) 0.2, 0, 1, "The speed factor the player will be slowed down.");
		
		List<Item> itemList = ImmutableList.copyOf(Item.itemRegistry);
		for(Item item : itemList)
		{
			if(!(item instanceof Engram)) {
				config.getFloat(item.getUnlocalizedName().substring(5, item.getUnlocalizedName().length()), GENERAL, (int) genNewWeight(item.getUnlocalizedName().charAt(6)), 0, 16, "Sets the carry weight of item " + item.getUnlocalizedName().substring(5, item.getUnlocalizedName().length()));
			}
		}
		
		List<Block> blockList = ImmutableList.copyOf(Block.blockRegistry);
		for(Block block : blockList)
		{
			config.getFloat(block.getUnlocalizedName().substring(5, block.getUnlocalizedName().length()), GENERAL, (int) genNewWeight(block.getUnlocalizedName().charAt(6)), 0, 16, "Sets the carry weight of block " + block.getUnlocalizedName().substring(5, block.getUnlocalizedName().length()));
		}
		
		config.save();
	}
	
	private static int genNewWeight(char c)
	{
		int number = Character.getNumericValue(c);
		return (int)  Math.abs((((number / 2) + 4) / 9) - 1) != 0 ? (int) Math.abs((((number / 2) + 4) / 9) - 1) : (int) Math.abs((((number / 2) + 4) / 9) - 1) + 2;
	}
	
	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) 
	{
		if(event.modID.equals(ARKCraft.MODID))
		{
			syncConfig(config);
		}
	}
	
	private void syncConfig(Configuration config)
	{
		loadConfig();
		if(config.hasChanged())
		{
			config.save();
		}
	}
	
	public static Configuration getConfig() 
	{
		return config;
	}
	
}
