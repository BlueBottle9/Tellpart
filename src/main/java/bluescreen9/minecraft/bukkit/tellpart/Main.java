package bluescreen9.minecraft.bukkit.tellpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import bluescreen9.minecraft.bukkit.lang.Lang;
import bluescreen9.minecraft.bukkit.lang.MessageProcessor;

public class Main extends JavaPlugin{
				protected static Plugin Tellpart;
				protected static Lang Language;
				protected static FileConfiguration Config;
				protected static ArrayList<String> EmptyComplete = new ArrayList<String>();
					@Override
					public void onEnable() {//初始化
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
						getCommand("back").setExecutor(new BackCommand());
						getCommand("back").setTabCompleter(new BackCommand());
						getServer().getPluginManager().registerEvents(new EventListener(), Tellpart);
						Data.loadData();
						new BukkitRunnable() {//定时更新数据
							public void run() {
								Data.uploadData();
							}
						}.runTaskTimerAsynchronously(Tellpart, 10L, 5L);
						new BukkitRunnable() {//刷新冷却时间
							public void run() {
								ColdDown.reFresh();
							}
						}.runTaskTimerAsynchronously(Tellpart, 10L, 20L);
						
						new BukkitRunnable() {//更新传送请求过期
							public void run() {
								for (UUID u:TeleportRequest.tphRequests.keySet()) {
										HashMap<UUID, Integer> map = TeleportRequest.tphRequests.get(u);
										for (UUID uuid:map.keySet()) {
											int i = map.get(uuid);
											if (i > 0) {
												map.replace(uuid, i - 1);
											}else {
												final Player target = Bukkit.getPlayer(u);
												final Player sender = Bukkit.getPlayer(uuid);
												map.remove(uuid);
												Language.sendMessage(target, "tph.lapse.target", Lang.ColorProcessor,new MessageProcessor() {
													public String process(String original) {
														return original.replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName());
													}
												});
														
												Language.sendMessage(sender, "tph.lapse.sender", Lang.ColorProcessor,new MessageProcessor() {
													public String process(String original) {
														return original.replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName());
													};
												});
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
											final Player target = Bukkit.getPlayer(u);
											final Player sender = Bukkit.getPlayer(uuid);
											map.remove(uuid);
											Language.sendMessage(target, "tpt.lapse.target", Lang.ColorProcessor,new MessageProcessor(){
												public String process(String original){
														return original.replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName());
												}
											});
											Language.sendMessage(sender, "tpt.lapse.sender", Lang.ColorProcessor,new MessageProcessor(){
												public String process(String original){
													return original.replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName());
												}
											});
										}
										TeleportRequest.tptRequests.replace(u, map);
									}
							}
							}
						}.runTaskTimerAsynchronously(Tellpart, 10L,20L);
					}
					
					
					
					@Override
					public void onDisable() {
							Data.saveData();//保存数据
					}
}
