package com.arkcraft.common.item.itemblock;

import com.arkcraft.common.tileentity.energy.TileEntityCable;
import com.arkcraft.common.tileentity.energy.TileEntityCable.CableType;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockCable extends ItemBlock {

	public ItemBlockCable(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();
		//TileEntity te = worldIn.getTileEntity(pos);
		if (!block.isReplaceable(worldIn, pos)) {
			pos = pos.offset(facing);
		}

		if (!stack.isEmpty() && player.canPlayerEdit(pos, facing, stack)) {
			int meta = stack.getMetadata();
			/*if(te != null && te instanceof TileEntityCable){
				TileEntityCable c = (TileEntityCable) te;
				if(meta == 0){
					if(!c.hasBase){
						c.hasBase = true;
						SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, playerIn);
						worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
						--stack.stackSize;
					}
				}else{
					if(!c.hasVertical){
						c.hasVertical = true;
						SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, playerIn);
						worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
						--stack.stackSize;
					}
				}
			}else */
			if (worldIn.mayPlace(this.block, pos, false, facing, player)) {
				int i = this.getMetadata(stack.getMetadata());
				IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player);

				if (placeBlockAt(stack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1)) {
					SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
					worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					stack.shrink(1);
					TileEntity te = worldIn.getTileEntity(pos);
					if (te != null && te instanceof TileEntityCable) {
						TileEntityCable c = (TileEntityCable) te;
						c.type = CableType.VALUES[meta % CableType.VALUES.length];
					}
				}
			}

			return EnumActionResult.SUCCESS;
		} else {
			return EnumActionResult.FAIL;
		}
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return super.getTranslationKey(stack) + "." + (stack.getMetadata() == 0 ? "normal" : "vertical");
	}
}
