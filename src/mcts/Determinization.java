package mcts;

import battleship.*;

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
}
