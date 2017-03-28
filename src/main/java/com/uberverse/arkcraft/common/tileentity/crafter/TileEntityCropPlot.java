package com.uberverse.arkcraft.common.tileentity.crafter;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.uberverse.arkcraft.common.block.crafter.BlockCropPlot;
import com.uberverse.arkcraft.common.block.crafter.BlockCropPlot.BerryColor;
import com.uberverse.arkcraft.common.config.ModuleItemBalance.CROP_PLOT;
import com.uberverse.arkcraft.common.item.ARKCraftSeed;
import com.uberverse.arkcraft.common.item.ItemBerry;
import com.uberverse.arkcraft.common.item.ItemFertilizer;
import com.uberverse.arkcraft.common.item.ItemWaterContainer;
import com.uberverse.arkcraft.common.tileentity.IDecayer;
import com.uberverse.arkcraft.common.tileentity.IHoverInfo;
import com.uberverse.arkcraft.util.I18n;
import com.uberverse.arkcraft.util.InventoryUtil;
import com.uberverse.arkcraft.util.Utils;
import com.uberverse.lib.LogHelper;

public class TileEntityCropPlot extends TileEntityArkCraft implements IInventory, IHoverInfo, IDecayer, ITickable
{
	private ItemStack[] stack = new ItemStack[this.getSizeInventory()];
	private int growthTime = 0;
	private CropPlotState state = CropPlotState.EMPTY;
	private FluidTank water = new FluidTank(1600);
	private ItemStack growing;
	public Part part = Part.MIDDLE;
	private static boolean LOG = false;

	@Override
	public String getName()
	{
		return "cropPlot";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(getName());
	}

	@Override
	public int getSizeInventory()
	{
		return 10;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		TileEntityCropPlot te = (TileEntityCropPlot) world.getTileEntity(part.offset(pos, true));
		return te.stack[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int par2)
	{
		TileEntityCropPlot te = (TileEntityCropPlot) world.getTileEntity(part.offset(pos, true));
		if (te.stack[slot] != null) {
			ItemStack itemstack;
			if (te.stack[slot].stackSize <= par2) {
				itemstack = te.stack[slot];
				te.stack[slot] = null;
				return itemstack;
			}
			else {
				itemstack = te.stack[slot].splitStack(par2);

				if (te.stack[slot].stackSize == 0) {
					te.stack[slot] = null;
				}
				return itemstack;
			}
		}
		else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.stack[index] = stack;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 100;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return Utils.isUseable(pos, player, world, this);
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return stack != null ? stack.getItem() instanceof ItemBerry || stack.getItem() instanceof ARKCraftSeed || stack.getItem() instanceof ItemWaterContainer || stack.getItem() instanceof ItemFertilizer : false;
	}

	@Override
	public int getField(int id)
	{
		return id == 0 ? water.getFluidAmount() : id == 1 ? fertilized ? 1 : 0 : 0;
	}

	@Override
	public void setField(int id, int value)
	{
		if (id == 0)
			water.setFluid(new FluidStack(FluidRegistry.WATER, value));
		else if (id == 1)
			fertilized = 1 == value;
	}

	@Override
	public int getFieldCount()
	{
		return 2;
	}

	@Override
	public void clear()
	{
		TileEntityCropPlot te = (TileEntityCropPlot) world.getTileEntity(part.offset(pos, true));
		te.stack = new ItemStack[this.getSizeInventory()];
	}

	private boolean fertilized = false;

	private final boolean isRaining()
	{
		return world.isRaining() && world.canSeeSky(pos);
	}

	@Override
	public void update()
	{
		IBlockState state = world.getBlockState(pos);
		CropPlotType type = state.getValue(BlockCropPlot.TYPE);
		water.setCapacity(type.maxWater);
		if (state.getBlock() == Blocks.AIR)
			return;// Fixed crash on chunk unloading, and block breaking.
		if (!world.isRemote) {
			if (isRaining()) {
				int rand = world.rand.nextInt(50);
				if (rand % 10 < 2) {
					if (part == Part.MIDDLE) {
						fillWithRain(false);
					}
					else {
						TileEntity tile = world.getTileEntity(part.offset(pos, true));
						if (tile instanceof TileEntityCropPlot && tile.hasWorld()) {
							((TileEntityCropPlot) tile).fillWithRain(false);
						}
					}
				}
			}
			if (part == Part.MIDDLE) {
				int sIndex = -1;
				int fIndex = -1;
				// finding seed and fertilizer (and consuming water if
				// necessary)
				for (int i = 0; i < getSizeInventory(); i++) {
					if (stack[i] != null) {
						ItemStack stack = this.stack[i];
						Item item = stack.getItem();
						if (item instanceof ARKCraftSeed && sIndex < 0) {
							sIndex = i;
						}
						else if (item instanceof ItemFertilizer && fIndex < 0) {
							fIndex = i;
						}
						else if (item instanceof ItemWaterContainer) {
							int itemVal = getItemWaterValue(stack);
							int maxValAdded = type.maxWater - water.getFluidAmount();
							int valToAdd = itemVal > maxValAdded ? maxValAdded : itemVal;

							itemVal -= valToAdd;

							if (valToAdd > 0) {
								water.fillInternal(new FluidStack(FluidRegistry.WATER, valToAdd), true);
								ItemWaterContainer.setWaterValueLeft(stack, itemVal / 240);
							}
						}
					}
				}

				if (growing == null && fIndex > -1 && sIndex > -1 && water.getFluidAmount() > 0) {
					growthTime = CROP_PLOT.SEEDLING_TIME_FOR_BERRY * 20;
					if (LOG)
						LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]: Started Growing: " + stack[sIndex]);
					growing = decrStackSize(sIndex, 1);
					this.state = CropPlotState.SEEDED;
				}

				if (growing != null) {
					if (growthTime >= 0 && world.getLight(pos) > 7) {
						if (fIndex > -1 && water.getFluidAmount() > 0) {
							// grow!
							growthTime--;
							if (!isRaining())
								water.drain(world.getDifficulty().ordinal() + 1, true);

							long val = ItemFertilizer.getFertilizingValueLeft(stack[fIndex]);
							ItemFertilizer.setFertilizingValueLeft(stack[fIndex], --val);
							fertilized = true;
							if (val <= 0) {
								ItemFertilizer.setFertilizingValueLeft(stack[fIndex], ((ItemFertilizer) stack[fIndex].getItem()).getFertilizingTime());
								decrStackSize(fIndex, 1);
							}
						}
						else {
							int rand = world.rand.nextInt(100);// 5% chance
							// if plant dies to return the seed
							boolean ret = false;
							if (rand % (world.getDifficulty() == EnumDifficulty.NORMAL ? 40 : (world.getDifficulty() == EnumDifficulty.EASY ? 30 : 20)) == 0 && world.getDifficulty() != EnumDifficulty.HARD) {
								ret = TileEntityHopper.putStackInInventoryAllSlots(this, growing, null) == null;
							}
							if (LOG)
								LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]: Crop died: " + growing + "Seed return: " + ret);
							growing = null;
							this.state = CropPlotState.EMPTY;
							setState(0, state);
						}
					}
					if (growthTime < 0) {
						if (this.state == CropPlotState.FRUITLING) {
							int d = world.getDifficulty().ordinal();
							boolean success = d == 0 ? true : world.rand.nextInt(d + 1) == 1;
							if (success) {
								ItemStack r = ARKCraftSeed.getBerryForSeed(growing);
								if (LOG)
									LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]: Growing Successful: " + growing + ", output: " + r);
								TileEntityHopper.putStackInInventoryAllSlots(this, r, null);
							}
							else {
								if (LOG)
									LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]: Growing Failed: " + growing);
							}
							growthTime = MathHelper.floor(CROP_PLOT.FRUIT_OUTPUT_TIME_FOR_BERRY * (20D * ((world.getDifficulty().ordinal() * 0.5D) + 1)));
						}
						else {
							this.state = this.state.next();
							if (LOG)
								LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]: Growing State Updated! Growing: " + growing + ", state: " + this.state.name());
							if (this.state.getTime() > 0)
								growthTime = MathHelper.floor(this.state.getTime() * (20D * ((world.getDifficulty().ordinal() * 0.5D) + 1)));
							else
								growthTime = -1;
						}
					}
					setState(this.state.age, state);
				}
				else {
					setState(0, state);
				}
				markDirty();
				markBlockForUpdate();
			}
		}
	}

	public static int getItemWaterValue(ItemStack stack)
	{
		if (stack != null && stack.getItem() instanceof ItemWaterContainer)
			return ItemWaterContainer.getWaterValueLeft(stack) * 2 * 120;
		return 0;
	}

	private void setState(int age, IBlockState state)
	{
		if ((state.getValue(BlockCropPlot.AGE)) != age) {
			TileEntity tileentity = world.getTileEntity(pos);
			world.setBlockState(pos, state.withProperty(BlockCropPlot.AGE, age), 3);
			if (tileentity != null) {
				tileentity.validate();
				world.setTileEntity(pos, tileentity);
			}
			markDirty();
			// world.markBlockForUpdate(pos);
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return null;
	}

	public static enum CropPlotState
	{
		EMPTY(0) {
			@Override
			public int getTime()
			{
				return 0;
			}

		},
		SEEDED(0) {
			@Override
			public int getTime()
			{
				return 0;
			}
		},
		SEEDLING(1) {
			@Override
			public int getTime()
			{
				return CROP_PLOT.SEEDLING_TIME_FOR_BERRY;
			}
		},
		MIDLING(2) {
			@Override
			public int getTime()
			{
				return CROP_PLOT.MIDLING_TIME_FOR_BERRY;
			}
		},
		GROWTHLING(3) {
			@Override
			public int getTime()
			{
				return CROP_PLOT.GROWTHING_TIME_FOR_BERRY;
			}
		},
		FRUITLING(4) {
			@Override
			public int getTime()
			{
				return -1;
			}
		}

		;
		public final int age;
		public static final CropPlotState[] VALUES = values();

		public abstract int getTime();

		public CropPlotState next()
		{
			return VALUES[(ordinal() + 1) % VALUES.length];
		}

		private CropPlotState(int age)
		{
			this.age = age;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> text)
	{
		String name = I18n.translate(growing != null ? growing.getUnlocalizedName() + ".name" : "arkcraft.empty");
		text.add(TextFormatting.YELLOW + I18n.translate("tile.crop_plot." + getType().name().toLowerCase() + ".name"));
		text.add(I18n.format("arkcraft.growing") + ": " + I18n.format("arkcraft.cropPlotState.head", name, I18n.format("arkcraft.cropPlotState." + state.name().toLowerCase())));
		String water = (this.water.getFluidAmount() == 0 ? TextFormatting.RED : this.water.getFluidAmount() < getType().maxWater / 2 ? TextFormatting.YELLOW : TextFormatting.GREEN) + "" + (this.water.getFluidAmount() / 20) + "/" + getType().maxWater / 20 + TextFormatting.BLUE;
		text.add(TextFormatting.BLUE + I18n.format("arkcraft.water", I18n.format("tile.water.name"), water, getField(0) > 0 ? I18n.format("arkcraft.cropPlotWater.irrigated") : I18n.format("arkcraft.cropPlotWater.notIrrigated")));
		long f = 0;
		for (int i = 0; i < 10; i++) {
			if (stack[i] != null) {
				Item item = stack[i].getItem();
				if (item instanceof ItemFertilizer) {
					f += ItemFertilizer.getFertilizingValueLeft(stack[i]) * stack[i].stackSize;
				}
			}
		}
		String toAdd = "" + f / 20;
		text.add("#8B4513" + I18n.format("arkcraft.gui.fertilizer", toAdd));
		if(growthTime > 0)text.add(TextFormatting.GRAY + I18n.format("arkcraft.cropPlotGrowth", Utils.formatTime(growthTime).substring(1)));
	}

	public ItemStack[] getStack()
	{
		return stack;
	}

	public void setStack(ItemStack[] stack)
	{
		this.stack = stack;
	}

	public static enum CropPlotType implements IStringSerializable
	{
		SMALL(200), MEDIUM(400), LARGE(600);

		public static final CropPlotType[] VALUES = values();
		public final int maxWater;

		private CropPlotType(int maxWater)
		{
			this.maxWater = maxWater * 120;
		}

		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}

	public CropPlotType getType()
	{
		return world.getBlockState(pos).getValue(BlockCropPlot.TYPE);
	}

	public BerryColor getGrowingColor()
	{
		return growing != null && growing.getItem() instanceof ARKCraftSeed ? ((ARKCraftSeed) growing.getItem()).getBerryColor(growing) : BerryColor.AMAR;
	}

	public boolean isTransparent()
	{
		return part != Part.MIDDLE;
	}

	public static enum Part
	{
		MIDDLE(null, null), NORTH(EnumFacing.NORTH, null), NORTH_EAST(EnumFacing.NORTH, EnumFacing.EAST),
		NORTH_WEST(EnumFacing.NORTH, EnumFacing.WEST), SOUTH(EnumFacing.SOUTH, null), SOUTH_EAST(EnumFacing.SOUTH, EnumFacing.EAST),
		SOUTH_WEST(EnumFacing.SOUTH, EnumFacing.WEST), WEST(EnumFacing.WEST, null), EAST(EnumFacing.EAST, null)

		;
		private final EnumFacing m, s;

		private Part(EnumFacing m, EnumFacing s)
		{
			this.m = m;
			this.s = s;
		}

		public static final Part[] VALUES = values();

		public static Part getPart(EnumFacing m, EnumFacing s)
		{
			if (m.getOpposite() == s)
				return null;
			for (Part p : VALUES) {
				if (p.m == m && p.s == s) {
					return p;
				}
			}
			return NORTH_WEST;
		}

		public BlockPos offset(BlockPos pos, boolean reverse)
		{
			if (m != null) {
				pos = pos.offset(reverse ? m.getOpposite() : m);
				if (s != null) {
					pos = pos.offset(reverse ? s.getOpposite() : s);
				}
			}
			return pos;
		}
	}

	public void fillWithRain(boolean big)
	{
		water.fill(new FluidStack(FluidRegistry.WATER, big ? 350 + world.rand.nextInt(50) : world.rand.nextInt(20) + 5), true);
	}

	@Override
	public IInventory getIInventory()
	{
		return this;
	}

	@Override
	public double getDecayModifier(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemBerry || stack.getItem() instanceof ItemFertilizer)
			return 200d;
		return IDecayer.super.getDecayModifier(stack);
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
	}
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		part = Part.VALUES[compound.getInteger("part")];
		if (!isTransparent()) {
			InventoryUtil.readFromNBT(compound, this);
			growthTime = compound.getInteger("growth");
			water.readFromNBT(compound.getCompoundTag("waterTank"));
			fertilized = compound.getBoolean("fertilized");
			state = CropPlotState.VALUES[compound.getInteger("cropState")];
			growing = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("growing"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		InventoryUtil.writeToNBT(compound, this);
		compound.setInteger("growth", growthTime);
		compound.setTag("waterTank", water.writeToNBT(new NBTTagCompound()));
		compound.setBoolean("fertilized", fertilized);
		compound.setInteger("cropState", state.ordinal());
		NBTTagCompound g = new NBTTagCompound();
		if (growing != null)
			growing.writeToNBT(g);
		compound.setTag("growing", g);
		compound.setInteger("part", part.ordinal());
		return compound;
	}

	public TileEntityCropPlot getCenter()
	{
		if (!isTransparent())
			return this;
		else {
			for (int x = -1; x < 2; x++)
				for (int z = -1; z < 2; z++) {
					BlockPos pos2 = pos.add(x, 0, z);
					if (!(x == pos.getX() && z == pos.getZ()) && world.getBlockState(pos2).getBlock() instanceof BlockCropPlot) {
						TileEntity t = world.getTileEntity(pos2);
						if (t instanceof TileEntityCropPlot) {
							TileEntityCropPlot tcp = (TileEntityCropPlot) t;
							if (!tcp.isTransparent())
								return tcp;
						}
					}
				}
		}

		return null;
	}
	public FluidTank getWater() {
		return water;
	}
}
