package com.puzzlegalaxy.slider;

import java.util.Arrays;
import java.util.UUID;

public class Main {

	public static boolean debug = true;
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID());
	}
	
	public static void debug(String msg) {
		if (debug) {
			System.out.println(msg);
		}
	}
	
}
