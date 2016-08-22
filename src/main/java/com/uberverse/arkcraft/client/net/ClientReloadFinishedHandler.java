package com.uberverse.arkcraft.client.net;

import com.uberverse.arkcraft.common.item.firearms.ItemRangedWeapon;
import com.uberverse.arkcraft.common.network.ReloadFinished;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ClientReloadFinishedHandler implements IMessageHandler<ReloadFinished, IMessage>
{
	@Override
	public IMessage onMessage(final ReloadFinished message, MessageContext ctx)
	{
		if (ctx.side != Side.CLIENT)
		{
			System.err.println("ReloadFinished received on wrong side:" + ctx.side);
			return null;
		}
		processMessage(message, Minecraft.getMinecraft().thePlayer);
		return null;
	}

	static void processMessage(ReloadFinished message, EntityPlayer player)
	{
		if (player != null)
		{
			ItemStack stack = player.getCurrentEquippedItem();
			if (stack != null && stack
					.getItem() instanceof ItemRangedWeapon) ((ItemRangedWeapon) stack.getItem())
							.setReloading(stack, player, false);
		}
	}
}
