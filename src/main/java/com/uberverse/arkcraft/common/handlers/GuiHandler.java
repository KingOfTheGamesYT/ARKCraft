package com.uberverse.arkcraft.common.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.gui.GUIAttachment;
import com.uberverse.arkcraft.common.container.ContainerInventoryAttachment;
import com.uberverse.arkcraft.common.container.inventory.InventoryAttachment;
import com.uberverse.lib.LogHelper;

public class GuiHandler implements IGuiHandler
{
//	public static EntityARKCreature rightClickedEntity;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (world.isRemote)
		{
			LogHelper
					.info("GuiHandler: getServerGuiElement called from client");
		}
		else
		{
			LogHelper
					.info("GuiHandler: getServerGuiElement called from server");
		}
		if (ID == ARKCraft.GUI.ATTACHMENT_GUI.getID())
		{
			return new ContainerInventoryAttachment(player, player.inventory,
					InventoryAttachment.create(player.getHeldItem()));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (world.isRemote)
		{
			LogHelper
					.info("GuiHandler: getClientGuiElement called from client");
		}
		else
		{
			LogHelper
					.info("GuiHandler: getClientGuiElement called from server");
		}
		if (ID == ARKCraft.GUI.ATTACHMENT_GUI.getID())
		{
			return new GUIAttachment(player, player.inventory,
					InventoryAttachment.create(player.getHeldItem()));
		}
		return null;
	}

	/*
	private static BookData getBookDataFromStack(ItemStack stack)
	{
		return BookDataStore.getBookFromName(stack.getUnlocalizedName());
	}	*/

}
