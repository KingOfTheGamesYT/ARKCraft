package com.uberverse.arkcraft.common.entity;

public interface IArkLeveling
{
	public void addXP(long xp);
	
	void checkLevel();
	
	long getRequiredXP();
}
