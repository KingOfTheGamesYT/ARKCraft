package com.uberverse.arkcraft.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import com.uberverse.arkcraft.init.ARKCraftBlocks;

public class TileEntityCable extends TileEntity {
	public boolean hasVertical, hasBase;
	public byte connections;
	public TileEntityCable checkConnections() {
		for(EnumFacing f : EnumFacing.HORIZONTALS){
			BlockPos p = pos.offset(f);
			connections &= ~(1 << f.ordinal());
			if(world.getBlockState(p).getBlock() == ARKCraftBlocks.cable){
				connections |= 1 << f.ordinal();
			}
		}
		connections &= ~(1 << EnumFacing.UP.ordinal());
		if(world.getBlockState(pos.up()).getBlock() == ARKCraftBlocks.cable){
			connections |= 1 << EnumFacing.UP.ordinal();
		}
		return this;
	}
	public final boolean connects(EnumFacing side) {
		return (connections & (1 << side.ordinal())) != 0;
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("hasBase", hasBase);
		compound.setBoolean("hasVertical", hasVertical);
		return super.writeToNBT(compound);
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		hasBase = compound.getBoolean("hasBase");
		hasVertical = compound.getBoolean("hasVertical");
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new SPacketUpdateTileEntity(pos, 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}
}
