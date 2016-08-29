package com.uberverse.arkcraft.common.entity;

public interface IArkLeveling
{
	public void addXP(long xp);
	
	public long getXP();

	void updateLevel();

	long getRequiredXP();

	public short getLevel();
	
	public short getMaxLevel();
}
