package mcts;

import battleship.Player;
import battleship.Randomizer;
import utils.Logger;

import java.util.List;

class MCTSAlgorithm {
  private Node root;
  private int maxIterations;

  MCTSAlgorithm(Player self, Player opponent, int iterations) {
    this.root = new Node(null, self, opponent);
    this.maxIterations = iterations;
  }

  /**
   * UCT calculation.
   *
   * @param totalPlaysParent  n
   * @param totalWinsCurrent  w_i
   * @param totalPlaysCurrent n_i
   * @return UCT value
   * <p>
   * (w_i / n_i) + sqrt((2 * ln( n )) / n_i)
   * where
   * w_i     total wins at current node
   * n_i     total plays at current node
   * n       total plays at parent node
   */
  private static double uctValue(int totalPlaysParent, int totalWinsCurrent, int totalPlaysCurrent) {
    if (totalPlaysCurrent == 0) {
      return Double.MAX_VALUE;
    }

    return ((double) totalWinsCurrent / (double) totalPlaysCurrent)
        + Math.sqrt(2 * Math.log(totalPlaysParent) / (double) totalPlaysCurrent);
  }

  /**
   * Run the algorithm.
   *
   * @return Field(row, col) of the suggested next move.
   */
  List<Node> run() {
    int currentIteration = 1;

    Logger.debug("Starting MCTS");

    while (currentIteration <= maxIterations) {
      Logger.debug("MCTS Iteration: " + currentIteration);

      Node currentNode = selectNode(this.root); // Step 1: Select a child node to expand
      expandNode(currentNode); // Step 2: Expand node
      Node simulateNode = currentNode.randomChild();
      if (simulateNode == null || simulateNode.getSelf().getAllPossibleMoves().isEmpty()) break;

      boolean win = simulateGamePlayForNode(simulateNode); // Step 3: Simulate game play (playout)
      backPropagate(simulateNode, win); // Step 4: Back-propagate, update parents

      currentIteration++;
    }

    return root.getChildren();
  }

  private Node selectNode(Node node) {
    if (node.getChildren().isEmpty()) {
      return node;
    }

    Node bestNode = node;

    // Hold the best UCT Value
    double bestUct = 0;
    // For each child of the root node, calculate UCT value
    // and select node with max UCT value
    for (Node c : node.getChildren()) {
      double uct = uctValue(c.getParent().getPlays(), c.getWins(), c.getPlays());

      if (uct == Double.MAX_VALUE) return c;

      if (bestNode == node || uct > bestUct) {
        bestNode = c;
        bestUct = uct;
      }
    }

    return selectNode(bestNode);
  }

  private void expandNode(Node parent) {
    List<Field> possibleMoves = parent.getOpponent().getAllPossibleMoves();

    // For each possible move, play out and add the playout
    // resulting node to parents children nodes
    for (Field move : possibleMoves) {
      Node child = new Node(parent, parent.getOpponent(), parent.getSelf());
      child.playNextMove(move);
      parent.addChild(child);
    }
  }

  /**
   * Update the statistics up the tree.
   *
   * @param node
   * @param win
   */
  private void backPropagate(Node node, boolean win) {
    if (win)
      node.incrementWins();
    node.incrementPlays();

    Node parent = node.getParent();

    // Recursively call backPropagate, with inverted win flag
    // because the parent of the current node has won if the child has lost and vice versa
    if (parent != null)
      backPropagate(parent, !win);
  }

  /**
   * Takes a Node and simulates game play.
   *
   * @param node The node on which to simulate game play.
   * @return true if WIN, false else
   */
  private boolean simulateGamePlayForNode(Node node) {
    Player winner = node.getSelf().deepClone();
    Player loser = node.getOpponent().deepClone();

    Logger.debug("Simulation beginnt!");

    return !simulateGamePlayForNode(loser.deepClone(), winner.deepClone());
  }

  /**
   * Takes a Node and simulates game play.
   *
   * @param winner
   * @param loser
   * @return true if WIN, false else
   */
  private boolean simulateGamePlayForNode(Player winner, Player loser) {
    List<Field> possibleMoves = winner.getAllPossibleMoves();
    Field fieldToShoot = possibleMoves.get(Randomizer.nextInt(0, winner.getAllPossibleMoves().size() - 1));

    if (loser.playerGrid.hasShip(fieldToShoot.row, fieldToShoot.col)) {
      loser.playerGrid.markHit(fieldToShoot.row, fieldToShoot.col);
      winner.oppGrid.markHit(fieldToShoot.row, fieldToShoot.col);
    } else {
      loser.playerGrid.markMiss(fieldToShoot.row, fieldToShoot.col);
      winner.oppGrid.markMiss(fieldToShoot.row, fieldToShoot.col);
    }

    if (loser.playerGrid.hasLost()) {
      return true;
    }

    return !simulateGamePlayForNode(loser, winner);
  }
}