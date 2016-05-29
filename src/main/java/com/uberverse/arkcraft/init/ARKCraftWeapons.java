package com.uberverse.arkcraft.init;

import java.util.HashMap;
import java.util.Map;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.item.ARKCraftItem;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ARKCraftWeapons {
	
	public static ARKCraftItem azul;
	
	public static Map<String, Item> allItems = new HashMap<String, Item>();

	public static Map<String, Item> getAllItems()
	{
		return allItems;
	}

	public static void init()
	{
		azul = addItem("azul");
	}
	
	public static ARKCraftItem addItem(String name)
	{
		ARKCraftItem i = new ARKCraftItem();
		registerItem(name, i);
		return i;
	}
	
	public static void registerItem(String name, Item item)
	{
		allItems.put(name, item);
		item.setUnlocalizedName(name);
		item.setCreativeTab(ARKCraft.tabARK);
		GameRegistry.registerItem(item, name);
	}

}
