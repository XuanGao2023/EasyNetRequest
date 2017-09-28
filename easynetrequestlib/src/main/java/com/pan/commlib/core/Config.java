package com.pan.commlib.core;


public class Config {
	public static boolean TESTING = false;

	public static void ensureTesting() {
		if (!Config.TESTING) throw new IllegalAccessError("only valid during testing!");
	}
}
