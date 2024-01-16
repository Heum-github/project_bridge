package com.smhrd.bridge.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smhrd.bridge.entity.SongList;
import com.smhrd.bridge.entity.UserInfo;
import com.smhrd.bridge.entity.UserLikeList;
import com.smhrd.bridge.service.PreferenceService;

@Controller
public class PreferenceController {

	@Autowired
	PreferenceService service;

	@GetMapping("/preference")
	public String preference(HttpSession session, Model model, @RequestParam(defaultValue = "1") int page) {
		UserInfo member = (UserInfo) session.getAttribute("loginUser");

		if (member != null) {
			int pageSize = 30;

			// 첫 번째 페이지에는 TOP30 노래를 가져옴
			List<SongList> top30List = service.songRanking();

			// 이어서 나머지 노래를 가져옴
			List<SongList> remainingList = service.allSong();

			// 전체 노래 리스트를 합침
			List<SongList> list = new ArrayList<>();
			list.addAll(top30List);
			list.addAll(remainingList);

			int totalSongList = list.size();
			int totalPages = (int) Math.ceil((double) totalSongList / pageSize);

			int startIndex = (page - 1) * pageSize;
			int endIndex = Math.min(startIndex + pageSize, totalSongList);

			List<SongList> songList = list.subList(startIndex, endIndex);

			model.addAttribute("songList", songList);
			model.addAttribute("page", page);
			model.addAttribute("totalPages", totalPages);
		}
		return "preference";
	}

	@PostMapping("/preference/submit")
	public String songList(@RequestParam(name = "song_num", required = false) Integer[] song_nums,
			@RequestParam(name = "rating", required = false) Integer[] ratings, HttpSession session,
			HttpServletRequest request) {
		UserInfo member = (UserInfo) session.getAttribute("loginUser");
		
		List<UserLikeList> userLikeList = new ArrayList<>();
		if (song_nums != null) {
			for (int i = 0; i < song_nums.length; i++) {
				UserLikeList userLike = new UserLikeList();
	            userLike.setSong_num(song_nums[i]);
	            userLike.setUser_id(member.getUser_id());
	            userLike.setRating(ratings[i]);
	            userLikeList.add(userLike);
			}
			int result = service.songList(userLikeList);

			if (result > 0) {
				System.out.println("저장 성공");
				return "redirect:/matchingpage";
			} else {
				System.out.println("저장 실패");
			}
		}
		return "redirect:/matchingPage";
	}

	/*
	 * @GetMapping("/preference/getSavedSongs")
	 * 
	 * @ResponseBody public List<UserLikeList> getSavedSongs(HttpSession session) {
	 * UserInfo member = (UserInfo) session.getAttribute("loginUser");
	 * 
	 * if (member != null) { List<UserLikeList> savedSongs =
	 * service.getSavedSongs(member.getUser_id()); return savedSongs; } else {
	 * return Collections.emptyList(); } }
	 */

}
