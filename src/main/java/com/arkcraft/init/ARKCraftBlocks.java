package com.arkcraft.init;

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
import com.arkcraft.common.item.itemblock.*;
import com.arkcraft.common.tileentity.TileEntityCrystal;
import com.arkcraft.common.tileentity.crafter.TileEntityCompostBin;
import com.arkcraft.common.tileentity.crafter.TileEntityCropPlot;
import com.arkcraft.common.tileentity.crafter.burner.TileEntityCampfire;
import com.arkcraft.common.tileentity.crafter.burner.TileEntityRefiningForge;
import com.arkcraft.common.tileentity.crafter.engram.TileEntityFabricator;
import com.arkcraft.common.tileentity.crafter.engram.TileEntityMP;
import com.arkcraft.common.tileentity.crafter.engram.TileEntitySmithy;
import com.arkcraft.common.tileentity.energy.TileEntityCable;
import com.arkcraft.common.tileentity.energy.TileEntityCreativeGenerator;
import com.arkcraft.common.tileentity.energy.TileEntityElectricLamp;
import com.arkcraft.common.tileentity.energy.TileEntityElectricOutlet;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ARKCraftBlocks {
	public static BlockLight blockLight;
	public static BlockBerryBush berryBush;
	public static BlockCompostBin compostBin;
	public static BlockSmithy smithy;
	public static BlockMortarAndPestle mortarAndPestle;
	public static BlockCropPlot cropPlot;
	public static BlockRefiningForge refiningForge;
	public static BlockCampfire campfire;
	public static BlockGreenScreen greenScreen;
	public static BlockFabricator fabricator;

	public static BlockRockResource rockResource;
	public static BlockMetalResource metalResource;
	public static BlockObsidianResource obsidianResource;
	public static BlockCrystalResource crystalResource;
	public static BlockOilResource oilResource;
	public static BlockSmallRockResource smallRockResource;
	public static BlockCable cable;
	public static BlockCreativeGenerator creativeGenerator;
	public static BlockElectricOutlet electricOutlet;
	public static BlockElectricLamp electricLamp;

	public static ARKCraftBlocks getInstance() {
		return new ARKCraftBlocks();
	}

	public static void init() {
		InitializationManager init = InitializationManager.instance();

		// Misc
		blockLight = init.registerBlock("light", new BlockLight());
		berryBush = init.registerBlock("berry_bush", new BlockBerryBush(0.4F));
		greenScreen = init.registerBlock("green_screen", new BlockGreenScreen());

		// Containers
		smithy = new BlockSmithy();
		init.registerBlock("smithy", smithy, new ItemSmithy(smithy));
		mortarAndPestle = new BlockMortarAndPestle();
		init.registerBlock("mortar_and_pestle", mortarAndPestle, new ItemMortarAndPestle(mortarAndPestle));
		cropPlot = new BlockCropPlot();
		init.registerBlock("crop_plot", cropPlot, new ItemCropPlot(cropPlot));
		compostBin = new BlockCompostBin();
		init.registerBlock("compost_bin", compostBin, new ItemCompostBin(compostBin));
		refiningForge = new BlockRefiningForge();
		init.registerBlock("refining_forge", refiningForge, new ItemRefiningForge(refiningForge));
		campfire = new BlockCampfire();
		init.registerBlock("campfire", campfire, new ItemCampfire(campfire));
		fabricator = new BlockFabricator();
		init.registerBlock("fabricator", fabricator, new ItemBlockFabricator(fabricator));
		cable = new BlockCable();
		init.registerBlock("cable", cable, new ItemBlockCable(cable));
		creativeGenerator = new BlockCreativeGenerator();
		init.registerBlock("creative_generator", creativeGenerator);
		electricOutlet = new BlockElectricOutlet();
		init.registerBlock("electric_outlet", electricOutlet);
		electricLamp = new BlockElectricLamp();
		init.registerBlock("electric_lamp", electricLamp);

		rockResource = init.registerBlock("rock_resource", new BlockRockResource());
		metalResource = init.registerBlock("metal_resource", new BlockMetalResource());
		obsidianResource = init.registerBlock("obsidian_resource", new BlockObsidianResource());
		crystalResource = init.registerBlock("crystal_resource", new BlockCrystalResource());
		oilResource = init.registerBlock("oil_resource", new BlockOilResource());
		smallRockResource = init.registerBlock("small_rock_resource", new BlockSmallRockResource());

		// Tile Entities
		GameRegistry.registerTileEntity(TileEntityCropPlot.class, new ResourceLocation(ARKCraft.MODID, "te_crop_plot"));
		GameRegistry.registerTileEntity(TileEntityMP.class, new ResourceLocation(ARKCraft.MODID, "te_mortar_and_pestle"));
		GameRegistry.registerTileEntity(TileEntityCompostBin.class, new ResourceLocation(ARKCraft.MODID, "te_compost_bin"));
		GameRegistry.registerTileEntity(TileEntitySmithy.class, new ResourceLocation(ARKCraft.MODID, "te_smithy"));
		GameRegistry.registerTileEntity(TileEntityRefiningForge.class, new ResourceLocation(ARKCraft.MODID, "te_refining_forge"));
		GameRegistry.registerTileEntity(TileEntityCampfire.class, new ResourceLocation(ARKCraft.MODID, "te_campfire"));
		//TODO check use of crystal tileentity without any function
		GameRegistry.registerTileEntity(TileEntityCrystal.class, new ResourceLocation(ARKCraft.MODID, "te_crystal"));
		GameRegistry.registerTileEntity(TileEntityFabricator.class, new ResourceLocation(ARKCraft.MODID, "te_fabricator"));
		GameRegistry.registerTileEntity(TileEntityCable.class, new ResourceLocation(ARKCraft.MODID, "te_cable"));
		GameRegistry.registerTileEntity(TileEntityCreativeGenerator.class, new ResourceLocation(ARKCraft.MODID, "te_creative_generator"));
		GameRegistry.registerTileEntity(TileEntityElectricOutlet.class, new ResourceLocation(ARKCraft.MODID, "te_electric_outlet"));
		GameRegistry.registerTileEntity(TileEntityElectricLamp.class, new ResourceLocation(ARKCraft.MODID, "te_electric_lamp"));
	}
}
