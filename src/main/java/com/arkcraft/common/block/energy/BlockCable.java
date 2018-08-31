package com.arkcraft.common.block.energy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.tileentity.energy.TileEntityCable;
import com.arkcraft.common.tileentity.energy.TileEntityCable.CableType;

import com.google.common.collect.Lists;

public class BlockCable extends BlockContainer {
	public static final UnlistedPropertyCableData DATA = new UnlistedPropertyCableData();
	public static final AxisAlignedBB VERTICAL = new AxisAlignedBB(6/16d, 0, 6/16d, 10/16d, 1, 10/16d);
	public static final AxisAlignedBB NO_CONNECTION = new AxisAlignedBB(4/16d, 0, 6/16d, 12/16d, 2/16d, 10/16d);
	public static final AxisAlignedBB CENTER = new AxisAlignedBB(6/16d, 0, 6/16d, 10/16d, 2/16d, 10/16d);
	public static final AxisAlignedBB[] CONNECT = new AxisAlignedBB[]{new AxisAlignedBB(6/16d, 0, 0, 10/16d, 2/16d, 6/16d),
			new AxisAlignedBB(6/16d, 0, 10/16d, 10/16d, 2/16d, 1),
			new AxisAlignedBB(0, 0, 6/16d, 6/16d, 2/16d, 10/16d),
			new AxisAlignedBB(10/16d, 0, 6/16d, 1, 2/16d, 10/16d)
	};
	public BlockCable() {
		super(Material.ROCK);
		setCreativeTab(ARKCraft.tabARK);
		this.setHardness(0.5F);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCable();
	}
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	public static class UnlistedPropertyCableData implements IUnlistedProperty<TileEntityCable>{

		@Override
		public String getName() {
			return "cabledata";
		}

		@Override
		public boolean isValid(TileEntityCable value) {
			return value != null;
		}

		@Override
		public Class<TileEntityCable> getType() {
			return TileEntityCable.class;
		}

		@Override
		public String valueToString(TileEntityCable value) {
			return value.toString();
		}
	}
	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[]{DATA});
	}
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		return state instanceof IExtendedBlockState && te != null && te instanceof TileEntityCable ? ((IExtendedBlockState)state).withProperty(DATA, ((TileEntityCable) te).checkConnections()) : state;
	}
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityCable){
			TileEntityCable c = (TileEntityCable) te;
			if(c.type == CableType.VERTICAL && c.connects(EnumFacing.UP)){
				return VERTICAL;
			}
		}
		return NULL_AABB;
	}
	@SuppressWarnings("deprecation")
	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start,
			Vec3d end) {
		List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();

		for (AxisAlignedBB axisalignedbb : getCollisionBoxList(this.getActualState(blockState, worldIn, pos), worldIn, pos))
		{
			list.add(this.rayTrace(pos, start, end, axisalignedbb));
		}

		RayTraceResult raytraceresult1 = null;
		double d1 = 0.0D;
		int element = -1;
		int value = -1;

		for (RayTraceResult raytraceresult : list)
		{
			element++;
			if (raytraceresult != null)
			{
				double d0 = raytraceresult.hitVec.squareDistanceTo(end);

				if (d0 > d1)
				{
					raytraceresult1 = raytraceresult;
					d1 = d0;
					value = element;
				}
			}
		}
		if(raytraceresult1 != null)raytraceresult1.subHit = value;
		return raytraceresult1;
	}
	private List<AxisAlignedBB> getCollisionBoxList(IBlockState blockState, World worldIn, BlockPos pos) {
		List<AxisAlignedBB> list = new ArrayList<>();
		addSelectionBoxToList(blockState, worldIn, pos, list);
		List<AxisAlignedBB> ret = new ArrayList<>();
		list.stream().map(i -> i.offset(-pos.getX(), -pos.getY(), -pos.getZ())).forEach(ret::add);
		return ret;
	}
	@SuppressWarnings("deprecation")
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		try{
			Minecraft mc = Minecraft.getMinecraft();
			TileEntity tile = worldIn.getTileEntity(pos);
			if(tile == null)return super.getSelectedBoundingBox(state, worldIn, pos);
			float pitch = mc.player.rotationPitch;
			float yaw = mc.player.rotationYaw;
			Vec3d start = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
			float f1 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
			float f2 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
			float f3 = -MathHelper.cos(-pitch * 0.017453292F);
			float f4 = MathHelper.sin(-pitch * 0.017453292F);
			float f5 = f2 * f3;
			float f6 = f1 * f3;
			double d3 = 5.0D;
			Vec3d end = start.addVector(f5 * d3, f4 * d3, f6 * d3);
			RayTraceResult ray;
			List<AxisAlignedBB> list = new ArrayList<>();
			ray = collisionRayTrace(state, worldIn, pos, start, end);
			addSelectionBoxToList(state, worldIn, pos, list);
			if(ray != null){
				return list.get(ray.subHit).expandXyz(0.000001);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return FULL_BLOCK_AABB;
	}
	public void addSelectionBoxToList(IBlockState state, World world, BlockPos pos, List<AxisAlignedBB> boxes) {
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityCable){
			TileEntityCable c = (TileEntityCable) te;
			switch(c.type){
			case NORMAL:
				if(c.connections == 0)boxes.add(NO_CONNECTION.offset(pos));
				else boxes.add(CENTER.offset(pos));
				break;
			case VERTICAL:
				if(c.connects(EnumFacing.UP))boxes.add(VERTICAL.offset(pos));
				else boxes.add(CENTER.offset(pos));
				break;
			default:
				break;
			}
			for(EnumFacing f : EnumFacing.HORIZONTALS){
				if(c.connects(f)){
					boxes.add(CONNECT[f.ordinal() - 2].offset(pos));
				}
			}
		}
	}
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityCable)((TileEntityCable)te).invalidateGrid2();
	}
}
