package com.priceline.chutes;

import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Game {
    private GameConfig config;
    private List<Player> players;

    public Game() {
        this.config = new GameConfig();
        this.players = config.initializePlayers();
    }

    // This function not unit tested, instead all the business logic is moved to playerTurn, which is unit tested
    public void playGame(){
        while(true){
            for (Player currentPlayer : players) {
                if (playerTurn(currentPlayer)) return;
            }
        }
    }

    public boolean playerTurn(Player currentPlayer) {
        int spinResult = config.spin();
        int movedPosition = currentPlayer.getPosition() + spinResult;
        int nextPosition = config.move(movedPosition);

        // This is just some fun output for the user
        if (nextPosition < movedPosition) {
            log.info("Player " + currentPlayer.getName() + " tried to move to position " + movedPosition + " but fell down a chute to position " + nextPosition);
        } else if (nextPosition > movedPosition) {
            log.info("Player " + currentPlayer.getName() + " moves to position " + movedPosition + " and found a ladder to position " + nextPosition);
        }

        if (nextPosition > config.boardSize){
            log.info("Player " + currentPlayer.getName() + " tried to move to space " + nextPosition);
        } else {
            log.info("Player " + currentPlayer.getName() + " is at position " + nextPosition);
            currentPlayer.setPosition(nextPosition);
            if (currentPlayer.getPosition() == config.boardSize) {
                log.info("The winner is player " + currentPlayer.getName());
                return true;
            }
        }

        return false;
    }

    // This function not unit tested
    public static void main(String[] args) {
        try {
            new Game().playGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
