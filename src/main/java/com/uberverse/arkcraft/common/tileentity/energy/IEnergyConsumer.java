package com.uberverse.arkcraft.common.tileentity.energy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface IEnergyConsumer {
	void setPowered(boolean powered);
	Vec3d getRenderPoint();
	BlockPos getPos2();
}
