package com.uberverse.arkcraft.common.event;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.util.I18n;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
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
				player.sendMessage(new ITextComponent(ChatFormatting.RED
						+ "You are running a decompiled version of ARKCraft!"));
			}
			else if (ARKCraft.versionCheckResult != null && ARKCraft.versionCheckResult.status == Status.OUTDATED
					|| ARKCraft.versionCheckResult.status == Status.BETA_OUTDATED)
			{
				player.sendMessage(new ITextComponent(ChatFormatting.RED + I18n.translate(
						"chat.notification.outdated")));
				player.sendMessage(new ITextComponent(ChatFormatting.RED + I18n.format(
						"chat.notification.outdatedversion", ARKCraft.instance().version())));
			}
			else if (ARKCraft.versionCheckResult == null)
			{
				player.sendMessage(new ITextComponent(ChatFormatting.RED + "No Internet access"));
			}
		}

	}
}
