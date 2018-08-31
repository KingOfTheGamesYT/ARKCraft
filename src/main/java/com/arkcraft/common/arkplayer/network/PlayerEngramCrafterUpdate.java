package com.arkcraft.common.arkplayer.network;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.arkplayer.ARKPlayer;
import com.arkcraft.common.engram.IEngramCrafter;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerEngramCrafterUpdate implements IMessage
{
	private IEngramCrafter crafter;
	private NBTTagCompound nbt;

	public PlayerEngramCrafterUpdate()
	{}

	public PlayerEngramCrafterUpdate(IEngramCrafter crafter)
	{
		this.crafter = crafter;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		NBTTagCompound n = new NBTTagCompound();
		crafter.writeToNBT(n);
		ByteBufUtils.writeTag(buf, n);
	}

	public static class Handler implements IMessageHandler<PlayerEngramCrafterUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(PlayerEngramCrafterUpdate message, MessageContext ctx)
		{
			if (ctx.side.isClient())
			{
				ARKPlayer.get(ARKCraft.proxy.getPlayerFromContext(ctx)).getEngramCrafter().readFromNBT(message.nbt);
			}
			return null;
		}
	}
}
