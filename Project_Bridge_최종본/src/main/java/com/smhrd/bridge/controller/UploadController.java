package com.smhrd.bridge.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smhrd.bridge.entity.UserInfo;
import com.smhrd.bridge.entity.UserSong;
import com.smhrd.bridge.service.UploadService;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

@Controller
public class UploadController {
	
	@Autowired
	UploadService service;
	
	@PostMapping("/uploadmusic/save")
	public String uploadMusic(UserSong usersong, @RequestPart("music") MultipartFile music) throws JsonProcessingException {
		String newFileName = UUID.randomUUID().toString() + music.getOriginalFilename();
		try {
			music.transferTo(new File(newFileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		usersong.setSong_file(newFileName);
		service.uploadMusic(usersong);
		return "redirect:/mypage";
	}

	@GetMapping("/mypage") 
	public String mypage(HttpSession session, Model model, @RequestParam(defaultValue = "1") int page) throws IOException {
		UserInfo member = (UserInfo) session.getAttribute("loginUser");
		if (member != null) {
			int pageSize = 3; // 한 페이지에 표시할 항목 수
			List<UserSong> songs = service.mypage(member);
			int totalRecipes = songs.size();
			int totalPages = (int) Math.ceil((double) totalRecipes / pageSize);
			
			int startIndex = (page - 1) * pageSize;
	        int endIndex = Math.min(startIndex + pageSize, totalRecipes);
	        
	        List<UserSong> musicToDisplay = songs.subList(startIndex, endIndex);

	        model.addAttribute("musics", musicToDisplay);
	        model.addAttribute("page", page);
	        model.addAttribute("totalPages", totalPages);        			
		}
		return "mypage";
	}
}