package com.arkcraft.common.network;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.item.ranged.ItemRangedWeapon;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ReloadFinished implements IMessage
{
	public ReloadFinished()
	{}

	@Override
	public void fromBytes(ByteBuf buf)
	{}

	@Override
	public void toBytes(ByteBuf buf)
	{}

	public static class Handler implements IMessageHandler<ReloadFinished, IMessage>
	{
		@Override
		public IMessage onMessage(final ReloadFinished message, MessageContext ctx)
		{
			if (ctx.side != Side.CLIENT)
			{
				System.err.println("ReloadFinished received on wrong side:" + ctx.side);
				return null;
			}
			processMessage(message, ARKCraft.proxy().getPlayerFromContext(ctx));
			return null;
		}

		static void processMessage(ReloadFinished message, EntityPlayer player)
		{
			if (player != null)
			{
				ItemStack stack = player.getHeldItemMainhand();
				if (stack != null && stack.getItem() instanceof ItemRangedWeapon) ((ItemRangedWeapon) stack.getItem())
						.setReloading(stack, player, false);
			}
		}
	}

}
