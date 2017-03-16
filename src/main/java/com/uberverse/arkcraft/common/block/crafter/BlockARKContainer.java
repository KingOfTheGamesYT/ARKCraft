package com.uberverse.arkcraft.common.block.crafter;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.util.Identifiable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockARKContainer extends BlockContainer implements Identifiable
{
	protected BlockARKContainer(Material materialIn)
	{
		super(materialIn);
		setCreativeTab(ARKCraft.tabARK);
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof IInventory)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileEntity);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking())
		{
			playerIn.openGui(ARKCraft.instance(), getId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	@Override
	public abstract TileEntity createNewTileEntity(World worldIn, int meta);
}
