package com.markl.game.util;

import java.util.Random;

/**
 * Various utility functions for coding convenience.
 *
 * Author: Mark Lucernas
 * Date: 2020-05-25
 */
public class Utils {

	public static void main(String[] args) {
		System.out.println(getRandomInt(0, 5));
	}

	private static Random rand = new Random();

	/**
	 * Generates random integer.
	 * @param min starting number to generate random numbers from.
	 * @param max end number to generate random numbers until.
	 * @return randomly generated int.
	 */
	public static int getRandomInt(int min, int max) {
		int random = rand.nextInt((max - min) + 1) + min;
		return random;
	}

	/**
	 * Generates random integer exclusive of some numbers.
	 * @param min Min number to generate random numbers from.
	 * @param max Max number to generate random numbers until.
	 * @param exlude Int or int array number/numbers to exclude from being
	 * generated.
	 * @return randomly generated int.
	 *
	 * ref: https://stackoverflow.com/a/6443346/11850077
	 */
	public static int getRandomIntWithExclusion(int min, int max, int... exclude) {
		int random = min + rand.nextInt(max - min + 1 - exclude.length);
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
