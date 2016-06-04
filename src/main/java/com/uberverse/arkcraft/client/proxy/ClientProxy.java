package com.uberverse.arkcraft.client.proxy;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.event.ClientEventHandler;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.lib.LogHelper;

public class ClientProxy extends CommonProxy{
	
	boolean initDone = false;

	@Override
	public void init()
	{
		if (initDone) { return; }
		super.init();

		ClientEventHandler.init();

		//MinecraftForge.EVENT_BUS.register(new GuiOverlay());

	//	KeyBindings.preInit();
	//	dossierProxy.init();
	//	registerItemVariants();
		LogHelper.info("CommonProxy: Init run finished.");
		initDone = true;
	}

	@Override
	public void registerEventHandlers()
	{
		super.registerEventHandlers();

		/*
		CoreClientEventHandler mod1Eventhandler = new CoreClientEventHandler();
		FMLCommonHandler.instance().bus().register(mod1Eventhandler);
		MinecraftForge.EVENT_BUS.register(mod1Eventhandler);	*/

	}

	/* We register the block/item textures and models here */
	@Override
	public void registerRenderers()
	{
		for (Map.Entry<String, Block> e : ARKCraftBlocks.allBlocks.entrySet())
		{
			String name = e.getKey();
			Block b = e.getValue();
			registerBlockTexture(b, name);
		}

		for (Map.Entry<String, Item> e : ARKCraftItems.allItems.entrySet())
		{
			String name = e.getKey();
			Item item = e.getValue();
			registerItemTexture(item, name);
		}
		
		// registerItemVariants();
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
	}

	public EntityPlayer getPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
	// public void registerSound() {
	// MinecraftForge.EVENT_BUS.register(new SoundHandler());
	// }

	
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