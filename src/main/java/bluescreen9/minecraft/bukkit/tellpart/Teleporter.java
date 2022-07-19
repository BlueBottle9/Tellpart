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

public class Teleporter {
	protected static HashMap<UUID, TeleportingTask> Teleporting = new HashMap<UUID,TeleportingTask>();
				public static void home(Player player) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "home.start")));
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
										player.teleport(Data.getHome(player));
										ColdDown.set(player, TeleportType.HOME,Main.Config.getInt("home.could-down"));
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "home.finish")));
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
				}
				
				public static void tpr(Player player) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpr.start")));
					
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
										Location loc = player.getWorld().getHighestBlockAt(x, z).getLocation();
										player.teleport(loc);
										ColdDown.set(player, TeleportType.TPR,Main.Config.getInt("tpr.could-down"));
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpr.finish.message")).
												replaceAll("<%tpr-dest>", loc.getBlockX() + " " +loc.getBlockY() + " " + loc.getBlockZ()));
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
				}
				
				public static void tpt(final Player sender,Player target) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(sender, "tpt.start.target")).
							replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>", target.getName()));
					target.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(target, "tpt.start.sender")).
							replaceAll("<%sender-name>", target.getName()).replaceAll("<%target-name>", sender.getName()));
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
										target.teleport(player);
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpt.finish.target")).
												replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", target.getName()));
										target.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(target, "tpt.finish.sender")).
												replaceAll("<%target-name>", sender.getName()).replaceAll("<%sender-name>", target.getName()));
										ColdDown.set(player, TeleportType.TPT,Main.Config.getInt("tpt.could-down"));
										TeleportRequest.remove(sender, target, TeleportType.TPT);
									}
								}.runTask(Main.Tellpart);
								cancel();
							}
							time = time - 1;
						}
					};
					task.init(bar, sender, TeleportType.TPH,Main.Config.getInt("tph.delay"),target);
					bar.addPlayer(target);
					task.runTaskTimerAsynchronously(Main.Tellpart, 0L, 2L);
				}
				
				public static void tph(final Player sender,Player target) {
					target.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(target, "tph.start.sender")).
							replaceAll("<%sender-name>", target.getName()).replaceAll("<%target-name>", sender.getName()));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(sender, "tph.start.target")).
							replaceAll("<%sender-name>", sender.getName()).replaceAll("<%target-name>",target.getName()));
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
										sender.teleport(target);
										target.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(target, "tph.finish.sender")).
												replaceAll("<%target-name>", sender.getName()).replaceAll("<%sender-name>", target.getName()));
										sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(sender, "tph.finish.target")).
												replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", sender.getName()));
										ColdDown.set(player, TeleportType.TPH,Main.Config.getInt("tph.could-down"));
										TeleportRequest.remove(sender, target, TeleportType.TPH);
									}
								}.runTask(Main.Tellpart);
								cancel();
							}
							time = time - 1;
						}
					};
					task.init(bar, target, TeleportType.TPH,Main.Config.getInt("tph.delay"),target);
					bar.addPlayer(sender);
					task.runTaskTimerAsynchronously(Main.Tellpart, 0L, 2L);
				}
				
				public static void tpp(Player player,final String point) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpp.start")).replaceAll("<%point-name>", point));
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
										player.teleport(TeleportPoint.getPoints(player).get(point));
										ColdDown.set(player, TeleportType.TPP,Main.Config.getInt("tpp.could-down"));
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpp.finish")).
												replaceAll("<%point-name>", point));
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
				}
				
				public static void cancel(Player player) {
					TeleportingTask task = Teleporting.get(player.getUniqueId());
					Player target = task.target;
						if (task.Type.equals(TeleportType.TPH)) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tph.cancel.sender").
											replaceAll("<%target-name>", target.getName())).replaceAll("<%sender-name>", player.getName()));
									target.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tph.cancel.sender").
											replaceAll("<%target-name>", target.getName())).replaceAll("<%sender-name>", player.getName()));
						}
						if (task.Type.equals(TeleportType.TPT)) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpt.cancel.sender").
										replaceAll("<%target-name>", target.getName())).replaceAll("<%sender-name>", player.getName()));
								target.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpt.cancel.sender").
										replaceAll("<%target-name>", target.getName())).replaceAll("<%sender-name>", player.getName()));
						}
						if (task.Type.equals(TeleportType.TPR)) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpr.cancel")));
						}
						if (task.Type.equals(TeleportType.TPP)) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpp.cancel")));
						}
						if (task.Type.equals(TeleportType.TPR)) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "tpr.cancel")));
						}
						if (task.Type.equals(TeleportType.HOME)) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "home.cancel")));
						}
							Teleporting.get(player.getUniqueId()).cancel();
							Teleporting.remove(player.getUniqueId());
				}
				
				public static int random(int min,int max) {
					max++;
					return (int) (min + ((new Random().nextDouble() * (max - min))));
				}
}
