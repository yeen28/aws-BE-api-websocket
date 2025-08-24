package com.nameless.social.api.utils;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrUtilsTest {
	@Test
	void parseTagsTest() {
		// given
		List<String> successTags = List.of(
				"[\"tag1\", \"tag2\"]"
		);
		List<List<String>> expected = List.of(
				List.of("tag1", "tag2")
		);

		for (int i = 0; i < successTags.size(); i++) {
			// when
			List<String> result = StrUtils.parseTags(successTags.get(i));

			// then
			assertEquals(expected.get(i), result);
		}
	}

	@Test
	void parseTagsFailTest() {
		// given
		List<String> failTags = List.of(
				"",
				"abcd",
				"[\\\"tag1\\\", \\\"tag2\\\"]",
				"{\\\"tag1\\\", \\\"tag2\\\"}",
				"{\"tag1\", \"tag2\"}",
				"{'tag1', 'tag2'}"
		);

		failTags.forEach(failTag -> {
			// when
			List<String> result = StrUtils.parseTags(failTag);

			// then
			assertEquals(List.of(), result);
		});
	}
}