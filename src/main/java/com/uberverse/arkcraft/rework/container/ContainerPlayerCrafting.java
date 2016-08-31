package com.uberverse.arkcraft.rework.container;

import com.uberverse.arkcraft.rework.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.rework.engram.EngramManager.EngramType;

import net.minecraft.entity.player.EntityPlayer;

public class ContainerPlayerCrafting extends ContainerEngramCrafting
{
	public ContainerPlayerCrafting(EntityPlayer player)
	{
		super(EngramType.PLAYER, player, ARKPlayer.get(player).getEngramCrafter());
	}

	@Override
	public int getScrollableSlotsWidth()
	{
		return 5;
	}

	@Override
	public int getScrollableSlotsHeight()
	{
		return 6;
	}

	@Override
	public int getScrollableSlotsX()
	{
		return 25;
	}

	@Override
	public int getScrollableSlotsY()
	{
		return 26;
	}

	@Override
	public int getPlayerInventorySlotsX()
	{
		return 8;
	}

	@Override
	public int getPlayerInventorySlotsY()
	{
		return 174;
	}

	@Override
	public int getPlayerHotbarSlotsX()
	{
		return 8;
	}

	@Override
	public int getPlayerHotbarSlotsY()
	{
		return 232;
	}

	@Override
	public int getInventorySlotsX()
	{
		return 0;
	}

	@Override
	public int getInventorySlotsY()
	{
		return 0;
	}

	@Override
	public int getInventorySlotsWidth()
	{
		return 0;
	}

	@Override
	public int getInventorySlotsHeight()
	{
		return 0;
	}
}
