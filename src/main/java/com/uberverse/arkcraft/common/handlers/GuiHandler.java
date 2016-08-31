package com.uberverse.arkcraft.common.handlers;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.client.book.GuiInfoBook;
import com.uberverse.arkcraft.client.book.core.BookData;
import com.uberverse.arkcraft.client.book.core.BookDataStore;
import com.uberverse.arkcraft.client.gui.GUIAttachment;
import com.uberverse.arkcraft.client.gui.GUICampfire;
import com.uberverse.arkcraft.client.gui.GUICompostBin;
import com.uberverse.arkcraft.client.gui.GUIEngram;
import com.uberverse.arkcraft.client.gui.GUIForge;
import com.uberverse.arkcraft.client.gui.GUIMortarPestle;
import com.uberverse.arkcraft.client.gui.GuiCropPlotNew;
import com.uberverse.arkcraft.common.block.container.ContainerCropPlotNew;
import com.uberverse.arkcraft.common.block.container.ContainerEngram;
import com.uberverse.arkcraft.common.block.container.ContainerInventoryCampfire;
import com.uberverse.arkcraft.common.block.container.ContainerInventoryCompostBin;
import com.uberverse.arkcraft.common.block.container.ContainerInventoryForge;
import com.uberverse.arkcraft.common.block.container.ContainerInventoryMP;
import com.uberverse.arkcraft.common.block.tile.TileEntityCropPlotNew;
import com.uberverse.arkcraft.common.block.tile.TileInventoryCampfire;
import com.uberverse.arkcraft.common.block.tile.TileInventoryCompostBin;
import com.uberverse.arkcraft.common.block.tile.TileInventoryForge;
import com.uberverse.arkcraft.common.block.tile.TileInventoryMP;
import com.uberverse.arkcraft.common.container.ContainerInventoryAttachment;
import com.uberverse.arkcraft.common.container.inventory.InventoryAttachment;
import com.uberverse.arkcraft.old.ARKPlayer;
import com.uberverse.arkcraft.rework.container.ContainerPlayerCrafting;
import com.uberverse.arkcraft.rework.container.ContainerSmithy;
import com.uberverse.arkcraft.rework.gui.GUIPlayerCrafting;
import com.uberverse.arkcraft.rework.gui.GUISmithy;
import com.uberverse.arkcraft.rework.tileentity.TileEntitySmithy;
import com.uberverse.lib.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (world.isRemote)
		{
			LogHelper.info("GuiHandler: getServerGuiElement called from client");
		}
		else
		{
			LogHelper.info("GuiHandler: getServerGuiElement called from server");
		}

		if (id == ARKCraft.GUI.SMITHY.getID())
		{
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntitySmithy)
			{
				return new ContainerSmithy(player, (TileEntitySmithy) tileEntity);
			}
			else
			{
				LogHelper.info("GuiHandler - getServerGuiElement: TileEntitySmithy not found!");
			}
		}
		else if (id == ARKCraft.GUI.PESTLE_AND_MORTAR.getID())
		{
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryMP)
			{
				return new ContainerInventoryMP(player.inventory, (TileInventoryMP) tileEntity);
			}
			else
			{
				LogHelper.info("GuiHandler - getServerGuiElement: TileEntityMP not found!");
			}
		}
		else if (id == ARKCraft.GUI.COMPOST_BIN.getID())
		{
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryCompostBin)
			{
				return new ContainerInventoryCompostBin(player.inventory, (TileInventoryCompostBin) tileEntity);
			}
			else
			{
				LogHelper.info("GuiHandler - getServerGuiElement: TileEntityCompostBin not found!");
			}
		}
		else if (id == ARKCraft.GUI.FORGE_GUI.getID())
		{
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryForge)
			{
				return new ContainerInventoryForge(player.inventory, (TileInventoryForge) tileEntity);
			}
			else
			{
				LogHelper.info("GuiHandler - getServerGuiElement: TileEntityForge not found!");
			}
		}
		else if (id == ARKCraft.GUI.CAMPFIRE_GUI.getID())
		{
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryCampfire)
			{
				return new ContainerInventoryCampfire(player.inventory, (TileInventoryCampfire) tileEntity);
			}
			else
			{
				LogHelper.info("GuiHandler - getServerGuiElement: TileEntityCampfire not found!");
			}
		}
		else if (id == ARKCraft.GUI.ENGRAM_GUI.getID()) return new ContainerEngram(ARKPlayer.get(player).getEngramInventory(), player);
		else if (id == ARKCraft.GUI.CROP_PLOT.getID())
		{
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntityCropPlotNew)
			{
				return new ContainerCropPlotNew(player.inventory, (TileEntityCropPlotNew) tileEntity);
			}
			else
			{
				LogHelper.info("GuiHandler - getServerGuiElement: TileEntityCropPlotNew not found!");
			}
		}
		else if (id == ARKCraft.GUI.PLAYER.getID())
		{
			return new ContainerPlayerCrafting(player);
		}
		else if (id == ARKCraft.GUI.ATTACHMENT_GUI.getID())
			return new ContainerInventoryAttachment(player, player.inventory, InventoryAttachment.create(player.getHeldItem()));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (world.isRemote)
		{
			LogHelper.info("GuiHandler: getClientGuiElement called from client");
		}
		else
		{
			LogHelper.info("GuiHandler: getClientGuiElement called from server");
		}
		if (ID == ARKCraft.GUI.SMITHY.getID())
		{
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntitySmithy)
			{
				return new GUISmithy(player, (TileEntitySmithy) tileEntity);
			}
			else
			{
				LogHelper.info("GuiHandler - getClientGuiElement: TileEntitySmithy not found!");
			}
		}
		else if (ID == ARKCraft.GUI.PESTLE_AND_MORTAR.getID())
		{
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryMP)
			{
				return new GUIMortarPestle(player.inventory, (TileInventoryMP) tileEntity);
			}
			else
			{
				LogHelper.info("GuiHandler - getClientGuiElement: TileEntityMP not found!");
			}
		}
		else if (ID == ARKCraft.GUI.CAMPFIRE_GUI.getID())
		{
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryCampfire)
			{
				return new GUICampfire(player.inventory, (TileInventoryCampfire) tileEntity);
			}
			else
			{
				LogHelper.info("GuiHandler - getClientGuiElement: TileEntityCampfire not found!");
			}
		}
		else if (ID == ARKCraft.GUI.CROP_PLOT.getID())
		{
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntityCropPlotNew)
			{
				return new GuiCropPlotNew(player.inventory, (TileEntityCropPlotNew) tileEntity);
			}
			else
			{
				LogHelper.info("GuiHandler - getClientGuiElement: TileEntityCropPlotNew not found!");
			}
		}
		else if (ID == ARKCraft.GUI.FORGE_GUI.getID())
		{
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryForge)
			{
				return new GUIForge(player.inventory, (TileInventoryForge) tileEntity);
			}
			else
			{
				LogHelper.info("GuiHandler - getClientGuiElement: TileEntityForge not found!");
			}
		}
		else if (ID == ARKCraft.GUI.ENGRAM_GUI.getID())
		{
			return new GUIEngram(player);
		}
		else if (ID == ARKCraft.GUI.COMPOST_BIN.getID())
		{
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryCompostBin)
			{
				return new GUICompostBin(player.inventory, (TileInventoryCompostBin) tileEntity);
			}
			else
			{
				LogHelper.info("GuiHandler - getClientGuiElement: TileEntityCompostBin not found!");
			}
		}
		else if (ID == ARKCraft.GUI.BOOK_GUI.getID())
		{
			ItemStack stack = player.getCurrentEquippedItem();
			return new GuiInfoBook(stack, GuiHandler.getBookDataFromStack(stack));
		}
		else if (ID == ARKCraft.GUI.PLAYER.getID())
		{
			return new GUIPlayerCrafting(new ContainerPlayerCrafting(player));
		}
		else if (ID == ARKCraft.GUI.ATTACHMENT_GUI
				.getID()) { return new GUIAttachment(player, player.inventory, InventoryAttachment.create(player.getHeldItem())); }
		return null;
	}

	private static BookData getBookDataFromStack(ItemStack stack)
	{
		return BookDataStore.getBookDataFromName(stack.getUnlocalizedName());
	}
}
