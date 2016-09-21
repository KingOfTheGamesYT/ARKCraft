package com.uberverse.arkcraft.common.event;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.util.I18n;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class VersionDetectionHandler
{
	public static void init()
	{
		VersionDetectionHandler handler = new VersionDetectionHandler();
		FMLCommonHandler.instance().bus().register(handler);
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event)
	{
		EntityPlayer player = event.player;
		if (player != null)
		{
			if (ARKCraft.instance().isDebugger())
			{
				player.addChatComponentMessage(
						new ChatComponentText(EnumChatFormatting.RED
								+ "You are running a decompiled version of ARKCraft!"));
			}
			if (ARKCraft.versionCheckResult != null
					&& ARKCraft.versionCheckResult.status == Status.OUTDATED
					|| ARKCraft.versionCheckResult.status == Status.BETA_OUTDATED)
			{
				player.addChatComponentMessage(
						new ChatComponentText(EnumChatFormatting.RED + I18n
								.translate("chat.notification.outdated")));
				player.addChatComponentMessage(
						new ChatComponentText(EnumChatFormatting.RED + I18n
								.format("chat.notification.outdatedversion",
										ARKCraft.instance().version())));
			}
		}
	}
}
