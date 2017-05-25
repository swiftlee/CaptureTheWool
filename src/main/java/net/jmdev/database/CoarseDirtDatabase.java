package net.jmdev.database;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/21/2017 | 16:20
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
public class CoarseDirtDatabase {

    private static YamlConfiguration dirtDatabase = new YamlConfiguration();

    public YamlConfiguration getYML() {

        return dirtDatabase;

    }

    public void load() {

        if (dirtDatabase == null) {

            dirtDatabase = new YamlConfiguration();
        }

        try {

            dirtDatabase.load("plugins/CaptureTheWool/dirtDatabase.yml");

        } catch (FileNotFoundException e1) {

            save();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void save() {

        try {

            dirtDatabase.save("plugins/CaptureTheWool/dirtDatabase.yml");

        } catch (Exception e1) {

            e1.printStackTrace();

        }

    }

    public void reload() {

        save();
        load();

    }

    public void addNewLocation(String loc, ArrayList<String> locList) {

        locList.add(loc);
        dirtDatabase.set("locations", locList);
        reload();

    }

}
