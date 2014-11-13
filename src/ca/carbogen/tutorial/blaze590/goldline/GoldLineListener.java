package ca.carbogen.tutorial.blaze590.goldline;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.projectkorra.ProjectKorra.Methods;
import com.projectkorra.ProjectKorra.ProjectKorra;

public class GoldLineListener implements Listener
{
	@EventHandler
	public void onPlayerSwing(PlayerAnimationEvent e)
	{
		Player player = e.getPlayer(); // Make the variable 'player' represent the player that punched.
		if(!Methods.canBend(player.getName(), "GoldLine")) // If the player cannot use this ability...
			return; // End the method. (Don't do whatever comes next.)
		
		if(!Methods.getBoundAbility(player).equalsIgnoreCase("GoldLine")) // If the player is not on the item slot
																		 // which GoldLine is bound to...
			return; // End the method. (Don't do whatever comes next.)
		
		new GoldLine(player);
	}
	
	public static void manageGoldLine()
	{
		BukkitRunnable br = new BukkitRunnable()	// Make a new BukkitRunnable object (an object that can be
													// run at regular interval.) called 'br'...
		{
			public void run() // 'run()' is the method inside the BukkitRunnable which will be run.
			{
				GoldLine.progressAll();	// Make every instance of GoldLine run their 'progress()' method.
			}
		};
		
		ProjectKorra.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(ProjectKorra.plugin, br, 0, 1);
			// The above line tells ProjectKorra to schedule 'br' to be run every 1 ticks (give or take).
	}
}
