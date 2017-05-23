package net.jmdev.core;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/13/2017 | 21:48
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
public enum WoolStatus {

    IN_SPAWN, TAKEN;

    private static WoolStatus redStatus;
    private static WoolStatus blueStatus;

    public static WoolStatus getRedStatus() {

        return redStatus;

    }

    public static void setRedStatus(WoolStatus redStatus) {

        WoolStatus.redStatus = redStatus;

    }

    public static WoolStatus getBlueStatus() {

        return blueStatus;

    }

    public static void setBlueStatus(WoolStatus blueStatus) {

        WoolStatus.blueStatus = blueStatus;

    }

    @Override
    public String toString() {

        if (this == WoolStatus.IN_SPAWN) return "In Spawn";
        else if (this == WoolStatus.TAKEN) return "Taken";
        else return "wot";

    }

}
