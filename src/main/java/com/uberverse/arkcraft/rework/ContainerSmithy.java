package com.uberverse.arkcraft.rework;

import com.uberverse.arkcraft.rework.EngramManager.EngramType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSmithy extends ContainerEngramCrafting
{
	public ContainerSmithy(EntityPlayer player, TileEntitySmithy tileEntity)
	{
		super(EngramType.SMITHY, player, tileEntity);
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
		return 98;
	}

	@Override
	public int getScrollableSlotsY()
	{
		return 18;
	}

	@Override
	public int getPlayerInventorySlotsX()
	{
		return 8;
	}

	@Override
	public int getPlayerInventorySlotsY()
	{
		return 140;
	}

	@Override
	public int getPlayerHotbarSlotsX()
	{
		return 8;
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

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (slot.inventory == getIInventory())
			{

				if (!this.mergeItemStack(itemstack1, playerInvBoundLeft, playerInvBoundRight,
						false)) return null;

			}
			else if (slot.inventory == getPlayerInventory()) if (!this.mergeItemStack(itemstack1,
					invBoundLeft, invBoundRight, false)) return null;
			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}
}
