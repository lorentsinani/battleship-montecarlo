package mcts;

import battleship.*;

import java.util.ArrayList;
import java.util.List;

class Determinization {
  Player humanPlayer;
  Player computerPlayer;

  public Determinization() {
  }

  private static boolean validDeterminization(Grid informationSet, Grid possibleGrid) {
    for (int x = 0; x < Grid.GRID_DIMENSION; x++) {
      for (int y = 0; y < Grid.GRID_DIMENSION; y++) {
        if (informationSet.getStatus(x, y) == Location.HIT) {
          if (!possibleGrid.hasShip(x, y)) {
            return false;
          }
        } else if (informationSet.getStatus(x, y) == Location.MISSED) {
          if (possibleGrid.hasShip(x, y)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  private static void reproduceShots(Grid informationSet, Grid possibleGrid) {
    for (int x = 0; x < Grid.GRID_DIMENSION; x++) {
      for (int y = 0; y < Grid.GRID_DIMENSION; y++) {
        if (informationSet.getStatus(x, y) == Location.HIT) {
          possibleGrid.markHit(x, y);
        } else if (informationSet.getStatus(x, y) == Location.MISSED) {
          possibleGrid.markMiss(x, y);
        }
      }
    }
  }

  private static Grid makeRandomDeterminization(Grid informationSet) {
    Grid result = new Grid();

    reproduceShots(informationSet, result);

    List<Integer> remainingShipLengths = new ArrayList<>();

    for (int i = 0; i < Player.getNumOfShips(); i++) {
      remainingShipLengths.add(Player.SHIP_LENGTHS[i]);
    }

    for (int x = 0; x < Grid.GRID_DIMENSION; x++) {
      for (int y = 0; y < Grid.GRID_DIMENSION; y++) {
        if (informationSet.getStatus(x, y) == Location.HIT && !result.hasShip(x, y)) {
          if (remainingShipLengths.size() == 0) {
            return null;
          }
          int shipLength = remainingShipLengths.remove(Randomizer.nextInt(0, remainingShipLengths.size() - 1));

          Ship newShip = new Ship(shipLength);

          int dir = Randomizer.nextInt(0, 1);
          if (Battleship.hasErrorsComp(x, y, dir, result, shipLength)) {
            dir = Randomizer.nextInt(0, 1); //0 = HORIZONTAL
          }
          if (Battleship.hasErrorsComp(x, y, dir, result, shipLength)) {
            return null;
          }

          newShip.setLocation(x, y);
          newShip.setDirection(dir);
          result.addShip(newShip);
        }
      }
    }

    for (Integer remainingShipLength : remainingShipLengths) {
      Ship newShip = new Ship(remainingShipLength);

      int row = Randomizer.nextInt(0, Grid.GRID_DIMENSION - 1);
      int col = Randomizer.nextInt(0, Grid.GRID_DIMENSION - 1);
      int dir = Randomizer.nextInt(0, 1);
      while (Battleship.hasErrorsComp(row, col, dir, result, remainingShipLength)) {// while the random numbers make errors, start again

        row = Randomizer.nextInt(0, Grid.GRID_DIMENSION - 1);
        col = Randomizer.nextInt(0, Grid.GRID_DIMENSION - 1);
        dir = Randomizer.nextInt(0, 1);
      }

      newShip.setLocation(row, col);
      newShip.setDirection(dir);
      result.addShip(newShip);
    }

    return result;
  }


  private static Grid constructPossibleGrid(Grid informationSet) {
    Grid possibleGrid = null;
    while (possibleGrid == null || !validDeterminization(informationSet, possibleGrid)) {
      possibleGrid = makeRandomDeterminization(informationSet);
    }
    return possibleGrid;
  }

  private static Determinization createDeterminization(Player comp, Player user) {
    Determinization determinization = new Determinization();

    determinization.humanPlayer = user;
    determinization.computerPlayer = comp;

    // The shots that are marked in the respective opp grid
    Grid compFiredShots = comp.oppGrid;

    determinization.humanPlayer.playerGrid = constructPossibleGrid(compFiredShots);
    return determinization;
  }

  static List<Determinization> createDeterminizations(Player comp, Player user, int count) {
    long maxTime = 8000l;

    long currentTime = System.currentTimeMillis();
    long endTime = currentTime + maxTime;

    List<Determinization> determinizations = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      // Create a det. for player (opponent)
      determinizations.add(createDeterminization(comp.deepClone(), user.deepClone()));

      currentTime = System.currentTimeMillis();
      if (currentTime > endTime) {
        break;
      }
    }

    return determinizations;
  }

}
