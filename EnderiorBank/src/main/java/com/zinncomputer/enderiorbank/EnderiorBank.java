package com.zinncomputer.enderiorbank;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.zinncomputer.enderiorbank.listeners.BlockBreakListener;
import com.zinncomputer.enderiorbank.listeners.BlockPlaceListener;
import com.zinncomputer.enderiorbank.listeners.InventoryOpenListener;
import com.zinncomputer.enderiorbank.utils.BankObject;
import com.zinncomputer.enderiorbank.utils.Utils;

public class EnderiorBank extends JavaPlugin {

	public static Plugin instance;

	public static YamlConfiguration LANG;
	public static File LANG_FILE;

	public static Economy econ = null;

	public static List<BankObject> banks = new ArrayList<BankObject>();

	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		Utils.loadLang();
		ConfigurationSerialization.registerClass(BankObject.class);
		if (!Utils.setupEconomy()) {
			getLogger()
					.severe(String
							.format("[%s] - Disabled due to no Vault dependency found!",
									getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		loadBanks();
		Bukkit.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryOpenListener(), this);
	}

	@Override
	public void onDisable() {
		saveBanks();
	}

	@SuppressWarnings("unchecked")
	public void loadBanks() {
		File f = new File(getDataFolder(), "banks.yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				getLogger().severe(
						"There was an error creating the banks.yml file.");
				getLogger()
						.severe("Please report the text between the ---s to the developer.");
				getLogger().severe(
						"------------------------------------------------");
				e.printStackTrace();
				getLogger().severe(
						"------------------------------------------------");
			}
			return;
		} else {
			FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
			List<Map<?, ?>> b = fc.getMapList("banks");
			for (Map<?, ?> m : b) {
				Map<String, Object> ser = (Map<String, Object>) m;
				BankObject bo = new BankObject();
				bo = bo.deserialize(ser);
				banks.add(bo);
			}
		}
	}

	public void saveBanks() {
		File f = new File(getDataFolder(), "banks.yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				getLogger().severe(
						"There was an error creating the banks.yml file.");
				getLogger()
						.severe("Please report the text between the ---s to the developer.");
				getLogger().severe(
						"------------------------------------------------");
				e.printStackTrace();
				getLogger().severe(
						"------------------------------------------------");
			}
			return;
		} else {
			FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
			for (BankObject bo : banks) {
				fc.set("banks." + String.valueOf(bo.id), bo.serialize());
			}
			try {
				fc.save(f);
			} catch (IOException e) {
				getLogger().severe(
						"There was an error saving to the banks.yml file.");
				getLogger()
						.severe("Please report the text between the ---s to the developer.");
				getLogger().severe(
						"------------------------------------------------");
				e.printStackTrace();
				getLogger().severe(
						"------------------------------------------------");
			}
		}
	}
}
