package com.uberverse.arkcraft.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.ARKContainerBlock;
import com.uberverse.arkcraft.common.block.ARKCraftBerryBush;
import com.uberverse.arkcraft.common.block.BlockARKBase;
import com.uberverse.arkcraft.common.block.BlockCompostBin;
import com.uberverse.arkcraft.common.block.BlockCropPlot;
import com.uberverse.arkcraft.common.block.BlockFlashlight;
import com.uberverse.arkcraft.common.block.BlockMortarAndPestle;
import com.uberverse.arkcraft.common.block.BlockRefiningForge;
import com.uberverse.arkcraft.common.block.BlockSmithy;
import com.uberverse.arkcraft.common.block.BlockSpikes;
import com.uberverse.arkcraft.common.block.itemblock.ItemCompostBin;
import com.uberverse.arkcraft.common.block.itemblock.ItemCropPlot;
import com.uberverse.arkcraft.common.block.itemblock.ItemMortarAndPestle;
import com.uberverse.arkcraft.common.block.itemblock.ItemRefiningForge;
import com.uberverse.arkcraft.common.block.itemblock.ItemSmithy;
import com.uberverse.arkcraft.common.block.tile.TileEntityCropPlotNew;
import com.uberverse.arkcraft.common.block.tile.TileInventoryCompostBin;
import com.uberverse.arkcraft.common.block.tile.TileInventoryForge;
import com.uberverse.arkcraft.common.block.tile.TileInventoryMP;
import com.uberverse.arkcraft.common.block.tile.TileInventorySmithy;
import com.uberverse.arkcraft.common.tileentity.TileFlashlight;

public class ARKCraftBlocks {

	public static BlockFlashlight block_flashlight;
	public static ARKCraftBerryBush berryBush;

	public static BlockARKBase oreSurface;
	public static BlockSpikes wooden_spikes;
	public static BlockCompostBin compost_bin;
	public static BlockSmithy smithy;
	public static BlockMortarAndPestle pestle;
	public static BlockCropPlot crop_plot;
	public static BlockRefiningForge refining_forge;

	public static ARKCraftBlocks getInstance()
	{
		return new ARKCraftBlocks();
	}

	public static Map<String, Block> allBlocks = new HashMap<String, Block>();

	public static Map<String, Block> getAllBlocks()
	{
		return allBlocks;
	}

	public static void init()
	{
		//Misc
		block_flashlight = new BlockFlashlight();
		GameRegistry.registerBlock(block_flashlight, "block_flashlight");
	//	berryBush = (ARKCraftBerryBush) registerBlock(new ARKCraftBerryBush(0.4F), "berryBush");

		wooden_spikes = (BlockSpikes) registerBlock(new BlockSpikes(Material.wood, 3.0F),
				"wooden_spikes");

		// Containers
		smithy = registerSmithy("smithy", Material.wood,
				ARKCraft.GUI.SMITHY.getID(), false, false, 3);
		pestle = registerMortarAndPestle("mortar_and_pestle", Material.rock,
				ARKCraft.GUI.PESTLE_AND_MORTAR.getID(), false, false, 3);
		crop_plot = registerCropPlot("crop_plot", Material.wood,
				ARKCraft.GUI.CROP_PLOT.getID(), false, 3);
		compost_bin = registerCompostBin("compost_bin", Material.wood,
				ARKCraft.GUI.COMPOST_BIN.getID(), false, false, 3);
		refining_forge = registerRefiningForge("refining_forge", Material.rock, false,
				ARKCraft.GUI.FORGE_GUI.getID(), false, false, 3);

		// Tile Entities
		GameRegistry.registerTileEntity(TileEntityCropPlotNew.class, ARKCraft.MODID + "cropPlot");
		GameRegistry.registerTileEntity(TileInventoryMP.class, "TileInventoryMP");
		GameRegistry.registerTileEntity(TileInventoryCompostBin.class, "TileEntityCompostBin");
		GameRegistry.registerTileEntity(TileInventorySmithy.class, "TileInventorySmithy");
		GameRegistry.registerTileEntity(TileInventoryForge.class, "TileInventoryForge");
		GameRegistry.registerTileEntity(TileFlashlight.class, "TileFlashlight");

	}

	private static Block registerBlock(Block block, String name)
	{
		block.setCreativeTab(ARKCraft.tabARK);
		return registerBlockNoTab(block, name);
	}

	private static Block registerBlockNoTab(Block block, String name)
	{
		GameRegistry.registerBlock(block, name);
		block.setUnlocalizedName(name);
		allBlocks.put(name, block);
		return block;
	}

	private static Block registerBlockWithItemBlock(Block block, Class<? extends ItemBlock> itemBlock, String name)
	{
		block.setCreativeTab(ARKCraft.tabARK);
		GameRegistry.registerBlock(block, itemBlock, name);
		block.setUnlocalizedName(name);
		allBlocks.put(name, block);
		return block;
	}

	protected static Block getRegisteredBlock(String name)
	{
		return (Block) Block.blockRegistry.getObject(new ResourceLocation(name));
	}

	protected static ARKContainerBlock addContainer(String name, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType)
	{
		ARKContainerBlock container = new ARKContainerBlock(mat, ID);
		container.setRenderAsNormalBlock(renderAsNormalBlock);
		container.setOpaque(isOpaque);
		container.setRenderType(renderType);
		registerBlock(container, name);
		return container;
	}

	protected static BlockSmithy registerSmithy(String name, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType)
	{
		BlockSmithy container = new BlockSmithy(mat, ID);
		registerBlockWithItemBlock(container, ItemSmithy.class, name);
		return container;
	}

	protected static BlockMortarAndPestle registerMortarAndPestle(String name, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType)
	{
		BlockMortarAndPestle container = new BlockMortarAndPestle(mat, ID);
		registerBlockWithItemBlock(container, ItemMortarAndPestle.class, name);
		return container;
	}

	protected static BlockRefiningForge registerRefiningForge(String name, Material mat, boolean isBurning, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType)
	{
		BlockRefiningForge container = new BlockRefiningForge(mat, ID);
		registerBlockWithItemBlock(container, ItemRefiningForge.class, name);
		return container;
	}

	protected static BlockCropPlot registerCropPlot(String name, Material mat, int ID, boolean isOpaque, int renderType)
	{
		BlockCropPlot container = new BlockCropPlot(mat, ID);
		registerBlockWithItemBlock(container, ItemCropPlot.class, name);
		return container;
	}

	protected static BlockCompostBin registerCompostBin(String name, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType)
	{
		BlockCompostBin container = new BlockCompostBin(mat, ID);
		registerBlockWithItemBlock(container, ItemCompostBin.class, name);
		return container;
	}
}


