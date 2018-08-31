/**
 *
 */
package com.arkcraft.common.advancement;

import net.minecraft.advancements.CriteriaTriggers;

/**
 * @author ERBF
 */
public class ARKCraftAdvancementTriggers {
	public static final BigBangTrigger BIG_BANG_TRIGGER = new BigBangTrigger();

	public static void init() {
		//Criteria registration
		CriteriaTriggers.register(BIG_BANG_TRIGGER);
	}
}