package com.uberverse.arkcraft.init;

import com.google.common.collect.Lists;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.crafter.BlockCropPlot.BerryColor;
import com.uberverse.arkcraft.common.config.ModuleItemBalance.PLAYER;
import com.uberverse.arkcraft.common.engram.EngramManager;
import com.uberverse.arkcraft.common.engram.EngramManager.Engram;
import com.uberverse.arkcraft.common.entity.EntitySpear;
import com.uberverse.arkcraft.common.handlers.EntityHandler;
import com.uberverse.arkcraft.common.item.ARKCraftBook;
import com.uberverse.arkcraft.common.item.ARKCraftFeces;
import com.uberverse.arkcraft.common.item.ARKCraftFood;
import com.uberverse.arkcraft.common.item.ARKCraftItem;
import com.uberverse.arkcraft.common.item.ARKCraftSeed;
import com.uberverse.arkcraft.common.item.ItemBerry;
import com.uberverse.arkcraft.common.item.ItemBlueprint;
import com.uberverse.arkcraft.common.item.ItemFertilizer;
import com.uberverse.arkcraft.common.item.ItemFuel;
import com.uberverse.arkcraft.common.item.armor.ItemARKArmor;
import com.uberverse.arkcraft.common.item.melee.ItemPike;
import com.uberverse.arkcraft.common.item.melee.ItemSpear;
import com.uberverse.arkcraft.common.item.tool.ItemHatchetMetal;
import com.uberverse.arkcraft.common.item.tool.ItemHatchetStone;
import com.uberverse.arkcraft.common.item.tool.ItemPickMetal;
import com.uberverse.arkcraft.common.item.tool.ItemPickStone;
import com.uberverse.arkcraft.common.item.tool.ItemToolBase;
import com.uberverse.arkcraft.common.item.tools.ItemMetalSickle;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot.CropPlotType;
import com.uberverse.arkcraft.util.AbstractItemStack;
import com.uberverse.arkcraft.util.CollectionUtil;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
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

	// Misc
	public static ARKCraftItem stone, fiber, thatch, wood, flint, metal, spark_powder, hide, charcoal, metal_ingot,
			cementing_paste, crystal, spy_glass, narcotics, gunpowder, chitin, keratin, pelt, obsidian, oil, gasoline;
	public static ARKCraftFeces small_feces, medium_feces, large_feces, player_feces;
	public static ItemFertilizer fertilizer;
	public static ARKCraftBook info_book;

	public static ItemBlueprint blueprint;

	public static ItemSpear spear;
	public static ItemPike pike;
	public static Item tabItem;

	// Armor MAT
	public static ArmorMaterial CLOTH = EnumHelper.addArmorMaterial("CLOTH_MAT", "CLOTH_MAT", 4, new int[] { 1, 2, 1,
			1 }, 15);
	public static ArmorMaterial CHITIN = EnumHelper.addArmorMaterial("CHITIN_MAT", "CHITIN_MAT", 16, new int[] { 3, 7,
			6, 3 }, 10);
	public static ArmorMaterial HIDE = EnumHelper.addArmorMaterial("HIDE_MAT", "HIDE_MAT", 40, new int[] { 3, 8, 6, 3 },
			30);
	public static ArmorMaterial FUR = EnumHelper.addArmorMaterial("FUR_MAT", "HIDE_MAT", 40, new int[] { 3, 8, 6, 3 },
			30);
	public static ArmorMaterial FLAK = EnumHelper.addArmorMaterial("FLAK_MAT", "FLAK_MAT", 60, new int[] {12, 40, 30, 15}, 30);

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

		// Tools
		metalPick = init.registerItem("metal_pick", new ItemPickMetal());
		stonePick = init.registerItem("stone_pick", new ItemPickStone());
		metalHatchet = init.registerItem("metal_hatchet", new ItemHatchetMetal());
		stoneHatchet = init.registerItem("stone_hatchet", new ItemHatchetStone());
		metal_sickle = init.registerItem("metal_sickle", new ItemMetalSickle(METAL));

		// Weapons
		spear = init.registerItem("spear", new ItemSpear(WOOD));
		EntityHandler.registerModEntity(EntitySpear.class, "spear", ARKCraft.instance(), 16, 20, true);
		pike = init.registerItem("pike", new ItemPike(METAL));

		// Armor
		chitin_helm = addArmorItem("chitin_helm", CHITIN, "chitinArmor", 0, false);
		chitin_chest = addArmorItem("chitin_chest", CHITIN, "chitinArmor", 1, false);
		chitin_legs = addArmorItem("chitin_legs", CHITIN, "chitinArmor", 2, false);
		chitin_boots = addArmorItem("chitin_boots", CHITIN, "chitinArmor", 3, false);

		cloth_helm = addArmorItem("cloth_helm", CLOTH, "clothArmor", 0, false);
		cloth_chest = addArmorItem("cloth_chest", CLOTH, "clothArmor", 1, false);
		cloth_legs = addArmorItem("cloth_legs", CLOTH, "clothArmor", 2, false);
		cloth_boots = addArmorItem("cloth_boots", CLOTH, "clothArmor", 3, false);

		hide_helm = addArmorItem("hide_helm", HIDE, "hideArmor", 0, false);
		hide_chest = addArmorItem("hide_chest", HIDE, "hideArmor", 1, false);
		hide_legs = addArmorItem("hide_legs", HIDE, "hideArmor", 2, false);
		hide_boots = addArmorItem("hide_boots", HIDE, "hideArmor", 3, false);

		fur_helm = addArmorItem("fur_helm", FUR, "furArmor", 0, false);
		fur_chest = addArmorItem("fur_chest", FUR, "furArmor", 1, false);
		fur_legs = addArmorItem("fur_legs", FUR, "furArmor", 2, false);
		fur_boots = addArmorItem("fur_boots", FUR, "furArmor", 3, false);
		
		flak_helm = addArmorItem("flak_helm", FLAK, "flakAmor", 0, false);
		flak_chest = addArmorItem("flak_chest", FLAK, "flakArmor", 1, false);
		flak_legs = addArmorItem("flak_legs", FLAK, "flakArmor", 2, false);
		flak_boots = addArmorItem("flak_boots", FLAK, "flakArmor", 3, false);

		// Food
		tintoBerry = addBerry("tinto", 4, 0.3F, false, true);
		amarBerry = addBerry("amar", 4, 0.3F, false, true);
		azulBerry = addBerry("azul", 4, 0.3F, false, true);
		mejoBerry = addBerry("mejo", 4, 0.3F, false, true);
		narcoBerry = addBerry("narco", 4, 0.3F, true, true);
		stimBerry = addBerry("stim", 4, 0.3F, true, true);
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
		small_feces = init.registerItem("small_feces", new ARKCraftFeces(1600, 1600));
		medium_feces = init.registerItem("medium_feces", new ARKCraftFeces(1600, 1600));
		large_feces = init.registerItem("large_feces", new ARKCraftFeces(1600, 1600));
		player_feces = init.registerItem("human_feces", new ARKCraftFeces(1600, 1600));

		// fertilizer
		fertilizer = init.registerItem("fertilizer", new ItemFertilizer(54000));

		info_book = init.registerItem("info_book", new ARKCraftBook("info_book"));
		tabItem = init.registerItem("tabItem", new Item());

		// Effectiveness register
		ItemToolBase.registerEffectiveBlocks(Blocks.log, Blocks.log2, ARKCraftBlocks.rockResource,
				ARKCraftBlocks.oilResource, ARKCraftBlocks.metalResource, ARKCraftBlocks.obsidianResource,
				ARKCraftBlocks.crystalResource);
		ItemToolBase.registerBlockDrops(Blocks.log, Lists.newArrayList(new AbstractItemStack[] { new AbstractItemStack(
				wood, 2), new AbstractItemStack(thatch, 2) }));
		ItemToolBase.registerBlockDrops(Blocks.log2, Lists.newArrayList(new AbstractItemStack[] { new AbstractItemStack(
				wood, 2), new AbstractItemStack(thatch, 2) }));
	}

	private static ItemFuel addFuel(String name)
	{
		return InitializationManager.instance().registerItem(name, new ItemFuel(name));
	}

	public static void initBlueprints()
	{
		blueprint = InitializationManager.instance().registerItem("blueprint", "blueprint/", new ItemBlueprint(), false,
				CollectionUtil.convert(EngramManager.instance().getBlueprintEngrams(), (Engram e) -> e.getName())
						.toArray(new String[0]));
	}

	public static ARKCraftItem addItem(String name)
	{
		return InitializationManager.instance().registerItem(name, new ARKCraftItem());
	}

	protected static ARKCraftFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible)
	{
		return InitializationManager.instance().registerItem(name, new ARKCraftFood(heal, sat, fav, alwaysEdible,
				PLAYER.SECONDS_BEFORE_FOOD_DECAY));
	}

	private static ItemBerry addBerry(String name, int heal, float sat, boolean fav, boolean alwaysEdible)
	{
		return InitializationManager.instance().registerItem(name, new ItemBerry(heal, sat, fav, alwaysEdible,
				PLAYER.SECONDS_BEFORE_FOOD_DECAY));
	}

	protected static ARKCraftSeed addSeedItem(String name, CropPlotType type, BerryColor color)
	{
		return InitializationManager.instance().registerItem(name, new ARKCraftSeed(type, color));
	}

	public static ItemARKArmor addArmorItem(String name, ArmorMaterial mat, String armorTexName, int type,
			boolean golden)
	{
		return InitializationManager.instance().registerItem(name, new ItemARKArmor(mat, armorTexName, type, golden));
	}
}
