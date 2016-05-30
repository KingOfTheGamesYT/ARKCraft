package com.uberverse.arkcraft.client.proxy;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.event.ClientEventHandler;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.lib.LogHelper;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void init()
	{
		ClientEventHandler.init();

	//	MinecraftForge.EVENT_BUS.register(new GuiOverlay());

		//KeyBindings.preInit();
		LogHelper.info("CommonProxy: Init run finished.");
		registerRenderers();
	}
	
	private void registerRenderers()
	{
		for (Entry<String, Item> i : ARKCraftItems.allItems.entrySet())
		{
			registerItemTexture(i.getValue(), 0, i.getKey());
		}	
		for (Map.Entry<String, Block> e : ARKCraftBlocks.allBlocks.entrySet())
		{
			String name = e.getKey();
			Block b = e.getValue();
			registerBlockTexture(b, name);
		}
	}
	
	public void registerBlockTexture(final Block block, final String blockName)
	{
		registerBlockTexture(block, 0, blockName);
	}
	
	public void registerBlockTexture(final Block block, int meta, final String blockName)
	{
		registerItemTexture(Item.getItemFromBlock(block), meta, blockName);
	}
	
	public void registerItemTexture(final Item item, final String name)
	{
		registerItemTexture(item, 0, name);
	}

	public void registerItemTexture(final Item item, int meta, final String name)
	{
		Minecraft
				.getMinecraft()
				.getRenderItem()
				.getItemModelMesher()
				.register(
						item,
						meta,
						new ModelResourceLocation(ARKCraft.MODID + ":" + name,
								"inventory"));
		ModelBakery.addVariantName(item, ARKCraft.MODID + ":" + name);
	//	ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(ARKCraft.MODID + ":" + name,
		//		"inventory"));;
	}
	
	private static void registerItemVariants()
	{
		ModelBakery.addVariantName(ARKCraftItems.slingshot,
				"arkcraft:slingshot", "arkcraft:slingshot_pulled");
		ModelBakery.addVariantName(ARKCraftItems.shotgun,
				"arkcraft:weapons/shotgun", "arkcraft:weapons/shotgun_reload");
		ModelBakery.addVariantName(ARKCraftItems.longneck_rifle,
				"arkcraft:weapons/longneck_rifle",
				"arkcraft:weapons/longneck_rifle_scope",
				"arkcraft:weapons/longneck_rifle_scope_reload",
				"arkcraft:weapons/longneck_rifle_reload",
				"arkcraft:weapons/longneck_rifle_flashlight",
				"arkcraft:weapons/longneck_rifle_flashlight_reload",
				"arkcraft:weapons/longneck_rifle_laser",
				"arkcraft:weapons/longneck_rifle_laser_reload",
				"arkcraft:weapons/longneck_rifle_silencer",
				"arkcraft:weapons/longneck_rifle_silencer_reload");
		ModelBakery.addVariantName(ARKCraftItems.simple_pistol,
				"arkcraft:weapons/simple_pistol",
				"arkcraft:weapons/simple_pistol_scope",
				"arkcraft:weapons/simple_pistol_reload",
				"arkcraft:weapons/simple_pistol_scope_reload",
				"arkcraft:weapons/simple_pistol_flashlight",
				"arkcraft:weapons/simple_pistol_flashlight_reload",
				"arkcraft:weapons/simple_pistol_laser",
				"arkcraft:weapons/simple_pistol_laser_reload",
				"arkcraft:weapons/simple_pistol_silencer",
				"arkcraft:weapons/simple_pistol_silencer_reload");
		ModelBakery.addVariantName(ARKCraftItems.fabricated_pistol,
				"arkcraft:weapons/fabricated_pistol",
				"arkcraft:weapons/fabricated_pistol_scope",
				"arkcraft:weapons/fabricated_pistol_reload",
				"arkcraft:weapons/fabricated_pistol_scope_reload",
				"arkcraft:weapons/fabricated_pistol_flashlight",
				"arkcraft:weapons/fabricated_pistol_flashlight_reload",
				"arkcraft:weapons/fabricated_pistol_laser",
				"arkcraft:weapons/fabricated_pistol_laser_reload",
				"arkcraft:weapons/fabricated_pistol_silencer",
				"arkcraft:weapons/fabricated_pistol_silencer_reload",
				"arkcraft:weapons/fabricated_pistol_holo_scope",
				"arkcraft:weapons/fabricated_pistol_holo_scope_reload");

		ModelBakery.addVariantName(ARKCraftItems.bow, "arkcraft:bow",
				"arkcraft:bow_pulling_0", "arkcraft:bow_pulling_1",
				"arkcraft:bow_pulling_2");
	}
}