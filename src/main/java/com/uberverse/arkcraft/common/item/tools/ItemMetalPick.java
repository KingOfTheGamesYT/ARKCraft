package com.uberverse.arkcraft.common.item.tools;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class ItemMetalPick extends ARKCraftPickaxe
{
	private static final Set EFFECTIVE_ON = Sets.newHashSet(new Block[] {
			Blocks.activator_rail, Blocks.coal_ore, Blocks.cobblestone,
			Blocks.detector_rail, Blocks.diamond_block, Blocks.diamond_ore,
			Blocks.double_stone_slab, Blocks.golden_rail, Blocks.gold_block,
			Blocks.gold_ore, Blocks.ice, Blocks.iron_block, Blocks.iron_ore,
			Blocks.lapis_block, Blocks.lapis_ore, Blocks.lit_redstone_ore,
			Blocks.mossy_cobblestone, Blocks.netherrack, Blocks.packed_ice,
			Blocks.rail, Blocks.redstone_ore, Blocks.sandstone,
			Blocks.red_sandstone, Blocks.stone, Blocks.stone_slab });

	public ItemMetalPick(ToolMaterial material)
	{

		super(5, material, EFFECTIVE_ON);
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of("pickaxe", "axe");
	}

	@Override
	public float getStrVsBlock(ItemStack stack, Block block)
	{

		return block.getMaterial() != Material.iron
				&& block.getMaterial() != Material.anvil
				&& block.getMaterial() != Material.rock
						? super.getStrVsBlock(stack, block)
						: this.efficiencyOnProperMaterial;

	}

	public boolean isArkTool()
	{
		return true;
	}
}
