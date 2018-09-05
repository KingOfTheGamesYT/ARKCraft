package com.arkcraft.common.item;

import com.arkcraft.ARKCraft;
import com.arkcraft.client.book.GuiInfoBook;
import com.arkcraft.client.book.proxy.BookClient;
import com.arkcraft.common.proxy.CommonProxy;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ARKCraftBook extends Item {

	public ARKCraftBook() {
		super();
		this.setCreativeTab(ARKCraft.tabARK);
		this.maxStackSize = 1;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if (worldIn.isRemote) openBook(itemStackIn, worldIn, playerIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@SideOnly(Side.CLIENT)
	public void openBook(ItemStack stack, World world, EntityPlayer player) {
		player.openGui(ARKCraft.instance(), CommonProxy.GUI.BOOK.id, world, 0, 0, 0);
		FMLClientHandler.instance().displayGuiScreen(player, new GuiInfoBook(stack, BookClient.bookInfo.bd));
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(ChatFormatting.GOLD + "Knowledge is Power");
	}
}
