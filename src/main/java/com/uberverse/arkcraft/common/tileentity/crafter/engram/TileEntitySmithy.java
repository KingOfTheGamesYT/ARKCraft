package com.uberverse.arkcraft.common.tileentity.crafter.engram;

import java.util.Random;

public class TileEntitySmithy extends TileEntityEngramCrafter
{
	@Override     
	public void update()     
	{         
		super.update();         
		if (isCrafting() && new Random().nextInt(50) == 0) worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(),                 "arkcraft:smithyhammer" + (new Random().nextInt(4) + 1), 1, 1, true);    
		}
	public TileEntitySmithy()
	{
		super(24, "smithy");
	}
}
