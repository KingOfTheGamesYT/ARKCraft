/**
 * 
 */
package com.uberverse.arkcraft.init;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.util.CollectionUtil;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author Lewis_McReu
 */
public class InitializationManager
{
	private static final InitializationManager instance = new InitializationManager();

	public static InitializationManager instance()
	{
		return instance;
	}

	private final Registry registry = new Registry();

	public Registry getRegistry()
	{
		return registry;
	}

	private static final int[] defaultMeta = new int[] { 0 };
	private static final String defaultPrefix = "";
	private static final boolean defaultRender = true;

	// Item register methods
	public <E extends Item> E registerItem(String name, E item, String... variants)
	{
		return this.registerItem(name, item, defaultPrefix, defaultMeta, variants);
	}

	public <E extends Item> E registerItem(String name, String modelLocationPrefix, E item, String... variants)
	{
		return this.registerItem(name, item, modelLocationPrefix, defaultMeta, variants);
	}

	public <E extends Item> E registerItem(String name, E item, int[] metas, String... variants)
	{
		return this.registerItem(name, item, defaultPrefix, metas, variants);
	}

	public <E extends Item> E registerItem(String name, E item, String modelLocationPrefix, int[] metas,
			String... variants)
	{
		return registerItem(name, item, modelLocationPrefix, metas, defaultRender, variants);
	}

	public <E extends Item> E registerItem(String name, E item, boolean standardRender, String... variants)
	{
		return this.registerItem(name, item, defaultPrefix, defaultMeta, standardRender, variants);
	}

	public <E extends Item> E registerItem(String name, String modelLocationPrefix, E item, boolean standardRender,
			String... variants)
	{
		return this.registerItem(name, item, modelLocationPrefix, defaultMeta, standardRender, variants);
	}

	public <E extends Item> E registerItem(String name, E item, int[] metas, boolean standardRender, String... variants)
	{
		return this.registerItem(name, item, defaultPrefix, metas, standardRender, variants);
	}

	public <E extends Item> E registerItem(String name, E item, String modelLocationPrefix, int[] metas,
			boolean standardRender, String... variants)
	{
		item.setUnlocalizedName(name);
		GameRegistry.registerItem(item, name);
		registry.addEntry(new RegistryEntry<E>(name, item, modelLocationPrefix, standardRender).addVariants(variants)
				.addMetas(CollectionUtil.convert(metas)));
		return item;
	}

	// Block register methods
	public <E extends Block> E registerBlock(String name, E block, String... variants)
	{
		return this.registerBlock(name, block, defaultPrefix, defaultMeta, variants);
	}

	public <E extends Block> E registerBlock(String name, String modelLocationPrefix, E block, String... variants)
	{
		return this.registerBlock(name, block, modelLocationPrefix, defaultMeta, variants);
	}

	public <E extends Block> E registerBlock(String name, E block, int[] metas, String... variants)
	{
		return this.registerBlock(name, block, defaultPrefix, metas, variants);
	}

	public <E extends Block> E registerBlock(String name, E block, String modelLocationPrefix, int[] metas,
			String... variants)
	{
		block.setUnlocalizedName(name);
		GameRegistry.registerBlock(block, name);
		registry.addEntry(new RegistryEntry<Item>(name, get(block), modelLocationPrefix).addMetas(CollectionUtil
				.convert(metas)).addVariants(variants));
		return block;
	}

	public <E extends Block> E registerBlock(String name, E block, Class<? extends ItemBlock> itemClass)
	{
		return this.registerBlock(name, block, itemClass, defaultMeta);
	}

	public <E extends Block> E registerBlock(String name, E block, Class<? extends ItemBlock> itemClass,
			String modelLocationPrefix)
	{
		return this.registerBlock(name, block, itemClass, modelLocationPrefix, defaultMeta);
	}

	public <E extends Block> E registerBlock(String name, E block, Class<? extends ItemBlock> itemClass, int[] metas)
	{
		return this.registerBlock(name, block, itemClass, "", metas);
	}

	public <E extends Block> E registerBlock(String name, E block, Class<? extends ItemBlock> itemClass,
			String modelLocationPrefix, int[] metas, String... variants)
	{
		block.setUnlocalizedName(name);
		GameRegistry.registerBlock(block, itemClass, name);
		registry.addEntry(new RegistryEntry<Item>(name, get(block), modelLocationPrefix).addMetas(CollectionUtil
				.convert(metas)).addVariants(variants));
		return block;
	}

	public <E extends Block> E registerBlock(String name, E block, Class<? extends ItemBlock> itemClass,
			Object[] itemCtorArgs)
	{
		return this.registerBlock(name, block, itemClass, itemCtorArgs, defaultMeta);
	}

	public <E extends Block> E registerBlock(String name, E block, Class<? extends ItemBlock> itemClass,
			Object[] itemCtorArgs, String modelLocationPrefix)
	{
		return this.registerBlock(name, block, itemClass, itemCtorArgs, modelLocationPrefix, defaultMeta);
	}

	public <E extends Block> E registerBlock(String name, E block, Class<? extends ItemBlock> itemClass,
			Object[] itemCtorArgs, int[] metas)
	{
		return this.registerBlock(name, block, itemClass, itemCtorArgs, "", metas);
	}

	public <E extends Block> E registerBlock(String name, E block, Class<? extends ItemBlock> itemClass,
			Object[] itemCtorArgs, String modelLocationPrefix, int[] metas, String... variants)
	{
		block.setUnlocalizedName(name);
		GameRegistry.registerBlock(block, itemClass, name, itemCtorArgs);
		this.registerItem(name, get(block), modelLocationPrefix, metas, variants);
		return block;
	}

	private Item get(Block block)
	{
		return Item.getItemFromBlock(block);
	}

	public class Registry
	{
		private final Collection<RegistryEntry<?>> entries;

		private Registry()
		{
			entries = Lists.newArrayList();
		}

		private void addEntry(RegistryEntry<?> entry)
		{
			entries.add(entry);
		}

		public void forEachEntry(Consumer<RegistryEntry<?>> consumer)
		{
			entries.forEach(consumer);
		}
	}

	public class RegistryEntry<E extends Item>
	{
		public final String name;
		public final Item content;
		public final String modelLocationPrefix;
		public final Collection<Integer> metas;
		public final Collection<String> variants;
		public final boolean standardRender;

		private RegistryEntry(String name, E content, String modelLocationPrefix, boolean standardRender)
		{
			super();
			this.name = name;
			this.content = content;
			this.modelLocationPrefix = modelLocationPrefix;
			this.metas = Lists.newArrayList();
			this.variants = Lists.newArrayList();
			this.standardRender = standardRender;
		}

		// TODO remove + fix with blocks
		private RegistryEntry(String name, E content, String modelLocationPrefix)
		{
			this(name, content, modelLocationPrefix, true);
		}

		private RegistryEntry<E> addMetas(Integer... metas)
		{
			Collections.addAll(this.metas, metas);
			return this;
		}

		private RegistryEntry<E> addVariants(String... variants)
		{
			Collections.addAll(this.variants, variants);
			return this;
		}

		public String[] getVariants()
		{
			return CollectionUtil.adapt(variants, (String v) -> {
				return ARKCraft.instance().modid() + ":" + modelLocationPrefix + v;
			}).toArray(new String[0]);
		}

		public void forEachMeta(Consumer<Integer> con)
		{
			for (int i : metas)
				con.accept(i);
		}
	}
}
