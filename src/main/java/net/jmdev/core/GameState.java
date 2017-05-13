package net.jmdev.core;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/12/2017 | 18:30
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
public enum GameState {

    LOBBY, STARTING, PLAYING, FINISHED;

    private static GameState gameState;

    public static GameState getGameState() {

        return gameState;

    }

    public static void setGameState(GameState gameState) {

        GameState.gameState = gameState;

    }

}
