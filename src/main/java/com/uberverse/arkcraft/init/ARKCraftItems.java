package com.uberverse.arkcraft.init;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.google.common.collect.Sets;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.item.ARKCraftFeces;
import com.uberverse.arkcraft.common.item.ARKCraftFood;
import com.uberverse.arkcraft.common.item.ARKCraftItem;
import com.uberverse.arkcraft.common.item.ARKCraftSeed;
import com.uberverse.arkcraft.common.item.explosives.ItemGrenade;
import com.uberverse.arkcraft.common.item.firearms.ItemSlingshot;
import com.uberverse.arkcraft.common.item.melee.ItemSpear;
import com.uberverse.arkcraft.common.item.tools.ItemMetalHatchet;
import com.uberverse.arkcraft.common.item.tools.ItemMetalPick;
import com.uberverse.arkcraft.common.item.tools.ItemMetalSickle;
import com.uberverse.arkcraft.common.item.tools.ItemStoneHatchet;
import com.uberverse.arkcraft.common.item.tools.ItemStonePick;

public class ARKCraftItems {
	
	//Tools
	public static ItemStonePick stone_pick;
	public static ItemStoneHatchet stone_hatchet;
	public static ItemMetalPick metal_pick;
	public static ItemMetalHatchet metal_hatchet;
	public static ItemMetalSickle metal_sickle;
	
    private static final Set EFFECTIVE_ON = Sets.newHashSet(new Block[] {Blocks.activator_rail, Blocks.coal_ore, Blocks.cobblestone, Blocks.detector_rail, Blocks.diamond_block, Blocks.diamond_ore, Blocks.double_stone_slab, Blocks.golden_rail, Blocks.gold_block, Blocks.gold_ore, Blocks.ice, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.mossy_cobblestone, Blocks.netherrack, Blocks.packed_ice, Blocks.rail, Blocks.redstone_ore, Blocks.sandstone, Blocks.red_sandstone, Blocks.stone, Blocks.stone_slab});
	
	//Food
	public static ARKCraftFood tintoBerry, amarBerry, azulBerry, mejoBerry,
	narcoBerry, stimBerry, meat_raw, meat_cooked, primemeat_raw,
	primemeat_cooked, spoiled_meat;
	public static ARKCraftSeed tintoBerrySeed, amarBerrySeed, azulBerrySeed, mejoBerrySeed, narcoBerrySeed, stimBerrySeed;
	
	//Misc
	public static ARKCraftItem stone, spy_glass, fiber, thatch, wood, flint, metal;
	public static ARKCraftFeces small_feces, medium_feces, large_feces, fertilizer, player_feces;
	
	public static ItemGrenade grenade;
	public static ItemSlingshot slingshot;
	public static ItemSpear spear;

	
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
		stone = addItem("stone");
		spy_glass = addItem("spy_glass");
		fiber = addItem("fiber");
		thatch = addItem("thatch");
		wood = addItem("wood");
		flint = addItem("flint");
		metal = addItem("metal");
		
		//Tools
		metal_pick = addMetalPick("metal_pick", METAL);
		metal_hatchet = addMetalHatchet("metal_hatchet", 1F, EFFECTIVE_ON, 10, 3);
		stone_hatchet = addStoneHatchet("stone_hatchet", STONE);
		stone_pick = addStonePick("stone_pick", STONE);
	
		// Food
		tintoBerry = addFood("tinto", 4, 0.3F, false, true);
		amarBerry = addFood("amar", 4, 0.3F, false, true);
		azulBerry = addFood("azul", 4, 0.3F, false, true);
		mejoBerry = addFood("mejo", 4, 0.3F, false, true);
		narcoBerry = addFood("narco", 4, 0.3F, true, true);
		stimBerry = addFood("stim", 4, 0.3F, true, true);
		meat_raw = addFood("meat_raw", 3, 0.3F, false, false);
		meat_cooked = addFood("meat_cooked", 6, 0.9F, false, false);
		primemeat_raw = addFood("primemeat_raw", 3, 0.3F, false, false);
		primemeat_cooked = addFood("primemeat_cooked", 8, 1.2F, false, false);
		spoiled_meat = addFood("spoiled_meat", 2, 0.1F, false, false);

		// Seeds
		tintoBerrySeed = addSeedItem("tintoBerrySeed");
		amarBerrySeed = addSeedItem("amarBerrySeed");
		azulBerrySeed = addSeedItem("azulBerrySeed");
		mejoBerrySeed = addSeedItem("mejoBerrySeed");
		narcoBerrySeed = addSeedItem("narcoBerrySeed");
		stimBerrySeed = addSeedItem("stimBerrySeed");
		
		//feces
		small_feces = addFecesItem(
				"small_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_SMALL_FECES_TO_DECOMPOSE);
		medium_feces = addFecesItem(
				"medium_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_SMALL_FECES_TO_DECOMPOSE);
		large_feces = addFecesItem(
				"large_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_SMALL_FECES_TO_DECOMPOSE);
		player_feces = addFecesItem(
				"player_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_PLAYER_FECES_TO_DECOMPOSE);

		// Technically not feces, but used in all situations the same
		// (currently)
		fertilizer = addFecesItem("fertilizer",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_FERTILIZER_TO_DECOMPOSE);

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
	
	public static ARKCraftFeces addFecesItem(String name, int maxDamageIn)
	{
		ARKCraftFeces i = new ARKCraftFeces();
		i.setMaxDamage(maxDamageIn);
		registerItem(name, i);
		return i;
	}

	public static ItemStoneHatchet addStoneHatchet(String name, ToolMaterial m)
	{
		ItemStoneHatchet i = new ItemStoneHatchet(m);
		registerItem(name, i);
		return i;
	}

	
	public static ItemMetalHatchet addMetalHatchet(String name, float attackDamage, Set effectiveBlocks, int durability, int efficiency)
	{
		ItemMetalHatchet i = new ItemMetalHatchet(name, attackDamage, effectiveBlocks, durability, efficiency);
		registerItem(name, i);
		return i;
	}	
	
	public static ARKCraftItem addItem(String name)
	{
		ARKCraftItem i = new ARKCraftItem();
		registerItem(name, i);
		return i;
	}
	
	protected static ARKCraftFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible)
	{
		ARKCraftFood f = new ARKCraftFood(heal, sat, fav, alwaysEdible);
		registerItem(name, f);
		return f;
	}
	
	public static ARKCraftFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible, PotionEffect... effect)
	{
		ARKCraftFood f = new ARKCraftFood(heal, sat, fav, alwaysEdible, effect);
		registerItem(name, f);
		return f;
	}
	
	protected static ARKCraftSeed addSeedItem(String name)
	{
		ARKCraftSeed i = new ARKCraftSeed();
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
