package com.arkcraft.common.container.block;

import com.arkcraft.common.engram.EngramManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import com.arkcraft.common.container.engram.ContainerEngramCrafting;
import com.arkcraft.common.tileentity.crafter.engram.TileEntityFabricator;

public class ContainerFabricator  extends ContainerEngramCrafting
{
	private TileEntityFabricator te;
	public ContainerFabricator(EntityPlayer player, TileEntityFabricator tileEntity)
	{
		super(EngramManager.EngramType.FABRICATOR, player, tileEntity);
		this.te = tileEntity;
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
	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id) {
		if(id == 3 || id == 4){
			te.setActive(id == 3 ? true : false);
			return true;
		}else
			return super.enchantItem(playerIn, id);
	}
}