package com.arkcraft.registry;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.item.*;
import com.arkcraft.common.item.armor.ItemARKArmor;
import com.arkcraft.common.item.melee.ItemPike;
import com.arkcraft.common.item.melee.ItemSpear;
import com.arkcraft.common.item.tool.ItemHatchetMetal;
import com.arkcraft.common.item.tool.ItemHatchetStone;
import com.arkcraft.common.item.tool.ItemPickMetal;
import com.arkcraft.common.item.tool.ItemPickStone;
import com.arkcraft.common.item.tools.ItemMetalSickle;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ARKCraft.MODID)
public class ItemRegistry {
	//Tools
	@GameRegistry.ObjectHolder("metal_sickle")
	public static final ItemMetalSickle METAL_SICKLE = null;
	@GameRegistry.ObjectHolder("stone_pick")
	public static final ItemPickStone PICK_STONE = null;
	@GameRegistry.ObjectHolder("metal_pick")
	public static final ItemPickMetal PICK_METAL = null;
	@GameRegistry.ObjectHolder("stone_hatchet")
	public static final ItemHatchetStone HATCHET_STONE = null;
	@GameRegistry.ObjectHolder("metal_hatchet")
	public static final ItemHatchetMetal HATCHET_METAL = null;
	@GameRegistry.ObjectHolder("spy_glass")
	public static final ARKCraftItem SPY_GLASS = null;
	@GameRegistry.ObjectHolder("info_book")
	public static final ARKCraftBook INFO_BOOK = null;

	@GameRegistry.ObjectHolder("blueprint")
	public static final ItemBlueprint BLUEPRINT = null;

	//Armour
	@GameRegistry.ObjectHolder("chitin_helm")
	public static final ItemARKArmor CHITIN_HELM = null;
	@GameRegistry.ObjectHolder("chitin_chest")
	public static final ItemARKArmor CHITIN_CHESTPLATE = null;
	@GameRegistry.ObjectHolder("chitin_legs")
	public static final ItemARKArmor CHITIN_LEGGINGS = null;
	@GameRegistry.ObjectHolder("chitin_boots")
	public static final ItemARKArmor CHITIN_BOOTS = null;
	@GameRegistry.ObjectHolder("cloth_helm")
	public static final ItemARKArmor CLOTH_HELM = null;
	@GameRegistry.ObjectHolder("cloth_chest")
	public static final ItemARKArmor CLOTH_CHESTPLATE = null;
	@GameRegistry.ObjectHolder("cloth_legs")
	public static final ItemARKArmor CLOTH_LEGGINGS = null;
	@GameRegistry.ObjectHolder("cloth_boots")
	public static final ItemARKArmor CLOTH_BOOTS = null;
	@GameRegistry.ObjectHolder("hide_helm")
	public static final ItemARKArmor HIDE_HELM = null;
	@GameRegistry.ObjectHolder("hide_chest")
	public static final ItemARKArmor HIDE_CHESTPLATE = null;
	@GameRegistry.ObjectHolder("hide_legs")
	public static final ItemARKArmor HIDE_LEGGINGS = null;
	@GameRegistry.ObjectHolder("hide_boots")
	public static final ItemARKArmor HIDE_BOOTS = null;
	@GameRegistry.ObjectHolder("fur_helm")
	public static final ItemARKArmor FUR_HELM = null;
	@GameRegistry.ObjectHolder("fur_chest")
	public static final ItemARKArmor FUR_CHESTPLATE = null;
	@GameRegistry.ObjectHolder("fur_legs")
	public static final ItemARKArmor FUR_LEGGINGS = null;
	@GameRegistry.ObjectHolder("fur_boots")
	public static final ItemARKArmor FUR_BOOTS = null;
	@GameRegistry.ObjectHolder("flak_helm")
	public static final ItemARKArmor FLAK_HELM = null;
	@GameRegistry.ObjectHolder("flak_chest")
	public static final ItemARKArmor FLAK_CHESTPLATE = null;
	@GameRegistry.ObjectHolder("flak_legs")
	public static final ItemARKArmor FLAK_LEGGINGS = null;
	@GameRegistry.ObjectHolder("flak_boots")
	public static final ItemARKArmor FLAK_BOOTS = null;

	//Edibles
	@GameRegistry.ObjectHolder("tinto")
	public static final ARKCraftFood TINTO_BERRY = null;
	@GameRegistry.ObjectHolder("amar")
	public static final ARKCraftFood AMAR_BERRY = null;
	@GameRegistry.ObjectHolder("azul")
	public static final ARKCraftFood AZUL_BERRY = null;
	@GameRegistry.ObjectHolder("mejo")
	public static final ARKCraftFood MEJO_BERRY = null;
	@GameRegistry.ObjectHolder("narco")
	public static final ARKCraftFood NARCO_BERRY = null;
	@GameRegistry.ObjectHolder("stim")
	public static final ARKCraftFood STIM_BERRY = null;
	@GameRegistry.ObjectHolder("meat_raw")
	public static final ARKCraftFood RAW_MEAT = null;
	@GameRegistry.ObjectHolder("meat_cooked")
	public static final ARKCraftFood COOKED_MEAT = null;
	@GameRegistry.ObjectHolder("primemeat_raw")
	public static final ARKCraftFood RAW_PRIME_MEAT = null;
	@GameRegistry.ObjectHolder("primemeat_cooked")
	public static final ARKCraftFood COOKED_PRIME_MEAT = null;
	@GameRegistry.ObjectHolder("spoiled_meat")
	public static final ARKCraftFood SPOILED_MEAT = null;

	//Seeds
	@GameRegistry.ObjectHolder("tintoBerrySeed")
	public static final ARKCraftSeed TINTO_BERRY_SEED = null;
	@GameRegistry.ObjectHolder("amarBerrySeed")
	public static final ARKCraftSeed AMAR_BERRY_SEED = null;
	@GameRegistry.ObjectHolder("azulBerrySeed")
	public static final ARKCraftSeed AZUL_BERRY_SEED = null;
	@GameRegistry.ObjectHolder("mejoBerrySeed")
	public static final ARKCraftSeed MEJO_BERRY_SEED = null;
	@GameRegistry.ObjectHolder("narcoBerrySeed")
	public static final ARKCraftSeed NARCO_BERRY_SEED = null;
	@GameRegistry.ObjectHolder("stimBerrySeed")
	public static final ARKCraftSeed STIM_BERRY_SEED = null;

	//Hydration
	@GameRegistry.ObjectHolder("waterskin")
	public static final ItemWaterContainer WATER_SKIN = null;
	@GameRegistry.ObjectHolder("water_jar")
	public static final ItemWaterContainer WATER_JAR = null;

	//Resources
	@GameRegistry.ObjectHolder("stone")
	public static final ARKCraftItem STONE = null;
	@GameRegistry.ObjectHolder("fiber")
	public static final ARKCraftItem FIBER = null;
	@GameRegistry.ObjectHolder("thatch")
	public static final ARKCraftItem THATCH = null;
	@GameRegistry.ObjectHolder("wood")
	public static final ARKCraftItem WOOD = null;
	@GameRegistry.ObjectHolder("flint")
	public static final ARKCraftItem FLINT = null;
	@GameRegistry.ObjectHolder("metal")
	public static final ARKCraftItem METAL = null;
	@GameRegistry.ObjectHolder("spark_powder")
	public static final ARKCraftItem SPARK_POWDER = null;
	@GameRegistry.ObjectHolder("hide")
	public static final ARKCraftItem HIDE = null;
	@GameRegistry.ObjectHolder("charcoal")
	public static final ARKCraftItem CHARCOAL = null;
	@GameRegistry.ObjectHolder("crystal")
	public static final ARKCraftItem CRYSTAL = null;
	@GameRegistry.ObjectHolder("chitin")
	public static final ARKCraftItem CHITIN = null;
	@GameRegistry.ObjectHolder("keratin")
	public static final ARKCraftItem KERATIN = null;
	@GameRegistry.ObjectHolder("pelt")
	public static final ARKCraftItem PELT = null;
	@GameRegistry.ObjectHolder("obsidian")
	public static final ARKCraftItem OBSIDIAN = null;
	@GameRegistry.ObjectHolder("oil")
	public static final ARKCraftItem OIL = null;
	@GameRegistry.ObjectHolder("silica_pearls")
	public static final ARKCraftItem SILICA_PEARLS = null;
	@GameRegistry.ObjectHolder("polymer")
	public static final ARKCraftItem POLYMER = null;
	@GameRegistry.ObjectHolder("organic_polymer")
	public static final ARKCraftItem ORGANIC_POLYMER = null;
	@GameRegistry.ObjectHolder("sap")
	public static final ARKCraftItem SAP = null;
	@GameRegistry.ObjectHolder("absorbent_substrate")
	public static final ARKCraftItem ABSORBENT_SUBSTRATE = null;
	@GameRegistry.ObjectHolder("angler_gel")
	public static final ARKCraftItem ANGLER_GEL = null;
	@GameRegistry.ObjectHolder("black_pearl")
	public static final ARKCraftItem BLACK_PEARL = null;
	@GameRegistry.ObjectHolder("leech_blood")
	public static final ARKCraftItem LEECH_BLOOD = null;
	@GameRegistry.ObjectHolder("rare_flower")
	public static final ARKCraftItem RARE_FLOWER = null;
	@GameRegistry.ObjectHolder("rare_mushroom")
	public static final ARKCraftItem RARE_MUSHROOM = null;
	@GameRegistry.ObjectHolder("woolly_rhino_horn")
	public static final ARKCraftItem WOOLLY_RHINO_HORN = null;

	//Forged items
	@GameRegistry.ObjectHolder("metal_ingot")
	public static final ARKCraftItem METAL_INGOT = null;

	//Mixed items
	@GameRegistry.ObjectHolder("cementing_paste")
	public static final ARKCraftItem CEMENTING_PASTE = null;
	@GameRegistry.ObjectHolder("stimulant")
	public static final ARKCraftItem STIMULANT = null;
	@GameRegistry.ObjectHolder("narcotics")
	public static final ARKCraftItem NARCOTICS = null;
	@GameRegistry.ObjectHolder("gunpowder")
	public static final ARKCraftItem GUNPOWDER = null;
	@GameRegistry.ObjectHolder("refertilizer")
	public static final ARKCraftItem REFERTILIZER = null;
	@GameRegistry.ObjectHolder("fertilizer")
	public static final ItemFertilizer FERTILIZER = null;

	//Fabricated items
	@GameRegistry.ObjectHolder("gasoline")
	public static final ARKCraftItem GASOLINE = null;
	@GameRegistry.ObjectHolder("electronics")
	public static final ARKCraftItem ELECTRONICS = null;

	//Faeces
	@GameRegistry.ObjectHolder("small_feces")
	public static final ARKCraftFeces SMALL_FAECES = null;
	@GameRegistry.ObjectHolder("medium_feces")
	public static final ARKCraftFeces MEDIUM_FAECES = null;
	@GameRegistry.ObjectHolder("large_feces")
	public static final ARKCraftFeces LARGE_FAECES = null;
	@GameRegistry.ObjectHolder("human_feces")
	public static final ARKCraftFeces HUMAN_FAECES = null;

	//Miscellaneous
	@GameRegistry.ObjectHolder("blood_pack")
	public static final ARKCraftItem BLOOD_PACK = null;
}
