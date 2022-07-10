package bluescreen9.minecraft.bukkit.tellpart;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class ColdDown {
				private static HashMap<UUID, Integer> home = new HashMap<UUID, Integer>();
				private static HashMap<UUID, Integer> tpr = new HashMap<UUID, Integer>();
				private static HashMap<UUID, Integer> tph = new HashMap<UUID, Integer>();
				private static HashMap<UUID, Integer> tpt = new HashMap<UUID, Integer>();
				private static HashMap<UUID, Integer> tpp = new HashMap<UUID, Integer>();
				
				public static int get(Player player,TeleportType type) {
					UUID uuid = player.getUniqueId();
					if (TeleportType.HOME.equals(type)) {
							if (!home.containsKey(uuid)) {
								return 0;
							}
							return home.get(uuid);
					}
					if (TeleportType.TPR.equals(type)) {
						if (!tpr.containsKey(uuid)) {
							return 0;
						}
						return tpr.get(uuid);
					}
					if (TeleportType.TPT.equals(type)) {
						if (!tpt.containsKey(uuid)) {
							return 0;
						}
						return tpt.get(uuid);
					}
					if (TeleportType.TPP.equals(type)) {
						if (!tpp.containsKey(uuid)) {
							return 0;
						}
						return tpp.get(uuid);
					}
					if (TeleportType.TPH.equals(type)) {
						if (!tph.containsKey(uuid)) {
							return 0;
						}
						return tph.get(uuid);
					}
					return 0;
				}
				
				public static void set(Player player,TeleportType type,int value) {
						UUID uuid = player.getUniqueId();
						if (TeleportType.HOME.equals(type)) {
							if (!home.containsKey(uuid)) {
								home.put(uuid, value);
								return;
							}
							home.replace(uuid, value);
							return;
						}
						if (TeleportType.TPR.equals(type)) {
							if (!tpr.containsKey(uuid)) {
								tpr.put(uuid, value);
								return;
							}
							tpr.replace(uuid, value);
							return;
						}
						if (TeleportType.TPT.equals(type)) {
							if (!tpt.containsKey(uuid)) {
								tpt.put(uuid, value);
								return;
							}
							tpt.replace(uuid, value);
							return;
						}
						if (TeleportType.TPH.equals(type)) {
							if (!tph.containsKey(uuid)) {
								tph.put(uuid, value);
								return;
							}
							tph.replace(uuid, value);
							return;
						}
						if (TeleportType.TPP.equals(type)) {
							if (!tpp.containsKey(uuid)) {
								tpp.put(uuid, value);
								return;
							}
							tpp.replace(uuid, value);
						}
				}
				
				protected static void reFresh() {
					for (UUID uuid:home.keySet()) {
						int i = home.get(uuid) - 1;
						if (i < 0) {
							i = 0;
						}
							home.replace(uuid, i);
					}
					for (UUID uuid:tpr.keySet()) {
						int i = tpr.get(uuid) - 1;
						if (i < 0) {
							i = 0;
						}
							tpr.replace(uuid, i);
					}
					for (UUID uuid:tpt.keySet()) {
						int i = tpt.get(uuid) - 1;
						if (i < 0) {
							i = 0;
						}
							tpt.replace(uuid, i);
					}
					for (UUID uuid:tph.keySet()) {
						int i = tph.get(uuid) - 1;
						if (i < 0) {
							i = 0;
						}
							tph.replace(uuid, i);
					}
					for (UUID uuid:tpp.keySet()) {
						int i = tpp.get(uuid) - 1;
						if (i < 0) {
							i = 0;
						}
							tpp.replace(uuid, i);
					}
				}
}
