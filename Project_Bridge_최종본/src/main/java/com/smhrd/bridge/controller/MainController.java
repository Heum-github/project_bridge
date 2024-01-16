package com.smhrd.bridge.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.smhrd.bridge.entity.UserInfo;

@Controller
public class MainController {
	
	@GetMapping("/")
	public String main(HttpSession session) {
		UserInfo user = (UserInfo) session.getAttribute("loginUser");
		if (user == null) {
			return "login";
		} else { 
			return "main";
		}
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/main")
	public String main() {
		return "main";
	}
	
	@GetMapping("/uploadmusic")
	public String uploadmusic() {
		return "uploadmusic";
	}
}
