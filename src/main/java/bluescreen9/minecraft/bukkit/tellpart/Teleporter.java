package bluescreen9.minecraft.bukkit.tellpart;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import bluescreen9.minecraft.bukkit.lang.Lang;
import bluescreen9.minecraft.bukkit.lang.MessageProcessor;

public class Teleporter {//传送器
	protected static HashMap<UUID, TeleportingTask> Teleporting = new HashMap<UUID,TeleportingTask>();//进行中的传送任务
				public static void home(Player player) {//回家
					Main.Language.sendMessage(player, "home.start", Lang.ColorProcessor);
					BossBar bar = Bukkit.createBossBar("", BarColor.valueOf(Main.Config.getString("bar.color")), BarStyle.valueOf(Main.Config.getString("bar.style")), BarFlag.PLAY_BOSS_MUSIC);
					bar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
					TeleportingTask task = new TeleportingTask() {
						public void run() {
							double progress = ((double)(time/10D)) / ((double)totalTime / 10D);
							if (progress < 0) {
								progress = 0;
							}
							this.bar.setTitle(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "home.bar")).
									replaceAll("<%home-time>", "" + (time / 10)));
							this.bar.setProgress(progress);
							if (this.bar.getProgress() == 0) {
								new BukkitRunnable() {
									public void run() {
										BackPoints.BackPoint.put(player.getUniqueId(), player.getLocation());
										player.teleport(Data.getHome(player));
										ColdDown.set(player, TeleportType.HOME,Main.Config.getInt("home.could-down"));
										Main.Language.sendMessage(player, "home.finish", Lang.ColorProcessor);
									}
								}.runTask(Main.Tellpart);
								cancel();
							}
							time = time - 1;
						}
					};
					task.init(bar, player, TeleportType.HOME,Main.Config.getInt("home.delay"),null);
					bar.addPlayer(player);
					task.runTaskTimerAsynchronously(Main.Tellpart, 0L, 2L);
					Teleporting.put(player.getUniqueId(), task);
				}
				
				public static void tpr(Player player) {//随机传送
					Main.Language.sendMessage(player, "tpr.start", Lang.ColorProcessor);
					
					BossBar bar = Bukkit.createBossBar("", BarColor.valueOf(Main.Config.getString("bar.color")), BarStyle.valueOf(Main.Config.getString("bar.style")), BarFlag.PLAY_BOSS_MUSIC);
					bar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
					TeleportingTask task = new TeleportingTask() {
						public void run() {
							double progress = ((double)(time/10D)) / ((double)totalTime / 10D);
							if (progress < 0) {
								progress = 0;
							}
							this.bar.setTitle(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpr.bar")).
									replaceAll("<%tpr-time>", "" + (time / 10)));
							this.bar.setProgress(progress);
							if (this.bar.getProgress() == 0) {
								World world = player.getWorld();
								 int i = (int) (world.getWorldBorder().getSize() / 2);
								final int x = random(-i, i);
								final int z = random(i, -i);
								new BukkitRunnable() {
									public void run() {
										final Location loc = player.getWorld().getHighestBlockAt(x, z).getLocation();
										BackPoints.BackPoint.put(player.getUniqueId(), player.getLocation());
										player.teleport(loc);
										ColdDown.set(player, TeleportType.TPR,Main.Config.getInt("tpr.could-down"));
										Main.Language.sendMessage(player, "tpr.finish.message", Lang.ColorProcessor,new MessageProcessor() {
											public String process(String original) {
												return original.replaceAll("<%tpr-dest>", loc.getBlockX() + " " +loc.getBlockY() + " " + loc.getBlockZ());
											}
										});
										player.sendTitle(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpr.finish.title.main").
												replaceAll("<%tpr-dest>", loc.getBlockX() + " " +loc.getBlockY() + " " + loc.getBlockZ())),
												ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpr.finish.title.sub").
														replaceAll("<%tpr-dest>", loc.getBlockX() + " " +loc.getBlockY() + " " + loc.getBlockZ()))
												, 40, 60, 50);
									}
								}.runTask(Main.Tellpart);
								cancel();
							}
							time = time - 1;
						}
					};
					task.init(bar, player, TeleportType.TPR,Main.Config.getInt("tpr.delay"),null);
					bar.addPlayer(player);
					task.runTaskTimerAsynchronously(Main.Tellpart, 0L, 2L);
					Teleporting.put(player.getUniqueId(), task);
				}
				
				public static void tpt(final Player sender,final Player target) {//请求传送到他人处
					Main.Language.sendMessage(target, "tpt.start.target", Lang.ColorProcessor,new MessageProcessor() {
						public String process(String original) {
							return original.replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName());
						}
					});
					Main.Language.sendMessage(sender, "tpt.start.sender", Lang.ColorProcessor,new MessageProcessor() {
						public String process(String original) {
							return original.replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName());
						}
					});
					BossBar bar = Bukkit.createBossBar("", BarColor.valueOf(Main.Config.getString("bar.color")), BarStyle.valueOf(Main.Config.getString("bar.style")), BarFlag.PLAY_BOSS_MUSIC);
					bar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
					TeleportingTask task = new TeleportingTask() {
						public void run() {
							double progress = ((double)(time/10D)) / ((double)totalTime / 10D);
							if (progress < 0) {
								progress = 0;
							}
							this.bar.setTitle(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpt.bar")).
									replaceAll("<%tpt-time>", "" + (time / 10)));
							this.bar.setProgress(progress);
							if (this.bar.getProgress() == 0) {
								new BukkitRunnable() {
									public void run() {
										BackPoints.BackPoint.put(target.getUniqueId(), target.getLocation());
										sender.teleport(target);
										Main.Language.sendMessage(target, "tpt.finish.target", Lang.ColorProcessor,new MessageProcessor() {
											public String process(String original) {
												return original.replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", sender.getName());
											}
										});
										Main.Language.sendMessage(sender, "tpt.finish.sender", Lang.ColorProcessor,new MessageProcessor() {
											public String process(String original) {
												return original.replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", sender.getName());
											}
										});
										ColdDown.set(player, TeleportType.TPT,Main.Config.getInt("tpt.could-down"));
										TeleportRequest.remove(sender, target, TeleportType.TPT);
										cancel();
									}
								}.runTask(Main.Tellpart);
								cancel();
							}
							time = time - 1;
						}
					};
					task.init(bar, sender, TeleportType.TPH,Main.Config.getInt("tph.delay"),target);
					bar.addPlayer(sender);
					task.runTaskTimerAsynchronously(Main.Tellpart, 0L, 2L);
					Teleporting.put(sender.getUniqueId(), task);
					Teleporting.put(target.getUniqueId(), task);
				}
				
				public static void tph(final Player sender,final Player target) {//请求他人传送到此处
					Main.Language.sendMessage(target, "tph.start.target", Lang.ColorProcessor,new MessageProcessor() {
						public String process(String original) {
							return original.replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName());
						}
					});
					Main.Language.sendMessage(sender, "tph.start.sender", Lang.ColorProcessor,new MessageProcessor() {
						public String process(String original) {
							return original.replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>",target.getName());
						}
					});
					BossBar bar = Bukkit.createBossBar("", BarColor.valueOf(Main.Config.getString("bar.color")), BarStyle.valueOf(Main.Config.getString("bar.style")), BarFlag.PLAY_BOSS_MUSIC);
					bar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
					TeleportingTask task = new TeleportingTask() {
						public void run() {
							double progress = ((double)(time/10D)) / ((double)totalTime / 10D);
							if (progress < 0) {
								progress = 0;
							}
							this.bar.setTitle(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tph.bar")).
									replaceAll("<%tph-time>", "" + (time / 10)));
							this.bar.setProgress(progress);
							if (this.bar.getProgress() == 0) {
								new BukkitRunnable() {
									public void run() {
										BackPoints.BackPoint.put(sender.getUniqueId(), sender.getLocation());
										target.teleport(sender);
										Main.Language.sendMessage(target, "tph.finsh.target", Lang.ColorProcessor,new MessageProcessor() {
											public String process(String original) {
												return original.replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName());
											}
										});
										Main.Language.sendMessage(sender, "tph.start.sender", Lang.ColorProcessor,new MessageProcessor() {
											public String process(String original) {
												return original.replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName());
											}
										});
										ColdDown.set(player, TeleportType.TPH,Main.Config.getInt("tph.could-down"));
										TeleportRequest.remove(sender, target, TeleportType.TPH);
										cancel();
									}
								}.runTask(Main.Tellpart);
								cancel();
							}
							time = time - 1;
						}
					};
					task.init(bar, target, TeleportType.TPH,Main.Config.getInt("tph.delay"),target);
					bar.addPlayer(target);
					task.runTaskTimerAsynchronously(Main.Tellpart, 0L, 2L);
					Teleporting.put(sender.getUniqueId(), task);
					Teleporting.put(target.getUniqueId(), task);
				}
				
				public static void tpp(Player player,final String point) {//传送至传送点
					Main.Language.sendMessage(player, "tpp.start", Lang.ColorProcessor,new MessageProcessor() {
						public String process(String original) {
							return original.replaceAll("<%point-name>", point);
						}
					});
					BossBar bar = Bukkit.createBossBar("", BarColor.valueOf(Main.Config.getString("bar.color")), BarStyle.valueOf(Main.Config.getString("bar.style")), BarFlag.PLAY_BOSS_MUSIC);
					bar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
					TeleportingTask task = new TeleportingTask() {
						public void run() {
							double progress = ((double)(time/10D)) / ((double)totalTime / 10D);
							if (progress < 0) {
								progress = 0;
							}
							this.bar.setTitle(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpp.bar")).
									replaceAll("<%tpp-time>", "" + (time / 10)).replaceAll("<%point-name>", point));
							this.bar.setProgress(progress);
							if (this.bar.getProgress() == 0) {
								new BukkitRunnable() {
									public void run() {
										BackPoints.BackPoint.put(player.getUniqueId(), player.getLocation());
										player.teleport(TeleportPoint.getPoints(player).get(point));
										ColdDown.set(player, TeleportType.TPP,Main.Config.getInt("tpp.could-down"));
										Main.Language.sendMessage(player, "tpp.finish", Lang.ColorProcessor,new MessageProcessor() {
											public String process(String original) {
												return original.replaceAll("<%point-name>", point);
											}
										});
									}
								}.runTask(Main.Tellpart);
								cancel();
							}
							time = time - 1;
						}
					};
					task.init(bar, player, TeleportType.TPP,Main.Config.getInt("tpr.delay"),null);
					bar.addPlayer(player);
					task.runTaskTimerAsynchronously(Main.Tellpart, 0L, 2L);
					Teleporting.put(player.getUniqueId(), task);
				}
				
				public static void cancel(final Player player) {//取消
					TeleportingTask task = Teleporting.get(player.getUniqueId());
					final Player target = task.target;
						if (task.Type.equals(TeleportType.TPH)) {
							Main.Language.sendMessage(player, "tph.cancel.sender", Lang.ColorProcessor,new MessageProcessor() {
								public String process(String original) {
									return original.replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", player.getName());
								}
							});
							Main.Language.sendMessage(target, "tph.cancel.target", Lang.ColorProcessor,new MessageProcessor() {
								public String process(String original) {
									return original.replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", player.getName());
								}
							});
						}
						if (task.Type.equals(TeleportType.TPT)) {
							Main.Language.sendMessage(player, "tpt.cancel.sender", Lang.ColorProcessor,new MessageProcessor() {
								public String process(String original) {
									return original.replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", player.getName());
								}
							});
							Main.Language.sendMessage(target, "tpt.cancel.target", Lang.ColorProcessor,new MessageProcessor() {
								public String process(String original) {
									return original.replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", player.getName());
								}
							});
						}
						if (task.Type.equals(TeleportType.TPR)) {
								Main.Language.sendMessage(player, "tpr.cancel", Lang.ColorProcessor);
						}
						if (task.Type.equals(TeleportType.TPP)) {
							Main.Language.sendMessage(player, "tpp.cancel", Lang.ColorProcessor);
						}
						if (task.Type.equals(TeleportType.HOME)) {
							Main.Language.sendMessage(player, "home.cancel", Lang.ColorProcessor);
						}
						if (task.Type.equals(TeleportType.BACK)) {
							Main.Language.sendMessage(player, "back.cancel", Lang.ColorProcessor);
						}
							Teleporting.get(player.getUniqueId()).cancel();
							Teleporting.remove(player.getUniqueId());
				}
				
				public static int random(int min,int max) {
					max++;
					return (int) (min + ((new Random().nextDouble() * (max - min))));
				}
				
				public static void back(Player player,final Location loc) {
					Main.Language.sendMessage(player, "back.start", Lang.ColorProcessor);
					BossBar bar = Bukkit.createBossBar("", BarColor.valueOf(Main.Config.getString("bar.color")), BarStyle.valueOf(Main.Config.getString("bar.style")), BarFlag.PLAY_BOSS_MUSIC);
					bar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
					TeleportingTask task = new TeleportingTask() {
						public void run() {
							double progress = ((double)(time/10D)) / ((double)totalTime / 10D);
							if (progress < 0) {
								progress = 0;
							}
							this.bar.setTitle(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "back.bar")).replaceAll("<%back-time>", "" + time/10));
							this.bar.setProgress(progress);
							if (this.bar.getProgress() == 0) {
								new BukkitRunnable() {
									public void run() {
										BackPoints.BackPoint.put(player.getUniqueId(), player.getLocation());
										player.teleport(loc);
										Main.Language.sendMessage(player, "back.finish", Lang.ColorProcessor);
									}
								}.runTask(Main.Tellpart);
								cancel();
							}
							time = time - 1;
						}
					};
					task.init(bar, player, TeleportType.BACK,Main.Config.getInt("back.delay"),null);
					bar.addPlayer(player);
					task.runTaskTimerAsynchronously(Main.Tellpart, 0L, 2L);
					Teleporting.put(player.getUniqueId(), task);
				}
}
