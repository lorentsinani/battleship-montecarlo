package mcts;

import battleship.Grid;

/**
 * Class representing a matrix of chances
 * In this project it is used to represent the chances of a ship being on each of the matrix fields
 */
class ChanceMatrix {
  private float[][] grid = new float[Grid.GRID_DIMENSION][Grid.GRID_DIMENSION];

  /***
   * Analyzes this ChanceMatrix and returns the Field with the highest win chance
   * @return The Field with the highest win chance
   */
  Field bestField() {
    Field highestField = new Field(0, 0);

    for (int x = 0; x < Grid.GRID_DIMENSION; x++) {
      for (int y = 0; y < Grid.GRID_DIMENSION; y++) {
        if (grid[x][y] > grid[highestField.row][highestField.col]) {
          highestField = new Field(x, y);
        }
      }
    }

    return highestField;
  }

  public void divide(int divisor) {
    for (int x = 0; x < Grid.GRID_DIMENSION; x++) {
      for (int y = 0; y < Grid.GRID_DIMENSION; y++) {
        grid[x][y] /= divisor;
      }
    }
  }

  void incrementRowCol(Field field, float value) {
    grid[field.row][field.col] += value;
  }

  public void print() {
    for (int x = 0; x < Grid.GRID_DIMENSION; x++) {
      for (int y = 0; y < Grid.GRID_DIMENSION; y++) {
        System.out.print(grid[x][y]);
        System.out.print(" ");
      }
      System.out.println();
    }
  }
}
