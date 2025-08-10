package com.nameless.social.api.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrUtilsTest {
	@Test
	void parseTagsTest() {
		// given
		String tags = "[\"tag1\", \"tag2\"]";

		// when
		List<String> result = StrUtils.parseTags(tags);

		// then
		assertEquals(List.of("tag1", "tag2"), result);
	}

	@Test
	void parseTagsFailTest() {
		// given
		List<String> failTags = List.of(
				"",
				"abcd"
		);

		// when
		failTags.forEach(failTag -> {
			List<String> result = StrUtils.parseTags(failTag);

			// then
			assertEquals(List.of(), result);
		});
	}
}