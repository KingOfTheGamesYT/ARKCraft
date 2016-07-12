package com.uberverse.arkcraft.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.tile.IHoverInfo;

import io.netty.buffer.ByteBuf;

public class MessageHover implements IMessage, IMessageHandler<MessageHover, IMessage>{

	BlockPos pos;
	NBTTagCompound tag;
	public MessageHover() {}
	public MessageHover(BlockPos pos, NBTTagCompound tag){
		this.pos = pos;
		this.tag = tag;
	}
	@Override
	public IMessage onMessage(final MessageHover message, final MessageContext ctx) {
		if(ctx.side == Side.CLIENT){
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {

				@Override
				public void run() {
					if(message.pos != null){
						TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(message.pos);
						if(tile instanceof IHoverInfo){
							((IHoverInfo)tile).readFromNBTPacket(message.tag);
						}
					}
				}
			});
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		ByteBufUtils.writeTag(buf, tag);
	}
	public static class MessageHoverReq implements IMessage, IMessageHandler<MessageHoverReq, MessageHover>{
		BlockPos pos;
		public MessageHoverReq() {
		}
		public MessageHoverReq(BlockPos pos) {
			this.pos = pos;
		}
		@Override
		public MessageHover onMessage(final MessageHoverReq message, final MessageContext ctx) {
			if(ctx.side == Side.SERVER){
				MinecraftServer.getServer().addScheduledTask(new Runnable() {

					@Override
					public void run() {
						if(message.pos != null){
							TileEntity tile = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.pos);
							if(tile instanceof IHoverInfo){
								NBTTagCompound tag = new NBTTagCompound();
								((IHoverInfo)tile).writeToNBTPacket(tag);
								ARKCraft.modChannel.sendTo(new MessageHover(message.pos, tag), ctx.getServerHandler().playerEntity);
							}
						}
					}
				});
			}
			return null;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(pos.getX());
			buf.writeInt(pos.getY());
			buf.writeInt(pos.getZ());
		}

	}
}
