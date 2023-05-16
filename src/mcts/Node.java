package mcts;

import battleship.Player;

class Node {
  private Node parent; // null means root node

  // The move that was done by the algorithm
  private Field move;

  private Player opponent;
  private Player self;

  // Statistics
  private int plays = 0;
  private int wins = 0;

  /**
   * Initialize a Node.
   *
   * @param opponent The human (opponent) player
   * @param self     The computer player
   */
  Node(Node parent, Player self, Player opponent) {
    this.parent = parent;
    this.self = self.deepClone();
    this.opponent = opponent.deepClone();

  }

  public void incrementPlays() {
    this.plays++;
  }

  public void incrementWins() {
    this.wins++;
  }

  public int getPlays() {
    return plays;
  }

  public int getWins() {
    return wins;
  }

  public float getWinChance() {
    return ((float) this.wins) / (((float) this.plays));
  }

  public Player getOpponent() {
    return opponent;
  }

  public Player getSelf() {
    return self;
  }

  public Field getMove() {
    return move;
  }

  public Node getParent() {
    return parent;
  }
}