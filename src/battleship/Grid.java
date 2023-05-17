package battleship;

import utils.Logger;

import static battleship.Location.HIT;

public class Grid {
  public static final int GRID_DIMENSION = 5;
  // Constants for number of rows and columns.
  public static final int NUM_ROWS = Constants.GRID_DIMENSION;
  public static final int NUM_COLS = Constants.GRID_DIMENSION;
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
    // Mark a hit in this location by calling the markHit method
  // on the Location object.
  public void markHit(int row, int col) {
    grid[row][col].markHit();
    points++;
  }

  // Mark a miss on this location.
  public void markMiss(int row, int col) {
    grid[row][col].markMiss();
  }

  // Get the status of this location in the grid
  public int getStatus(int row, int col) {
    return grid[row][col].getStatus();
  }

  // Return whether or not there is a ship here
  public boolean hasShip(int row, int col) {
    return grid[row][col].hasShip();
  }

  // Return the number of rows in the Grid
  public int numRows() {
    return NUM_ROWS;
  }

  // Return the number of columns in the grid
  public int numCols() {
    return NUM_COLS;
  }

  public void printStatus() {
    generalPrintMethod(0);
  }

  public void printShips() {
    generalPrintMethod(1);
  }

  public void printCombined() {
    generalPrintMethod(2);
  }

  public boolean hasLost() {
    for (int x = 0; x < Grid.GRID_DIMENSION; x++) {
      for (int y = 0; y < Grid.GRID_DIMENSION; y++) {
        if (hasShip(x, y) && getStatus(x, y) != HIT) {
          return false;
        }
      }
    }
    return true;
  }

  public void addShip(Ship s) {
    int row = s.getRow();
    int col = s.getCol();
    int length = s.getLength();
    int dir = s.getDirection();

    if (!(s.isDirectionSet()) || !(s.isLocationSet()))
      throw new IllegalArgumentException("ERROR! Direction or Location is unset/default");

    // 0 - hor; 1 - ver
    if (dir == 0) {// Hortizontal
      for (int i = col; i < col + length; i++) {
        Logger.debug("row = " + row + "; col = " + i);

        grid[row][i].setShip(true);
        grid[row][i].setLengthOfShip(length);
        grid[row][i].setDirectionOfShip(dir);
      }
    } else if (dir == 1) {// Vertical
      for (int i = row; i < row + length; i++) {
        Logger.debug("row = " + row + "; col = " + i);

        grid[i][col].setShip(true);
        grid[i][col].setLengthOfShip(length);
        grid[i][col].setDirectionOfShip(dir);
      }
    }
  }

  // Type: 0 for status, 1 for ships, 2 for combined
  private void generalPrintMethod(int type) {
    System.out.println();
    // Print columns (HEADER)
    System.out.print("  ");
    for (int i = 1; i <= NUM_COLS; i++) {
      System.out.print(i + " ");
    }
    System.out.println();

    // Print rows
    int endLetterForLoop = (NUM_ROWS - 1) + 65;
    for (int i = 65; i <= endLetterForLoop; i++) {
      char theChar = (char) i;
      System.out.print(theChar + " ");

      for (int j = 0; j < NUM_COLS; j++) {
        if (type == 0) { //type == 0; status
          if (grid[switchCounterToIntegerForArray(i)][j].isUnguessed())
            System.out.print("- ");
          else if (grid[switchCounterToIntegerForArray(i)][j].checkMiss())
            System.out.print("O ");
          else if (grid[switchCounterToIntegerForArray(i)][j].checkHit())
            System.out.print("X ");
        } else if (type == 1) { //type == 1; ships
          if (grid[switchCounterToIntegerForArray(i)][j].hasShip()) {
            // System.out.print("X ");
            if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 2) {
              System.out.print("D ");
            } else if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 3) {
              System.out.print("C ");
            } else if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 4) {
              System.out.print("B ");
            } else if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 5) {
              System.out.print("A ");
            }
          } else if (!(grid[switchCounterToIntegerForArray(i)][j].hasShip())) {
            System.out.print("- ");
          }

        } else { // type == 2; combined
          if (grid[switchCounterToIntegerForArray(i)][j].checkHit())
            System.out.print("X ");
          else if (grid[switchCounterToIntegerForArray(i)][j].checkMiss())
            System.out.print("O ");
          else if (grid[switchCounterToIntegerForArray(i)][j].hasShip()) {
            if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 2) {
              System.out.print("D ");
            } else if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 3) {
              System.out.print("C ");
            } else if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 4) {
              System.out.print("B ");
            } else if (grid[switchCounterToIntegerForArray(i)][j].getLengthOfShip() == 5) {
              System.out.print("A ");
            }
          } else if (!(grid[switchCounterToIntegerForArray(i)][j].hasShip())) {
            System.out.print("- ");
          }
        }
      }
      System.out.println();
    }
  }

  public int switchCounterToIntegerForArray(int val) {
    if(val < 65 || val > 90)
      throw new IllegalArgumentException("Illegal argument value in switchCounterToIntegerForArray()");

    else return val - 65;
  }
}