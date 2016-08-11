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
public class ContainerEngram extends Container implements IContainerScrollable
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

	/* (non-Javadoc)
	 * @see com.uberverse.arkcraft.common.block.container.IContainerScrollable#getScrollingOffset()
	 */
	@Override
	public int getScrollingOffset() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.uberverse.arkcraft.common.block.container.IContainerScrollable#scroll(int)
	 */
	@Override
	public void scroll(int offset) {
	}

	/* (non-Javadoc)
	 * @see com.uberverse.arkcraft.common.block.container.IContainerScrollable#getScrollableSlotsWidth()
	 */
	@Override
	public int getScrollableSlotsWidth() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.uberverse.arkcraft.common.block.container.IContainerScrollable#getScrollableSlotsHeight()
	 */
	@Override
	public int getScrollableSlotsHeight() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.uberverse.arkcraft.common.block.container.IContainerScrollable#getScrollableSlotsCount()
	 */
	@Override
	public int getScrollableSlotsCount() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.uberverse.arkcraft.common.block.container.IContainerScrollable#getRequiredSlotsCount()
	 */
	@Override
	public int getRequiredSlotsCount() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.uberverse.arkcraft.common.block.container.IContainerScrollable#getMaxOffset()
	 */
	@Override
	public int getMaxOffset() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.uberverse.arkcraft.common.block.container.IContainerScrollable#getRelativeScrollingOffset()
	 */
	@Override
	public double getRelativeScrollingOffset() {
		
		return 0;
	}
	
}
