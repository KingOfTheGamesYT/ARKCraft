package com.uberverse.arkcraft.common.network;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.block.container.ContainerInventoryCampfire;

import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CampfireToggleMessage implements IMessage
{
	@Override
	public void fromBytes(ByteBuf buf)
	{
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
	}

	public static class Handler implements IMessageHandler<CampfireToggleMessage, IMessage>
	{
		@Override
		public IMessage onMessage(CampfireToggleMessage message, MessageContext ctx)
		{
			ARKCraft.modLog.info("receive");
			if (ctx.side.isServer())
			{
				Container c = ctx.getServerHandler().playerEntity.openContainer;
				if (c instanceof ContainerInventoryCampfire) ((ContainerInventoryCampfire) c)
						.toggleBurning();
			}
			return null;
		}
	}
}