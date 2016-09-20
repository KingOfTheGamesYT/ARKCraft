package com.uberverse.arkcraft.common.block.resource;

import java.util.Collection;

import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.block.IExperienceSource;
import com.uberverse.arkcraft.common.entity.IArkLevelable;
import com.uberverse.arkcraft.util.AbstractItemStack;
import com.uberverse.arkcraft.wip.itemquality.tools.ItemToolBase;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
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
		grantXP(ARKPlayer.get(player));

		ItemStack stack = player.getHeldItem();
		if (stack != null && stack.getItem() instanceof ItemToolBase)
		{
			ItemToolBase tool = (ItemToolBase) stack.getItem();

			Collection<AbstractItemStack> list = getDrops();

			list = tool.applyOutputModifiers(list, stack);

			for (AbstractItemStack ais : list)
			{
				for (ItemStack s : ais.toItemStacks())
				{
					worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), s));
				}
			}
		}
	}

	public abstract Collection<AbstractItemStack> getDrops();
}
