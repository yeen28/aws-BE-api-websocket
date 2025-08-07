package com.nameless.social.api.controller;

import com.nameless.social.api.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {
	private final ClubService clubService;
}
