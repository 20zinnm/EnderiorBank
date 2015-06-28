package com.zinncomputer.enderiorbank.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.zinncomputer.enderiorbank.EnderiorBank;
import com.zinncomputer.enderiorbank.utils.Lang;
import com.zinncomputer.enderiorbank.utils.Utils;

import static com.zinncomputer.enderiorbank.utils.Utils.*;

public class BlockBreakListener implements Listener {

	/*
	 * Disables the breaking of banks to those without the permission.
	 */
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e) {
		if (!e.getPlayer().hasPermission("enderiorbank.break")
				&& isBankAtLoc(e.getBlock().getLocation())) {
			e.setCancelled(true);
		} else {
			if (isBankAtLoc(e.getBlock().getLocation())) {
				e.getPlayer().sendMessage(
						Lang.TITLE.toString() + Lang.BROKE_BANK.toString());
				EnderiorBank.banks.remove(Utils.getBankAtLoc(e.getBlock().getLocation()));
			} 
		}
	}

}
