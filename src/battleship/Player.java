package battleship;

import mcts.Field;

import java.util.ArrayList;
import java.util.List;

public class Player {
  // These are the lengths of all the ships.
  public static final int[] SHIP_LENGTHS = {2, 2, 3};
  public Ship[] ships;
  public String name;
  // The own grid
  // Contains HITs MISSes and location of SHIPS
  public Grid playerGrid;
  // The grid, that's shown to the opponent
  // Only contains HITs and MISSes
  public Grid oppGrid;

  public Player(String inName) {
    ships = new Ship[getNumOfShips()];
    for (int i = 0; i < getNumOfShips(); i++) {
      Ship tempShip = new Ship(SHIP_LENGTHS[i]);
      ships[i] = tempShip;
    }
    playerGrid = new Grid();
    oppGrid = new Grid();
    name = inName;
  }

  public static int getNumOfShips() {
    return SHIP_LENGTHS.length;
  }

  public Player deepClone() {
    Player result = new Player(name);
    for (int i = 0; i < getNumOfShips(); i++) {
      result.ships[i] = this.ships[i].deepClone();
    }
    result.playerGrid = this.playerGrid.deepClone();
    result.oppGrid = this.oppGrid.deepClone();
    return result;
  }

  public int numOfShipsLeft() {
    int counter = getNumOfShips();
    for (Ship s : ships) {
      if (s.isLocationSet() && s.isDirectionSet())
        counter--;
    }
    return counter;
  }


  /**
   * Checks the grid and returns ALL possible moves in a List of Fields.
   *
   * @return List<Field> List of possible moves
   */
  public List<Field> getAllPossibleMoves() {
    List<Field> result = new ArrayList<>();

    for (int row = 0; row < oppGrid.numRows(); row++) {
      for (int col = 0; col < oppGrid.numCols(); col++) {
        if (oppGrid.getStatus(row, col) == Location.UNGUESSED) {
          Field possibleMove = new Field(row, col);
          result.add(possibleMove);
        }
      }
    }

    return result;
  }
}
