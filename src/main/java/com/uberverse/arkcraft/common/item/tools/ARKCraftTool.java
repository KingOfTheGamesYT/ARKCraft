package com.uberverse.arkcraft.common.item.tools;

import java.util.Set;

import com.uberverse.arkcraft.client.event.ClientEventHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ARKCraftTool extends ItemTool{
	
	boolean arkMode;

	public ARKCraftTool(float attackDamage, ToolMaterial material, Set effectiveBlocks) {
		super(attackDamage, material, effectiveBlocks);
	}
	
	public ARKCraftTool(String name, float attackDamage, ToolMaterial material, Set effectiveBlocks){
		super(attackDamage, material, effectiveBlocks);
		this.setUnlocalizedName(name);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
    {
		super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
		IBlockState blockState = blockIn.getDefaultState();

		System.out.println(blockState.getBlock());
		if (arkMode != ClientEventHandler.openOverlay())
		{
			if(blockState.getBlock() == Blocks.log)
			{
				this.destroyBlocks(blockState.getBlock(), worldIn, pos);
			}

		}
		return true;
    }
	
	public void destroyBlocks(Block block, World world, BlockPos pos)
	{
		int checkSize = 1;
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		
		for (int x = -checkSize; x <= checkSize; x++) {
			for (int z = -checkSize; z <= checkSize; z++) {
				for (int y = 0; y <= checkSize; y++) {
					
					IBlockState blockState = world.getBlockState(new BlockPos(i + x, j + y, k + z));
					if(blockState.getBlock() == Blocks.log)
					{
						world.destroyBlock(new BlockPos(i + x, j + y, k + z), true);
						this.destroyBlocks(blockState.getBlock(), world, new BlockPos(i + x, j + y, k + z));
						System.out.println("worked");
					}
				}	
			}
		}

	}
}
