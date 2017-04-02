package com.uberverse.arkcraft.common.handlers;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.network.IGuiHandler;

import com.uberverse.arkcraft.client.book.GuiInfoBook;
import com.uberverse.arkcraft.client.book.core.BookData;
import com.uberverse.arkcraft.client.book.core.BookDataStore;
import com.uberverse.arkcraft.client.gui.block.GUICampfire;
import com.uberverse.arkcraft.client.gui.block.GUICompostBin;
import com.uberverse.arkcraft.client.gui.block.GUIMortarPestle;
import com.uberverse.arkcraft.client.gui.block.GUIRefiningForge;
import com.uberverse.arkcraft.client.gui.block.GUISmithy;
import com.uberverse.arkcraft.client.gui.block.GuiCropPlotNew;
import com.uberverse.arkcraft.client.gui.entity.GuiInventoryDodo;
import com.uberverse.arkcraft.client.gui.item.GUIAttachment;
import com.uberverse.arkcraft.client.gui.player.GUIEngram;
import com.uberverse.arkcraft.client.gui.player.GUIPlayerCrafting;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.common.container.block.ContainerCampfire;
import com.uberverse.arkcraft.common.container.block.ContainerCompostBin;
import com.uberverse.arkcraft.common.container.block.ContainerCropPlot;
import com.uberverse.arkcraft.common.container.block.ContainerMP;
import com.uberverse.arkcraft.common.container.block.ContainerRefiningForge;
import com.uberverse.arkcraft.common.container.block.ContainerSmithy;
import com.uberverse.arkcraft.common.container.entity.ContainerInventoryDodo;
import com.uberverse.arkcraft.common.container.item.ContainerInventoryAttachment;
import com.uberverse.arkcraft.common.container.player.ContainerEngram;
import com.uberverse.arkcraft.common.container.player.ContainerPlayerCrafting;
import com.uberverse.arkcraft.common.entity.EntityDodo;
import com.uberverse.arkcraft.common.inventory.InventoryAttachment;
import com.uberverse.arkcraft.common.proxy.CommonProxy;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCompostBin;
import com.uberverse.arkcraft.common.tileentity.crafter.TileEntityCropPlot;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityCampfire;
import com.uberverse.arkcraft.common.tileentity.crafter.burner.TileEntityRefiningForge;
import com.uberverse.arkcraft.common.tileentity.crafter.engram.TileEntityMP;
import com.uberverse.arkcraft.common.tileentity.crafter.engram.TileEntitySmithy;
import com.uberverse.lib.LogHelper;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == CommonProxy.GUI.SMITHY.id) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntitySmithy)
				return new ContainerSmithy(player, (TileEntitySmithy) tileEntity);
		}
		else if (id == CommonProxy.GUI.MORTAR_AND_PESTLE.id) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntityMP)
				return new ContainerMP(player, (TileEntityMP) tileEntity);
		}
		else if (id == CommonProxy.GUI.COMPOST_BIN.id) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntityCompostBin)
				return new ContainerCompostBin(player, (TileEntityCompostBin) tileEntity);
		}
		else if (id == CommonProxy.GUI.REFINING_FORGE.id) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntityRefiningForge)
				return new ContainerRefiningForge((TileEntityRefiningForge) tileEntity, player);
		}
		else if (id == CommonProxy.GUI.CAMPFIRE.id) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntityCampfire)
				return new ContainerCampfire((TileEntityCampfire) tileEntity, player);
		}
		else if (id == CommonProxy.GUI.ENGRAMS.id)
			return new ContainerEngram(ARKPlayer.get(player).getEngramInventory());
		else if (id == CommonProxy.GUI.CROP_PLOT.id) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntityCropPlot)
				return new ContainerCropPlot(player.inventory, (TileEntityCropPlot) tileEntity);
		}
		else if (id == CommonProxy.GUI.PLAYER.id)
			return new ContainerPlayerCrafting(player);
		else if (id == CommonProxy.GUI.ATTACHMENTS.id)
			return new ContainerInventoryAttachment(player, player.inventory, InventoryAttachment.create(player.getHeldItemMainhand()));

		else if (id == CommonProxy.GUI.INV_DODO.id) {
			Entity entity = getEntityAt(player, x, y, z);
			if (entity != null && entity instanceof EntityDodo)
				return new ContainerInventoryDodo(player.inventory, ((EntityDodo) entity).invDodo, (EntityDodo) entity);
		}else if(id == CommonProxy.GUI.FABRICATOR.id){
			//TODO
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == CommonProxy.GUI.SMITHY.id) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntitySmithy)
				return new GUISmithy(player, (TileEntitySmithy) tileEntity);
		}
		else if (id == CommonProxy.GUI.MORTAR_AND_PESTLE.id) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntityMP)
				return new GUIMortarPestle(player, (TileEntityMP) tileEntity);
		}
		else if (id == CommonProxy.GUI.CAMPFIRE.id) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntityCampfire)
				return new GUICampfire((ContainerCampfire) getServerGuiElement(id, player, world, x, y, z));
		}
		else if (id == CommonProxy.GUI.CROP_PLOT.id) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntityCropPlot)
				return new GuiCropPlotNew(player.inventory, (TileEntityCropPlot) tileEntity);
		}
		else if (id == CommonProxy.GUI.REFINING_FORGE.id) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileEntityRefiningForge)
				return new GUIRefiningForge(new ContainerRefiningForge((TileEntityRefiningForge) tileEntity, player));
		}
		else if (id == CommonProxy.GUI.ENGRAMS.id)
			return new GUIEngram(player);
		else if (id == CommonProxy.GUI.COMPOST_BIN.id) {
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileEntityCompostBin)
				return new GUICompostBin(player, (TileEntityCompostBin) tileEntity);
		}
		else if (id == CommonProxy.GUI.BOOK.id) {
			ItemStack stack = player.getHeldItemMainhand();
			return new GuiInfoBook(stack, GuiHandler.getBookDataFromStack(stack));
		}
		else if (id == CommonProxy.GUI.PLAYER.id)
			return new GUIPlayerCrafting(new ContainerPlayerCrafting(player));
		else if (id == CommonProxy.GUI.ATTACHMENTS.id)
			return new GUIAttachment(player, player.inventory, InventoryAttachment.create(player.getHeldItemMainhand()));
		else if (id == CommonProxy.GUI.INV_DODO.id) {
			Entity entity = getEntityAt(player, x, y, z);
			if (entity != null && entity instanceof EntityDodo)
				return new GuiInventoryDodo(player.inventory, ((EntityDodo) entity).invDodo, (EntityDodo) entity);
		}else if(id == CommonProxy.GUI.FABRICATOR.id){
			//TODO
		}
		return null;

	}

	private static BookData getBookDataFromStack(ItemStack stack)
	{
		return BookDataStore.getBookDataFromName(stack.getUnlocalizedName());
	}

	private Entity getEntityAt(EntityPlayer player, int x, int y, int z)
	{
		AxisAlignedBB targetBox = new AxisAlignedBB(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
		@SuppressWarnings("rawtypes")
		List entities = player.world.getEntitiesWithinAABBExcludingEntity(player, targetBox);
		@SuppressWarnings("rawtypes")
		Iterator iterator = entities.iterator();
		while (iterator.hasNext()) {
			Entity entity = (Entity) iterator.next();
			// TODO change to umbrella all the entities
			if (entity instanceof EntityDodo) {
				LogHelper.info("GuiHandler: Found entity!");
				return entity;
			}
		}
		return null;
	}
}
