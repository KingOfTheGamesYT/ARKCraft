package com.arkcraft.client.gui.engram;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.engram.CraftingOrder;
import com.arkcraft.common.engram.EngramManager;
import com.arkcraft.common.engram.IEngramCrafter;
import com.arkcraft.common.item.Qualitable;
import com.arkcraft.util.AbstractItemStack;
import com.arkcraft.util.I18n;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.arkcraft.client.gui.component.GuiTexturedButton;
import com.arkcraft.client.gui.scrollable.GUIScrollable;
import com.arkcraft.common.container.engram.ContainerEngramCrafting;
import com.arkcraft.common.container.engram.ContainerEngramCrafting.BlueprintSlot;
import com.arkcraft.common.container.engram.ContainerEngramCrafting.EngramCraftingSlot;
import com.arkcraft.common.container.engram.ContainerEngramCrafting.EngramSlot;
import com.arkcraft.common.container.engram.ContainerEngramCrafting.QueueSlot;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public abstract class GUIEngramCrafting extends GUIScrollable
{
	public static final ResourceLocation buttons = new ResourceLocation(ARKCraft.MODID, "textures/gui/buttons.png");

	protected int buttonCounter = 0;
	private GuiButton craft, craftall;

	public GUIEngramCrafting(ContainerEngramCrafting container)
	{
		super(container);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		craft = new GuiTexturedButton(buttonCounter++, guiLeft + getC1ButtonX(), guiTop + getC1ButtonY(),
				getC1ButtonWidth(), getC1ButtonHeight(), getC1ButtonResource(), getC1ButtonU(), getC1ButtonV());
		craftall = new GuiTexturedButton(buttonCounter++, guiLeft + getCAButtonX(), guiTop + getCAButtonY(),
				getCAButtonWidth(), getCAButtonHeight(), getCAButtonResource(), getCAButtonU(), getCAButtonV());
		this.buttonList.add(craft);
		this.buttonList.add(craftall);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button == craft)
		{
			((ContainerEngramCrafting) inventorySlots).craftOne();
			mc.playerController.sendEnchantPacket(inventorySlots.windowId, 0);
		}
		else if (button == craftall)
		{
			((ContainerEngramCrafting) inventorySlots).craftAll();
			mc.playerController.sendEnchantPacket(inventorySlots.windowId, 1);
		}
		else super.actionPerformed(button);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		ContainerEngramCrafting c = (ContainerEngramCrafting) inventorySlots;
		for (Object o : inventorySlots.inventorySlots)
		{
			if (o instanceof QueueSlot)
			{
				QueueSlot e = (QueueSlot) o;
				IEngramCrafter ec = c.getCrafter();
				if (ec.isCrafting())
				{
					CraftingOrder co = e.getCraftingOrder();
					if (ec.getCraftingQueue().peek() != null && ec.getCraftingQueue().peek() == co)
					{
						double fraction = ec.getRelativeProgress();
						if (Double.isNaN(fraction)) fraction = 1;
						fraction = MathHelper.clamp(fraction, 0, 1);
						if (fraction == 0) { return; }
						int x = e.xPos;
						int y = e.yPos + 16;

						int red = (int) (255 * (1 - fraction));
						int green = (int) (255 * fraction);
						int blue = 0;
						int alpha = 96;

						int color = new Color(red, green, blue, alpha).getRGB();

						drawRect(x, (int) (y - (fraction * 16)), x + 16, y, color);
						GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
					}
				}
			}
			else if (o instanceof EngramSlot)
			{
				EngramSlot e = (EngramSlot) o;
				if (c.getSelectedBlueprintIndex() < 0 && c.getSelectedEngram() == e.getEngram() && e.getSlotIndex() < c
						.getTotalSlotsAmount()) drawSelection(e);
			}
			else if (o instanceof BlueprintSlot)
			{
				BlueprintSlot b = (BlueprintSlot) o;
				if (c.getSelectedBlueprintIndex() == b.slotNumber && b.hasBlueprint()) drawSelection(b);
			}
		}

		// Item Rendering! TODO
		// GlStateManager.enableDepth();
		// GlStateManager.scale(0.5, 0.5, 0.5);
		// ItemStack itemstack = new ItemStack(ARKCraftItems.amarBerry);
		// this.itemRender.renderItemAndEffectIntoGUI(itemstack, 100,100);
		// this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj,
		// itemstack, 100, 100, "64");
		// GlStateManager.scale(2, 2, 2);
	}

	private void drawSelection(Slot s)
	{
		int x = s.xPos;
		int y = s.yPos;
		drawRect(x - 1, y - 1, x + 17, y + 17, new Color(0, 128, 255, 255).getRGB());
		GlStateManager.color(1, 1, 1, 1);
	}

	private EngramManager.Engram tooltipped;
	private List<EngramManager.EngramRecipe> recipes = new ArrayList<>();
	private int shown;
	private int ticker;

	@SuppressWarnings("unchecked")
	@Override
	protected void drawHoveringText(@SuppressWarnings("rawtypes") List textLines, int x, int y,
			FontRenderer fontRendererObj)
	{
		Slot slot = getSlotUnderMouse();
		if (slot instanceof EngramCraftingSlot)
		{
			EngramCraftingSlot e = (EngramCraftingSlot) slot;
			if (e.getEngram() != null)
			{
				if (recipes.size() > 1)
				{
					ticker++;
				}
				if (tooltipped != e.getEngram())
				{
					tooltipped = e.getEngram();
					recipes.clear();
					recipes.addAll(tooltipped.getRecipes());
					shown = 0;
					ticker = 0;
				}

				if (ticker == 80)
				{
					ticker = 0;
					shown = (shown + 1 == recipes.size()) ? 0 : shown + 1;
				}
				textLines.clear();

				Qualitable.ItemQuality out = Qualitable.ItemQuality.PRIMITIVE;
				double multiplier = 1;
				if (tooltipped.isQualitable() && slot instanceof BlueprintSlot)
				{
					BlueprintSlot b = (BlueprintSlot) slot;
					out = b.getItemQuality();
					multiplier = b.getItemQuality().resourceMultiplier;
				}

				ItemStack output = tooltipped.getOutputAsItemStack(out);
				textLines.add(output.getItem().getItemStackDisplayName(output) + (output.stackSize > 1 ? " x "
						+ output.stackSize : ""));
				EngramManager.EngramRecipe er = recipes.get(shown);

				for (AbstractItemStack i : er.getItems())
				{
					textLines.add(ChatFormatting.GOLD + I18n.format("gui.engramcrafting.engram.tooltip.ingredient",
							I18n.translate(i.item.getUnlocalizedName() + ".name"), (int) (i.getAmount() * multiplier)));
				}
			}
		}
		// TODO queue slot stuff

		super.drawHoveringText(textLines, x, y, fontRendererObj);
	}

	public abstract int getC1ButtonX();

	public abstract int getC1ButtonY();

	public int getC1ButtonU()
	{
		return 0;
	}

	public int getC1ButtonV()
	{
		return 42;
	}

	public int getC1ButtonWidth()
	{
		return 32;
	}

	public int getC1ButtonHeight()
	{
		return 14;
	}

	public ResourceLocation getC1ButtonResource()
	{
		return buttons;
	}

	public abstract int getCAButtonX();

	public abstract int getCAButtonY();

	public int getCAButtonU()
	{
		return 0;
	}

	public int getCAButtonV()
	{
		return 0;
	}

	public int getCAButtonWidth()
	{
		return 47;
	}

	public int getCAButtonHeight()
	{
		return 14;
	}

	public ResourceLocation getCAButtonResource()
	{
		return buttons;
	}

}
