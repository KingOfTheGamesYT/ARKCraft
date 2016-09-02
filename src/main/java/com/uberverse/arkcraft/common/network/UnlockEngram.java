package com.uberverse.arkcraft.common.network;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Lewis_McReu
 */
public class UnlockEngram implements IMessage
{
	public UnlockEngram()
	{}

	private short engramId;

	public UnlockEngram(short engramId)
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

	public static class Handler implements IMessageHandler<UnlockEngram, UpdateEngrams>
	{
		@Override
		public UpdateEngrams onMessage(UnlockEngram message, MessageContext ctx)
		{
			if (ctx.side.isServer())
			{
				ARKPlayer p = ARKPlayer.get(ctx.getServerHandler().playerEntity);
				p.learnEngram(message.engramId);
				return new UpdateEngrams(p.getUnlockedEngrams(), p.getEngramPoints());
			}
			return null;
		}
	}
}