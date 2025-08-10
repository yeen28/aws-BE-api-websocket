package com.nameless.social.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class StrUtils {
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static List<String> parseTags(String stringTags) {
		if (StringUtils.isEmpty(stringTags)) {
			return List.of();
		}

		try {
			return OBJECT_MAPPER.readValue(stringTags, new TypeReference<List<String>>() {});
		} catch (JsonProcessingException e) {
			throw new CustomException(ErrorCode.JSON_PROCESSING_EXCEPTION);
		}
	}
}
