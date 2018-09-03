package com.arkcraft.common.block.resource;

import com.arkcraft.common.arkplayer.ARKPlayer;
import com.arkcraft.common.block.IExperienceSource;
import com.arkcraft.common.entity.IArkLevelable;
import com.arkcraft.init.ARKCraftItems;
import com.arkcraft.util.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSmallRockResource extends Block implements IExperienceSource {
	private static final AxisAlignedBB BB = new AxisAlignedBB(0, 0, 0, 1, 0.2f, 1);

	public BlockSmallRockResource() {
		super(Material.ROCK);
		setHardness(1.5f);
		setResistance(10f);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (playerIn.getHeldItemMainhand() == null) {
			grantXP(ARKPlayer.get(playerIn));
			InventoryUtil.addOrDrop(new ItemStack(ARKCraftItems.stone), playerIn.inventory, pos, worldIn);
			worldIn.setBlockToAir(pos);
			return true;
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, EnumHand.MAIN_HAND, side, hitX, hitY, hitZ);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public void grantXP(IArkLevelable leveling) {
		leveling.addXP(0.4);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}
}
