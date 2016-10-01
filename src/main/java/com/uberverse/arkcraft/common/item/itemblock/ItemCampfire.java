package com.uberverse.arkcraft.common.item.itemblock;

import com.uberverse.arkcraft.init.ARKCraftBlocks;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemCampfire extends ItemBlock
{
	public ItemCampfire(Block block)
	{
		super(block);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ)
	{
		boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
		BlockPos blockpos1 = flag ? pos : pos.offset(side);

		if (!playerIn.canPlayerEdit(blockpos1, side, stack))
		{
			return false;
		}
		else
		{
			Block block = worldIn.getBlockState(blockpos1).getBlock();

			if (!worldIn.canBlockBePlaced(block, blockpos1, false, side, (Entity) null, stack))
			{
				return false;
			}
			else if (ARKCraftBlocks.campfire.canPlaceBlockAt(worldIn, blockpos1))
			{
				--stack.stackSize;
				worldIn.setBlockState(blockpos1, ARKCraftBlocks.campfire.getDefaultState());
				return true;
			}
			else
			{
				return false;
			}
		}
	}
}
