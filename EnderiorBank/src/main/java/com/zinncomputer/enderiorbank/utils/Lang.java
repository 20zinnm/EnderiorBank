package com.zinncomputer.enderiorbank.utils;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * An enum for requesting strings from the language file.
 * 
 * @author gomeow
 */
public enum Lang {
	TITLE("title-name", "&8&l[&e&lEnderior&b&lBank&8&l] "), BROKE_BANK(
			"destroyed-bank", "&cYou destroyed an Enderior Bank."), PLACED_BANK(
			"placed-bank", "&aYou placed an Enderior Bank."), INVALID_ARGS(
			"invalid-args", "&cInvalid args!"), PLAYER_ONLY("player-only",
			"Sorry but that can only be run by a player!"), CHEST_TITLE(
			"chest-title", "&8&lEnderior Bank"), NO_KEY("no-key",
			"&cYou don't have the key to this bank!"), NO_GOLD("no-gold",
			"&cYou do not have the minimum 32 gold in your inventory!"), BANK_EMPTY(
			"bank-is-empty", "&cThe bank has no gold to withdraw!"), WITHDRAW(
			"withdraw", "&aYou withdrew &quantity gold!"), DEPOSIT("deposit",
			"&aYou deposited 64 gold to this bank."), NEM("not-enough-money",
			"&cYou don't have enough money!"), UPGRADED("upgraded",
			"&aYour bank was upgrated to level %level!"), NO_PERMS(
			"no-permissions", "&cYou don''t have permission for that!");

	private String path;
	private String def;
	private static YamlConfiguration LANG;

	/**
	 * Lang enum constructor.
	 * 
	 * @param path
	 *            The string path.
	 * @param start
	 *            The default string.
	 */
	Lang(String path, String start) {
		this.path = path;
		this.def = start;
	}

	/**
	 * Set the {@code YamlConfiguration} to use.
	 * 
	 * @param config
	 *            The config to set.
	 */
	public static void setFile(YamlConfiguration config) {
		LANG = config;
	}

	@Override
	public String toString() {
		return ChatColor.translateAlternateColorCodes('&',
				LANG.getString(this.path, def));
	}

	/**
	 * Get the default value of the path.
	 * 
	 * @return The default value of the path.
	 */
	public String getDefault() {
		return this.def;
	}

	/**
	 * Get the path to the string.
	 * 
	 * @return The path to the string.
	 */
	public String getPath() {
		return this.path;
	}
}