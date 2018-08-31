package com.arkcraft.common.block.resource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.arkcraft.common.block.IExperienceSource;
import com.arkcraft.common.item.tool.ItemToolBase;
import com.arkcraft.common.arkplayer.ARKPlayer;
import com.arkcraft.common.entity.IArkLevelable;
import com.arkcraft.util.AbstractItemStack;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockARKResource extends Block implements IExperienceSource
{
	public BlockARKResource(Material materialIn)
	{
		super(materialIn);
		setHardness(1.5f);
		setResistance(10f);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	@Override
	public void grantXP(IArkLevelable leveling)
	{
		leveling.addXP(0.4);
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if (!player.capabilities.isCreativeMode)
		{
			grantXP(ARKPlayer.get(player));

			ItemStack stack = player.getHeldItemMainhand();
			if (stack != null && stack.getItem() instanceof ItemToolBase)
			{
				ItemToolBase tool = (ItemToolBase) stack.getItem();

				Collection<AbstractItemStack> list = getDrops();

				list = tool.applyOutputModifiers(list, stack);

				for (AbstractItemStack ais : list)
				{
					for (ItemStack s : ais.toItemStacks())
					{
						worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), s));
					}
				}
			}
		}
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return Collections.emptyList();
	}

	public abstract Collection<AbstractItemStack> getDrops();
}
