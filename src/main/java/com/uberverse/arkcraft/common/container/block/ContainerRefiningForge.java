package com.uberverse.arkcraft.common.container.block;

import com.uberverse.arkcraft.common.container.burner.ContainerBurner;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityRefiningForge;

import net.minecraft.entity.player.EntityPlayer;

public class ContainerRefiningForge extends ContainerBurner
{
	public ContainerRefiningForge(TileEntityRefiningForge burner, EntityPlayer player)
	{
		super(burner, player);
	}

	@Override
	public int getSlotsX()
	{
		return 53;
	}

	@Override
	public int getSlotsY()
	{
		return 26;
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
