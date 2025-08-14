package com.nameless.social.websocket.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MediaUtilsTest {
	@Test
	void getTypeTest() {
		// given
		List<String> actualStrings = List.of(
				"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAYAAAC",
				"data:image/jpg;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAYAAAC"
		);
		List<String> expectedStrings = List.of(
				"image/png",
				"image/jpg"
		);

		for (int i = 0; i < actualStrings.size(); i++) {
			// when
			String result = MediaUtils.getType(actualStrings.get(i));

			// then
			assertEquals(expectedStrings.get(i), result);
		}
	}
}