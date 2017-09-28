package com.pan.commlib.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The helper to facilitate the output debug info from logcat.
 */
public class DataConvertUtils {
	final private static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	/**
	 * * Convert bytes to hex string.
	 *
	 * @param bytes the bytes to be converted.
	 * @return the string result.
	 */
	public static String convertBytesToHex(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return "";
		}
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Convert map to string.
	 * <p>
	 * This method will call the toString() method of K and V.
	 *
	 * @param map the map to be output.
	 * @return the string result.
	 */
	public static String convertMaptoString(Map map) {
		if (map == null) {
			return "";
		}
		String str = "[";
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			str += " " + pair.getKey() + ":" + (pair.getValue() == null ? "" : pair.getValue());
		}
		return str + " ]";
	}

	/**
	 * Convert list to string.
	 * <p>
	 * This method will call the toString() method of the item of the list.
	 *
	 * @param list the list to be output.
	 * @return the string result.
	 */
	public static String convertListToString(List list) {
		if (list == null) {
			return "";
		}
		Iterator it = list.iterator();
		String str = "";
		while (it.hasNext()) {
			Object obj = it.next();
			str += (obj == null ? "" : obj);
			if (it.hasNext()) {
				str += " ,";
			}
		}
		return str;
	}
}
