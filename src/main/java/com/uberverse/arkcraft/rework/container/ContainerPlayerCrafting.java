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
		return 4;
	}

	@Override
	public int getScrollableSlotsHeight()
	{
		return 6;
	}

	@Override
	public int getScrollableSlotsX()
	{
		return 26;
	}

	@Override
	public int getScrollableSlotsY()
	{
		return 18;
	}

	@Override
	public int getPlayerInventorySlotsX()
	{
		return 24;
	}

	@Override
	public int getPlayerInventorySlotsY()
	{
		return 174;
	}

	@Override
	public int getPlayerHotbarSlotsX()
	{
		return getPlayerInventorySlotsX();
	}

	@Override
	public int getPlayerHotbarSlotsY()
	{
		return getPlayerInventorySlotsY()+58;
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

	@Override
	public int getQueueSlotsWidth()
	{
		return 5;
	}

	@Override
	public int getQueueSlotsHeight()
	{
		return 1;
	}

	@Override
	public int getQueueSlotsX()
	{
		return 106;
	}

	@Override
	public int getQueueSlotsY()
	{
		return 133;
	}
}
