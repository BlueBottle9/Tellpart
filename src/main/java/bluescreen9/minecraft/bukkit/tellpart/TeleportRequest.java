package bluescreen9.minecraft.bukkit.tellpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import bluescreen9.minecraft.bukkit.lang.Lang;
import bluescreen9.minecraft.bukkit.lang.MessageProcessor;

public class TeleportRequest {
				protected static HashMap<UUID, HashMap<UUID, Integer>> tphRequests = new HashMap<UUID, HashMap<UUID, Integer>>();//TPH请求
				protected static HashMap<UUID, HashMap<UUID, Integer>> tptRequests = new HashMap<UUID, HashMap<UUID, Integer>>();//TPT请求
				
				public static void send(final Player sender,final Player target,TeleportType type) {//发送请求
					if (type.equals(TeleportType.TPH)) {
						HashMap<UUID, Integer> map = null;
						if (get(target, type) == null) {
							map = new HashMap<UUID, Integer>();
						}else {
							map = get(target, type);
						}
						map.put(sender.getUniqueId(), Main.Config.getInt("tph.valid-time"));
						tphRequests.put(sender.getUniqueId(), map);
						Main.Language.sendMessage(sender, "tph.request.sender", Lang.ColorProcessor,new MessageProcessor() {
							public String process(String original) {
								return original.replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", sender.getName()).
										replaceAll("<%valid-time>", "" + Main.Config.getInt("tph.valid-time"));
							}
						});
						Main.Language.sendMessage(target, "tph.request.target", Lang.ColorProcessor,new MessageProcessor() {
							public String process(String original) {
								return original.replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", sender.getName()).
										replaceAll("<%valid-time>", "" + Main.Config.getInt("tph.valid-time"));
							}
						});
					}
					if (type.equals(TeleportType.TPT)) {
						HashMap<UUID, Integer> map = null;
						if (get(target, type) == null) {
							map = new HashMap<UUID, Integer>();
						}else {
							map = get(target, type);
						}
						map.put(sender.getUniqueId(), Main.Config.getInt("tpt.valid-time"));
						tptRequests.put(target.getUniqueId(), map);
						Main.Language.sendMessage(sender, "tpt.request.sender", Lang.ColorProcessor,new MessageProcessor() {
							public String process(String original) {
								return original.replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", sender.getName()).
										replaceAll("<%valid-time>", "" + Main.Config.getInt("tpt.valid-time"));
							}
						});
						Main.Language.sendMessage(target, "tpt.request.target", Lang.ColorProcessor,new MessageProcessor() {
							public String process(String original) {
								return original.replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", sender.getName()).
										replaceAll("<%valid-time>", "" + Main.Config.getInt("tpt.valid-time"));
							}
						});
					}
				}
				
				public static HashMap<UUID, Integer> get(Player player,TeleportType type) {//得到传送请求
					if (type.equals(TeleportType.TPH)) {
						if (tphRequests.get(player.getUniqueId()) == null) {
							tphRequests.put(player.getUniqueId(), new HashMap<UUID, Integer>());
						}
						return tphRequests.get(player.getUniqueId());
					}
					if (type.equals(TeleportType.TPT)) {
						if (tptRequests.get(player.getUniqueId()) == null) {
							tptRequests.put(player.getUniqueId(), new HashMap<UUID, Integer>());
						}
						return tptRequests.get(player.getUniqueId());
					}
					return new HashMap<UUID, Integer>();
				}
				
				public static List<String> getNames(Player player,TeleportType type) {//得到请求的所有(玩家的)名称
					if (type.equals(TeleportType.TPH)) {
						HashMap<UUID, Integer> uuids = tphRequests.get(player.getUniqueId());
						ArrayList<String> names = new ArrayList<String>();
						for (UUID u:uuids.keySet()) {
							names.add(Bukkit.getPlayer(u).getName());
						}
						return names;
					}
					if (type.equals(TeleportType.TPT)) {
						HashMap<UUID, Integer> uuids = tptRequests.get(player.getUniqueId());
						ArrayList<String> names = new ArrayList<String>();
						for (UUID u:uuids.keySet()) {
							names.add(Bukkit.getPlayer(u).getName());
						}

					}
					return null;
				}
				
				public static void remove(Player sender,Player target,TeleportType type) {//移除请求
						if (type.equals(TeleportType.TPH)) {
							HashMap<UUID, Integer> replace = tphRequests.get(target.getUniqueId());
							replace.remove(sender.getUniqueId());
							tphRequests.replace(target.getUniqueId(), replace);
						}
						if (type.equals(TeleportType.TPT)) {
							HashMap<UUID, Integer> replace = tptRequests.get(target.getUniqueId());
							replace.remove(sender.getUniqueId());
							tptRequests.replace(target.getUniqueId(), replace);
						}
							
						}
}
