package com.uberverse.arkcraft.common.tileentity.crafter.burner;

import com.uberverse.arkcraft.common.burner.BurnerManager.BurnerType;

public class TileEntityCampfire extends TileEntityBurner
{
	@Override
	public int getSizeInventory()
	{
		return 4;
	}

	@Override
	public BurnerType getBurnerType()
	{
		return BurnerType.CAMPFIRE;
	}
}
