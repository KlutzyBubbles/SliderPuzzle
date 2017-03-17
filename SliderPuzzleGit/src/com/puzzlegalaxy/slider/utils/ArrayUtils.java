package com.puzzlegalaxy.slider.utils;

import java.util.Arrays;

import com.puzzlegalaxy.slider.Main;
import com.puzzlegalaxy.slider.exceptions.InvalidLevelException;

public class ArrayUtils {

	public static int[][] intArrFromString(String s) throws InvalidLevelException {
		s = s.replace(" ", "");
		Main.debug(s);
		if (s.equals("{[]}")) {
			return new int[0][0];
		}
		if (!s.contains("]") || !s.contains("[") || !s.contains(",")) {
			throw new InvalidLevelException("The specified string is not an int[] (Code: 2)");
		}
		String[] splitY = s.replace("{", "").replace("}", "").replace("]", "").split("\\[");
		int[][] val = new int[splitY.length][splitY[0].split(",").length];

		for (int i = 0; i < splitY.length; i++) {
			String[] splitX = splitY[i].split(",");
			if (splitX.length >= splitY[i].length()) {
				for (int ii = 0; ii < splitX.length; ii++) {
					Main.debugLoop("NUM: " + splitX[ii]);
					try {
						val[i][ii] = Integer.parseInt(splitX[ii]);
					} catch (NumberFormatException e) {
						throw new InvalidLevelException("There is an unknown variable in the int[] (Code: 3)");
					}
				}
			}
		}
		return val;
	}

	public static Object[][] objArrFromString(String s) throws InvalidLevelException {
		Main.debug("STRING TO OBJ[][]: " + s);
		if (!s.contains("}") || !s.contains("{") || !s.contains("]") || !s.contains("[") || !s.contains(",")) {
			throw new InvalidLevelException("The specified string is not an valid matrix (Code: 8)");
		}
		String[] splitY = s.replace("{", "").replace("}", "").replace("]", "").split("\\[");
		Main.debug("1: " + Arrays.toString(splitY));
		if (splitY[0] == null || splitY[0].equals("") || splitY[0].equals(" ")) {
			String[] temp = new String[splitY.length - 1];
			for (int i = 1; i < splitY.length; i++) {
				temp[i - 1] = splitY[i];
			}
			splitY = temp;
		}
		Main.debug("2: " + Arrays.toString(splitY));
		Object[][] val = new Object[splitY.length][splitY[0].split(",").length];
		for (int i = 0; i < splitY.length; i++) {
			String[] splitX = splitY[i].split(",");
			if (splitX.length >= val.length) {
				for (int ii = 0; ii < splitX.length; ii++) {
					Main.debug("[" + ii + "," + i + "]");
					if (i == 0 && ii == 0) {
						val[i][ii] = splitX[ii];
						continue;
					}
					Main.debug("NUM: " + splitX[ii]);
					try {
						Integer.parseInt(splitX[ii]);
					} catch (NumberFormatException e) {
						throw new InvalidLevelException("Only value [0,0] of the matrix can be an Object (Code: 9)");
					}
					val[i][ii] = splitX[ii];
				}
			}
		}
		return val;
	}

	public static String intArrToString(int[][] a) {
		StringBuilder b = new StringBuilder();
		String[] rows = new String[a.length];
		for (int i = 0; i < rows.length; i++) {
			b.append("[");
			for (int num : a[i]) {
				b.append(num + ",");
			}
			b.append("]");
			rows[i] = b.toString().replace(",]", "]");
			b.setLength(0);
		}
		b.append("[");
		for (String row : rows) {
			b.append(row);
		}
		b.append("]");
		return b.toString();
	}

	public static String objArrToString(Object[][] a) {
		StringBuilder b = new StringBuilder();
		String[] rows = new String[a.length];
		for (int i = 0; i < rows.length; i++) {
			b.append("[");
			for (Object o : a[i]) {
				b.append(String.valueOf(o) + ",");
			}
			b.append("]");
			rows[i] = b.toString().replace(",]", "]");
			b.setLength(0);
		}
		b.append("[");
		for (String row : rows) {
			b.append(row);
		}
		b.append("]");
		return b.toString();
	}

}
