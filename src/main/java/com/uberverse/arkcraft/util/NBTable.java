package com.uberverse.arkcraft.util;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTable
{
	public void writeToNBT(NBTTagCompound compound);

	public void readFromNBT(NBTTagCompound compound);
}
