package com.uberverse.arkcraft.common.item.tools;

import java.util.Set;
import java.util.Random;






import com.uberverse.arkcraft.client.event.ClientEventHandler;
import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ARKCraftTool extends ItemTool{
	
	boolean arkMode;
	public static  int count = 0;
	public int wood;
	public int thatch;

	@SuppressWarnings("rawtypes")
	public ARKCraftTool(float attackDamage, ToolMaterial material, Set effectiveBlocks) {
		super(attackDamage, material, effectiveBlocks);
	}
	
	@SuppressWarnings("rawtypes")
	public ARKCraftTool(String name, float attackDamage, ToolMaterial material, Set effectiveBlocks){
		super(attackDamage, material, effectiveBlocks);
		this.setUnlocalizedName(name);
	}
	
	 public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
	    { 
		if (arkMode != ClientEventHandler.openOverlay()) 
		{
			IBlockState blockState = worldIn.getBlockState(pos);
			if (blockState.getBlock() == Blocks.log || blockState.getBlock() == Blocks.log2) 
			{
				if(playerIn instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) playerIn;
					this.destroyBlocks(worldIn, pos, player);
					System.out.println(count);
					Random randomGenerator = new Random();
					wood = (int) (10 + randomGenerator.nextInt(100)/20.0*count);
					thatch = (int) (10 + randomGenerator.nextInt(100)/20.0*count);
					Float offset = worldIn.rand.nextFloat();
					EntityItem entityWood = new EntityItem(worldIn, pos.getX() + offset,
							pos.getY(), pos.getZ() + offset, (new ItemStack(ARKCraftItems.wood, wood)));
					EntityItem entityThatch = new EntityItem(worldIn, pos.getX() + offset,
							pos.getY() + blockIn.getBlockBoundsMaxY(), pos.getZ() + offset, (new ItemStack(ARKCraftItems.thatch, thatch)));
					worldIn.spawnEntityInWorld(entityThatch);
					worldIn.spawnEntityInWorld(entityWood);

				
					
				//	player.dropItem(ARKCraftItems.wood, wood);
				//	player.dropItem(ARKCraftItems.thatch, thatch);
				//	player.inventory.addItemStackToInventory(new ItemStack(ARKCraftItems.wood, wood));
				//	player.inventory.addItemStackToInventory(new ItemStack(ARKCraftItems.thatch, thatch));
					System.out.println(" Wood: " + wood + " Thatch: " + thatch);
					count = 0;
				}
			}
		}
		return true;
	}
	
	public void destroyBlocks(World world, BlockPos pos, EntityPlayer player) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		for (int i = x - 1; i <= x + 1; i++) {
			for (int k = z - 1; k <= z + 1; k++) {
				for (int j = y - 1; j <= y + 1; j++) {
						IBlockState blockState = world.getBlockState(new BlockPos(i, j, k));
						if (blockState.getBlock() == Blocks.log || blockState.getBlock() == Blocks.log2) 
						{
							world.destroyBlock(new BlockPos(i, j, k), false);
							++count;
							this.destroyBlocks(world, new BlockPos(i, j, k), player);
					}
				}
			}
		}

	}
}
