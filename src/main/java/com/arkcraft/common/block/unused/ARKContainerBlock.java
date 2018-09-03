package com.arkcraft.common.block.unused;

import com.arkcraft.ARKCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ARKContainerBlock extends Block {
	private int renderType = 3; // default value
	private boolean isOpaque = false;
	private int ID;
	private boolean render = false;

	public ARKContainerBlock(Material mat, int ID) {
		super(mat);

		this.ID = ID;
	}

	public int getRenderType() {
		return renderType;
	}

	public void setRenderType(int renderType) {
		this.renderType = renderType;
	}

	public void setOpaque(boolean opaque) {
		opaque = isOpaque;
	}

	public boolean isOpaqueCube() {
		return isOpaque;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking()) {
			if (!worldIn.isRemote) {
				playerIn.openGui(ARKCraft.instance(), ID, worldIn, side.getXOffset(), side.getYOffset(), side.getZOffset());
				return true;
			}
		}
		return false;
	}


	public void setRenderAsNormalBlock(boolean b) {
		render = b;
	}

	public boolean renderAsNormalBlock() {
		return render;
	}
}
