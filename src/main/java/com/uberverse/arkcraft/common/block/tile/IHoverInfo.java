package com.uberverse.arkcraft.common.block.tile;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Implement this on a {@link net.minecraft.tileentity.TileEntity} to allow
 * hovering text on the client upon looking at the block.
 *
 * @author tom5454
 */
public interface IHoverInfo
{
	@SideOnly(Side.CLIENT)
	void addInformation(List<String> text);

	void writeToNBTPacket(NBTTagCompound tag);

	void readFromNBTPacket(NBTTagCompound tag);
}
