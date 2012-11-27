package com.aegamesi.mc.po8.support;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SerializedLocation implements Serializable {
	private static final long serialVersionUID = -9094035533656633605L;

	public final String WORLDNAME;
	public final double X;
	public final double Y;
	public final double Z;
	public final float YAW;
	public final float PITCH;

	public SerializedLocation(Location location) {
		this.WORLDNAME = location.getWorld().getName();
		this.X = location.getX();
		this.Y = location.getY();
		this.Z = location.getZ();
		this.YAW = location.getYaw();
		this.PITCH = location.getPitch();
	}

	public Location deserialize() {
		return new Location(Bukkit.getWorld(this.WORLDNAME), this.X, this.Y, this.Z, this.YAW, this.PITCH);
	}

	public boolean equals(Object o) {
		if (!(o instanceof SerializedLocation))
			return false;
		SerializedLocation l = (SerializedLocation) o;
		return deserialize().equals(l.deserialize());
	}

	public int hashCode() {
		return deserialize().hashCode();
	}
}