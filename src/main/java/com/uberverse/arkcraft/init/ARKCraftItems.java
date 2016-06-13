package com.uberverse.arkcraft.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.item.ARKCraftItem;
import com.uberverse.arkcraft.common.item.explosives.ItemGrenade;
import com.uberverse.arkcraft.common.item.firearms.ItemSlingshot;
import com.uberverse.arkcraft.common.item.melee.ItemSpear;
import com.uberverse.arkcraft.common.item.tools.ItemMetalHatchet;
import com.uberverse.arkcraft.common.item.tools.ItemMetalPick;
import com.uberverse.arkcraft.common.item.tools.ItemStoneHatchet;
import com.uberverse.arkcraft.common.item.tools.ItemStonePick;

public class ARKCraftItems {
	
	public static ARKCraftItem azul, stone, spy_glass;
	public static ItemGrenade grenade;
	public static ItemSlingshot slingshot;
	public static ItemSpear spear;
	public static ItemStonePick stone_pick;
	public static ItemStoneHatchet stone_hatchet;
	public static ItemMetalPick metal_pick;
	public static ItemMetalHatchet metal_hatchet;
	
	public static ToolMaterial METAL = EnumHelper.addToolMaterial("METAL_MAT",
			3, 1500, 6.0F, 0.8F, 8);
	public static ToolMaterial STONE = EnumHelper.addToolMaterial("STONE_MAT",
			2, 500, 3.5F, 0.4F, 13);
	
	public static Map<String, Item> allItems = new HashMap<String, Item>();

	public static Map<String, Item> getAllItems()
	{
		return allItems;
	}
	
	public static void init()
	{
	//	slingshot = addSlingshot("slingshot");
	//	grenade = addGrenade("grenade");
		stone = addItem("stone");
		spy_glass = addItem("spy_glass");
		// stoneSpear = addWeaponThrowable("stoneSpear", ToolMaterial.STONE);
		
		//Tools
		metal_pick = addMetalPick("metal_pick", METAL);
		metal_hatchet = addMetalHatchet("metal_hatchet", METAL);
		stone_hatchet = addStoneHatchet("stone_hatchet", STONE);
		stone_pick = addStonePick("stone_pick", STONE);
	

	//	spear = addSpearItem("spear", ToolMaterial.WOOD);
	//	wooden_club = addWoodenClub("wooden_club", ToolMaterial.WOOD);

	//	EntityHandler.registerModEntity(EntityGrenade.class, "grenade",
	//			ARKCraft.instance, 64, 10, true);
	//	EntityHandler.registerModEntity(EntitySpear.class, "spear",
	//			ARKCraft.instance, 64, 10, true);

	}

	protected static ItemSlingshot addSlingshot(String name)
	{
		ItemSlingshot slingshot = new ItemSlingshot();
		registerItem(name, slingshot);
		return slingshot;
	}

	protected static ItemGrenade addGrenade(String name)
	{
		ItemGrenade slingshot = new ItemGrenade();
		registerItem(name, slingshot);
		return slingshot;
	}

	public static ItemSpear addSpearItem(String name, ToolMaterial mat)
	{
		ItemSpear weapon = new ItemSpear(mat);
		registerItem(name, weapon);
		return weapon;
	}
	
	public static ItemMetalPick addMetalPick(String name, ToolMaterial m)
	{
		ItemMetalPick i = new ItemMetalPick(m);
		registerItem(name, i);
		return i;
	}

	public static ItemStonePick addStonePick(String name, ToolMaterial m)
	{
		ItemStonePick i = new ItemStonePick(m);
		registerItem(name, i);
		return i;
	}

	public static ItemStoneHatchet addStoneHatchet(String name, ToolMaterial m)
	{
		ItemStoneHatchet i = new ItemStoneHatchet(m);
		registerItem(name, i);
		return i;
	}

	public static ItemMetalHatchet addMetalHatchet(String name, ToolMaterial m)
	{
		ItemMetalHatchet i = new ItemMetalHatchet(m);
		registerItem(name, i);
		return i;
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
		GameRegistry.registerItem(item, name);
		item.setCreativeTab(ARKCraft.tabARK);
	}

}
