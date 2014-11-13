package ca.carbogen.tutorial.blaze590.goldline;

import org.bukkit.permissions.Permission;

public class GoldLinePermissions 
{
	public Permission glDefault;	// The default permission, as per the PK ability tutorial.
	
	public GoldLinePermissions()
	{
		super();	// Required, not sure why...
		glDefault = new Permission("bending.ability.GoldLine");	// Define 'glDefault' as "bending.ability.GoldLine".
	}
}
