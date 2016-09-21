package com.uberverse.arkcraft.init;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.ARKCraftBerryBush;
import com.uberverse.arkcraft.common.block.BlockGreenScreen;
import com.uberverse.arkcraft.common.block.BlockLight;
import com.uberverse.arkcraft.common.block.crafter.BlockCampfire;
import com.uberverse.arkcraft.common.block.crafter.BlockCompostBin;
import com.uberverse.arkcraft.common.block.crafter.BlockCropPlot;
import com.uberverse.arkcraft.common.block.crafter.BlockMortarAndPestle;
import com.uberverse.arkcraft.common.block.crafter.BlockRefiningForge;
import com.uberverse.arkcraft.common.block.crafter.BlockSmithy;
import com.uberverse.arkcraft.common.block.resource.BlockCrystalResource;
import com.uberverse.arkcraft.common.block.resource.BlockMetalResource;
import com.uberverse.arkcraft.common.block.resource.BlockObsidianResource;
import com.uberverse.arkcraft.common.block.resource.BlockOilResource;
import com.uberverse.arkcraft.common.block.resource.BlockRockResource;
import com.uberverse.arkcraft.common.item.itemblock.ItemCampfire;
import com.uberverse.arkcraft.common.item.itemblock.ItemCompostBin;
import com.uberverse.arkcraft.common.item.itemblock.ItemCropPlot;
import com.uberverse.arkcraft.common.item.itemblock.ItemMortarAndPestle;
import com.uberverse.arkcraft.common.item.itemblock.ItemRefiningForge;
import com.uberverse.arkcraft.common.item.itemblock.ItemSmithy;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot;
import com.uberverse.arkcraft.common.tileentity.crafter.TileInventoryCompostBin;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityCampfire;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityRefiningForge;
import com.uberverse.arkcraft.common.tileentity.crafter.engram.TileEntityMP;
import com.uberverse.arkcraft.common.tileentity.crafter.engram.TileEntitySmithy;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ARKCraftBlocks
{
	public static BlockLight block_light;
	public static ARKCraftBerryBush berryBush;
	public static BlockCompostBin compost_bin;
	public static BlockSmithy smithy;
	public static BlockMortarAndPestle pestle;
	public static BlockCropPlot crop_plot;
	public static BlockRefiningForge refining_forge;
	public static BlockCampfire campfire;
	public static BlockGreenScreen greenScreen;

	public static BlockRockResource rockResource;
	public static BlockMetalResource metalResource;
	public static BlockObsidianResource obsidianResource;
	public static BlockCrystalResource crystalResource;
	public static BlockOilResource oilResource;

	public static ARKCraftBlocks getInstance()
	{
		return new ARKCraftBlocks();
	}

	public static void init()
	{
		InitializationManager init = InitializationManager.instance();

		// Misc
		block_light = init.registerBlock("block_light", new BlockLight());
		berryBush = init.registerBlock("berryBush", new ARKCraftBerryBush(0.4F));
		greenScreen = init.registerBlock("greenScreen", new BlockGreenScreen());

		// Containers
		smithy = init.registerBlock("smithy", new BlockSmithy(), ItemSmithy.class);
		pestle = init.registerBlock("mortar_and_pestle", new BlockMortarAndPestle(), ItemMortarAndPestle.class);
		crop_plot = init.registerBlock("crop_plot", new BlockCropPlot(), ItemCropPlot.class);
		compost_bin = init.registerBlock("compost_bin", new BlockCompostBin(), ItemCompostBin.class);
		refining_forge = init.registerBlock("refining_forge", new BlockRefiningForge(), ItemRefiningForge.class);
		campfire = init.registerBlock("campfire", new BlockCampfire(), ItemCampfire.class);

		rockResource = init.registerBlock("rock_resource", new BlockRockResource());
		metalResource = init.registerBlock("metal_resource", new BlockMetalResource());
		obsidianResource = init.registerBlock("obsidian_resource", new BlockObsidianResource());
		crystalResource = init.registerBlock("crystal_resource", new BlockCrystalResource());
		oilResource = init.registerBlock("oil_resource", new BlockOilResource());

		// Tile Entities
		GameRegistry.registerTileEntity(TileEntityCropPlot.class, ARKCraft.MODID + "cropPlot");
		GameRegistry.registerTileEntity(TileEntityMP.class, ARKCraft.MODID + "TileInventoryMP");
		GameRegistry.registerTileEntity(TileInventoryCompostBin.class, ARKCraft.MODID + "TileEntityCompostBin");
		GameRegistry.registerTileEntity(TileEntitySmithy.class, ARKCraft.MODID + "TileEntitySmithy");
		GameRegistry.registerTileEntity(TileEntityRefiningForge.class, ARKCraft.MODID + "TileEntityRefiningForge");
		GameRegistry.registerTileEntity(TileEntityCampfire.class, ARKCraft.MODID + "TileEntityCampfire");
	}
}
