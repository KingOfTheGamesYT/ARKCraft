package com.uberverse.arkcraft.common.entity;

public interface IArkLevelable
{
	void addXP(double xp);

	double getXP();

	void updateLevel();

	double getRequiredXP();

	short getLevel();

	short getMaxLevel();
}
