package com.uberverse.arkcraft.rework;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTable
{
	public void writeToNBT(NBTTagCompound compound);

	public void readFromNBT(NBTTagCompound compound);
}
