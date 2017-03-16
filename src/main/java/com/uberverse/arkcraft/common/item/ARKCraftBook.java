package com.uberverse.arkcraft.common.item;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.book.GuiInfoBook;
import com.uberverse.arkcraft.client.book.proxy.BookClient;
import com.uberverse.arkcraft.common.proxy.CommonProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ARKCraftBook extends Item
{

	public ARKCraftBook(String name)
	{
		super();
		this.setUnlocalizedName(name);
		this.setCreativeTab(ARKCraft.tabARK);
		this.maxStackSize = 1;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		if (worldIn.isRemote) openBook(itemStackIn, worldIn, playerIn);
		return itemStackIn;
	}

	@SideOnly(Side.CLIENT)
	public void openBook(ItemStack stack, World world, EntityPlayer player)
	{
		player.openGui(ARKCraft.instance(), CommonProxy.GUI.BOOK.id, world, 0, 0, 0);
		FMLClientHandler.instance().displayGuiScreen(player, new GuiInfoBook(stack, BookClient.bookInfo.bd));
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		list.add(ChatFormatting.GOLD + "Knowledge is Power");
	}

}
