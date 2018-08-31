package com.arkcraft.common.advancement;

import com.arkcraft.ARKCraft;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class BigBangTrigger implements ICriterionTrigger {
	private static final ResourceLocation ID = new ResourceLocation(ARKCraft.MODID, "right_click");

	private final Map<PlayerAdvancements, BigBangTrigger.Listeners> listeners = new HashMap();

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn, Listener listener) {
		Listeners listeners = this.listeners.get(playerAdvancementsIn);

		if (listeners == null) {
			listeners = new Listeners(playerAdvancementsIn);
			this.listeners.put(playerAdvancementsIn, listeners);
		}
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener listener) {
		Listeners listeners = this.listeners.get(playerAdvancementsIn);
		if (listeners != null) {
			listeners.remove(listener);
			if (listeners.isEmpty()) {
				this.listeners.remove(playerAdvancementsIn);
			}
		}
	}

	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
		this.listeners.remove(playerAdvancementsIn);
	}

	@Override
	public ICriterionInstance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		return new Instance();
	}

	public void trigger(EntityPlayerMP player) {
		Listeners listeners = this.listeners.get(player.getAdvancements());
		if (listeners != null) {
			listeners.trigger();
		}
	}

	public static class Instance extends AbstractCriterionInstance {
		public Instance() {
			super(BigBangTrigger.ID);
		}

		public boolean test() {
			return true;
		}
	}

	static class Listeners {
		private final PlayerAdvancements playerAdvancements;
		private final Set<Listener<Instance>> listeners = new HashSet<>();

		public Listeners(PlayerAdvancements playerAdvancements) {
			this.playerAdvancements = playerAdvancements;
		}

		public boolean isEmpty() {
			return this.listeners.isEmpty();
		}

		public void add(Listener<Instance> listener) {
			this.listeners.add(listener);
		}

		public void remove(Listener<Instance> listener) {
			this.listeners.remove(listener);
		}

		public void trigger() {
			List<Listener<Instance>> list = null;
			for (Listener<Instance> listener : this.listeners) {
				if (listener.getCriterionInstance().test()) {
					if (list == null) {
						list = new ArrayList<>();
					}
					list.add(listener);
				}
			}
			if (list != null) {
				for (Listener<Instance> listener : list) {
					listener.grantCriterion(this.playerAdvancements);
				}
			}
		}
	}
}