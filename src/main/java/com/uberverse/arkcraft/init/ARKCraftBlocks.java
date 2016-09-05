package com.uberverse.arkcraft.init;

import java.util.HashMap;
import java.util.Map;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.ARKCraftBerryBush;
import com.uberverse.arkcraft.common.block.BlockGreenScreen;
import com.uberverse.arkcraft.common.block.BlockLight;
import com.uberverse.arkcraft.common.block.BlockSpikes;
import com.uberverse.arkcraft.common.block.crafter.BlockCompostBin;
import com.uberverse.arkcraft.common.block.crafter.BlockCropPlot;
import com.uberverse.arkcraft.common.block.crafter.BlockMortarAndPestle;
import com.uberverse.arkcraft.common.block.crafter.BlockSmithy;
import com.uberverse.arkcraft.common.block.unused.ARKContainerBlock;
import com.uberverse.arkcraft.common.block.unused.BlockARKBase;
import com.uberverse.arkcraft.common.item.itemblock.ItemCampfire;
import com.uberverse.arkcraft.common.item.itemblock.ItemCompostBin;
import com.uberverse.arkcraft.common.item.itemblock.ItemCropPlot;
import com.uberverse.arkcraft.common.item.itemblock.ItemMortarAndPestle;
import com.uberverse.arkcraft.common.item.itemblock.ItemRefiningForge;
import com.uberverse.arkcraft.common.item.itemblock.ItemSmithy;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlotNew;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityMP;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntitySmithy;
import com.uberverse.arkcraft.common.tileentity.crafter.TileInventoryCompostBin;
import com.uberverse.arkcraft.wip.burners.BlockCampfire;
import com.uberverse.arkcraft.wip.burners.BlockRefiningForge;
import com.uberverse.arkcraft.wip.burners.TileInventoryCampfire;
import com.uberverse.arkcraft.wip.burners.TileInventoryForge;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ARKCraftBlocks
{

	public static BlockLight block_light;
	public static ARKCraftBerryBush berryBush;

	public static BlockARKBase oreSurface;
	public static BlockSpikes wooden_spikes;
	public static BlockCompostBin compost_bin;
	public static BlockSmithy smithy;
	public static BlockMortarAndPestle pestle;
	public static BlockCropPlot crop_plot;
	public static BlockRefiningForge refining_forge;
	public static BlockCampfire campfire;

	public static BlockGreenScreen greenScreen;

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
		// Misc
		 block_light = new BlockLight();
		 GameRegistry.registerBlock(block_light, "block_light");
		berryBush = (ARKCraftBerryBush) registerBlock(new ARKCraftBerryBush(0.4F), "berryBush");

		greenScreen = (BlockGreenScreen) registerBlockNoTab(new BlockGreenScreen(Material.gourd), "greenScreen");

		// Containers
		smithy = registerSmithy("smithy", Material.wood, ARKCraft.GUI.SMITHY.getID(), false, false, 3);
		pestle = registerMortarAndPestle("mortar_and_pestle", Material.rock, ARKCraft.GUI.PESTLE_AND_MORTAR.getID(), false, false, 3);
		crop_plot = registerCropPlot("crop_plot", Material.wood, ARKCraft.GUI.CROP_PLOT.getID(), false, 3);
		compost_bin = registerCompostBin("compost_bin", Material.wood, ARKCraft.GUI.COMPOST_BIN.getID(), false, false, 3);
		refining_forge = registerRefiningForge("refining_forge", Material.rock, false, ARKCraft.GUI.FORGE_GUI.getID(), false, false, 3);
		campfire = registerCampfire("campfire", Material.wood, ARKCraft.GUI.CAMPFIRE_GUI.getID(), false, 3);

		// Tile Entities
		GameRegistry.registerTileEntity(TileEntityCropPlotNew.class, ARKCraft.MODID + "cropPlot");
		GameRegistry.registerTileEntity(TileEntityMP.class, "TileInventoryMP");
		GameRegistry.registerTileEntity(TileInventoryCompostBin.class, "TileEntityCompostBin");
		GameRegistry.registerTileEntity(TileEntitySmithy.class, "TileEntitySmithy");
		GameRegistry.registerTileEntity(TileInventoryForge.class, "TileInventoryForge");
		GameRegistry.registerTileEntity(TileInventoryCampfire.class, "TileInventoryCampfire");
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

	protected static BlockMortarAndPestle registerMortarAndPestle(String name, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque,
			int renderType)
	{
		BlockMortarAndPestle container = new BlockMortarAndPestle(mat, ID);
		registerBlockWithItemBlock(container, ItemMortarAndPestle.class, name);
		return container;
	}

	protected static BlockRefiningForge registerRefiningForge(String name, Material mat, boolean isBurning, int ID, boolean renderAsNormalBlock,
			boolean isOpaque, int renderType)
	{
		BlockRefiningForge container = new BlockRefiningForge(mat, ID);
		registerBlockWithItemBlock(container, ItemRefiningForge.class, name);
		return container;
	}

	protected static BlockCampfire registerCampfire(String name, Material mat, int ID, boolean isOpaque, int renderType)
	{
		BlockCampfire container = new BlockCampfire(mat, ID);
		registerBlockWithItemBlock(container, ItemCampfire.class, name);
		return container;
	}

	protected static BlockCropPlot registerCropPlot(String name, Material mat, int ID, boolean isOpaque, int renderType)
	{
		BlockCropPlot container = new BlockCropPlot(mat, ID);
		registerBlockWithItemBlock(container, ItemCropPlot.class, name);
		return container;
	}

	protected static BlockCompostBin registerCompostBin(String name, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque,
			int renderType)
	{
		BlockCompostBin container = new BlockCompostBin(mat, ID);
		registerBlockWithItemBlock(container, ItemCompostBin.class, name);
		return container;
	}
}
