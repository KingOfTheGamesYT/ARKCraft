package com.arkcraft.registry;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.block.crafter.BlockCropPlot;
import com.arkcraft.common.config.ModuleItemBalance;
import com.arkcraft.common.item.*;
import com.arkcraft.common.item.ammo.ItemProjectile;
import com.arkcraft.common.item.armor.ItemARKArmor;
import com.arkcraft.common.item.attachments.AttachmentType;
import com.arkcraft.common.item.attachments.ItemAttachment;
import com.arkcraft.common.item.explosives.ItemRocketLauncher;
import com.arkcraft.common.item.melee.ItemPike;
import com.arkcraft.common.item.melee.ItemSpear;
import com.arkcraft.common.item.ranged.*;
import com.arkcraft.common.item.tool.ItemHatchetMetal;
import com.arkcraft.common.item.tool.ItemHatchetStone;
import com.arkcraft.common.item.tool.ItemPickMetal;
import com.arkcraft.common.item.tool.ItemPickStone;
import com.arkcraft.common.item.tools.ItemMetalSickle;
import com.arkcraft.common.tileentity.crafter.TileEntityCropPlot;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = ARKCraft.MODID)
public class ItemRegistryHandler {
	@SubscribeEvent
	public static void handleItemRegistry(RegistryEvent.Register<Item> registerEvent) {
		IForgeRegistry<Item> registry = registerEvent.getRegistry();

		registry.registerAll(
				genericItem("cementing_paste"),
				genericItem("crystal"),
				genericItem("hide"),
				genericItem("charcoal"),
				genericItem("metal_ingot"),
				genericItem("stone"),
				genericItem("fiber"),
				customItem("thatch", new ItemFuel()),
				customItem("wood", new ItemFuel()),
				genericItem("flint"),
				genericItem("metal"),
				genericItem("spark_powder"),
				genericItem("spy_glass"),
				genericItem("narcotics"),
				genericItem("gunpowder"),
				genericItem("chitin"),
				genericItem("keratin"),
				genericItem("pelt"),
				genericItem("obsidian"),
				genericItem("oil"),
				customItem("gasoline", new ItemFuel()),
				genericItem("stimulant"),
				genericItem("silica_pearls"),
				genericItem("polymer"),
				genericItem("organic_polymer"),
				genericItem("sap"),
				genericItem("electronics"),
				genericItem("blood_pack"),
				genericItem("absorbent_substrate"),
				genericItem("angler_gel"),
				genericItem("black_pearl"),
				genericItem("leech_blood"),
				genericItem("rare_flower"),
				genericItem("rare_mushroom"),
				genericItem("refertilizer"),
				genericItem("woolly_rhino_horn"),
				customItem("metal_pick", new ItemPickMetal()),
				customItem("stone_pick", new ItemPickStone()),
				customItem("metal_hatchet", new ItemHatchetMetal()),
				customItem("stone_hatchet", new ItemHatchetStone()),
				customItem("metal_sickle", new ItemMetalSickle(null)),//TODO toolmaterial
				customItem("spear", new ItemSpear(null)),//TODO toolmaterial
				customItem("pike", new ItemPike(null)),//TODO toolmaterial --> rework items to not require these
				customItem("waterskin", new ItemLeakingWaterContainer(100)),
				customItem("water_jar", new ItemWaterContainer(200)),
				customItem("chitin_helm", new ItemARKArmor(null, "chitinArmor", EntityEquipmentSlot.HEAD, false)), //TODO armor material
				customItem("chitin_chest", new ItemARKArmor(null, "chitinArmor", EntityEquipmentSlot.CHEST, false)), //TODO armor material
				customItem("chitin_legs", new ItemARKArmor(null, "chitinArmor", EntityEquipmentSlot.LEGS, false)), //TODO armor material
				customItem("chitin_boots", new ItemARKArmor(null, "chitinArmor", EntityEquipmentSlot.FEET, false)), //TODO armor material
				customItem("cloth_helm", new ItemARKArmor(null, "clothArmor", EntityEquipmentSlot.HEAD, false)), //TODO armor material
				customItem("cloth_chest", new ItemARKArmor(null, "clothArmor", EntityEquipmentSlot.CHEST, false)), //TODO armor material
				customItem("cloth_legs", new ItemARKArmor(null, "clothArmor", EntityEquipmentSlot.LEGS, false)), //TODO armor material
				customItem("cloth_boots", new ItemARKArmor(null, "clothArmor", EntityEquipmentSlot.FEET, false)), //TODO armor material
				customItem("hide_helm", new ItemARKArmor(null, "hideArmor", EntityEquipmentSlot.HEAD, false)), //TODO armor material
				customItem("hide_chest", new ItemARKArmor(null, "hideArmor", EntityEquipmentSlot.CHEST, false)), //TODO armor material
				customItem("hide_legs", new ItemARKArmor(null, "hideArmor", EntityEquipmentSlot.LEGS, false)), //TODO armor material
				customItem("hide_boots", new ItemARKArmor(null, "hideArmor", EntityEquipmentSlot.FEET, false)), //TODO armor material
				customItem("fur_helm", new ItemARKArmor(null, "furArmor", EntityEquipmentSlot.HEAD, false)), //TODO armor material
				customItem("fur_chest", new ItemARKArmor(null, "furArmor", EntityEquipmentSlot.CHEST, false)), //TODO armor material
				customItem("fur_legs", new ItemARKArmor(null, "furArmor", EntityEquipmentSlot.LEGS, false)), //TODO armor material
				customItem("fur_boots", new ItemARKArmor(null, "furArmor", EntityEquipmentSlot.FEET, false)), //TODO armor material
				customItem("flak_helm", new ItemARKArmor(null, "flakArmor", EntityEquipmentSlot.HEAD, false)), //TODO armor material
				customItem("flak_chest", new ItemARKArmor(null, "flakArmor", EntityEquipmentSlot.CHEST, false)), //TODO armor material
				customItem("flak_legs", new ItemARKArmor(null, "flakArmor", EntityEquipmentSlot.LEGS, false)), //TODO armor material
				customItem("flak_boots", new ItemARKArmor(null, "flakArmor", EntityEquipmentSlot.FEET, false)), //TODO armor material
				customItem("tinto", new ItemBerry(1, 0.1F, false, true, ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY)),
				customItem("amar", new ItemBerry(1, 0.1F, false, true, ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY)),
				customItem("azul", new ItemBerry(1, 0.1F, false, true, ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY)),
				customItem("mejo", new ItemBerry(1, 0.1F, false, true, ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY)),
				customItem("narco", new ItemBerry(1, 0.1F, true, true, ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY)),
				customItem("stim", new ItemBerry(1, 0.1F, true, true, ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY)),
				customItem("meat_raw", new ARKCraftFood(3, 0.3F, false, false, ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY)),
				customItem("meat_cooked", new ARKCraftFood(6, 0.9F, false, false, ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY)),
				customItem("primemeat_raw", new ARKCraftFood(3, 0.3F, false, false, ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY)),
				customItem("primemeat_cooked", new ARKCraftFood(8, 1.2F, false, false, ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY)),
				customItem("spoiled_meat", new ARKCraftFood(2, 0.1F, false, false, ModuleItemBalance.PLAYER.SECONDS_BEFORE_FOOD_DECAY)),
				customItem("tintoBerrySeed", new ARKCraftSeed(TileEntityCropPlot.CropPlotType.SMALL, BlockCropPlot.BerryColor.TINTO)),
				customItem("amarBerrySeed", new ARKCraftSeed(TileEntityCropPlot.CropPlotType.SMALL, BlockCropPlot.BerryColor.AMAR)),
				customItem("azulBerrySeed", new ARKCraftSeed(TileEntityCropPlot.CropPlotType.SMALL, BlockCropPlot.BerryColor.AZUL)),
				customItem("mejoBerrySeed", new ARKCraftSeed(TileEntityCropPlot.CropPlotType.SMALL, BlockCropPlot.BerryColor.MEJO)),
				customItem("narcoBerrySeed", new ARKCraftSeed(TileEntityCropPlot.CropPlotType.SMALL, BlockCropPlot.BerryColor.NARCO)),
				customItem("stimBerrySeed", new ARKCraftSeed(TileEntityCropPlot.CropPlotType.SMALL, BlockCropPlot.BerryColor.STIM)),
				customItem("small_feces", new ARKCraftFeces(1600, 50000)),
				customItem("medium_feces", new ARKCraftFeces(1600, 100000)),
				customItem("large_feces", new ARKCraftFeces(1600, 200000)),
				customItem("human_feces", new ARKCraftFeces(1600, 50000)),
				customItem("fertilizer", new ItemFertilizer(54000)),
				customItem("info_book", new ARKCraftBook()),
				genericItem("tabItem"),
				customItem("blueprint", new ItemBlueprint()),
				customItem("scope", new ItemAttachment(AttachmentType.SCOPE)),
				customItem("flash_light", new ItemAttachment(AttachmentType.FLASH)),
				customItem("holo_scope", new ItemAttachment(AttachmentType.HOLO_SCOPE)),
				customItem("laser", new ItemAttachment(AttachmentType.LASER)),
				customItem("silencer", new ItemAttachment(AttachmentType.SILENCER)),
				customItem("slingshot", new ItemSlingshot()),
				//TODO refactor for config enabling
				customItem("simple_rifle_ammo", new ItemProjectile()),
				customItem("tranquilizer", new ItemProjectile()),
				customItem("longneck_rifle", new ItemLongneckRifle()),
				customItem("shotgun", new ItemShotgun()),
				customItem("simple_shotgun_ammo", new ItemProjectile()),
				customItem("simple_pistol", new ItemSimplePistol()),
				customItem("simple_bullet", new ItemProjectile()),
				customItem("rocket_launcher", new ItemRocketLauncher()),
				customItem("rocket_propelled_grenade", new ItemProjectile()),
				customItem("fabricated_pistol", new ItemFabricatedPistol()),
				customItem("advanced_bullet", new ItemProjectile())
		);
	}

	private static Item customItem(String name, Item item) {
		return item.setRegistryName(new ResourceLocation(ARKCraft.MODID, name)).setTranslationKey(name);
	}

	private static Item genericItem(String name) {
		return new Item().setRegistryName(new ResourceLocation(ARKCraft.MODID, name)).setTranslationKey(name);
	}
}
