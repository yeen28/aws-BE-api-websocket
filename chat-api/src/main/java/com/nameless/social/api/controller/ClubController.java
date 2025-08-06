package com.nameless.social.api.controller;

import com.nameless.social.api.dto.ClubDto;
import com.nameless.social.api.model.ClubModel;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {
	private final ClubService clubService;

	@PostMapping
	public CommonResponse<ClubModel> createClub(@RequestBody final ClubDto clubDto) {
		return CommonResponse.success(clubService.createClub(clubDto));
	}

	@GetMapping("/{id}")
	public CommonResponse<ClubModel> getClubById(@PathVariable Long id) {
		return CommonResponse.success(clubService.findById(id));
	}

	@GetMapping
	public CommonResponse<List<ClubModel>> getAllClub() {
		return CommonResponse.success(clubService.findAll());
	}
}
