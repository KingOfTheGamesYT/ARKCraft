package com.arkcraft.common.container.block;

import com.arkcraft.common.engram.EngramManager;
import com.arkcraft.common.container.engram.ContainerEngramCrafting;
import com.arkcraft.common.tileentity.crafter.engram.TileEntitySmithy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerSmithy extends ContainerEngramCrafting
{
	public ContainerSmithy(EntityPlayer player, TileEntitySmithy tileEntity)
	{
		super(EngramManager.EngramType.SMITHY, player, tileEntity);
	}

	@Override
	public int getScrollableSlotsWidth()
	{
		return 3;
	}

	@Override
	public int getScrollableSlotsHeight()
	{
		return 5;
	}

	@Override
	public int getScrollableSlotsX()
	{
		return 124;
	}

	@Override
	public int getScrollableSlotsY()
	{
		return 18;
	}

	@Override
	public int getPlayerInventorySlotsX()
	{
		return 20;
	}

	@Override
	public int getPlayerInventorySlotsY()
	{
		return 140;
	}

	@Override
	public int getPlayerHotbarSlotsX()
	{
		return getPlayerInventorySlotsX();
	}

	@Override
	public int getPlayerHotbarSlotsY()
	{
		return 198;
	}

	@Override
	public int getInventorySlotsX()
	{
		return 8;
	}

	@Override
	public int getInventorySlotsY()
	{
		return 18;
	}

	@Override
	public int getInventorySlotsWidth()
	{
		return 4;
	}

	@Override
	public int getInventorySlotsHeight()
	{
		return 6;
	}

	@Override
	public int getQueueSlotsWidth()
	{
		return 1;
	}

	@Override
	public int getQueueSlotsHeight()
	{
		return 5;
	}

	@Override
	public int getQueueSlotsX()
	{
		return 93;
	}

	@Override
	public int getQueueSlotsY()
	{
		return 18;
	}

	@Override
	protected IInventory getBlueprintInventory()
	{
		return getIInventory();
	}
}
