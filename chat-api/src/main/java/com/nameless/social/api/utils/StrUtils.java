package com.nameless.social.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nameless.social.api.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Slf4j
public class StrUtils {
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static List<String> parseTags(final String stringTags) {
		if (StringUtils.isEmpty(stringTags)) {
			return List.of();
		}

		try {
			return OBJECT_MAPPER.readValue(stringTags, new TypeReference<List<String>>() {});
		} catch (JsonProcessingException e) {
			log.warn("Json 파싱 오류 - {} {} {}", stringTags, ErrorCode.JSON_PROCESSING_EXCEPTION.getCode(), ErrorCode.JSON_PROCESSING_EXCEPTION.getMessage());
			return List.of();
		}
	}
}
