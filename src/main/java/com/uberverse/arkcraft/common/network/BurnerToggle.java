package com.uberverse.arkcraft.common.network;

import com.uberverse.arkcraft.common.container.block.IBurner;

import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BurnerToggle implements IMessage
{
	@Override
	public void fromBytes(ByteBuf buf)
	{}

	@Override
	public void toBytes(ByteBuf buf)
	{}

	public static class Handler implements IMessageHandler<BurnerToggle, IMessage>
	{
		@Override
		public IMessage onMessage(BurnerToggle message, MessageContext ctx)
		{
			if (ctx.side.isServer())
			{
				Container c = ctx.getServerHandler().playerEntity.openContainer;
				if (c instanceof IBurner) ((IBurner) c).toggleBurning();
			}
			return null;
		}
	}
}