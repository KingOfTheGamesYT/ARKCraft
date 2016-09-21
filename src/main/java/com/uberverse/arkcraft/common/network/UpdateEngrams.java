package com.uberverse.arkcraft.common.network;

import java.util.ArrayList;
import java.util.Collection;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Lewis_McReu
 */
public class UpdateEngrams implements IMessage
{
	private Collection<Short> engrams;
	private int points;

	public UpdateEngrams()
	{
		this.engrams = new ArrayList<>();
	}

	public UpdateEngrams(Collection<Short> engrams, int points)
	{
		this.engrams = engrams;
		this.points = points;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		points = buf.readInt();
		int size = buf.readInt();
		for (int i = 0; i < size; i++)
		{
			engrams.add(buf.readShort());
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(points);
		buf.writeInt(engrams.size());
		for (short i : engrams)
		{
			buf.writeShort(i);
		}
	}

	public static class Handler
			implements IMessageHandler<UpdateEngrams, IMessage>
	{
		@Override
		public IMessage onMessage(UpdateEngrams message, MessageContext ctx)
		{
			if (ctx.side.isClient())
			{
				ARKPlayer p =
						ARKPlayer.get(ARKCraft.proxy.getPlayerFromContext(ctx));
				p.updateUnlockedEngrams(message.engrams, message.points);
			}
			return null;
		}
	}
}
