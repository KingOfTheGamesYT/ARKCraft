package com.uberverse.arkcraft.common.tileentity.energy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import com.uberverse.arkcraft.common.block.energy.BlockElectricLamp;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityArkCraft;
import com.uberverse.arkcraft.init.ARKCraftBlocks;

public class TileEntityElectricLamp extends TileEntityArkCraft implements IEnergyConsumer {

	@Override
	public void setPowered(boolean powered) {
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() == ARKCraftBlocks.electricLamp){
			TileEntity tileentity = world.getTileEntity(pos);
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockElectricLamp.POWERED, powered), 3);
			if (tileentity != null) {
				tileentity.validate();
				world.setTileEntity(pos, tileentity);
			}
			markDirty();
		}
	}

	@Override
	public Vec3d getRenderPoint() {
		return new Vec3d(.5, .5, .5);
	}

	@Override
	public BlockPos getPos2() {
		return pos;
	}

}
