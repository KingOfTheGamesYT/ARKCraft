package com.arkcraft.common.advancement;

import com.arkcraft.ARKCraft;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

public class RightClickTrigger implements ICriterionTrigger {
	private static final ResourceLocation ID = new ResourceLocation(ARKCraft.MODID, "right_click");

	private final Map<PlayerAdvancements, RightClickTrigger.Listeners> listeners = new HashMap();

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
		Block block = null;
		if (json.has("block")) {
			ResourceLocation resourceLocation = new ResourceLocation(JsonUtils.getString(json, "block"));
			if (!Block.REGISTRY.containsKey(resourceLocation)) {
				throw new JsonSyntaxException("Unknown block type '" + resourceLocation + "'");
			}
		}
		ItemPredicate itemPredicate = null;
		if (json.has("item")) {
			itemPredicate = ItemPredicate.deserialize(json.get("item"));
		}
		return new Instance(itemPredicate, block);
	}

	public static class Instance extends AbstractCriterionInstance {
		private final ItemPredicate item;
		private final Block block;

		public Instance(@Nullable ItemPredicate item, @Nullable Block block) {
			super(RightClickTrigger.ID);

			this.item = item;
			this.block = block;
		}

		public boolean test(ItemStack itemStack, Block block) {
			boolean out = true;
			if (this.item != null) {
				out = this.item.test(itemStack);
			}
			if (this.block != null) {
				out = out && this.block == block;
			}
			return out;
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

		public void add(ICriterionTrigger.Listener<Instance> listener) {
			this.listeners.add(listener);
		}

		public void remove(ICriterionTrigger.Listener<Instance> listener) {
			this.listeners.remove(listener);
		}

		public void trigger(ItemStack itemStack, Block block) {
			List<Listener<Instance>> list = null;
			for (Listener<Instance> listener : this.listeners) {
				if (listener.getCriterionInstance().test(itemStack, block)) {
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