package bluescreen9.minecraft.bukkit.tellpart;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportPoint {
					protected static HashMap<UUID, HashMap<String, Location>> tpPoints = new HashMap<UUID,HashMap<String,Location>>();
					
					public static HashMap<String, Location> getPoints(Player player) {
						if (!tpPoints.containsKey(player.getUniqueId())) {
							return new HashMap<String, Location>();	
						}
						return tpPoints.get(player.getUniqueId());
					}
					
					public static void remove(Player player,String key) {
								tpPoints.get(player.getUniqueId()).remove(key);
					}
					
					public static void add(Player player,String key,Location loc) {
							if (tpPoints.get(player.getUniqueId()) == null) {
								tpPoints.put(player.getUniqueId(), new HashMap<String, Location>());
							}
								tpPoints.get(player.getUniqueId()).put(key, loc);
					}
}
