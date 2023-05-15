package battleship;

public class Location {
  // Global Vars
  public static final int UNGUESSED = 0;
  public static final int HIT = 1;
  public static final int MISSED = 2;

  // Instance Variables
  private boolean hasShip;
  private int status;
  private int lengthOfShip;
  private int directionOfShip;

  public Location() {
    status = 0;
    hasShip = false;
    lengthOfShip = -1;
    directionOfShip = -1;
  }

  public Location deepClone() {
    Location result = new Location();

    result.status = this.status;
    result.hasShip = this.hasShip;
    result.lengthOfShip = this.lengthOfShip;
    result.directionOfShip = this.directionOfShip;
    return result;
  }

  // Was this Location a hit?
  public boolean checkHit() {
    return status == HIT;
  }

  // Was this location a miss?
  public boolean checkMiss() {
    return status == MISSED;
  }

  // Was this location unguessed?
  public boolean isUnguessed() {
    return status == UNGUESSED;
  }

  // Mark this location a hit.
  public void markHit() {
    setStatus(HIT);
  }

  // Mark this location a miss.
  public void markMiss() {
    setStatus(MISSED);
  }

  // Return regardless if this location has a ship.
  public boolean hasShip() {
    return hasShip;
  }

  // Set the value of whether this location has a ship.
  public void setShip(boolean val) {
    this.hasShip = val;
  }

  // Get the status of this Location.
  public int getStatus() {
    return status;
  }

  // Set the status of this Location.
  public void setStatus(int status) {
    this.status = status;
  }

  public int getLengthOfShip() {
    return lengthOfShip;
  }

  public void setLengthOfShip(int val) {
    lengthOfShip = val;
  }

  public void setDirectionOfShip(int val) {
    directionOfShip = val;
  }
}
