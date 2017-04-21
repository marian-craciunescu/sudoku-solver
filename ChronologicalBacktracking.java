public class ChronologicalBacktracking {
    private static int MAX_ROW =11;
	static int recursionEntry = 0;


	public static void solve(SudokuTable t, int row, int col) {
		recursionEntry++;
		if (row >  MAX_ROW) {
			SudokuTable.displaySudokuBoard(t);
			System.exit(0);
			return;
		}
		if (t.cells[row][col].value != Integer.MAX_VALUE) {
			next(t, row, col);
		} else {
			for (int val = 1; val < 13; val++) {
				if (SudokuTable.isValidMove(t, row, col, val) == true) {
					t.cells[row][col].value = val;
					next(t, row, col);
				}
			}
			t.cells[row][col].value = Integer.MAX_VALUE;

		}
	}

	public static void next(SudokuTable t, int row, int col) {
		if (col < MAX_ROW) {
			solve(t, row, col + 1);
		} else
			solve(t, row + 1, 0);
	}
}
