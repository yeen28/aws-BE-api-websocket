package com.nameless.social.websocket.utils;

public class MediaUtils {
	/**
	 * 이미지의 경우: data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAYAAAC…
	 * @param str
	 * @return
	 */
	public static String getType(final String str) {
		if (!str.contains(";") | !str.contains("/")) {
			return "";
		}

		String rawType = str.split(";")[0];
		return rawType.substring(rawType.indexOf(":") + 1);
	}
}
