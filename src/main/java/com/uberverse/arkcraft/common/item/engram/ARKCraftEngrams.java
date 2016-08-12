/**
 * 
 */
package com.uberverse.arkcraft.common.item.engram;

import java.util.ArrayList;

/**
 * @author ERBF | Aug 11, 2016
 *
 */
public class ARKCraftEngrams 
{
	
	public static ArrayList<Engram> engramList = new ArrayList<Engram>();
	
	//Level 1
	public static Engram stonePick, torch;
	
	//Level 2
	public static Engram campfire, stoneHatchet, spear, note, clothPants, clothShirt, thatchFoundation, thatchDoorframe;
	
	//Level 3
	public static Engram woodenClub, waterskin, clothGloves, clothBoots, clothHat, woodenSign, hideSleepingBag, 
						 thatchRoof, thatchWall, thatchDoor;
	
	public static void init()
	{
		//Level 1
		stonePick = new Engram("engramStonePick", 1, 0);
		torch = new Engram("engramTorch", 1, 0);
		
		//Level 2
		campfire = new Engram("engramCampfire", 2, 3);
		stoneHatchet = new Engram("engramStoneHatchet", 2, 3);
		spear = new Engram("engramSpear", 2, 3);
		note = new Engram("engramNote", 2, 3);
		clothPants = new Engram("engramClothPants", 2, 3);
		clothShirt = new Engram("engramClothShirt", 2, 3);
		thatchFoundation = new Engram("engramThatchFoundation", 2, 3);
		thatchDoorframe = new Engram("engramThatchDoorframe", 2, 3);
		
		//Level 3
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
	}

}
