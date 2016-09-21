/**
 * 
 */
package com.uberverse.arkcraft.deprecated;

import java.util.ArrayList;

import net.minecraft.client.resources.I18n;

/**
 * @author ERBF | Aug 11, 2016
 */
public class ARKCraftEngrams
{

	public static ArrayList<Engram> engramList = new ArrayList<Engram>();

	// Level 1
	public static Engram stonePick, torch;

	// Level 2
	public static Engram campfire, stoneHatchet, spear, note, clothPants,
			clothShirt, thatchFoundation, thatchDoorframe;

	// Level 3
	public static Engram woodenClub, waterskin, clothGloves, clothBoots,
			clothHat, woodenSign, hideSleepingBag, thatchRoof, thatchWall,
			thatchDoor;

	// Level 5
	public static Engram slingshot, storageBox, simpleBed, phiomiaSaddle,
			mortarAndPestle, sparkpowder, bloodExtractionSyringe, narcotic,
			paintbrush, singlePanelFlag, multiPanelFlag, standingTorch,
			slopedThatchWallLeft, slopedThatchWallRight, slopedThatchRoof,
			woodenFoundation, woodenWall;

	public static void init()
	{
		// Level 1
		stonePick = new Engram("engramStonePick", 1, 0);
		torch = new Engram("engramTorch", 1, 0);

		// Level 2
		campfire = new Engram("engramCampfire", 2, 3);
		stoneHatchet = new Engram("engramStoneHatchet", 2, 3);
		spear = new Engram("engramSpear", 2, 3);
		note = new Engram("engramNote", 2, 3);
		clothPants = new Engram("engramClothPants", 2, 3);
		clothShirt = new Engram("engramClothShirt", 2, 3);
		thatchFoundation = new Engram("engramThatchFoundation", 2, 3);
		thatchDoorframe = new Engram("engramThatchDoorframe", 2, 3);

		// Level 3
		woodenClub = new Engram("engramWoodenClub", 3, 4);
		waterskin = new Engram("engramWaterskin", 3, 6);
		clothGloves = new Engram("engramClothGloves", 3, 3);
		clothBoots = new Engram("engramClothBoots", 3, 3);
		clothHat = new Engram("engramClothHat", 3, 3);
		woodenSign = new Engram("engramWoodenSign", 3, 3);
		hideSleepingBag = new Engram("engramHideSleepingBag", 3, 3);
		thatchRoof = new Engram("engramThatchRoof", 3, 3);
		thatchWall = new Engram("engramThatchWall", 3, 3);
		thatchDoor = new Engram("engramThatchDoor", 3, 3);

		// Level 5
		slingshot = new Engram("engramSlingshot", 5, 6);
		storageBox = new Engram("engramStorageBox", 5, 6);
		simpleBed = new Engram("engramSimpleBed", 5, 8);
		phiomiaSaddle = new Engram("engramPhiomiaSaddle", 5, 6);
		mortarAndPestle = new Engram("engramMortarAndPestle", 5, 6);
		sparkpowder = new Engram("engramSparkpowder", 5, 3);
		bloodExtractionSyringe =
				new Engram("engramBloodExtractionSyringe", 5, 6);
		narcotic = new Engram("engramNarcotic", 5, 6);
		paintbrush = new Engram("engramPaintbrush", 5, 3);
		singlePanelFlag = new Engram("engramSinglePanelFlag", 5, 6);
		multiPanelFlag = new Engram("engramMultiPanelFlag", 5, 6);
		standingTorch = new Engram("engramStandingTorch", 5, 6);
		slopedThatchWallLeft = new Engram("engramSlopedThatchWallLeft", 5, 3);
		slopedThatchWallRight = new Engram("engramSlopedThatchWallRight", 5, 3);
		slopedThatchRoof = new Engram("engramSlopedThatchRoof", 5, 3);
		woodenFoundation = new Engram("engramWoodenFoundation", 5, 6);
		woodenWall = new Engram("engramWoodenWall", 5, 7);
	}

	public static Engram getEngramByLocalizedName(String localizedName)
	{
		String name = I18n.format(localizedName);
		for (Engram engram : engramList)
		{
			if (engram.getFormattedName()
					.equalsIgnoreCase(name)) { return engram; }
		}
		return null;
	}

	public static Engram getEngramByUnlocalizedName(String unlocalizedName)
	{
		for (Engram engram : engramList)
		{
			if (engram.getName()
					.equalsIgnoreCase(unlocalizedName)) { return engram; }
		}
		return null;
	}

}
