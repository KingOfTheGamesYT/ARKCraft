package com.uberverse.arkcraft.common.network;

import com.uberverse.arkcraft.rework.container.ContainerEngramCrafting;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CraftMessage implements IMessage
{
	public CraftMessage()
	{}

	private boolean craftAll;

	public CraftMessage(boolean state)
	{
		System.out.println("craft " + (state ? "all" : "one"));
		this.craftAll = state;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		craftAll = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(craftAll);
	}

	public static class Handler implements IMessageHandler<CraftMessage, IMessage>
	{
		@Override
		public IMessage onMessage(CraftMessage message, MessageContext ctx)
		{
			if (ctx.side.isServer())
			{
				if (ctx.getServerHandler().playerEntity.openContainer instanceof ContainerEngramCrafting)
				{
					System.out.println("Arrived");
					ContainerEngramCrafting c = (ContainerEngramCrafting) ctx.getServerHandler().playerEntity.openContainer;
					if (message.craftAll) c.craftAll();
					else c.craftOne();
				}
			}
			return null;
		}
	}
}