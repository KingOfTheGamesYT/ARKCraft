package com.arkcraft.common.block.energy;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.arkcraft.common.block.crafter.BlockARKContainer;
import com.arkcraft.common.tileentity.energy.TileEntityElectricOutlet;
import com.arkcraft.common.tileentity.energy.TileEntityGenerator;

public class BlockElectricOutlet extends BlockARKContainer {

	public BlockElectricOutlet() {
		super(Material.IRON);
		setHardness(10);
		setResistance(100);
	}

	@Override
	public int getId() {
		return -1;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityElectricOutlet();
	}
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityGenerator)((TileEntityGenerator)te).invalidateGrid2();
	}
}
