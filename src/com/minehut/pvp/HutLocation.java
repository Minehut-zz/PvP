package com.minehut.pvp;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class HutLocation {

	public int id = -1;
	
	private UUID worldID = UUID.randomUUID();
	
	private double x = 0, y = 0, z = 0;
	
	public HutLocation(Location location) {
		this.worldID = location.getWorld().getUID();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
	}
	
	public HutLocation(UUID worldID, double x, double y, double z) {
		this.worldID = worldID;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Location getLocation() {
		return new Location(this.getWorld(), this.x, this.y, this.z);
	}
	
	
	public World getWorld() {
		return Bukkit.getWorld(this.worldID);
	}
}
