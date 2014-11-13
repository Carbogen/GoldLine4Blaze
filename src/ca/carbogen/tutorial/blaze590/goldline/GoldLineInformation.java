package ca.carbogen.tutorial.blaze590.goldline;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import com.projectkorra.ProjectKorra.Element;
import com.projectkorra.ProjectKorra.ProjectKorra;
import com.projectkorra.ProjectKorra.Ability.AbilityModule;

public class GoldLineInformation extends AbilityModule	// This class extends (is a child of) the AbilityModule class.
{
	private Plugin pk;	// Variable which represents the ProjectKorra plugin (optional, makes things easier).
	FileConfiguration config;	// Variable which represents the ProjectKorra configuration file (config.yml).
	
	public GoldLineInformation()	// Constructor, run to initialize the ability.
	{
		super("GoldLine");	// Make this ability's name "GoldLine".
	}
	
	public String getDescription()	// This is the message players will get when running "/pk help GoldLine".
	{
		return "Dear Blaze590, here is a simple ability which involves block manipulation.";	// The help message.
	}
	
	public String getAuthor()	// This returns the name of whoever made this ability.
	{
		return "Carbogen";	// That's me!
	}
	
	public String getVersion()	// Returns the version of this ability. Make sure you update this with every new release!
	{
		return "v1.0.0";	// First release.
	}
	
	public String getElement()	// Get this ability's element.
	{
		return Element.Earth.toString();	// This is an earthbending ability.
	}
	
	public boolean isShiftAbility()	// Is this a "shift ability"?
	{
		return false;	// Nope.
	}

	public boolean isHarmlessAbility()	// Is this ability harmless?
	{
		return false;	// Nope.
	}
	
	public void onThisLoad()	// This is run when PK starts up and finds this ability...
	{
		loadConfig();	// Runs this classe's 'loadConfig()' method.
		config.addDefault("ExtraAbilities.Carbogen.GoldLine.range", 8);	// Adds a variable to the config...
																		// name: range, value: 8 (blocks).
		config.addDefault("ExtraAbilities.Carbogen.GoldLine.cleanup", 4000); // Adds a variable to the config...
																		// name: cleanup, value: 4000 (milliseconds).
		pk.saveConfig();	// Tells ProjectKorra to save the changes made to the configuration file.
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new GoldLineListener(), ProjectKorra.plugin);
										// Register the listener for this ability with ProjectKorra.
		
		ProjectKorra.plugin.getServer().getPluginManager().addPermission(new GoldLinePermissions().glDefault);
										// Sets a permission for our ability.

		ProjectKorra.plugin.getServer().getPluginManager().getPermission("bending.ability.GoldLine")
					.setDefault(PermissionDefault.TRUE);
										// Make our permission available to the default group (TRUE).
		
		GoldLineListener.manageGoldLine();	// Make our runnable and set its schedule (look inside our Listener class).
		ProjectKorra.log.info(getName()+" by "+getAuthor()+" has been loaded.");	// Output a message to the console.
	}
	
	public void loadConfig()
	{
		pk = ProjectKorra.plugin;	// This class's 'pk' variable is now ProjectKorra.
		config = pk.getConfig();	// Set our 'config' variable to ProjectKorra's config file.
	}
	
	public void stop()
	{
		ProjectKorra.plugin.getServer().getPluginManager().removePermission(new GoldLinePermissions().glDefault);
										// Remove our ability's permission.
	}
}
