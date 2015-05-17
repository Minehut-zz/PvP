package com.minehut.pvp.arena;

import org.bukkit.Material;

public enum  ArenaType {

	ranged("ranged", Material.BOW),
	melee("melee", Material.STONE_SWORD),
	both("both", Material.WOOD_SWORD);

	private String type;
	private Material material;
	
	private ArenaType(String type, Material material) {
		this.type = type;
		this.material = material;
	}

	public static ArenaType getArenaType(String str) {
		for (ArenaType type : ArenaType.values()) {
			if (type.name().equalsIgnoreCase(str))
				return type;
		}
		return null;
	}

	public static ArenaType getArenaType(Material material) {
		for (ArenaType type : ArenaType.values()) {
			if (type.material == material)
				return type;
		}
		return null;
	}

	public String getType() {
		return type;
	}

	public Material getMaterial() {
		return material;
	}
}
