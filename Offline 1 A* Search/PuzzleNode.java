public class PuzzleNode {

    private int[][] puzzle = null;
    private int boardSize = 0;
    private int fromInitialCost;
    private int blankRow = 0;
    private int blankColumn = 0;
    private PuzzleNode prevState;
    private String move;

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int[][] getpuzzle() {
        return puzzle;
    }

    public void setpuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
    }

    public int getFromInitialCost() {
        return fromInitialCost;
    }

    public void setFromInitialCost(int fromInitialCost) {
        this.fromInitialCost = fromInitialCost;
    }

    public int getBlankRow() {
        return blankRow;
    }

    public void setBlankRow(int blankRow) {
        this.blankRow = blankRow;
    }

    public int getBlankColumn() {
        return blankColumn;
    }

    public void setBlankColumn(int blankColumn) {
        this.blankColumn = blankColumn;
    }

    public PuzzleNode getPrevState() {
        return prevState;
    }

    public void setPrevState(PuzzleNode prevState) {
        this.prevState = prevState;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public PuzzleNode(int[][] puzzle, int size, PuzzleNode prevState) {
        this.puzzle = puzzle;
        this.boardSize = size;
        int k = 0;

        if (prevState != null) {
            fromInitialCost = prevState.fromInitialCost + 1;
        } else {
            fromInitialCost = 0;
        }
    }

    public int getInverseCount(int[] arr) {
        int inverseCount = 0;
        for (int i = 0; i < boardSize*boardSize; i++) {
            for (int j = i + 1; j < boardSize*boardSize; j++) {
                if (arr[i] != 0 && arr[j] != 0 && arr[j] < arr[i]) {
                    inverseCount++;
                }
            }
        }
        return inverseCount;
    }

    public int blankFinder() {
        for (int i = boardSize - 1; i >= 0; i--) {
            for (int j = boardSize - 1; j >= 0; j--) {
                if (puzzle[i][j] == 0) {
                    this.blankRow = i;
                    this.blankColumn = j;
                    return boardSize - i;
                }
            }
        }
        return -1;
    }

    public boolean isSolvable() {
        int arr[];
        arr = new int[boardSize * boardSize];
        int k = 0;
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                arr[k++] = puzzle[i][j];

        int inversionCount = getInverseCount(arr);
        if (boardSize*boardSize % 2 == 1)
            return inversionCount % 2 == 0;
        else {
            int blank = blankFinder();
            if (blank % 2 == 1)
                return inversionCount % 2 == 0;
            else
                return inversionCount % 2 == 1;
        }
    }

    public PuzzleNode neighbourGenerator(String action) {
        int[][] newBoard = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                newBoard[i][j] = puzzle[i][j];
            }
        }

        if (action.equalsIgnoreCase("right")) {
            newBoard[blankRow][blankColumn] = puzzle[blankRow][blankColumn - 1];
            newBoard[blankRow][blankColumn - 1] = 0;

            PuzzleNode newNode = new PuzzleNode(newBoard, boardSize, this);
            newNode.setMove("right");
            newNode.setBlankColumn(this.blankColumn - 1);
            newNode.setBlankRow(this.blankRow);

            return newNode;

        } else if (action.equalsIgnoreCase("left")) {
            newBoard[blankRow][blankColumn] = puzzle[blankRow][blankColumn + 1];
            newBoard[blankRow][blankColumn + 1] = 0;

            PuzzleNode newNode = new PuzzleNode(newBoard, boardSize, this);
            newNode.setMove("left");
            newNode.setBlankColumn(this.blankColumn + 1);
            newNode.setBlankRow(this.blankRow);

            return newNode;

        } else if (action.equalsIgnoreCase("up")) {
            newBoard[blankRow][blankColumn] = puzzle[blankRow + 1][blankColumn];
            newBoard[blankRow + 1][blankColumn] = 0;

            PuzzleNode newNode = new PuzzleNode(newBoard, boardSize, this);
            newNode.setMove("up");
            newNode.setBlankColumn(this.blankColumn);
            newNode.setBlankRow(this.blankRow + 1);

            return newNode;
        } else {
            newBoard[blankRow][blankColumn] = puzzle[blankRow - 1][blankColumn];
            newBoard[blankRow - 1][blankColumn] = 0;

            PuzzleNode newNode = new PuzzleNode(newBoard, boardSize, this);
            newNode.setMove("down");
            newNode.setBlankColumn(this.blankColumn);
            newNode.setBlankRow(this.blankRow - 1);
            return newNode;
        }
    }
}