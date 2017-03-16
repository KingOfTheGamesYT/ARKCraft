package com.uberverse.arkcraft.common.item.itemblock;

import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot.CropPlotType;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot.Part;
import com.uberverse.arkcraft.init.ARKCraftBlocks;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCropPlot extends ItemBlockARK
{
	public ItemCropPlot(Block block)
	{
		super(block);
		this.setMaxStackSize(16);
		setHasSubtypes(true);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	
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
			else if (ARKCraftBlocks.cropPlot.canPlaceBlockAt(worldIn, blockpos1))
			{
				if (stack.getMetadata() > 0)
				{
					for (Part p : Part.VALUES)
					{
						if (p != Part.MIDDLE)
						{
							if (!ARKCraftBlocks.cropPlot.canPlaceBlockAt(worldIn, p.offset(blockpos1, false)))
								return EnumActionResult.FAIL;
						}
					}
				}
				--stack.stackSize;
				worldIn.setBlockState(blockpos1, ARKCraftBlocks.cropPlot.getDefaultState());
				ARKCraftBlocks.cropPlot.onBlockPlacedBy(worldIn, blockpos1, ARKCraftBlocks.cropPlot.getDefaultState(),
						playerIn, stack);
				return EnumActionResult.PASS;
			}
			else
			{
				return EnumActionResult.FAIL;
			}
		}
	}
	

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + CropPlotType.VALUES[stack.getMetadata()
				% CropPlotType.VALUES.length].name().toLowerCase();
	}
}
