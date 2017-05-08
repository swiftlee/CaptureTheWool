package net.jmdev.database;

import net.jmdev.util.TextUtils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.util.UUID;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/06/2017 | 18:25
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
public class CoinsDatabase {

    private static YamlConfiguration coinDatabase = new YamlConfiguration();

    public void load() {

        if (coinDatabase == null)
            coinDatabase = new YamlConfiguration();


        try {

            coinDatabase.load("plugins/CaptureTheWool/coinDatabase.yml");

        } catch (FileNotFoundException e1) {

            save();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void save() {

        try {

            coinDatabase.save("plugins/CaptureTheWool/coinDatabase.yml");

        } catch (Exception e1) {

            e1.printStackTrace();

        }

    }

    public void reload() {

        save();
        load();

    }

    public String getCoins(Player p) {

        String coins = "";
        String key = p.getUniqueId().toString();

        if (coinDatabase.contains(key)) {

            coins += (TextUtils.formatText("&7Coins: &b" + coinDatabase.getInt(key + ".coins") + "\n")).replace("null", "&b0");

            return coins;

        }

        return TextUtils.formatText("&7Coins: &b0");

    }

    public int getCoins(UUID u) {

        for (String key : coinDatabase.getKeys(true)) {

            if (key.equals(u.toString())) {

                return coinDatabase.getInt(u.toString() + ".coins");

            }

        }

        return 0;

    }

    public void addToPlayerValue(Player p, int amount) {

        boolean exists = false;
        String key = p.getUniqueId().toString();

        if (coinDatabase.contains(key)) {

            exists = true;

        }


        if (!exists) {

            coinDatabase.createSection(key);
            coinDatabase.createSection(key + ".coins");

            reload();

            addToPlayerValue(p, amount);

        } else {

            int currentAmount = coinDatabase.getInt(key + ".coins");

            coinDatabase.set(p.getUniqueId().toString() + ".coins",
                    currentAmount + amount);
            reload();

        }

    }

}
