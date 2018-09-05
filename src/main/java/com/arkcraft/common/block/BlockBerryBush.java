package com.arkcraft.common.block;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.arkplayer.ARKPlayer;
import com.arkcraft.common.config.ModuleItemBalance;
import com.arkcraft.common.entity.IArkLevelable;
import com.arkcraft.common.item.IDecayable;
import com.arkcraft.init.ARKCraftItems;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockBerryBush extends BlockBush implements IExperienceSource {
	public static final PropertyInteger HARVEST_COUNT = PropertyInteger.create("harvest", 0, 3);

	public BlockBerryBush() {
		super();
		this.setDefaultState(this.blockState.getBaseState().withProperty(HARVEST_COUNT, 0));
		this.setSoundType(SoundType.GROUND);
		this.setTickRandomly(true);
		this.setHardness(0.4F);
		this.setCreativeTab(ARKCraft.tabARK);
		this.setBlockUnbreakable();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess soruce, BlockPos pos) {
		return new AxisAlignedBB(0f, 0f, 0f, 1f, 1f, 1f);
	}

	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (rand.nextBoolean()) {
			int harvestCount = getMetaFromState(state);

			if (harvestCount < 3) {
				worldIn.setBlockState(pos, state.withProperty(HARVEST_COUNT, harvestCount + 1));
			}
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.setBlockState(pos, state.withProperty(HARVEST_COUNT, 3));
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return Lists.newArrayList();
	}

	public Item getHarvestItem(Random rand, EntityPlayer player) {
		ItemStack heldStack = player.getHeldItemMainhand();

		if (heldStack != null && heldStack.getItem() == ARKCraftItems.metal_sickle) {
			if (rand.nextInt(30) <= 15) {
				return ARKCraftItems.fiber;
			}
		} else {
			if (rand.nextInt(10) <= 3) {
				return ARKCraftItems.fiber;
			} else if (rand.nextInt(10) <= 4) {
				return rand.nextInt(10) <= 5 ? ARKCraftItems.amarBerry : ARKCraftItems.narcoBerry;
			} else if (rand.nextInt(15) <= 4) {
				return ARKCraftItems.stimBerry;
			} else if (rand.nextInt(10) >= 4 && rand.nextInt(10) <= 8) {
				return rand.nextInt(10) <= 5 ? ARKCraftItems.mejoBerry : ARKCraftItems.tintoBerry;
			} else {
				return ARKCraftItems.azulBerry;
			}
		}
		return null;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, 0);
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		onLeftClicked(worldIn, pos, worldIn.getBlockState(pos), playerIn);
	}

	public void onLeftClicked(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
		if (!worldIn.isRemote) {
			int harvestCount = getMetaFromState(state);
			if (harvestCount > 0) {
				for (int i = 0; i < ModuleItemBalance.PLANTS.BERRIES_MIN_PER_PICKING || i <= worldIn.rand.nextInt(ModuleItemBalance.PLANTS.BERRIES_MAX_PER_PICKING); i++) {
					Item itemPicked = getHarvestItem(worldIn.rand, playerIn);
					Item seed = getSeedDrop(worldIn.rand);
					if (seed != null)
						this.entityDropItem(worldIn, pos, playerIn, new ItemStack(seed, 1, 0));
					ItemStack out = new ItemStack(itemPicked, 1, 0);
					if (itemPicked instanceof IDecayable)
						IDecayable.setDecayStart(out, ARKCraft.proxy().getWorldTime());
					this.entityDropItem(worldIn, pos, playerIn, out);
				}
				worldIn.setBlockState(pos, state.withProperty(HARVEST_COUNT, harvestCount - 1));
				if (harvestCount == 1) {
					worldIn.setBlockToAir(pos);
				}
				grantXP(ARKPlayer.get(playerIn));
			}
		}
	}

	private Item getSeedDrop(Random rand) {
		if (rand.nextInt(30) == 0) {
			if (rand.nextInt(5) == 4) {
				int r = rand.nextInt(6);
				switch (r) {
					// TODO when new seeds are done!
				}
			}
			int r = rand.nextInt(6);
			switch (r) {
				case 0:
					return ARKCraftItems.amarBerrySeed;
				case 1:
					return ARKCraftItems.azulBerrySeed;
				case 2:
					return ARKCraftItems.mejoBerrySeed;
				case 3:
					return ARKCraftItems.narcoBerrySeed;
				case 4:
					return ARKCraftItems.stimBerrySeed;
				case 5:
					return ARKCraftItems.tintoBerrySeed;
			}
		}
		return null;
	}

	/**
	 * Drops an item at the position of the bush.
	 */
	private void entityDropItem(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack itemStackIn) {
		if (!itemStackIn.isEmpty() && itemStackIn.getItem() != null) {
			Float offset = worldIn.rand.nextFloat();
			//TODO Dropping may be little incorrect now not sure though
			EntityItem entityitem = new EntityItem(worldIn, pos.getX() + offset, pos.getY() + 0.5, pos.getZ() + offset, itemStackIn);
			entityitem.setDefaultPickupDelay();
			if (playerIn.captureDrops) {
				playerIn.capturedDrops.add(entityitem);
			} else {
				worldIn.spawnEntity(entityitem);
			}
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 3;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(HARVEST_COUNT, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(HARVEST_COUNT).intValue();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, HARVEST_COUNT);
	}

	/*
	 * @Override public void onBlockClicked(World worldIn, BlockPos pos,
	 * EntityPlayer playerIn) {} public Vec3 modifyAcceleration(World worldIn,
	 * BlockPos pos, Entity entityIn, Vec3 motion) { return motion; }
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public int quantityDropped(Random random) {
		return random.nextInt(10) <= 5 ? 1 : 2;
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	//TODO
	/*
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
    } */

	@Override
	public void grantXP(IArkLevelable leveling) {
		leveling.addXP(0.4);
	}

	/**
	 * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public Block.EnumOffsetType getOffsetType() {
		return Block.EnumOffsetType.XYZ;
	}
}
