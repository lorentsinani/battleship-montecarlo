package mcts;

/**
 * Class representing a Field in a matrix
 */
public class Field {
  public Field(int inRow, int inCol) {
    row = inRow;
    col = inCol;
  }

  public int row;
  public int col;
}