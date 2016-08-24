package com.uberverse.arkcraft.common.network;

import com.uberverse.arkcraft.common.block.container.ContainerInventoryForge;

import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ForgeToggleMessage implements IMessage
{
	@Override
	public void fromBytes(ByteBuf buf)
	{
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
	}

	public static class Handler implements IMessageHandler<ForgeToggleMessage, IMessage>
	{
		@Override
		public IMessage onMessage(ForgeToggleMessage message, MessageContext ctx)
		{
			if (ctx.side.isServer())
			{
				Container c = ctx.getServerHandler().playerEntity.openContainer;
				if (c instanceof ContainerInventoryForge) ((ContainerInventoryForge) c)
						.toggleBurning();
			}
			return null;
		}
	}
}