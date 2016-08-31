package com.uberverse.arkcraft.rework.arkplayer.network;

import com.uberverse.arkcraft.rework.arkplayer.ARKPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerEngramCrafterProgressUpdate implements IMessage
{

	private int progress;

	public PlayerEngramCrafterProgressUpdate()
	{
	}

	public PlayerEngramCrafterProgressUpdate(int progress)
	{
		this.progress = progress;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		progress = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(progress);
	}

	public static class Handler implements IMessageHandler<PlayerEngramCrafterProgressUpdate, IMessage>
	{

		@Override
		public IMessage onMessage(PlayerEngramCrafterProgressUpdate message, MessageContext ctx)
		{
			if (ctx.side.isClient())
			{
				ARKPlayer.get(Minecraft.getMinecraft().thePlayer).getEngramCrafter()
						.setProgress(message.progress);
			}
			return null;
		}

	}
}
