package com.arkcraft.common.tileentity.energy;

import net.minecraft.util.EnumFacing;

public class TileEntityCreativeGenerator extends TileEntityGenerator {
	public boolean active;
	@Override
	public boolean canConnect(EnumFacing face) {
		return true;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void updateI() {
		if(!world.isRemote){
			boolean a = active;
			active = world.isBlockIndirectlyGettingPowered(pos) > 0;
			if(a != active){
				grid.markNetDirty();
			}
		}
	}

}
