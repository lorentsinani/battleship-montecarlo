package mcts;

import battleship.Grid;
import utils.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class representing a matrix of chances
 * In this project it is used to represent the chances of a ship being on each of the matrix fields
 */
class ChanceMatrix {
  static int iteration = 1;
  private float[][] grid = new float[Grid.GRID_DIMENSION][Grid.GRID_DIMENSION];

  /***
   * Analyzes this ChanceMatrix and returns the Field with the highest win chance
   * @return The Field with the highest win chance
   */
  Field bestField() {
    Field highestField = new Field(0, 0);

    for (int x = 0; x < Grid.GRID_DIMENSION; x++) {
      for (int y = 0; y < Grid.GRID_DIMENSION; y++) {
        if (grid[x][y] > grid[highestField.row][highestField.col]) {
          highestField = new Field(x, y);
        }
      }
    }

    return highestField;
  }

  public void divide(int divisor) {
    for (int x = 0; x < Grid.GRID_DIMENSION; x++) {
      for (int y = 0; y < Grid.GRID_DIMENSION; y++) {
        grid[x][y] /= divisor;
      }
    }
  }

  void incrementRowCol(Field field, float value) {
    grid[field.row][field.col] += value;
  }

  public void printAndWriteToImage() {
    BufferedImage image = new BufferedImage(Grid.GRID_DIMENSION, Grid.GRID_DIMENSION, BufferedImage.TYPE_BYTE_GRAY);
    System.out.println("Iteration : " + iteration);
    for (int x = 0; x < Grid.GRID_DIMENSION; x++) {
      for (int y = 0; y < Grid.GRID_DIMENSION; y++) {
        float cellValue = grid[x][y];
        int colorValue = convertRange(cellValue, 0f, 50f, 255, 0);
        System.out.print(cellValue);
        System.out.print(" ("+colorValue+")\t");
        Color color = new Color(255, colorValue, colorValue);
        image.setRGB(y, x, color.getRGB());
      }
      System.out.println();
    }
    try {
      File output = new File(String.format("image_%d.jpg", iteration));
      ImageIO.write(image, "jpg", output);
    }
    catch (IOException exception){
      Logger.debug("IOException at printAndWriteToImage()");
    }
    iteration++;
  }

  public static int convertRange(float value, float minInput, float maxInput, int minOutput, int maxOutput) {
    // Ensure value is within the input range
    value = Math.max(minInput, Math.min(maxInput, value));

    // Calculate the proportionate value in the output range
    float proportion = (value - minInput) / (maxInput - minInput);

    return (int) (proportion * (maxOutput - minOutput) + minOutput);
  }

}
