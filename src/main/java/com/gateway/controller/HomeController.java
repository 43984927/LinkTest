package com.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "redirect:/api/v1/home";
	}

	@GetMapping("/speedtest")
	public String speedtest() {
		return "redirect:/api/v1/speedtest";
	}
}
