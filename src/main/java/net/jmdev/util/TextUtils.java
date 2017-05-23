package net.jmdev.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/06/2017 | 18:37
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
public class TextUtils {

    public static String formatText(String txt) {

        return ChatColor.translateAlternateColorCodes('&', txt);

    }

    public static Location parseLocation(String loc) {

        try {

            World world = Bukkit.getWorld(loc.split(",")[0]);
            double x = Double.valueOf(loc.split(",")[1]);
            double y = Double.valueOf(loc.split(",")[2]);
            double z = Double.valueOf(loc.split(",")[3]);
            float yaw = Float.valueOf(loc.split(",")[4]);
            float pitch = Float.valueOf(loc.split(",")[5]);

            return new Location(world, x, y, z, yaw, pitch);

        } catch (IndexOutOfBoundsException e) {

            World world = Bukkit.getWorld(loc.split(",")[0]);
            double x = Double.valueOf(loc.split(",")[1]);
            double y = Double.valueOf(loc.split(",")[2]);
            double z = Double.valueOf(loc.split(",")[3]);

            return new Location(world, x, y, z);

        }

    }

    public static String locationToString(Location loc, boolean yawPitch) {

        if (yawPitch) {

            return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();

        } else {

            return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();

        }

    }

    public static String formatTime(int seconds) {

        if (seconds >= 3600) {

            return (seconds / 3600) + ":" + ((seconds % 3600) <= 9 ? "0" + (seconds % 3600) + ":" + (seconds - 3600) : (seconds % 3600) + ":" + (seconds - 3600));

        } else if (seconds >= 60) {

            return (seconds / 60 + ":" + ((seconds % 60) <= 9 ? "0" + (seconds % 60) : (seconds % 60)));

        } else {

            return seconds + "";

        }

    }

    public static String getTimeUnit(int seconds) {

        if (seconds >= 3600) {

            return ((seconds / 3600) == 1 ? "hour" : "hours");

        } else if (seconds >= 60) {

            return ((seconds / 60) == 1 ? "minute" : "minutes");

        } else {

            return (seconds == 1 ? "second" : "seconds");

        }

    }

}
