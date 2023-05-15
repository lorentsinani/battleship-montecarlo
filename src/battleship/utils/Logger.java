package utils;

import java.util.Map;

public class Logger {
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
  private static Map<String, String> env = System.getenv();

  public static void debug(String message) {
    String debug = env.getOrDefault("DEBUG", "off");

    if (debug.equals("on")) {
      System.out.println(ANSI_BLACK_BACKGROUND + ANSI_RED + "DEBUG: " + "\t\t" + message + "\t\t" + ANSI_RESET);
    }
  }
}