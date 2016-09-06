package com.uberverse.arkcraft.common.tileentity.crafter.burner;

import com.uberverse.arkcraft.common.burner.BurnerManager.BurnerType;

public class TileEntityRefiningForge extends TileEntityBurner
{
	@Override
	public int getSizeInventory()
	{
		return 8;
	}

	@Override
	public BurnerType getBurnerType()
	{
		return BurnerType.REFINING_FORGE;
	}
}
