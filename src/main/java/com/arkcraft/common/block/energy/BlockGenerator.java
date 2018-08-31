package com.arkcraft.common.block.energy;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.arkcraft.common.block.crafter.BlockARKContainer;
import com.arkcraft.common.tileentity.energy.TileEntityGenerator;

public abstract class BlockGenerator extends BlockARKContainer {

	protected BlockGenerator(Material materialIn) {
		super(materialIn);
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public abstract TileEntityGenerator createNewTileEntity(World worldIn, int meta);

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityGenerator)((TileEntityGenerator)te).invalidateGrid2();
	}
}
