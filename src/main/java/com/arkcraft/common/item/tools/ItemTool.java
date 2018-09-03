package com.arkcraft.common.item.tools;

import com.arkcraft.common.arkplayer.ARKPlayer;
import com.arkcraft.init.ARKCraftItems;
import com.google.common.collect.Multimap;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ItemTool extends Item {
	public static int count = 0;
	public String[] toolQuality = {"primitive", "ramshackle", "apprentice", "journeyman", "mastercraft", "ascendant"};
	public String toolType;
	public float qualityMultiplier;
	public int maxDamage;
	public String myTag;
	public String ID;
	public int durability;
	protected float efficiencyOnProperMaterial = 4.0F;
	boolean arkMode;
	private Set effectiveBlocks;
	private float damageVsEntity;
	/*
	 * ===================================== FORGE START
	 * =================================
	 */
	private String toolClass;

	protected ItemTool(String name, float attackDamage, Set effectiveBlocks, int durability, int efficiency) {
		this.effectiveBlocks = effectiveBlocks;
		this.maxStackSize = 1;
		// this.setMaxDamage(durability);
		this.efficiencyOnProperMaterial = efficiency;
		this.damageVsEntity = attackDamage;
		this.setTranslationKey(name);
		this.setHasSubtypes(true);
		this.durability = durability;
		// this.setMaxDamage(0);
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

	@Override
	public int getMaxDamage(ItemStack stack) {
		return durability;
		// return getDurability(stack);
	}

	// public String getTranslationKey(ItemStack stack)
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
	// String unloc = getTranslationKey();
	// return getTranslationKey() + "." + itemData.getString("toolType");
	// }
	// }
	// // This will be used if the item is obtained without nbt data on it.
	// return "item.nullTool";
	// }

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isAdvanced) {
		// if (stack.hasTagCompound() &&
		// stack.getTagCompound().hasKey("toolType"))
		// {
		// // StatCollector is a class which allows us to handle string
		// // language translation. This requires that you fill out the
		// // translation in you language class.
		// tooltip.add(I18n.format(
		// "tooltip.arkcraft." + stack.getTagCompound().getString("toolType") +
		// ".desc"));
		// }
		// else // If the brain does not have valid tag data, a default message
		// {
		// tooltip.add(I18n.format("tooltip.arkcraft.nullTool.desc"));
		// }
		if (stack.hasTagCompound()) {
			tooltip.add(Integer.toString(stack.getTagCompound().getInteger("toolDurability")));
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		/*
		 * ClientProxy p = ((ClientProxy)ARKCraft.proxy); List<ItemStack> list =
		 * new ArrayList<ItemStack>(); getSubItems(this, getCreativeTab(),
		 * list); for(int i = 0;i<list.size();i++){
		 */
		switch (ToolLevel.VALUES[stack.getMetadata() % ToolLevel.VALUES.length]) {
			case PRIMITIVE:
				return ("" + I18n.format(this.getTranslationKey(stack) + ".name")).trim();
			case RAMSCHACKLE:
				return (ChatFormatting.GREEN + "" + I18n.format(this.getTranslationKey(stack) + ".name")).trim();
			case APPRENTICE:
				return (ChatFormatting.BLUE + "" + I18n.format(this.getTranslationKey(stack) + ".name")).trim();
			case JOURNEYMAN:
				return (ChatFormatting.DARK_PURPLE + "" + I18n.format(this.getTranslationKey(stack) + ".name")).trim();
			case MASTERCRAFT:
				return (ChatFormatting.YELLOW + "" + I18n.format(this.getTranslationKey(stack) + ".name")).trim();
			case ASCENDANT:
				return (ChatFormatting.RED + "" + I18n.format(this.getTranslationKey(stack) + ".name")).trim();
			default:
				break;
		}
		return super.getItemStackDisplayName(stack);
	}

	public float getDestroySpeed(ItemStack stack, Block block) {
		return this.effectiveBlocks.contains(block) ? this.efficiencyOnProperMaterial : 1.0F;
	}

	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(2, attacker);
		return true;
	}

	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
		if (playerIn instanceof EntityPlayer && ARKPlayer.isARKMode((EntityPlayer) playerIn)) {
			IBlockState blockState = worldIn.getBlockState(pos);
			Float offset = worldIn.rand.nextFloat();

			if (playerIn instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) playerIn;

				if (blockState.getBlock() == Blocks.LOG || blockState.getBlock() == Blocks.LOG2) {
					this.destroyBlocks(worldIn, pos, player);

					if (toolType == "stonePickaxe") {
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.thatch, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.wood, 5));

					} else if (toolType == "stoneHatchet") {
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.thatch, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.wood, 10));
					} else if (toolType == "metalPickaxe") {
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.thatch, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.wood, 10));
					} else if (toolType == "metalHatchet") {
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.thatch, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.wood, 10));
					}
					worldIn.destroyBlock(new BlockPos(pos), false);
					stack.damageItem(count, player);
					count = 0;
				} else if (blockState.getBlock() == Blocks.STONE) {
					if (toolType == "stonePickaxe") {
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.flint, 10));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 1));
					} else if (toolType == "stoneHatchet") {
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.flint, 10));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 1));
					} else if (toolType == "metalPickaxe") {
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.flint, 10));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 1));
					} else if (toolType == "metalHatchet") {
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.flint, 10));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 1));
					}
					worldIn.destroyBlock(new BlockPos(pos), false);
					stack.damageItem(1, player);
					count = 0;
				} else if (blockState.getBlock() == Blocks.IRON_ORE) {
					if (toolType == "stonePickaxe") {
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 10));
					} else if (toolType == "stoneHatchet") {
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 10));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 10));
					} else if (toolType == "metalPickaxe") {
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 10));
					} else if (toolType == "metalHatchet") {
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 10));
					}
					worldIn.destroyBlock(new BlockPos(pos), false);
					stack.damageItem(1, player);
					count = 0;
				}
			}
		} else if (playerIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) playerIn;
			stack.damageItem(1, player);
		}
		return true;
	}

	public void entityDropItem(World worldIn, BlockPos pos, EntityPlayer playerIn, Block block, ItemStack itemStackIn) {
		if (itemStackIn.getCount() != 0 && itemStackIn.getItem() != null) {
			Float offset = worldIn.rand.nextFloat();
			EntityItem entityitem = new EntityItem(worldIn, pos.getX() + offset, pos.getY() + 1, pos.getZ() + offset, itemStackIn);
			entityitem.setDefaultPickupDelay();
			if (playerIn.captureDrops) {
				playerIn.capturedDrops.add(entityitem);
			} else {
				worldIn.spawnEntity(entityitem);
			}
		}
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
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit
	 * damage.
	 */
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
		// TODO replace random UUID
		multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(UUID.randomUUID(), "Tool modifier", (double) this.damageVsEntity, 0));
		return multimap;
	}

	public void destroyBlocks(World world, BlockPos pos, EntityPlayer player) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		for (int i = x - 1; i <= x + 1; i++) {
			for (int k = z - 1; k <= z + 1; k++) {
				for (int j = y - 1; j <= y + 1; j++) {
					IBlockState blockState = world.getBlockState(new BlockPos(i, j, k));
					if (blockState.getBlock() == Blocks.LOG || blockState.getBlock() == Blocks.LOG2) {
						world.destroyBlock(new BlockPos(i, j, k), false);
						++count;
						this.destroyBlocks(world, new BlockPos(i, j, k), player);
					}
				}
			}
		}

	}

	public float qualityMultiplier(ItemStack stack) {
		int metadata = stack.getMetadata();
		if (toolQuality[metadata] == "ramshackle") {
			return qualityMultiplier = 1.2F;
		} else if (toolQuality[metadata] == "apprentice") {
			return qualityMultiplier = 1.4F;
		} else if (toolQuality[metadata] == "journeyman") {
			return qualityMultiplier = 1.6F;
		} else if (toolQuality[metadata] == "mastercraft") {
			return qualityMultiplier = 1.8F;
		} else if (toolQuality[metadata] == "ascendant") {
			return qualityMultiplier = 2F;
		}
		return qualityMultiplier = 1F;
	}

	// Works
	/*
	 * @Override public String getItemStackDisplayName(ItemStack stack) { int
	 * metadata = stack.getMetadata(); if (toolQuality[metadata] ==
	 * "ramshackle") { return (EnumChatFormatting.GREEN + "" + StatCollector
	 * .translateToLocal(this .getTranslationKeyInefficiently(stack) +
	 * ".name")) .trim(); } else if (toolQuality[metadata] == "apprentice") {
	 * return (EnumChatFormatting.BLUE + "" + StatCollector
	 * .translateToLocal(this .getTranslationKeyInefficiently(stack) +
	 * ".name")) .trim(); } else if (toolQuality[metadata] == "journeyman") {
	 * return (EnumChatFormatting.DARK_PURPLE + "" + StatCollector
	 * .translateToLocal(this .getTranslationKeyInefficiently(stack) +
	 * ".name")) .trim(); } else if (toolQuality[metadata] == "mastercraft") {
	 * return (EnumChatFormatting.YELLOW + "" + StatCollector
	 * .translateToLocal(this .getTranslationKeyInefficiently(stack) +
	 * ".name")) .trim(); } else if (toolQuality[metadata] == "ascendant") {
	 * return (EnumChatFormatting.RED + "" + StatCollector
	 * .translateToLocal(this .getTranslationKeyInefficiently(stack) +
	 * ".name")) .trim(); } return ("" + I18n.format(this
	 * .getTranslationKeyInefficiently(stack) + ".name")).trim(); }
	 */

	public int getDurablity(ItemStack stack) {
		int metadata = stack.getMetadata();
		if (toolQuality[metadata] == "ramshackle") {
			return 5;
		} else if (toolQuality[metadata] == "apprentice") {
			return 20;
		} else if (toolQuality[metadata] == "journeyman") {
			return 20;
		} else if (toolQuality[metadata] == "mastercraft") {
			return 30;
		} else if (toolQuality[metadata] == "ascendant") {
			return 100;
		}
		return 20;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
		int level = super.getHarvestLevel(stack, toolClass, player, blockState);
		if (level == -1 && toolClass != null && toolClass.equals(this.toolClass)) {
			return 3;
		} else {
			return level;
		}
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack) {
		return toolClass != null ? com.google.common.collect.ImmutableSet.of(toolClass) : super.getToolClasses(stack);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		for (String type : getToolClasses(stack)) {
			if (state.getBlock().isToolEffective(type, state))
				return efficiencyOnProperMaterial;
		}
		return super.getDestroySpeed(stack, state);
	}
	/*
	 * ===================================== FORGE END
	 * =================================
	 */

}