package com.smhrd.bridge.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ChatController {
	
	@RequestMapping("/mychat")
	public ModelAndView chat() {
		ModelAndView mv = new ModelAndView();		
		mv.setViewName("chat");
		return mv;
	}

}