Most of the changes made are documented in the code.  I don't normally do this when I write code, but I figured it would be easier than writing a long README.md file for review purposes.  So here's a long README.md file anyway which goes over most of the same things.

Changes to the code:

Basically, I noticed that the abstractions of the "Board" and "BoardSpace" were unnecessary.  It is not noteworthy to notice if a space is a chute or a ladder, only that there is some transformation on that space.  That mapping can easily be stored in a simple map rather than a complex data structure as was done in the provided code.  To retain the "Chutes & Ladders" feeling I added some logic in the game engine code to do some fun output if a chute or ladder is encountered, but that's just syntactic sugar on top of a clean and simple data structure.

The abstraction I chose to use was player-centric, rather than board-centric.  The board is simply a size (final space number) and a map of chutes and ladders.  The positioning of players on the board is driven in the Player POJO.  There is logic to determine that the player's position never jumps "off the board", but it is assumed that every space from 1 to the board size exists and so it never has to be explicitly mentioned in a "board" class, per se.

I believe I found a bug in the code, although I only found it after I had done significant surgery on the code so it may have been a bug I created (and then fixed) rather than a bug in the original code.  The bug is that, when a player tried to move off the board, there was a "break" statement in the inner for-loop which caused the for loop (which controls the player turn order) to start over.  Meaning every time any player tried to move off the board, player 1 would get an extra turn.  This is obviously unfair in favor of player 1, so I fixed that bug.

Features I added:

1) I made the board configurable.  The provided code provides a set board with a set size and set chutes and ladders.  I made all of that configurable, so you can have as big a board as you like, and whatever chutes and ladders configuration you want.  You can set this in src/main/resources/application.config.  Also, it is safe such that the program will error on start if the provided parameters are bad in any way (board too small, chutes and ladders pointing off the board, etc).  Also the spinner itself is also configurable and can "roll a die" of any size, rather than being locked at 6.

2) I made the player count configurable, so there can be any number of players.  The player count is also configured in the same place.  Player names were not added (and the existing ones were removed), left as a TODO; I decided I had more interesting features to add than spend time on that, but I left the code extensible so that feature could be added later if so desired.  Players are numbered 1 through N, rather than named.

3) I added additional output.  The existing application only said who wins, which is rather opaque.  It's not "fun" for the log-reader nor is it good for maintainability, as if there is a bug (such as the one described above), it would be hard to spot.  The logging I added will trace the progression of the whole game, including when chutes and ladders are used, so that it more resembles an actual game of chutes and ladders (or at least the log of such a game).

4) I compacted the Player POJO class into an actual POJO, using Lombok.  I don't like unit testing simple things, so I decided to add Lombok to that POJO class to avoid unit testing it.  Don't test other people's code (that is, code generated from libraries).

5) I extensively unit tested the code that needed to be tested, except for the MovementMap class, using JUnit and Mockito.  Honestly, I spent a lot of time on the features above and ran out of time to test that class.  But the other code is thoroughly unit tested, and is documented as to what/why some things are not unit tested.
