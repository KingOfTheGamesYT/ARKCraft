package com.uberverse.arkcraft.wip.itemquality;

import com.uberverse.arkcraft.common.item.firearms.ItemRangedWeapon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemStonePickaxe extends ItemToolBase
{
	public ItemStonePickaxe()
	{
		super(100, 4, 2, ItemType.TOOL, ToolType.PICK);
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
	{
		// TODO Auto-generated method stub
		System.out.println("show " + entityLiving.worldObj.getTotalWorldTime());
		if (entityLiving instanceof EntityPlayer)
		{
			MovingObjectPosition mop = ItemRangedWeapon.rayTrace(entityLiving, 5, 0);
			if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
				IBlockState bs = entityLiving.worldObj.getBlockState(mop.getBlockPos());
				if (bs.getBlock() instanceof BlockLog)
				{
					World w = entityLiving.worldObj;
					Block search = bs.getBlock();
					BlockPos pos = mop.getBlockPos();

					int height = 0;
					while (w.getBlockState(pos).getBlock() == search)
					{
						height++;
						pos = pos.up();
					}
					System.out.println(height);

					pos = mop.getBlockPos();
					int width = 1;
					if (w.getBlockState(pos.north()).getBlock() == search || w.getBlockState(pos.south()).getBlock() == search
							|| w.getBlockState(pos.east()).getBlock() == search || w.getBlockState(pos.west()).getBlock() == search)
						width++;

					System.out.println(width);

					int total = width * width * height;
					System.out.println(total);

					setSpeedDivider(stack, (float) 1 / (float) total);
				}
				return true;
			}
		}
		return super.onEntitySwing(entityLiving, stack);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
	{
		// TODO Auto-generated method stub
		System.out.println("destroy");
		return super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
	}
}
