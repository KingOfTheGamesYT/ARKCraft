package com.uberverse.arkcraft.common.block.container;

import java.util.Random;

import com.uberverse.arkcraft.client.gui.GUIEngram;
import com.uberverse.arkcraft.common.inventory.InventoryPlayerEngram;
import com.uberverse.arkcraft.common.item.engram.ARKCraftEngrams;
import com.uberverse.arkcraft.common.item.engram.Engram;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;

/**
 * @author ERBF
 *
 */
public class ContainerEngram extends Container implements IContainerScrollable
{

	private static InventoryPlayerEngram invEngram;
	
	public ContainerEngram(InventoryPlayerEngram inventory, EntityPlayer player)
	{
		invEngram = inventory;
		
		int index = 0;
		for (int y = 0; y < 4; ++y) {
	        for (int x = 0; x < 8; ++x) {
	            this.addSlotToContainer(new EngramSlot(inventory, index, 1 + x * 20, 44 + y * 20));
	            try {
	            	inventory.setInventorySlotContents(index, new ItemStack(ARKCraftEngrams.engramList.get(index)));
	            } catch (IndexOutOfBoundsException e) {}
	            index++;
	        }
	    }
	}
	
	public static InventoryPlayerEngram getEngramInventory()
	{
		return invEngram;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return false;
	}

	@Override
	public int getScrollingOffset() {
		return 0;
	}

	@Override
	public void scroll(int offset) {
	}

	@Override
	public int getScrollableSlotsWidth() {
		return 18;
	}

	@Override
	public int getScrollableSlotsHeight() {
		return 18;
	}

	@Override
	public int getScrollableSlotsCount() {
		return 32;
	}

	@Override
	public int getRequiredSlotsCount() {
		return 8;
	}

	@Override
	public int getMaxOffset() {
		return 18;
	}

	@Override
	public double getRelativeScrollingOffset() {
		return 18;
	}
	
	public class EngramSlot extends Slot {

		public EngramSlot(InventoryPlayerEngram inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public boolean canTakeStack(EntityPlayer playerIn)
	    {
	        return true;
	    }
		
		@Override
		public int getSlotStackLimit()
		{
			return 1;
		}
		
		@Override
		public boolean isItemValid(ItemStack stack)
	    {
	        return false;
	    }
		
		@Override
		public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) throws NullPointerException, ReportedException
	    {
	        if(stack.getItem() instanceof Engram) {
	        	Engram engram = (Engram) stack.getItem();
				GUIEngram.setEngramTitle(engram.getFormattedName());
				GUIEngram.setEngramDescription(engram.getFormattedDesc());
				ContainerEngram.getEngramInventory().setInventorySlotContents(this.getSlotIndex(), playerIn.inventory.getItemStack());
				playerIn.inventory.getItemStack();
	        }
	    }
		
	}
	
}
