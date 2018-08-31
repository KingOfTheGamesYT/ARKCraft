package com.arkcraft.common.arkplayer.event;

import com.arkcraft.common.arkplayer.ARKPlayer;
import com.arkcraft.common.config.WeightsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class PlayerCommonEventHandler {
	public static final ResourceLocation CAP = new ResourceLocation("arkcraft:arkplayer");

	public static void init() {
		PlayerCommonEventHandler p = new PlayerCommonEventHandler();
		MinecraftForge.EVENT_BUS.register(p);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		// Update ARKPlayer
		if (event.phase.equals(Phase.END)) {
			ARKPlayer.get(event.player).update();
		}
	}

	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) event.getEntityLiving();
			if (WeightsConfig.isEnabled && (!p.capabilities.isCreativeMode || WeightsConfig.allowInCreative) && ARKPlayer.get(p).isEncumbered()) {
				p.motionY *= 0;
				if (p.world.isRemote)
					p.sendMessage(new TextComponentTranslation("ark.splash.noJump"));

			}
		}
	}

	// TODO add events for inventory changes --> recalculate weight
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDestroyItem(PlayerDestroyItemEvent event) {
		updateWeight(event.getEntityPlayer());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onTossItem(ItemTossEvent event) {
		updateWeight(event.getPlayer());
	}

	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event) {
		updateWeight(event.getEntityPlayer());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onArrowLoose(ArrowLooseEvent event) {
		updateWeight(event.getEntityPlayer());
	}

	private void updateWeight(EntityPlayer player) {
		ARKPlayer.get(player).updateWeight();
	}

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<EntityPlayer> event) {
		event.addCapability(CAP, new ARKPlayer(event.getObject()));
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();
		ARKPlayer p = player.getCapability(ARKPlayer.ARK_PLAYER_CAPABILITY, null);
		ARKPlayer old = player.getCapability(ARKPlayer.ARK_PLAYER_CAPABILITY, null);
		NBTTagCompound tag = new NBTTagCompound();
		old.saveNBTData(tag);
		p.loadNBTData(tag);
	}
}
