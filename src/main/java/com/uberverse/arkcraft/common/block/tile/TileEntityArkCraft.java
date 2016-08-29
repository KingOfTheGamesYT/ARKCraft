package com.uberverse.arkcraft.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import com.uberverse.arkcraft.ARKCraft;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class TileEntityArkCraft extends TileEntity
{
	@Override
	public Packet getDescriptionPacket()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		NBTTagCompound tag = new NBTTagCompound();
		writeToPacket(tag);
		ByteBufUtils.writeTag(buf, tag);
		return new FMLProxyPacket(new PacketBuffer(buf), ARKCraft.descriptionPacketChannel);
	}

	public void writeToPacket(NBTTagCompound tag)
	{
	}

	public void readFromPacket(NBTTagCompound tag)
	{
	}
}
