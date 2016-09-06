package com.uberverse.arkcraft.common.container.block;

import com.uberverse.arkcraft.common.container.burner.ContainerBurner;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityCampfire;

import net.minecraft.entity.player.EntityPlayer;

public class ContainerCampfire extends ContainerBurner
{
	public ContainerCampfire(TileEntityCampfire burner, EntityPlayer player)
	{
		super(burner, player);
	}

	public int getSlotsX()
	{
		return 53;
	}

	@Override
	public int getSlotsY()
	{
		return 35;
	}

	@Override
	public int getSlotsWidth()
	{
		return 4;
	}

	@Override
	public int getPlayerInventorySlotsX()
	{
		return 8;
	}

	@Override
	public int getPlayerInventorySlotsY()
	{
		return 84;
	}

	@Override
	public int getPlayerHotbarSlotsX()
	{
		return 8;
	}

	@Override
	public int getPlayerHotbarSlotsY()
	{
		return 142;
	}
}
