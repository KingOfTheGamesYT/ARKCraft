package com.uberverse.arkcraft.common.block;

import java.util.Random;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.config.ModuleItemBalance;
import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ARKCraftBerryBush extends BlockBush
{
	public static final PropertyInteger HARVEST_COUNT = PropertyInteger.create("harvest", 0, 3);

	public ARKCraftBerryBush(float hardness)
	{
		super();
		this.setDefaultState(this.blockState.getBaseState().withProperty(HARVEST_COUNT, 0));
		this.setStepSound(Block.soundTypeGrass);
		this.setTickRandomly(true);
		this.setHardness(hardness);
		this.setCreativeTab(ARKCraft.tabARK);
		this.setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f);
	}

	@Override
	public boolean isReplaceable(World worldIn, BlockPos pos)
	{
		return true;
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (rand.nextBoolean())
		{
			int harvestCount = getMetaFromState(state);

			if (harvestCount < 3)
			{
				worldIn.setBlockState(pos, state.withProperty(HARVEST_COUNT, harvestCount + 1));
			}
		}
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		worldIn.setBlockState(pos, state.withProperty(HARVEST_COUNT, 3));
	}

	public Item getHarvestItem(Random rand, EntityPlayer player)
	{
		ItemStack heldStack = player.getCurrentEquippedItem();

		if (heldStack != null && heldStack.getItem() == ARKCraftItems.metal_sickle)
		{
			if (rand.nextInt(30) <= 15) { return ARKCraftItems.fiber; }
		}
		else
		{
			if (rand.nextInt(10) <= 3)
			{
				return ARKCraftItems.fiber;
			}
			else if (rand.nextInt(10) <= 4)
			{
				return rand.nextInt(10) <= 5 ? ARKCraftItems.amarBerry : ARKCraftItems.narcoBerry;
			}
			else if (rand.nextInt(15) <= 4)
			{
				return ARKCraftItems.stimBerry;
			}
			else if (rand.nextInt(10) >= 4 && rand.nextInt(10) <= 8)
			{
				return rand.nextInt(10) <= 5 ? ARKCraftItems.mejoBerry : ARKCraftItems.tintoBerry;
			}
			else
			{
				return ARKCraftItems.azulBerry;
			}
		}
		return null;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(this, 1, 0);
	}

	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
	{
		onLeftClicked(worldIn, pos, worldIn.getBlockState(pos), playerIn);
	}

	public void onLeftClicked(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn)
	{
		if (!worldIn.isRemote)
		{
			int harvestCount = getMetaFromState(state);
			if (harvestCount > 0)
			{
				this.setBlockUnbreakable();
				for (int i = 0; i < ModuleItemBalance.PLANTS.BERRIES_MIN_PER_PICKING || i <= worldIn.rand.nextInt(
						ModuleItemBalance.PLANTS.BERRIES_MAX_PER_PICKING); i++)
				{
					Item itemPicked = getHarvestItem(worldIn.rand, playerIn);
					this.entityDropItem(worldIn, pos, playerIn, new ItemStack(itemPicked, 1, 0));
				}
				worldIn.setBlockState(pos, state.withProperty(HARVEST_COUNT, harvestCount - 1));
				if (harvestCount == 1)
				{
					worldIn.setBlockToAir(pos);
				}
			}
		}
	}

	/**
	 * Drops an item at the position of the bush.
	 */
	private void entityDropItem(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack itemStackIn)
	{
		if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null)
		{
			Float offset = worldIn.rand.nextFloat();
			EntityItem entityitem = new EntityItem(worldIn, pos.getX() + offset, pos.getY() + this.maxY, pos.getZ()
					+ offset, itemStackIn);
			entityitem.setDefaultPickupDelay();
			if (playerIn.captureDrops)
			{
				playerIn.capturedDrops.add(entityitem);
			}
			else
			{
				worldIn.spawnEntityInWorld(entityitem);
			}
		}
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 3;
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(HARVEST_COUNT, meta);
	}

	public int getMetaFromState(IBlockState state)
	{
		return ((Integer) state.getValue(HARVEST_COUNT)).intValue();
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { HARVEST_COUNT });
	}

	/*
	 * @Override public void onBlockClicked(World worldIn, BlockPos pos,
	 * EntityPlayer playerIn) {}
	 * public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity
	 * entityIn, Vec3 motion) { return motion; }
	 */
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return random.nextInt(10) <= 5 ? 1 : 2;
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
	{
		return this.blockMaterial != Material.air;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return null;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		return ColorizerGrass.getGrassColor(0.5D, 1.0D);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state)
	{
		return this.getBlockColor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
	}

}
