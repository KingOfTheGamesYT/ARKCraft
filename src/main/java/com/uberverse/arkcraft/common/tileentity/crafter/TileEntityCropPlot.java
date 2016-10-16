package com.uberverse.arkcraft.common.tileentity.crafter;

import java.util.List;

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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityCropPlot extends TileEntityArkCraft implements IInventory, IUpdatePlayerListBox, IHoverInfo,
		IDecayer
{
	private ItemStack[] stack = new ItemStack[this.getSizeInventory()];
	private int growthTime = 0;
	private CropPlotState state = CropPlotState.EMPTY;
	private int water = 0;
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
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText(getName());
	}

	@Override
	public int getSizeInventory()
	{
		return 10;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		TileEntityCropPlot te = (TileEntityCropPlot) worldObj.getTileEntity(part.offset(pos, true));
		return te.stack[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int par2)
	{
		TileEntityCropPlot te = (TileEntityCropPlot) worldObj.getTileEntity(part.offset(pos, true));
		if (te.stack[slot] != null)
		{
			ItemStack itemstack;
			if (te.stack[slot].stackSize <= par2)
			{
				itemstack = te.stack[slot];
				te.stack[slot] = null;
				return itemstack;
			}
			else
			{
				itemstack = te.stack[slot].splitStack(par2);

				if (te.stack[slot].stackSize == 0)
				{
					te.stack[slot] = null;
				}
				return itemstack;
			}
		}
		else
		{
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
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return Utils.isUseable(pos, player, worldObj, this);
	}

	@Override
	public void openInventory(EntityPlayer player)
	{}

	@Override
	public void closeInventory(EntityPlayer player)
	{}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return stack != null ? stack.getItem() instanceof ItemBerry || stack.getItem() instanceof ARKCraftSeed || stack
				.getItem() instanceof ItemWaterContainer || stack.getItem() instanceof ItemFertilizer : false;
	}

	@Override
	public int getField(int id)
	{
		return id == 0 ? water : id == 1 ? fertilized ? 1 : 0 : 0;
	}

	@Override
	public void setField(int id, int value)
	{
		if (id == 0) water = value;
		else if (id == 1) fertilized = 1 == value;
	}

	@Override
	public int getFieldCount()
	{
		return 2;
	}

	@Override
	public void clear()
	{
		TileEntityCropPlot te = (TileEntityCropPlot) worldObj.getTileEntity(part.offset(pos, true));
		te.stack = new ItemStack[this.getSizeInventory()];
	}

	private boolean fertilized = false;

	private final boolean isRaining()
	{
		return worldObj.isRaining() && worldObj.canSeeSky(pos);
	}

	@Override
	public void update()
	{
		IBlockState state = worldObj.getBlockState(pos);
		if (state.getBlock() == Blocks.air) return;// Fixed crash on chunk unloading, and block breaking.
		if (!worldObj.isRemote)
		{
			if (isRaining())
			{
				int rand = worldObj.rand.nextInt(50);
				if (rand % 10 < 2)
				{
					if (part == Part.MIDDLE)
					{
						fillWithRain(false);
					}
					else
					{
						TileEntity tile = worldObj.getTileEntity(part.offset(pos, true));
						if (tile instanceof TileEntityCropPlot && tile.hasWorldObj())
						{
							((TileEntityCropPlot) tile).fillWithRain(false);
						}
					}
				}
			}
			if (part == Part.MIDDLE)
			{
				int sIndex = -1;
				int fIndex = -1;
				// finding seed and fertilizer (and consuming water if necessary)
				for (int i = 0; i < getSizeInventory(); i++)
				{
					if (stack[i] != null)
					{
						ItemStack stack = this.stack[i];
						Item item = stack.getItem();
						if (item instanceof ARKCraftSeed && sIndex < 0)
						{
							sIndex = i;
						}
						else if (item instanceof ItemFertilizer && fIndex < 0)
						{
							fIndex = i;
						}
						else if (item instanceof ItemWaterContainer)
						{
							int itemVal = getItemWaterValue(stack);
							int maxValAdded = getType().maxWater - water;
							int valToAdd = itemVal > maxValAdded ? maxValAdded : itemVal;

							itemVal -= valToAdd;

							if (valToAdd > 0)
							{
								water += valToAdd;
								ItemWaterContainer.setWaterValueLeft(stack, itemVal / 240);
								MathHelper.clamp_int(water, 0, getType().maxWater);
							}
						}
					}
				}

				if (growing == null && fIndex > -1 && sIndex > -1 && water > 0)
				{
					growthTime = CROP_PLOT.SEEDLING_TIME_FOR_BERRY * 20;
					if (LOG) LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()
							+ "]: Started Growing: " + stack[sIndex]);
					growing = decrStackSize(sIndex, 1);
					this.state = CropPlotState.SEEDED;
				}

				if (growing != null)
				{
					if (growthTime >= 0 && worldObj.getLight(pos) > 7)
					{
						if (fIndex > -1 && water > 0)
						{
							// grow!
							growthTime--;
							if (!isRaining()) water -= (worldObj.getDifficulty().ordinal() + 1) + 100;

							long val = ItemFertilizer.getFertilizingValueLeft(stack[fIndex]);
							ItemFertilizer.setFertilizingValueLeft(stack[fIndex], --val);
							fertilized = true;
							if (val <= 0)
							{
								ItemFertilizer.setFertilizingValueLeft(stack[fIndex], ((ItemFertilizer) stack[fIndex]
										.getItem()).getFertilizingTime());
								decrStackSize(fIndex, 1);
							}
						}
						else
						{
							int rand = worldObj.rand.nextInt(100);// 5% chance
							// if plant dies to return the seed
							boolean ret = false;
							if (rand % (worldObj.getDifficulty() == EnumDifficulty.NORMAL ? 40 : (worldObj
									.getDifficulty() == EnumDifficulty.EASY ? 30 : 20)) == 0 && worldObj
											.getDifficulty() != EnumDifficulty.HARD)
							{
								ret = TileEntityHopper.func_174918_a(this, growing, null) == null;
							}
							if (LOG) LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos
									.getZ() + "]: Crop died: " + growing + "Seed return: " + ret);
							growing = null;
							this.state = CropPlotState.EMPTY;
							setState(0, state);
						}
					}
					if (growthTime < 0)
					{
						if (state == CropPlotState.FRUITLING)
						{
							int d = worldObj.getDifficulty().ordinal();
							boolean success = d == 0 ? true : worldObj.rand.nextInt(d + 1) == 1;
							if (success)
							{
								ItemStack r = ARKCraftSeed.getBerryForSeed(growing);
								if (LOG) LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos
										.getZ() + "]: Growing Successful: " + growing + ", output: " + r);
								TileEntityHopper.func_174918_a(this, r, null);
							}
							else
							{
								if (LOG) LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos
										.getZ() + "]: Growing Failed: " + growing);
							}
							growthTime = MathHelper.floor_double(CROP_PLOT.FRUIT_OUTPUT_TIME_FOR_BERRY * (20D
									* ((worldObj.getDifficulty().ordinal() * 0.5D) + 1)));
						}
						else
						{
							this.state = this.state.next();
							if (LOG) LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos
									.getZ() + "]: Growing State Updated! Growing: " + growing + ", state: " + this.state
											.name());
							if (this.state.getTime() > 0) growthTime = MathHelper.floor_double(this.state.getTime()
									* (20D * ((worldObj.getDifficulty().ordinal() * 0.5D) + 1)));
							else growthTime = -1;
						}
					}
					setState(this.state.age, state);
				}
				else
				{
					setState(0, state);
				}
				markDirty();
				worldObj.markBlockForUpdate(pos);

				// TODO remove this code -- discuss with @tom5454
				// canGrow = false;
				// for (int i = 0; i < 10; i++)
				// {
				// if (stack[i] != null)
				// {
				// Item item = stack[i].getItem();
				// if (item instanceof ItemFertilizer)
				// {
				// fertilized = false;
				// if (canGrow)
				// {
				// long val = ItemFertilizer.getFertilizingValueLeft(stack[i]);
				// ItemFertilizer.setFertilizingValueLeft(stack[i], val--);
				// fertilized = true;
				// if (val <= 0)
				// {
				// ItemFertilizer.setFertilizingValueLeft(stack[i], i);
				// decrStackSize(i, 1);
				// }
				// }
				// }
				// else if (item instanceof ARKCraftSeed)
				// {
				// if (growing == null && ((ARKCraftSeed) item).getType().ordinal() <= getType().ordinal())
				// {
				// if (canGrow)
				// {
				// growthTime = CROP_PLOT.SEEDLING_TIME_FOR_BERRY * 20;
				// if (LOG) LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", "
				// + pos.getZ() + "]: Started Growing: " + stack[i]);
				// growing = decrStackSize(i, 1);
				// this.state = CropPlotState.SEEDED;
				// }
				// }
				// }
				// else
				// {
				// int itemWater = getItemWaterValue(stack[i]);
				// if (itemWater > 0)
				// {
				// if (water + itemWater <= getType().getMaxWater())
				// {
				// stack[i] = getContainerItem(stack[i]);
				// water += itemWater;
				// }
				// }
				// }
				// }
				// }
				// if (growing != null)
				// {
				// if (growthTime >= 0 && worldObj.getLight(pos) > 7)
				// {
				// if (canGrow)
				// {
				// growthTime--;
				// water -= (worldObj.getDifficulty().ordinal() + 1);
				// }
				// else
				// {
				// int rand = worldObj.rand.nextInt(100);// 5% chance
				// // if plant
				// // dies to
				// // return
				// // the seed
				// boolean ret = false;
				// if (rand % (worldObj.getDifficulty() == EnumDifficulty.NORMAL ? 40 : (worldObj
				// .getDifficulty() == EnumDifficulty.EASY ? 30 : 20)) == 0 && worldObj
				// .getDifficulty() != EnumDifficulty.HARD)
				// {
				// ret = TileEntityHopper.func_174918_a(this, growing, null) == null;
				// }
				// if (LOG) LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos
				// .getZ() + "]: Crop died: " + growing + "Seed return: " + ret);
				// growing = null;
				// this.state = CropPlotState.EMPTY;
				// setState(0, state);
				// }
				// }
				// if (growthTime < 0)
				// {
				// if (state == CropPlotState.FRUITLING)
				// {
				// int d = worldObj.getDifficulty().ordinal();
				// boolean success = d == 0 ? true : worldObj.rand.nextInt(d + 1) == 1;
				// if (success)
				// {
				// ItemStack r = ARKCraftSeed.getBerryForSeed(growing);
				// if (LOG) LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos
				// .getZ() + "]: Growing Successful: " + growing + ", output: " + r);
				// TileEntityHopper.func_174918_a(this, r, null);
				// }
				// else
				// {
				// if (LOG) LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos
				// .getZ() + "]: Growing Failed: " + growing);
				// }
				// growthTime = MathHelper.floor_double(CROP_PLOT.FRUIT_OUTPUT_TIME_FOR_BERRY * (20D
				// * ((worldObj.getDifficulty().ordinal() * 0.5D) + 1)));
				// }
				// else
				// {
				// this.state = this.state.next();
				// if (LOG) LogHelper.info("[Crop Plot at " + pos.getX() + ", " + pos.getY() + ", " + pos
				// .getZ() + "]: Growing State Updated! Growing: " + growing + ", state: " + this.state
				// .name());
				// if (this.state.getTime() > 0) growthTime = MathHelper.floor_double(this.state.getTime()
				// * (20D * ((worldObj.getDifficulty().ordinal() * 0.5D) + 1)));
				// else growthTime = -1;
				// }
				// }
				// setState(this.state.age, state);
				// // if(growthTime % 50 == 0)LogHelper.info("[Crop Plot at " +
				// // pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]:
				// // Growing: "+growing + ", growth remaining: " + growthTime
				// // + ", water: " + water + ", fertilizer: " + fertilizer +
				// // ".");
				// }
				// else
				// {
				// setState(0, state);
				// }
			}
		}
	}

	public static int getItemWaterValue(ItemStack stack)
	{
		if (stack != null && stack.getItem() instanceof ItemWaterContainer) return ItemWaterContainer.getWaterValueLeft(
				stack) * 2 * 120;
		return 0;
	}

	private void setState(int age, IBlockState state)
	{
		if (((Integer) state.getValue(BlockCropPlot.AGE)) != age)
		{
			TileEntity tileentity = worldObj.getTileEntity(pos);
			worldObj.setBlockState(pos, state.withProperty(BlockCropPlot.AGE, age), 3);
			if (tileentity != null)
			{
				tileentity.validate();
				worldObj.setTileEntity(pos, tileentity);
			}
			worldObj.markBlockForUpdate(pos);
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return null;
	}

	public static enum CropPlotState
	{
		EMPTY(0)
		{
			@Override
			public int getTime()
			{
				return 0;
			}

		},
		SEEDED(0)
		{
			@Override
			public int getTime()
			{
				return 0;
			}
		},
		SEEDLING(1)
		{
			@Override
			public int getTime()
			{
				return CROP_PLOT.SEEDLING_TIME_FOR_BERRY;
			}
		},
		MIDLING(2)
		{
			@Override
			public int getTime()
			{
				return CROP_PLOT.MIDLING_TIME_FOR_BERRY;
			}
		},
		GROWTHLING(3)
		{
			@Override
			public int getTime()
			{
				return CROP_PLOT.GROWTHING_TIME_FOR_BERRY;
			}
		},
		FRUITLING(4)
		{
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
		String seedName = growing != null ? growing.getUnlocalizedName() : "arkcraft.empty";
		String name = I18n.translate(seedName);
		if (name.equals(seedName)) name = I18n.format(seedName + ".name");
		text.add(EnumChatFormatting.YELLOW + I18n.translate("tile.crop_plot." + getType().name().toLowerCase()
				+ ".name"));
		text.add(I18n.format("arkcraft.growing") + ": " + I18n.format("arkcraft.cropPlotState.head", name, I18n.format(
				"arkcraft.cropPlotState." + state.name().toLowerCase())));
		String water = (this.water == 0 ? EnumChatFormatting.RED : this.water < getType().maxWater / 2
				? EnumChatFormatting.YELLOW : EnumChatFormatting.GREEN) + "" + (this.water / 20) + "/"
				+ getType().maxWater / 20 + EnumChatFormatting.BLUE;
		text.add(EnumChatFormatting.BLUE + I18n.format("arkcraft.water", I18n.format("tile.water.name"), water,
				getField(0) > 0 ? I18n.format("arkcraft.cropPlotWater.irrigated") : I18n.format(
						"arkcraft.cropPlotWater.notIrrigated")));
		long f = 0;
		for (int i = 0; i < 10; i++)
		{
			if (stack[i] != null)
			{
				Item item = stack[i].getItem();
				if (item instanceof ItemFertilizer)
				{
					f += ItemFertilizer.getFertilizingValueLeft(stack[i]) * stack[i].stackSize;
				}
			}
		}
		String toAdd = "" + f / 20;
		text.add("#8B4513" + I18n.format("arkcraft.gui.fertilizer", " " + toAdd));
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
		return (CropPlotType) worldObj.getBlockState(pos).getValue(BlockCropPlot.TYPE);
	}

	public BerryColor getGrowingColor()
	{
		return growing != null && growing.getItem() instanceof ARKCraftSeed ? ((ARKCraftSeed) growing.getItem())
				.getBerryColor(growing) : BerryColor.AMAR;
	}

	public boolean isTransparent()
	{
		return part != Part.MIDDLE;
	}

	public static enum Part
	{
		MIDDLE(null, null),
		NORTH(EnumFacing.NORTH, null),
		NORTH_EAST(EnumFacing.NORTH, EnumFacing.EAST),
		NORTH_WEST(EnumFacing.NORTH, EnumFacing.WEST),
		SOUTH(EnumFacing.SOUTH, null),
		SOUTH_EAST(EnumFacing.SOUTH, EnumFacing.EAST),
		SOUTH_WEST(EnumFacing.SOUTH, EnumFacing.WEST),
		WEST(EnumFacing.WEST, null),
		EAST(EnumFacing.EAST, null)

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
			if (m.getOpposite() == s) return null;
			for (Part p : VALUES)
			{
				if (p.m == m && p.s == s) { return p; }
			}
			return NORTH_WEST;
		}

		public BlockPos offset(BlockPos pos, boolean reverse)
		{
			if (m != null)
			{
				pos = pos.offset(reverse ? m.getOpposite() : m);
				if (s != null)
				{
					pos = pos.offset(reverse ? s.getOpposite() : s);
				}
			}
			return pos;
		}
	}

	public void fillWithRain(boolean big)
	{
		water = Math.min(water + (big ? 350 + worldObj.rand.nextInt(50) : worldObj.rand.nextInt(20) + 5),
				getType().maxWater);
	}

	@Override
	public IInventory getIInventory()
	{
		return this;
	}

	@Override
	public double getDecayModifier(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemBerry || stack.getItem() instanceof ItemFertilizer) return 200d;
		return IDecayer.super.getDecayModifier(stack);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(pos, 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		part = Part.VALUES[compound.getInteger("part")];
		if (!isTransparent())
		{
			InventoryUtil.readFromNBT(compound, this);
			growthTime = compound.getInteger("growth");
			water = compound.getInteger("water");
			fertilized = compound.getBoolean("fertilized");
			state = CropPlotState.VALUES[compound.getInteger("cropState")];
			growing = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("growing"));
			// transparent = compound.getBoolean("transparent");
			// getType() = CropPlotType.VALUES[compound.getInteger("plotType")];
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		InventoryUtil.writeToNBT(compound, this);
		compound.setInteger("growth", growthTime);
		compound.setInteger("water", water);
		compound.setBoolean("fertilized", fertilized);
		compound.setInteger("cropState", state.ordinal());
		NBTTagCompound g = new NBTTagCompound();
		if (growing != null) growing.writeToNBT(g);
		compound.setTag("growing", g);
		// compound.setBoolean("transparent", transparent);
		compound.setInteger("part", part.ordinal());
		// compound.setInteger("plotType", getType().ordinal());
		// return compound; //for mc 1.9
	}

	public TileEntityCropPlot getCenter()
	{
		if (!isTransparent()) return this;
		else
		{
			for (int x = -1; x < 2; x++)
				for (int z = -1; z < 2; z++)
				{
					BlockPos pos2 = pos.add(x, 0, z);
					if (!(x == pos.getX() && z == pos.getZ()) && worldObj.getBlockState(pos2)
							.getBlock() instanceof BlockCropPlot)
					{
						TileEntity t = worldObj.getTileEntity(pos2);
						if (t instanceof TileEntityCropPlot)
						{
							TileEntityCropPlot tcp = (TileEntityCropPlot) t;
							if (!tcp.isTransparent()) return tcp;
						}
					}
				}
		}

		return null;
	}

	// @Override
	// public void writeToNBTPacket(NBTTagCompound tag)
	// {
	// TileEntity tile = worldObj.getTileEntity(part.offset(pos, true));
	// if (tile instanceof TileEntityCropPlot)
	// {
	// ((TileEntityCropPlot) tile).writeToNBTPacket_p(tag);
	// }
	// }
	//
	// private void writeToNBTPacket_p(NBTTagCompound tag)
	// {
	// tag.setInteger("w", water);
	// tag.setString("n", growing != null ? growing.getUnlocalizedName() : "arkcraft.empty");
	// tag.setInteger("s", state.ordinal());
	// long f = 0;
	// for (int i = 0; i < 10; i++)
	// {
	// if (stack[i] != null)
	// {
	// Item item = stack[i].getItem();
	// if (item instanceof ItemFertilizer)
	// {
	// f += ItemFertilizer.getFertilizingValueLeft(stack[i]) * stack[i].stackSize;
	// }
	// }
	// }
	// tag.setLong("f", (f / 40)); // TODO why 40? @tom5454 ; also why integer before?
	// tag.setInteger("t", getType().ordinal());
	// tag.setInteger("d", worldObj.getDifficulty().ordinal());
	// }
	//
	// @Override
	// public void readFromNBTPacket(NBTTagCompound tag)
	// {
	// water = tag.getInteger("w");
	// seedName = tag.getString("n");
	// stateName = "arkcraft.cropPlotState." + CropPlotState.VALUES[tag.getInteger("s")].name().toLowerCase();
	// fertilizerClient = tag.getLong("f");
	// // type = CropPlotType.VALUES[tag.getInteger("t")];
	// stringType = "tile.crop_plot." + CropPlotType.VALUES[tag.getInteger("t")].name().toLowerCase() + ".name";
	// difficultyClient = tag.getInteger("d");
	// }
	//
	// @Override
	// public void writeToPacket(NBTTagCompound tag)
	// {
	// tag.setInteger("c", getGrowingColor().ordinal());
	// // tag.setBoolean("t", transparent);
	// tag.setInteger("p", part.ordinal());
	// tag.setInteger("d", worldObj.getDifficulty().ordinal());
	// }
	//
	// @Override
	// public void readFromPacket(NBTTagCompound tag)
	// {
	// colorClient = BerryColor.VALUES[tag.getInteger("c")];
	// // transparent = tag.getBoolean("t");
	// part = Part.VALUES[tag.getInteger("p")];
	// difficultyClient = tag.getInteger("d");
	// }
}
