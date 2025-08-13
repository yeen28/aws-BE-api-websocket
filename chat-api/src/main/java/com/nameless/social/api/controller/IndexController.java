package com.nameless.social.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

// TODO FE와 연동 후 제거
@Controller
public class IndexController {
	@GetMapping("/api")
	public ModelAndView index() {
		return new ModelAndView("/index");
	}
}
