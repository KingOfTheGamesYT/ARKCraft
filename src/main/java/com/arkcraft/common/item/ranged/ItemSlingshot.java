package com.arkcraft.common.item.ranged;

import com.arkcraft.common.item.IMeshedItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.ARKCraft;
import com.arkcraft.common.entity.projectile.EntityStone;

public class ItemSlingshot extends Item implements IMeshedItem
{

	public ItemSlingshot()
	{
		super();
		ARKCraft.proxy.registerModelMeshDef(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		if (playerIn.capabilities.isCreativeMode || decreasStack(itemStackIn, playerIn))
		{
			setLastUseTime(itemStackIn, worldIn.getTotalWorldTime());
			worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvent.REGISTRY.getObject(new ResourceLocation("random.bow")), SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!worldIn.isRemote)
			{
				worldIn.spawnEntity(new EntityStone(worldIn, playerIn));
			}
		}
		/*
		 * else if(p.capabilities.isCreativeMode ||
		 * p.inventory.consumeInventoryItem(ARKCraftItems.explosive_ball)) {
		 * setLastUseTime(stack, w.getTotalWorldTime()); w.playSoundAtEntity(p,
		 * "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		 * if(!w.isRemote) w.spawnEntityInWorld(new EntityExplosive(w,p)); }
		 */

		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}

	private boolean decreasStack(ItemStack stack, EntityPlayer player)
	{
		--stack.stackSize;

		if (stack.stackSize == 0)
		{
			player.inventory.deleteStack(stack);
		}
		return true;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return oldStack != null && newStack != null && oldStack.getItem() != newStack.getItem() && slotChanged;
	}

	private void setLastUseTime(ItemStack stack, long time)
	{
		stack.setTagInfo("LastUse", new NBTTagLong(time));
	}

	private long getLastUseTime(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound().getLong("LastUse") : 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack) {
		long ticksSinceLastUse = Minecraft.getMinecraft().world.getTotalWorldTime() - getLastUseTime(stack);
		if (ticksSinceLastUse < 5)
		{
			return new ModelResourceLocation(ARKCraft.MODID + ":slingshot_pulled", "inventory");
		}
		else
		{
			return null;
		}
	}

}
