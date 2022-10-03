package bluescreen9.minecraft.bukkit.tellpart;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import bluescreen9.minecraft.bukkit.lang.Lang;
// /back 指令
public class BackCommand implements TabExecutor{

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {//补全
		return Main.EmptyComplete;//返回空
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				if (!Main.Config.getBoolean("back.enable")) {//已启用？
					Main.Language.sendMessage(sender, "command.back.disabled", Lang.ColorProcessor);
					return true;
				}
				
				
				if (!(sender instanceof Player)) {//是玩家？
						Main.Language.sendMessage(sender, "command.back.playeronly",Lang.ColorProcessor);
					return true;
				}
				
				if (args.length != 0) {//命令长度正确？
					Main.Language.sendMessage(sender, "command.back.wrongusage", Lang.ColorProcessor);
					return true;
				}
				Player player = (Player)sender;
				if (Teleporter.Teleporting.containsKey(player.getUniqueId())) {
					Main.Language.sendMessage(player, "command.back.alreadyteleporting", Lang.ColorProcessor);
					return true;
				}
				Location back = BackPoints.BackPoint.get(player.getUniqueId());
				if (back == null) {//没有可用的返回点？
					Main.Language.sendMessage(player, "command.back.nopoint", Lang.ColorProcessor);
					return true;
				}
				
				Teleporter.back(player, back);//传送至返回点
		return true;
	}

}
