package com.uberverse.arkcraft.client.proxy;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.event.ClientEventHandler;
import com.uberverse.arkcraft.client.gui.GuiOverlayReloading;
import com.uberverse.arkcraft.client.render.RenderAdvancedBullet;
import com.uberverse.arkcraft.client.render.RenderSimpleBullet;
import com.uberverse.arkcraft.client.render.RenderSimpleRifleAmmo;
import com.uberverse.arkcraft.client.render.RenderSimpleShotgunAmmo;
import com.uberverse.arkcraft.client.render.RenderSpear;
import com.uberverse.arkcraft.client.render.RenderTranquilizer;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.entity.EntityAdvancedBullet;
import com.uberverse.arkcraft.common.entity.EntityGrenade;
import com.uberverse.arkcraft.common.entity.EntitySimpleBullet;
import com.uberverse.arkcraft.common.entity.EntitySimpleRifleAmmo;
import com.uberverse.arkcraft.common.entity.EntitySimpleShotgunAmmo;
import com.uberverse.arkcraft.common.entity.EntitySpear;
import com.uberverse.arkcraft.common.entity.EntityStone;
import com.uberverse.arkcraft.common.entity.EntityTranquilizer;
import com.uberverse.arkcraft.common.item.firearms.ItemRangedWeapon;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.ARKCraftWeapons;
import com.uberverse.lib.LogHelper;

public class ClientProxy extends CommonProxy{
	
	boolean initDone = false;

	@Override
	public void init()
	{
		if (initDone) { return; }
		super.init();

		ClientEventHandler.init();

	//1	MinecraftForge.EVENT_BUS.register(new GuiOverlay());
		MinecraftForge.EVENT_BUS.register(new GuiOverlayReloading());

	//	KeyBindings.preInit();
	//	dossierProxy.init();
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
		
		for (Entry<String, Item> i : ARKCraftWeapons.allWeaponItems.entrySet())
		{
			registerItemTexture(i.getValue(), 0, i.getKey());
		}

		// Register models for entities
		registerEntityModels();

		// Register variant models for all weapon items
		registerItemVariants();
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

	public void registerItemTexture(final Item item, int meta, String name)
	{
		if (item instanceof ItemRangedWeapon) name = "weapons/" + name;
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
	
	private static void registerEntityModels()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityStone.class,
				new RenderSnowball(Minecraft.getMinecraft().getRenderManager(),
						ARKCraftItems.stone, Minecraft.getMinecraft()
								.getRenderItem()));

		RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class,
				new RenderSnowball(Minecraft.getMinecraft().getRenderManager(),
						ARKCraftItems.grenade, Minecraft.getMinecraft()
								.getRenderItem()));

		if (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL)
		{
			RenderingRegistry.registerEntityRenderingHandler(
					EntitySimpleBullet.class, new RenderSimpleBullet());
		}
		if (ModuleItemBalance.WEAPONS.SHOTGUN)
		{
			RenderingRegistry.registerEntityRenderingHandler(
					EntitySimpleShotgunAmmo.class,
					new RenderSimpleShotgunAmmo());
		}
		if (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE)
		{
			RenderingRegistry.registerEntityRenderingHandler(
					EntitySimpleRifleAmmo.class, new RenderSimpleRifleAmmo());
			RenderingRegistry.registerEntityRenderingHandler(
					EntityTranquilizer.class, new RenderTranquilizer());
		}
		if (ModuleItemBalance.WEAPONS.SPEAR)
		{
			RenderingRegistry.registerEntityRenderingHandler(EntitySpear.class,
					new RenderSpear());
		}
		if (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL)
		{
			RenderingRegistry.registerEntityRenderingHandler(
					EntityAdvancedBullet.class, new RenderAdvancedBullet());
		}
	}

	public static void registerItemVariants()
	{
	//	ModelBakery.addVariantName(ARKCraftItems.slingshot,
	//			"arkcraft:slingshot", "arkcraft:slingshot_pulled");
		ModelBakery.addVariantName(ARKCraftWeapons.shotgun,
				"arkcraft:weapons/shotgun", "arkcraft:weapons/shotgun_reload");
		ModelBakery.addVariantName(ARKCraftWeapons.longneck_rifle,
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
		ModelBakery.addVariantName(ARKCraftWeapons.simple_pistol,
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
		ModelBakery.addVariantName(ARKCraftWeapons.fabricated_pistol,
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
	}
}