package com.uberverse.arkcraft.common.block.container;

import com.uberverse.arkcraft.common.inventory.InventoryPlayerEngram;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

/**
 * @author ERBF
 *
 */
public class ContainerEngram extends Container implements IContainerScrollable
{

	public ContainerEngram(InventoryPlayerEngram inventory, EntityPlayer player)
	{
		for (int y = 0; y < 4; ++y) {
	        for (int x = 0; x < 8; ++x) {
	            this.addSlotToContainer(new EngramSlot(inventory, x + y * 3, 1 + x * 20, 44 + y * 20));
	        }
	    }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return true;
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
	
	public class EngramSlot extends Slot {

		public EngramSlot(InventoryPlayerEngram inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public boolean canTakeStack(EntityPlayer playerIn)
	    {
	        return false;
	    }
		
		@Override
		public int getSlotStackLimit()
		{
			return 1;
		}
		
	}
	
}
