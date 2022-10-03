package bluescreen9.minecraft.bukkit.tellpart;

import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class TeleportingTask extends BukkitRunnable{//传送任务
					protected TeleportType Type;
					protected Player player;
					protected BossBar bar;
					protected int time;
					protected int totalTime;
					protected Player target;
					protected Location loc;
					@Override
					public synchronized void cancel() throws IllegalStateException {
						bar.removePlayer(player);
						bar.setVisible(false);
						Teleporter.Teleporting.remove(player.getUniqueId());
						if (target != null) {
							Teleporter.Teleporting.remove(target.getUniqueId());
						}
						super.cancel();
					}
					
					public void init(BossBar bar,Player player,TeleportType type,int time,Player target) {
							this.bar = bar;
							this.player = player;
							this.Type = type;
							this.totalTime = time * 10;
							this.time = time * 10;
							this.target = target;
							this.loc = player.getLocation();
					}
}
