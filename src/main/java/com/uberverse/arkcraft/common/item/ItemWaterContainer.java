package com.uberverse.arkcraft.common.item;

import java.util.List;

import com.uberverse.arkcraft.util.I18n;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemWaterContainer extends ARKCraftItem
{
	private int waterValue;
	private int maxWaterValue;
	
	public ItemWaterContainer(int waterValue, int maxWaterValue)
	{
		this.waterValue = waterValue;
		this.maxWaterValue = maxWaterValue;
		setMaxStackSize(1);
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		ItemStack s = new ItemStack(this);
		setWaterValueLeft(s, waterValue);
		subItems.add(s);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		int value = getWaterValueLeft(stack);
		tooltip.add(I18n.format("arkcraft.tooltip.fertilizer", value + waterValue * (stack.stackSize - 1)));
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) 
	{
		 MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);

	        if (movingobjectposition == null)
	        {
	            return itemStackIn;
	        }
	        else
	        {	  
	        	if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
	            {
	        		ItemStack ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemStackIn, movingobjectposition);
	                if (ret != null) return ret;
	        		
	                BlockPos blockpos = movingobjectposition.getBlockPos();
	                IBlockState blockState = worldIn.getBlockState(blockpos);
                    Material material = blockState.getBlock().getMaterial();

					if (material == Material.water && ((Integer)blockState.getValue(BlockLiquid.LEVEL)).intValue() == 0)
                    {
						System.out.println(waterValue);
						if(waterValue < maxWaterValue)
						waterValue = maxWaterValue;
                    }
					
	            }
	        }
			return itemStackIn;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
	{
		if(worldIn.getTotalWorldTime() %20 == 0)
		{
			if(waterValue > 0) 	setWaterValueLeft(stack, waterValue--);
		}
	}
	
	public int waterleft()
	{
		return waterValue;
	}
	
	public static int getWaterValueLeft(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound().getInteger("water") : 0;
	}

	public static void setWaterValueLeft(ItemStack stack, int f)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("water", f);
	}
}
