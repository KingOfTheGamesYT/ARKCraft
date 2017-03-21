package com.uberverse.arkcraft.common.item.ranged;

import com.uberverse.arkcraft.ARKCraft;
import com.uberverse.arkcraft.common.entity.projectile.EntityStone;
import com.uberverse.arkcraft.init.ARKCraftItems;

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

public class ItemSlingshot extends Item
{

	public ItemSlingshot()
	{
		super();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		if (playerIn.capabilities.isCreativeMode || playerIn.inventory.(ARKCraftItems.stone))
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

	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
	{
		long ticksSinceLastUse = player.world.getTotalWorldTime() - getLastUseTime(stack);
		if (ticksSinceLastUse < 5)
		{
			return new ModelResourceLocation(ARKCraft.MODID + ":slingshot_pulled", "inventory");
		}
		else
		{
			return null;
		}
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

}
