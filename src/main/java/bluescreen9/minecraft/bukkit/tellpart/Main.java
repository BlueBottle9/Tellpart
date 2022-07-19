package bluescreen9.minecraft.bukkit.tellpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import bluescreen9.minecraft.bukkit.lang.Lang;

public class Main extends JavaPlugin{
				protected static Plugin Tellpart;
				protected static Lang Language;
				protected static FileConfiguration Config;
				protected static ArrayList<String> EmptyComplete = new ArrayList<String>();
					@Override
					public void onEnable() {
						Tellpart = Main.getPlugin(Main.class);
						saveDefaultConfig();
						reloadConfig();
						Config = getConfig();
						Language = new Lang(Tellpart);
						Language.copyDeafultLangFile();
						Language.loadLanguages();
						Language.setDefaultLang(Config.getString("default-language"));
						getCommand("tpt").setExecutor(new TPTCommand());
						getCommand("tpt").setTabCompleter(new TPTCommand());
						getCommand("tph").setExecutor(new TPHCommand());
						getCommand("tph").setTabCompleter(new TPHCommand());
						getCommand("tpr").setExecutor(new TPRCommand());
						getCommand("tpr").setTabCompleter(new TPRCommand());
						getCommand("tph").setExecutor(new TPHCommand());
						getCommand("home").setExecutor(new HomeCommand());
						getCommand("home").setTabCompleter(new HomeCommand());
						getCommand("tpp").setExecutor(new TPPCommand());
						getCommand("tpp").setTabCompleter(new TPPCommand());
						Data.loadData();
						new BukkitRunnable() {
							public void run() {
								Data.uploadData();
							}
						}.runTaskTimerAsynchronously(Tellpart, 10L, 5L);
						new BukkitRunnable() {
							public void run() {
								ColdDown.reFresh();
							}
						}.runTaskTimerAsynchronously(Tellpart, 10L, 20L);
						
						new BukkitRunnable() {
							public void run() {
								for (UUID u:TeleportRequest.tphRequests.keySet()) {
										HashMap<UUID, Integer> map = TeleportRequest.tphRequests.get(u);
										for (UUID uuid:map.keySet()) {
											int i = map.get(uuid);
											if (i > 0) {
												map.replace(uuid, i - 1);
											}else {
												Player target = Bukkit.getPlayer(u);
												Player sender = Bukkit.getPlayer(uuid);
												map.remove(uuid);
												target.sendMessage(ChatColor.translateAlternateColorCodes('&', Language.get(target, "tph.lapse.target")).
														replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName()));
												sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Language.get(sender, "tph.lapse.sender")).
														replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName()));
											
											}
											TeleportRequest.tphRequests.replace(u, map);
										}
								}
								for (UUID u:TeleportRequest.tptRequests.keySet()) {
									HashMap<UUID, Integer> map = TeleportRequest.tptRequests.get(u);
									for (UUID uuid:map.keySet()) {
										int i = map.get(uuid);
										if (i > 0) {
											map.replace(uuid, i - 1);
										}else {
											Player target = Bukkit.getPlayer(u);
											Player sender = Bukkit.getPlayer(uuid);
											map.remove(uuid);
											target.sendMessage(ChatColor.translateAlternateColorCodes('&', Language.get(target, "tpt.lapse.target")).
													replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName()));
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Language.get(sender, "tpt.lapse.sender")).
													replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName()));
										}
										TeleportRequest.tptRequests.replace(u, map);
									}
							}
							}
						}.runTaskTimerAsynchronously(Tellpart, 10L,20L);
					}
					
					
					
					@Override
					public void onDisable() {
							Data.saveData();
					}
}
