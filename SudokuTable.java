import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.naming.NoInitialContextException;

public class SudokuTable {
    static final int N = 12;
    static final int FILE_ROW_NUMBER = 23;
    Cell[][] cells;

    public SudokuTable() {
        this.cells = new Cell[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Cell c = new Cell(i, j);
                this.cells[i][j] = c;
            }
        }

        // parseInputFile("sudoku.in");
        parseInputFile("new.in");
    }

    public static int convertCharToInt(char c) {
        if (c > '0' && c <= '9') {
            return c - '0';
        } else if (c == 'A' || c == 'a') {
            return 10;
        } else if (c == 'B' || c == 'b') {
            return 11;
        } else if (c == 'C' || c == 'c') {
            return 12;
        }
        return Integer.MAX_VALUE;
    }

    public static String convertIntToChar(int v) {
        String st = "";
        if (v > 0 && v <= 9) {
            st += v;
        } else if (v == 10) {
            st += 'A';
        } else if (v == 11) {
            st += 'B';
        } else if (v == 12) {
            st += 'C';
        } else if (v == Integer.MAX_VALUE) {
            st += 'X';
        }

        return st;
    }

    public void parseInputFile(String filename) {

        File file = new File(filename);

        try {

            Scanner scanner = new Scanner(file);
            int currentLineIndex = 0;
            int row = 0;
            while (currentLineIndex < FILE_ROW_NUMBER - 1) {

                String valueLine = scanner.nextLine();
                String constraintLine = scanner.nextLine();
                currentLineIndex += 2;
                int col = 0;
                for (int i = 0; i < valueLine.length(); i++) {
                    char val = valueLine.charAt(i);
                    char plusValue = ' ';
                    if (i < 22) {
                        plusValue = valueLine.charAt(i + 1);
                    }

                    cells[row][col].value = convertCharToInt(val);
                    // System.out.println("[" + row + "," + col + "] = "
                    // + cells[row][col].value);
                    if (plusValue == '|') {
                        cells[row][col].constraintOrizontal = Constraint.isLeft;
                        cells[row][col + 1].constraintOrizontal = Constraint.isRigth;

                    }
                    if (i % 2 == 0)
                        col++;
                }

                col = 0;
                for (int i = 0; i < constraintLine.length(); i += 2) {
                    char c = constraintLine.charAt(i);
                    if (c == '-') {
                        cells[row][col].constraintVertical = Constraint.isUp;
                        if (cells[row][col].constraintVertical == Constraint.isUp)
                            // System.out.println("[" + row + "," + col
                            // + "] = Constraint.isUp ");

                            cells[row + 1][col].constraintVertical = Constraint.isDown;
                    }
                    col++;
                }
                row++;
            }
            // read the last line without any modification
            String valueLine = scanner.nextLine();
            int col = 0;

            for (int i = 0; i < valueLine.length(); i++) {
                char val = valueLine.charAt(i);
                char plusValue = ' ';
                if (i < 22) {
                    plusValue = valueLine.charAt(i + 1);
                }
                cells[11][col].value = convertCharToInt(val);

                if (plusValue == '|') {
                    cells[11][col].constraintOrizontal = Constraint.isLeft;
                    cells[11][col + 1].constraintOrizontal = Constraint.isRigth;

                }
                if (i % 2 == 0)
                    col++;
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void displaySudokuBoard(SudokuTable t) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                System.out.print(convertIntToChar(t.cells[i][j].value) + " ");

                if (t.cells[i][j].constraintOrizontal == Constraint.isLeft) {
                    System.out.print("|");
                } else
                    System.out.print(" ");

            }
            System.out.println();
            for (int j = 0; j < N; j++) {
                if (t.cells[i][j].constraintVertical == Constraint.isUp) {
                    System.out.print("- ");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    /**
     * check if num is contained in the unit rectangle of 3X4 cell
     *
     * @param t   sudokuTable current sudokuBoard Configuration
     * @param row current row in which we search
     * @param col current col in which we search
     * @param num value to to be searched
     * @return true iff num is not contained false if num is already placed in
     * that rectangle
     */
    public static boolean checkBoxForValue(SudokuTable t, int row, int col,
        int num) {
        int r, c;
        row = (row / 3) * 3;
        col = (col / 4) * 4;
        for (r = 0; r < 3; r++)
            for (c = 0; c < 4; c++)
                if (t.cells[row + r][col + c].value == num)
                    return false;
        return true;
    }

    /**
     * @return the number of cells that are completed in a box.
     */
    public static int getNumberOfCompletedCellsInBox(SudokuTable t, int row,
        int col) {
        int MarkedCells = 0;
        int r, c;
        row = (row / 3) * 3;
        col = (col / 4) * 4;
        for (r = 0; r < 3; r++)
            for (c = 0; c < 4; c++)
                if (t.cells[row + r][col + c].value != Integer.MAX_VALUE)
                    MarkedCells++;
        return MarkedCells;

    }

    /**
     * @return a Pair<Int,Int> which means the start of a box(I.E.
     * (0,0),(3,0),(6,0),(6,8)....etc
     */
    public static Pair getBoxWithMaxMarkedCells(SudokuTable t) {
        Pair result = new Pair();
        int maxMarkedCell = Integer.MIN_VALUE;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int max = getNumberOfCompletedCellsInBox(t, i * 3, j * 4);
                if (max > maxMarkedCell && max != 12) {
                    result.first = i * 3;
                    result.second = j * 4;
                }
            }
        }

        return result;
    }

    public boolean isSolved() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.cells[i][j].value == Integer.MAX_VALUE) {
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * check if num is contained in the row or column
     *
     * @param t   sudokuTable current sudokuBoard Configuration
     * @param row current row in which we search
     * @param col current col in which we search
     * @param val value to to be searched
     * @return true if val is not contained false if val is already placed in
     * that rectangle
     */
    public static boolean checkRowAndColumnForValue(SudokuTable t, int row,
        int col, int val) {

        for (int i = 0; i < N; i++) {
            if (t.cells[row][i].value == val || t.cells[i][col].value == val) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkForConstraints(SudokuTable t, int row, int col,
        int val) {
        if (t.cells[row][col].constraintOrizontal == Constraint.isLeft) {
            if (val == (t.cells[row][col + 1].value - 1))
                return false;
        } else if (t.cells[row][col].constraintOrizontal == Constraint.isRigth
            && col != 0) {
            if (val == (t.cells[row][col - 1].value + 1))
                return false;
        }

        if (t.cells[row][col].constraintVertical == Constraint.isUp) {
            if (val == (t.cells[row + 1][col].value - 1))
                return false;
        } else if (t.cells[row][col].constraintVertical == Constraint.isDown
            && row != 0) {
            if (val == (t.cells[row - 1][col].value + 1))
                return false;
        }

        return true;

    }

    /**
     * @return true if val is a valid number for the (row col) cell false if val
     * is not good
     */
    public static boolean isValidMove(SudokuTable t, int row, int col, int val) {
        if (checkRowAndColumnForValue(t, row, col, val) == false)
            return false;

        if (checkBoxForValue(t, row, col, val) == false)
            return false;

        if (checkForConstraints(t, row, col, val) == false)
            return false;

        return true;
    }

    /**
     * @return 0 if there are more than one or none posibility for the cell a
     * digit from 1 to 12 signifying the actual posibility
     */
    public int uniquePossibility(SudokuTable t, int row, int col) {
        int noPosibility = 0;
        int result = 0;
        for (int val = 1; val < 13; val++) {
            // if we can put val in the cell add it to the list
            if (SudokuTable.isValidMove(t, row, col, val) == true
                && t.cells[row][col].value == Integer.MAX_VALUE) {
                noPosibility++;
                result = val;
                if (noPosibility > 1) {
                    return 0;
                }
            }
        }
        return result;
    }

    /**
     * @return true if there are no more possible values
     * false elsewhere
     */
    public boolean isImpossiblePosition(SudokuTable t, int row, int col) {
        for (int val = 1; val < 13; val++) {
            // if we can put val in the cell add it to the list
            if (SudokuTable.isValidMove(t, row, col, val) == true) {
                return false;
            }
        }
        return true;

    }

    public static void main(String[] args) {
        SudokuTable t = new SudokuTable();
        displaySudokuBoard(t);
        System.out.println("Solving using chronological backtracking  .....\n");
        // ChronologicalBacktracking.solve(t, 0, 0);
        System.out.println("Solving using prospective backtracking  .....\n");
        // try {
        // System.setOut(new PrintStream(new File("output-file.txt")));
        // } catch (FileNotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        ProspectivBactracking.solve(t);
    }

}
