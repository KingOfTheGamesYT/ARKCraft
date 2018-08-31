package com.arkcraft.common.block.crafter;

import com.arkcraft.common.burner.IBurner;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public abstract class BlockBurner extends BlockARKContainer
{
    public static final PropertyBool BURNING = PropertyBool.create("burning");

    protected BlockBurner(Material materialIn)
    {
        super(materialIn);
        setDefaultState(getDefaultState().withProperty(BURNING, false));
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof IBurner) {
            IBurner burner = (IBurner) tileEntity;
            return state.withProperty(BURNING, burner.isBurning());
        }
        return state;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        int lightValue = 0;
        IBlockState blockState = getActualState(getDefaultState(), world, pos);
        boolean burning = (Boolean) blockState.getValue(BURNING);

        if (burning) {
            lightValue = 10;
        }
        else {
            lightValue = 0;
        }
        lightValue = MathHelper.clamp(lightValue, 0, 15);
        return lightValue;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BURNING, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (Boolean) state.getValue(BURNING) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BURNING);
    }
}
