package com.arkcraft.common.network;

import com.arkcraft.common.arkplayer.ARKPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ARKModeToggle implements IMessage
{
	@Override
	public void fromBytes(ByteBuf buf)
	{

	}

	@Override
	public void toBytes(ByteBuf buf)
	{

	}

	public static class Handler implements IMessageHandler<ARKModeToggle, IMessage>
	{
		@Override
		public IMessage onMessage(final ARKModeToggle message, MessageContext ctx)
		{
			if (ctx.side != Side.SERVER)
			{
				System.err.println("MPUpdateDoReloadStarted received on wrong side:" + ctx.side);
				return null;
			}
			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			player.getServer().addScheduledTask(new Runnable()
			{
				public void run()
				{
					ARKPlayer.get(player).toggleARKMode();
				}
			});
			return null;
		}
	}

}
