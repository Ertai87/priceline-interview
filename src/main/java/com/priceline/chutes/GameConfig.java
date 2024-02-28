package com.priceline.chutes;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/*
 * Similar to the "Board" class from the original implementation.  However, the "board" does not actually need to exist.
 * The only thing that is required is some tracking of how big the board is, to make sure players don't fall off the edge,
 * and the mapping of where the special tiles lead.  So the actual "board" is just those 2 things and the rest is abstracted out.
 */

public class GameConfig {
    Random random = new Random();

    int numPlayers = 4;
    int boardSize = 100;
    int spinnerSize = 6;
    MovementMap movementMap = new MovementMap();

    public GameConfig() {
        Properties appConfig = new Properties();
        try {
            appConfig.load(new FileInputStream("./src/main/resources/application.config"));
            System.out.println("Found application config, using values found...");

            // Try to set config using appconfig, use default values on any error; there's probably a library for this but nothing interesting came up on Google
            try {
                boardSize = Integer.parseInt(appConfig.getProperty("board.size"));
            } catch (Exception e) {
                System.out.println("Board size config not found, using default...");
            }

            try {
                spinnerSize = Integer.parseInt(appConfig.getProperty("spinner.size"));
            } catch (Exception e) {
                System.out.println("Spinner size config not found, using default...");
            }

            try {
                numPlayers = Integer.parseInt(appConfig.getProperty("players.count"));
            } catch (Exception e) {
                System.out.println("Player count config not found, using default...");
            }

            try {
                // This logic is why I'm not using default values for the configuration; it's really hard to do it for this case and I'm keeping it consistent
                movementMap = new MovementMap(boardSize, Arrays.stream(appConfig.getProperty("board.specialtiles").split(",")).map(num -> Integer.parseInt(num.trim())).toList());
            } catch (Exception e) {
                // Default movement map has values up to 100, so the board size must be at least 100 to use that map
                if (boardSize < 100) throw new IllegalArgumentException("Board size must be at least 100 to use default movement map");
                System.out.println("Movement config not found, using default...");
            }

            if (boardSize < 1 || numPlayers < 1 || spinnerSize < 1) {
                throw new IllegalArgumentException("Configured values are not acceptable, please ensure the values make sense");
            }
        } catch (IOException e) {
            System.out.println("Config file not found, using default properties");
        }
    }

    // TODO: add support for player names in appconfig
    public List<Player> initializePlayers() {
        List<Player> players = new LinkedList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("" + (i + 1)));
        }
        return players;
    }

    public int move(int position) {
        return movementMap.getFinalLocation(position);
    }

    public int spin() {
        return random.nextInt(spinnerSize) + 1;
    }
}
