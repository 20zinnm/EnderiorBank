package com.zinncomputer.enderiorbank.listeners;

import static com.zinncomputer.enderiorbank.EnderiorBank.banks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.zinncomputer.enderiorbank.EnderiorBank;
import com.zinncomputer.enderiorbank.utils.BankObject;
import com.zinncomputer.enderiorbank.utils.Lang;

public class BlockPlaceListener implements Listener {

	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent e) {
		if (e.getBlock().getType().equals(Material.CHEST)) {
			if (e.getPlayer().hasPermission("enderiorbank.place")) {
				BankObject bo = new BankObject(e.getBlock().getLocation());
				banks.add(bo);
				e.getPlayer().sendMessage(
						Lang.TITLE.toString() + Lang.PLACED_BANK.toString());
				ItemStack is = new ItemStack(
						Material.getMaterial(EnderiorBank.instance.getConfig()
								.getString("key.material")));
				ItemMeta im = is.getItemMeta();
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor
						.translateAlternateColorCodes(
								'&',
								EnderiorBank.instance.getConfig().getString(
										"key.lore")).replace("%id",
								String.valueOf(bo.id)));
				im.setLore(lore);
				im.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD
						+ "Enderior Bank Key");
				is.setItemMeta(im);
				e.getPlayer().getInventory().addItem(is);
			}
		}
	}

}
