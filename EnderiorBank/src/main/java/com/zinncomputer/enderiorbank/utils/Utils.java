package com.zinncomputer.enderiorbank.utils;

import static com.zinncomputer.enderiorbank.EnderiorBank.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Utils {

	public static boolean isBankAtLoc(Location loc) {
		for (BankObject bo : banks) {
			if (bo.getLocationInWorld() == loc) {
				return true;
			}
		}
		return false;
	}

	public static Double nextPrice(int level) {
		return instance.getConfig().getDouble("bank.price.initial")
				+ ((level + 1) * instance.getConfig().getDouble(
						"bank.price.multiply-by"));
	}

	public static Double currentPrice(int level) {
		return instance.getConfig().getDouble("bank.price.initial")
				+ (level * instance.getConfig().getDouble(
						"bank.price.multiply-by"));
	}

	public static Long nextCapacity(int level) {
		return instance.getConfig().getLong("bank.capacity.initial")
				+ ((level + 1) * instance.getConfig().getLong(
						"bank.capacity.multiply-by"));
	}

	public static Long currencyCapacity(int level) {
		return instance.getConfig().getLong("bank.capacity.initial")
				+ (level * instance.getConfig().getLong(
						"bank.capacity.multiply-by"));
	}

	public static BankObject getBankAtLoc(Location loc) {
		if (!isBankAtLoc(loc))
			return null;
		for (BankObject bo : banks) {
			if (bo.getLocationInWorld() == loc)
				return bo;
		}
		return null;
	}

	public static int getItemCount(PlayerInventory inventory) {
		for (int i = 2304; i > 0; i--) {
			Material material = Material.GOLD_INGOT;
			if (inventory.contains(new ItemStack(material, i))) {
				return i;
			}
		}
		return 0;
	}

	public static boolean setupEconomy() {
		if (instance.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = instance.getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	
	/**
	 * Load the lang.yml file.
	 * 
	 * @return The lang.yml config.
	 */
	public static void loadLang() {
		File lang = new File(instance.getDataFolder(), "lang.yml");
		if (!lang.exists()) {
			try {
				instance.getDataFolder().mkdir();
				lang.createNewFile();
				InputStream defConfigStream = instance.getResource("lang.yml");
				if (defConfigStream != null) {
					@SuppressWarnings("deprecation")
					YamlConfiguration defConfig = YamlConfiguration
							.loadConfiguration(defConfigStream);
					defConfig.save(lang);
					Lang.setFile(defConfig);
					return;
				}
			} catch (IOException e) {
				instance.getLogger()
						.warning(
								"There was an issue loading the lang file. Using defaults.");
			}
		}
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
		for (Lang item : Lang.values()) {
			if (conf.getString(item.getPath()) == null) {
				conf.set(item.getPath(), item.getDefault());
			}
		}
		Lang.setFile(conf);
		LANG = conf;
		LANG_FILE = lang;
		try {
			conf.save(getLangFile());
		} catch (IOException e) {

			instance.getLogger().warning(
					"There was an issue saving the lang file. It's not fatal.");
		}
	}

	/**
	 * Gets the lang.yml config.
	 * 
	 * @return The lang.yml config.
	 */
	public YamlConfiguration getLang() {
		return LANG;
	}

	/**
	 * Get the lang.yml file.
	 * 
	 * @return The lang.yml file.
	 */
	public static File getLangFile() {
		return LANG_FILE;
	}
}
