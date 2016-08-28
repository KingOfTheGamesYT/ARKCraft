package com.uberverse.arkcraft.common.network;

import com.uberverse.arkcraft.common.entity.data.ARKPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UnlockEngramMessage implements IMessage
{
	public UnlockEngramMessage()
	{
	}

	private short engramId;

	public UnlockEngramMessage(short engramId)
	{
		this.engramId = engramId;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		engramId = buf.readShort();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeShort(engramId);
	}

	public static class Handler implements IMessageHandler<UnlockEngramMessage, IMessage>
	{
		@Override
		public IMessage onMessage(UnlockEngramMessage message, MessageContext ctx)
		{
			if (ctx.side.isServer())
			{
				ARKPlayer.get(ctx.getServerHandler().playerEntity).learnEngram(message.engramId);
			}
			return null;
		}
	}
}