
// functia intoarce false daca ultima completare duce la blocare - implementarea cautarilor prospective
bool propagate_constraints(SudokuBoard& board, int row, int col)
{
	/* Atunci cand facem o completare, domeniul unui subset de celule de pe tabla
	 * (linie, coloana, regiune) se micsoreaza cu o valoare
	 *
	 * In cazul particular al problemei Sudoku clasice, atata timp cat intre oricare
	 * doua celule de pe tabla care pot intra in conflict exista macar o solutie de
	 * compatibiliate, nu putem propaga nimic. 
	 *
	 * Propagarea are loc in momentul in care obtinem celule care au o singura varianta
	 * de completare, iar completarea lor triviala duce la reducerea optiunilor pentru 
	 * alte celule... aceste constrangeri se *propaga* in lant.
          */
	bool in_queue[9][9] = { 0 };
	std::deque< std::pair<int,int> > q;

	// adauga in coada celulele de pe aceeasi linie/coloana/regiune cu (row, col)
	for (int i = 0; i < 9; i++)
		for (int j = 0; j < 9; j++)
			if (board.is_empty(i,j) && (i == row || j == col || (i/3 == row/3 && j/3 == col/3))){
				q.push_back(std::pair<int,int>(i,j));
				in_queue[i][j] = true;
			}

	while (!q.empty()){
		// scoate o variabila din coada
		std::pair<int,int> cell = q.front();
		in_queue[cell.first][cell.second] = false;
		q.pop_front();

		if (board.impossible(cell.first, cell.second)){
			return false;
		}
		
		/* daca cell are domeniu de dimensiune 1, completeaza-l direct cu valoarea 
		 * si adauga in coada celulele afectate la randul lor de cell (si care nu sunt deja in coada)
              */
		 int to_fill = board.unique_possibility(cell.first, cell.second);
		 if (to_fill){
		 	board.put(cell.first, cell.second, to_fill);
	 		for (int i = 0; i < 9; i++)
				for (int j = 0; j < 9; j++)
					if (board.is_empty(i,j) && in_queue[i][j] == false && (i == cell.first || j == cell.second || (i/3 == cell.first/3 && j/3 == cell.second/3))){
						q.push_back(std::pair<int,int>(i,j));
						in_queue[i][j] = true;
					}
		 }
	}
	return true;
}

void backtracking(SudokuBoard& board, int row, int col)
{
	static bool found_solution = false;
	static int recursions = 0;

	// daca e solutie, afiseaz-o si iesi
	if (row == 9 || board.is_done()){
		std::cout << board;
		std::cout << "Numar apeluri recursive: " << recursions << "\n";
		found_solution = true;
		return;
	}

	if (board.is_empty(row, col)){
		// daca la (row, col) nu e completat, atunci incercam sa completam
		SudokuBoard next;

		// actualizeaza numarul de apeluri recursive
		recursions++;

		for (int i = 1; i <= 9 && !found_solution; i++){
			if (board.allows(row, col, i)){
				next = board;
				next.put(row, col, i);
				// apeleaza intai propagarea constrangerilor
				if (propagate_constraints(board, row, col)){
					backtracking(next, row+(col+1)/9, (col+1)%9);
				}
			}
		}
	} else {
		// daca e completat deja, treci mai departe
		backtracking(board, row+(col+1)/9, (col+1)%9);
	}
}
