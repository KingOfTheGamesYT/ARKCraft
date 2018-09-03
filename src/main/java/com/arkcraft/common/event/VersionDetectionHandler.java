package com.arkcraft.common.event;

import com.arkcraft.ARKCraft;
import com.arkcraft.util.I18n;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@Mod.EventBusSubscriber(modid = ARKCraft.MODID)
public class VersionDetectionHandler {
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (player != null) {
			if (ARKCraft.versionCheckResult == null) {
				player.sendMessage(new TextComponentString(ChatFormatting.RED + "ARKCraft - Couldn't check version"));
			} else if (ARKCraft.versionCheckResult != null && ARKCraft.versionCheckResult.status == Status.OUTDATED || ARKCraft.versionCheckResult.status == Status.BETA_OUTDATED) {
				player.sendMessage(new TextComponentString(ChatFormatting.RED + I18n.translate("chat.notification.outdated")));
				player.sendMessage(new TextComponentString(ChatFormatting.RED + I18n.format("chat.notification.outdatedversion", ARKCraft.instance().version())));
			}
		}
	}
}
