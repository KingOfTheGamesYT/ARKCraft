package com.arkcraft.registry;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.block.BlockBerryBush;
import com.arkcraft.common.block.BlockGreenScreen;
import com.arkcraft.common.block.BlockLight;
import com.arkcraft.common.block.crafter.*;
import com.arkcraft.common.block.energy.BlockCable;
import com.arkcraft.common.block.energy.BlockCreativeGenerator;
import com.arkcraft.common.block.energy.BlockElectricLamp;
import com.arkcraft.common.block.energy.BlockElectricOutlet;
import com.arkcraft.common.block.resource.*;
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
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = ARKCraft.MODID)
public class BlockRegistryHandler {
	@SubscribeEvent
	public static void handleBlockRegistry(RegistryEvent.Register<Block> registerEvent) {
		IForgeRegistry<Block> registry = registerEvent.getRegistry();
		//Blocks
		registry.registerAll(
				new BlockLight(),
				new BlockBerryBush(),
				new BlockGreenScreen(),
				new BlockSmithy(),
				new BlockMortarAndPestle(),
				new BlockCropPlot(),
				new BlockRefiningForge(),
				new BlockCampfire(),
				new BlockFabricator(),
				new BlockCable(),
				new BlockCreativeGenerator(),
				new BlockElectricOutlet(),
				new BlockElectricLamp(),
				new BlockRockResource(),
				new BlockMetalResource(),
				new BlockObsidianResource(),
				new BlockCrystalResource(),
				new BlockOilResource(),
				new BlockSmallRockResource()
		);

		//TileEntities
		registerTileEntity("te_crop_plot", TileEntityCropPlot.class);
		registerTileEntity("te_mortar_and_pestle", TileEntityMP.class);
		registerTileEntity("te_compost_bin", TileEntityCompostBin.class);
		registerTileEntity("te_smithy", TileEntitySmithy.class);
		registerTileEntity("te_refining_forge", TileEntityRefiningForge.class);
		registerTileEntity("te_campfire", TileEntityCampfire.class);
		registerTileEntity("te_crystal", TileEntityCrystal.class);
		registerTileEntity("te_fabricator", TileEntityFabricator.class);
		registerTileEntity("te_cable", TileEntityCable.class);
		registerTileEntity("te_creative_generator", TileEntityCreativeGenerator.class);
		registerTileEntity("te_electric_outlet", TileEntityElectricOutlet.class);
		registerTileEntity("te_electric_lamp", TileEntityElectricLamp.class);
	}

	private static <E extends TileEntity> void registerTileEntity(String name, Class<E> tileEntityClass) {
		GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(ARKCraft.MODID, name));
	}
}
