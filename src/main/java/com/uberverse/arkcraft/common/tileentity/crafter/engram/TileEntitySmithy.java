package com.uberverse.arkcraft.common.tileentity.crafter.engram;

import java.util.Random;

public class TileEntitySmithy extends TileEntityEngramCrafter
{
	@Override
	public void update()
	{
		super.update();
		if (isCrafting() && new Random().nextInt(100) == 0) worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(),
				"arkcraft:smithy_hammer", 1, 0, true);
	}

	public TileEntitySmithy()
	{
		super(24, "smithy");
	}
}
