package com.uberverse.arkcraft.common.block.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

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

}
