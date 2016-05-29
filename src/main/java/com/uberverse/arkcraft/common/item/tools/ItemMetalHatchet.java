package com.uberverse.arkcraft.common.item.tools;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableSet;

public class ItemMetalHatchet extends ItemAxe
{

	public ItemMetalHatchet(ToolMaterial material)
    {

        super(material);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack)
    {
        return ImmutableSet.of("pickaxe", "axe");
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block)
    {

        return block.getMaterial() != Material.wood && block.getMaterial() != Material.vine && block.getMaterial() != Material.plants ?
                super.getStrVsBlock(stack, block) : this.efficiencyOnProperMaterial;
    }

    public boolean isArkTool()
    {
        return true;
    }

}
