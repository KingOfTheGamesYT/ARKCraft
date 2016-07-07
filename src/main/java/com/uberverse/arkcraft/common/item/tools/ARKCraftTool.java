package com.uberverse.arkcraft.common.item.tools;

import java.util.List;
import java.util.Set;
import java.util.Random;



















import com.google.common.collect.Sets;
import com.uberverse.arkcraft.client.event.ClientEventHandler;
import com.uberverse.arkcraft.init.ARKCraftItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ARKCraftTool extends Item{
	
	boolean arkMode;
	public static int count = 0;
	public int min = 5;
	public int max = 20;
	
	public String[] toolQuality = {"primitive", "ramshackle", "apprentice", "journeyman", "mastercraft", "ascendant"};
	
	public String toolType;
	public int harvestLevel;
	public int durablity;
	public float eff;
	public float dmg;
	public int enchant;

	public float qualityMultiplier;

	@SuppressWarnings("rawtypes")
	public ARKCraftTool(String name, float attackDamage, Set effectiveBlocks, int harvestLevel,  int durablity, float eff, float dmg, int enchant){
		//super(attackDamage, material, effectiveBlocks);
		EnumHelper.addToolMaterial(name, harvestLevel, durablity, eff, dmg, enchant);
		this.setUnlocalizedName(name);
        this.setHasSubtypes(true);
        this.setMaxDamage(0); 
		this.toolType = toolType;
		this.harvestLevel = harvestLevel;
		this.durablity = durablity;
		this.eff = eff;
		this.dmg = dmg;
		this.enchant = enchant;
	}
	
	 public String getUnlocalizedName(ItemStack stack)
	 {
	        int metadata = stack.getMetadata();
	        return super.getUnlocalizedName() + "." + toolQuality[metadata];
	 }
	 
	 @Override
	 public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) 
	 {
		 
	 }
	 
	 
//	 @Override
//	    public void addInformation(ItemStack itemStack, EntityPlayer playerIn, List tooltip, boolean advanced)
//	    {
//	        tooltip.add("");
//	    }
	/**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     *  
     * @param subItems The List of sub-items. This is a List of ItemStacks.
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List subItems)
    {
        for (int i = 0; i < 6; ++i)
        {
            subItems.add(new ItemStack(item, 1, i));
        }
    }
    
    public int DropAmmounts()
    {
    	return (int)(Math.random() * (max - min) + min);
    }
    
    public Item getHarvestItem(Random rand) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack heldStack = player.getCurrentEquippedItem();

		//TODO replace spyglass with sickle
		if (heldStack != null && heldStack.getItem() == ARKCraftItems.metal_hatchet) {
			if (rand.nextInt(10) <= 3) {
				return ARKCraftItems.wood;
			} else if (rand.nextInt(15) <= 4) {
				return ARKCraftItems.thatch;
			} else {
				return ARKCraftItems.wood;
			}
		}
		return null;

		/*
		 * else if (rand.nextInt(10) <= 4) { return rand.nextInt(10) <= 5 ?
		 * ARKCraftItems.amarBerry : ARKCraftItems.narcoBerry; } else if
		 * (rand.nextInt(15) <= 4) { return ARKCraftItems.stimBerry; } else if
		 * (rand.nextInt(10) >= 4 && rand.nextInt(10) <= 8) { return
		 * rand.nextInt(10) <= 5 ? ARKCraftItems.mejoBerry :
		 * ARKCraftItems.tintoBerry; } else { return ARKCraftItems.azulBerry; }
		 */
	}
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
    {
    	int metadata = stack.getMetadata();
		if (toolQuality[metadata] == "ramshackle") 
		{
		this.durablity = 1;
		} 
		else if (toolQuality[metadata] == "apprentice") 
		{
			this.durablity = 2;	
		} 
		else if (toolQuality[metadata] == "journeyman") 
		{
			this.durablity = 3;		
		} 
		else if (toolQuality[metadata] == "mastercraft") 
		{
			this.durablity = 5;		
		} 
		else if (toolQuality[metadata] == "ascendant") 
		{
			this.durablity = 1000;		
		}
		System.out.println(durablity);
		
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
			return	qualityMultiplier = 1.6F;
		} 
		else if (toolQuality[metadata] == "mastercraft") 
		{
			return	qualityMultiplier = 1.8F;
		} 
		else if (toolQuality[metadata] == "ascendant") 
		{
			return	qualityMultiplier = 2F;
		}
		return qualityMultiplier = 1F;
	}
	
	public void entityDropItem(World worldIn, BlockPos pos, EntityPlayer playerIn, Block block, ItemStack itemStackIn) {
		if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
			Float offset = worldIn.rand.nextFloat();
			EntityItem entityitem = new EntityItem(worldIn, pos.getX() + offset, pos.getY() + block.getBlockBoundsMaxY(),
					pos.getZ() + offset, itemStackIn);
			entityitem.setDefaultPickupDelay();
			if (playerIn.captureDrops) {
				playerIn.capturedDrops.add(entityitem);
			} else {
				worldIn.spawnEntityInWorld(entityitem);
			}
		}
	}
	
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
	{ 
		if (arkMode != ClientEventHandler.openOverlay()) 
		{
			IBlockState blockState = worldIn.getBlockState(pos);
			Float offset = worldIn.rand.nextFloat();
			
			if(playerIn instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) playerIn;
				
				if (blockState.getBlock() == Blocks.log || blockState.getBlock() == Blocks.log2) 
				{
					this.destroyBlocks(worldIn, pos, player);
					
					if(toolType == "stonePickaxe")
					{
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.thatch, DropAmmounts()));
						System.out.println(DropAmmounts());
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.wood, 5));

					}
					else if(toolType == "stoneHatchet")
					{
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.thatch, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.wood, 10));
					}
					else if(toolType == "metalPickaxe")
					{
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.thatch, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.wood, 10));
					}
					else if(toolType == "metalHatchet")
					{
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.thatch, DropAmmounts()));
						System.out.println(DropAmmounts());
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.wood, 10));
					}
					worldIn.destroyBlock(new BlockPos(pos), false);
					count = 0;
				}		
				
				else if(blockState.getBlock() == Blocks.stone)
				{
					if(toolType == "stonePickaxe")
					{
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.flint, 10));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 1));
					}
					else if(toolType == "stoneHatchet")
					{
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.flint, 10));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 1));
					}
					else if(toolType == "metalPickaxe")
					{
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.flint, 10));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 1));
					}
					else if(toolType == "metalHatchet")
					{
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.flint, 10));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 1));
					}
					worldIn.destroyBlock(new BlockPos(pos), false);
					count = 0;
				}
				else if(blockState.getBlock() == Blocks.iron_ore)
				{
					if(toolType == "stonePickaxe")
					{
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 10));
					}
					else if(toolType == "stoneHatchet")
					{
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 10));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 10));
					}
					else if(toolType == "metalPickaxe")
					{
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 10));
					}
					else if(toolType == "metalHatchet")
					{
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.stone, 5));
						entityDropItem(worldIn, pos, player, blockIn, new ItemStack(ARKCraftItems.metal, 10));
					}
					worldIn.destroyBlock(new BlockPos(pos), false);
					count = 0;
				}
			}
		}
		return true;
	}
	
	//@Override
//	public boolean canHarvestBlock(Block blockIn)
	//{
	//    return blockIn == Blocks.obsidian ? this.toolMaterial.getHarvestLevel() == 3 : (blockIn != Blocks.diamond_block && blockIn != Blocks.diamond_ore ? (blockIn != Blocks.emerald_ore && blockIn != Blocks.emerald_block ? (blockIn != Blocks.gold_block && blockIn != Blocks.gold_ore ? (blockIn != Blocks.iron_block && blockIn != Blocks.iron_ore ? (blockIn != Blocks.lapis_block && blockIn != Blocks.lapis_ore ? (blockIn != Blocks.redstone_ore && blockIn != Blocks.lit_redstone_ore ? (blockIn.getMaterial() == Material.rock ? true : (blockIn.getMaterial() == Material.iron ? true : blockIn.getMaterial() == Material.anvil)) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2);
//	}
	
	//Works
	public void destroyBlocks(World world, BlockPos pos, EntityPlayer player) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		for (int i = x - 1; i <= x + 1; i++) {
			for (int k = z - 1; k <= z + 1; k++) {
				for (int j = y - 1; j <= y + 1; j++) {
						IBlockState blockState = world.getBlockState(new BlockPos(i, j, k));
						if (blockState.getBlock() == Blocks.log || blockState.getBlock() == Blocks.log2) 
						{
							world.destroyBlock(new BlockPos(i, j, k), false);
							++count;
							this.destroyBlocks(world, new BlockPos(i, j, k), player);
					}
				}
			}
		}

	}
	
	//Works
	@Override
	public String getItemStackDisplayName(ItemStack stack) 
	{
		int metadata = stack.getMetadata();
		if (toolQuality[metadata] == "ramshackle") {
			return (EnumChatFormatting.GREEN + "" + StatCollector
					.translateToLocal(this
							.getUnlocalizedNameInefficiently(stack) + ".name"))
					.trim();
		} else if (toolQuality[metadata] == "apprentice") {
			return (EnumChatFormatting.BLUE + "" + StatCollector
					.translateToLocal(this
							.getUnlocalizedNameInefficiently(stack) + ".name"))
					.trim();

		} else if (toolQuality[metadata] == "journeyman") {
			return (EnumChatFormatting.DARK_PURPLE + "" + StatCollector
					.translateToLocal(this
							.getUnlocalizedNameInefficiently(stack) + ".name"))
					.trim();

		} else if (toolQuality[metadata] == "mastercraft") {
			return (EnumChatFormatting.YELLOW + "" + StatCollector
					.translateToLocal(this
							.getUnlocalizedNameInefficiently(stack) + ".name"))
					.trim();

		} else if (toolQuality[metadata] == "ascendant") {
			return (EnumChatFormatting.RED + "" + StatCollector
					.translateToLocal(this
							.getUnlocalizedNameInefficiently(stack) + ".name"))
					.trim();

		}
		return ("" + StatCollector.translateToLocal(this
				.getUnlocalizedNameInefficiently(stack) + ".name")).trim();
	}
}
