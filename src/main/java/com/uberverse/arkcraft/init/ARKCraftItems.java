package com.uberverse.arkcraft.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.PotionEffect;

import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.BlockCropPlot.BerryColor;
import com.uberverse.arkcraft.common.block.tile.TileEntityCropPlotNew.CropPlotType;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.config.ModuleItemBalance.PLAYER;
import com.uberverse.arkcraft.common.entity.EntitySpear;
import com.uberverse.arkcraft.common.handlers.EntityHandler;
import com.uberverse.arkcraft.common.item.ARKCraftBook;
import com.uberverse.arkcraft.common.item.ARKCraftFeces;
import com.uberverse.arkcraft.common.item.ARKCraftFood;
import com.uberverse.arkcraft.common.item.ARKCraftItem;
import com.uberverse.arkcraft.common.item.ARKCraftSeed;
import com.uberverse.arkcraft.common.item.armor.ItemARKArmor;
import com.uberverse.arkcraft.common.item.explosives.ItemGrenade;
import com.uberverse.arkcraft.common.item.melee.ItemPike;
import com.uberverse.arkcraft.common.item.melee.ItemSpear;
import com.uberverse.arkcraft.common.item.tools.ItemMetalHatchet;
import com.uberverse.arkcraft.common.item.tools.ItemMetalPick;
import com.uberverse.arkcraft.common.item.tools.ItemMetalSickle;
import com.uberverse.arkcraft.common.item.tools.ItemStoneHatchet;
import com.uberverse.arkcraft.common.item.tools.ItemStonePick;

public class ARKCraftItems
{

	// Tools
	public static ItemStonePick stone_pick;
	public static ItemStoneHatchet stone_hatchet;
	public static ItemMetalPick metal_pick;
	public static ItemMetalHatchet metal_hatchet;
	public static ItemMetalSickle metal_sickle;

	// Armor
	public static ItemARKArmor chitinHelm, chitinChest, chitinLegs, chitinBoots;
	public static ItemARKArmor clothHelm, clothChest, clothLegs, clothBoots;
	public static ItemARKArmor hideHelm, hideChest, hideLegs, hideBoots;
	public static ItemARKArmor furHelm, furChest, furLegs, furBoots;

	// Food
	public static ARKCraftFood tintoBerry, amarBerry, azulBerry, mejoBerry, narcoBerry, stimBerry,
	meat_raw, meat_cooked, primemeat_raw, primemeat_cooked, spoiled_meat;
	public static ARKCraftSeed tintoBerrySeed, amarBerrySeed, azulBerrySeed, mejoBerrySeed,
	narcoBerrySeed, stimBerrySeed;

	// Misc
	public static ARKCraftItem stone, fiber, thatch, wood, flint, metal, spark_powder, hide,
	charcoal, metal_ingot, cementing_paste, crystal, spy_glass, narcotics, gunpowder,
	chitin, keratin;
	public static ARKCraftFeces small_feces, medium_feces, large_feces, fertilizer, player_feces;
	public static ARKCraftBook info_book;

	public static ItemGrenade grenade;
	public static ItemSpear spear;
	public static ItemPike pike;
	public static Item tabItem;

	// Armor MAT
	public static ArmorMaterial CLOTH = EnumHelper.addArmorMaterial("CLOTH_MAT", "CLOTH_MAT", 4,
			new int[] { 1, 2, 1, 1 }, 15);
	public static ArmorMaterial CHITIN = EnumHelper.addArmorMaterial("CHITIN_MAT", "CHITIN_MAT", 16,
			new int[] { 3, 7, 6, 3 }, 10);
	public static ArmorMaterial HIDE = EnumHelper.addArmorMaterial("HIDE_MAT", "HIDE_MAT", 40,
			new int[] { 3, 8, 6, 3 }, 30);
	public static ArmorMaterial FUR = EnumHelper.addArmorMaterial("FUR_MAT", "HIDE_MAT", 40,
			new int[] { 3, 8, 6, 3 }, 30);

	// Tool MAT
	public static ToolMaterial METAL = EnumHelper.addToolMaterial("METAL_MAT", 3, 1500, 6.0F, 2.5F,
			8);
	public static ToolMaterial STONE = EnumHelper.addToolMaterial("STONE_MAT", 2, 500, 3.5F, 1.5F,
			13);
	public static ToolMaterial WOOD = EnumHelper.addToolMaterial("WOOD_MAT", 1, 200, 2.5F, 1.0F, 3);

	public static Map<String, Item> allItems = new HashMap<String, Item>();

	public static Map<String, Item> getAllItems()
	{
		return allItems;
	}

	public static void init()
	{

		// Resources
		cementing_paste = addItem("cementing_paste");
		crystal = addItem("crystal");
		hide = addItem("hide");
		charcoal = addItem("charcoal");
		metal_ingot = addItem("metal_ingot");
		stone = addItem("stone");
		fiber = addItem("fiber");
		thatch = addItem("thatch");
		wood = addItem("wood");
		flint = addItem("flint");
		metal = addItem("metal");
		spark_powder = addItem("spark_powder");
		spy_glass = addItem("spy_glass");
		narcotics = addItem("narcotics");
		gunpowder = addItem("gunpowder");
		chitin = addItem("chitin");
		keratin = addItem("keratin");

		// Tools
		metal_pick = addMetalPick("metal_pick", METAL);
		metal_hatchet = addMetalHatchet("metal_hatchet", METAL);
		stone_hatchet = addStoneHatchet("stone_hatchet", STONE);
		stone_pick = addStonePick("stone_pick", STONE);
		metal_sickle = addMetalSickle("metal_sickle", METAL);

		// Weapons
		spear = addSpear("spear", WOOD);
		pike = addPike("pike", METAL);

		// Armor
		chitinHelm = addArmorItem("chitin_helm", CHITIN, "chitinArmor", 0, false);
		chitinChest = addArmorItem("chitin_chest", CHITIN, "chitinArmor", 1, false);
		chitinLegs = addArmorItem("chitin_legs", CHITIN, "chitinArmor", 2, false);
		chitinBoots = addArmorItem("chitin_boots", CHITIN, "chitinArmor", 3, false);

		clothHelm = addArmorItem("cloth_helm", CLOTH, "clothArmor", 0, false);
		clothChest = addArmorItem("cloth_chest", CLOTH, "clothArmor", 1, false);
		clothLegs = addArmorItem("cloth_legs", CLOTH, "clothArmor", 2, false);
		clothBoots = addArmorItem("cloth_boots", CLOTH, "clothArmor", 3, false);

		hideHelm = addArmorItem("hide_helm", HIDE, "hideArmor", 0, false);
		hideChest = addArmorItem("hide_chest", HIDE, "hideArmor", 1, false);
		hideLegs = addArmorItem("hide_legs", HIDE, "hideArmor", 2, false);
		hideBoots = addArmorItem("hide_boots", HIDE, "hideArmor", 3, false);

		furHelm = addArmorItem("fur_helm", FUR, "furArmor", 0, false);
		furChest = addArmorItem("fur_chest", FUR, "furArmor", 1, false);
		furLegs = addArmorItem("fur_legs", FUR, "furArmor", 2, false);
		furBoots = addArmorItem("fur_boots", FUR, "furArmor", 3, false);

		EntityHandler.registerModEntity(EntitySpear.class, "spear", ARKCraft.instance, 16, 20,
				true);

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
		tintoBerrySeed = addSeedItem("tintoBerrySeed", CropPlotType.SMALL, BerryColor.TINTO);
		amarBerrySeed = addSeedItem("amarBerrySeed", CropPlotType.SMALL, BerryColor.AMAR);
		azulBerrySeed = addSeedItem("azulBerrySeed", CropPlotType.SMALL, BerryColor.AZUL);
		mejoBerrySeed = addSeedItem("mejoBerrySeed", CropPlotType.SMALL, BerryColor.MEJO);
		narcoBerrySeed = addSeedItem("narcoBerrySeed", CropPlotType.SMALL, BerryColor.NARCO);
		stimBerrySeed = addSeedItem("stimBerrySeed", CropPlotType.SMALL, BerryColor.STIM);

		// feces
		small_feces = addFecesItem("small_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_SMALL_FECES_TO_DECOMPOSE);
		medium_feces = addFecesItem("medium_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_SMALL_FECES_TO_DECOMPOSE);
		large_feces = addFecesItem("large_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_SMALL_FECES_TO_DECOMPOSE);
		player_feces = addFecesItem("player_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_PLAYER_FECES_TO_DECOMPOSE);

		// Technically not feces, but used in all situations the same
		// (currently)
		fertilizer = addFecesItem("fertilizer",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_FERTILIZER_TO_DECOMPOSE);

		info_book = addBook("info_book");

		tabItem = new Item().setUnlocalizedName("tabItem");
		registerItem("tabItem", tabItem);
		tabItem.setCreativeTab(null);
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

	public static ItemMetalSickle addMetalSickle(String name, ToolMaterial m)
	{
		ItemMetalSickle i = new ItemMetalSickle(m);
		registerItem(name, i);
		return i;
	}

	public static ARKCraftFeces addFecesItem(String name, int maxDamageIn)
	{
		ARKCraftFeces i = new ARKCraftFeces();
		i.setMaxDamage(maxDamageIn * 20);
		registerItem(name, i);
		return i;
	}

	public static ItemStoneHatchet addStoneHatchet(String name, ToolMaterial m)
	{
		ItemStoneHatchet i = new ItemStoneHatchet(m);
		registerItem(name, i);
		return i;
	}

	public static ItemSpear addSpear(String name, ToolMaterial m)
	{
		ItemSpear i = new ItemSpear(m);
		registerItem(name, i);
		return i;
	}

	public static ItemPike addPike(String name, ToolMaterial m)
	{
		ItemPike i = new ItemPike(m);
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

	protected static ARKCraftFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible)
	{
		ARKCraftFood f = new ARKCraftFood(heal, sat, fav, alwaysEdible, PLAYER.TICKS_BEFORE_FOOD_DECAY);
		registerItem(name, f);
		return f;
	}

	protected static ARKCraftFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible, int decayTime)
	{
		ARKCraftFood f = new ARKCraftFood(heal, sat, fav, alwaysEdible, decayTime);
		registerItem(name, f);
		return f;
	}

	public static ARKCraftFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible, int decayTime, PotionEffect... effect)
	{
		ARKCraftFood f = new ARKCraftFood(heal, sat, fav, alwaysEdible, decayTime, effect);
		registerItem(name, f);
		return f;
	}

	protected static ARKCraftSeed addSeedItem(String name, CropPlotType type, BerryColor color)
	{
		ARKCraftSeed i = new ARKCraftSeed(type, color);
		registerItem(name, i);
		return i;
	}

	public static ARKCraftBook addBook(String name)
	{
		ARKCraftBook book = new ARKCraftBook(name);
		registerItem(name, book);
		return book;
	}

	public static ItemARKArmor addArmorItem(String name, ArmorMaterial mat, String armorTexName, int type, boolean golden)
	{
		ItemARKArmor item = new ItemARKArmor(mat, armorTexName, type, golden);
		registerItem(name, item);
		return item;
	}

	public static void registerItem(String name, Item item)
	{
		allItems.put(name, item);
		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, name);
		item.setCreativeTab(ARKCraft.tabARK);
	}

}
