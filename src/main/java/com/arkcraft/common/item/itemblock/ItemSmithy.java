package com.arkcraft.common.item.itemblock;

import com.arkcraft.common.block.crafter.BlockSmithy;
import com.arkcraft.common.block.crafter.EnumPart;
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

public class ItemSmithy extends ItemBlockARK {
	public ItemSmithy(Block block) {
		super(block);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return EnumActionResult.SUCCESS;
		} else if (side != EnumFacing.UP) {
			return EnumActionResult.SUCCESS;
		} else {
			ItemStack itemStack = player.getHeldItem(hand);
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);
			if (!flag) {
				pos = pos.up();
			}
			EnumFacing enumfacing1 = EnumFacing.getDirectionFromEntityLiving(pos, player);
			// BlockPos blockpos1 = pos.offset(enumfacing1); // like a bed,
			// placed vertically
			BlockPos blockpos1 = pos.offset(enumfacing1.rotateYCCW());
			boolean flag1 = block.isReplaceable(worldIn, blockpos1);
			boolean flag2 = worldIn.isAirBlock(pos) || flag;
			boolean flag3 = worldIn.isAirBlock(blockpos1) || flag1;

			if (player.canPlayerEdit(pos, side, itemStack) && player.canPlayerEdit(blockpos1, side, itemStack)) {
				if (flag2 && flag3 && worldIn.isSideSolid(pos.down(), EnumFacing.UP) && worldIn.isSideSolid(blockpos1.down(), EnumFacing.UP)) {
					IBlockState iblockstate1 = ARKCraftBlocks.smithy.getDefaultState().withProperty(BlockSmithy.FACING,
							enumfacing1).withProperty(BlockSmithy.PART, EnumPart.RIGHT);
					if (worldIn.setBlockState(pos, iblockstate1, 3)) {
						IBlockState iblockstate2 = iblockstate1.withProperty(BlockSmithy.PART,
								EnumPart.LEFT);
						worldIn.setBlockState(blockpos1, iblockstate2, 3);
					}
					itemStack.setCount(itemStack.getCount() - 1);
					return EnumActionResult.SUCCESS;
				} else {
					return EnumActionResult.FAIL;
				}
			} else {
				return EnumActionResult.FAIL;
			}
		}
	}
}
