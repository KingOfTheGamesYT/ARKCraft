package com.arkcraft.common.item.itemblock;

import com.arkcraft.common.block.crafter.BlockRefiningForge;
import com.arkcraft.init.ARKCraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRefiningForge extends ItemBlockARK {
	public ItemRefiningForge(Block block) {
		super(block);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos,
									  EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		if (worldIn.isRemote) {
			return EnumActionResult.PASS;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);
			if (!flag) {
				pos = pos.up();
			}
			// BlockPos blockpos1 = pos.offset(enumfacing1); // like a bed,
			// placed vertically
			BlockPos blockpos1 = pos.up();
			boolean flag1 = block.isReplaceable(worldIn, blockpos1);
			boolean flag2 = worldIn.isAirBlock(pos) || flag;
			boolean flag3 = worldIn.isAirBlock(blockpos1) || flag1;

			if (playerIn.canPlayerEdit(pos, facing, stack) && playerIn.canPlayerEdit(blockpos1, facing, stack)) {
				if (flag2 && flag3 && worldIn.isSideSolid(pos.down(), EnumFacing.UP)) {
					IBlockState iblockstate1 = ARKCraftBlocks.refiningForge.getStateForPlacement(worldIn, blockpos1, facing,
							hitX, hitY, hitZ, 0, playerIn).withProperty(BlockRefiningForge.PART,
							BlockRefiningForge.EnumPart.BOTTOM);
					if (worldIn.setBlockState(pos, iblockstate1, 3)) {
						IBlockState iblockstate2 = iblockstate1.withProperty(BlockRefiningForge.PART,
								BlockRefiningForge.EnumPart.TOP);
						worldIn.setBlockState(blockpos1, iblockstate2, 3);
					}
					stack.shrink(1);
					return EnumActionResult.PASS;
				} else {
					return EnumActionResult.FAIL;
				}
			} else {
				return EnumActionResult.FAIL;
			}
		}
	}
}
