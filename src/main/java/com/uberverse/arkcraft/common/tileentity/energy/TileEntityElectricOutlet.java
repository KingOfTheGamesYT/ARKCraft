package com.uberverse.arkcraft.common.tileentity.energy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Vec3d;

import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityArkCraft;

public class TileEntityElectricOutlet extends TileEntityArkCraft implements ITickable, IEnergyGridDevice {
	private EnergyGrid grid;
	private static final int RANGE = 5;
	private List<IEnergyConsumer> consumers = new ArrayList<>();
	private List<Vec3d> consumersClient = new ArrayList<>();
	private int powered;
	private boolean poweredLast;
	@Override
	public void invalidateGrid(){
		grid = null;
	}
	@Override
	public void setGrid(EnergyGrid grid) {
		this.grid = grid;
	}
	@Override
	public final void update() {
		if(!world.isRemote){
			if(world.getTotalWorldTime() % 20 == 0){
				if(grid != null){
					List<IEnergyConsumer> consumersOld = new ArrayList<>(consumers);
					consumers.clear();
					Iterable<MutableBlockPos> ps = BlockPos.getAllInBoxMutable(pos.add(-RANGE, -RANGE, -RANGE), pos.add(RANGE, RANGE, RANGE));
					for(MutableBlockPos p : ps){
						TileEntity tile = world.getTileEntity(p);
						if(tile != null && tile instanceof IEnergyConsumer && !consumers.contains(tile)){
							consumers.add((IEnergyConsumer) tile);
						}
					}
					consumersOld.removeAll(consumers);
					consumersOld.forEach(c -> c.setPowered(false));
					markBlockForUpdate();
				}
			}else{
				boolean p = grid != null && grid.isActive();
				if(p != poweredLast || world.getTotalWorldTime() % 10 == 0){
					poweredLast = p;
					if(!p){
						powered = 5;
					}
					consumers.forEach(c -> c.setPowered(p));
				}
				if(powered > 0){
					powered--;
					if(powered == 0 && !p){
						consumers.forEach(c -> c.setPowered(false));
					}
				}
			}
		}
	}
	@Override
	public BlockPos getPos2() {
		return pos;
	}
	public void invalidateGrid2() {
		if(grid != null)grid.invalidate();
	}
	@Override
	public boolean canConnect(EnumFacing face) {
		return true;
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		consumers.stream().map(c -> new Vec3d(c.getPos2()).add(c.getRenderPoint())).map(TileEntityElectricOutlet::writeVec3d).forEach(list::appendTag);
		compound.setTag("consumers", list);
		return super.writeToNBT(compound);
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		NBTTagList list = compound.getTagList("consumers", 10);
		consumersClient.clear();
		for(int i = 0;i<list.tagCount();i++){
			NBTTagCompound tag = list.getCompoundTagAt(i);
			consumersClient.add(new Vec3d(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z")));
		}
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new SPacketUpdateTileEntity(pos, 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
		world.markBlockRangeForRenderUpdate(pos, pos);
	}
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}
	public static NBTTagCompound writeVec3d(Vec3d vec) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setDouble("x", vec.xCoord);
		tag.setDouble("y", vec.yCoord);
		tag.setDouble("z", vec.zCoord);
		return tag;
	}
	public List<Vec3d> getConsumersClient() {
		return consumersClient;
	}
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
