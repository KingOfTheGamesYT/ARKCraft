package com.uberverse.arkcraft.client.proxy;

import java.util.Map;
import java.util.Map.Entry;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.event.ClientEventHandler;
import com.uberverse.arkcraft.client.gui.overlay.GUIOverlayARKMode;
import com.uberverse.arkcraft.client.gui.overlay.GUIOverlayReloading;
import com.uberverse.arkcraft.client.render.RenderAdvancedBullet;
import com.uberverse.arkcraft.client.render.RenderSimpleBullet;
import com.uberverse.arkcraft.client.render.RenderSimpleRifleAmmo;
import com.uberverse.arkcraft.client.render.RenderSimpleShotgunAmmo;
import com.uberverse.arkcraft.client.render.RenderSpear;
import com.uberverse.arkcraft.client.render.RenderTranquilizer;
import com.uberverse.arkcraft.client.render.creature.RenderDodo;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.entity.EntityAdvancedBullet;
import com.uberverse.arkcraft.common.entity.EntityDodo;
import com.uberverse.arkcraft.common.entity.EntityGrenade;
import com.uberverse.arkcraft.common.entity.EntitySimpleBullet;
import com.uberverse.arkcraft.common.entity.EntitySimpleRifleAmmo;
import com.uberverse.arkcraft.common.entity.EntitySimpleShotgunAmmo;
import com.uberverse.arkcraft.common.entity.EntitySpear;
import com.uberverse.arkcraft.common.entity.EntityStone;
import com.uberverse.arkcraft.common.entity.EntityTranquilizer;
import com.uberverse.arkcraft.common.handlers.EntityHandler;
import com.uberverse.arkcraft.common.item.firearms.ItemRangedWeapon;
import com.uberverse.arkcraft.common.model.ModelDodo;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.ARKCraftRangedWeapons;
import com.uberverse.arkcraft.init.InitializationManager;
import com.uberverse.arkcraft.init.InitializationManager.RegistryEntry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);

		ClientEventHandler.init();
		dossierProxy.init();

		MinecraftForge.EVENT_BUS.register(new GUIOverlayReloading());
		MinecraftForge.EVENT_BUS.register(new GUIOverlayARKMode());

		EntityHandler.registerEntityEgg(EntityDodo.class, ARKCraft.MODID + ".dodo", BiomeGenBase.beach, BiomeGenBase.desert, BiomeGenBase.forest,
				BiomeGenBase.birchForest, BiomeGenBase.extremeHills);

		registerRenderers();
	}

	@Override
	protected final void registerEventHandlers()
	{
		super.registerEventHandlers();
		ClientEventHandler.init();
	}

	/* We register the block/item textures and models here */
	private void registerRenderers()
	{

		// TODO update this a bit + make client component to init manager
		InitializationManager.instance().getRegistry().forEachEntry((RegistryEntry<?> r) -> {
			r.forEachMeta((Integer meta) -> {
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(r.content, meta,
						new ModelResourceLocation(ARKCraft.MODID + ":" + r.modelLocationPrefix + r.name, "inventory"));
				ModelLoader.addVariantName(r.content, ARKCraft.MODID + ":" + r.name);
			});
			ModelLoader.addVariantName(r.content, r.getVariants());
		});

	//	for (Map.Entry<String, Block> e : ARKCraftBlocks.allBlocks.entrySet())
	//	{
	//		String name = e.getKey();
	//		Block b = e.getValue();
	///		registerBlockTexture(b, name);
	//	}

		for (Map.Entry<String, Item> e : ARKCraftItems.allItems.entrySet())
		{
			String name = e.getKey();
			Item item = e.getValue();
			registerItemTexture(item, name);
		}

		for (Entry<String, Item> i : ARKCraftRangedWeapons.allWeaponItems.entrySet())
		{
			registerItemTexture(i.getValue(), 0, i.getKey());
		}

		// Register models for entities
		registerEntityModels();

		// Register variant models for all weapon items
		registerItemVariants();

		registerBlockTexture(ARKCraftBlocks.crop_plot, 1, "crop_plot");
		registerBlockTexture(ARKCraftBlocks.crop_plot, 2, "crop_plot");
	}


	private void registerBlockTexture(final Block block, int meta, final String blockName)
	{
		registerItemTexture(Item.getItemFromBlock(block), meta, blockName);
	}

	private void registerItemTexture(final Item item, final String name)
	{
		registerItemTexture(item, 0, name);
	}

	public void registerItemTexture(final Item item, int meta, String name)
	{
		if (item instanceof ItemRangedWeapon) name = "weapons/" + name;
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta,
				new ModelResourceLocation(ARKCraft.MODID + ":" + name, "inventory"));
		ModelLoader.addVariantName(item, ARKCraft.MODID + ":" + name);
	}

	public void registerItemTexture(final Item item, final int meta, final String name, final String namePrefix)
	{
		String fullPrefix = namePrefix.substring(namePrefix.length() - 1).equals("/") ? namePrefix : (namePrefix + "/");
		String fullName = fullPrefix + name;
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta,
				new ModelResourceLocation(ARKCraft.MODID + ":" + fullName, "inventory"));
		ModelLoader.addVariantName(item, ARKCraft.MODID + ":" + name);
	}

	@Override
	public EntityPlayer getPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}

	private static void registerEntityModels()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityStone.class,
				new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ARKCraftItems.stone, Minecraft.getMinecraft().getRenderItem()));

		RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class,
				new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ARKCraftItems.grenade, Minecraft.getMinecraft().getRenderItem()));

		RenderingRegistry.registerEntityRenderingHandler(EntityDodo.class, new RenderDodo(new ModelDodo(), 0.3F));

		if (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL)
		{
			RenderingRegistry.registerEntityRenderingHandler(EntitySimpleBullet.class, new RenderSimpleBullet());
		}
		if (ModuleItemBalance.WEAPONS.SHOTGUN)
		{
			RenderingRegistry.registerEntityRenderingHandler(EntitySimpleShotgunAmmo.class, new RenderSimpleShotgunAmmo());
		}
		if (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE)
		{
			RenderingRegistry.registerEntityRenderingHandler(EntitySimpleRifleAmmo.class, new RenderSimpleRifleAmmo());
			RenderingRegistry.registerEntityRenderingHandler(EntityTranquilizer.class, new RenderTranquilizer());
		}
		if (ModuleItemBalance.WEAPONS.SPEAR)
		{
			RenderingRegistry.registerEntityRenderingHandler(EntitySpear.class, new RenderSpear());
		}
		if (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL)
		{
			RenderingRegistry.registerEntityRenderingHandler(EntityAdvancedBullet.class, new RenderAdvancedBullet());
		}
	}

	private static void registerItemVariants()
	{
		// ModelLoader.addVariantName(ARKCraftRangedWeapons.slingshot, "arkcraft:slingshot", "arkcraft:slingshot_pulled");
		ModelLoader.addVariantName(ARKCraftRangedWeapons.shotgun, "arkcraft:weapons/shotgun", "arkcraft:weapons/shotgun_reload");
		ModelLoader.addVariantName(ARKCraftRangedWeapons.longneck_rifle, "arkcraft:weapons/longneck_rifle", "arkcraft:weapons/longneck_rifle_scope",
				"arkcraft:weapons/longneck_rifle_scope_reload", "arkcraft:weapons/longneck_rifle_reload",
				"arkcraft:weapons/longneck_rifle_flashlight", "arkcraft:weapons/longneck_rifle_flashlight_reload",
				"arkcraft:weapons/longneck_rifle_laser", "arkcraft:weapons/longneck_rifle_laser_reload", "arkcraft:weapons/longneck_rifle_silencer",
				"arkcraft:weapons/longneck_rifle_silencer_reload");
		ModelLoader.addVariantName(ARKCraftRangedWeapons.simple_pistol, "arkcraft:weapons/simple_pistol", "arkcraft:weapons/simple_pistol_scope",
				"arkcraft:weapons/simple_pistol_reload", "arkcraft:weapons/simple_pistol_scope_reload", "arkcraft:weapons/simple_pistol_flashlight",
				"arkcraft:weapons/simple_pistol_flashlight_reload", "arkcraft:weapons/simple_pistol_laser",
				"arkcraft:weapons/simple_pistol_laser_reload", "arkcraft:weapons/simple_pistol_silencer",
				"arkcraft:weapons/simple_pistol_silencer_reload");
		ModelLoader.addVariantName(ARKCraftRangedWeapons.fabricated_pistol, "arkcraft:weapons/fabricated_pistol",
				"arkcraft:weapons/fabricated_pistol_scope", "arkcraft:weapons/fabricated_pistol_reload",
				"arkcraft:weapons/fabricated_pistol_scope_reload", "arkcraft:weapons/fabricated_pistol_flashlight",
				"arkcraft:weapons/fabricated_pistol_flashlight_reload", "arkcraft:weapons/fabricated_pistol_laser",
				"arkcraft:weapons/fabricated_pistol_laser_reload", "arkcraft:weapons/fabricated_pistol_silencer",
				"arkcraft:weapons/fabricated_pistol_silencer_reload", "arkcraft:weapons/fabricated_pistol_holo_scope",
				"arkcraft:weapons/fabricated_pistol_holo_scope_reload");
	}

	private static void registerBlueprintVariants()
	{
		// TODO do this
	}

	@Override
	public EntityPlayer getPlayerFromContext(MessageContext ctx)
	{
		return Minecraft.getMinecraft().thePlayer;
	}
}