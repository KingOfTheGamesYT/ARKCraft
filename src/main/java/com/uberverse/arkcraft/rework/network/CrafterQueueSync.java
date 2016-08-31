package com.uberverse.arkcraft.rework.network;

import java.util.ArrayList;
import java.util.Collection;

import com.uberverse.arkcraft.rework.container.ContainerEngramCrafting;
import com.uberverse.arkcraft.rework.engram.CraftingOrder;
import com.uberverse.arkcraft.rework.engram.EngramManager;
import com.uberverse.arkcraft.rework.engram.IEngramCrafter;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CrafterQueueSync implements IMessage
{

	public CrafterQueueSync()
	{
		list = new ArrayList<>();
	}

	private Collection<CraftingOrder> list;

	public CrafterQueueSync(IEngramCrafter crafter)
	{
		list = crafter.getCraftingQueue();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		while (buf.readBoolean())
		{
			list.add(new CraftingOrder(EngramManager.instance().getEngram(buf.readShort()), buf.readInt()));
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		for (CraftingOrder o : list)
		{
			buf.writeBoolean(true);
			buf.writeShort(o.getEngram().getId());
			buf.writeInt(o.getCount());
		}
		buf.writeBoolean(false);
	}

	public static class Handler implements IMessageHandler<CrafterQueueSync, IMessage>
	{

		@Override
		public IMessage onMessage(CrafterQueueSync message, MessageContext ctx)
		{
			if (ctx.side.isClient() && Minecraft.getMinecraft().thePlayer.openContainer instanceof ContainerEngramCrafting)
			{
				
			}
			return null;
		}

	}
}
