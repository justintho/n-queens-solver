import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class NQueens {

	public static void main(String[] args) {
		//Used for scanning user input
		Scanner scan = new Scanner(System.in);
		int algorithmChoice;
		
		//Declaring a variable to store the board's state
		String board = "";

		//Prints out introduction text
		printIntroduction();
		
		//Loops until user selects to exit program
		while (true) {
			//Prompts user for algorithm to use and exits program if user enters 3
			algorithmChoice = promptAlgorithm(scan);
			if (algorithmChoice == 5) {
				System.out.println("\nProgram terminated.");
				System.exit(0);
			}
			
			//Generates random board and prints it out
			board = generateBoard();
			
			//Solves board using specified algorithm
			if (algorithmChoice == 1 || algorithmChoice == 2)
				solveBoard(board, algorithmChoice);
			else
				solve1000Boards(algorithmChoice);
		}
	}
	
	//Prints introduction message
	public static void printIntroduction() {
		System.out.println("CS4200 N-Queens Problem (n = 8):");
		System.out.println("The goal of this program is to solve a randomly generated");
		System.out.println("N-Queens problem (n = 8) using two different algorithms.");
		System.out.println("Queens are represented by 'X' on the board, while '-' are");
		System.out.println("empty spaces. The program will print out the solution, if ");
		System.out.println("one was found, or simply the best solution it was able to ");
		System.out.println("come up with. It will also print out the time and search");
		System.out.println("cost (steps taken) that was required to compute that solution.");
		System.out.println();
	}
	
	//Prompts user for algorithm choice by using values 1-5.
	public static int promptAlgorithm(Scanner scan) {
		int algorithmChoice;
		System.out.println("Please select which algorithm to use: ");
		System.out.println("[1] Steepest-ascent hill climbing");
		System.out.println("[2] Min-conflicts");
		System.out.println("[3] Steepest-ascent hill climbing (1000 times)");
		System.out.println("[4] Min-conflicts (1000 times)");
		System.out.println("[5] Exit Program");
		System.out.print("Input: ");
		algorithmChoice = scan.nextInt();
		
		//Verifies input
		while (algorithmChoice < 1 || algorithmChoice > 5) {
			System.out.println("Please only enter values ranging from 1-5.");
			System.out.println("Please select which algorithm to use: ");
			System.out.println("[1] Steepest-ascent hill climbing");
			System.out.println("[2] Min-conflicts");
			System.out.println("[3] Steepest-ascent hill climbing (1000 times)");
			System.out.println("[4] Min-conflicts (1000 times)");
			System.out.println("[5] Exit Program");
			System.out.print("Input: ");
			algorithmChoice = scan.nextInt();
		}
		return algorithmChoice;
	}
	
	//Generates random board and prints it out
	//Each queen is randomly placed in its own column
	public static String generateBoard() {
		String board = "";
		Random rand = new Random();
		
		for (int i = 0; i < 8; i++)
			board += rand.nextInt(8);
		
		System.out.println();
		System.out.println("Board:");
		printBoard(board);
		return board;
	}
	
	//Solves the board using the specified algorithm
	//Returns Results object, which contains the number of conflicts, the time, and the cost
	//for the solve100Boards function to use.
	public static Results solveBoard(String board, int algorithmChoice) {
		//Declaring variables that all algorithms use
		String finalBoard = board;
		double startTime;
		double endTime;
		double time;
		int cost = 0;
		int numConflicts;
		
		//Starting timer
		startTime = System.nanoTime();
		
		//Checks if the board is already solved.
		numConflicts = scanConflicts(board);
		System.out.println("Number of Conflicts: " + numConflicts);
		if (numConflicts != 0) {
			//Uses steepest hill climbing search algorithm
			if (algorithmChoice == 1) {
				boolean betterExists = true;
				String betterBoard = "";
				
				while(betterExists) {
					//Checks all possible moves and returns the best move, if any.
					numConflicts = scanConflicts(board);
					betterBoard = steepestHillClimbing(board, numConflicts);
					
					//If a better state does not exist, exit the loop.
					if (board.equals(betterBoard)) {
						betterExists = false;
						finalBoard = betterBoard;
					}
					
					//If a better state exists, use that state
					if (betterExists) {
						cost++;
						board = betterBoard;
					}
				}
			}
			//Uses min-conflicts algorithm
			else {
				//Limit of iterations until deemed as failure
				int maxSteps = 500;
				
				for (int i = 0; i < maxSteps; i++) {
					//Checks if problem is solved
					numConflicts = scanConflicts(board);
					if (numConflicts == 0) 
						break;
					
					//Chooses random conflicting queen and makes best move with that queen.
					board = minConflicts(board, numConflicts);
					cost++;
				}
				//Stores final board configuration
				finalBoard = board;
			}
		}
		
		//Ends timer and computes time in ms.
		endTime = System.nanoTime();
		time  = (endTime - startTime) / 1000000.0;
		
		//Prints out results
		System.out.println();
		System.out.println("Solution: ");
		printBoard(finalBoard);
		System.out.println("Number of Conflicts: " + scanConflicts(finalBoard));
		System.out.println("Time: " + time + " ms");
		System.out.println("Cost: " + cost);
		System.out.println();
		
		return new Results(numConflicts, time, cost);
	}
	
	//Solves 100 randomly generate boards and prints the average time/cost
	//Prints average time/cost for all runs and also for successful runs only.
	public static void solve1000Boards(int algorithmChoice) {
		//Declaring variables for the 100 runs
		int runs = 1000;
		double sumTime = 0;
		int sumCost = 0;
		int successfulRuns = 0;
		double sumTimeSuccess = 0;
		int sumCostSuccess = 0;
		DecimalFormat format = new DecimalFormat("#0.0###");
		
		//Determines algorithm name for final output
		String algorithmName = "";
		if (algorithmChoice == 3)
			algorithmName = "Steepest Hill-Climbing Algorithm";
		else
			algorithmName = "Min-Conflicts Algorithm";
		
		//Determines which algorithm to use
		int algChoice = 0;
		if (algorithmChoice == 3)
			algChoice = 1;
		else
			algChoice = 2;
		
		//Performs 100 runs of the specified algorithm.
		for (int i = 0; i < runs; i++) {
			//Prints run count
			System.out.println("Run: " + (i + 1));
			System.out.println("---------------");
			
			//Generates random board and solves it using appropriate algorithm
			String board = generateBoard();
			Results results = solveBoard(board, algChoice);
			
			//Add time and cost to the appropriate sum variables
			sumTime += results.getTime();
			sumCost += results.getCost();
			
			//Add time and cost of successful runs to the appropriate sum variables
			if (results.getNumConflicts() == 0) {
				successfulRuns++; //increments number of successful runs
				sumTimeSuccess += results.getTime();
				sumCostSuccess += results.getCost();
			}
		}
		
		//Prints out the results
		System.out.println("Results after " + runs  + " random cases of " + algorithmName + ":");
		System.out.println("Success Rate: " + format.format(((successfulRuns / (double)runs) * 100)) + "%");
		System.out.println("Avg. Time: " + format.format((sumTime / runs)) + " ms");
		System.out.println("Avg. Cost: " + format.format((sumCost / (double)runs)));
		System.out.println("Avg. Time for Successful Runs: " + format.format((sumTimeSuccess / successfulRuns)) + " ms");
		System.out.println("Avg. Cost for Successful Runs: " + format.format((sumCostSuccess / (double)successfulRuns)));
		System.out.println();
	}
	
	//Counts the number of conflicts on the board
	//Only counts conflicts with other queens in front of current piece to avoid repeat counts
	//Does not need to count vertical conflicts since each queen starts in its own column and only moves vertically
	public static int scanConflicts(String board) {
		int numConflicts = 0;
		
		for (int i = 0; i < board.length(); i++) {
			for (int j = i + 1; j < board.length(); j++) {
				//Counts front horizontal conflicts
				if (Character.getNumericValue(board.charAt(j)) == Character.getNumericValue(board.charAt(i))) {
					numConflicts++;
				}
				//Counts front upper-diagonal conflicts
				else if ((Character.getNumericValue(board.charAt(i)) + (j-i)) == Character.getNumericValue(board.charAt(j))) {
					numConflicts++;
				}
				//Counts front lower-diagonal conflicts
				else if ((Character.getNumericValue(board.charAt(i)) - (j-i)) == Character.getNumericValue(board.charAt(j))) {
					numConflicts++;
				}
			}
		}
		
		return numConflicts;
	}
	
	//Returns the best move(randomly if multiple best moves exist) or the original board if no better move is found
	public static String steepestHillClimbing(String board, int numConflicts) {
		String bestMove = "";
		int lowestH = numConflicts;
		int h = 0;
		ArrayList<String> moves = new ArrayList<String>();
		StringBuilder sb;
		
		//Tries each possible move and stores the best moves in an ArrayList
		for (int i = 0; i < board.length(); i++) {
			sb = new StringBuilder(board);
			for (int j = 0; j < board.length(); j++) {
				sb.setCharAt(i, Character.forDigit(j, 10));
				h = scanConflicts(sb.toString());
				if (h < lowestH) {
					lowestH = h;
					moves = new ArrayList<String>();
					moves.add(sb.toString());
				}
				else if (h == lowestH) {
					moves.add(sb.toString());
				}
			}
		}
		
		//If no move is better, return the original board.
		if (lowestH == numConflicts)
			return board;
		//Else randomly returns one of the best moves.
		else {
			if (moves.size() > 1) {
				Random rand = new Random();
				bestMove = moves.get(rand.nextInt(moves.size()));
			}
			else
				bestMove = moves.get(0);
		}
		
		return bestMove;
	}
	
	//Randomly select a conflicting queen column and return the best move for that queen.
	public static String minConflicts(String board, int numConflicts) {
		String newBoard = board;
		String bestMove = "";
		Random rand = new Random();
		int[] conflictingQueens;
		int selectedQueen;
		StringBuilder sb;
		int h;
		int lowestH = numConflicts;
		ArrayList<String> moves = new ArrayList<String>();
		
		//Determines which queens are conflicting and randomly selects one.
		conflictingQueens = attackingQueens(newBoard);
		selectedQueen = conflictingQueens[rand.nextInt(conflictingQueens.length)];
			
		//Tries to move the selected queen to each row and creates an ArrayList of the best move(s).
		for (int i = 0; i < board.length(); i++) {
			sb = new StringBuilder(board);
			sb.setCharAt(selectedQueen, Character.forDigit(i, 10));
			h = scanConflicts(sb.toString());
			if (h < lowestH) {
				lowestH = h;
				moves = new ArrayList<String>();
				moves.add(sb.toString());
			}
			else if (h == lowestH) {
				moves.add(sb.toString());
			}
		}
		
		//Randomly returns one of the best moves
		if (moves.size() > 1)
			bestMove = moves.get(rand.nextInt(moves.size()));
		else
			bestMove = moves.get(0);
		
		return bestMove;
	}
	
	//Returns an array of int values which indicate which queens are conflicting
	public static int[] attackingQueens(String board) {
		boolean[] conflictingQueens = new boolean[8];
		ArrayList<Integer> results = new ArrayList<Integer>();
		
		for (int i = 0; i < board.length(); i++) {
			for (int j = i + 1; j < board.length(); j++) {
				//Checks front horizontal conflicts
				if (Character.getNumericValue(board.charAt(j)) == Character.getNumericValue(board.charAt(i))) {
					conflictingQueens[i] = true;
					conflictingQueens[j] = true;
				}
				//Checks front upper-diagonal conflicts
				else if ((Character.getNumericValue(board.charAt(i)) + (j-i)) == Character.getNumericValue(board.charAt(j))) {
					conflictingQueens[i] = true;
					conflictingQueens[j] = true;
				}
				//Checks front lower-diagonal conflicts
				else if ((Character.getNumericValue(board.charAt(i)) - (j-i)) == Character.getNumericValue(board.charAt(j))) {
					conflictingQueens[i] = true;
					conflictingQueens[j] = true;
				}
			}
		}
		
		//Adding the indexes of conflicting queens to results array list
		for (int i = 0; i < conflictingQueens.length; i++) {
			if (conflictingQueens[i] == true) {
				results.add(i);
			}
		}
		
		//Returns results as an array of primitive int values
		return results.stream().mapToInt(i -> i).toArray();
	}
	
	//Prints out board in a formatted manner
	public static void printBoard(String board) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (Character.getNumericValue(board.charAt(j)) == i)
					System.out.print("X ");
				else
					System.out.print("- ");
			}
			System.out.println();
		}
	}
	
	//Class used to store and pass results from solveBoard to solve100Boards functions.
	static class Results {
		//Variables
		private int numConflicts;
		private double time;
		private int cost;
		
		//Constructor
		public Results(int numConflicts, double time, int cost) {
			this.numConflicts = numConflicts;
			this.time = time;
			this.cost = cost;
		}
		
		//Getters
		public int getNumConflicts() {
			return numConflicts;
		}
		
		public double getTime() {
			return time;
		}
		
		public int getCost() {
			return cost;
		}
	}

}
