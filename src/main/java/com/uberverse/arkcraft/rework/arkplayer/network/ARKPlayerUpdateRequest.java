package com.uberverse.arkcraft.rework.arkplayer.network;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.rework.arkplayer.ARKPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ARKPlayerUpdateRequest implements IMessage
{
	public ARKPlayerUpdateRequest()
	{}

	private boolean all;

	public ARKPlayerUpdateRequest(boolean all)
	{
		System.out.println("send");
		this.all = all;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		System.out.println("read");
		all = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		System.out.println("write");
		buf.writeBoolean(all);
	}

	public static class Handler implements IMessageHandler<ARKPlayerUpdateRequest, ARKPlayerUpdate>
	{
		@Override
		public ARKPlayerUpdate onMessage(ARKPlayerUpdateRequest message, MessageContext ctx)
		{
			System.out.println("arrived");
			if (ctx.side.isServer())
			{
				EntityPlayerMP p = ctx.getServerHandler().playerEntity;
				ARKCraft.modChannel.sendTo(new ARKPlayerUpdate(ARKPlayer.get(p), message.all), p);
			}
			return null;
		}
	}
}
