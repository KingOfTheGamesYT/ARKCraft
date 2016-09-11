package com.uberverse.arkcraft.init;

import java.util.HashMap;
import java.util.Map;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.ARKCraftBerryBush;
import com.uberverse.arkcraft.common.block.BlockGreenScreen;
import com.uberverse.arkcraft.common.block.BlockLight;
import com.uberverse.arkcraft.common.block.BlockSpikes;
import com.uberverse.arkcraft.common.block.crafter.BlockCampfire;
import com.uberverse.arkcraft.common.block.crafter.BlockCompostBin;
import com.uberverse.arkcraft.common.block.crafter.BlockCropPlot;
import com.uberverse.arkcraft.common.block.crafter.BlockMortarAndPestle;
import com.uberverse.arkcraft.common.block.crafter.BlockRefiningForge;
import com.uberverse.arkcraft.common.block.crafter.BlockSmithy;
import com.uberverse.arkcraft.common.block.unused.ARKContainerBlock;
import com.uberverse.arkcraft.common.block.unused.BlockARKBase;
import com.uberverse.arkcraft.common.item.itemblock.ItemCampfire;
import com.uberverse.arkcraft.common.item.itemblock.ItemCompostBin;
import com.uberverse.arkcraft.common.item.itemblock.ItemCropPlot;
import com.uberverse.arkcraft.common.item.itemblock.ItemMortarAndPestle;
import com.uberverse.arkcraft.common.item.itemblock.ItemRefiningForge;
import com.uberverse.arkcraft.common.item.itemblock.ItemSmithy;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot;
import com.uberverse.arkcraft.common.tileentity.crafter.TileInventoryCompostBin;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityCampfire;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityRefiningForge;
import com.uberverse.arkcraft.common.tileentity.crafter.engram.TileEntityMP;
import com.uberverse.arkcraft.common.tileentity.crafter.engram.TileEntitySmithy;

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
		InitializationManager init = InitializationManager.instance();
		// Misc
		block_light = new BlockLight();
		GameRegistry.registerBlock(block_light, "block_light");
		berryBush = (ARKCraftBerryBush) registerBlock(new ARKCraftBerryBush(0.4F), "berryBush");

		greenScreen = (BlockGreenScreen) registerBlockNoTab(new BlockGreenScreen(Material.gourd), "greenScreen");

		// Containers
		smithy = init.registerBlock("smithy", new BlockSmithy(), ItemSmithy.class);
		// registerSmithy("smithy", Material.wood, CommonProxy.GUI.SMITHY.id, false, false, 3);
		pestle = registerMortarAndPestle("mortar_and_pestle", Material.rock, CommonProxy.GUI.MORTAR_AND_PESTLE.id, false, false, 3);
		crop_plot = registerCropPlot("crop_plot", Material.wood, CommonProxy.GUI.CROP_PLOT.id, false, 3);
		compost_bin = registerCompostBin("compost_bin", Material.wood, CommonProxy.GUI.COMPOST_BIN.id, false, false, 3);
		refining_forge = registerRefiningForge("refining_forge", Material.rock, false, false, false, 3);
		campfire = registerCampfire("campfire", Material.wood, false, 3);

		// Tile Entities
		GameRegistry.registerTileEntity(TileEntityCropPlot.class, ARKCraft.MODID + "cropPlot");
		GameRegistry.registerTileEntity(TileEntityMP.class, ARKCraft.MODID + "TileInventoryMP");
		GameRegistry.registerTileEntity(TileInventoryCompostBin.class, ARKCraft.MODID + "TileEntityCompostBin");
		GameRegistry.registerTileEntity(TileEntitySmithy.class, ARKCraft.MODID + "TileEntitySmithy");
		GameRegistry.registerTileEntity(TileEntityRefiningForge.class, ARKCraft.MODID + "TileEntityRefiningForge");
		GameRegistry.registerTileEntity(TileEntityCampfire.class, ARKCraft.MODID + "TileEntityCampfire");
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
		BlockSmithy container = new BlockSmithy();
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

	protected static BlockRefiningForge registerRefiningForge(String name, Material mat, boolean isBurning, boolean renderAsNormalBlock,
			boolean isOpaque, int renderType)
	{
		BlockRefiningForge container = new BlockRefiningForge(mat);
		registerBlockWithItemBlock(container, ItemRefiningForge.class, name);
		return container;
	}

	protected static BlockCampfire registerCampfire(String name, Material mat, boolean isOpaque, int renderType)
	{
		BlockCampfire container = new BlockCampfire(mat);
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
