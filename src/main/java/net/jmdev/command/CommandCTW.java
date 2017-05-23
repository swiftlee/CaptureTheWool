package net.jmdev.command;

import net.jmdev.CaptureTheWool;
import net.jmdev.core.BungeeMode;
import net.jmdev.util.TextUtils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/13/2017 | 15:57
 * __________________
 *
 *  [2016] J&M Plugin Development
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of J&M Plugin Development and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to J&M Plugin Development
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from J&M Plugin Development.
 */
public class CommandCTW implements CommandExecutor {

    private CaptureTheWool plugin;

    public CommandCTW(CaptureTheWool plugin) {

        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (BungeeMode.getMode() == BungeeMode.ON && cmd.getName().equalsIgnoreCase("ctw")) { //if bungeeMode is on and cmd name is ctw

            if (sender instanceof Player && sender.hasPermission("ctw.cmd")) { //if command sender is a player

                Player p = (Player) sender;

                if (args.length > 0 && args.length < 3) { //if args is greater than 0 and less than 3

                    if (!args[0].equalsIgnoreCase("help") && args.length == 1) {

                        p.sendMessage(TextUtils.formatText("&cInvalid argument length. Try &7/ctw help &cfor more information."));
                        return true;

                    }

                    if (args[0].equalsIgnoreCase("setspawn")) { //if 1st arg is setspawn

                        if (args[1].equalsIgnoreCase("blue")) { //if 2nd arg is blue

                            if (p.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("game.worldName"))) {

                                double x = p.getLocation().getX();
                                double y = p.getLocation().getY();
                                double z = p.getLocation().getZ();
                                float yaw = p.getLocation().getYaw();
                                float pitch = p.getLocation().getPitch();

                                String location = p.getWorld().getName() + "," + x + "," + y + "," + z + "," + yaw + "," + pitch;

                                ArrayList<String> currentLocations = new ArrayList<>();
                                currentLocations.addAll(plugin.getConfig().getStringList("spawnLocations.blueTeam"));
                                currentLocations.add(location);
                                plugin.getConfig().set("spawnLocations.blueTeam", currentLocations);

                                p.sendMessage(TextUtils.formatText("&aSet player spawn location for &9BLUE &ateam."));
                                plugin.saveConfig();
                                plugin.reloadConfig();

                            } else { //if player not in game world

                                p.sendMessage(TextUtils.formatText("&cYou are not in the right world to do this."));

                            }

                        } else if (args[1].equalsIgnoreCase("red")) { //if 2nd arg is red

                            if (p.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("game.worldName"))) {

                                double x = p.getLocation().getX();
                                double y = p.getLocation().getY();
                                double z = p.getLocation().getZ();
                                float yaw = p.getLocation().getYaw();
                                float pitch = p.getLocation().getPitch();

                                String location = p.getWorld().getName() + "," + x + "," + y + "," + z + "," + yaw + "," + pitch;

                                ArrayList<String> currentLocations = new ArrayList<>();
                                currentLocations.addAll(plugin.getConfig().getStringList("spawnLocations.redTeam"));
                                currentLocations.add(location);
                                plugin.getConfig().set("spawnLocations.redTeam", currentLocations);

                                p.sendMessage(TextUtils.formatText("&aSet player spawn location for &cRED &ateam."));
                                plugin.saveConfig();
                                plugin.reloadConfig();

                            } else { //if not in game world

                                p.sendMessage(TextUtils.formatText("&cYou are not in the right world to do this."));

                            }

                        } else if (args[1].equalsIgnoreCase("waitingroom")) {

                            if (p.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("lobby.worldName"))) { //if player in lobby world

                                String worldName = plugin.getConfig().getString("lobby.worldName");
                                double x = p.getLocation().getX();
                                double y = p.getLocation().getY();
                                double z = p.getLocation().getZ();
                                float yaw = p.getLocation().getYaw();
                                float pitch = p.getLocation().getPitch();

                                String location = worldName + "," + x + "," + y + "," + z + "," + yaw + "," + pitch;

                                plugin.getConfig().set("lobby.spawnLocation", location);

                                p.sendMessage(TextUtils.formatText("&aSet player spawn location for &6waitingroom&a."));
                                plugin.saveConfig();
                                plugin.reloadConfig();

                            } else { //if player not in lobby world

                                p.sendMessage(TextUtils.formatText("&cYou are not in the right world to do this."));

                            }

                        } else { //if 2nd arg isn't blue/red

                            p.sendMessage(TextUtils.formatText("&cInvalid argument: &7'" + args[1] + "'&c. Try &7/ctw <set(spawn/wool)> <blue/red/waitingroom>"));

                        }

                    } else if (args[0].equalsIgnoreCase("setwool")) { //if 1st arg is setwool


                        if (args[1].equalsIgnoreCase("blue")) { //if 2nd arg is blue

                            Block b = p.getTargetBlock((Set<Material>) null, 5);
                            double x = b.getX();
                            double y = b.getY();
                            double z = b.getZ();

                            String location = b.getWorld().getName() + "," + x + "," + y + "," + z;

                            plugin.getConfig().set("game.blueWoolLocation", location);

                            p.sendMessage(TextUtils.formatText("&aSet wool location for &9BLUE &ateam."));
                            plugin.saveConfig();
                            plugin.reloadConfig();

                        } else if (args[1].equalsIgnoreCase("red")) { //if 2nd arg is red

                            Block b = p.getTargetBlock((Set<Material>) null, 5);
                            double x = b.getX();
                            double y = b.getY();
                            double z = b.getZ();

                            String location = b.getWorld().getName() + "," + x + "," + y + "," + z;

                            plugin.getConfig().set("game.redWoolLocation", location);

                            p.sendMessage(TextUtils.formatText("&aSet wool location for &cRED &ateam."));
                            plugin.saveConfig();
                            plugin.reloadConfig();

                        }

                    } else if (args[0].equalsIgnoreCase("remove")) {

                        if (args[1].equalsIgnoreCase("bluespawn")) {

                            try {

                                ArrayList<String> locations = new ArrayList<>();
                                locations.addAll(plugin.getConfig().getStringList("spawnLocations.blueTeam"));
                                locations.remove(locations.size() - 1);
                                plugin.getConfig().set("spawnLocations.blueTeam", locations);

                                p.sendMessage(TextUtils.formatText("&aRemoved last added spawnpoint for &9BLUE &ateam."));
                                plugin.saveConfig();
                                plugin.reloadConfig();

                            } catch (IndexOutOfBoundsException e) {

                                p.sendMessage(TextUtils.formatText("&aNo locations to remove for &9BLUE &ateam."));

                            }

                        } else if (args[1].equalsIgnoreCase("redspawn")) {

                            try {

                                ArrayList<String> locations = new ArrayList<>();
                                locations.addAll(plugin.getConfig().getStringList("spawnLocations.redTeam"));
                                locations.remove(locations.size() - 1);
                                plugin.getConfig().set("spawnLocations.redTeam", locations);

                                p.sendMessage(TextUtils.formatText("&aRemoved last added spawnpoint for &cRED &ateam."));
                                plugin.saveConfig();
                                plugin.reloadConfig();

                            } catch (IndexOutOfBoundsException e) {

                                p.sendMessage(TextUtils.formatText("&aNo locations to remove for &cRED &ateam!"));

                            }

                        } else {

                            p.sendMessage(TextUtils.formatText("&cInvalid argument: &7'" + args[1] + "'&c. Try &7/ctw help &cfor more information."));

                        }

                    } else if (args[0].equalsIgnoreCase("help")) {

                        String allCommands = TextUtils.formatText("&aHere is a list of all available commands:");
                        String setspawn = TextUtils.formatText("&7- &6/ctw setspawn <blue/red/waitingroom>");
                        String setwool = TextUtils.formatText("&7- &6/ctw setwool <blue/red>");
                        String remove = TextUtils.formatText("&7- &6/ctw remove <bluespawn/redspawn>");
                        String finalMessage = "";

                        ArrayList<String> commands = new ArrayList<>();
                        commands.add(allCommands);
                        commands.add(setspawn);
                        commands.add(setwool);
                        commands.add(remove);

                        for (String s : commands)
                            finalMessage += s + "\n";

                        p.sendMessage(finalMessage);

                    } else { //if 1st arg isn't setspawn or setwool or remove or help

                        p.sendMessage(TextUtils.formatText("&cInvalid argument: &7'" + args[0] + "'&c. Try &7/ctw help &cfor more information."));

                    }

                } else { //if invalid argument length

                    p.sendMessage(TextUtils.formatText("&cInvalid argument length. Try &7/ctw help &cfor more information."));

                }

            } else { //if command sender is not a player

                sender.sendMessage(TextUtils.formatText("&cYou must be a player to execute command 'ctw'."));

            }

            return true;

        }

        return false;
    }

}
