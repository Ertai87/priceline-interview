package com.priceline.chutes;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameTest {
    @Nested
    class ConstructorTests {
        @Test
        public void shouldCreateAConfigAndPlayers() {
            try(MockedConstruction<GameConfig> mockGameConfig = Mockito.mockConstruction(GameConfig.class, 
            (mock, context) -> {
                when(mock.initializePlayers()).thenReturn(new LinkedList<>());
            })) {
                new Game();
                assertEquals(1, mockGameConfig.constructed().size());
                verify(mockGameConfig.constructed().get(0), times(1)).initializePlayers();
            }
        }
    }
 
    @Nested
    class PlayerTurnTests {
        @Test
        public void shouldDoASimpleMove() {
            try(MockedConstruction<GameConfig> mockGameConfig = Mockito.mockConstruction(GameConfig.class, 
            (mock, context) -> {
                mock.boardSize = 100;
                mock.movementMap = new MovementMap(100, List.of());
                when(mock.move(anyInt())).thenReturn(1);
                when(mock.spin()).thenReturn(1);
            })) {
                Game tested = new Game();
                Player player = new Player("testplayer");
                boolean winning = tested.playerTurn(player);
                assertEquals(1, player.getPosition());
                assertFalse(winning);
                verify(mockGameConfig.constructed().get(0), times(1)).move(1);
            }
        }

        @Test
        public void shouldNotMoveOffTheBoard() {
            try(MockedConstruction<GameConfig> mockGameConfig = Mockito.mockConstruction(GameConfig.class, 
            (mock, context) -> {
                mock.boardSize = 10;
                mock.movementMap = new MovementMap(100, List.of());
                when(mock.move(anyInt())).thenReturn(11);
                when(mock.spin()).thenReturn(11);
            })) {
                Game tested = new Game();
                Player player = new Player("testplayer");
                boolean winning = tested.playerTurn(player);
                assertEquals(0, player.getPosition());
                assertFalse(winning);
                verify(mockGameConfig.constructed().get(0), times(1)).move(anyInt());
            }
        }

        @Test
        public void shouldMoveFromNonzeroPosition() {
            int currentPosition = 5;
            int stepsToMove = 1;

            try(MockedConstruction<GameConfig> mockGameConfig = Mockito.mockConstruction(GameConfig.class, 
            (mock, context) -> {
                mock.boardSize = 10;
                mock.movementMap = new MovementMap(100, List.of());
                when(mock.move(anyInt())).thenReturn(6);
                when(mock.spin()).thenReturn(stepsToMove);
            })) {
                Game tested = new Game();
                Player player = new Player("testplayer");
                player.setPosition(currentPosition);
                boolean winning = tested.playerTurn(player);
                assertEquals(6, player.getPosition());
                assertFalse(winning);
                verify(mockGameConfig.constructed().get(0), times(1)).move(currentPosition + stepsToMove);
            }
        }

        @Test
        public void shouldCalculateWinningMove() {
            try(MockedConstruction<GameConfig> mockGameConfig = Mockito.mockConstruction(GameConfig.class, 
            (mock, context) -> {
                mock.boardSize = 10;
                mock.movementMap = new MovementMap(100, List.of());
                when(mock.move(anyInt())).thenReturn(10);
                when(mock.spin()).thenReturn(10);
            })) {
                Game tested = new Game();
                Player player = new Player("testplayer");
                boolean winning = tested.playerTurn(player);
                assertEquals(10, player.getPosition());
                assertTrue(winning);
                verify(mockGameConfig.constructed().get(0), times(1)).move(10);
            }
        }
    }
}