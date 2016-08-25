package com.uberverse.arkcraft.common.block.itemblock;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.uberverse.arkcraft.common.block.tile.TileEntityCropPlotNew.CropPlotType;
import com.uberverse.arkcraft.common.block.tile.TileEntityCropPlotNew.Part;
import com.uberverse.arkcraft.init.ARKCraftBlocks;

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
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
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
			else if (ARKCraftBlocks.crop_plot.canPlaceBlockAt(worldIn, blockpos1))
			{
				if(stack.getMetadata() > 0){
					for(Part p : Part.VALUES){
						if(p != Part.MIDDLE){
							if(!ARKCraftBlocks.crop_plot.canPlaceBlockAt(worldIn, p.offset(blockpos1, false)))return false;
						}
					}
				}
				--stack.stackSize;
				worldIn.setBlockState(blockpos1, ARKCraftBlocks.crop_plot.getDefaultState());
				ARKCraftBlocks.crop_plot.onBlockPlacedBy(worldIn, blockpos1, ARKCraftBlocks.crop_plot.getDefaultState(), playerIn, stack);
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack) + "." + CropPlotType.VALUES[stack.getMetadata() % CropPlotType.VALUES.length].name().toLowerCase();
	}
}
