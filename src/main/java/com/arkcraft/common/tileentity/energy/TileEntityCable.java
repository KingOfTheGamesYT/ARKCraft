package com.arkcraft.common.tileentity.energy;

import com.arkcraft.init.ARKCraftBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;

import com.arkcraft.common.tileentity.crafter.TileEntityArkCraft;

public class TileEntityCable extends TileEntityArkCraft implements IEnergyGridDevice {
	public CableType type = CableType.NORMAL;
	public EnergyGrid grid;
	public byte connections;
	private boolean powered;
	public TileEntityCable checkConnections() {
		for(EnumFacing f : EnumFacing.HORIZONTALS){
			BlockPos p = pos.offset(f);
			connections &= ~(1 << f.ordinal());
			TileEntity tile = world.getTileEntity(p);
			if(tile != null && tile instanceof IEnergyGridDevice){
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
		compound.setInteger("cableType", type.ordinal());
		compound.setBoolean("powered", powered);
		return super.writeToNBT(compound);
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		type = CableType.VALUES[compound.getInteger("cableType") % CableType.VALUES.length];
		powered = compound.getBoolean("powered");
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
		world.markBlockRangeForRenderUpdate(pos, pos);
	}
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}
	@Override
	public void invalidateGrid() {
		grid = null;
		markNetDirty();
	}
	public static enum CableType {
		NORMAL, VERTICAL
		;
		public static final CableType[] VALUES = values();
	}
	@Override
	public void setGrid(EnergyGrid grid) {
		this.grid = grid;
		markNetDirty();
	}
	@Override
	public BlockPos getPos2() {
		return pos;
	}
	@Override
	public boolean canConnect(EnumFacing face) {
		switch (type) {
		case NORMAL:
			return face.getAxis() != Axis.Y;
		case VERTICAL:
			return true;
		default:
			break;
		}
		return false;
	}
	public boolean isPowered() {
		return powered;
	}
	@Override
	public void markNetDirty() {
		boolean p = powered;
		powered = grid != null ? grid.isActive() : false;
		if(p != powered)markBlockForUpdate();
	}
	public void invalidateGrid2() {
		if(grid != null)grid.invalidate();
	}
}
