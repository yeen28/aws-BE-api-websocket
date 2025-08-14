package com.nameless.social.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api")
public class IndexController {
	// 브라우저 직접 접근 /api → index.html 반환
	@GetMapping({"", "/"})
	public ModelAndView index() {
		return new ModelAndView("/index");
	}
}
