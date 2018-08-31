package com.arkcraft.common.block.resource;

import java.util.Arrays;
import java.util.Collection;

import com.arkcraft.common.tileentity.TileEntityCrystal;
import com.arkcraft.init.ARKCraftItems;
import com.arkcraft.util.AbstractItemStack;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrystalResource extends BlockARKResource
{

	public BlockCrystalResource()
	{
		super(Material.ROCK);
		setLightLevel(5);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityCrystal();
	}

	@Override
	public Collection<AbstractItemStack> getDrops()
	{
		return Arrays.asList(new AbstractItemStack(ARKCraftItems.crystal, 10), new AbstractItemStack(
				ARKCraftItems.stone, 10));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
}
