package mcts;

import battleship.Player;
import battleship.Randomizer;

import java.util.ArrayList;
import java.util.List;

class Node {
  private Node parent; // null means root node

  // The move that was done by the algorithm
  private Field move;

  private Player opponent;
  private Player self;

  // Statistics
  private int plays = 0;
  private int wins = 0;

  private List<Node> children;

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

    this.children = new ArrayList<>();
  }

  public void playNextMove(Field move) {
    this.move = move;

    if (opponent.playerGrid.hasShip(move.row, move.col)) {
      self.oppGrid.markHit(move.row, move.col);
      opponent.playerGrid.markHit(move.row, move.col);
    } else {
      self.oppGrid.markMiss(move.row, move.col);
      opponent.playerGrid.markMiss(move.row, move.col);
    }

  }

  public Node randomChild() {
    if (children.isEmpty()) return null;

    return children.get(Randomizer.nextInt(0, children.size() - 1));
  }

  public void addChild(Node child) {
    this.children.add(child);
  }

  public List<Node> getChildren() {
    return this.children;
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
