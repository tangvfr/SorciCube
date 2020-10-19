package fr.tangv.sorcicubespell.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import fr.tangv.sorcicubespell.SorciCubeSpell;

public class CommandTPS implements CommandExecutor {

	private SorciCubeSpell sorci;
	
	public CommandTPS(SorciCubeSpell sorci) {
		this.sorci = sorci;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		long rTime = 1000;
		if (args.length >= 1)
			try {
				rTime = Math.abs(Long.parseLong(args[0])*1000);
			} catch (Exception e) {}
		final long time = rTime;
		final long start = System.currentTimeMillis()+rTime;
		sender.sendMessage(sorci.getMessage().getString("message_calc_tps").replace("{time}", Long.toString(time)));
		Bukkit.getScheduler().runTaskTimerAsynchronously(this.sorci, new BukkitRunnable() {
			private long tps = 0;
			
			@Override
			public void run() {
				if (start < System.currentTimeMillis()) {
					this.cancel();
					sender.sendMessage(sorci.getMessage().getString("message_result_tps").replace("{time}", Long.toString(time)).replace("{tps}", Long.toString(tps)));
				} else
					tps++;
			}
		}, 0, 1);
		return true;
	}
	
}
