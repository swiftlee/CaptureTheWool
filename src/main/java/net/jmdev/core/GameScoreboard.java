package net.jmdev.core;

import net.jmdev.CaptureTheWool;
import net.jmdev.util.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/13/2017 | 21:29
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
public class GameScoreboard {

    private static ScoreboardManager manager = Bukkit.getScoreboardManager();
    private static Scoreboard redBoard = manager.getNewScoreboard();
    private static Scoreboard blueBoard = manager.getNewScoreboard();

    public static void setupGameScoreboard(CaptureTheWool plugin) {

        Objective redObjective = redBoard.registerNewObjective("red", "dummy");
        redObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        redObjective.setDisplayName(TextUtils.formatText(plugin.getConfig().getStringList("scoreboard").get(0)));

        Team redTeam = redBoard.registerNewTeam("Red");
        redTeam.setPrefix(TextUtils.formatText("&c&lRed &r"));
        redTeam.setAllowFriendlyFire(false);

        for (UUID p : GameManager.redTeam) {

            redTeam.addEntry(Bukkit.getPlayer(p).getName());

            for (int i = 0; i < plugin.getConfig().getStringList("scoreboard").size(); i++) {

                if (i > 0) {

                    redObjective.getScore(TextUtils.formatText(plugin.getConfig().getStringList("scoreboard").get(i).replace("{teamColor}", "&cRed").replace("{woolStatus}", WoolStatus.getRedStatus().toString()).replace("{redCaptured}", String.valueOf(GameManager.redCaptured)).replace("{blueCaptured}", String.valueOf(GameManager.blueCaptured)))).setScore(plugin.getConfig().getStringList("scoreboard").size() - i);

                }

                if (i == plugin.getConfig().getStringList("scoreboard").size() - 1)
                    Bukkit.getPlayer(p).setScoreboard(redBoard);

            }

        }

        Objective blueObjective = blueBoard.registerNewObjective("blue", "dummy");

        blueObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        blueObjective.setDisplayName(TextUtils.formatText(plugin.getConfig().getStringList("scoreboard").get(0)));

        Team blueTeam = blueBoard.registerNewTeam("Blue");
        blueTeam.setPrefix(TextUtils.formatText("&9&lBlue &r"));
        blueTeam.setAllowFriendlyFire(false);

        for (UUID p : GameManager.blueTeam) {

            blueTeam.addEntry(Bukkit.getPlayer(p).getName());

            for (int i = 0; i < plugin.getConfig().getStringList("scoreboard").size(); i++) {

                if (i > 0) {

                    blueObjective.getScore(TextUtils.formatText(plugin.getConfig().getStringList("scoreboard").get(i).replace("{teamColor}", "&9Blue").replace("{woolStatus}", WoolStatus.getBlueStatus().toString()).replace("{redCaptured}", String.valueOf(GameManager.redCaptured)).replace("{blueCaptured}", String.valueOf(GameManager.blueCaptured)))).setScore(plugin.getConfig().getStringList("scoreboard").size() - i);

                }

                if (i == plugin.getConfig().getStringList("scoreboard").size() - 1)
                    Bukkit.getPlayer(p).setScoreboard(blueBoard);

            }

        }

    }

    public static void updateBoard(CaptureTheWool plugin) {

        redBoard.getEntries().stream().forEach(entry -> redBoard.getObjective("red").getScoreboard().resetScores(entry));
        blueBoard.getEntries().stream().forEach(entry -> blueBoard.getObjective("blue").getScoreboard().resetScores(entry));

        GameManager.blueTeam.stream().forEach(p -> {

            for (int i = 1; i < plugin.getConfig().getStringList("scoreboard").size(); i++) {

                blueBoard.getObjective("blue").getScore(TextUtils.formatText(plugin.getConfig().getStringList("scoreboard").get(i).replace("{teamColor}", "&9Blue").replace("{woolStatus}", WoolStatus.getBlueStatus().toString()).replace("{redCaptured}", String.valueOf(GameManager.redCaptured)).replace("{blueCaptured}", String.valueOf(GameManager.blueCaptured)))).setScore(plugin.getConfig().getStringList("scoreboard").size() - i);

                if (i == plugin.getConfig().getStringList("scoreboard").size() - 1)
                    Bukkit.getPlayer(p).setScoreboard(blueBoard);

            }

        });

        GameManager.redTeam.stream().forEach(p -> {

            for (int i = 1; i < plugin.getConfig().getStringList("scoreboard").size(); i++) {

                redBoard.getObjective("red").getScore(TextUtils.formatText(plugin.getConfig().getStringList("scoreboard").get(i).replace("{teamColor}", "&cRed").replace("{woolStatus}", WoolStatus.getRedStatus().toString()).replace("{redCaptured}", String.valueOf(GameManager.redCaptured)).replace("{blueCaptured}", String.valueOf(GameManager.blueCaptured)))).setScore(plugin.getConfig().getStringList("scoreboard").size() - i);

                if (i == plugin.getConfig().getStringList("scoreboard").size() - 1)
                    Bukkit.getPlayer(p).setScoreboard(redBoard);

            }

        });

    }

}
