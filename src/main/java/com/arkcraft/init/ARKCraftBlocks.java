package com.arkcraft.init;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.block.BlockBerryBush;
import com.arkcraft.common.block.BlockGreenScreen;
import com.arkcraft.common.block.BlockLight;
import com.arkcraft.common.block.energy.BlockCable;
import com.arkcraft.common.block.energy.BlockCreativeGenerator;
import com.arkcraft.common.block.energy.BlockElectricLamp;
import com.arkcraft.common.block.energy.BlockElectricOutlet;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.common.block.crafter.BlockCampfire;
import com.arkcraft.common.block.crafter.BlockCompostBin;
import com.arkcraft.common.block.crafter.BlockCropPlot;
import com.arkcraft.common.block.crafter.BlockFabricator;
import com.arkcraft.common.block.crafter.BlockMortarAndPestle;
import com.arkcraft.common.block.crafter.BlockRefiningForge;
import com.arkcraft.common.block.crafter.BlockSmithy;
import com.arkcraft.common.block.resource.BlockCrystalResource;
import com.arkcraft.common.block.resource.BlockMetalResource;
import com.arkcraft.common.block.resource.BlockObsidianResource;
import com.arkcraft.common.block.resource.BlockOilResource;
import com.arkcraft.common.block.resource.BlockRockResource;
import com.arkcraft.common.block.resource.BlockSmallRockResource;
import com.arkcraft.common.item.itemblock.ItemBlockCable;
import com.arkcraft.common.item.itemblock.ItemBlockFabricator;
import com.arkcraft.common.item.itemblock.ItemCampfire;
import com.arkcraft.common.item.itemblock.ItemCompostBin;
import com.arkcraft.common.item.itemblock.ItemCropPlot;
import com.arkcraft.common.item.itemblock.ItemMortarAndPestle;
import com.arkcraft.common.item.itemblock.ItemRefiningForge;
import com.arkcraft.common.item.itemblock.ItemSmithy;
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

public class ARKCraftBlocks
{
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

	public static ARKCraftBlocks getInstance()
	{
		return new ARKCraftBlocks();
	}

	public static void init()
	{
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
		GameRegistry.registerTileEntity(TileEntityCropPlot.class, ARKCraft.MODID + "te_cropplot");
		GameRegistry.registerTileEntity(TileEntityMP.class, ARKCraft.MODID + "te_mortarandpestle");
		GameRegistry.registerTileEntity(TileEntityCompostBin.class, ARKCraft.MODID + "te_compostbin");
		GameRegistry.registerTileEntity(TileEntitySmithy.class, ARKCraft.MODID + "te_smithy");
		GameRegistry.registerTileEntity(TileEntityRefiningForge.class, ARKCraft.MODID + "te_refiningforge");
		GameRegistry.registerTileEntity(TileEntityCampfire.class, ARKCraft.MODID + "te_campfire");
		GameRegistry.registerTileEntity(TileEntityCrystal.class, ARKCraft.MODID + "te_crystal");
		GameRegistry.registerTileEntity(TileEntityFabricator.class, ARKCraft.MODID + "te_fabricator");
		GameRegistry.registerTileEntity(TileEntityCable.class, ARKCraft.MODID + "te_cable");
		GameRegistry.registerTileEntity(TileEntityCreativeGenerator.class, ARKCraft.MODID + "te_creative_gen");
		GameRegistry.registerTileEntity(TileEntityElectricOutlet.class, ARKCraft.MODID + "te_electric_outlet");
		GameRegistry.registerTileEntity(TileEntityElectricLamp.class, ARKCraft.MODID + "te_electric_lamp");
	}
}
