import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to N Puzzle Solver! Provide your n value and the inputs serially. Press 0 to quit.");
        Scanner scn = new Scanner(System.in);
        while(scn.hasNextLine())
        {
            int n = Integer.parseInt(scn.nextLine());
            if(n==0)
                return;

            int m = (int) Math.sqrt(n+1);
            int [][] puzzle = new int[m][m];
            for (int i=0;i<m; i++) {
                int j=0;
                String[] inputs = scn.nextLine().split(" ");
                if (inputs.length != m) {
                    System.out.println("Invalid Input");
                    return;
                }
                for (String s : inputs) {
                    try {
                        puzzle[i][j++] = Integer.parseInt(s);
                    } catch (NumberFormatException ne) {
                        j--;
                        puzzle[i][j++] = 0;
                    }
                }
            }

            PuzzleNode board = new PuzzleNode(puzzle, m, null);
            if(board.isSolvable())
                System.out.println("Your Puzzle is Solvable!");
            else
                System.out.println("Your Puzzle is not Solvable!");
        }
    }
}