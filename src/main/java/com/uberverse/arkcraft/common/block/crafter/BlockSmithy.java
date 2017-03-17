package com.uberverse.arkcraft.common.block.crafter;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.common.tileentity.crafter.engram.TileEntitySmithy;

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
public class BlockSmithy extends BlockARKContainer
{
    public static final PropertyEnum PART = PropertyEnum.create("part", EnumPart.class);
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockSmithy()
    {
        super(Material.WOOD);
        this.setHardness(0.5F);
        this.setCreativeTab(ARKCraft.tabARK);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySmithy();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos blockPos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!playerIn.isSneaking()) {
            if (state.getValue(PART) == EnumPart.RIGHT)
                playerIn.openGui(ARKCraft.instance(), getId(), worldIn, blockPos.getX(), blockPos.getY(), blockPos.getZ());
            else {
                EnumFacing f = (EnumFacing) state.getValue(FACING);
                BlockPos pos = blockPos.offset(f.rotateY());
                IBlockState oState = worldIn.getBlockState(pos);
                oState.getBlock().onBlockActivated(worldIn, pos, oState, playerIn, side, hitX, hitY, hitZ);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    // ---------------- Stuff for multiblock ------------------------
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

        if (state.getValue(PART) == BlockSmithy.EnumPart.LEFT) {
            if (worldIn.getBlockState(pos.offset(enumfacing.rotateY())).getBlock() != this) {
                worldIn.setBlockToAir(pos);
            }
        }
        else if (worldIn.getBlockState(pos.offset(enumfacing.rotateYCCW())).getBlock() != this) {
            worldIn.setBlockToAir(pos);
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
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(PART, BlockSmithy.EnumPart.LEFT).withProperty(FACING, enumfacing) : this.getDefaultState().withProperty(PART, BlockSmithy.EnumPart.RIGHT).withProperty(FACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
        if (state.getValue(PART) == BlockSmithy.EnumPart.LEFT) {
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

        @Override
        public String toString()
        {
            return this.name;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    @Override
    public int getId()
    {
        return CommonProxy.GUI.SMITHY.id;
    }
}
