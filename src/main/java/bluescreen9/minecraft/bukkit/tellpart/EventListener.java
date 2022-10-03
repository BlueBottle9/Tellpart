package bluescreen9.minecraft.bukkit.tellpart;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.WorldSaveEvent;

public class EventListener implements Listener{
					@EventHandler
					public void onDie(PlayerDeathEvent event) {//玩家寄时
							Player player = event.getEntity();
							BackPoints.BackPoint.put(player.getUniqueId(), player.getLocation());
					}
					
					@EventHandler
					public void onServerSave(WorldSaveEvent event) {//监听世界保存
						Data.saveData();//顺势保存数据
					}
					
					@EventHandler
					public void onPlayerMove(PlayerMoveEvent event) {//移动取消传送
						if (!Main.Config.getBoolean("move-to-cancel")) {
							return;
						}
						if  (event.isCancelled()) {
							return;
						}
						UUID uuid = event.getPlayer().getUniqueId();
						if (Teleporter.Teleporting.get(uuid) == null) {
							return;
						}
						TeleportingTask task = Teleporter.Teleporting.get(uuid);
						if (task.Type == TeleportType.TPT && uuid.equals(task.player.getUniqueId())) {
								return;
						}
						if (task.Type == TeleportType.TPH && uuid.equals(task.target.getUniqueId())) {
							return;
						}
						if (event.getTo().distance(task.loc) < 1.0D) {
							return;
						}
							Teleporter.cancel(event.getPlayer());
					}
}
