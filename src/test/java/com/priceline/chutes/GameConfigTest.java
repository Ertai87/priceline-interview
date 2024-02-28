package com.priceline.chutes;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/* No constructor test in this class because the presence/absence of application.config will make the test
 * unreliable.  According to StackOverflow there is no way to nicely inject an app.properites file without
 * using Spring, and I'm choosing not to do that for simplicity reasons
 */

 /*
  * Class under test is mocked so as to allow mocking of fields and not calling the real constructor.
  */

@ExtendWith(MockitoExtension.class)
public class GameConfigTest {
    @Nested
    public class InitializePlayersTest {
        @Test
        public void isAbleToCreateAPlayer() {
            try(MockedConstruction<GameConfig> mockGameConfig = Mockito.mockConstruction(GameConfig.class, 
            (mock, context) -> {
                mock.numPlayers = 1;
                when(mock.initializePlayers()).thenCallRealMethod();
            })) {
                GameConfig tested = new GameConfig();
                List<Player> players = tested.initializePlayers();
                assertEquals(players.size(), 1);
                assertEquals(players.get(0).getPosition(), 0);
            }
        }

        @Test
        public void isAbleToCreateTheCorrectNumberOfPlayers() {
            Random rand = new Random();
            for (int i = 0; i < 3; i++) { // run the test 3 times with random values
                int numPlayers = rand.nextInt(10) + 1;
                try(MockedConstruction<GameConfig> mockGameConfig = Mockito.mockConstruction(GameConfig.class, 
                (mock, context) -> {
                    mock.numPlayers = numPlayers;
                    when(mock.initializePlayers()).thenCallRealMethod();
                })) {
                    GameConfig tested = new GameConfig();
                    List<Player> players = tested.initializePlayers();
                    assertEquals(players.size(), numPlayers);
                }
            }
        }
    }

    @Nested
    public class MoveTest {
        @Test
        public void returnsValueFromMovementMap() {
            int finalLocation = 1;

            try(MockedConstruction<GameConfig> mockGameConfig = Mockito.mockConstruction(GameConfig.class, 
            (mock, context) -> {
                when(mock.move(anyInt())).thenCallRealMethod();
                mock.movementMap = Mockito.mock(MovementMap.class);
                when(mock.movementMap.getFinalLocation(anyInt())).thenReturn(finalLocation);
            })) {
                GameConfig tested = new GameConfig();
                int position = tested.move(0);
                assertEquals(finalLocation, position);
                verify(tested.movementMap, times(1)).getFinalLocation(anyInt());
            }
        }
    }

    @Nested
    public class SpinTest {
        int randomNumber = 4; // https://xkcd.com/221/
        // But also we want to fix the random number for validation purposes

        int spinnerSize = 6;

        @Test
        public void returnsRandomNumberBasedOnSpinnerSize() {
            try(MockedConstruction<GameConfig> mockGameConfig = Mockito.mockConstruction(GameConfig.class, 
            (mock, context) -> {
                mock.spinnerSize = spinnerSize;
                when(mock.spin()).thenCallRealMethod();
                mock.random = Mockito.mock(Random.class);
                when(mock.random.nextInt(anyInt())).thenReturn(randomNumber);
            })) {
                GameConfig tested = new GameConfig();
                int spin = tested.spin();
                assertEquals(randomNumber + 1, spin);
                verify(tested.random, times(1)).nextInt(spinnerSize);
            }
        }
    }
}
