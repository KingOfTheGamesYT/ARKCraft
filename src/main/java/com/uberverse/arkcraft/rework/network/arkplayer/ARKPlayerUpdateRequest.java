package com.uberverse.arkcraft.rework.network.arkplayer;

import com.uberverse.arkcraft.rework.ARKPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ARKPlayerUpdateRequest implements IMessage
{
	public ARKPlayerUpdateRequest()
	{
	}

	private boolean all;

	public ARKPlayerUpdateRequest(boolean all)
	{
		this.all = all;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		all = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(all);
	}

	public static class Handler implements IMessageHandler<ARKPlayerUpdateRequest, ARKPlayerUpdate>
	{
		@Override
		public ARKPlayerUpdate onMessage(ARKPlayerUpdateRequest message, MessageContext ctx)
		{
			if (ctx.side.isServer())
			{
				EntityPlayerMP p = ctx.getServerHandler().playerEntity;
				return new ARKPlayerUpdate(ARKPlayer.get(p), message.all);
			}
			return null;
		}
	}
}
