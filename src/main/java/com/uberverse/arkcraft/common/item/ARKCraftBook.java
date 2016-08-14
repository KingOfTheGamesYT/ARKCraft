package com.uberverse.arkcraft.common.item;

import java.util.List;

import com.uberverse.arkcraft.ARKCraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ARKCraftBook extends Item {

	public ARKCraftBook(String name) {
		super();
		this.setUnlocalizedName(name);
		this.setCreativeTab(ARKCraft.tabARK);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer player) {
		//TODO do things
		return stack;
	}
	
	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(EnumChatFormatting.GOLD + "Knowledge is Power");
	}
	
}
