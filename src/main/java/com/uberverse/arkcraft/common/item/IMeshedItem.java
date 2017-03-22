package com.uberverse.arkcraft.common.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IMeshedItem {
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack);
}
