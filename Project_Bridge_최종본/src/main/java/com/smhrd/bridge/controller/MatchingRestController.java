package com.smhrd.bridge.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.bridge.converter.ImageConverter;
import com.smhrd.bridge.converter.ImageToBase64;
import com.smhrd.bridge.entity.UserInfo;
import com.smhrd.bridge.entity.UserSong;
import com.smhrd.bridge.service.MatchingService;

@RestController
public class MatchingRestController{
	 @Autowired MatchingService service;
	 
	 @GetMapping("matchingPage/matchingProfile") 
	 public List<UserSong> matchingProfile(String user_id) throws IOException { 
		 List<UserSong> songs = service.matchingProfile(user_id);
		 for(int i = 0; i < songs.size(); i++) {
				UserSong contents = songs.get(i);
				File file = new File("c:\\uploadMusic\\" + contents.getSong_file());
				ImageConverter<File, String> converter= new ImageToBase64();
				String fileStringValue = converter.convert(file);
				songs.get(i).setSong_file(fileStringValue);
			}		 
		 return	songs;
	 }
	 
	 @GetMapping("matchingPage/recommender") 
	 public List<UserSong> matchingRecommender(String user_id) throws IOException { 
		 List<UserSong> songs = service.matchingRecommender(user_id);
		 for(int i = 0; i < songs.size(); i++) {
				UserSong contents = songs.get(i);
				File file = new File("c:\\uploadMusic\\" + contents.getSong_file());
				ImageConverter<File, String> converter= new ImageToBase64();
				String fileStringValue = converter.convert(file);
				songs.get(i).setSong_file(fileStringValue);
			}		 
		 return	songs;
	 }
	 
	 @GetMapping("matchingPage/searchProfile")
	 public List<UserInfo> searchProfile(String user){
		 return service.searchProfile(user);
	 }
}
