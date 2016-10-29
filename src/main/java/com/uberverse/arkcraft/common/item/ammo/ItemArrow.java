package com.uberverse.arkcraft.common.item.ammo;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.item.ranged.ItemARKBow;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemArrow extends Item
{
	private String name;
	
	public ItemArrow(String name)
	{
		this.setCreativeTab(ARKCraft.tabARK);
		this.name = name;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) 
	{
		if(playerIn.isSneaking())
		{
			for(int i=0; i < playerIn.inventory.getSizeInventory(); i++)
			{
				ItemStack item = playerIn.inventory.getStackInSlot(i);
				if(item != null && item.getItem() instanceof ItemARKBow)
				{
				//	getSelecetedArrow(itemStackIn);
					ItemARKBow.setArrowType(itemStackIn, name);
					System.out.println("Arrow selected " +  itemStackIn + name);

				}
			}
			
		}
		return itemStackIn;
	}	

}
