package battleship;

import utils.Logger;

import static battleship.Location.HIT;

public class Grid {
  public static final int GRID_DIMENSION = 5;
  // Constants for number of rows and columns.
  public static final int NUM_ROWS = GRID_DIMENSION;
  public static final int NUM_COLS = GRID_DIMENSION;
  private Location[][] grid;
  private int points;

  public Grid() {
    if (NUM_ROWS > 26) {
      throw new IllegalArgumentException("ERROR! NUM_ROWS CANNOT BE > 26");
    }

    grid = new Location[NUM_ROWS][NUM_COLS];
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        Location tempLoc = new Location();
        grid[row][col] = tempLoc;
      }
    }
  }

  public Grid deepClone() {
    Grid result = new Grid();

    result.points = this.points;
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        result.grid[row][col] = this.grid[row][col].deepClone();
      }
    }

    return result;
  }
}