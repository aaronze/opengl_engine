package sagma.games.rtsServer;

public class CommandCodes {
	public final static String PACKET_START = "1"; // Start of the packet. AUTO. DO NOT USE MANUALLY.
	public final static String PACKET_END = "0"; // ALSO DO NOT USE MANUALLY. (unless your really clever and use it for flow-control in packets, you crazy person)
	
	public final static String PING = "2"; // Request for a OK packet in return
	public final static String OK = "OK"; // It's OK.
	
	public final static String AUTHENTICATION = "3"; // Username and Password. In that order.
	public final static String NEW_USER = "4";
	
	public final static String REQUEST_VERSION = "5"; // Request for version number
	public final static String VERSION = "6"; // Version number sent
	
	public final static String ERROR_GENERIC = "100";
	public final static String ERROR_BAD_AUTHENTICATION = "101"; // Bad username or password. Telling the user which one failed is a major security risk.
	public final static String ERROR_DENIAL_OF_SERVICE = "102"; // IP was blocked due to spamming the network in a (failed) attempt to hack. (AUTO)
	public final static String ERROR_UNEXPECTED_BEHAVIOUR = "103"; // Something might have gone wrong. Not sure. Multiple of these indicates interference.
	public final static String ERROR_USER_NOT_FOUND = "104"; // When a user attempts interaction with a user that quit unexpectedly.
	public final static String ERROR_MALFORMED_PACKET = "105"; // Packets ain't getting there right. Might indicate a hacking attempt.
	public final static String ERROR_INTERFERENCE = "106"; // Hacking detected through multiple UNEXPECTED BEHAVIOUR or MALFORMED PACKETS.
	
	public final static String ADMIN_GENERIC = "200"; // Generic admin action. Why is it here? Why not?
	public final static String ADMIN_KICK = "201"; // For kicking players to the main-screen, with notification and optional messages/reasons
	public final static String ADMIN_BAN = "202"; // For banning a specific player at an IP address. Exits to main screen, displays optional messages
	public final static String ADMIN_SELF_DESTRUCT = "203"; // Used for hackers. Exits and corrupts the game.
	public final static String ADMIN_NOTIFICATION = "204"; // Sends a subtle notification with given messages
	public final static String ADMIN_ANNOUNCEMENT = "205"; // Sends a less-subtle notification with given messages
	public final static String ADMIN_MESSAGE = "206"; // Sends a private-direct message that must be dealt with
	public final static String ADMIN_MUTE = "207"; // Removes a player's ability to chat. Optional notification message.
	public final static String ADMIN_PROMOTE = "208"; // Promotes a player to a given rank, or up one rank if left blank
	public final static String ADMIN_DEMOTE = "209"; // Demotes a player to a given rank, or down one rank if left blank
	public final static String ADMIN_USERNAME_CHANGE = "210"; // Alters a user's username, and refractors all changes on client-side
	public final static String ADMIN_PASSWORD_CHANGE = "211"; // Alters a user's password.
	public final static String ADMIN_EXIT = "212"; // Forces a user to immediately exit. No notification.
	public final static String ADMIN_ARE_YOU_THERE = "213"; // Prompts a user for a response. If no response is given within the given time-frame, user quits.
	public final static String ADMIN_IP_BLOCK = "214"; // Blocks all activity from a given IP address. Does NOT affect client.
	
	public final static String WARNING_GENERIC = "300";
	public final static String WARNING_PING = "301"; // Really bad ping times that are slowing the server. User is notified to take action.
	public final static String WARNING_LANGUAGE = "302"; // Bad language detected, user is warned. Report is auto-filed.
	public final static String WARNING_BAD_BEHAVIOUR = "303"; // Bad behaviour reported, user is warned. Report is auto-filed.
	
	public final static String PERMISSION_ADD = "400"; // Adds a user permission to do something
	public final static String PERMISSION_REMOVE = "401"; // Removes a user permission to do something
	public final static String PERMISSION_CLEAR = "402"; // Sets all permissions to default
	public final static String PERMISSION_REFRESH = "403"; // Forces permissions re-sync with server
	
	public final static String HELP_GENERIC = "500";
	public final static String HELP_BUG_REPORT = "501"; // Bug-report submitted
	public final static String HELP_PETITION = "502"; // Petition submitted
	public final static String HELP_URGENT = "503"; // Petition submitted with Urgent Flag set.
}
