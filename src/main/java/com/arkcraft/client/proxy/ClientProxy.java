package com.arkcraft.client.proxy;

import com.arkcraft.ARKCraft;
import com.arkcraft.client.ARKCustomModelLoader;
import com.arkcraft.client.event.ClientEventHandler;
import com.arkcraft.client.model.ModelCable;
import com.arkcraft.client.model.ModelDodo;
import com.arkcraft.client.render.creature.RenderDodo;
import com.arkcraft.client.render.projectile.*;
import com.arkcraft.client.tesr.TileEntityElectricOutletSpecialRenderer;
import com.arkcraft.common.config.ModuleItemBalance;
import com.arkcraft.common.entity.EntityDodo;
import com.arkcraft.common.entity.projectile.*;
import com.arkcraft.common.item.IMeshedItem;
import com.arkcraft.common.item.ItemBlueprint;
import com.arkcraft.common.item.ranged.ItemRangedWeapon;
import com.arkcraft.common.proxy.CommonProxy;
import com.arkcraft.common.tileentity.energy.TileEntityElectricOutlet;
import com.arkcraft.init.ARKCraftBlocks;
import com.arkcraft.init.ARKCraftItems;
import com.arkcraft.init.InitializationManager;
import com.arkcraft.util.CollectionUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ClientProxy extends CommonProxy {
	List<IMeshedItem> meshedItems = new ArrayList<>();

	public static void registerBlockRenderer() {

		// reg(ModBlocks.tutorialTileEntity);

		// ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrystal.class,
		// new TileEntityCrystalRender());
	}

	public static void reg(Block block) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(ARKCraft.MODID + ":" + block.getTranslationKey().substring(5), "inventory"));
	}

	private static void registerEntityModels() {
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
	public void init(FMLInitializationEvent event) {
		super.init(event);

		dossierProxy.init();

		ClientEventHandler.init();

		registerRenderers();
	}

	/* We register the block/item textures and models here */
	private void registerRenderers() {
		// TODO update this a bit + make client component to init manager
		registerBlockRenderer();
		InitializationManager.instance().getRegistry().forEachEntry((InitializationManager.RegistryEntry<?> r) -> {
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
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return new ModelResourceLocation(ARKCraft.instance().modid() + ":blueprint/" + ItemBlueprint.getEngram(stack).getName(), "inventory");
			}
		});

		// Register models for entities
		registerEntityModels();

		registerBlockTexture(ARKCraftBlocks.cropPlot, 1, "crop_plot");
		registerBlockTexture(ARKCraftBlocks.cropPlot, 2, "crop_plot");
		registerBlockTexture(ARKCraftBlocks.cable, 1, "cable_vert");

		meshedItems.forEach(i -> Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register((Item) i, i::getModel));

		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
				return worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : (ColorizerGrass.getGrassColor(0.5D, 1.0D));
			}
		}, new Block[]{ARKCraftBlocks.berryBush});
		ARKCustomModelLoader.init();
		ARKCustomModelLoader.instance.modelMap.put(new ResourceLocation("arkcraft:cable"), new ModelCable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectricOutlet.class, new TileEntityElectricOutletSpecialRenderer());
	}

	private void registerBlockTexture(final Block block, int meta, final String blockName) {
		registerItemTexture(Item.getItemFromBlock(block), meta, blockName);
	}

	private void registerItemTexture(final Item item, final String name) {
		registerItemTexture(item, 0, name);
	}

	public void registerItemTexture(final Item item, int meta, String name) {
		if (item instanceof ItemRangedWeapon)
			name = "weapons/" + name;
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(ARKCraft.MODID + ":" + name, "inventory"));
		ModelLoader.registerItemVariants(item, new ResourceLocation(ARKCraft.MODID, name));
	}

	public void registerItemTexture(final Item item, final int meta, final String name, final String namePrefix) {
		String fullPrefix = namePrefix.substring(namePrefix.length() - 1).equals("/") ? namePrefix : (namePrefix + "/");
		String fullName = fullPrefix + name;
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(ARKCraft.MODID + ":" + fullName, "inventory"));
		ModelLoader.registerItemVariants(item, new ResourceLocation(ARKCraft.MODID, name));
	}

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}

	@Override
	public EntityPlayer getPlayerFromContext(MessageContext ctx) {
		return Minecraft.getMinecraft().player;
	}

	@Override
	public long getTime() {
		return Minecraft.getSystemTime();
	}

	@Override
	public long getWorldTime() {
		if (Minecraft.getMinecraft().world != null)
			return Minecraft.getMinecraft().world.getTotalWorldTime();
		return 0;
	}

	@Override
	public void registerModelMeshDef(IMeshedItem i) {
		meshedItems.add(i);
	}
}