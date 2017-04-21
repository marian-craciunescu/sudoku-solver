
import java.util.Vector;


public class Cell {
	public int rowPosition;
	public int colPosition;
	public int value;
	public Constraint constraintOrizontal;
	public Constraint constraintVertical;
	
	public Cell(int row,int col){
		this.rowPosition=row;
		this.colPosition=col;
		this.value=Integer.MAX_VALUE;
		this.constraintOrizontal=Constraint.NONE;
		this.constraintVertical=Constraint.NONE;
	}
	
	public void parseValue() {
	}
	
}
