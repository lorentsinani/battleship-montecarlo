package mcts;

import battleship.Player;

import java.util.List;

public class ISMCTS {
  /**
   * Function for the computer to select a Field to shoot
   *
   * @param comp The computer
   * @param user The human player
   * @return The field to shoot
   */
  public static Field selectFieldToShoot(Player comp, Player user) {
    List<Determinization> determinizations = Determinization.createDeterminizations(comp, user, 50);

    ChanceMatrix resultsMatrix = new ChanceMatrix();

    int iterationCount = 600;

    for (Determinization d : determinizations) {
      MCTSAlgorithm algorithm = new MCTSAlgorithm(d.humanPlayer, d.computerPlayer, iterationCount);
      List<Node> selectedFields = algorithm.run();

      for (Node field : selectedFields) {
        resultsMatrix.incrementRowCol(field.getMove(), field.getWinChance());
      }
    }

    resultsMatrix.print();
    resultsMatrix.divide(iterationCount);
    return resultsMatrix.bestField();
  }
}
