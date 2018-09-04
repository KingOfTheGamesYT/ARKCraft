package com.arkcraft.registry;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.block.BlockBerryBush;
import com.arkcraft.common.block.BlockGreenScreen;
import com.arkcraft.common.block.BlockLight;
import com.arkcraft.common.block.crafter.*;
import com.arkcraft.common.block.energy.BlockCable;
import com.arkcraft.common.block.energy.BlockCreativeGenerator;
import com.arkcraft.common.block.energy.BlockElectricLamp;
import com.arkcraft.common.block.energy.BlockElectricOutlet;
import com.arkcraft.common.block.resource.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ARKCraft.MODID)
public class BlockRegistry {
	//Utility blocks
	@GameRegistry.ObjectHolder("light")
	public static final BlockLight LIGHT = null;
	@GameRegistry.ObjectHolder("green_screen")
	public static final BlockGreenScreen GREEN_SCREEN = null;

	//Resource blocks
	@GameRegistry.ObjectHolder("berry_bush")
	public static final BlockBerryBush BERRY_BUSH = null;
	@GameRegistry.ObjectHolder("rock_resource")
	public static final BlockRockResource ROCK_RESOURCE = null;
	@GameRegistry.ObjectHolder("metal_resource")
	public static final BlockMetalResource METAL_RESOURCE = null;
	@GameRegistry.ObjectHolder("obsidian_resource")
	public static final BlockObsidianResource OBSIDIAN_RESOURCE = null;
	@GameRegistry.ObjectHolder("crystal_resource")
	public static final BlockCrystalResource CRYSTAL_RESOURCE = null;
	@GameRegistry.ObjectHolder("oil_resource")
	public static final BlockOilResource OIL_RESOURCE = null;
	@GameRegistry.ObjectHolder("small_rock_resource")
	public static final BlockSmallRockResource SMALL_ROCK_RESOURCE = null;

	//Machine blocks
	@GameRegistry.ObjectHolder("compost_bin")
	public static final BlockCompostBin COMPOST_BIN = null;
	@GameRegistry.ObjectHolder("smithy")
	public static final BlockSmithy SMITHY = null;
	@GameRegistry.ObjectHolder("mortar_and_pestle")
	public static final BlockMortarAndPestle MORTAR_AND_PESTLE = null;
	@GameRegistry.ObjectHolder("crop_plot")
	public static final BlockCropPlot CROP_PLOT = null;
	@GameRegistry.ObjectHolder("refining_forge")
	public static final BlockRefiningForge REFINING_FORGE = null;
	@GameRegistry.ObjectHolder("campfire")
	public static final BlockCampfire CAMPFIRE = null;
	@GameRegistry.ObjectHolder("fabricator")
	public static final BlockFabricator FABRICATOR = null;

	//Power blocks
	@GameRegistry.ObjectHolder("cable")
	public static final BlockCable CABLE = null;
	@GameRegistry.ObjectHolder("creative_generator")
	public static final BlockCreativeGenerator CREATIVE_GENERATOR = null;
	@GameRegistry.ObjectHolder("electric_outlet")
	public static final BlockElectricLamp ELECTRIC_LAMP = null;
	@GameRegistry.ObjectHolder("electric_lamp")
	public static final BlockElectricOutlet ELECTRIC_OUTLET = null;
}
