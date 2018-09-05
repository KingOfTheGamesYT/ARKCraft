package com.arkcraft.init;

import com.arkcraft.common.block.crafter.BlockCropPlot;
import com.arkcraft.common.config.ModuleItemBalance;
import com.arkcraft.common.engram.EngramManager;
import com.arkcraft.common.item.*;
import com.arkcraft.common.item.armor.ItemARKArmor;
import com.arkcraft.common.item.melee.ItemPike;
import com.arkcraft.common.item.melee.ItemSpear;
import com.arkcraft.common.item.tool.*;
import com.arkcraft.common.item.tools.ItemMetalSickle;
import com.arkcraft.common.tileentity.crafter.TileEntityCropPlot.CropPlotType;
import com.arkcraft.util.AbstractItemStack;
import com.arkcraft.util.CollectionUtil;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;

public class ARKCraftItems
{
	// Tools
	// TODO check sickle code
	public static ItemMetalSickle metal_sickle;
	public static ItemPickStone stonePick;
	public static ItemPickMetal metalPick;
	public static ItemHatchetStone stoneHatchet;
	public static ItemHatchetMetal metalHatchet;

	// Armor
	public static ItemARKArmor chitin_helm, chitin_chest, chitin_legs, chitin_boots;
	public static ItemARKArmor cloth_helm, cloth_chest, cloth_legs, cloth_boots;
	public static ItemARKArmor hide_helm, hide_chest, hide_legs, hide_boots;
	public static ItemARKArmor fur_helm, fur_chest, fur_legs, fur_boots;
	public static ItemARKArmor flak_helm, flak_chest, flak_legs, flak_boots;

	// Food
	public static ARKCraftFood tintoBerry, amarBerry, azulBerry, mejoBerry, narcoBerry, stimBerry, meat_raw,
	meat_cooked, primemeat_raw, primemeat_cooked, spoiled_meat;
	public static ARKCraftSeed tintoBerrySeed, amarBerrySeed, azulBerrySeed, mejoBerrySeed, narcoBerrySeed,
	stimBerrySeed;
	public static ItemWaterContainer water_skin, water_jar;

	// Misc
	public static ARKCraftItem stone, fiber, thatch, wood, flint, metal, spark_powder, hide, charcoal, metal_ingot,
	cementing_paste, crystal, spy_glass, narcotics, gunpowder, chitin, keratin, pelt, obsidian, oil, gasoline,
	stimulant, silica_pearls, polymer, organic_polymer, sap, electronics, blood_pack, absorbent_substrate,
	angler_gel, black_pearl, leech_blood, rare_flower, rare_mushroom, refertilizer, woolly_rhino_horn;

	public static ARKCraftFeces small_feces, medium_feces, large_feces, player_feces;
	public static ItemFertilizer fertilizer;
	public static ARKCraftBook info_book;

	public static ItemBlueprint blueprint;

	public static ItemSpear spear;
	public static ItemPike pike;
	public static Item tabItem;

	// Armor MAT
	public static ArmorMaterial CLOTH = EnumHelper.addArmorMaterial("CLOTH_MAT", "CLOTH_MAT", 4, new int[] { 1, 2, 1,
			1 }, 15, SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_leather")), 0);
	public static ArmorMaterial CHITIN = EnumHelper.addArmorMaterial("CHITIN_MAT", "CHITIN_MAT", 16, new int[] { 3, 7,
			6, 3 }, 10, SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_leather")), 0);
	public static ArmorMaterial HIDE = EnumHelper.addArmorMaterial("HIDE_MAT", "HIDE_MAT", 40, new int[] { 3, 8, 6, 3 },
			30, SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_leather")), 0);
	public static ArmorMaterial FUR = EnumHelper.addArmorMaterial("FUR_MAT", "HIDE_MAT", 40, new int[] { 3, 8, 6, 3 },
			30, SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_leather")), 0);
	public static ArmorMaterial FLAK = EnumHelper.addArmorMaterial("FLAK_MAT", "FLAK_MAT", 60, new int[] { 12, 40, 30,
			15 }, 30, SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_leather")), 0);//TODO: replace toughness values

	// Tool MAT
	public static ToolMaterial METAL = EnumHelper.addToolMaterial("METAL_MAT", 3, 1500, 6.0F, 2.5F, 8);
	public static ToolMaterial STONE = EnumHelper.addToolMaterial("STONE_MAT", 2, 500, 3.5F, 1.5F, 13);
	public static ToolMaterial WOOD = EnumHelper.addToolMaterial("WOOD_MAT", 1, 200, 2.5F, 1.0F, 3);

	public static void init()
	{
		InitializationManager init = InitializationManager.instance();

		// Resources
		cementing_paste = addItem("cementing_paste");
		crystal = addItem("crystal");
		hide = addItem("hide");
		charcoal = addItem("charcoal");
		metal_ingot = addItem("metal_ingot");
		stone = addItem("stone");
		fiber = addItem("fiber");
		thatch = addFuel("thatch");
		wood = addFuel("wood");
		flint = addItem("flint");
		metal = addItem("metal");
		spark_powder = addItem("spark_powder");
		spy_glass = addItem("spy_glass");
		narcotics = addItem("narcotics");
		gunpowder = addItem("gunpowder");
		chitin = addItem("chitin");
		keratin = addItem("keratin");
		pelt = addItem("pelt");
		obsidian = addItem("obsidian");
		oil = addItem("oil");
		gasoline = addFuel("gasoline");
		stimulant = addItem("stimulant");
		silica_pearls = addItem("silica_pearls");
		polymer = addItem("polymer");
		organic_polymer = addItem("organic_polymer");
		sap = addItem("sap");
		electronics = addItem("electronics");
		blood_pack = addItem("blood_pack");
		absorbent_substrate = addItem("absorbent_substrate");
		angler_gel = addItem("angler_gel");
		black_pearl = addItem("black_pearl");
		leech_blood = addItem("leech_blood");
		rare_flower = addItem("rare_flower");
		rare_mushroom = addItem("rare_mushroom");
		refertilizer = addItem("refertilizer");
		woolly_rhino_horn = addItem("woolly_rhino_horn");

		// Tools
		metalPick = init.registerItem("metal_pick", new ItemPickMetal());
		stonePick = init.registerItem("stone_pick", new ItemPickStone());
		metalHatchet = init.registerItem("metal_hatchet", new ItemHatchetMetal());
		stoneHatchet = init.registerItem("stone_hatchet", new ItemHatchetStone());
		metal_sickle = init.registerItem("metal_sickle", new ItemMetalSickle(METAL));

		water_skin = init.registerItem("waterskin", new ItemLeakingWaterContainer(100));
		water_jar = init.registerItem("water_jar", new ItemWaterContainer(200));

		// Weapons
		spear = init.registerItem("spear", new ItemSpear(WOOD));
		pike = init.registerItem("pike", new ItemPike(METAL));

		// Armor
		chitin_helm = addArmorItem("chitin_helm", CHITIN, "chitinArmor", EntityEquipmentSlot.HEAD, false);
		chitin_chest = addArmorItem("chitin_chest", CHITIN, "chitinArmor", EntityEquipmentSlot.CHEST, false);
		chitin_legs = addArmorItem("chitin_legs", CHITIN, "chitinArmor", EntityEquipmentSlot.LEGS, false);
		chitin_boots = addArmorItem("chitin_boots", CHITIN, "chitinArmor", EntityEquipmentSlot.FEET, false);

		cloth_helm = addArmorItem("cloth_helm", CLOTH, "clothArmor", EntityEquipmentSlot.HEAD, false);
		cloth_chest = addArmorItem("cloth_chest", CLOTH, "clothArmor", EntityEquipmentSlot.CHEST, false);
		cloth_legs = addArmorItem("cloth_legs", CLOTH, "clothArmor", EntityEquipmentSlot.LEGS, false);
		cloth_boots = addArmorItem("cloth_boots", CLOTH, "clothArmor", EntityEquipmentSlot.FEET, false);

		hide_helm = addArmorItem("hide_helm", HIDE, "hideArmor", EntityEquipmentSlot.HEAD, false);
		hide_chest = addArmorItem("hide_chest", HIDE, "hideArmor", EntityEquipmentSlot.CHEST, false);
		hide_legs = addArmorItem("hide_legs", HIDE, "hideArmor", EntityEquipmentSlot.LEGS, false);
		hide_boots = addArmorItem("hide_boots", HIDE, "hideArmor", EntityEquipmentSlot.FEET, false);

		fur_helm = addArmorItem("fur_helm", FUR, "furArmor", EntityEquipmentSlot.HEAD, false);
		fur_chest = addArmorItem("fur_chest", FUR, "furArmor", EntityEquipmentSlot.CHEST, false);
		fur_legs = addArmorItem("fur_legs", FUR, "furArmor", EntityEquipmentSlot.LEGS, false);
		fur_boots = addArmorItem("fur_boots", FUR, "furArmor", EntityEquipmentSlot.FEET, false);

		flak_helm = addArmorItem("flak_helm", FLAK, "flakArmor", EntityEquipmentSlot.HEAD, false);
		flak_chest = addArmorItem("flak_chest", FLAK, "flakArmor", EntityEquipmentSlot.CHEST, false);
		flak_legs = addArmorItem("flak_legs", FLAK, "flakArmor", EntityEquipmentSlot.LEGS, false);
		flak_boots = addArmorItem("flak_boots", FLAK, "flakArmor", EntityEquipmentSlot.FEET, false);

		// Food
		tintoBerry = addBerry("tinto", 1, 0.1F, false, true);
		amarBerry = addBerry("amar", 1, 0.1F, false, true);
		azulBerry = addBerry("azul", 1, 0.1F, false, true);
		mejoBerry = addBerry("mejo", 1, 0.1F, false, true);
		narcoBerry = addBerry("narco", 1, 0.1F, true, true);
		stimBerry = addBerry("stim", 1, 0.1F, true, true);
		meat_raw = addFood("meat_raw", 3, 0.3F, false, false);
		meat_cooked = addFood("meat_cooked", 6, 0.9F, false, false);
		primemeat_raw = addFood("primemeat_raw", 3, 0.3F, false, false);
		primemeat_cooked = addFood("primemeat_cooked", 8, 1.2F, false, false);
		spoiled_meat = addFood("spoiled_meat", 2, 0.1F, false, false);

		// Seeds
		tintoBerrySeed = addSeedItem("tintoBerrySeed", CropPlotType.SMALL, BlockCropPlot.BerryColor.TINTO);
		amarBerrySeed = addSeedItem("amarBerrySeed", CropPlotType.SMALL, BlockCropPlot.BerryColor.AMAR);
		azulBerrySeed = addSeedItem("azulBerrySeed", CropPlotType.SMALL, BlockCropPlot.BerryColor.AZUL);
		mejoBerrySeed = addSeedItem("mejoBerrySeed", CropPlotType.SMALL, BlockCropPlot.BerryColor.MEJO);
		narcoBerrySeed = addSeedItem("narcoBerrySeed", CropPlotType.SMALL, BlockCropPlot.BerryColor.NARCO);
		stimBerrySeed = addSeedItem("stimBerrySeed", CropPlotType.SMALL, BlockCropPlot.BerryColor.STIM);

		// feces
		small_feces = init.registerItem("small_feces", new ARKCraftFeces(1600, 50000));
		medium_feces = init.registerItem("medium_feces", new ARKCraftFeces(1600,  100000));
		large_feces = init.registerItem("large_feces", new ARKCraftFeces(1600, 200000));
		player_feces = init.registerItem("human_feces", new ARKCraftFeces(1600, 50000));

		// fertilizer
		fertilizer = init.registerItem("fertilizer", new ItemFertilizer(54000));

		info_book = init.registerItem("info_book", new ARKCraftBook());
		tabItem = init.registerItem("tabItem", new Item());

		// Effectiveness register
		ItemToolBase.registerEffectiveBlocks(Blocks.LOG, Blocks.LOG2, ARKCraftBlocks.rockResource,
				ARKCraftBlocks.oilResource, ARKCraftBlocks.metalResource, ARKCraftBlocks.obsidianResource,
				ARKCraftBlocks.crystalResource);
		ItemToolBase.registerBlockDrops(Blocks.LOG, Lists.newArrayList(new AbstractItemStack(
				wood, 2), new AbstractItemStack(thatch, 2)));
		ItemToolBase.registerBlockDrops(Blocks.LOG2, Lists.newArrayList(new AbstractItemStack(
				wood, 2), new AbstractItemStack(thatch, 2)));
	}

	private static ItemFuel addFuel(String name)
	{
		return InitializationManager.instance().registerItem(name, new ItemFuel());
	}

	public static void initBlueprints()
	{
		blueprint = InitializationManager.instance().registerItem("blueprint", "blueprint/", new ItemBlueprint(), false,
				CollectionUtil.convert(EngramManager.instance().getBlueprintEngrams(), (EngramManager.Engram e) -> e.getName())
				.toArray(new String[0]));
	}

	public static ARKCraftItem addItem(String name)
	{
		return InitializationManager.instance().registerItem(name, new ARKCraftItem());
	}

	protected static ARKCraftFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible)
	{
		return InitializationManager.instance().registerItem(name, new ARKCraftFood(heal, sat, fav, alwaysEdible,
				ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY));
	}

	private static ItemBerry addBerry(String name, int heal, float sat, boolean fav, boolean alwaysEdible)
	{
		return InitializationManager.instance().registerItem(name, new ItemBerry(heal, sat, fav, alwaysEdible,
				ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY));
	}

	protected static ARKCraftSeed addSeedItem(String name, CropPlotType type, BlockCropPlot.BerryColor color)
	{
		return InitializationManager.instance().registerItem(name, new ARKCraftSeed(type, color));
	}

	public static ItemARKArmor addArmorItem(String name, ArmorMaterial mat, String armorTexName, EntityEquipmentSlot type,
			boolean golden)
	{
		return InitializationManager.instance().registerItem(name, new ItemARKArmor(mat, armorTexName, type, golden));
	}
}
