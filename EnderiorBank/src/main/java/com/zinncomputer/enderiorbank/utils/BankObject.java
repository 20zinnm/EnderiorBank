package com.zinncomputer.enderiorbank.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class BankObject implements ConfigurationSerializable {

	public long id;
	private int level;
	private Location locationInWorld;
	private long currencyDeposited;

	public BankObject(Location loc) {
		this.id = System.currentTimeMillis();
		this.level = 1;
		this.locationInWorld = loc;
		this.currencyDeposited = 0;
	}

	public BankObject() {
	}

	/*
	 * Serializes a bank object to a map.
	 */
	public Map<String, Object> serialize() {
		Map<String, Object> ser = new HashMap<String, Object>();
		ser.put("id", id);
		ser.put("level", level);
		ser.put("locationInWorld", locationInWorld.serialize());
		ser.put("currencyDeposited", currencyDeposited);
		return ser;
	}

	/*
	 * Deserialize a bank object from a Map.
	 */
	@SuppressWarnings("unchecked")
	public BankObject deserialize(Map<String, Object> ser) {
		this.id = Long.valueOf((String) ser.get("id"));
		this.level = (Integer) ser.get("level");
		this.locationInWorld = Location.deserialize((Map<String, Object>) ser
				.get("locationInWorld"));
		this.currencyDeposited = Long.valueOf((String) ser
				.get("currencyDeposited"));
		return this;
	}

	public Location getLocationInWorld() {
		return locationInWorld;
	}

	public void setLocationInWorld(Location locationInWorld) {
		this.locationInWorld = locationInWorld;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getCurrencyDeposited() {
		return currencyDeposited;
	}

	public void setCurrencyDeposited(long currencyDeposited) {
		this.currencyDeposited = currencyDeposited;
	}

}
