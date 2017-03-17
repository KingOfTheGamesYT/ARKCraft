package com.uberverse.arkcraft.common.block.crafter;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCompostBin;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author wildbill22
 */
public class BlockCompostBin extends BlockARKContainer
{
    public static final PropertyEnum PART = PropertyEnum.create("part", EnumPart.class);
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockCompostBin()
    {
        super(Material.WOOD);
        this.setCreativeTab(ARKCraft.tabARK);
        this.setDefaultState(super.getDefaultState().withProperty(PART, EnumPart.LEFT).withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos blockPos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote) {
            return true;
        }
        if (state.getValue(PART) != BlockCompostBin.EnumPart.LEFT) {
            blockPos = blockPos.offset(((EnumFacing) state.getValue(FACING)).rotateYCCW());
            state = worldIn.getBlockState(blockPos);
            if (state.getBlock() != this) {
                return true;
            }
        }
        if (!playerIn.isSneaking()) {
            playerIn.openGui(ARKCraft.instance(), getId(), worldIn, blockPos.getX(), blockPos.getY(), blockPos.getZ());
            return true;
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityCompostBin();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    // ---------------- Stuff for multiblock ------------------------

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode && state.getValue(PART) == BlockCompostBin.EnumPart.LEFT) {
            BlockPos blockpos1 = pos.offset(((EnumFacing) state.getValue(FACING)).getOpposite());
            if (worldIn.getBlockState(blockpos1).getBlock() == this) {
                worldIn.setBlockToAir(blockpos1);
            }
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        EnumFacing f = (EnumFacing) worldIn.getBlockState(pos).getValue(FACING);
        EnumPart p = (EnumPart) worldIn.getBlockState(pos).getValue(PART);
        switch (f) {
        case NORTH:
            switch (p) {
            case LEFT:
                setBlockBounds(0, 0, 0, 2, 1, 1);
                break;
            case RIGHT:
                setBlockBounds(-1, 0, 0, 1, 1, 1);
                break;
            }
            break;
        case EAST:
            switch (p) {
            case LEFT:
                setBlockBounds(0, 0, 0, 1, 1, 2);
                break;
            case RIGHT:
                setBlockBounds(0, 0, -1, 1, 1, 1);
                break;
            }
            break;
        case SOUTH:
            switch (p) {
            case LEFT:
                setBlockBounds(-1, 0, 0, 1, 1, 1);
                break;
            case RIGHT:
                setBlockBounds(0, 0, 0, 2, 1, 1);
                break;
            }
            break;
        case WEST:
            switch (p) {
            case LEFT:
                setBlockBounds(0, 0, -1, 1, 1, 1);
                break;
            case RIGHT:
                setBlockBounds(0, 0, 0, 1, 1, 2);
                break;
            }
            break;
        default:
            break;
        }
    }

    /**
     * Called when a neighboring block changes.
     */

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

        if (state.getValue(PART) == BlockCompostBin.EnumPart.LEFT) {
            if (worldIn.getBlockState(pos.offset(enumfacing.rotateY())).getBlock() != this) {
                worldIn.setBlockToAir(pos);
            }
        }
        else if (worldIn.getBlockState(pos.offset(enumfacing.rotateYCCW())).getBlock() != this) {
            worldIn.setBlockToAir(pos);
        }
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *
     * @param chance
     *            The chance that each Item is actually spawned (1.0 = always,
     *            0.0 = never)
     * @param fortune
     *            The player's fortune level
     */
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (state.getValue(PART) == BlockCompostBin.EnumPart.RIGHT) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.SOLID;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(PART, BlockCompostBin.EnumPart.LEFT).withProperty(FACING, enumfacing) : this.getDefaultState().withProperty(PART, BlockCompostBin.EnumPart.RIGHT).withProperty(FACING, enumfacing);
    }

    /**
     * Get the actual Block state of this Block at the given position. This
     * applies properties not visible in the metadata, such as fence
     * connections.
     */
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
        if (state.getValue(PART) == BlockCompostBin.EnumPart.LEFT) {
            i |= 8;
        }
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, PART);
    }

    public static enum EnumPart implements IStringSerializable
    {
        LEFT("left"), RIGHT("right");
        private final String name;

        private EnumPart(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
    }

    @Override
    public int getId()
    {
        return CommonProxy.GUI.COMPOST_BIN.id;
    }
}
