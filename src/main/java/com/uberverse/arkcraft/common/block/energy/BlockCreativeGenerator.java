package com.uberverse.arkcraft.common.block.energy;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import com.uberverse.arkcraft.common.tileentity.energy.TileEntityCreativeGenerator;
import com.uberverse.arkcraft.common.tileentity.energy.TileEntityGenerator;

public class BlockCreativeGenerator extends BlockGenerator {

	public BlockCreativeGenerator() {
		super(Material.IRON);
		setBlockUnbreakable();
		setResistance(18000000F);
	}

	@Override
	public int getId() {
		return -1;
	}

	@Override
	public TileEntityGenerator createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCreativeGenerator();
	}

}
