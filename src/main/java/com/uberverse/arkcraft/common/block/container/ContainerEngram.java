package com.uberverse.arkcraft.common.block.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * @author ERBF
 *
 */
public class ContainerEngram extends Container 
{

	public ContainerEngram(InventoryPlayer inventory, EntityPlayer player)
	{
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		
		return false;
	}

	public class EngramSlot extends Slot {

		public EngramSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public boolean canTakeStack(EntityPlayer playerIn)
	    {
	        return false;
	    }
		
	}
	
}
