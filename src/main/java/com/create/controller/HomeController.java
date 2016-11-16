package com.create.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	private static final String HOME_VIEW = "home";

	@GetMapping(value = "/")
	public String home() {
		return HOME_VIEW;
	}
}
