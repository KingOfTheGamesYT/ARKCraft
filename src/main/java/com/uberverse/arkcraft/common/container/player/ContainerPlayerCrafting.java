package com.uberverse.arkcraft.common.container.player;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.container.engram.ContainerEngramCrafting;
import com.uberverse.arkcraft.common.engram.EngramManager.EngramType;
import com.uberverse.arkcraft.common.proxy.CommonProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerPlayerCrafting extends ContainerEngramCrafting
{
	public ContainerPlayerCrafting(EntityPlayer player)
	{
		super(EngramType.PLAYER, player,
				ARKPlayer.get(player).getEngramCrafter());
	}

	@Override
	public boolean enchantItem(EntityPlayer playerIn, int id)
	{
		if (id == 2)
		{
			playerIn.openGui(ARKCraft.instance(), CommonProxy.GUI.ENGRAMS.id,
					playerIn.worldObj, 0, 0, 0);
			return true;
		}
		return super.enchantItem(playerIn, id);
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
		return getPlayerInventorySlotsY() + 58;
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

	@Override
	protected IInventory getBlueprintInventory()
	{
		return getPlayerInventory();
	}
}
