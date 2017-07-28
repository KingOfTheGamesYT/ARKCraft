package com.uberverse.arkcraft.common.item.itemblock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.uberverse.arkcraft.common.tileentity.energy.TileEntityCable;
import com.uberverse.arkcraft.common.tileentity.energy.TileEntityCable.CableType;

public class ItemBlockCable extends ItemBlock {

	public ItemBlockCable(Block block) {
		super(block);
		setHasSubtypes(true);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack(itemIn, 1, 0));
		list.add(new ItemStack(itemIn, 1, 1));
	}
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();
		//TileEntity te = worldIn.getTileEntity(pos);
		if (!block.isReplaceable(worldIn, pos))
		{
			pos = pos.offset(facing);
		}

		if (stack.stackSize != 0 && playerIn.canPlayerEdit(pos, facing, stack))
		{
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
			}else */if(worldIn.canBlockBePlaced(this.block, pos, false, facing, (Entity)null, stack)){
				int i = this.getMetadata(stack.getMetadata());
				IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, playerIn, stack);

				if (placeBlockAt(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
				{
					SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, playerIn);
					worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					--stack.stackSize;
					TileEntity te = worldIn.getTileEntity(pos);
					if(te != null && te instanceof TileEntityCable){
						TileEntityCable c = (TileEntityCable) te;
						c.type = CableType.VALUES[meta % CableType.VALUES.length];
					}
				}
			}

			return EnumActionResult.SUCCESS;
		}
		else
		{
			return EnumActionResult.FAIL;
		}
	}
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack) + "." + (stack.getMetadata() == 0 ? "normal" : "vertical");
	}
}
