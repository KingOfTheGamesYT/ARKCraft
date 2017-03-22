package com.uberverse.arkcraft.client.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.event.ClientEventHandler;
import com.uberverse.arkcraft.client.gui.overlay.GUIOverlayARKMode;
import com.uberverse.arkcraft.client.gui.overlay.GUIOverlayReloading;
import com.uberverse.arkcraft.client.model.ModelDodo;
import com.uberverse.arkcraft.client.render.creature.RenderDodo;
import com.uberverse.arkcraft.client.render.projectile.RenderAdvancedBullet;
import com.uberverse.arkcraft.client.render.projectile.RenderSimpleBullet;
import com.uberverse.arkcraft.client.render.projectile.RenderSimpleRifleAmmo;
import com.uberverse.arkcraft.client.render.projectile.RenderSimpleShotgunAmmo;
import com.uberverse.arkcraft.client.render.projectile.RenderSpear;
import com.uberverse.arkcraft.client.render.projectile.RenderStoneArrow;
import com.uberverse.arkcraft.client.render.projectile.RenderTranquilizer;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.common.entity.EntityDodo;
import com.uberverse.arkcraft.common.entity.projectile.EntityAdvancedBullet;
import com.uberverse.arkcraft.common.entity.projectile.EntitySimpleBullet;
import com.uberverse.arkcraft.common.entity.projectile.EntitySimpleRifleAmmo;
import com.uberverse.arkcraft.common.entity.projectile.EntitySimpleShotgunAmmo;
import com.uberverse.arkcraft.common.entity.projectile.EntitySpear;
import com.uberverse.arkcraft.common.entity.projectile.EntityStone;
import com.uberverse.arkcraft.common.entity.projectile.EntityStoneArrow;
import com.uberverse.arkcraft.common.entity.projectile.EntityTranquilizer;
import com.uberverse.arkcraft.common.item.IMeshedItem;
import com.uberverse.arkcraft.common.item.ItemBlueprint;
import com.uberverse.arkcraft.common.item.ranged.ItemRangedWeapon;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.init.ARKCraftBlocks;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.init.InitializationManager;
import com.uberverse.arkcraft.init.InitializationManager.RegistryEntry;
import com.uberverse.arkcraft.util.CollectionUtil;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);

		dossierProxy.init();

		registerRenderers();
	}

	@Override
	protected final void registerEventHandlers()
	{
		super.registerEventHandlers();
		ClientEventHandler.init();
		MinecraftForge.EVENT_BUS.register(new GUIOverlayReloading());
		MinecraftForge.EVENT_BUS.register(new GUIOverlayARKMode());
	}

	/* We register the block/item textures and models here */
	private void registerRenderers()
	{
		// TODO update this a bit + make client component to init manager
		registerBlockRenderer();
		InitializationManager.instance().getRegistry().forEachEntry((RegistryEntry<?> r) -> {
			if (r.standardRender) {
				r.forEachMeta((Integer meta) -> {
					Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(r.content, meta, new ModelResourceLocation(ARKCraft.MODID + ":" + r.modelLocationPrefix + r.name, "inventory"));
				});
			}
			Collection<String> v = new ArrayList<>(Arrays.asList(r.getVariants()));
			v.add(ARKCraft.MODID + ":" + r.modelLocationPrefix + r.name);
			ModelLoader.registerItemVariants(r.content, CollectionUtil.convert(v, (e) -> new ResourceLocation(e)).toArray(new ResourceLocation[0]));
		});

		// TODO this can also render other item's models for this one!
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ARKCraftItems.blueprint, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack)
			{
				return new ModelResourceLocation(ARKCraft.instance().modid() + ":blueprint/" + ItemBlueprint.getEngram(stack).getName(), "inventory");
			}
		});

		// Register models for entities
		registerEntityModels();

		registerBlockTexture(ARKCraftBlocks.cropPlot, 1, "crop_plot");
		registerBlockTexture(ARKCraftBlocks.cropPlot, 2, "crop_plot");

		meshedItems.forEach(i -> Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register((Item) i, i::getModel));
	}

	public static void registerBlockRenderer()
	{

		// reg(ModBlocks.tutorialTileEntity);

		// ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrystal.class,
		// new TileEntityCrystalRender());
	}

	public static void reg(Block block)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(ARKCraft.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
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
		if (item instanceof ItemRangedWeapon)
			name = "weapons/" + name;
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(ARKCraft.MODID + ":" + name, "inventory"));
		ModelLoader.registerItemVariants(item, new ResourceLocation(ARKCraft.MODID, name));
	}

	public void registerItemTexture(final Item item, final int meta, final String name, final String namePrefix)
	{
		String fullPrefix = namePrefix.substring(namePrefix.length() - 1).equals("/") ? namePrefix : (namePrefix + "/");
		String fullName = fullPrefix + name;
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(ARKCraft.MODID + ":" + fullName, "inventory"));
		ModelLoader.registerItemVariants(item, new ResourceLocation(ARKCraft.MODID, name));
	}

	@Override
	public EntityPlayer getPlayer()
	{
		return Minecraft.getMinecraft().player;
	}

	private static void registerEntityModels()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityStone.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ARKCraftItems.stone, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityStoneArrow.class, new RenderStoneArrow());

		// RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class,
		// new RenderSnowball(Minecraft.getMinecraft().getRenderManager(),
		// ARKCraftItems.grenade, Minecraft.getMinecraft().getRenderItem()));

		RenderingRegistry.registerEntityRenderingHandler(EntityDodo.class, new RenderDodo(new ModelDodo(), 0.3F));

		if (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL) {
			RenderingRegistry.registerEntityRenderingHandler(EntitySimpleBullet.class, new RenderSimpleBullet());
		}
		if (ModuleItemBalance.WEAPONS.SHOTGUN) {
			RenderingRegistry.registerEntityRenderingHandler(EntitySimpleShotgunAmmo.class, new RenderSimpleShotgunAmmo());
		}
		if (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE) {
			RenderingRegistry.registerEntityRenderingHandler(EntitySimpleRifleAmmo.class, new RenderSimpleRifleAmmo());
			RenderingRegistry.registerEntityRenderingHandler(EntityTranquilizer.class, new RenderTranquilizer());
		}
		if (ModuleItemBalance.WEAPONS.SPEAR) {
			RenderingRegistry.registerEntityRenderingHandler(EntitySpear.class, new RenderSpear());
		}
		if (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL) {
			RenderingRegistry.registerEntityRenderingHandler(EntityAdvancedBullet.class, new RenderAdvancedBullet());
		}
	}

	@Override
	public EntityPlayer getPlayerFromContext(MessageContext ctx)
	{
		return Minecraft.getMinecraft().player;
	}

	@Override
	public long getTime()
	{
		return Minecraft.getSystemTime();
	}

	@Override
	public long getWorldTime()
	{
		if (Minecraft.getMinecraft().world != null)
			return Minecraft.getMinecraft().world.getTotalWorldTime();
		return 0;
	}
	List<IMeshedItem> meshedItems = new ArrayList<>();
	@Override
	public void registerModelMeshDef(IMeshedItem i) {
		meshedItems.add(i);
	}
}