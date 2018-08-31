package com.arkcraft.common.item.itemblock;

import com.arkcraft.common.block.crafter.BlockCompostBin;
import com.arkcraft.init.ARKCraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemCompostBin extends ItemBlockARK {
	public ItemCompostBin(Block block) {
		super(block);
		this.setMaxStackSize(1);
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 *
	 * @param pos  The block being right-clicked
	 * @param side The side being right-clicked
	 */
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
							 float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		} else if (side != EnumFacing.UP) {
			return false;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);
			if (!flag) {
				pos = pos.up();
			}
			int i = MathHelper.floor(playerIn.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
			EnumFacing enumfacing1 = EnumFacing.getDirectionFromEntityLiving(pos, playerIn);
			// BlockPos blockpos1 = pos.offset(enumfacing1); // like a bed,
			// placed vertically
			BlockPos blockpos1 = pos.offset(enumfacing1.rotateYCCW());
			boolean flag1 = block.isReplaceable(worldIn, blockpos1);
			boolean flag2 = worldIn.isAirBlock(pos) || flag;
			boolean flag3 = worldIn.isAirBlock(blockpos1) || flag1;

			if (playerIn.canPlayerEdit(pos, side, stack) && playerIn.canPlayerEdit(blockpos1, side, stack)) {
				if (flag2 && flag3 && worldIn.isSideSolid(pos.down(), EnumFacing.UP) && worldIn.isSideSolid(blockpos1.down(), EnumFacing.UP)) {
					IBlockState iblockstate1 = ARKCraftBlocks.compostBin.getDefaultState().withProperty(
							BlockCompostBin.FACING, enumfacing1).withProperty(BlockCompostBin.PART,
							BlockCompostBin.EnumPart.RIGHT);
					if (worldIn.setBlockState(pos, iblockstate1, 3)) {
						IBlockState iblockstate2 = iblockstate1.withProperty(BlockCompostBin.PART,
								BlockCompostBin.EnumPart.LEFT);
						worldIn.setBlockState(blockpos1, iblockstate2, 3);
					}
					stack.setCount(stack.getCount() - 1);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos,
									  EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return EnumActionResult.SUCCESS;
		} else if (side != EnumFacing.UP) {
			return EnumActionResult.FAIL;
		} else {
			ItemStack stack = playerIn.getHeldItemMainhand();
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);
			if (!flag) {
				pos = pos.up();
			}
			EnumFacing enumfacing1 = EnumFacing.getDirectionFromEntityLiving(pos, playerIn);
			// BlockPos blockpos1 = pos.offset(enumfacing1); // like a bed,
			// placed vertically
			BlockPos blockpos1 = pos.offset(enumfacing1.rotateYCCW());
			boolean flag1 = block.isReplaceable(worldIn, blockpos1);
			boolean flag2 = worldIn.isAirBlock(pos) || flag;
			boolean flag3 = worldIn.isAirBlock(blockpos1) || flag1;

			if (playerIn.canPlayerEdit(pos, side, stack) && playerIn.canPlayerEdit(blockpos1, side, stack)) {
				if (flag2 && flag3 && worldIn.isSideSolid(pos.down(), EnumFacing.UP) && worldIn.isSideSolid(blockpos1.down(), EnumFacing.UP)) {
					IBlockState iblockstate1 = ARKCraftBlocks.compostBin.getDefaultState().withProperty(
							BlockCompostBin.FACING, enumfacing1).withProperty(BlockCompostBin.PART,
							BlockCompostBin.EnumPart.RIGHT);
					if (worldIn.setBlockState(pos, iblockstate1, 3)) {
						IBlockState iblockstate2 = iblockstate1.withProperty(BlockCompostBin.PART,
								BlockCompostBin.EnumPart.LEFT);
						worldIn.setBlockState(blockpos1, iblockstate2, 3);
					}
					stack.setCount(stack.getCount() - 1);
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
