package com.markl.game.util;

import java.util.Random;

/**
 * Various utility functions for coding convenience.
 *
 * Author: Mark Lucernas
 * Date: 2020-05-25
 */
public class Utils {

  /**
   * Generates random integer with exclusive of some numbers.
   * @param start starting number to generate random numbers from.
   * @param end end number to generate random numbers until.
   * @param exlude int or int array number/numbers to exclude from being
   * generated.
   * @return randomly generated int.
   *
   * ref: https://stackoverflow.com/a/6443346/11850077
   */
  public static int getRandomWithExclusion(int start, int end, int... exclude) {
    Random rnd = new Random();
    int random = start + rnd.nextInt(end - start + 1 - exclude.length);
    for (int ex : exclude) {
      if (random < ex) {
        break;
      }
      random++;
    }
    return random;
  }

  /**
   * Appends an integer number to an existing int array.
   * @param arr int array to append to.
   * @param num number to append to int array.
   * @return new int[] with arr and num appended to it.
   */
  public static int[] appendToIntArray(int[] arr, int num) {
    int[] newArr = new int[arr.length + 1];

    for (int i = 0; i < arr.length; i++) {
      newArr[i] = arr[i];
    }
    newArr[arr.length] = num;

    return newArr;
  }

  /**
   * Appends a string to an existing string array.
   * @param arr String arr to append to.
   * @param str String to append ti string array.
   * @return new String[] with arr and str appended to it.
   */
  public static String[] appendToStringArray(String[] arr, String str) {
    String[] newArr = new String[arr.length + 1];

    for (int i = 0; i < arr.length; i++) {
      newArr[i] = arr[i];
    }
    newArr[arr.length] = str;

    return newArr;
  }

  /**
   * Constructor method that ensures this Utils class cannot be instantiated.
   */
  private Utils() {
    throw new RuntimeException("You cannot instantiate Utils class");
  }

} // Utils
