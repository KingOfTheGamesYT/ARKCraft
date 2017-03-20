package com.uberverse.arkcraft.util;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTable
{
	public NBTTagCompound writeToNBT(NBTTagCompound compound);

	public void readFromNBT(NBTTagCompound compound);
}
