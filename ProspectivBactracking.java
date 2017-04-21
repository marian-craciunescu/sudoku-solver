import java.util.ArrayList;


public class ProspectivBactracking {

	static int recursionEntry = 0;

	/**
	 * in this solution we use the prospection technique. So we use variable
	 * ordering which means that basically we check the cell with the least
	 * values posible.
	 * */
	public static void solve(SudokuTable t) {
		recursionEntry++;

		if (t.isSolved() == true) {
			System.out.println("---------------entry in recursion= "
					+ recursionEntry + "------------------------------");
			SudokuTable.displaySudokuBoard(t);
			System.exit(0);
			return;
		}
		// check for the
		int minPosibillity = Integer.MAX_VALUE, row = Integer.MAX_VALUE, col = Integer.MAX_VALUE;
		ArrayList<Integer> leastPossibleValues = null;
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 12; j++) {
				// iterate over posible values
				ArrayList<Integer> possibleValues = new ArrayList<Integer>();
				for (int val = 1; val < 13; val++) {
					// if we can put val in the cell add it to the list
					if (SudokuTable.isValidMove(t, i, j, val) == true
							&& t.cells[i][j].value == Integer.MAX_VALUE) {
						possibleValues.add(val);
					}
				}
				// new minimum posibilities...update the min and memorize the
				// cell possition
				if (possibleValues.size() > 0
						&& possibleValues.size() <= minPosibillity) {
					leastPossibleValues = possibleValues;
					minPosibillity = possibleValues.size();
					row = i;
					col = j;
				}
			}
		}
		// We have reached and invalid solution with no posibilty...just return
		// from recursivity
		if (minPosibillity >= 12) {
			return;
		}
		//enter recursivity for every possible solution
		for (int v : leastPossibleValues) {
			t.cells[row][col].value = v;
			solve(t);
		}
		t.cells[row][col].value = Integer.MAX_VALUE;
		return;

	}

}
