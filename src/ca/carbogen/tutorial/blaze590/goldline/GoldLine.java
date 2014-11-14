package ca.carbogen.tutorial.blaze590.goldline;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import com.projectkorra.ProjectKorra.Methods;
import com.projectkorra.ProjectKorra.ProjectKorra;
import com.projectkorra.ProjectKorra.TempBlock;

public class GoldLine 
{
	// Public static variables can be accessed in another class.
	public static ConcurrentHashMap<Player, GoldLine> instances = 
			new ConcurrentHashMap<Player, GoldLine>(); 	// This variable lets us access a specific player's GoldLine.
	public static int range = 
			ProjectKorra.plugin.getConfig().getInt("ExtraAbilities.Carbogen.GoldLine.range");	// Get the config 
																								// value for range.
	public static int cleanup = 
			ProjectKorra.plugin.getConfig().getInt("ExtraAbilities.Carbogen.GoldLine.cleanup");	// Get the config 
																								// value for cleanup.
	
	// Instance variables who's values only concerns this instance of GoldLine.
	private Player player; 	// Remembers who this instance was created by.
	private long startTime; // Keeps track of the time the ability started.
	private List<Block> affectedBlocks = new ArrayList<Block>(); 	// 'affectedBlocks' will be a list of all the 
																	// blocks which will be affected by this ability.
	private List<TempBlock> tempBlocks = new ArrayList<TempBlock>(); 	// 'tempBlocks' will contain all the gold 
																		// blocks we will create, and will be used 
																		// as reference to revert them.
	
	public GoldLine(Player player)	// Constructor which takes 1 argument: 'player' (a Player).
									// Constructors create instances of an object, so there can be multiple
									// GoldLines at once, each with their own values.
	{
		if(instances.containsKey(player))	// If there is already an instance associated with this player...
			return;	// End the method, making the move not happen.
		
		this.player = player;	// Set the 'player' value of this instance to whatever value the method's 
								// 'player' variable holds.
		
		this.startTime = System.currentTimeMillis();	// Set the 'startTime' variable to the current 
														// time in milliseconds.
		
		findAffectedBlocks();
		
		instances.put(player, this); 	// Register this instance of GoldLine in the 'instances' variable.
	}
	
	public void findAffectedBlocks() 	// This method is responsible for finding and 
										// registering all blocks to be turned to gold.
	{
		Location origin = player.getLocation(); // Declare a Location variable representing the player's location.
		
		origin.setY(origin.getY() - 1);	// Substract 1 from origin's Y coordinate, to get 
										// the block beneath the player.
		
		Location destination = Methods.getTargetedLocation(player, range); 	// Get the location the player is
																			// is looking at, with a range limit.
		
		BlockIterator bi = new BlockIterator(									// Create a new BlockIterator...
						origin.getWorld(), 										// in the same world as 'origin',
						origin.toVector(), 										// from origin,
						Methods.getDirection(origin, destination).normalize(),  // towards 'destination',
						0, 														// not sure what this is xD,
						range);													// with this range.
		
		while(bi.hasNext())	// Literally, while our variable, 'bi', has more values to give us...
		{
			Block b = bi.next();	// Declare 'b' as bi's next value.
			
			if(Methods.isEarthbendable(player, b)) // If 'player' can earthbend 'b'...
			{
				affectedBlocks.add(b);	// Add 'b' to 'affectedBlocks' for later use.
			}
		}
	}
	
	public void turnBlockToGold(Block b) // This method will turn a specified block into gold.
	{
		TempBlock tb = new TempBlock(	// Create a new TempBlock called 'tb'...
				b,						// on top of 'b',
				Material.GOLD_BLOCK,	// made of gold,
				(byte) 0);				// with data of 0. (Data makes the difference 
										// between Oak wood, Acacia wood, Spruce wood, etc.)
		
		tempBlocks.add(tb);	// Register 'tb' in the list, 'tempBlocks'.
	}
	
	public void revertAllGoldBackToEarth()	// This method reverts all Gold blocks back into their previous type.
	{
		for(TempBlock tb : tempBlocks)	// Do the following for every TempBlock in 'tempBlocks'...
		{
			tb.revertBlock();	// Revert 'tb' to the block it replaced.
		}
	}
	
	public void remove()	// This method will remove this instance of GoldLine, either because
							// the move has finished or because an error has occurred (like the player dying).
	{
		revertAllGoldBackToEarth(); // Revert all the gold blocks back to their original state.
		affectedBlocks.clear();		// Clear the list of affected blocks.
		tempBlocks.clear();			// Clear the list of temporary blocks.
		instances.remove(player);	// Remove this instance of GoldLine from the 'instances' Map.
	}
	
	public void progress()	// This method is intended to be run every few ticks on the server, it
							// handles the animations as well as the conclusion of the move.
	{
		long currentTime = System.currentTimeMillis();	// Sets a variable, 'currentTime', as the current system time.
		
		if(											// Literally, if
				!player.isOnline() || 				// the player is NOT online, OR
				player.isDead() ||					// the player is dead, OR
				!Methods.getBoundAbility(player).equalsIgnoreCase("GoldLine") || 
													// if the player's bound ability is NOT "GoldLine", OR
				currentTime > startTime + cleanup)	// currentTime is GREATER THAN (startTime PLUS cleanup)...
											
		{
			remove();	// Remove this instance...
			return;		// End the method here.
		}
		
		for(Block b : affectedBlocks)	// Literally, for every Block in 'affectedBlocks'...
		{
			if(b.getType() != Material.GOLD_BLOCK) // if b's type is NOT already gold...
			{
				turnBlockToGold(b);	// Turn 'b' to gold.
				
				break;	// Break out of the for-loop.
						// The reason we want to break out is so that it turns one block to gold at a time.
						// The next time this method runs, 'b' will already be gold, hence the if-statement on
						// line 132 won't let this code be run, and the loop will proceed to the next block.
			}
		}
	}
	
	public static void progressAll()	// Create a static (can be accessed from outside this class) method 
										// which runs the 'progress()' method for every instance of GoldLine.
	{
		for(Player p : instances.keySet())	// For every player in instances' keys...
		{
			GoldLine gl = instances.get(p);	// Get the instance of GoldLine which belongs to the player, 'p',
			gl.progress(); 					// Run that instance's 'progress()' method.
		}
	}
}
