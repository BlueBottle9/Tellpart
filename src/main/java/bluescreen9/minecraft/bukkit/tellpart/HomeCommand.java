package bluescreen9.minecraft.bukkit.tellpart;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import bluescreen9.minecraft.bukkit.lang.Lang;
import bluescreen9.minecraft.bukkit.lang.MessageProcessor;
// /home 指令
public class HomeCommand implements TabExecutor{
	private static ArrayList<String> complete = new ArrayList<String>();//补全
	static {
		complete.add("set");
	} 
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			return complete;
		}
		return Main.EmptyComplete;//空补全
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if (!(sender instanceof Player)) {//不是玩家？
					Main.Language.sendMessage(sender, "command.playeronly", Lang.ColorProcessor);
					return true;
			}
			final Player player = (Player) sender;
			if (!Main.Config.getBoolean("home.enable")) {//已启用？
				Main.Language.sendMessage(player, "command.home.disabled", Lang.ColorProcessor);
				return true;
			}
			if (args.length != 0 && args.length != 1) {//命令语法正确？
				Main.Language.sendMessage(player, "command.home.wrongsusage", Lang.ColorProcessor);
				return true;
			}
			if (args.length == 1 && !(args[0].equals("set"))) {
				Main.Language.sendMessage(player, "command.home.wrongsusage", Lang.ColorProcessor);
				return true;
			}
			
			if (Teleporter.Teleporting.containsKey(player.getUniqueId())) {
				Main.Language.sendMessage(player, "command.home.alreadyteleporting", Lang.ColorProcessor);
				return true;
			}
			
			if (ColdDown.get(player, TeleportType.HOME) > 0) {//冷却中？
				Main.Language.sendMessage(player, "command.home.coldingdown", Lang.ColorProcessor,new MessageProcessor() {
					public String process(String original) {
					return original.replaceAll("<%home-colding>","" + ColdDown.get(player, TeleportType.HOME));
				}});
				return true;
			}
			
			if (args.length == 1) {//设置家？
				Main.Language.sendMessage(player, "command.home.homeset", Lang.ColorProcessor);
				Data.setHome(player, player.getLocation());
				return true;
			}
			
			if (args.length == 0) {//回家
				if (Data.getHome(player) == null) {//没有家？
					Main.Language.sendMessage(player, "command.home.homeunset", Lang.ColorProcessor);
					return true;
				}
				Teleporter.home(player);
			}
		return true;
	}

}
