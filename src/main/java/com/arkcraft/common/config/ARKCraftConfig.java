package com.arkcraft.common.config;

import com.arkcraft.ARKCraft;
import net.minecraftforge.common.config.Config;

import static net.minecraftforge.common.config.Config.*;

//TODO expand
@Config(modid = ARKCraft.MODID, type = Type.INSTANCE)
public class ARKCraftConfig {
	@Name("Game balancing")
	public static Balance BALANCE = new Balance();

	public static class Balance {
		@Comment({"Base number of seconds before an item decays.", "This value is altered by a multiplier for certain items."})
		@RangeInt(min = 1, max = 100000)
		public int decayRate = 2000;

		@Name("World generation")
		public Generation generation = new Generation();

		@Name("Plants")
		public Plants plants = new Plants();

		@Name("Player")
		public Player player = new Player();

		public static class Generation {
			@Comment("Number of ore veins that can spawn per chunk.")
			@RangeInt(min = 1, max = 10)
			public int maximumOreVeinSpawnPerChunk = 5;

			@Comment("Maximum number of ore blocks that an ore vein can contain.")
			@RangeInt(min = 2, max = 10)
			public int maximumOreBlocksPerVein = 7;
		}

		public static class Plants {
			@Comment("Minimum berries harvested from a bush per action")
			@RangeInt(min = 0, max = 5)
			public int minimumBerriesPerPicking = 1;

			@Comment("Maximum berries harvested from a bush per action")
			@RangeInt(min = 1, max = 10)
			public int maximumBerriesPerPicking = 5;
		}

		public static class Player {
			@Comment("Ticks between defecation")
			@RangeInt(min = 600, max = 3000)
			public int ticksBetweenDefecation = 1500;
		}
	}
}
