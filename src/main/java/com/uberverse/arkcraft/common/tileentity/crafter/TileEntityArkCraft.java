package com.uberverse.arkcraft.common.tileentity.crafter;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityArkCraft extends TileEntity
{
	public void writeToPacket(NBTTagCompound tag)
	{
	}

	public void readFromPacket(NBTTagCompound tag)
	{
	}
	public final void markBlockForUpdate(BlockPos pos){
		markBlockForUpdate(world, pos);
	}
	public static final void markBlockForUpdate(World world, BlockPos pos){
		IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 2);
	}
	public final void markBlockForUpdate(){
		markBlockForUpdate(pos);
	}
}
