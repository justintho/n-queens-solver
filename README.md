# N Queens Solver

Overview: This project is an N Queens Puzzle Solver which uses two different algorithms, steepest-ascent hill climbing and min-conflicts, to solve the problem. An N queens puzzle is a puzzle in which the goal is to move the queens on the board so that no queen is able to attack each other in the least amount of moves. In this project, it is made evident that the success rate of the steepest-ascent hill climbing is, for the most part, much less efficient and successful than the min-conflicts algorithm at solving N Queens puzzles.

<h2>Sample Output</h2>

```
CS4200 N-Queens Problem (n = 8):
The goal of this program is to solve a randomly generated
N-Queens problem (n = 8) using two different algorithms.
Queens are represented by 'X' on the board, while '-' are
empty spaces. The program will print out the solution, if
one was found, or simply the best solution it was able to
come up with. It will also print out the time and search
cost (steps taken) that was required to compute that solution.

Please select which algorithm to use:
[1] Steepest-ascent hill climbing
[2] Min-conflicts
[3] Steepest-ascent hill climbing (1000 times)
[4] Min-conflicts (1000 times)
[5] Exit Program
Input: 1

Board:
- - - - - X - -
- - - - - - - -
- - - - - - - -
- - - - - - - X
X - - - - - - -
- - - X - - X -
- - - - X - - -
- X X - - - - -
Number of Conflicts: 5

Solution:
- - - - - X - -
- - - - - - - X
- X - - - - - -
- - - X - - - -
X - - - - - - -
- - - - - - X -
- - - - X - - -
- - X - - - - -
Number of Conflicts: 0

Time: 0.3491 ms
Cost: 4
