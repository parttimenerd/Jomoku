#Jomoku - A simple Gomoku game written in Java

Gomoku is an abstract strategy board game. Also called Gobang or Five in a Row, it is traditionally played with Go pieces (black and white stones) on a go board (19x19 intersections)[attribution needed]; however, because once placed, pieces are not moved or removed from the board, gomoku may also be played as a paper and pencil game. This game is known in several countries under different names.
Black plays first, and players alternate in placing a stone of their color on an empty intersection. The winner is the first player to get an unbroken row of five stones horizontally, vertically, or diagonally.

(http://en.wikipedia.org/wiki/Gomoku)

The programm itself has no dependencies - but isn't built for speed, so it can be a bit slow on old hardware - it's built for fun...

#Usage
Just execute the JAR file on the console (for more informations and options use the argument `-help`).
Then the player being the black player or the player being the white player is prompted to type in the position he or she want's to place a stone on. The format of this input is "[column number]x[row number"].
The board being played on is also printed on the console after every player action.

Example:

       |   |   |   |   |   |   |   |   |   |   | 1 | 1 | 1 | 1 | 1
       | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 0 | 1 | 2 | 3 | 4
     0 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     1 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     2 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     3 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     4 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     5 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     6 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     7 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     8 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     9 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    10 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    11 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    12 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    13 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    14 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    BLACK Player: 4x4
       |   |   |   |   |   |   |   |   |   |   | 1 | 1 | 1 | 1 | 1
       | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 0 | 1 | 2 | 3 | 4
     0 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     1 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     2 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     3 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     4 |   |   |   |   | X |   |   |   |   |   |   |   |   |   |  
     5 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     6 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     7 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     8 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     9 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    10 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    11 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    12 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    13 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    14 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    WHITE Player: 4x5
       |   |   |   |   |   |   |   |   |   |   | 1 | 1 | 1 | 1 | 1
       | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 0 | 1 | 2 | 3 | 4
     0 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     1 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     2 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     3 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     4 |   |   |   |   | X |   |   |   |   |   |   |   |   |   |  
     5 |   |   |   |   | O |   |   |   |   |   |   |   |   |   |  
     6 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     7 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     8 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
     9 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    10 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    11 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    12 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    13 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  
    14 |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  

#TODO
- Improve opponent engine

#License

Copyright (C) 2012  Johannes Bechberger

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

<http://www.gnu.org/licenses/>
