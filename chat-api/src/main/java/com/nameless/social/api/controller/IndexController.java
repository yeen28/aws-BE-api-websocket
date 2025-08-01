package com.nameless.social.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	@GetMapping("/index")
	public ModelAndView index() {
		return new ModelAndView("/index");
	}
}
