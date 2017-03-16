package com.uberverse.arkcraft.common.item.itemblock;

import com.uberverse.arkcraft.init.ARKCraftBlocks;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCampfire extends ItemBlock
{
	public ItemCampfire(Block block)
	{
		super(block);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
		BlockPos blockpos1 = flag ? pos : pos.offset(facing);

		if (!playerIn.canPlayerEdit(blockpos1, facing, stack))
		{
			return EnumActionResult.FAIL;
		}
		else
		{
			Block block = worldIn.getBlockState(blockpos1).getBlock();

			if (!worldIn.canBlockBePlaced(block, blockpos1, false, facing, (Entity) null, stack))
			{
				return EnumActionResult.FAIL;
			}
			else if (ARKCraftBlocks.campfire.canPlaceBlockAt(worldIn, blockpos1))
			{
				--stack.stackSize;
				worldIn.setBlockState(blockpos1, ARKCraftBlocks.campfire.getDefaultState());
				return EnumActionResult.PASS;
			}
			else
			{
				return EnumActionResult.FAIL;
			}
		}
	}
}
