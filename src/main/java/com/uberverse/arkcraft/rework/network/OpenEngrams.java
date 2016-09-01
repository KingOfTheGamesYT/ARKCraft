package com.uberverse.arkcraft.rework.network;

import com.uberverse.arkcraft.ARKCraft;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenEngrams implements IMessage
{
	@Override
	public void fromBytes(ByteBuf buf)
	{}

	@Override
	public void toBytes(ByteBuf buf)
	{}

	public static class Handler implements IMessageHandler<OpenEngrams, IMessage>
	{
		@Override
		public IMessage onMessage(OpenEngrams message, MessageContext ctx)
		{
			if (ctx.side.isServer())
			{
				final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				player.getServerForPlayer().addScheduledTask(new Runnable()
				{
					public void run()
					{
						player.openGui(ARKCraft.instance, ARKCraft.GUI.ENGRAM_GUI.getID(), player.worldObj, 0, 0, 0);
					}
				});
			}
			return null;
		}
	}
}
