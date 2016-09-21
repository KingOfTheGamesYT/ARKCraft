package com.uberverse.arkcraft.common.item.tools;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.uberverse.arkcraft.common.arkplayer.ARKPlayer;
import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTool extends Item
{
	public String[] toolQuality = { "primitive", "ramshackle", "apprentice",
			"journeyman", "mastercraft", "ascendant" };
	private Set effectiveBlocks;
	protected float efficiencyOnProperMaterial = 4.0F;
	private float damageVsEntity;
	public static int count = 0;
	public String toolType;
	boolean arkMode;
	public float qualityMultiplier;
	public int maxDamage;
	public String myTag;
	public String ID;
	public int durability;

	protected ItemTool(String name, float attackDamage, Set effectiveBlocks, int durability, int efficiency)
	{
		this.effectiveBlocks = effectiveBlocks;
		this.maxStackSize = 1;
		// this.setMaxDamage(durability);
		this.efficiencyOnProperMaterial = efficiency;
		this.damageVsEntity = attackDamage;
		this.setUnlocalizedName(name);
		this.setHasSubtypes(true);
		this.durability = durability;
		// this.setMaxDamage(0);
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return durability;
		// return getDurability(stack);
	}
	/*
	 * public void onCreated(ItemStack stack, World worldIn, EntityPlayer
	 * playerIn) { getNBTTag(stack, myTag); System.out.println(getNBTTag(stack,
	 * myTag)); }
	 */

	/*
	 * public void setNBData(ItemStack stack) { NBTTagCompound data = new
	 * NBTTagCompound(); for (int i = 1; i < toolQuality.length +1; i++) {
	 * data.setInteger("toolDurability", i); // stack.setTagInfo(myTag, data); }
	 * }
	 */
	/*
	 * public int getDurability(ItemStack stack) { if (stack.hasTagCompound())
	 * return stack.getTagCompound().getInteger("toolDurability"); else return
	 * 0; } /* public void setDurability(ItemStack stack, int toolDurability) {
	 * if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
	 * stack.getTagCompound().setInteger("toolDurability", toolDurability); }
	 */

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player,
			List tooltip, boolean isAdvanced)
	{
		// if (stack.hasTagCompound() &&
		// stack.getTagCompound().hasKey("toolType"))
		// {
		// // StatCollector is a class which allows us to handle string
		// // language translation. This requires that you fill out the
		// // translation in you language class.
		// tooltip.add(StatCollector.translateToLocal(
		// "tooltip.arkcraft." + stack.getTagCompound().getString("toolType") +
		// ".desc"));
		// }
		// else // If the brain does not have valid tag data, a default message
		// {
		// tooltip.add(StatCollector.translateToLocal("tooltip.arkcraft.nullTool.desc"));
		// }
		if (stack.hasTagCompound())
		{
			tooltip.add(Integer.toString(
					stack.getTagCompound().getInteger("toolDurability")));
		}
	}

	// public String getUnlocalizedName(ItemStack stack)
	// {
	// if (stack.hasTagCompound())
	// {
	// // This is the object holding all of the item data.
	// NBTTagCompound itemData = stack.getTagCompound();
	// // This checks to see if the item has data stored under the
	// // brainType key.
	// if (itemData.hasKey("toolType"))
	// {
	// // This retrieves data from the brainType key and uses it in
	// // the return value
	// String unloc = getUnlocalizedName();
	// return getUnlocalizedName() + "." + itemData.getString("toolType");
	// }
	// }
	// // This will be used if the item is obtained without nbt data on it.
	// return "item.nullTool";
	// }

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		/*
		 * ClientProxy p = ((ClientProxy)ARKCraft.proxy); List<ItemStack> list =
		 * new ArrayList<ItemStack>(); getSubItems(this, getCreativeTab(),
		 * list); for(int i = 0;i<list.size();i++){
		 */
		switch (ToolLevel.VALUES[stack.getMetadata() % ToolLevel.VALUES.length])
		{
			case PRIMITIVE:
				return ("" + StatCollector.translateToLocal(
						this.getUnlocalizedNameInefficiently(stack) + ".name"))
								.trim();
			case RAMSCHACKLE:
				return (EnumChatFormatting.GREEN + ""
						+ StatCollector.translateToLocal(
								this.getUnlocalizedNameInefficiently(stack)
										+ ".name")).trim();
			case APPRENTICE:
				return (EnumChatFormatting.BLUE + ""
						+ StatCollector.translateToLocal(
								this.getUnlocalizedNameInefficiently(stack)
										+ ".name")).trim();
			case JOURNEYMAN:
				return (EnumChatFormatting.DARK_PURPLE + ""
						+ StatCollector.translateToLocal(
								this.getUnlocalizedNameInefficiently(stack)
										+ ".name")).trim();
			case MASTERCRAFT:
				return (EnumChatFormatting.YELLOW + ""
						+ StatCollector.translateToLocal(
								this.getUnlocalizedNameInefficiently(stack)
										+ ".name")).trim();
			case ASCENDANT:
				return (EnumChatFormatting.RED + ""
						+ StatCollector.translateToLocal(
								this.getUnlocalizedNameInefficiently(stack)
										+ ".name")).trim();
			default:
				break;
		}
		return super.getItemStackDisplayName(stack);
	}

	public float getStrVsBlock(ItemStack stack, Block block)
	{
		return this.effectiveBlocks.contains(block)
				? this.efficiencyOnProperMaterial : 1.0F;
	}

	public boolean hitEntity(ItemStack stack, EntityLivingBase target,
			EntityLivingBase attacker)
	{
		stack.damageItem(2, attacker);
		return true;
	}

	public boolean onBlockDestroyed(ItemStack stack, World worldIn,
			Block blockIn, BlockPos pos, EntityLivingBase playerIn)
	{
		if (playerIn instanceof EntityPlayer
				&& ARKPlayer.isARKMode((EntityPlayer) playerIn))
		{
			IBlockState blockState = worldIn.getBlockState(pos);
			Float offset = worldIn.rand.nextFloat();

			if (playerIn instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) playerIn;

				if (blockState.getBlock() == Blocks.log
						|| blockState.getBlock() == Blocks.log2)
				{
					this.destroyBlocks(worldIn, pos, player);

					if (toolType == "stonePickaxe")
					{
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.thatch, 5));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.wood, 5));

					}
					else if (toolType == "stoneHatchet")
					{
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.thatch, 5));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.wood, 10));
					}
					else if (toolType == "metalPickaxe")
					{
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.thatch, 5));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.wood, 10));
					}
					else if (toolType == "metalHatchet")
					{
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.thatch, 5));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.wood, 10));
					}
					worldIn.destroyBlock(new BlockPos(pos), false);
					stack.damageItem(count, player);
					count = 0;
				}

				else if (blockState.getBlock() == Blocks.stone)
				{
					if (toolType == "stonePickaxe")
					{
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.flint, 10));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.metal, 1));
					}
					else if (toolType == "stoneHatchet")
					{
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.flint, 10));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.metal, 1));
					}
					else if (toolType == "metalPickaxe")
					{
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.flint, 10));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.metal, 1));
					}
					else if (toolType == "metalHatchet")
					{
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.flint, 10));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.metal, 1));
					}
					worldIn.destroyBlock(new BlockPos(pos), false);
					stack.damageItem(1, player);
					count = 0;
				}
				else if (blockState.getBlock() == Blocks.iron_ore)
				{
					if (toolType == "stonePickaxe")
					{
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.metal, 10));
					}
					else if (toolType == "stoneHatchet")
					{
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.stone, 10));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.metal, 10));
					}
					else if (toolType == "metalPickaxe")
					{
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.metal, 10));
					}
					else if (toolType == "metalHatchet")
					{
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn,
								new ItemStack(ARKCraftItems.metal, 10));
					}
					worldIn.destroyBlock(new BlockPos(pos), false);
					stack.damageItem(1, player);
					count = 0;
				}
			}
		}

		else if (playerIn instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) playerIn;
			stack.damageItem(1, player);
		}
		return true;
	}

	public void entityDropItem(World worldIn, BlockPos pos,
			EntityPlayer playerIn, Block block, ItemStack itemStackIn)
	{
		if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null)
		{
			Float offset = worldIn.rand.nextFloat();
			EntityItem entityitem = new EntityItem(worldIn, pos.getX() + offset,
					pos.getY() + block.getBlockBoundsMaxY(),
					pos.getZ() + offset, itemStackIn);
			entityitem.setDefaultPickupDelay();
			if (playerIn.captureDrops)
			{
				playerIn.capturedDrops.add(entityitem);
			}
			else
			{
				worldIn.spawnEntityInWorld(entityitem);
			}
		}
	}

	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 * 
	 * @param toRepair
	 *            The ItemStack to be repaired
	 * @param repair
	 *            The ItemStack that should repair this Item (leather for
	 *            leather armor, etc.)
	 */
	// public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	// {
	// ItemStack mat = this.toolMaterial.getRepairItemStack();
	// if (mat != null &&
	// net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false))
	// return true;
	// return super.getIsRepairable(toRepair, repair);
	// }

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit
	 * damage.
	 */
	public Multimap getItemAttributeModifiers()
	{
		Multimap multimap = super.getItemAttributeModifiers();
		multimap.put(
				SharedMonsterAttributes.attackDamage
						.getAttributeUnlocalizedName(),
				new AttributeModifier(itemModifierUUID, "Tool modifier",
						(double) this.damageVsEntity, 0));
		return multimap;
	}

	public void destroyBlocks(World world, BlockPos pos, EntityPlayer player)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		for (int i = x - 1; i <= x + 1; i++)
		{
			for (int k = z - 1; k <= z + 1; k++)
			{
				for (int j = y - 1; j <= y + 1; j++)
				{
					IBlockState blockState =
							world.getBlockState(new BlockPos(i, j, k));
					if (blockState.getBlock() == Blocks.log
							|| blockState.getBlock() == Blocks.log2)
					{
						world.destroyBlock(new BlockPos(i, j, k), false);
						++count;
						this.destroyBlocks(world, new BlockPos(i, j, k),
								player);
					}
				}
			}
		}

	}

	public float qualityMultiplier(ItemStack stack)
	{
		int metadata = stack.getMetadata();
		if (toolQuality[metadata] == "ramshackle")
		{
			return qualityMultiplier = 1.2F;
		}
		else if (toolQuality[metadata] == "apprentice")
		{
			return qualityMultiplier = 1.4F;
		}
		else if (toolQuality[metadata] == "journeyman")
		{
			return qualityMultiplier = 1.6F;
		}
		else if (toolQuality[metadata] == "mastercraft")
		{
			return qualityMultiplier = 1.8F;
		}
		else if (toolQuality[metadata] == "ascendant") { return qualityMultiplier =
				2F; }
		return qualityMultiplier = 1F;
	}

	public int getDurablity(ItemStack stack)
	{
		int metadata = stack.getMetadata();
		if (toolQuality[metadata] == "ramshackle")
		{
			return 5;
		}
		else if (toolQuality[metadata] == "apprentice")
		{
			return 20;
		}
		else if (toolQuality[metadata] == "journeyman")
		{
			return 20;
		}
		else if (toolQuality[metadata] == "mastercraft")
		{
			return 30;
		}
		else if (toolQuality[metadata] == "ascendant") { return 100; }
		return 20;
	}

	// Works
	/*
	 * @Override public String getItemStackDisplayName(ItemStack stack) { int
	 * metadata = stack.getMetadata(); if (toolQuality[metadata] ==
	 * "ramshackle") { return (EnumChatFormatting.GREEN + "" + StatCollector
	 * .translateToLocal(this .getUnlocalizedNameInefficiently(stack) +
	 * ".name")) .trim(); } else if (toolQuality[metadata] == "apprentice") {
	 * return (EnumChatFormatting.BLUE + "" + StatCollector
	 * .translateToLocal(this .getUnlocalizedNameInefficiently(stack) +
	 * ".name")) .trim(); } else if (toolQuality[metadata] == "journeyman") {
	 * return (EnumChatFormatting.DARK_PURPLE + "" + StatCollector
	 * .translateToLocal(this .getUnlocalizedNameInefficiently(stack) +
	 * ".name")) .trim(); } else if (toolQuality[metadata] == "mastercraft") {
	 * return (EnumChatFormatting.YELLOW + "" + StatCollector
	 * .translateToLocal(this .getUnlocalizedNameInefficiently(stack) +
	 * ".name")) .trim(); } else if (toolQuality[metadata] == "ascendant") {
	 * return (EnumChatFormatting.RED + "" + StatCollector
	 * .translateToLocal(this .getUnlocalizedNameInefficiently(stack) +
	 * ".name")) .trim(); } return ("" + StatCollector.translateToLocal(this
	 * .getUnlocalizedNameInefficiently(stack) + ".name")).trim(); }
	 */

	/*
	 * ===================================== FORGE START
	 * =================================
	 */
	private String toolClass;

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		int level = super.getHarvestLevel(stack, toolClass);
		if (level == -1 && toolClass != null
				&& toolClass.equals(this.toolClass))
		{
			return 3;
		}
		else
		{
			return level;
		}
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return toolClass != null
				? com.google.common.collect.ImmutableSet.of(toolClass)
				: super.getToolClasses(stack);
	}

	@Override
	public float getDigSpeed(ItemStack stack,
			net.minecraft.block.state.IBlockState state)
	{
		for (String type : getToolClasses(stack))
		{
			if (state.getBlock().isToolEffective(type, state))
				return efficiencyOnProperMaterial;
		}
		return super.getDigSpeed(stack, state);
	}
	/*
	 * ===================================== FORGE END
	 * =================================
	 */

}