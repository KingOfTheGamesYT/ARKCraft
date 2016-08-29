package com.uberverse.arkcraft.rework.network.arkplayer;

import com.uberverse.arkcraft.rework.ARKPlayer;
import com.uberverse.arkcraft.rework.ARKPlayer.Variable;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ARKPlayerUpdate implements IMessage
{
	public ARKPlayerUpdate()
	{
	}

	private ARKPlayer player;
	private boolean all;

	public ARKPlayerUpdate(ARKPlayer player, boolean all)
	{
		this.player = player;
		this.all = all;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		all = buf.readBoolean();
		player = ARKPlayer.getDefault();
		if (all)
		{
			for (Variable<?> var : player.getStats().values())
			{
				var.read(buf);
			}
		}
		else
		{
			for (Variable<?> var : player.getStats().values())
			{
				var.readConditionally(buf);
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(all);
		if (all)
		{
			for (Variable<?> var : player.getStats().values())
			{
				var.write(buf);
			}
		}
		else
		{
			for (Variable<?> var : player.getStats().values())
			{
				var.writeConditionally(buf);
			}
		}
	}

	private void store(ARKPlayer player)
	{
		if (all) player.copy(this.player);
		else player.copyConditionally(this.player);
	}

	public static class Handler implements IMessageHandler<ARKPlayerUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(ARKPlayerUpdate message, MessageContext ctx)
		{
			if (ctx.side.isClient())
			{
				message.store(ARKPlayer.get(Minecraft.getMinecraft().thePlayer));
			}
			return null;
		}
	}
}
