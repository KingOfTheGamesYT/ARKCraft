package com.uberverse.arkcraft.wip.oregen;

import java.util.Arrays;
import java.util.Collection;

import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.init.ARKCraftItems;
import com.uberverse.arkcraft.util.AbstractItemStack;
import com.uberverse.arkcraft.util.AbstractItemStack.ChancingAbstractItemStack;
import com.uberverse.arkcraft.wip.itemquality.ItemToolBase;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRockResource extends BlockARKResource
{
	public BlockRockResource()
	{
		super(Material.rock);
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		grantXP(ARKPlayer.get(player));

		ItemStack stack = player.getHeldItem();
		if (stack != null && stack.getItem() instanceof ItemToolBase)
		{
			ItemToolBase tool = (ItemToolBase) stack.getItem();

			Collection<AbstractItemStack> list = Arrays.asList(new AbstractItemStack(ARKCraftItems.stone, 10),
					new ChancingAbstractItemStack(ARKCraftItems.metal, 0.25), new AbstractItemStack(ARKCraftItems.flint,
							10));

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
}
