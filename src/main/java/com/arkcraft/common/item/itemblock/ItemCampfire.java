package com.arkcraft.common.item.itemblock;

import com.arkcraft.init.ARKCraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCampfire extends ItemBlock {
	public ItemCampfire(Block block) {
		super(block);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
		BlockPos blockpos1 = flag ? pos : pos.offset(facing);

		if (!playerIn.canPlayerEdit(blockpos1, facing, stack)) {
			return EnumActionResult.FAIL;
		} else {
			Block block = worldIn.getBlockState(blockpos1).getBlock();

			if (!worldIn.mayPlace(block, blockpos1, false, facing, playerIn)) {
				return EnumActionResult.FAIL;
			} else if (ARKCraftBlocks.campfire.canPlaceBlockAt(worldIn, blockpos1)) {
				stack.shrink(1);
				worldIn.setBlockState(blockpos1, ARKCraftBlocks.campfire.getDefaultState());
				return EnumActionResult.PASS;
			} else {
				return EnumActionResult.FAIL;
			}
		}
	}
}
