package com.zinncomputer.enderiorbank.listeners;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.zinncomputer.enderiorbank.EnderiorBank;
import com.zinncomputer.enderiorbank.utils.BankObject;
import com.zinncomputer.enderiorbank.utils.IconMenu;
import com.zinncomputer.enderiorbank.utils.Lang;
import com.zinncomputer.enderiorbank.utils.Utils;

public class InventoryOpenListener implements Listener {

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onInventoryOpenEvent(InventoryOpenEvent e) {
		if (!(e.getPlayer() instanceof Player))
			return;
		if (e.getInventory().getHolder() instanceof Chest) {
			InventoryHolder invHolder = e.getInventory().getHolder();
			Chest c = (Chest) invHolder;
			Block b = c.getBlock();
			if (b instanceof Chest) {
				final BankObject bo = Utils.getBankAtLoc(b.getLocation());
				if (bo == null)
					return;
				if (e.getPlayer()
						.getInventory()
						.getItemInHand()
						.getType()
						.equals(Material.getMaterial(EnderiorBank.instance
								.getConfig().getString("key.material")))) {
					ItemStack is = e.getPlayer().getItemInHand();
					ItemMeta im = is.getItemMeta();
					if (Arrays
							.asList(im.getLore())
							.get(0)
							.contains(
									ChatColor
											.translateAlternateColorCodes(
													'&',
													EnderiorBank.instance
															.getConfig()
															.getString(
																	"key.lore")
															.replace(
																	"%id",
																	String.valueOf(bo.id))))) {
						ItemStack deposit = new ItemStack(Material.ENDER_CHEST,
								1);
						ItemStack info = new ItemStack(Material.BOOK, 1);
						ItemStack withdraw = new ItemStack(Material.PAPER, 1);
						ItemStack upgrade = new ItemStack(Material.DIAMOND, 1);
						IconMenu menu = new IconMenu(
								Lang.CHEST_TITLE.toString(), 9,
								new IconMenu.OptionClickEventHandler() {
									public void onOptionClick(
											IconMenu.OptionClickEvent event) {
										if (event.getName().equals(
												ChatColor.GREEN + ""
														+ ChatColor.BOLD
														+ "Deposit")) {
											if (event
													.getPlayer()
													.getInventory()
													.contains(
															Material.GOLD_INGOT)) {
												if (Utils.getItemCount(event
														.getPlayer()
														.getInventory()) >= 32) {
													event.getPlayer()
															.getInventory()
															.remove(new ItemStack(
																	Material.GOLD_INGOT,
																	32));
													EnderiorBank.banks
															.remove(bo);
													bo.setCurrencyDeposited(bo
															.getCurrencyDeposited() + 32);
													EnderiorBank.banks.add(bo);
													event.getPlayer()
															.sendMessage(
																	Lang.TITLE
																			.toString()
																			+ Lang.DEPOSIT
																					.toString());
												} else {
													event.getPlayer()
															.sendMessage(
																	Lang.TITLE
																			.toString()
																			+ Lang.NO_GOLD
																					.toString());
												}
											} else {
												event.getPlayer()
														.sendMessage(
																Lang.TITLE
																		.toString()
																		+ Lang.NO_GOLD
																				.toString());
											}
										} else if (event.getName().equals(
												ChatColor.RED + ""
														+ ChatColor.BOLD
														+ "Withdraw")) {
											if (bo.getCurrencyDeposited() > 64) {
												EnderiorBank.banks.remove(bo);
												bo.setCurrencyDeposited(bo
														.getCurrencyDeposited() - 64);
												event.getPlayer()
														.getInventory()
														.addItem(
																new ItemStack(
																		Material.GOLD_INGOT,
																		64));
												EnderiorBank.banks.add(bo);
												event.getPlayer()
														.sendMessage(
																Lang.TITLE
																		.toString()
																		+ Lang.WITHDRAW
																				.toString()
																				.replace(
																						"%quantity",
																						"64"));
											} else {
												EnderiorBank.banks.remove(bo);
												int i = (int) bo
														.getCurrencyDeposited();
												event.getPlayer()
														.getInventory()
														.addItem(
																new ItemStack(
																		Material.GOLD_INGOT,
																		(int) bo.getCurrencyDeposited()));
												bo.setCurrencyDeposited(0);
												EnderiorBank.banks.add(bo);
												event.getPlayer()
														.sendMessage(
																Lang.TITLE
																		.toString()
																		+ Lang.WITHDRAW
																				.toString()
																				.replace(
																						"%quantity",
																						String.valueOf(i)));
											}
										} else if (event.getName().equals(
												ChatColor.BLUE + ""
														+ ChatColor.BOLD
														+ "Upgrade")) {
											if (EnderiorBank.econ.has(event
													.getPlayer(), Utils
													.nextPrice(bo.getLevel()))) {
												EnderiorBank.econ.withdrawPlayer(
														event.getPlayer(),
														Utils.nextPrice(bo
																.getLevel()));
												EnderiorBank.banks.remove(bo);
												bo.setLevel(bo.getLevel() + 1);
												EnderiorBank.banks.add(bo);
												event.getPlayer()
														.sendMessage(
																Lang.TITLE
																		.toString()
																		+ Lang.UPGRADED
																				.toString()
																				.replace(
																						"%level",
																						String.valueOf(bo
																								.getLevel())));
												event.setWillClose(true);
											} else {
												event.getPlayer()
														.sendMessage(
																Lang.TITLE
																		.toString()
																		+ Lang.NEM
																				.toString());
											}
										}
									}
								}, EnderiorBank.instance)
								.setOption(
										1,
										deposit,
										ChatColor.GREEN + "" + ChatColor.BOLD
												+ "Deposit",
										ChatColor.GREEN + "Deposit your gold!")
								.setOption(
										5,
										info,
										ChatColor.GRAY + "" + ChatColor.BOLD
												+ "Info",
										ChatColor.GRAY + "Bank ID: " + bo.id,
										ChatColor.GRAY + "Level: "
												+ bo.getLevel(),
										ChatColor.GRAY
												+ "Gold: "
												+ bo.getCurrencyDeposited()
												+ "/"
												+ Utils.currencyCapacity(bo
														.getLevel()))
								.setOption(
										3,
										withdraw,
										ChatColor.RED + "" + ChatColor.BOLD
												+ "Withdraw",
										ChatColor.RED
												+ "Withdraw gold from this bank.",
										ChatColor.RED + "Click once per stack.");
						if (bo.getLevel() < EnderiorBank.instance.getConfig()
								.getInt("bank.maxlvl")) {
							menu.setOption(
									7,
									upgrade,
									ChatColor.BLUE + "" + ChatColor.BOLD
											+ "Upgrade",
									ChatColor.BLUE + "Cost: "
											+ Utils.nextPrice(bo.getLevel()),
									ChatColor.BLUE + "New Capacity: "
											+ Utils.nextCapacity(bo.getLevel()));
						} else {
							menu.setOption(7, new ItemStack(
									Material.IRON_FENCE, 1), ChatColor.BLACK
									+ "" + ChatColor.BOLD
									+ "Upgrade Unavailable", ChatColor.BLACK
									+ "This bank is already fully upgraded!");
						}
						e.getPlayer().closeInventory();
						menu.open((Player) e.getPlayer());
					}
				} else {
					e.getPlayer().sendMessage(
							Lang.TITLE.toString() + Lang.NO_KEY.toString());
					return;
				}
			}
		}
	}
}
