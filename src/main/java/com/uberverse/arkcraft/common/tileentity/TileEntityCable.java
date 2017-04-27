package com.uberverse.arkcraft.common.tileentity;

import net.minecraft.tileentity.TileEntity;

public class TileEntityCable extends TileEntity {
	public boolean hasCenter;
	public byte connections;
	public TileEntityCable checkConnections() {
		return this;
	}

}
